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
package org.mindinformatics.gwt.domeo.component.textmining.src;

import org.mindinformatics.gwt.domeo.client.IDomeo;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ITextMiningConnector {

	/**
	 * Return the annotator label that is used for selection.
	 * @return	The label of the annotator service.
	 */
	public String getAnnotatorLabel();
	
	public Widget getAnnotatorPanel();
	
	/**
	 * Standard selection of the connector
	 * @param domeo	The main aplication instance
	 * @return The selected connector
	 */
	public boolean selectConnector(IDomeo domeo);
	
	/**
	 * Annotation service call.
	 * @param mgr		The manager who takes care of unmarshalling the results
	 * @param url		The url of the annotated document
	 * @param content	The content to annotate
	 * @param params	The parameters
	 */
	 public void annotate(ITextminingRequestCompleted completionCallback, String url, String content, String... params) throws IllegalArgumentException;
}
