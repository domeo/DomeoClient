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
package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsMicroPublication extends JavaScriptObject {

	protected JsMicroPublication() {}
	
	public final native String getId() /*-{ return this['@id'];  }-*/;
	public final native String getType() /*-{ return this['@type']; }-*/;
	
	public final native String getArguesAsString() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpArgues]; 
	}-*/;
	public final native boolean isArguesString() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpArgues].constructor === String;
	}-*/;
	public final native JsMpAssertion getArguesAsObject() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpArgues]; 
	}-*/;
	public final native boolean isArguesObject() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpArgues].constructor === Object;
	}-*/;
	public final native boolean isAssertString() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpAsserts].constructor === String;
	}-*/;
	public final native boolean isAssertObject() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpAsserts].constructor === Object;
	}-*/;
	public final native JsMpAssertion getAssertAsObject() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpAsserts]; 
	}-*/;
	public final native String getAssertAsString() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpAsserts]; 
	}-*/;
	public final native JsArray<JsMpAssertion>  getAsserts() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpAsserts]; 
	}-*/;
	public final native JsArray<JavaScriptObject>  getAssertsAsObject() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpAsserts]; 
	}-*/;
	public final native Object  getAssertsAsObject2() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpAsserts]; 
	}-*/;
}
