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

import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;
import org.mindinformatics.gwt.framework.model.agents.IAgent;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotopiaSerializerManager {

public IDomeo _domeo;
	
	private HashMap<String, IAnnotopiaSerializer> serializers = new HashMap<String, IAnnotopiaSerializer>();

	private AnnotopiaSerializerManager(IDomeo domeo) {
		_domeo = domeo;
		registerBasicSerializers();
	}
	
	private static AnnotopiaSerializerManager _instance;
	public static AnnotopiaSerializerManager getInstance(IDomeo domeo) {
		if(_instance==null) {
			_instance = new AnnotopiaSerializerManager(domeo);
		} 
		return _instance;
	}
	
	private void registerBasicSerializers() {
		serializers.put(MAnnotationSet.class.getName(), new SAnnotationSetSerializer());	
		serializers.put(IAgent.class.getName(), new SAgentSerializer());
		serializers.put(MAgentPerson.class.getName(), new SAgentPersonSerializer());
		serializers.put(MAgentSoftware.class.getName(), new SAgentSoftwareSerializer());
	}
	
	// ------------------------------------------------------------------------
	//  ANNOTATION SET
	// ------------------------------------------------------------------------
	public JSONObject serialize(MAnnotationSet annotationSet) {
		_domeo.getLogger().debug(this, "Serializing Annotation Set with localId " + annotationSet.getLocalId() + " and " + annotationSet.getAnnotations().size() + " items");
		try {
			IAnnotopiaSerializer serializer = (IAnnotopiaSerializer)serializers.get(MAnnotationSet.class.getName());
			if(serializer!=null) return serializer.serialize(this, annotationSet);
			else {
				_domeo.getLogger().exception(this, "Serializer for Annotation Set not found");
				return new JSONObject();
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while serializing the Annotation Set " + e.getMessage());
			return new JSONObject();
		}
	}
	
	// ------------------------------------------------------------------------
	//  AGENTS
	// ------------------------------------------------------------------------
	public JSONObject serialize(IAgent agent) {
		_domeo.getLogger().debug(this, "Serializing Agent with Uri " + agent.getUri() + " and type " + agent.getClass().getName());
		try {
			IAnnotopiaSerializer serializer = (IAnnotopiaSerializer) serializers.get(agent.getClass().getName());
			if(serializer!=null) {
				return serializer.serialize(this, agent);
			} else {
				_domeo.getLogger().warn(this, "Serializer for " + agent.getClass().getName() + " not found, applying default Agent serializer");
				IAnnotopiaSerializer defaultSerializer = (IAnnotopiaSerializer)  serializers.get(IAgent.class.getName());
				return defaultSerializer.serialize(this, agent);
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while serializing the Agent " + e.getMessage());
			return null;
		}
	}
	
	public JSONValue serialize(MAgentPerson person) {
		_domeo.getLogger().debug(this, "Serializing AgentPerson with uuid " + person.getUuid());
		try {
			IAnnotopiaSerializer serializer = (IAnnotopiaSerializer) serializers.get(person.getClass().getName());
			if(serializer!=null) {
				return serializer.serialize(this, person);
			} else {
				_domeo.getLogger().exception(this, "Serializer for Person not found");
				IAnnotopiaSerializer defaultSerializer = (IAnnotopiaSerializer)  serializers.get(MAgentPerson.class.getName());
				return defaultSerializer.serialize(this, person);
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while serializing the Person " + e.getMessage());
			return null;
		}
	}
	
	public JSONValue serialize(MAgentSoftware software) {
		_domeo.getLogger().debug(this, "Serializing AgentSoftware with uuid " + software.getUuid()+ " and type " + software.getClass().getName());
		try {
			IAnnotopiaSerializer serializer = (IAnnotopiaSerializer) serializers.get(software.getClass().getName());
			if(serializer!=null) {
				return serializer.serialize(this, software);
			} else {
				_domeo.getLogger().warn(this, "Serializer for " + software.getClass().getName() + " not found, applying default Software serializer");
				IAnnotopiaSerializer defaultSerializer = (IAnnotopiaSerializer)  serializers.get(MAgentSoftware.class.getName());
				return defaultSerializer.serialize(this, software);
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while serializing the Software " + e.getMessage());
			return null;
		}
	}
}
