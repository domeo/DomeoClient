/*
 * Copyright 2013 Massachusetts General Hospital
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.mindinformatics.gwt.domeo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.framework.model.agents.IAgent;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public abstract class MAnnotation implements Serializable, IsSerializable,
		IUniquelyIdentifiable {

	DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yy h:mma");
	
	// Transient fields: these are not going to be stored in the 
	// Annotation Set graph
	private Long localId;				// sent to the server for matching saved items
    private Boolean hasChanged = true;	// flag for triggering the saving
    private Boolean newVersion = true;  // it will save as new version
	
    // Persistent mutable fields: these are going to be stored in the 
    // Annotation Set sometimes after being modified by the server
	private String lineageUri;		// Lineage Uniform Resource Identifier
	private String individualUuid;	// Individual Universal Unique Identifier (USED?)
	private String individualUri;	// Individual Uniform Resource Identifier
    private String versionNumber;	 	// computed by the server
    private String previousVersion;	  	// computed by the server
    private Date lastSavedOn;
   
    // Persistent immutable fields: these are going to be stored in the 
    // Annotation Set without being modified by the server
    private Date createdOn;
    private IAgent creator;
    private ISoftware tool;
    private ArrayList<MSelector> selectors = new ArrayList<MSelector>();
    private ArrayList<MAnnotation> annotatedBy = new ArrayList<MAnnotation>();
   
    
    public abstract String getAnnotationType();
    public abstract String getLabel();

    private transient int Y;
	public int getY() { return Y; }
	public void setY(int y) { Y = y; }

	public String getFormattedCreationDate() {
		return fmt.format(createdOn);
	}
	public String getFormattedLasSavedDate() {
		if(lastSavedOn!=null) return fmt.format(lastSavedOn);
		return "";
	}

	public String getLineageUri() {
		return lineageUri;
	}
	public void setLineageUri(String lineageUri) {
		this.lineageUri = lineageUri;
	}
	public String getIndividualUri() {
		return individualUri;
	}
	public void setIndividualUri(String individualUri) {
		this.individualUri = individualUri;
	}
	public String getUuid() {
		return individualUuid;
	}
	public void setUuid(String individualUuid) {
		this.individualUuid = individualUuid;
	}
	public Long getLocalId() {
		return localId;
	}
	public void setLocalId(Long localId) {
		this.localId = localId;
	}
	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getPreviousVersion() {
		return previousVersion;
	}
	public void setPreviousVersion(String previousVersion) {
		this.previousVersion = previousVersion;
	}
	public Boolean getHasChanged() {
		return hasChanged;
	}
	public void setHasChanged(Boolean hasChanged) {
		this.hasChanged = hasChanged;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public void setLastSavedOn(Date lastSavedOn) {
		this.lastSavedOn = lastSavedOn;
	}
	public Date getLastSavedOn() {
		return lastSavedOn;
	}
	public IAgent getCreator() {
		return creator;
	}
	public void setCreator(IAgent creator) {
		this.creator = creator;
	}
	public ISoftware getTool() {
		return tool;
	}
	public void setTool(ISoftware tool) {
		this.tool = tool;
	}
	public ArrayList<MSelector> getSelectors() {
		return selectors;
	}
	public MSelector getSelector() {
		if(selectors.size()>0)
			return selectors.get(0);
		else return null;
	}
	public void setSelector(MSelector selector) {
		selectors.clear();
		selectors.add(selector);
	}
	public void addSelector(MSelector selector) {
		selectors.add(selector);
	}
	public ArrayList<MAnnotation> getAnnotatedBy() {
		return annotatedBy;
	}
	public void addAnnotatedBy(MAnnotation annotation) {
		annotatedBy.add(annotation);
	}
	public Boolean getNewVersion() {
		return newVersion;
	}
	public void setNewVersion(Boolean newVersion) {
		this.newVersion = newVersion;
	}
}
