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
import java.util.HashMap;

import org.mindinformatics.gwt.domeo.model.ICache;
import org.mindinformatics.gwt.domeo.model.MAnnotation;

import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class MicroPublicationCache implements ICache {

	private HashMap<String, MMicroPublicationAnnotation> annotations 
		= new HashMap<String, MMicroPublicationAnnotation>();
	
	@Override
	public ArrayList<MAnnotation> getCachedAnnotations() {
		ArrayList<MAnnotation> a = new ArrayList<MAnnotation>();
		a.addAll(annotations.values());
		return a;
	}
	
	@Override
	public boolean cacheAnnotation(MAnnotation annotation) {
		// TODO Auto-generated method stub
		if(annotations.containsKey(annotation.getUuid()) ||
			!(annotation instanceof MMicroPublicationAnnotation)) return false;
		else {
			annotations.put(annotation.getUuid(), (MMicroPublicationAnnotation) annotation);
			return true;
		}	
	}

	@Override
	public void resetCache() {
		annotations = new HashMap<String, MMicroPublicationAnnotation>();
	}

	@Override
	public String getCachedType() {
		return MMicroPublicationAnnotation.class.getName();
	}
}
