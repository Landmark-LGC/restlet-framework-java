package org.restlet.ext.odata.internal;

import java.io.IOException;
import java.util.List;

import org.restlet.Context;
import org.restlet.ext.atom.Feed;
import org.restlet.ext.odata.internal.edm.FunctionImport;
import org.restlet.ext.odata.internal.edm.Metadata;
import org.restlet.ext.odata.internal.edm.TypeUtils;
import org.restlet.ext.odata.json.JsonFormatParser;
import org.restlet.ext.odata.json.JsonStreamReaderFactory;
import org.restlet.ext.odata.json.JsonStreamReaderFactory.JsonStreamReader;
import org.restlet.ext.odata.json.JsonStreamReaderFactory.JsonStreamReader.JsonEvent;
import org.restlet.representation.Representation;

import com.google.gson.JsonSyntaxException;

/**
 * This class is added for parsing the representation result provided by RESLET
 * as a response in the expected object/return type for the functions/actions.
 * 
 * @author Shantanu
 * @param <T>
 */
public class JsonContentFunctionHandler<T> extends JsonFormatParser<T>
		implements FunctionContentHandler {
	
	private Representation representation;
	private FunctionImport function;
	private T entity;
	
	public JsonContentFunctionHandler(Class<?> entityClass, Representation representation,
			FunctionImport function, Metadata metadata, T entity) {
		this.entityClass = entityClass;
		this.metadata = metadata;
		this.function = function;
		this.representation = representation;
		this.entity = entity;
	}

	@Override
	public Object parseResult() {
		Object value = null;
		try {
			JsonStreamReader jsr = JsonStreamReaderFactory
					.createJsonStreamReader(this.representation.getReader());
			boolean hasResultsProp = false;
			try {
				// {
				JsonEvent event = null;
				this.ensureStartObject(jsr.nextEvent());

				// "d" :
				this.ensureStartProperty(jsr.nextEvent(), DATA_PROPERTY);

				// {
				this.ensureStartObject(jsr.nextEvent());
				// results only for collections, if it is single entity or
				// property it won't be there
				// "results" :
				event = jsr.nextEvent();
				// if it is start property, check if its results/__metada and
				// then skip them
				if (event.isStartProperty()) {
					if (event.asStartProperty().getName()
							.equals((RESULTS_PROPERTY))) {
						hasResultsProp = true;
						// skip [
						event = jsr.nextEvent();
					}
				}
	            String edmReturnType = this.function.getReturnType();
				// create instance of entity, parse it and add it to list of
				// entities
				if (event.isStartArray()) { // collection later
					this.parseCollection(jsr);
					value = this.entity;
					// ] already processed by parseFeed
				} else { // simple
					if(TypeUtils.isEdmSimpleType(edmReturnType)){ // Return type startswith EDM : Simple
						event = jsr.nextEvent();
						if(event.isEndProperty()){
							value = TypeUtils.fromEdm(event.asEndProperty()
									.getValue(), edmReturnType);
						}
						this.ensureEndObject(jsr.nextEvent());
					} else { // complex
						do {
							this.addProperty(this.entity, event
									.asStartProperty().getName(), jsr);
							event = jsr.nextEvent();
						} while (!event.isEndObject());
						value = this.entity;
					}
				}

				if (hasResultsProp) {
					// EndProperty of "results" :
					this.ensureEndProperty(jsr.nextEvent());
				}

				event = jsr.nextEvent();

				if (hasResultsProp) {
					// EndObject and EndProperty of "result" :
					this.ensureEndObject(event);
					this.ensureEndProperty(jsr.nextEvent());
				}

				this.ensureEndObject(jsr.nextEvent());

				if (jsr.hasNext())
					throw new IllegalArgumentException("garbage after the feed");

			} catch (Exception e) {
				Context.getCurrentLogger().warning(
						"Cannot parse the json content due to Json Syntax Exception: "
								+ e.getMessage());
			} finally {
				jsr.close();
			}
		} catch (IOException e) {
			Context.getCurrentLogger().warning(
					"Cannot parse the json content due to IO Exception: "
							+ e.getMessage());
		} catch (JsonSyntaxException e) {
			Context.getCurrentLogger().warning(
					"Cannot parse the json content due to Json Syntax Exception: "
							+ e.getMessage());
		}
		return value;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void parseCollection(JsonStreamReader jsr) {
		try {
			boolean isPrimitiveCollection = TypeUtils.isEdmSimpleType(TypeUtils.getCollectionType(this.function.getReturnType()));
			if (isPrimitiveCollection) { // simple type
				String edmType = TypeUtils.toEdmType(this.entityClass.getName());
				// just add value to list
				while (jsr.hasNext()) {
					JsonEvent event2 = jsr.nextEvent();
					if (event2.isEndArray()) {
						break;
					} 
					Object value = TypeUtils
							.fromEdm(event2.asValue().getValue(),edmType);
					((List) this.entity).add(value);
				}
			} else { // complex type
				while (jsr.hasNext()) {
					JsonEvent event2 = jsr.nextEvent();
					if (event2.isStartObject()) {
						Object obj = this.entityClass.newInstance();
						// populate the object
						this.parseEntry(jsr, (T) obj);
						((List) this.entity).add(obj);
					} else if (event2.isEndArray()) {
						break;
					}
				}
			}
		} catch (InstantiationException e) {
			Context.getCurrentLogger().warning(
					"Cannot parse the feed due to: " + e.getMessage());
		} catch (IllegalAccessException e) {
			Context.getCurrentLogger().warning(
					"Cannot parse the feed due to: " + e.getMessage());
		}
	}

	@Override
	public Feed getFeed() {
		if (this.feed == null) {
			this.feed = new Feed();
		}
		return this.feed;
	}

}
