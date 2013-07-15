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

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.model.buffers.HighlightedTextBuffer;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class MMicroPublication implements IMpSupportingElement {
	
	String id;
	
	private MMpStatement argues;
	private ArrayList<MMpRelationship> evidence = new ArrayList<MMpRelationship>();
	
	public MMicroPublication(HighlightedTextBuffer buffer) {
		argues = new MMpStatement(buffer);
	}
	
	public MMicroPublication(MTextQuoteSelector selector) {
		argues = new MMpStatement(selector);
	}

	public ArrayList<MMpRelationship> getEvidence() {
		return evidence;
	}
}
