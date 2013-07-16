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

import org.mindinformatics.gwt.domeo.model.buffers.HighlightedTextBuffer;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class MMpStatement extends MMpElement {
	
	private HighlightedTextBuffer buffer;
	private String text;
	
	public MMpStatement() {}
	
	public MMpStatement(HighlightedTextBuffer buffer) {
		this.buffer = buffer;
	}
	
	public MMpStatement(MTextQuoteSelector selector) {
		this.setSelector(selector);
	}
	
	public HighlightedTextBuffer getBuffer() {
		return buffer;
	}

	public String getText() {
		if(text==null || text.trim().length()==0) {
			if(getSelector()!=null) return ((MTextQuoteSelector)getSelector()).getExact();
			else return buffer.getExact();
		}
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
