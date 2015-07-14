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

import org.mindinformatics.gwt.framework.src.Utils;

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
		return this.annotations.length || 0;
	}-*/;
	
	public final native boolean isAnnotationsArray() /*-{ 
		return Object.prototype.toString.call(this.annotations) === '[object Array]';
	}-*/;
	public final native JsArray<JavaScriptObject> getAnnotations() /*-{  
		return this.annotations; 
	}-*/;
	public final native JavaScriptObject getAnnotation() /*-{  
		return this.annotations; 
	}-*/;
	public final native boolean hasAnnotation() /*-{  
		return this.annotations!=null; 
	}-*/;
	public final native int annotationCounts() /*-{  
		if(Object.prototype.toString.call(this.annotations) === '[object Array]') return this.annotations.length; 
		if(this.annotations!=null)  return 1
		return 0
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
	public final native Object getCreatedBy() /*-{ 
		return this.createdBy; 
	}-*/;
	public final native boolean isCreatedByString() /*-{ 
		return this.createdBy.constructor === String;
	}-*/;
	public final native String getCreatedByAsString() /*-{ 
		return this.createdBy; 
	}-*/;
	public final native boolean isCreatedByObject() /*-{ 
		return this.createdBy.constructor === Object;
	}-*/;
	public final native JsAnnotopiaAgent getCreatedByAsObject() /*-{ 
		return this.createdBy; 
	}-*/;
	
	public final native Object getCreatedWith() /*-{ 
		return this.createdWith; 
	}-*/;
	public final native boolean isCreatedWithString() /*-{ 
		return this.createdWith.constructor === String;
	}-*/;
	public final native String getCreatedWithAsString() /*-{ 
		return this.createdWith; 
	}-*/;
	public final native boolean isCreatedWithObject() /*-{ 
		return this.createdWith.constructor === Object;
	}-*/;
	public final native JsAnnotopiaAgent getCreatedWithAsObject() /*-{ 
		return this.createdWith; 
	}-*/;
	
	public final native String getLastSavedOn() /*-{ 
		return this.lastUpdateOn; 
	}-*/;
	
	public final native JsAnnotopiaAgent getLastSavedBy() /*-{ 
		return this.lastSavedBy; 
	}-*/;
	public final native boolean isLastSavedByString() /*-{ 
		return this.lastSavedBy.constructor === String;
	}-*/;
	public final native String getLastSavedByAsString() /*-{ 
		return this.lastSavedBy; 
	}-*/;
	public final native boolean isLastSavedByObject() /*-{ 
		return this.lastSavedBy.constructor === Object;
	}-*/;
	public final native JsAnnotopiaAgent getLastSavedByAsObject() /*-{ 
		return this.lastSavedBy; 
	}-*/;
	
	public final Date getFormattedCreatedOn() {
		return Utils.fullfmt2.parse(getCreatedOn().trim());
	} 
	
	public final Date getFormattedLastSavedOn() {
		return Utils.fullfmt2.parse(getLastSavedOn());
	} 
	
	public final Date getFormattedCreatedOn2() {
		return Utils.fullfmt.parse(getCreatedOn().trim());
	} 
	
	public final Date getFormattedLastSavedOn2() {
		return Utils.fullfmt.parse(getLastSavedOn());
	} 
	
	public final native String getVersionNumber() /*-{ 
		return this.version; 
	}-*/;
	public final native String getPreviousVersion() /*-{ 
		return this.previousVersion; 
	}-*/;
}
