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

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IOpenAnnotation {

	public static final String ANNOTATION = "oa:Annotation";
	public static final String CONTENT_AS_TEXT = "cnt:ContentAsText";
	public static final String SPECIFIC_RESOURCE = "oa:SpecificResource";
	public static final String TEXT_QUOTE_SELECTOR = "oa:TextQuoteSelector";
	
	public static final String HAS_TARGET = "hasTarget";
	
	public static final String MOTIVATION_COMMENTING = "oa:commenting";
	public static final String MOTIVATION_HIGHLIGHTED = "oa:highlighting";
}
