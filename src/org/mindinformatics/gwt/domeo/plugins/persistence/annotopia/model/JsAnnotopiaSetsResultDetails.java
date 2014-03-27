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
import com.google.gwt.core.client.JsArray;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsAnnotopiaSetsResultDetails extends JavaScriptObject {

	protected JsAnnotopiaSetsResultDetails() {}
	
	public final native String getDuration() /*-{  return this.duration; }-*/;
	public final native String getTotal() /*-{  return this.total; }-*/;
	public final native String getPages() /*-{  return this.pages; }-*/;
	public final native String getOffset() /*-{  return this.offset; }-*/;
	public final native String getMax() /*-{  return this.max; }-*/;
	public final native JsArray<JsAnnotopiaAnnotationSetGraphs> getSets() /*-{  return this.sets; }-*/;
}
