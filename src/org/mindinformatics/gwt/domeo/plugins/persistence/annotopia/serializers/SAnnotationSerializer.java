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

import java.util.List;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.framework.src.ApplicationUtils;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * This class serializes Agents to Annotopia JSON format.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class SAnnotationSerializer extends AAnnotopiaSerializer implements IAnnotopiaSerializer {

	/**
	 * Allows to initialize the generic properties of every Annotation. This method is
	 * used by all the extensions of Annotation.
	 * @param manager	The serializer manager
	 * @param ann		The annotation to serialize
	 * @return The Annotation in JSON format
	 */
	protected JSONObject initializeAnnotation(AnnotopiaSerializerManager manager, MAnnotation ann) {
		JSONObject jsonAnnotation = new JSONObject();
		jsonAnnotation.put(IRdfsOntology.type, new JSONString("oa:Annotation"));
		if(ann.getAnnotationType().equals("ao:Highlight"))
			jsonAnnotation.put("motivatedBy", new JSONString("oa:highlighting"));
		else if(ann.getAnnotationType().equals("ao:PostIt")) {
			jsonAnnotation.put("motivatedBy", new JSONString("oa:commenting"));
		}
		
		jsonAnnotation.put(IRdfsOntology.id, nonNullable(ann.getIndividualUri()));
		jsonAnnotation.put(IDomeoOntology.transientLocalId, nonNullable(ann.getLocalId()));
		
		//jsonAnnotation.put(IPavOntology.createdBy, new JSONString(ann.getCreator()!=null?ann.getCreator().getUri():""));
		if(ann.getCreator()!=null) {
			//jsonAnnotation.put("createdBy", manager.serialize(ann.getCreator()));
			jsonAnnotation.put("annotatedBy", manager.serialize(ann.getCreator()));
		}		
		
		//jsonAnnotation.put(IPavOntology.createdBy, serializeAgent(manager, ann.getCreator()));
		//if(ann.getCreator()!=null) manager.addAgentToSerialize(ann.getCreator());
		
		if(ann.getCreatedOn()!=null) {
			//jsonAnnotation.put("createdOn", nonNullable(ann.getCreatedOn()));
			jsonAnnotation.put("annotatedAt", new JSONString(ApplicationUtils.fullfmt2.format(ann.getCreatedOn())));
		}
		
		//jsonAnnotation.put(IPavOntology.createdWith, new JSONString(ann.getTool()!=null?ann.getTool().getUri():""));
		if(ann.getTool()!=null) {
			jsonAnnotation.put("createdWith", manager.serialize(ann.getTool()));
		}
		
		//jsonAnnotation.put(IPavOntology.createdWith, serializeAgent(manager, ann.getTool()));
		//if(ann.getTool()!=null) manager.addAgentToSerialize(ann.getTool());
			
		jsonAnnotation.put("lastUpdateOn", nullable(ann.getLastSavedOn()));
		//annotationJson.put(IPavOntology.createdWith, new JSONString(ann.getTool()!=null?ann.getTool().getUri():""));
		//if(ann.getTool()!=null) manager.addAgentToSerialize(ann.getTool());
		//annotationJson.put(IPavOntology.createdWith, new JSONString(ann.getTool()!=null?ann.getTool().getUri():""));
		
		// These translate null values into blank strings
		jsonAnnotation.put("label", nullable(ann.getLabel()));	
		jsonAnnotation.put("lineageUri", nullable(ann.getLineageUri()));
		jsonAnnotation.put("versionNumber", nullable(ann.getVersionNumber()));
		jsonAnnotation.put("previousVersion", nullable(ann.getPreviousVersion()));
		
		jsonAnnotation.put(IDomeoOntology.transientHasChanged, new JSONString(Boolean.toString(ann.getHasChanged())));
		jsonAnnotation.put(IDomeoOntology.transientNewVersion, new JSONString(Boolean.toString(ann.getNewVersion())));
		jsonAnnotation.put(IDomeoOntology.transientHasChanged, new JSONString(ann.getHasChanged()+"")); //???
		
		jsonAnnotation.put("hasTarget", encodeSelectors(manager, ann));
		return jsonAnnotation;
	}
	
	/**
	 * Encodes selectors in JSON format.
	 * @param manager	The serializer manager
	 * @param ann		The annotation to serialize
	 * @return The Selectors in JSON format
	 */
	protected JSONArray encodeSelectors(AnnotopiaSerializerManager manager, MAnnotation ann) {
		JSONArray jsonSelectors = new JSONArray();
		List<MSelector> selectorsList = ann.getSelectors();
		for(int i=0; i<selectorsList.size(); i++) {
			jsonSelectors.set(i, manager.serialize(selectorsList.get(i)));
		}
		return jsonSelectors;
	}

	@Override
	public JSONObject serialize(AnnotopiaSerializerManager manager, Object obj) {
		JSONObject jsonAnnotation = initializeAnnotation(manager, (MAnnotation) obj);		
		return jsonAnnotation;
	}
}
