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

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsAnnotationProvenance extends JavaScriptObject {

	protected JsAnnotationProvenance() {}

	public final native String getId() /*-{ return this['@id'];  }-*/;
	public final native String getType() /*-{ return this['@type']; }-*/;
	

	public final native String getAnnotatedAt() /*-{ 
		return this.annotatedAt; 
	}-*/;
	public final Date getFormattedAnnotatedAt() {
		return ApplicationUtils.fullfmt2.parse(getAnnotatedAt().trim());
	} 	
	public final native Object getAnnotatedBy() /*-{ 
		return this.annotatedBy; 
	}-*/;
	public final native boolean isAnnotatedByString() /*-{ 
		return this.annotatedBy.constructor === String;
	}-*/;
	public final native String getAnnotatedByAsString() /*-{ 
		return this.annotatedBy; 
	}-*/;
	public final native boolean isAnnotatedByObject() /*-{ 
		return this.annotatedBy.constructor === Object;
	}-*/;
	public final native JsAnnotopiaAgent getAnnotatedByAsObject() /*-{ 
		return this.annotatedBy; 
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
	
	
	public final native JsAnnotopiaAgent getCreatedBy() /*-{ 
		return this.createdBy; 
	}-*/;
	public final native String getCreatedOn() /*-{ 
		return this.createdAt; 
	}-*/;
	public final Date getFormattedCreatedOn() {
		return ApplicationUtils.fullfmt2.parse(getCreatedOn().trim());
	} 
	
//	public final native JsAnnotopiaAgent getCreatedWith() /*-{ 
//		return this.createdWith; 
//	}-*/;
//	
//	public final native JsAnnotopiaAgent getLastSavedBy() /*-{ 
//		return this.lastSavedBy; 
//	}-*/;
//	public final native String getLastSavedOn() /*-{ 
//		return this.lastSavedOn; 
//	}-*/;
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
