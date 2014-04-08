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
public class JsOpenAnnotation extends JavaScriptObject {

	protected JsOpenAnnotation() {}

	public final native String getId() /*-{ return this['@id'];  }-*/;
	public final native String getType() /*-{ return this['@type']; }-*/;
	
	// AnnotatedAt and AnnotatedBy
	// ---------------------------
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
	
	// Targets
	// ---------------------------
	public final native boolean hasMultipleTargets() /*-{ 
		return this.hasTarget instanceof Array; 
	}-*/;	
	public final native JsArray<JavaScriptObject> getTargets() /*-{  
		return this.hasTarget; 
	}-*/;
	public final native JavaScriptObject getTarget() /*-{  
		return this.hasTarget; 
	}-*/;
	
	// Bodies
	// ---------------------------
	public final native boolean hasMultipleBodies() /*-{ 
		return this.hasBody instanceof Array; 
	}-*/;	
	public final native JsArray<JavaScriptObject> getBodies() /*-{  
		return this.hasBody; 
	}-*/;
	public final native JavaScriptObject getBody() /*-{  
		return this.hasBody; 
	}-*/;
}
