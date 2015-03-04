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


import java.util.Date;

import org.restlet.test.ext.odata.deepexpand.model.FinancialSource;
import org.restlet.test.ext.odata.deepexpand.model.JobPart;
import org.restlet.test.ext.odata.deepexpand.model.Registration;

/**
* Generated by the generator tool for the OData extension for the Restlet framework.<br>
*
* @see <a href="http://praktiki.metal.ntua.gr/CoopOData/CoopOData.svc/$metadata">Metadata of the target OData service</a>
*
*/
public class Payment {

    private double amount;
    private String comment;
    private Date endDate;
    private Integer id;
    private Date paymentDate;
    private Date startDate;
    private String state;
    private String type;
    private Tracking tracking;
    private JobPart jobPart;
    private Registration registration;
    private FinancialSource source;

    /**
     * Constructor without parameter.
     * 
     */
    public Payment() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param id
     *            The identifiant value of the entity.
     */
    public Payment(Integer id) {
        this();
        this.id = id;
    }

   /**
    * Returns the value of the "amount" attribute.
    *
    * @return The value of the "amount" attribute.
    */
   public double getAmount() {
      return amount;
   }
   /**
    * Returns the value of the "comment" attribute.
    *
    * @return The value of the "comment" attribute.
    */
   public String getComment() {
      return comment;
   }
   /**
    * Returns the value of the "endDate" attribute.
    *
    * @return The value of the "endDate" attribute.
    */
   public Date getEndDate() {
      return endDate;
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
    * Returns the value of the "paymentDate" attribute.
    *
    * @return The value of the "paymentDate" attribute.
    */
   public Date getPaymentDate() {
      return paymentDate;
   }
   /**
    * Returns the value of the "startDate" attribute.
    *
    * @return The value of the "startDate" attribute.
    */
   public Date getStartDate() {
      return startDate;
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
    * Returns the value of the "type" attribute.
    *
    * @return The value of the "type" attribute.
    */
   public String getType() {
      return type;
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
    * Returns the value of the "jobPart" attribute.
    *
    * @return The value of the "jobPart" attribute.
    */
   public JobPart getJobPart() {
      return jobPart;
   }
   
   /**
    * Returns the value of the "registration" attribute.
    *
    * @return The value of the "registration" attribute.
    */
   public Registration getRegistration() {
      return registration;
   }
   
   /**
    * Returns the value of the "source" attribute.
    *
    * @return The value of the "source" attribute.
    */
   public FinancialSource getSource() {
      return source;
   }
   
   /**
    * Sets the value of the "amount" attribute.
    *
    * @param amount
    *     The value of the "amount" attribute.
    */
   public void setAmount(double amount) {
      this.amount = amount;
   }
   /**
    * Sets the value of the "comment" attribute.
    *
    * @param comment
    *     The value of the "comment" attribute.
    */
   public void setComment(String comment) {
      this.comment = comment;
   }
   /**
    * Sets the value of the "endDate" attribute.
    *
    * @param endDate
    *     The value of the "endDate" attribute.
    */
   public void setEndDate(Date endDate) {
      this.endDate = endDate;
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
    * Sets the value of the "paymentDate" attribute.
    *
    * @param paymentDate
    *     The value of the "paymentDate" attribute.
    */
   public void setPaymentDate(Date paymentDate) {
      this.paymentDate = paymentDate;
   }
   /**
    * Sets the value of the "startDate" attribute.
    *
    * @param startDate
    *     The value of the "startDate" attribute.
    */
   public void setStartDate(Date startDate) {
      this.startDate = startDate;
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
    * Sets the value of the "type" attribute.
    *
    * @param type
    *     The value of the "type" attribute.
    */
   public void setType(String type) {
      this.type = type;
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
    * Sets the value of the "jobPart" attribute.
    *
    * @param jobPart"
    *     The value of the "jobPart" attribute.
    */
   public void setJobPart(JobPart jobPart) {
      this.jobPart = jobPart;
   }

   /**
    * Sets the value of the "registration" attribute.
    *
    * @param registration"
    *     The value of the "registration" attribute.
    */
   public void setRegistration(Registration registration) {
      this.registration = registration;
   }

   /**
    * Sets the value of the "source" attribute.
    *
    * @param source"
    *     The value of the "source" attribute.
    */
   public void setSource(FinancialSource source) {
      this.source = source;
   }

}