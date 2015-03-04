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

import org.restlet.test.ext.odata.deepexpand.model.Group;
import org.restlet.test.ext.odata.deepexpand.model.JobPart;
import org.restlet.test.ext.odata.deepexpand.model.JobPosting;
import org.restlet.test.ext.odata.deepexpand.model.Professor;

/**
* Generated by the generator tool for the OData extension for the Restlet framework.<br>
*
* @see <a href="http://praktiki.metal.ntua.gr/CoopOData/CoopOData.svc/$metadata">Metadata of the target OData service</a>
*
*/
public class Job {

    private String comments;
    private Date endDate;
    private Integer id;
    private Date startDate;
    private String state;
    private Tracking tracking;
    private Group group;
    private List<JobPart> jobParts = new ArrayList<JobPart>();
    private JobPosting jobPosting;
    private Professor supervisingProfessor;

    /**
     * Constructor without parameter.
     * 
     */
    public Job() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param id
     *            The identifiant value of the entity.
     */
    public Job(Integer id) {
        this();
        this.id = id;
    }

   /**
    * Returns the value of the "comments" attribute.
    *
    * @return The value of the "comments" attribute.
    */
   public String getComments() {
      return comments;
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
    * Returns the value of the "tracking" attribute.
    *
    * @return The value of the "tracking" attribute.
    */
   public Tracking getTracking() {
      return tracking;
   }
   /**
    * Returns the value of the "group" attribute.
    *
    * @return The value of the "group" attribute.
    */
   public Group getGroup() {
      return group;
   }
   
   /**
    * Returns the value of the "jobParts" attribute.
    *
    * @return The value of the "jobParts" attribute.
    */
   public List<JobPart> getJobParts() {
      return jobParts;
   }
   
   /**
    * Returns the value of the "jobPosting" attribute.
    *
    * @return The value of the "jobPosting" attribute.
    */
   public JobPosting getJobPosting() {
      return jobPosting;
   }
   
   /**
    * Returns the value of the "supervisingProfessor" attribute.
    *
    * @return The value of the "supervisingProfessor" attribute.
    */
   public Professor getSupervisingProfessor() {
      return supervisingProfessor;
   }
   
   /**
    * Sets the value of the "comments" attribute.
    *
    * @param comments
    *     The value of the "comments" attribute.
    */
   public void setComments(String comments) {
      this.comments = comments;
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
    * Sets the value of the "tracking" attribute.
    *
    * @param tracking
    *     The value of the "tracking" attribute.
    */
   public void setTracking(Tracking tracking) {
      this.tracking = tracking;
   }
   
   /**
    * Sets the value of the "group" attribute.
    *
    * @param group"
    *     The value of the "group" attribute.
    */
   public void setGroup(Group group) {
      this.group = group;
   }

   /**
    * Sets the value of the "jobParts" attribute.
    *
    * @param jobParts"
    *     The value of the "jobParts" attribute.
    */
   public void setJobParts(List<JobPart> jobParts) {
      this.jobParts = jobParts;
   }

   /**
    * Sets the value of the "jobPosting" attribute.
    *
    * @param jobPosting"
    *     The value of the "jobPosting" attribute.
    */
   public void setJobPosting(JobPosting jobPosting) {
      this.jobPosting = jobPosting;
   }

   /**
    * Sets the value of the "supervisingProfessor" attribute.
    *
    * @param supervisingProfessor"
    *     The value of the "supervisingProfessor" attribute.
    */
   public void setSupervisingProfessor(Professor supervisingProfessor) {
      this.supervisingProfessor = supervisingProfessor;
   }

}