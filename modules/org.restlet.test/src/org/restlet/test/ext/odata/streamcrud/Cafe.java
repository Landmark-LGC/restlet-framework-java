package org.restlet.test.ext.odata.streamcrud;

import org.restlet.data.Reference;
import org.restlet.ext.odata.streaming.StreamReference;

/**
 * Generated by the generator tool for the WCF Data Services extension for the
 * Restlet framework.<br>
 * 
 * @see <a href="http://localhost:8111/Cafe.svc/$metadata">Metadata of the
 *      target WCF Data Services</a>
 * 
 */
public class Cafe {

	private String city;
	private String id;
	private String name;
	private Integer zipCode;
	private StreamReference attachment;
	/** The reference of the underlying blob representation. */
	private Reference blobReference;
	/** The reference to update the underlying blob representation. */
	private Reference blobEditReference;

	/**
	 * Constructor without parameter.
	 * 
	 */
	public Cafe() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            The identifiant value of the entity.
	 */
	public Cafe(String id) {
		this();
		this.id = id;
	}

	/**
	 * Returns the value of the city attribute.
	 * 
	 * @return The value of the city attribute.
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Returns the value of the id attribute.
	 * 
	 * @return The value of the id attribute.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the value of the name attribute.
	 * 
	 * @return The value of the name attribute.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the value of the zipCode attribute.
	 * 
	 * @return The value of the zipCode attribute.
	 */
	public Integer getZipCode() {
		return zipCode;
	}

	/**
	 * Sets the value of the city attribute.
	 * 
	 * @param City
	 *            The value of the city attribute.
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Sets the value of the id attribute.
	 * 
	 * @param ID
	 *            The value of the id attribute.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the value of the name attribute.
	 * 
	 * @param Name
	 *            The value of the name attribute.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the value of the zipCode attribute.
	 * 
	 * @param ZipCode
	 *            The value of the zipCode attribute.
	 */
	public void setZipCode(Integer zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * Returns the @{Link Reference} of the underlying blob.
	 * 
	 * @return The @{Link Reference} of the underlying blob.
	 */
	public Reference getBlobReference() {
		return blobReference;
	}

	/**
	 * Returns the @{Link Reference} to update the underlying blob.
	 * 
	 * @return The @{Link Reference} to update the underlying blob.
	 */
	public Reference getBlobEditReference() {
		return blobEditReference;
	}

	/**
	 * Sets the value of the "attachment" attribute.
	 * 
	 * @param attachment
	 *            The value of the "attachment" attribute.
	 */
	public StreamReference getAttachment() {
		return attachment;
	}

	/**
	 * Sets the @{Link Reference} of the underlying blob.
	 * 
	 * @param ref
	 *            The @{Link Reference} of the underlying blob.
	 */
	public void setBlobReference(Reference ref) {
		this.blobReference = ref;
	}

	/**
	 * Sets the @{Link Reference} to update the underlying blob.
	 * 
	 * @param ref
	 *            The @{Link Reference} to update the underlying blob.
	 */
	public void setBlobEditReference(Reference ref) {
		this.blobEditReference = ref;
	}

	/**
	 * Sets the value of the "attachment" attribute.
	 * 
	 * @param attachment
	 *            The value of the "attachment" attribute.
	 */
	public void setAttachment(StreamReference attachment) {
		this.attachment = attachment;
	}
}
