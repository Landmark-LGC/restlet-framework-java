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

package org.restlet.test.ext.odata.deepexpand.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.restlet.test.ext.odata.deepexpand.model.Address;
import org.restlet.test.ext.odata.deepexpand.model.Language;
import org.restlet.test.ext.odata.deepexpand.model.Telephone;

/**
* Generated by the generator tool for the OData extension for the Restlet framework.<br>
*
* @see <a href="http://praktiki.metal.ntua.gr/CoopOData/CoopOData.svc/$metadata">Metadata of the target OData service</a>
*
*/
public class Person {

    private Date dateOfBirth;
    private String email;
    private String fatherName;
    private String gender;
    private Integer id;
    private String motherName;
    private String name;
    private String notes;
    private String surname;
    private Tracking tracking;
    private List<Address> addresses = new ArrayList<Address>();
    private Language preferredLanguage ;
    private List<Telephone> telephones = new ArrayList<Telephone>();

    /**
     * Constructor without parameter.
     * 
     */
    public Person() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param id
     *            The identifiant value of the entity.
     */
    public Person(Integer id) {
        this();
        this.id = id;
    }

   /**
    * Returns the value of the "dateOfBirth" attribute.
    *
    * @return The value of the "dateOfBirth" attribute.
    */
   public Date getDateOfBirth() {
      return dateOfBirth;
   }
   /**
    * Returns the value of the "email" attribute.
    *
    * @return The value of the "email" attribute.
    */
   public String getEmail() {
      return email;
   }
   /**
    * Returns the value of the "fatherName" attribute.
    *
    * @return The value of the "fatherName" attribute.
    */
   public String getFatherName() {
      return fatherName;
   }
   /**
    * Returns the value of the "gender" attribute.
    *
    * @return The value of the "gender" attribute.
    */
   public String getGender() {
      return gender;
   }
   /**
    * Returns the value of the "id" attribute.
    *
    * @return The value of the "id" attribute.
    */
   public Integer getId() {
      return id;
   }
   /**
    * Returns the value of the "motherName" attribute.
    *
    * @return The value of the "motherName" attribute.
    */
   public String getMotherName() {
      return motherName;
   }
   /**
    * Returns the value of the "name" attribute.
    *
    * @return The value of the "name" attribute.
    */
   public String getName() {
      return name;
   }
   /**
    * Returns the value of the "notes" attribute.
    *
    * @return The value of the "notes" attribute.
    */
   public String getNotes() {
      return notes;
   }
   /**
    * Returns the value of the "surname" attribute.
    *
    * @return The value of the "surname" attribute.
    */
   public String getSurname() {
      return surname;
   }
   /**
    * Returns the value of the "tracking" attribute.
    *
    * @return The value of the "tracking" attribute.
    */
   public Tracking getTracking() {
      return tracking;
   }
   /**
    * Returns the value of the "addresses" attribute.
    *
    * @return The value of the "addresses" attribute.
    */
   public List<Address> getAddresses() {
      return addresses;
   }
   
   /**
    * Returns the value of the "preferredLanguage" attribute.
    *
    * @return The value of the "preferredLanguage" attribute.
    */
   public Language getPreferredLanguage() {
      return preferredLanguage;
   }
   
   /**
    * Returns the value of the "telephones" attribute.
    *
    * @return The value of the "telephones" attribute.
    */
   public List<Telephone> getTelephones() {
      return telephones;
   }
   
   /**
    * Sets the value of the "dateOfBirth" attribute.
    *
    * @param dateOfBirth
    *     The value of the "dateOfBirth" attribute.
    */
   public void setDateOfBirth(Date dateOfBirth) {
      this.dateOfBirth = dateOfBirth;
   }
   /**
    * Sets the value of the "email" attribute.
    *
    * @param email
    *     The value of the "email" attribute.
    */
   public void setEmail(String email) {
      this.email = email;
   }
   /**
    * Sets the value of the "fatherName" attribute.
    *
    * @param fatherName
    *     The value of the "fatherName" attribute.
    */
   public void setFatherName(String fatherName) {
      this.fatherName = fatherName;
   }
   /**
    * Sets the value of the "gender" attribute.
    *
    * @param gender
    *     The value of the "gender" attribute.
    */
   public void setGender(String gender) {
      this.gender = gender;
   }
   /**
    * Sets the value of the "id" attribute.
    *
    * @param id
    *     The value of the "id" attribute.
    */
   public void setId(Integer id) {
      this.id = id;
   }
   /**
    * Sets the value of the "motherName" attribute.
    *
    * @param motherName
    *     The value of the "motherName" attribute.
    */
   public void setMotherName(String motherName) {
      this.motherName = motherName;
   }
   /**
    * Sets the value of the "name" attribute.
    *
    * @param name
    *     The value of the "name" attribute.
    */
   public void setName(String name) {
      this.name = name;
   }
   /**
    * Sets the value of the "notes" attribute.
    *
    * @param notes
    *     The value of the "notes" attribute.
    */
   public void setNotes(String notes) {
      this.notes = notes;
   }
   /**
    * Sets the value of the "surname" attribute.
    *
    * @param surname
    *     The value of the "surname" attribute.
    */
   public void setSurname(String surname) {
      this.surname = surname;
   }
   /**
    * Sets the value of the "tracking" attribute.
    *
    * @param tracking
    *     The value of the "tracking" attribute.
    */
   public void setTracking(Tracking tracking) {
      this.tracking = tracking;
   }
   
   /**
    * Sets the value of the "addresses" attribute.
    *
    * @param addresses"
    *     The value of the "addresses" attribute.
    */
   public void setAddresses(List<Address> addresses) {
      this.addresses = addresses;
   }

   /**
    * Sets the value of the "preferredLanguage" attribute.
    *
    * @param preferredLanguage"
    *     The value of the "preferredLanguage" attribute.
    */
   public void setPreferredLanguage(Language preferredLanguage) {
      this.preferredLanguage = preferredLanguage;
   }

   /**
    * Sets the value of the "telephones" attribute.
    *
    * @param telephones"
    *     The value of the "telephones" attribute.
    */
   public void setTelephones(List<Telephone> telephones) {
      this.telephones = telephones;
   }

}