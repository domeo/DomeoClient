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

import java.util.Collection;
import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.IDomeo;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class TextMiningRegistry {

	private IDomeo _domeo;
	private HashMap<String, ITextMiningConnector> textMiningConnectors =
		new HashMap<String, ITextMiningConnector>();
	
	// Singleton
	private TextMiningRegistry() {}
	private static TextMiningRegistry instance;
	public static TextMiningRegistry getInstance(IDomeo domeo) {
		if(instance==null) {
			instance = new TextMiningRegistry();
			instance._domeo = domeo;
		}
		return instance;
	}
	
	public int size() {
		return textMiningConnectors.size();
	}
	
	public ITextMiningConnector getTextMiningServiceByName(String name) {
		return textMiningConnectors.get(name);
	}
	
	public Collection<ITextMiningConnector> getTextMiningServices() {
		return textMiningConnectors.values();
	}
	
	public void registerTextMiningService(ITextMiningConnector connector) {
		_domeo.getLogger().info(this, "Registration of text mining service " + connector.getClass().getName());
		if(textMiningConnectors.get(connector.getAnnotatorLabel())!=null) {
			_domeo.getLogger().warn(this, "Text mining service already registered with name " + connector.getAnnotatorLabel());
		}
		textMiningConnectors.put(connector.getAnnotatorLabel(), connector);		
	}
}
