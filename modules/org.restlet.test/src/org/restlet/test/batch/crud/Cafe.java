package org.restlet.test.batch.crud;

/**
 * Generated by the generator tool for the RestletBatch service extension for
 * the Restlet framework.<br>
 * 
 * @see <a href="http://localhost:8111/Cafe.svc/$metadata">Metadata of the
 *      target Restlet Data Services</a>
 * 
 */
public class Cafe {

	/** The city. */
	private String city;

	/** The id. */
	private String id;

	/** The name. */
	private String name;

	/** The zip code. */
	private int zipCode;

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
	public int getZipCode() {
		return zipCode;
	}

	/**
	 * Sets the value of the city attribute.
	 * 
	 * @param city
	 *            the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Sets the value of the id attribute.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the value of the name attribute.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the value of the zipCode attribute.
	 * 
	 * @param zipCode
	 *            the new zip code
	 */
	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}
}