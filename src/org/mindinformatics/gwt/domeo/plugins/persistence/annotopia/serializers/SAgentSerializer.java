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
import org.mindinformatics.gwt.framework.component.agents.model.MAgent;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * This class serializes Agents to Annotopia JSON format.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class SAgentSerializer extends AAnnotopiaSerializer implements IAnnotopiaSerializer {

	/**
	 * Allows to initialize the generic properties of every Agent. This method is
	 * used by all the extensions of MAgent.
	 * @param manager	The serializer manager
	 * @param agent		The Agent to serialize
	 * @return The Agent in JSON format
	 */
	protected JSONObject initializeAgent(AnnotopiaSerializerManager manager, MAgent agent) {
		JSONObject jsonAgent = new JSONObject();
		jsonAgent.put(IDomeoOntology.generalId, new JSONString(agent.getUri()!=null?agent.getUri():""));
		jsonAgent.put(IDomeoOntology.generalType, new JSONString(agent.getAgentType()));
		//jsonAgent.put(IDomeoOntology.uuid, nonNullable(agent.getUuid())); // Used???
		
		jsonAgent.put("label", nullable(agent.getName()));	
		jsonAgent.put("name", nullable(agent.getName()));
		if(agent.getHomepage()!=null)jsonAgent.put("homepage", nullable(agent.getHomepage()));	
		return jsonAgent;
	}

	@Override
	public JSONObject serialize(AnnotopiaSerializerManager manager, Object obj) {
		JSONObject jsonAgent = initializeAgent(manager, (MAgent) obj);		
		return jsonAgent;
	}
}
