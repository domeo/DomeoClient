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

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.commentaries.linear.model.MLinearCommentAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.contentasrdf.model.MContentAsRdf;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.annotopia.SMicroPublicationSerializer;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.src.Utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
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
		jsonAnnotation.put("serializedBy",new JSONString("urn:application:domeo"));
		
		if(ann.getAnnotationType().equals("ao:Highlight"))
			jsonAnnotation.put("motivatedBy", new JSONString("oa:highlighting"));
		else if(ann.getAnnotationType().equals("ao:PostIt")) {
			jsonAnnotation.put("motivatedBy", new JSONString("oa:commenting"));
		} else if(ann.getAnnotationType().equals("ao:Qualifier")) {
			jsonAnnotation.put("motivatedBy", new JSONString("oa:tagging"));
		} else if(ann.getAnnotationType().equals("ao:LinearComment")) {
			jsonAnnotation.put("motivatedBy", new JSONString("oa:commenting"));
		} else if(ann.getAnnotationType().equals("ao:MicroPublicationAnnotation")) {
			jsonAnnotation.put("motivatedBy", new JSONString("mp:micropublishing"));
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
			jsonAnnotation.put("annotatedAt", new JSONString(Utils.fullfmt2.format(ann.getCreatedOn())));
		}
		
		//jsonAnnotation.put(IPavOntology.createdWith, new JSONString(ann.getTool()!=null?ann.getTool().getUri():""));
		if(ann.getTool()!=null) {
			jsonAnnotation.put("createdWith", manager.serialize(ann.getTool()));
		}
		
		//jsonAnnotation.put(IPavOntology.createdWith, serializeAgent(manager, ann.getTool()));
		//if(ann.getTool()!=null) manager.addAgentToSerialize(ann.getTool());
			
		jsonAnnotation.put("hasChanged", new JSONString(""+ann.getHasChanged()));
		
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
		
		if(ann.getAnnotationType().equals("ao:PostIt")) {
			jsonAnnotation.put("hasBody", encodeBodies(manager, ann));
		} 
		if(ann.getAnnotationType().equals("ao:Qualifier")) {
			jsonAnnotation.put("hasBody", encodeTags(manager, ann));
		}
		if(ann.getAnnotationType().equals("ao:LinearComment")) {
			jsonAnnotation.put("hasBody", encodeBodies(manager, ann));
		} 
		if(ann.getAnnotationType().equals("ao:MicroPublicationAnnotation")) {
			
			JSONObject body = (JSONObject) encodeBodies(manager, ann).get(0);
			String context = "{\"mp\":\"http://purl.org/mp/\",\"ex\":\"http://www.example.com/micropublication/ex1/\",\"dct\":\"http://purl.org/dc/terms/\",\"rdf\":\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\",\"rdfg\":\"http://www.w3.org/2004/03/trix/rdfg-1/\",\"xsd\":\"http://www.w3.org/2001/XMLSchema#\",\"rdfs\":\"http://www.w3.org/2000/01/rdf-schema#\",\"prov\":\"http://www.w3.org/ns/prov#\",\"obo\":\"http://purl.obolibrary.org/obo#\",\"foaf\":\"http://xmlns.com/foaf/spec/0.98#\",\"mp:asserts\":{\"@type\":\"@id\"},\"mp:challengedBy\":{\"@type\":\"@id\"},\"mp:argues\":{\"@type\":\"@id\"},\"mp:hasAttribution\":{\"@type\":\"@id\"},\"mp:attributionOfAgent\":{\"@type\":\"@id\"},\"mp:qualifiedBy\":{\"@type\":\"@id\"},\"mp:supportedBy\":{\"@type\":\"@id\"},\"mp:quotes\":{\"@type\":\"@id\"},\"mp:represents\":{\"@type\":\"@id\"},\"mp:atTime\":{\"@type\":\"xsd:dateTime\"}}";
			body.put("@context", JSONParser.parseStrict(context));	
			
			JSONArray a = new JSONArray();
			a.set(0, body);
			jsonAnnotation.put("hasBody", body);
		} 
		
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
	
	/**
	 * Encodes bodies in JSON format.
	 * @param manager	The serializer manager
	 * @param ann		The annotation to serialize
	 * @return The Textual Bodies in JSON format
	 */
	protected JSONArray encodeBodies(AnnotopiaSerializerManager manager, MAnnotation ann) {
		if(ann instanceof MPostItAnnotation) {
			MContentAsRdf body = ((MPostItAnnotation)ann).getBody();		
			JSONArray jsonBodies = new JSONArray();
			JSONObject jsonBody = new JSONObject();
			jsonBody.put(IRdfsOntology.id, nonNullable("urn:body:" + body.getIndividualUri()));
			JSONArray jsonTypes = new JSONArray();
			jsonTypes.set(0, new JSONString("cnt:ContentAsText"));
			jsonTypes.set(1, new JSONString("dctypes:Text"));
			jsonBody.put(IRdfsOntology.type, jsonTypes);
			jsonBody.put("format", new JSONString(body.getFormat()));
			jsonBody.put("chars", new JSONString(body.getChars()));
			jsonBodies.set(0, jsonBody);
			return jsonBodies;
		} else if(ann instanceof MLinearCommentAnnotation) {
			MLinearCommentAnnotation annotation = (MLinearCommentAnnotation) ann;
			JSONArray jsonBodies = new JSONArray();
			JSONObject jsonBody = new JSONObject();
			//jsonBody.put(IRdfsOntology.id, nonNullable("urn:body:" + body.getIndividualUri()));
			JSONArray jsonTypes = new JSONArray();
			jsonTypes.set(0, new JSONString("cnt:ContentAsText"));
			jsonTypes.set(1, new JSONString("dctypes:Text"));
			jsonBody.put(IRdfsOntology.type, jsonTypes);
			jsonBody.put("format", new JSONString("text/plain"));
			jsonBody.put("chars", new JSONString(((MLinearCommentAnnotation) ann).getText()));
			jsonBodies.set(0, jsonBody);
			return jsonBodies;
		} else if(ann instanceof MMicroPublicationAnnotation) {
			JSONArray jsonBodies = new JSONArray();
			SMicroPublicationSerializer s = new SMicroPublicationSerializer();
			jsonBodies.set(0, s.serialize(manager, ann));
			return jsonBodies;
		} else return null;
	}
	
	/**
	 * Encodes bodies in JSON format.
	 * @param manager	The serializer manager
	 * @param ann		The annotation to serialize
	 * @return The Textual Bodies in JSON format
	 */
	protected JSONArray encodeTags(AnnotopiaSerializerManager manager, MAnnotation ann) {
		ArrayList<MLinkedResource> terms = ((MQualifierAnnotation)ann).getTerms();	
		
		JSONArray jsonBodies = new JSONArray();
		
		int counter = 0;
		for(MLinkedResource term: terms) {
			JSONObject jsonBody = new JSONObject();
			jsonBody.put(IRdfsOntology.id, nonNullable(term.getUrl()));
			jsonBody.put(IRdfsOntology.type, nonNullable("oa:SemanticTag"));
			jsonBody.put(IRdfsOntology.label, nonNullable(term.getLabel()));
			
			JSONObject source = new JSONObject();
			source.put(IRdfsOntology.id, nonNullable(term.getSource().getUrl()));
			source.put(IRdfsOntology.label, nonNullable(term.getSource().getLabel()));
			jsonBody.put("at:source", source);
			
			jsonBodies.set(counter++, jsonBody);
		}
		return jsonBodies;
	}

	@Override
	public JSONObject serialize(AnnotopiaSerializerManager manager, Object obj) {
		JSONObject jsonAnnotation = initializeAnnotation(manager, (MAnnotation) obj);		
		return jsonAnnotation;
	}
}
