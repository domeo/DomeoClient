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

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsTextQuoteSelector extends JavaScriptObject {

	protected JsTextQuoteSelector() {}
	
	public final native String getId() /*-{ return this['@id'];  }-*/;
	public final native String getType() /*-{ return this['@type']; }-*/;
	
	// Quote
	public final native String getExact() /*-{ 
		return this.exact; 
	}-*/;
	public final native String getPrefix() /*-{ 
		return this.prefix; 
	}-*/;
	public final native String getSuffix() /*-{ 
		return this.suffix; 
	}-*/;
}
