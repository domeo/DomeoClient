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

import org.mindinformatics.gwt.framework.src.ApplicationUtils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsAnnotopiaAnnotationSetSummary extends JavaScriptObject {

	protected JsAnnotopiaAnnotationSetSummary() {}
	
	public final String getType() {
		return "Annotation Set";
	}
	
	public final String getTypeName() {
		return "Annotation Set";
	}

	public final String getDisplayName() {
		return "Annotation Set";
	}
	
	public final native int getNumberOfAnnotationItems() /*-{ 
		return this.annotations.length;
	}-*/;
	
	public final native JsArray<JavaScriptObject> getAnnotations() /*-{  
		return this['annotations']; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  Identity
	// ------------------------------------------------------------------------
	public final native String getId() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  General (RDFS and Dublin Core Terms
	// ------------------------------------------------------------------------
	public final native String getLabel() /*-{ 
		return this.label; 
	}-*/;
	public final native String getDescription() /*-{ 
		return this.description; 
	}-*/;
	public final native String getDescription2() /*-{ 
		return this.description2; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  PAV (Provenance, Authoring and Versioning) Ontology
	// ------------------------------------------------------------------------
	public final native String getCreatedOn() /*-{ 
		return this.createdAt; 
	}-*/;
	public final native JsAnnotopiaAgent getCreatedBy() /*-{ 
		return this.createdBy; 
	}-*/;
	public final native JsAnnotopiaAgent getCreatedWith() /*-{ 
		return this.createdWith; 
	}-*/;
	public final native String getLastSavedOn() /*-{ 
		return this.lastSavedOn; 
	}-*/;
	public final native JsAnnotopiaAgent getLastSavedBy() /*-{ 
		return this.lastSavedBy; 
	}-*/;
	
	public final Date getFormattedCreatedOn() {
		return ApplicationUtils.fullfmt2.parse(getCreatedOn().trim());
	} 
	
	public final Date getFormattedLastSavedOn() {
		return ApplicationUtils.fullfmt2.parse(getLastSavedOn());
	} 
	
	public final native String getVersionNumber() /*-{ 
		return this.versionNumber; 
	}-*/;
	public final native String getPreviousVersion() /*-{ 
		return this.previousVersion; 
	}-*/;
}
