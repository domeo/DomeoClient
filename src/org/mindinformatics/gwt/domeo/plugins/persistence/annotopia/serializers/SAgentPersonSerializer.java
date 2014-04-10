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

import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * This class serializes Agents to Annotopia JSON format.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class SAgentPersonSerializer extends SAgentSerializer implements IAnnotopiaSerializer {

	@Override
	public JSONObject serialize(AnnotopiaSerializerManager manager, Object obj) {
		MAgentPerson person = (MAgentPerson) obj;
		JSONObject jsonPerson = initializeAgent(manager, person);
		jsonPerson.put("title", new JSONString(person.getTitle()));
		jsonPerson.put("email", new JSONString(person.getEmail()));
		jsonPerson.put("firstname", new JSONString(person.getFirstName()));
		jsonPerson.put("middlename", new JSONString(person.getMiddleName()));
		jsonPerson.put("lastname", new JSONString(person.getLastName()));
		jsonPerson.put("picture", new JSONString(person.getPicture()));
		return jsonPerson;
	}
}
