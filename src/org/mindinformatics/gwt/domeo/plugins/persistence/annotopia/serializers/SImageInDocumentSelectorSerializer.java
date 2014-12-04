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
package org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.serializers;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MImageInDocumentSelector;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * This class serializes Agents to Annotopia JSON format.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class SImageInDocumentSelectorSerializer extends AAnnotopiaSerializer implements IAnnotopiaSerializer {

	@Override
	public JSONObject serialize(AnnotopiaSerializerManager manager, Object obj) {
		MImageInDocumentSelector selector = (MImageInDocumentSelector) obj;
		
		JSONObject jsonSpecificResource = new JSONObject();
		
		jsonSpecificResource.put(IDomeoOntology.generalId, new JSONString(selector.getUri()));
		jsonSpecificResource.put(IDomeoOntology.generalType, new JSONString("oa:SpecificResource"));
		//jsonAgent.put(IDomeoOntology.uuid, nonNullable(agent.getUuid())); // Used???
		
		JSONObject source = new JSONObject();
		source.put(IDomeoOntology.generalId, new JSONString(selector.getTarget().getUrl()));
		source.put(IDomeoOntology.generalType, new JSONString("dctypes:StillImage"));		
		source.put("format", new JSONString("image/jpeg"));		
		manager.serializeTitle(source);
		
		jsonSpecificResource.put("hasSource", source);
		
		manager.serializeExpression(source);
		

		jsonSpecificResource.put("hasScope", new JSONString(selector.getContext().getUrl()));

		return jsonSpecificResource;
	}
}
