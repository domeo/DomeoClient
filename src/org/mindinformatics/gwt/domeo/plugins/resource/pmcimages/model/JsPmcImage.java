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
package org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsPmcImage  extends JavaScriptObject {

	protected JsPmcImage() {}
	
	public final native String getName() /*-{ return this["uysie:hasFileName"]; }-*/;
	public final native String getImageId() /*-{ return this["uysie:hasImageID"]; }-*/;
	public final native String getTitle() /*-{ return this["uysie:hasTitle"]; }-*/;
	public final native String getCaption() /*-{ return this["uysie:hasCaption"]; }-*/;
	public final native String getFullText() /*-{ return this["uysie:hasFullText"]; }-*/;
}
