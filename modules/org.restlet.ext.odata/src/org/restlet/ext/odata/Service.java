/**
 * Copyright 2005-2014 Restlet
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL
 * 1.0 (the "Licenses"). You can select the license that you prefer but you may
 * not use this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.restlet.com/products/restlet-framework
 * 
 * Restlet is a registered trademark of Restlet
 */

package org.restlet.ext.odata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.CharacterSet;
import org.restlet.data.Cookie;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Tag;
import org.restlet.engine.header.Header;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.engine.header.HeaderReader;
import org.restlet.engine.header.HeaderUtils;
import org.restlet.ext.atom.Content;
import org.restlet.ext.atom.Entry;
import org.restlet.ext.odata.batch.util.RestletBatchRequestHelper;
import org.restlet.ext.odata.factory.FeedHandlerFactory;
import org.restlet.ext.odata.internal.FunctionContentHandler;
import org.restlet.ext.odata.internal.edm.AssociationEnd;
import org.restlet.ext.odata.internal.edm.EntityContainer;
import org.restlet.ext.odata.internal.edm.EntityType;
import org.restlet.ext.odata.internal.edm.FunctionImport;
import org.restlet.ext.odata.internal.edm.Metadata;
import org.restlet.ext.odata.internal.edm.Property;
import org.restlet.ext.odata.internal.edm.TypeUtils;
import org.restlet.ext.odata.internal.reflect.ReflectUtils;
import org.restlet.ext.odata.json.JsonFormatWriter;
import org.restlet.ext.odata.streaming.StreamReference;
import org.restlet.ext.odata.xml.XmlFormatWriter;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.ext.xml.format.FormatParser;
import org.restlet.ext.xml.format.FormatType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;
import org.w3c.dom.Node;

/**
 * Acts as a manager for a specific remote OData service. OData services are
 * stateless, but {@link Service} instances are not. State on the client is
 * maintained between interactions in order to support features such as update
 * management.<br>
 * <br>
 * This Java class is more or less equivalent to the WCF DataServiceContext
 * class.
 * 
 * @author Jerome Louvel
 * @see <a
 *      href="http://msdn.microsoft.com/en-us/library/system.data.services.client.dataservicecontext.aspx">DataServiceContext
 *      Class on MSDN</a>
 */
public class Service {
    /** WCF data services metadata namespace. */
    public final static String WCF_DATASERVICES_METADATA_NAMESPACE = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata";

    /** WCF data services namespace. */
    public final static String WCF_DATASERVICES_NAMESPACE = "http://schemas.microsoft.com/ado/2007/08/dataservices";

    /** WCF data services scheme namespace. */
    public final static String WCF_DATASERVICES_SCHEME_NAMESPACE = "http://schemas.microsoft.com/ado/2007/08/dataservices/scheme";

    /** The client connector used in case the context does not deliver one. */
    private Client clientConnector;

    /**
     * The version of the OData protocol extensions defined in every request
     * issued by this service.
     */
    private String clientVersion;

    /** The credentials used to authenticate requests. */
    private ChallengeResponse credentials;

    /** The latest request sent to the service. */
    private Request latestRequest;

    /** The response to the latest request. */
    private Response latestResponse;

    /** The internal logger. */
    private Logger logger;
    
    private FormatType formatType = FormatType.ATOM;
   
    /** 
     * add Query parameter in request header for each call.
     */
    private Map<String, String> parameters;
    /**
     * The maximum version of the OData protocol extensions the client can
     * accept in a response.
     */
    private String maxClientVersion;

    /** The metadata of the WCF service. */
    private Metadata metadata;

    /**
     * The version of the OData protocol extensions defined by the remote
     * service.
     */
    private String serverVersion;

    /** The reference of the WCF service. */
    private Reference serviceRef;

	/** List of cookies used to cache the cookies sent from server. */
	List<Cookie> cookies;
	
	/** The isPostRequest used to differentiate POST request. */
	private boolean isPostRequest;	

    /**
     * Constructor.
     * 
     * @param serviceRef
     *            The reference to the WCF service.
     */
    public Service(Reference serviceRef) {
        try {
            // Test the given service URI which may be actually redirected.
            ClientResource cr = new ClientResource(serviceRef);
            if (cr.getNext() == null) {
                // The context does not provide a client connector.
                // Let instantiate our own.
                Protocol rProtocol = cr.getProtocol();
                Reference rReference = cr.getReference();
                Protocol protocol = (rProtocol != null) ? rProtocol
                        : (rReference != null) ? rReference.getSchemeProtocol()
                                : null;

                if (protocol != null) {
                    this.clientConnector = new Client(protocol);
                    // Set the next handler for reuse
                    cr.setNext(this.clientConnector);
                }
            }

            cr.setFollowingRedirects(false);
            cr.get();

			// don't set serviceRef to redirected url otherwise it will use that url to fetch metadata
            /*if (cr.getStatus().isRedirection()) {
                this.serviceRef = cr.getLocationRef();
            } else {*/
                this.serviceRef = cr.getReference();
            //}
        } catch (Throwable e) {
            this.serviceRef = serviceRef;
        }
    }

    /**
     * Constructor.
     * 
     * @param serviceUri
     *            The URI of the WCF service.
     */
    public Service(String serviceUri) {
        this(new Reference(serviceUri));
    }

    /**
     * Adds an entity to an entity set.
     * @param <T>
     * @param <T>
     * 
     * @param entitySetName
     *            The path of the entity set relatively to the service URI.
     * @param entity
     *            The entity to put.
     * @throws Exception
     */
    public <T> T addEntity(String entitySetName, Object entity) throws Exception {
		if (entity != null) {
			isPostRequest = Boolean.TRUE;
			ClientResource resource = createResource(entitySetName);
			if (getMetadata() == null) {
				throw new Exception("Can't add entity to this entity set "
						+ resource.getReference()
						+ " due to the lack of the service's metadata.");
			}
			Metadata metadata = (Metadata) getMetadata();
			EntityType type = metadata.getEntityType(entity.getClass());
			Representation rep = null;
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				if (FormatType.ATOM.equals(this.getFormatType())) {
					Entry entry = toEntry(entity);
					entry.write(baos);
				} else {
					/*
					 * For JSON verbose implementation we have separate writer
					 * which will return the JSON representation of an entity
					 * for POST request.
					 */
					final JsonFormatWriter r = new JsonFormatWriter(
							(Metadata) getMetadata(), entity, isPostRequest);
					r.write(baos);
				}
				baos.flush();
				MediaType mediaType = FormatType.getMediaType(this
						.getFormatType());
				StringRepresentation r = new StringRepresentation(
						baos.toString(), mediaType);
				/* 
				 * Set Accept: header corresponding to media type for.
				 * json/atom/jsonverbose.
				 */
				resource.getRequest().getClientInfo().accept(mediaType);
				rep = resource.post(r);
				 /*
				  * Handle the blob type of properties where we need to make 
				  * separate PUT requests for the same after POSTing the non stream 
				  * properties of an entity.
				  */				
				if (type.isBlob()) {
					handleStreamProperties(entity, type);
				}

				// Parse the response to populate the newly created entity object.
				FormatParser<T> feedHandler = FeedHandlerFactory.getParser(
						this.getFormatType(), type, entity.getClass(),
						metadata, null);
				T newEntity = RestletBatchRequestHelper.getEntity(rep,
						feedHandler);
				return newEntity;

			} catch (ResourceException re) {
				throw new ResourceException(re.getStatus(),
						"Can't add entity to this entity set "
								+ resource.getReference());
			} catch (Exception e) {
				getLogger()
						.log(Level.WARNING,
								"Can't add the entity : " + " due to "
										+ e.getMessage());
			} finally {
				isPostRequest = Boolean.FALSE;
				this.latestRequest = resource.getRequest();
				this.latestResponse = resource.getResponse();
			}
		}
		return null;
	}

	/**
	 * Handle stream properties.
	 *
	 * @param entity the entity
	 * @param type the type
	 * @throws Exception the exception
	 */
	private void handleStreamProperties(Object entity, EntityType type)
			throws Exception {
		List<Property> properties = type.getProperties();
		Iterator<Property> iterator = properties.iterator();

		while (iterator.hasNext()) {
			Property prop = iterator.next();
			/* 
			 * Find the streaming property from entity 
			 * and assign stream value to it. 
			 */
			if (prop.getType().getName().contains("Stream")) {
				Object propertyObject = ReflectUtils.invokeGetter(
						entity, prop.getNormalizedName());
				if (null != propertyObject) {
					StreamReference streamReference = (StreamReference) propertyObject;
					if (streamReference != null) {
						updateNamedStream(entity, prop.getName(),
								streamReference);
					}
				}
			}
		}
	}

	/**
     * Adds an association between the source and the target entity via the
     * given property name.
     * 
     * @param source
     *            The source entity to update.
     * @param sourceProperty
     *            The name of the property of the source entity.
     * @param target
     *            The entity to add to the source entity.
     * @throws Exception
     */
    public void addLink(Object source, String sourceProperty, Object target)
            throws Exception {
        if (getMetadata() == null || source == null) {
            return;
        }
        if (target != null) {
            addEntity(getSubpath(source, sourceProperty), target);
        }
    }

    /**
     * Creates a query to a specific entity hosted by this service.
     * 
     * @param <T>
     *            The class of the target entity.
     * @param subpath
     *            The path to this entity relatively to the service URI.
     * @param entityClass
     *            The target class of the entity.
     * @return A query object.
     */
    public <T> Query<T> createQuery(String subpath, Class<T> entityClass) {
        return new Query<T>(this, subpath, entityClass);
    }

    /**
     * Returns an instance of {@link ClientResource} given an absolute
     * reference. This resource is completed with the service credentials. This
     * method can be overridden in order to complete the sent requests.
     * 
     * @param reference
     *            The reference of the target resource.
     * @return An instance of {@link ClientResource}.
     */
    public ClientResource createResource(Reference reference) {
        ClientResource resource = new ClientResource(reference);

        if (clientConnector != null) {
            // We provide our own cient connector.
            resource.setNext(clientConnector);
        }
        
        // add the cached cookies to request to prevent submitting the form in form based auth.
        if(this.cookies != null){
        	resource.getRequest().getCookies().addAll(this.cookies);
        }

        resource.setChallengeResponse(getCredentials());
        Series<Header> headers = new Series<Header>(Header.class);
        if (getClientVersion() != null || getMaxClientVersion() != null) {
        	
            if (getClientVersion() != null) {
                headers.add("DataServiceVersion", getClientVersion());
            }

            if (getMaxClientVersion() != null) {
                headers.add("MaxDataServiceVersion", getMaxClientVersion());
            }            
          
        }
       
		/*
		 * Check if query parameter map is not null and not empty, add all the
		 * Query parameters in request as a header.
		 */
		if (getParameter() != null && !getParameter().isEmpty()) {
			Iterator<java.util.Map.Entry<String, String>> iterator = getParameter()
					.entrySet().iterator();
			while (iterator.hasNext()) {
				java.util.Map.Entry<String, String> entry = iterator.next();
				headers.add(entry.getKey(), entry.getValue());
			}

		}    
        resource.setAttribute(HeaderConstants.ATTRIBUTE_HEADERS, headers);
       
        return resource;
    }

    /**
     * Returns an instance of {@link ClientResource} given a path (relative to
     * the service reference). This resource is completed with the service
     * credentials. This method can be overriden in order to complete the sent
     * requests.
     * 
     * @param relativePath
     *            The relative reference of the target resource.
     * @return An instance of {@link ClientResource} given a path (relative to
     *         the service reference).
     */
    public ClientResource createResource(String relativePath) {
        String ref = getServiceRef().toString();
        if (ref.endsWith("/")) {
            if (relativePath.startsWith("/")) {
                ref = ref + relativePath.substring(1);
            } else {
                ref = ref + relativePath;
            }
        } else {
            if (relativePath.startsWith("/")) {
                ref = ref + relativePath;
            } else {
                ref = ref + "/" + relativePath;
            }
        }

        return createResource(new Reference(ref));
    }

    /**
     * Deletes an entity.
     * 
     * @param entity
     *            The entity to delete
     * @throws ResourceException
     */
    public void deleteEntity(Object entity) throws ResourceException {
        if (getMetadata() == null) {
            return;
        }

        ClientResource resource = createResource(getSubpath(entity));

        try {
            resource.delete();
        } catch (ResourceException re) {
            throw new ResourceException(re.getStatus(),
                    "Can't delete this entity " + resource.getReference());
        } finally {
            this.latestRequest = resource.getRequest();
            this.latestResponse = resource.getResponse();
        }
    }

    /**
     * Deletes an entity.
     * 
     * @param entitySubpath
     *            The path of the entity to delete
     * @throws ResourceException
     */
    public void deleteEntity(String entitySubpath) throws ResourceException {
        ClientResource resource = createResource(entitySubpath);

        try {
            resource.delete();
        } catch (ResourceException re) {
            throw new ResourceException(re.getStatus(),
                    "Can't delete this entity " + resource.getReference());
        } finally {
            this.latestRequest = resource.getRequest();
            this.latestResponse = resource.getResponse();
        }
    }

    /**
     * Removes the association between a source entity and a target entity via
     * the given property name.
     * 
     * @param source
     *            The source entity to update.
     * @param sourceProperty
     *            The name of the property of the source entity.
     * @param target
     *            The entity to delete from the source entity.
     * @throws ResourceException
     */
    public void deleteLink(Object source, String sourceProperty, Object target)
            throws ResourceException {
        if (getMetadata() == null) {
            return;
        }
        deleteEntity(getSubpath(source, sourceProperty, target));
    }

    /**
     * Returns the version of the OData protocol extensions defined in every
     * request issued by this service.
     * 
     * @return The version of the OData protocol extensions defined in every
     *         request issued by this service.
     */
    public String getClientVersion() {
        return clientVersion;
    }

    /**
     * Returns the credentials used to authenticate requests.
     * 
     * @return The credentials used to authenticate requests.
     */
    public ChallengeResponse getCredentials() {
        return credentials;
    }

    /**
     * Returns the latest request sent to the service.
     * 
     * @return The latest request sent to the service.
     */
    public Request getLatestRequest() {
        return latestRequest;
    }

    /**
     * Returns the response to the latest request.
     * 
     * @return The response to the latest request.
     */
    public Response getLatestResponse() {
        return latestResponse;
    }

    /**
     * Returns the current logger.
     * 
     * @return The current logger.
     */
    private Logger getLogger() {
        if (logger == null) {
            logger = Context.getCurrentLogger();
        }
        return logger;
    }

    /**
     * Returns the maximum version of the OData protocol extensions the client
     * can accept in a response.
     * 
     * @return The maximum version of the OData protocol extensions the client
     *         can accept in a response.
     */
    public String getMaxClientVersion() {
        return maxClientVersion;
    }

    /**
     * Returns the metadata document related to the current service.
     * 
     * @return The metadata document related to the current service.
     */
    public Object getMetadata() {
        if (metadata == null) {
        	long beforeRequest = System.currentTimeMillis();
        	
            ClientResource resource = createResource("$metadata");

            try {
                getLogger().log(
                        Level.INFO,
                        "Get the metadata for " + getServiceRef() + " at "
                                + resource.getReference());
                Representation rep = resource.get(MediaType.APPLICATION_XML);
                // after metadata request is handled by submitting the form, get the cookies from response and cache it.
                Series<Cookie> responseCookies = resource.getResponse().getRequest().getCookies();
                if(responseCookies != null){
                	if(this.cookies == null){
                		this.cookies = new ArrayList<Cookie>();
                	}
                	this.cookies.addAll(responseCookies);
                }
                this.metadata = new Metadata(rep, resource.getReference());
            } catch (ResourceException e) {
                getLogger().log(
                        Level.SEVERE,
                        "Can't get the metadata for " + getServiceRef()
                                + " (response's status: "
                                + resource.getStatus() + ")");
            } catch (Exception e) {
                getLogger().log(Level.SEVERE,
                        "Can't get the metadata for " + getServiceRef(), e);
            } finally {
                this.latestRequest = resource.getRequest();
                this.latestResponse = resource.getResponse();
            }

			long afterRequest = System.currentTimeMillis();
            getLogger().fine("Metadata loaded in " + (afterRequest - beforeRequest) + " milliseconds.");
        }

        return metadata;
    }

    /**
     * Returns the version of the OData protocol extensions supported by the
     * remote service.
     * 
     * @return The version of the OData protocol extensions supported by the
     *         remote service.
     */
    @SuppressWarnings("unchecked")
    public String getServerVersion() {
        if (serverVersion == null) {
            // Get the version from the latest response.
            if (this.latestResponse != null) {
                Object o = this.latestResponse.getAttributes().get(
                        HeaderConstants.ATTRIBUTE_HEADERS);

                if (o != null) {
                    Series<Header> headers = (Series<Header>) o;
                    String strHeader = headers.getFirstValue(
                            "DataServiceVersion", true);

                    if (strHeader != null) {
                        HeaderReader<Object> reader = new HeaderReader<Object>(
                                strHeader);
                        this.serverVersion = reader.readToken();
                    }
                }
            }
        }

        return serverVersion;
    }

    /**
     * Returns the reference to the WCF service.
     * 
     * @return The reference to the WCF service.
     */
    public Reference getServiceRef() {
        return serviceRef;
    }

    /**
     * Extracts a String value from the Representation of a property or a
     * function, or a service operation, when this representation wraps an EDM
     * simple type.
     * 
     * @param representation
     *            The representation to parse
     * @param tagName
     *            The name of the property or function.
     * @return The String value taken from the representation.
     * @throws Exception
     *             Thrown when a parsing error occurs.
     */
    private String getSimpleValue(Representation representation, String tagName)
            throws Exception {
        String result = null;

        if (representation == null) {
            return result;
        }

        if (MediaType.APPLICATION_ALL_XML.isCompatible(representation
                .getMediaType())
                || MediaType.TEXT_XML.isCompatible(representation
                        .getMediaType())) {
            DomRepresentation xmlRep = new DomRepresentation(representation);
            // [ifndef android] instruction
            Node node = xmlRep.getNode(tagName);

            // [ifdef android] uncomment
            // Node node = null;
            // try {
            // org.w3c.dom.NodeList nl = xmlRep.getDocument()
            // .getElementsByTagName(tagName);
            // node = (nl.getLength() > 0) ? nl.item(0) : null;
            // } catch (IOException e1) {
            // }
            // [enddef]

            if (node != null) {
                // [ifndef android] instruction
                result = node.getTextContent();
                // [ifdef android] instruction uncomment
                // result =
                // org.restlet.ext.xml.XmlRepresentation.getTextContent(node);
            }
        } else {
            result = representation.getText();
        }

        return result;
    }

    /**
     * According to the metadata of the service, returns the path of the given
     * entity relatively to the current WCF service.
     * 
     * @param entity
     *            The entity.
     * @return The path of the given entity relatively to the current WCF
     *         service.
     */
    private String getSubpath(Object entity) {
        return ((Metadata) getMetadata()).getSubpath(entity);
    }

    /**
     * According to the metadata of the service, returns the path of the given
     * entity's property relatively to the current WCF service.
     * 
     * @param entity
     *            The entity.
     * @param propertyName
     *            The name of the property.
     * @return The path of the given entity's property relatively to the current
     *         WCF service.
     */
    private String getSubpath(Object entity, String propertyName) {
        return ((Metadata) getMetadata()).getSubpath(entity, propertyName);
    }

    /**
     * According to the metadata of the service, returns the relative path of
     * the given target entity linked to the source entity via the source
     * property.
     * 
     * @param source
     *            The source entity to update.
     * @param sourceProperty
     *            The name of the property of the source entity.
     * @param target
     *            The entity linked to the source entity.
     * @return
     */
    private String getSubpath(Object source, String sourceProperty,
            Object target) {
        return ((Metadata) getMetadata()).getSubpath(source, sourceProperty,
                target);
    }

    /**
     * Returns the ETag value for the given entity.
     * 
     * @param entity
     *            The given entity.
     * @return The ETag value for the given entity.
     */
    private String getTag(Object entity) {
        String result = null;
        if (entity != null) {
            Metadata metadata = (Metadata) getMetadata();
            EntityType type = metadata.getEntityType(entity.getClass());

            StringBuilder sb = new StringBuilder();
            boolean found = false;
            for (Property property : type.getProperties()) {
                if (property.isConcurrent()) {
                    found = true;
                    Object value = null;
                    try {
                        value = ReflectUtils.invokeGetter(entity,
                                property.getName());
                        if (value != null) {
                            sb.append(value);
                        }
                    } catch (Exception e) {
                        getLogger().warning(
                                "Cannot get the value of the property "
                                        + property.getName() + " on " + entity);
                    }
                }
            }

            if (found) {
                result = Reference.encode(sb.toString(), CharacterSet.US_ASCII);
            }
        }

        return result;
    }

    /**
     * Returns the binary representation of the given media resource. If the
     * entity is not a media resource, it returns null.
     * 
     * @param entity
     *            The given media resource.
     * @return The binary representation of the given media resource.
     */
    public Representation getValue(Object entity) throws ResourceException {
        Reference ref = getValueRef(entity);
        if (ref != null) {
            ClientResource cr = createResource(ref);
            return cr.get();
        }

        return null;
    }

    /**
     * Returns the binary representation of the given media resource. If the
     * entity is not a media resource, it returns null.
     * 
     * @param entity
     *            The given media resource.
     * @param acceptedMediaTypes
     *            The requested media types of the representation.
     * @return The given media resource.
     */
    public Representation getValue(Object entity,
            List<Preference<MediaType>> acceptedMediaTypes)
            throws ResourceException {
        Reference ref = getValueRef(entity);
        if (ref != null) {
            ClientResource cr = createResource(ref);
            cr.getClientInfo().setAcceptedMediaTypes(acceptedMediaTypes);
            return cr.get();
        }

        return null;
    }

    /**
     * Returns the binary representation of the given media resource. If the
     * entity is not a media resource, it returns null.
     * 
     * @param entity
     *            The given media resource.
     * @param mediaType
     *            The requested media type of the representation
     * @return The given media resource.
     */
    public Representation getValue(Object entity, MediaType mediaType)
            throws ResourceException {
        Reference ref = getValueRef(entity);
        if (ref != null) {
            ClientResource cr = createResource(ref);
            return cr.get(mediaType);
        }

        return null;
    }

    /**
     * Returns the reference used to edit the binary representation of the given
     * entity, if this is a media resource. It returns null otherwise.
     * 
     * @param entity
     *            The media resource.
     * @return Returns the reference used to edit the binary representation of
     *         the given entity, if this is a media resource. It returns null
     *         otherwise.
     */
    private Reference getValueEditRef(Object entity) {
        if (entity != null) {
            Metadata metadata = (Metadata) getMetadata();
            EntityType type = metadata.getEntityType(entity.getClass());
            if (type.isBlob() && type.getBlobValueEditRefProperty() != null) {
                try {
                    return (Reference) ReflectUtils.invokeGetter(entity, type
                            .getBlobValueEditRefProperty().getName());
                } catch (Exception e) {
                    getLogger().warning(
                            "Cannot get the value of the property "
                                    + type.getBlobValueEditRefProperty()
                                            .getName() + " on " + entity);
                }
            } else {
                getLogger().warning(
                        "This entity is not a media resource " + entity);
            }
        }

        return null;
    }

    /**
     * Returns the reference of the binary representation of the given entity,
     * if this is a media resource. It returns null otherwise.
     * 
     * @param entity
     *            The media resource.
     * @return The reference of the binary representation of the given entity,
     *         if this is a media resource. It returns null otherwise.
     */
    public Reference getValueRef(Object entity) {
        if (entity != null) {
            Metadata metadata = (Metadata) getMetadata();

            EntityType type = metadata.getEntityType(entity.getClass());
            if (type.isBlob() && type.getBlobValueRefProperty() != null) {
                try {
                    return (Reference) ReflectUtils.invokeGetter(entity, type
                            .getBlobValueRefProperty().getName());
                } catch (Exception e) {
                    getLogger().warning(
                            "Cannot get the value of the property "
                                    + type.getBlobValueRefProperty().getName()
                                    + " on " + entity);
                }
            } else {
                getLogger().warning(
                        "This entity is not a media resource " + entity);
            }
        }

        return null;
    }

    /**
     * Invokes a service operation and return the raw representation sent back
     * by the service.
     * 
     * @param service
     *            The name of the service.
     * @param parameters
     *            The list of required parameters.
     * @return The representation returned by the invocation of the service.
     * @throws ResourceException
     *             Thrown when the service call is not successfull.
     * @see <a
     *      href="http://msdn.microsoft.com/en-us/library/cc668788.aspx">Service
     *      Operations</a>
     */
    public Representation invokeComplex(String service,
            Series<Parameter> parameters) throws ResourceException {
        Representation result = null;
        Metadata metadata = (Metadata) getMetadata();
        // Look for the FunctionImport element.
        FunctionImport function = getFunction(service, metadata);
        
        if (function != null) {
            ClientResource resource = createResource(service);
            // Set format type
            resource.getRequest().getClientInfo().accept(FormatType.getMediaType(this.getFormatType()));
            if(function.getMethod() !=null){
            	resource.setMethod(function.getMethod());
            }
            if (parameters != null) {
            	// if this is GET/DELETE method then send the paramenters in query string
            	if(resource.getMethod().equals(new Method("GET")) || resource.getMethod().equals(new Method("DELETE"))){
                    for (org.restlet.ext.odata.internal.edm.Parameter parameter : function
                            .getParameters()) {                    	
                        resource.getReference().addQueryParameter(
                                parameter.getName(),
                                TypeUtils.getLiteralForm(parameters
                                        .getFirstValue(parameter.getName()),
                                        parameter.getType()));
                    }
            	}else{
            	    StringBuilder sb = new StringBuilder();
            		sb.append("{"+"\n");
            		String json ="";
            		int noOfParameters=0;
            		for (Parameter parameter : parameters) {
            			noOfParameters++;
						sb.append("\"").append(parameter.getName()).append("\"").append(":");
						for (org.restlet.ext.odata.internal.edm.Parameter edmParameter : function.getParameters()) {
							if(parameter.getName().equalsIgnoreCase(edmParameter.getName())){
								if(edmParameter.getType().contains("String")){
									json = "\"" + parameter.getValue()+ "\"";
								}else if(edmParameter.getType().contains("Collection")){
									json = parameter.getValue();
								}else{
									json = parameter.getValue();
								}
								sb.append(json);
								if(noOfParameters!=parameters.size()){
									sb.append(",");
								}
							}
						}
					}
            		sb.append("\n"+"}");
            		Series<Header> headerSeries = new Series<Header>(Header.class);
                	resource.getRequest().setEntity(sb.toString(), MediaType.APPLICATION_JSON);
                	HeaderUtils.addHeader(HeaderConstants.HEADER_ACCEPT,MediaType.APPLICATION_ATOM.getName(),headerSeries);
            	    resource.setAttribute(HeaderConstants.ATTRIBUTE_HEADERS, headerSeries);
            	}
            }
            result = resource.handle();
            this.latestRequest = resource.getRequest();
            this.latestResponse = resource.getResponse();

            if (resource.getStatus().isError()) {
                throw new ResourceException(resource.getStatus());
            }
        }
        return result;
    }

	private FunctionImport getFunction(String service, Metadata metadata) {
		FunctionImport function = null;
		if (metadata != null && service != null) {
			for (EntityContainer container : metadata.getContainers()) {
			    for (FunctionImport f : container.getFunctionImports()) {
			        if (service.equals(f.getName())) {
			            function = f;
			            break;
			        }
			    }
			    if (function != null) {
			        break;
			    }
			}
		}
		return function;
	}

    /**
     * Invokes a service operation and return the String value sent back by the
     * service.
     * 
     * @param service
     *            The name of the service.
     * @param parameters
     *            The list of required parameters.
     * @return The value returned by the invocation of the service as a String.
     * @throws ResourceException
     *             Thrown when the service call is not successfull.
     * @throws Exception
     *             Thrown when the value cannot be parsed.
     * @see <a
     *      href="http://msdn.microsoft.com/en-us/library/cc668788.aspx">Service
     *      Operations</a>
     */
    public String invokeSimple(String service, Series<Parameter> parameters)
            throws ResourceException, Exception {
        return getSimpleValue(invokeComplex(service, parameters), service);
    }
    
    public Object invokeFunction(String service, Series<Parameter> parameters, Class<?> classType, Object entity){
    	Representation representation = invokeComplex(service, parameters);
        Metadata metadata = (Metadata) getMetadata();
    	FunctionImport function = this.getFunction(service, metadata);
    	if(function.getJavaReturnType()!="void"){
	    	FunctionContentHandler functionParser = FeedHandlerFactory.getFunctionParser(getFormatType(), classType, representation, function, (Metadata) getMetadata(), entity);
			return functionParser.parseResult();
    	}
    	return null;
    }

    /**
     * Updates the given entity object with the value of the specified property.
     * 
     * @param entity
     *            The entity to update.
     * @param propertyName
     *            The name of the property.
     */
    public void loadProperty(Object entity, String propertyName) {
        if (getMetadata() == null || entity == null) {
            return;
        }

        Metadata metadata = (Metadata) getMetadata();

        EntityType type = metadata.getEntityType(entity.getClass());
        AssociationEnd association = metadata
                .getAssociation(type, propertyName);

        if (association != null) {
            EntityType propertyEntityType = association.getType();
            try {
                Class<?> propertyClass = ReflectUtils.getSimpleClass(entity,
                        propertyName);
                if (propertyClass == null) {
                    propertyClass = TypeUtils.getJavaClass(propertyEntityType);
                }
                Iterator<?> iterator = createQuery(
                        getSubpath(entity, propertyName), propertyClass)
                        .iterator();

                ReflectUtils.setProperty(entity, propertyName,
                        association.isToMany(), iterator, propertyClass);
            } catch (Exception e) {
                getLogger().log(
                        Level.WARNING,
                        "Can't set the property " + propertyName + " of "
                                + entity.getClass() + " for the service"
                                + getServiceRef(), e);
            }
        } else {
            ClientResource resource = createResource(getSubpath(entity,
                    propertyName));
            try {
                Representation rep = resource.get();

                try {
                    String value = getSimpleValue(rep, propertyName);
                    Property property = metadata.getProperty(entity,
                            propertyName);
                    ReflectUtils.setProperty(entity, property, value);
                } catch (Exception e) {
                    getLogger().log(
                            Level.WARNING,
                            "Can't set the property " + propertyName + " of "
                                    + entity.getClass() + " for the service"
                                    + getServiceRef(), e);
                }
            } catch (ResourceException e) {
                getLogger().log(
                        Level.WARNING,
                        "Can't get the following resource "
                                + resource.getReference() + " for the service"
                                + getServiceRef(), e);
            }
        }
    }

    /**
     * Sets the version of the OData protocol extensions defined in every
     * request issued by this service.
     * 
     * @param clientVersion
     *            The version of the OData protocol extensions defined in every
     *            request issued by this service.
     */
    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    /**
     * Sets the credentials used to authenticate requests.
     * 
     * @param credentials
     *            The credentials used to authenticate requests.
     */
    public void setCredentials(ChallengeResponse credentials) {
        this.credentials = credentials;
    }

    /**
     * Sets the latest request sent to the service.
     * 
     * @param latestRequest
     *            The latest request sent to the service.
     */
    public void setLatestRequest(Request latestRequest) {
        this.latestRequest = latestRequest;
    }

    /**
     * Sets the response to the latest request.
     * 
     * @param latestResponse
     *            The response to the latest request.
     */
    public void setLatestResponse(Response latestResponse) {
        this.latestResponse = latestResponse;
    }

    /**
     * Sets the association between the source and the target entity via the
     * given property name. If target is set to null, the call represents a
     * delete link operation.
     * 
     * @param source
     *            The source entity to update.
     * @param sourceProperty
     *            The name of the property of the source entity.
     * @param target
     *            The entity to add to the source entity.
     * @throws Exception
     */
    public void setLink(Object source, String sourceProperty, Object target)
            throws Exception {
        if (getMetadata() == null || source == null) {
            return;
        }
        if (target != null) {
            // TODO Take into account the case where the target does exist.
            Metadata metadata = (Metadata) getMetadata();
            ClientResource resource = createResource(metadata
                    .getSubpath(source) + "/$links/" + sourceProperty);

            try {
                // TODO Fix chunked request with net client connector
                StringBuilder sb = new StringBuilder("<uri xmlns=\"");
                sb.append(WCF_DATASERVICES_NAMESPACE);
                sb.append("\">");
                sb.append(getServiceRef().toString());
                sb.append(metadata.getSubpath(target));
                sb.append("</uri>");

                StringRepresentation r = new StringRepresentation(
                        sb.toString(), MediaType.APPLICATION_XML);
                resource.put(r);
            } catch (ResourceException re) {
                throw new ResourceException(re.getStatus(),
                        "Can't set entity to this entity set "
                                + resource.getReference());
            } finally {
                this.latestRequest = resource.getRequest();
                this.latestResponse = resource.getResponse();
            }
        } else {
            ReflectUtils.invokeSetter(source, sourceProperty, null);
            updateEntity(source);
        }
    }

    /**
     * Sets the maximum version of the OData protocol extensions the client can
     * accept in a response.
     * 
     * @param maxClientVersion
     *            The maximum version of the OData protocol extensions the
     *            client can accept in a response.
     */
    public void setMaxClientVersion(String maxClientVersion) {
        this.maxClientVersion = maxClientVersion;
    }

    /**
     * Sets the value of the given media entry link.
     * 
     * @param entity
     *            The media entry link which value is to be updated
     * @param blob
     *            The new representation.
     * @throws ResourceException
     */
    public void setValue(Object entity, Representation blob)
            throws ResourceException {
        Reference ref = getValueEditRef(entity);

        if (ref != null) {
            ClientResource cr = createResource(ref);
            cr.put(blob);
        }
    }

    /**
     * Converts an entity to an Atom entry object.
     * 
     * @param entity
     *            The entity to wrap.
     * @return The Atom entry object that corresponds to the given entity.
     */
	public Entry toEntry(final Object entity) {
		Entry result = null;
		if (entity != null) {
			Metadata metadata = (Metadata) getMetadata();
			EntityType type = metadata.getEntityType(entity.getClass());
			if (type != null) {
				final XmlFormatWriter r = new XmlFormatWriter((Metadata)
				getMetadata(), entity, isPostRequest);
				r.setNamespaceAware(true);
				result = new Entry();
				Content content = new Content();
				content.setInlineContent(r);
				content.setToEncode(false);
				result.setContent(content);
			}
		}
		return result;
	}

    /**
     * Updates an entity.
     * 
     * @param entity
     *            The entity to put.
     * @throws Exception
     */
    public void updateEntity(Object entity) throws Exception {
        if (getMetadata() == null || entity == null) {
            return;
        }
        EntityType type = metadata.getEntityType(entity.getClass());
		ClientResource resource = createResource(getSubpath(entity));
        if (type.isBlob()) {
			List<Property> properties = type.getProperties();
    		Iterator<Property> iterator = properties.iterator();
    		
    		while (iterator.hasNext()) {
				Property prop = iterator.next();
				// find the streaming property from entity and assign stream value to it.
				if (prop.getType().getName().contains("Stream")) { 
					Object propertyObject = ReflectUtils.invokeGetter(entity,
							prop.getNormalizedName());
					if (null != propertyObject) {
						StreamReference streamReference = (StreamReference) propertyObject;
						if (streamReference != null
								&& streamReference.isUpdateStreamData()) {
							updateNamedStream(entity, prop.getName(),
									streamReference);
						}
					}			
               }
    		}
    		// now do merge request for non-stream properties
    		this.mergeEntity(entity);
    	} else {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				if (FormatType.ATOM.equals(this.getFormatType())) {
					Entry entry = toEntry(entity);
					entry.write(baos);
				} else {
					/*
					 * For JSON verbose implementation we have separate writer
					 * which will return the JSON representation of an entity
					 * for PUT request.
					 */
					final JsonFormatWriter r = new JsonFormatWriter(
							(Metadata) getMetadata(), entity, isPostRequest);
					r.write(baos);
				}
				baos.flush();
				StringRepresentation r = new StringRepresentation(
						baos.toString(), FormatType.getMediaType(this.getFormatType()));
				String tag = getTag(entity);

				if (tag != null) {
					// Add a condition
					resource.getConditions().setMatch(
							Arrays.asList(new Tag(tag)));
				}
				resource.put(r);
			} catch (ResourceException re) {
				throw new ResourceException(re.getStatus(),
						"Can't update this entity " + resource.getReference());
			} finally {
				this.latestRequest = resource.getRequest();
				this.latestResponse = resource.getResponse();
			}
		}
    }
    
    /**
     * Update named stream.
     *
     * @param entity the entity
     * @param columnName the column name
     * @param sr the StreamReference
     * @throws Exception
     */
	public void updateNamedStream(Object entity, String columnName,
			StreamReference sr) throws Exception {
		if (getMetadata() == null || entity == null) {
			return;
		}
		EntityType type = metadata.getEntityType(entity.getClass());
		if (type.isBlob()) {
			if (null != sr) {
				ClientResource resource = createResource(getSubpath(entity)
						+ "/" + columnName);
				resource.put(sr.getInputStream());
			}
		}
	}

    /**
    * Method for merger operation.
    * @param entity
	*            The entity to merge.
	*/
    public void mergeEntity(Object entity) {
		this.merge(entity,null);
	}
	
	/**
	 * Updates an entity.
	 * 
	 * @param entity
	 *            The entity to put.
	 * @throws Exception
	 */
	private void merge(Object entity, String id) {
		if (getMetadata() == null || entity == null) {
			return;
		}
		EntityType type = metadata.getEntityType(entity.getClass());
		if (type.isBlob()) {
			List<Property> properties = type.getProperties();
			Iterator<Property> iterator = properties.iterator();
			while (iterator.hasNext()) {
				Property prop = iterator.next();
				if (prop.getType().getName().contains("Stream")) {
					try {
						// merge request should not contain data for stream
						// property, so setting it to null.
						ReflectUtils.invokeSetter(entity,
								prop.getNormalizedName(), null);
					} catch (Exception e) {
						getLogger().warning(
								"Can't merge the object: " + e.getMessage());
					}
					break;
				}

			}
		}
		// Entry entry = toEntry(entity);

		ClientResource resource;
		if (null != id) {
			resource = createResource(new Reference(id));
		} else {
			resource = createResource(getSubpath(entity));
		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (FormatType.ATOM.equals(this.getFormatType())) {
				Entry entry = toEntry(entity);
				entry.write(baos);
			} else {
				/*
				 * For JSON verbose implementation we have separate writer which
				 * will return the JSON representation of an entity for POST
				 * request.
				 */
				final JsonFormatWriter r = new JsonFormatWriter(
						(Metadata) getMetadata(), entity, isPostRequest);
				r.write(baos);
			}
			baos.flush();
			StringRepresentation r = new StringRepresentation(baos.toString(),
					FormatType.getMediaType(this.getFormatType()));
			String tag = getTag(entity);

			if (tag != null) {
				// Add a condition
				resource.getConditions().setMatch(Arrays.asList(new Tag(tag)));
			}
			resource.merge(r);
		} catch (ResourceException re) {
			throw new ResourceException(re.getStatus(),
					"Can't update this entity " + resource.getReference());
		} catch (IOException io) {
			getLogger()
					.warning(
							"IO exception while merging the entity: "
									+ io.getMessage());
		} finally {
			this.latestRequest = resource.getRequest();
			this.latestResponse = resource.getResponse();
		}
	}

	/**
	 * Gets Query parameter.
	 *
	 * @return the parameter
	 */
	public Map<String, String> getParameter() {
		return parameters;
	}

	/**
	 * Sets Query parameter in request header.
	 *
	 * @param parameter the parameter
	 */
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the FormatType
	 */
	public FormatType getFormatType() {
		if(formatType == null){
			formatType = FormatType.ATOM;
		}
		return formatType;
	}

	/**
	 * @param formatType the FormatType to set
	 */
	public void setFormatType(FormatType formatType) {
		this.formatType = formatType;
	}

}
