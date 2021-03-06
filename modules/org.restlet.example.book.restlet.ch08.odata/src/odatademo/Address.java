/**
 * Copyright 2005-2010 Noelios Technologies.
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL 1.0 (the
 * "Licenses"). You can select the license that you prefer but you may not use
 * this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0.html
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1.php
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1.php
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0.php
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.noelios.com/products/restlet-engine
 * 
 * Restlet is a registered trademark of Noelios Technologies.
 */

package odatademo;



/**
* Generated by the generator tool for the OData extension for the Restlet framework.<br>
*
* @see <a href="http://services.odata.org/OData/OData.svc/$metadata">Metadata of the target OData service</a>
*
*/
public class Address {

    private String city;
    private String country;
    private String state;
    private String street;
    private String zipCode;

    /**
     * Constructor without parameter.
     * 
     */
    public Address() {
        super();
    }
    
   /**
    * Returns the value of the "city" attribute.
    *
    * @return The value of the "city" attribute.
    */
   public String getCity() {
      return city;
   }

   /**
    * Returns the value of the "country" attribute.
    *
    * @return The value of the "country" attribute.
    */
   public String getCountry() {
      return country;
   }

   /**
    * Returns the value of the "state" attribute.
    *
    * @return The value of the "state" attribute.
    */
   public String getState() {
      return state;
   }

   /**
    * Returns the value of the "street" attribute.
    *
    * @return The value of the "street" attribute.
    */
   public String getStreet() {
      return street;
   }

   /**
    * Returns the value of the "zipCode" attribute.
    *
    * @return The value of the "zipCode" attribute.
    */
   public String getZipCode() {
      return zipCode;
   }

   /**
    * Sets the value of the "city" attribute.
    *
    * @param city
    *     The value of the "city" attribute.
    */
   public void setCity(String city) {
      this.city = city;
   }

   /**
    * Sets the value of the "country" attribute.
    *
    * @param country
    *     The value of the "country" attribute.
    */
   public void setCountry(String country) {
      this.country = country;
   }

   /**
    * Sets the value of the "state" attribute.
    *
    * @param state
    *     The value of the "state" attribute.
    */
   public void setState(String state) {
      this.state = state;
   }

   /**
    * Sets the value of the "street" attribute.
    *
    * @param street
    *     The value of the "street" attribute.
    */
   public void setStreet(String street) {
      this.street = street;
   }

   /**
    * Sets the value of the "zipCode" attribute.
    *
    * @param zipCode
    *     The value of the "zipCode" attribute.
    */
   public void setZipCode(String zipCode) {
      this.zipCode = zipCode;
   }

}