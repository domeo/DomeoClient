/*
 * Copyright 2014 Massachusetts General Hospital
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
package org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model;

import java.util.Date;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class MAnnotopiaAnnotationSet {

	private String id;
	private String type;
	private String label;
	private String description;
	
	int numberAnnoations;
	
	private boolean isLocked;
	private boolean isVisible;
	
	private Date createdOn;
	private Date lastSavedOn;
	
	private IAnnotopiaAgent createdBy;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isLocked() {
		return isLocked;
	}
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getLastSavedOn() {
		return lastSavedOn;
	}
	public void setLastSavedOn(Date lastSavedOn) {
		this.lastSavedOn = lastSavedOn;
	}
	public int getNumberAnnoations() {
		return numberAnnoations;
	}
	public void setNumberAnnoations(int numberAnnoations) {
		this.numberAnnoations = numberAnnoations;
	}
	public IAnnotopiaAgent getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(IAnnotopiaAgent createdBy) {
		this.createdBy = createdBy;
	}
}
