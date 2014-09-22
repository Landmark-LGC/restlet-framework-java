package org.restlet.ext.odata.internal;

import java.io.IOException;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.restlet.Context;
import org.restlet.ext.odata.internal.edm.FunctionImport;
import org.restlet.ext.odata.internal.edm.Metadata;
import org.restlet.ext.odata.internal.edm.TypeUtils;
import org.restlet.ext.odata.xml.AtomFeedHandler;
import org.restlet.ext.xml.format.XmlFormatParser;
import org.restlet.representation.Representation;

/**
 * This class is added for parsing the representation result provided by RESLET as a response in the 
 * respective object/ return type for the functions/actions.
 * 
 * @author Akshay
 */
public class AtomContentFunctionHandler<T> extends XmlFormatParser implements FunctionContentHandler {

	Representation representation;
	Class<?> entityClass;
	FunctionImport function;
	T entity;
	
	public AtomContentFunctionHandler(Class<?> entityClass, Representation representation,
			FunctionImport function, Metadata metadata, T entity) {
		this.entityClass = entityClass;
		//this.metadata = metadata;
		this.function = function;
		this.representation = representation;
		this.entity = entity;
	}

	/* (non-Javadoc)
	 * @see org.restlet.ext.odata.internal.FunctionContentHandler#parseResult()
	 */
	@SuppressWarnings({ "unchecked"})
	public Object parseResult() {
		try {
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader reader = factory.createXMLEventReader(representation.getReader());

            String edmReturnType = this.function.getReturnType();
			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();
				StartElement startElement = null;

				if (event.isEndDocument() || (event.isEndElement()
						&& startElement != null
						&& event.asEndElement().getName()
								.equals(startElement.getName()))) {
					break;
				}

				if (isStartElement(event, new QName(XmlFormatParser.NS_DATASERVICES, function.getName()))) {
					startElement = event.asStartElement();
				}

				if (event.isStartElement()
						&& event.asStartElement().getName().getNamespaceURI()
								.equals(NS_DATASERVICES)) {
					if (entity instanceof List) { // collection
						this.parseCollection(reader, startElement);
					} else { // simple
						if(TypeUtils.isEdmSimpleType(edmReturnType)){ // Return type startswith EDM : Simple
							entity = (T) TypeUtils.fromEdm(reader.getElementText(), edmReturnType);
						} else {
							AtomFeedHandler.parseProperties(reader,
									event.asStartElement(), entity);
						}
					}
				}
			}
		} catch (XMLStreamException e) {
			Context.getCurrentLogger().warning(
                    "Cannot parse the function xml due to Stream Exception: " + e.getMessage());
		} catch (IOException e) {
			Context.getCurrentLogger().warning(
                    "Cannot parse the function xml due to IO Exception: " + e.getMessage());
		}
		return entity;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void parseCollection(XMLEventReader reader, StartElement startElement) {
		try {
			boolean isPrimitiveCollection = TypeUtils.isEdmSimpleType(TypeUtils.getCollectionType(this.function.getReturnType()));
			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();

				if (event.isEndElement()
						&& event.asEndElement().getName()
								.equals(startElement.getName())) {
					//return entity;
					return;
				}
				if (event.isStartElement()
						&& event.asStartElement().getName()
								.getNamespaceURI()
								.equals(NS_DATASERVICES)
						&& event.asStartElement().getName()
								.equals(DATASERVICES_ELEMENT)) {
					if (isPrimitiveCollection) { // simple type
						// just add value to list
						Object value = TypeUtils.convert(entityClass,
								reader.getElementText());
						((List) entity).add(value);
					} else { // complex type
						Object obj = entityClass.newInstance();
						// create a new instance and populate the
						// properties
						AtomFeedHandler.parseProperties(reader,
								event.asStartElement(), obj);
						((List) entity).add(obj);
					}
				}
			}
		} catch (XMLStreamException e) {
			Context.getCurrentLogger().warning(
                    "Cannot parse the collection xml due to Stream Exception: " + e.getMessage());
		} catch (InstantiationException e) {
			Context.getCurrentLogger().warning(
                    "Cannot parse the collection xml due to Stream Exception: " + e.getMessage());
		} catch (IllegalAccessException e) {
			Context.getCurrentLogger().warning(
                    "Cannot parse the collection xml due to Stream Exception: " + e.getMessage());
		}
	}
			
}