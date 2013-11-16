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
package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model;

import org.mindinformatics.gwt.domeo.model.MAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MMicroPublicationAnnotation extends MAnnotation {

	protected static final String LABEL = "Micropublication";
	public static final String TYPE = "ao:MicroPublicationAnnotation";
	public static final String BODY_TYPE = "domeo:AntibodyUsage";
	
	private MMicroPublication microPublication;
	
	public MMicroPublication getMicroPublication() {
		return microPublication;
	}

	public void setMicroPublication(MMicroPublication microPublication) {
		this.microPublication = microPublication;
	}

	@Override
	public String getLabel() {
		return LABEL;
	}
	
	@Override
	public String getAnnotationType() {
		return TYPE;
	}
}
