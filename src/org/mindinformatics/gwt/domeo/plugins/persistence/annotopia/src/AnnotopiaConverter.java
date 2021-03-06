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
package org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.src;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.linkeddata.model.JsoLinkedDataResource;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.MOnlineImage;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MImageInDocumentSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.highlight.model.MHighlightAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.JsMicroPublication;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublication;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpDataImage;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpElement;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpQualifier;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpReference;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpRelationship;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpStatement;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MicroPublicationFactory;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.serialization.JsoMpRelationship;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.PostitType;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.IAnnotopia;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.IOpenAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotationProvenance;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaAgent;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaAnnotationSetGraph;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaAnnotationSetSummary;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaSetsResultWrapper;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsContentAsText;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsOpenAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsResource;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsSpecificResource;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.MAnnotopiaAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.MAnnotopiaPerson;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.MAnnotopiaSoftware;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationPostIt;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsImageInDocumentSelector;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.lenses.PubMedCitationPainter;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;
import org.mindinformatics.gwt.framework.component.agents.src.AgentsFactory;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
import org.mindinformatics.gwt.framework.component.resources.serialization.JsonGenericResource;
import org.mindinformatics.gwt.framework.model.agents.IAgent;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.utils.src.HtmlUtils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotopiaConverter {

	public static final String COLUMN = ": ";
	public static final String COMMA = ", ";
	
	IDomeo _domeo;
	
	/**
	 * Constructor
	 * @param domeo	The main application pointer
	 */
	public AnnotopiaConverter(IDomeo domeo) {
		_domeo = domeo;
	}
	
	/**
	 * This is reading the annotation sets and the annotations and caching all the agents.
	 * @param wrapper		The Annotation Set wrapper
	 * @param agents		Agent ID is the key while the Agent object is the value
	 * @param entityAgents  Entity (ann or set) ID is the key while the Agent ID is the value
	 * @param targets	    Target ID is the key while the Target object is the value
	 * @param targetSources Annotation ID is the key and the Target object is the value
	 */
	private void cacheAgents(JsAnnotopiaAnnotationSetGraph wrapper, 
			HashMap<String, JsAnnotopiaAgent> agents, HashMap<String, String> entityAgents, 
			HashMap<String, JsResource> targets, HashMap<String, String> targetSources, boolean parseAnnotations) 
	{	
		JsArray<JavaScriptObject> graphs = wrapper.getGraphs();
		if(graphs.length()==1) {	
			JavaScriptObject jsItem = graphs.get(0);
			
			// Extract types
			HashSet<String> typesSet = new HashSet<String>();
			if(hasMultipleTypes(jsItem)) {
				JsArrayString types = getObjectTypes(jsItem);
				for(int k=0; k<types.length(); k++) {
					typesSet.add(types.get(k));
				}
			} else {
				typesSet.add(getObjectType(jsItem));
			}
			
			try {
				// ----------------------------------------
				//  Caching Annotation Sets Agents
				// ----------------------------------------
				JsAnnotopiaAnnotationSetSummary jsSet = (JsAnnotopiaAnnotationSetSummary) jsItem;
				
				_domeo.getLogger().debug(this, "Caching createdBy");
				// Annotation Set: createdBy
				_domeo.getLogger().debug(this, "Caching createdBy " + jsSet.isCreatedByString());
				if(jsSet.getCreatedBy()!=null && jsSet.isCreatedByString()) {
					entityAgents.put("createdBy:"+jsSet.getId(), jsSet.getCreatedByAsString());
					_domeo.getLogger().debug(this, "Caching createdBy [" + "createdBy:"+jsSet.getId() + ":" + jsSet.getCreatedByAsString() + "]");
				} else if(jsSet.getCreatedBy()!=null && jsSet.isCreatedByObject()) {
					agents.put(jsSet.getCreatedByAsObject().getId(), jsSet.getCreatedByAsObject());
					_domeo.getLogger().debug(this, "Caching createdBy [" + "createdBy:"+jsSet.getId() + ":" + jsSet.getCreatedByAsObject().getId() + "]");
				}
				
				_domeo.getLogger().debug(this, "Caching createdWith");
				// Annotation Set: createdWith
				if(jsSet.getCreatedWith()!=null && jsSet.isCreatedWithString()) {
					entityAgents.put("createdWith:"+jsSet.getId(), jsSet.getCreatedWithAsString());
					_domeo.getLogger().debug(this, "Caching createdWith [" + "createdWith:"+jsSet.getId() + ":" + jsSet.getCreatedWithAsString() + "]");
				} else if(jsSet.getCreatedWith()!=null && jsSet.isCreatedWithObject()) {
					agents.put(jsSet.getCreatedWithAsObject().getId(), jsSet.getCreatedWithAsObject());
				}
				
				_domeo.getLogger().debug(this, "Caching lastSavedBy");
				// Annotation Set: lastSavedBy
				if(jsSet.getLastSavedBy()!=null && jsSet.isLastSavedByString()) {
					entityAgents.put("lastSavedBy:"+jsSet.getId(), jsSet.getLastSavedByAsString());
				} else if(jsSet.getLastSavedBy()!=null && jsSet.isLastSavedByObject()) {
					agents.put(jsSet.getLastSavedByAsObject().getId(), jsSet.getLastSavedByAsObject());
				}
				
				// ----------------------------------------
				//  Caching Annotation Agents
				// ----------------------------------------
				if(parseAnnotations) {
					_domeo.getLogger().debug(this, "Caching annotation agents");
					int max = jsSet.isAnnotationsArray() ? jsSet.getAnnotations().length() : (jsSet.hasAnnotation()?1:0);
					_domeo.getLogger().debug(this, "Detected annotations: " + max);
					for(int i=0; i<max; i++) {			
//						JavaScriptObject a = null;
//						if(max==1) a = jsSet.getAnnotation();
//						else a = jsSet.getAnnotations().get(i);
						JavaScriptObject a = jsSet.getAnnotations().get(i);
						if(getObjectType(a).equals(IOpenAnnotation.ANNOTATION)) {
							// Unmarshall annotatedBy
							JsAnnotationProvenance annotationProvenance = (JsAnnotationProvenance) a;
							if(annotationProvenance.getAnnotatedBy()!=null && annotationProvenance.isAnnotatedByString()) {
								entityAgents.put("createdBy:"+annotationProvenance.getId(), annotationProvenance.getAnnotatedByAsString());
								_domeo.getLogger().debug(this, "PUT: " + annotationProvenance.getId() + "-" +  annotationProvenance.getAnnotatedByAsString());
							} else if(annotationProvenance.getAnnotatedBy()!=null && annotationProvenance.isAnnotatedByObject()) {
								agents.put(annotationProvenance.getAnnotatedByAsObject().getId(), annotationProvenance.getAnnotatedByAsObject());
								_domeo.getLogger().debug(this, "PUT: " + annotationProvenance.getAnnotatedByAsObject().getId() + "-" +  annotationProvenance.getAnnotatedByAsObject().getName());
							}
							if(annotationProvenance.getCreatedWith()!=null && annotationProvenance.isCreatedWithString()) {
								entityAgents.put("createdWith:"+annotationProvenance.getId(), annotationProvenance.getCreatedWithAsString());
							} else if(annotationProvenance.getCreatedWith()!=null && annotationProvenance.isCreatedWithObject()) {
								agents.put(annotationProvenance.getCreatedWithAsObject().getId(), annotationProvenance.getCreatedWithAsObject());
							}
							if(annotationProvenance.getLastSavedBy()!=null && annotationProvenance.isLastSavedByString()) {
								entityAgents.put("lastSavedBy:"+annotationProvenance.getId(), annotationProvenance.getLastSavedByAsString());
							} else if(annotationProvenance.getLastSavedBy()!=null && annotationProvenance.isLastSavedByObject()) {
								agents.put(annotationProvenance.getLastSavedByAsObject().getId(), annotationProvenance.getLastSavedByAsObject());
							}
							
							JsOpenAnnotation annotation = (JsOpenAnnotation) a;			
							
							// Unmarshall targets
							boolean multipleTargets = annotation.hasMultipleTargets();
							if(multipleTargets) {
								JsArray<JavaScriptObject> jsTargets = annotation.getTargets();
								for(int t=0; t<jsTargets.length(); t++) {
									JavaScriptObject jsTarget = jsTargets.get(t);
									if(getObjectType(jsTarget).contains(IOpenAnnotation.SPECIFIC_RESOURCE)) {
										JsSpecificResource jsSpecificResource = (JsSpecificResource) jsTarget;
										if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceString()) {
											targetSources.put(jsSpecificResource.getId(), jsSpecificResource.getHasSourceAsString());
										} else if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceObject()) {
											targets.put(jsSpecificResource.getHasSourceAsObject().getId(), jsSpecificResource.getHasSourceAsObject());
										}
									}
								}
							} else {
								JavaScriptObject jsTarget = annotation.getTarget();
								if(getObjectType(jsTarget).contains(IOpenAnnotation.SPECIFIC_RESOURCE)) {
									JsSpecificResource jsSpecificResource = (JsSpecificResource) jsTarget;
									if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceString()) {
										targetSources.put(jsSpecificResource.getId(), jsSpecificResource.getHasSourceAsString());
									} else if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceObject()) {
										targets.put(jsSpecificResource.getHasSourceAsObject().getId(), jsSpecificResource.getHasSourceAsObject());
									}
								}
							}
						} else {
							_domeo.getLogger().warn(this, "Item not qualified as annotation... skipped" + jsSet.getId());
						}
					}		
				}
				
				/*
				Set<String> agentsKeys = agents.keySet();
				for(String agentsKey: agentsKeys) {
					_domeo.getLogger().debug(this, "AGENT: " + agentsKey + "-" + agents.get(agentsKey).getName());
				}
				Set<String> agentEntityKeys = entityAgents.keySet();
				for(String agentEntityKey: agentEntityKeys) {
					_domeo.getLogger().debug(this, "AGENTENTITY: " + agentEntityKey + "-" + entityAgents.get(agentEntityKey));
				}
				*/
			} catch(Exception e) {
				_domeo.getLogger().exception(this, "unmarshallAnnotationSet(): " + e.getMessage());
			}
		} else {
			_domeo.getLogger().exception(this, "Graph issue, unrecognized agents format");
		}	
	}
	
	/**
	 * This is reading the annotation sets and the annotations and caching all the agents.
	 * @param wrapper		The Annotation Set wrapper
	 * @param agents		Agent ID is the key while the Agent object is the value
	 * @param entityAgents  Entity (ann or set) ID is the key while the Agent ID is the value
	 * @param targets	    Target ID is the key while the Target object is the value
	 * @param targetSources Annotation ID is the key and the Target object is the value
	 */
	private void cacheBasicAgents(JavaScriptObject jsItem, 
			HashMap<String, JsAnnotopiaAgent> agents, HashMap<String, String> entityAgents, 
			HashMap<String, JsResource> targets, HashMap<String, String> targetSources, boolean parseAnnotations) 
	{	
		_domeo.getLogger().debug(this, "Basic agent caching");
		
//		JsArray<JavaScriptObject> graphs = wrapper.getGraphs();
//		if(graphs.length()==1) {	
//			JavaScriptObject jsItem = graphs.get(0);
			
			// Extract types
			HashSet<String> typesSet = new HashSet<String>();
			if(hasMultipleTypes(jsItem)) {
				JsArrayString types = getObjectTypes(jsItem);
				for(int k=0; k<types.length(); k++) {
					typesSet.add(types.get(k));
				}
			} else {
				typesSet.add(getObjectType(jsItem));
			}
			
			try {
				// ----------------------------------------
				//  Caching Annotation Sets Agents
				// ----------------------------------------
				JsAnnotopiaAnnotationSetSummary jsSet = (JsAnnotopiaAnnotationSetSummary) jsItem;
				
				_domeo.getLogger().debug(this, "Caching createdBy");
				// Annotation Set: createdBy
				_domeo.getLogger().debug(this, "Caching createdBy " + jsSet.isCreatedByString());
				if(jsSet.getCreatedBy()!=null && jsSet.isCreatedByString()) {
					entityAgents.put("createdBy:"+jsSet.getId(), jsSet.getCreatedByAsString());
					_domeo.getLogger().debug(this, "Caching createdBy [" + "createdBy:"+jsSet.getId() + ":" + jsSet.getCreatedByAsString() + "]");
				} else if(jsSet.getCreatedBy()!=null && jsSet.isCreatedByObject()) {
					agents.put(jsSet.getCreatedByAsObject().getId(), jsSet.getCreatedByAsObject());
					_domeo.getLogger().debug(this, "Caching createdBy [" + "createdBy:"+jsSet.getId() + ":" + jsSet.getCreatedByAsObject().getId() + "]");
				}
				
				_domeo.getLogger().debug(this, "Caching createdWith");
				// Annotation Set: createdWith
				if(jsSet.getCreatedWith()!=null && jsSet.isCreatedWithString()) {
					entityAgents.put("createdWith:"+jsSet.getId(), jsSet.getCreatedWithAsString());
					_domeo.getLogger().debug(this, "Caching createdWith [" + "createdWith:"+jsSet.getId() + ":" + jsSet.getCreatedWithAsString() + "]");
				} else if(jsSet.getCreatedWith()!=null && jsSet.isCreatedWithObject()) {
					agents.put(jsSet.getCreatedWithAsObject().getId(), jsSet.getCreatedWithAsObject());
				}
				
				_domeo.getLogger().debug(this, "Caching lastSavedBy");
				// Annotation Set: lastSavedBy
				if(jsSet.getLastSavedBy()!=null && jsSet.isLastSavedByString()) {
					entityAgents.put("lastSavedBy:"+jsSet.getId(), jsSet.getLastSavedByAsString());
				} else if(jsSet.getLastSavedBy()!=null && jsSet.isLastSavedByObject()) {
					agents.put(jsSet.getLastSavedByAsObject().getId(), jsSet.getLastSavedByAsObject());
				}
				
				// ----------------------------------------
				//  Caching Annotation Agents
				// ----------------------------------------
				if(parseAnnotations) {
					_domeo.getLogger().debug(this, "Caching annotation agents");
					int max = jsSet.isAnnotationsArray() ? jsSet.getAnnotations().length() : (jsSet.hasAnnotation()?1:0);
					_domeo.getLogger().debug(this, "Detected annotations: " + max);
					for(int i=0; i<max; i++) {			
//						JavaScriptObject a = null;
//						if(max==1) a = jsSet.getAnnotation();
//						else a = jsSet.getAnnotations().get(i);
						JavaScriptObject a = jsSet.getAnnotations().get(i);
						if(getObjectType(a).equals(IOpenAnnotation.ANNOTATION)) {
							// Unmarshall annotatedBy
							JsAnnotationProvenance annotationProvenance = (JsAnnotationProvenance) a;
							if(annotationProvenance.getAnnotatedBy()!=null && annotationProvenance.isAnnotatedByString()) {
								entityAgents.put("createdBy:"+annotationProvenance.getId(), annotationProvenance.getAnnotatedByAsString());
								_domeo.getLogger().debug(this, "PUT: " + annotationProvenance.getId() + "-" +  annotationProvenance.getAnnotatedByAsString());
							} else if(annotationProvenance.getAnnotatedBy()!=null && annotationProvenance.isAnnotatedByObject()) {
								agents.put(annotationProvenance.getAnnotatedByAsObject().getId(), annotationProvenance.getAnnotatedByAsObject());
								_domeo.getLogger().debug(this, "PUT: " + annotationProvenance.getAnnotatedByAsObject().getId() + "-" +  annotationProvenance.getAnnotatedByAsObject().getName());
							}
							if(annotationProvenance.getCreatedWith()!=null && annotationProvenance.isCreatedWithString()) {
								entityAgents.put("createdWith:"+annotationProvenance.getId(), annotationProvenance.getCreatedWithAsString());
							} else if(annotationProvenance.getCreatedWith()!=null && annotationProvenance.isCreatedWithObject()) {
								agents.put(annotationProvenance.getCreatedWithAsObject().getId(), annotationProvenance.getCreatedWithAsObject());
							}
							if(annotationProvenance.getLastSavedBy()!=null && annotationProvenance.isLastSavedByString()) {
								entityAgents.put("lastSavedBy:"+annotationProvenance.getId(), annotationProvenance.getLastSavedByAsString());
							} else if(annotationProvenance.getLastSavedBy()!=null && annotationProvenance.isLastSavedByObject()) {
								agents.put(annotationProvenance.getLastSavedByAsObject().getId(), annotationProvenance.getLastSavedByAsObject());
							}
							
							JsOpenAnnotation annotation = (JsOpenAnnotation) a;			
							
							// Unmarshall targets
							boolean multipleTargets = annotation.hasMultipleTargets();
							if(multipleTargets) {
								JsArray<JavaScriptObject> jsTargets = annotation.getTargets();
								for(int t=0; t<jsTargets.length(); t++) {
									JavaScriptObject jsTarget = jsTargets.get(t);
									if(getObjectType(jsTarget).contains(IOpenAnnotation.SPECIFIC_RESOURCE)) {
										JsSpecificResource jsSpecificResource = (JsSpecificResource) jsTarget;
										if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceString()) {
											targetSources.put(jsSpecificResource.getId(), jsSpecificResource.getHasSourceAsString());
										} else if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceObject()) {
											targets.put(jsSpecificResource.getHasSourceAsObject().getId(), jsSpecificResource.getHasSourceAsObject());
										}
									}
								}
							} else {
								JavaScriptObject jsTarget = annotation.getTarget();
								if(getObjectType(jsTarget).contains(IOpenAnnotation.SPECIFIC_RESOURCE)) {
									JsSpecificResource jsSpecificResource = (JsSpecificResource) jsTarget;
									if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceString()) {
										targetSources.put(jsSpecificResource.getId(), jsSpecificResource.getHasSourceAsString());
									} else if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceObject()) {
										targets.put(jsSpecificResource.getHasSourceAsObject().getId(), jsSpecificResource.getHasSourceAsObject());
									}
								}
							}
						} else {
							_domeo.getLogger().warn(this, "Item not qualified as annotation... skipped" + jsSet.getId());
						}
					}		
				}
				
				/*
				Set<String> agentsKeys = agents.keySet();
				for(String agentsKey: agentsKeys) {
					_domeo.getLogger().debug(this, "AGENT: " + agentsKey + "-" + agents.get(agentsKey).getName());
				}
				Set<String> agentEntityKeys = entityAgents.keySet();
				for(String agentEntityKey: agentEntityKeys) {
					_domeo.getLogger().debug(this, "AGENTENTITY: " + agentEntityKey + "-" + entityAgents.get(agentEntityKey));
				}
				*/
			} catch(Exception e) {
				_domeo.getLogger().exception(this, "unmarshallAnnotationSet(): " + e.getMessage());
			}
//		} else {
//			_domeo.getLogger().exception(this, "Graph issue, unrecognized agents format");
//		}	
	}
	
	/**
	 * Unmarshalls the annotation sets list summaries ignoring
	 * the sets details.
	 * @return Annotation Sets list summary
	 */
	public List<MAnnotopiaAnnotationSet> unmarshallAnnotationSetsList(JsAnnotopiaSetsResultWrapper wrapper) {
		List<MAnnotopiaAnnotationSet> sets = new ArrayList<MAnnotopiaAnnotationSet>();
		
		JsArray<JsAnnotopiaAnnotationSetGraph>  jsSets = wrapper.getResult().getSets();
		for(int i=0; i<jsSets.length(); i++) {			
			// Cache for lazy binding
			HashMap<String, JsAnnotopiaAgent> agents = new HashMap<String, JsAnnotopiaAgent>();
			HashMap<String, String> entityAgents = new HashMap<String, String>();
			HashMap<String, JsResource> targets = new HashMap<String, JsResource>();
			HashMap<String, String> targetSources = new HashMap<String, String>();
			// Caching of both sets and annotations
			cacheAgents(jsSets.get(i), agents, entityAgents, targets, targetSources, false);
			
			JsArray<JavaScriptObject> graphs = jsSets.get(i).getGraphs();
			if(graphs.length()==1) {	
				JavaScriptObject jsItem = graphs.get(0);
				
				// Extract types
				HashSet<String> typesSet = new HashSet<String>();
				if(hasMultipleTypes(jsItem)) {
					JsArrayString types = getObjectTypes(jsItem);
					for(int k=0; k<types.length(); k++) {
						typesSet.add(types.get(k));
					}
				} else {
					typesSet.add(getObjectType(jsItem));
				}
				
				try {
					MAnnotopiaAnnotationSet set = unmarshallAnnotationSet(jsItem, typesSet, agents, entityAgents);
					if(set!=null) sets.add(set);
				} catch(Exception e) {
					_domeo.getLogger().exception(this, e.getMessage());
				}
			} else {
				_domeo.getLogger().exception(this, "Unrecognized format");
			}			
		}		
		return sets;
	}
	
	public MAnnotationSet unmarshallBasicAnnotationSet(JavaScriptObject  annoSet, boolean persist) {		
		// Cache for lazy binding
		HashMap<String, JsAnnotopiaAgent> agents = new HashMap<String, JsAnnotopiaAgent>();
		HashMap<String, String> entityAgents = new HashMap<String, String>();
		HashMap<String, JsResource> targets = new HashMap<String, JsResource>();
		HashMap<String, String> targetSources = new HashMap<String, String>();

		// Caching of both sets and annotations
		cacheBasicAgents(annoSet, agents, entityAgents, targets, targetSources, true);
		
		_domeo.getLogger().debug(this, "Unmarshalling set x");

			JavaScriptObject jsItem = annoSet;
			
			// Extract types
			HashSet<String> typesSet = new HashSet<String>();
			if(hasMultipleTypes(jsItem)) {
				JsArrayString types = getObjectTypes(jsItem);
				for(int k=0; k<types.length(); k++) {
					typesSet.add(types.get(k));
				}
			} else {
				typesSet.add(getObjectType(jsItem));
			}
			
			try {
				MAnnotopiaAnnotationSet set = unmarshallAnnotationSet(jsItem, typesSet, agents, entityAgents);
				
				if(set!=null) {
					_domeo.getLogger().debug(this, "Unmarshalling createdWith " + set.getCreatedWith());
					// Translation to the old internal formats
					MAgentSoftware createdWith = new MAgentSoftware();
					createdWith.setUri(set.getCreatedWith().getId());
					createdWith.setName(set.getCreatedWith().getName());
					
					_domeo.getLogger().debug(this, "Unmarshalling createdBy " + set.getCreatedBy());
					MAgentPerson createdBy = new MAgentPerson();
					createdBy.setUri(set.getCreatedBy().getId());
					createdBy.setName(set.getCreatedBy().getName());
					
					// Annotation Set
					// Note: currently the lastSavedOn is forcing createdOn
					MAnnotationSet aSet = AnnotationFactory.createAnnotationSet(set.getId(), set.getId(), set.getLastSavedOn(), set.getVersionNumber(), set.getPreviousVersion(),
							 _domeo.getPersistenceManager().getCurrentResource(), set.getLabel(), set.getDescription());		
					aSet.setCreatedWith(createdWith);
					aSet.setCreatedBy(createdBy);
					aSet.setCreatedOn(set.getCreatedOn());
					
					_domeo.getLogger().debug(this, "Unmarshalling annotations");
					unmarshallAnnotations(aSet, set, jsItem, agents, entityAgents, targets, targetSources, persist);
					
					return aSet;
				}
			} catch(Exception e) {
				_domeo.getLogger().exception(this, "unmarshallAnnotationSet(): " + e.getMessage());
			}	
		return null;
	}
	
	public MAnnotationSet unmarshallAnnotationSet(JsAnnotopiaAnnotationSetGraph  wrapper, boolean persist) {		
		
		// Cache for lazy binding
		HashMap<String, JsAnnotopiaAgent> agents = new HashMap<String, JsAnnotopiaAgent>();
		HashMap<String, String> entityAgents = new HashMap<String, String>();
		HashMap<String, JsResource> targets = new HashMap<String, JsResource>();
		HashMap<String, String> targetSources = new HashMap<String, String>();

		// Caching of both sets and annotations
		cacheAgents(wrapper, agents, entityAgents, targets, targetSources, true);
		
		_domeo.getLogger().debug(this, "Unmarshalling set");
		JsArray<JavaScriptObject> graphs = wrapper.getGraphs();
		_domeo.getLogger().debug(this, "Graphs: " + graphs.length());
		if(graphs.length()==1) {	
			JavaScriptObject jsItem = graphs.get(0);
			
			// Extract types
			HashSet<String> typesSet = new HashSet<String>();
			if(hasMultipleTypes(jsItem)) {
				JsArrayString types = getObjectTypes(jsItem);
				for(int k=0; k<types.length(); k++) {
					typesSet.add(types.get(k));
				}
			} else {
				typesSet.add(getObjectType(jsItem));
			}
			
			try {
				MAnnotopiaAnnotationSet set = unmarshallAnnotationSet(jsItem, typesSet, agents, entityAgents);
				
				if(set!=null) {
					_domeo.getLogger().debug(this, "Unmarshalling createdWith " + set.getCreatedWith());
					// Translation to the old internal formats
					MAgentSoftware createdWith = new MAgentSoftware();
					createdWith.setUri(set.getCreatedWith().getId());
					createdWith.setName(set.getCreatedWith().getName());
					
					_domeo.getLogger().debug(this, "Unmarshalling createdBy " + set.getCreatedBy());
					MAgentPerson createdBy = new MAgentPerson();
					createdBy.setUri(set.getCreatedBy().getId());
					createdBy.setName(set.getCreatedBy().getName());
					
					// Annotation Set
					// Note: currently the lastSavedOn is forcing createdOn
					MAnnotationSet aSet = AnnotationFactory.createAnnotationSet(set.getId(), set.getId(), set.getLastSavedOn(), set.getVersionNumber(), set.getPreviousVersion(),
							 _domeo.getPersistenceManager().getCurrentResource(), set.getLabel(), set.getDescription());		
					aSet.setCreatedWith(createdWith);
					aSet.setCreatedBy(createdBy);
					aSet.setCreatedOn(set.getCreatedOn());
					
					_domeo.getLogger().debug(this, "Unmarshalling annotations");
					unmarshallAnnotations(aSet, set, jsItem, agents, entityAgents, targets, targetSources, persist);
					
					return aSet;
				}
			} catch(Exception e) {
				_domeo.getLogger().exception(this, "unmarshallAnnotationSet(): " + e.getMessage());
			}
		} else {
			_domeo.getLogger().exception(this, "Unrecognized format");
		}	
		return null;
	}
	
	
	private MAnnotopiaAnnotationSet unmarshallAnnotationSet(JavaScriptObject jsItem, HashSet<String> typesSet,
			HashMap<String, JsAnnotopiaAgent> agents, HashMap<String, String> entityAgents) 
	{
		if(typesSet.contains(IAnnotopia.ANNOTATION_SET_NS)) {
			MAnnotopiaAnnotationSet set = new MAnnotopiaAnnotationSet();
			set.setType(IAnnotopia.ANNOTATION_SET_ID);
			set.setLocked(false);
			set.setVisible(true);
			
			_domeo.getLogger().debug(this, "Unmarshalling set");
			JsAnnotopiaAnnotationSetSummary jsSet = (JsAnnotopiaAnnotationSetSummary) jsItem;
			set.setId(notNullableString("id", jsSet.getId()));	
			set.setLabel(notNullableString("label", jsSet.getLabel()));
			set.setDescription(nullableString("description", jsSet.getDescription()));
			
			/*
			if(jsSet.isAnnotationsArray()) 
				set.setNumberAnnotations(jsSet.getNumberOfAnnotationItems());
			else if(jsSet.getAnnotation()!=null)	
				set.setNumberAnnotations(1);
			else 
				set.setNumberAnnotations(0);
				*/
			set.setNumberAnnotations(jsSet.annotationCounts());
			
			_domeo.getLogger().debug(this, "Unmarshalling set created on");
			// Created on
			if(jsSet.getCreatedOn()!=null && !jsSet.getCreatedOn().isEmpty()) {
				try {
					set.setCreatedOn(jsSet.getFormattedCreatedOn());
				} catch (Exception e) {
					try {
						set.setCreatedOn(jsSet.getFormattedCreatedOn2());						
					} catch (Exception ex) {
						_domeo.getLogger().exception(this, "Problems in parsing createdOn " 
							+ jsSet.getCreatedOn() + " - " + e.getMessage());
					}
				}
			}
			
			// Created by
			_domeo.getLogger().debug(this, "Unmarshalling set created by");
			JsAnnotopiaAgent jsCreatedBy = null;
			if(jsSet.getCreatedBy()!=null && jsSet.isCreatedByString()) {				
				jsCreatedBy = agents.get(entityAgents.get("createdBy:"+jsSet.getId()));
			} else if(jsSet.getCreatedBy()!=null && jsSet.isCreatedByObject()) {
				jsCreatedBy = agents.get(jsSet.getCreatedByAsObject().getId());
			}
			if(jsCreatedBy!=null) {
				_domeo.getLogger().debug(this, "Set created by: " + jsCreatedBy.getId());
				MAnnotopiaPerson createdBy = new MAnnotopiaPerson();
				createdBy.setId(jsCreatedBy.getId());
				createdBy.setName(jsCreatedBy.getName());
				set.setCreatedBy(createdBy);
			} else {
				// TODO What to do here?
				_domeo.getLogger().warn(this, "No createdBy detected for set " + jsSet.getId());
			}
			
			_domeo.getLogger().debug(this, "Unmarshalling set created with");
			// Created with
			JsAnnotopiaAgent jsCreatedWith = null;
			if(jsSet.getCreatedWith()!=null && jsSet.isCreatedWithString()) {
				_domeo.getLogger().debug(this, "Unmarshalling set created with " + "createdWith:"+jsSet.getId());
				jsCreatedWith = agents.get(entityAgents.get("createdWith:"+jsSet.getId()));
			} else if(jsSet.getCreatedWith()!=null && jsSet.isCreatedWithObject()) {
				jsCreatedWith = agents.get(jsSet.getCreatedWithAsObject().getId());
			}
			_domeo.getLogger().debug(this, "Set created with: " + jsCreatedWith);
			if(jsCreatedWith!=null) {
				_domeo.getLogger().debug(this, "Set created with: " + jsCreatedWith.getId());
				MAnnotopiaSoftware createdWith = new MAnnotopiaSoftware();
				createdWith.setId(jsCreatedWith.getId());
				createdWith.setName(jsCreatedWith.getName());
				set.setCreatedWith(createdWith);
			} else {
				// TODO What to do here?
				_domeo.getLogger().warn(this, "No createdWith detected for set " + jsSet.getId());
			}
			
			_domeo.getLogger().debug(this, "Unmarshalling set last saved on");
			// Last saved on
			if(jsSet.getLastSavedOn()!=null && !jsSet.getLastSavedOn().isEmpty()) {
				try {
					set.setLastSavedOn(jsSet.getFormattedLastSavedOn());
				} catch (Exception e) {
					_domeo.getLogger().exception(this, "Problems in parsing lastSavedOn " 
						+ jsSet.getLastSavedOn() + " - " + e.getMessage());
				}
			} else {
				_domeo.getLogger().warn(this, "No lastSavedOn detected for set " + jsSet.getId() + " - using createdOn (TODO ?)");
			}
			
			// TODO Improve
			_domeo.getLogger().debug(this, "Unmarshalling set last saved by");
			// Created with
			JsAnnotopiaAgent jsLastSavedBy = null;
			if(jsSet.getLastSavedBy()!=null && jsSet.isLastSavedByString()) {
				_domeo.getLogger().debug(this, "Unmarshalling set lastSaved by " + "lastSavedBy:"+jsSet.getId());
				jsLastSavedBy = agents.get(entityAgents.get("lastSavedBy:"+jsSet.getId()));
			} else if(jsSet.getLastSavedBy()!=null && jsSet.isLastSavedByObject()) {
				jsLastSavedBy = agents.get(jsSet.getLastSavedByAsObject().getId());
			}
			_domeo.getLogger().debug(this, "Set last saved by: " + jsLastSavedBy);
			if(jsLastSavedBy!=null) {
				_domeo.getLogger().debug(this, "Set last saved by: " + jsLastSavedBy.getId());
				MAnnotopiaPerson lastSavedBy = new MAnnotopiaPerson();
				lastSavedBy.setId(jsLastSavedBy.getId());
				lastSavedBy.setName(jsLastSavedBy.getName());
				set.setLastSavedBy(lastSavedBy);
			} else {
				_domeo.getLogger().warn(this, "No lastSavedBy detected for set " + jsSet.getId() + " - using createdBy (TODO ?)");
			}

			_domeo.getLogger().debug(this, "Unmarshalling versioning");
			// Versioning
			set.setPreviousVersion(jsSet.getPreviousVersion());
			set.setVersionNumber(jsSet.getVersionNumber());
			
			_domeo.getLogger().debug(this, "Unmarshalling set completed");
			return set;
		} else {
			_domeo.getLogger().warn(this, "Item does not contain a recognized Set type " + typesSet);
			return null;
		}
	}

	
	private String notNullableString(String fieldLabel, String fieldValue) {
		if(fieldValue==null || fieldValue.isEmpty()) {
			throw new RuntimeException("Null variable: " + fieldLabel);
		} else return fieldValue;
	}
	
	private String nullableString(String fieldLabel, String fieldValue) {
		if(fieldValue==null || fieldValue.isEmpty()) {
			_domeo.getLogger().debug(this, "Found empty: " + fieldLabel);
			return "<empty>";
		} else return fieldValue;
	}
	
	private void unmarshallAnnotations(MAnnotationSet aSet, MAnnotopiaAnnotationSet set, JavaScriptObject jsItem,
			HashMap<String, JsAnnotopiaAgent> agents, HashMap<String, String> entityAgents, 
			HashMap<String, JsResource> targets, HashMap<String, String> targetSources, boolean persist) 
	{
		_domeo.getLogger().debug(this, "--------------------------------");
		_domeo.getLogger().debug(this, "ANNOTATION SET");

		JsAnnotopiaAnnotationSetSummary jsSet = (JsAnnotopiaAnnotationSetSummary) jsItem;
		int max = jsSet.isAnnotationsArray() ? jsSet.getAnnotations().length() : ((jsSet.getAnnotation()!=null)?1:0);
		_domeo.getLogger().debug(this, "Annotation detected: " + max);
		/*
		Set<String> agentsKeys = agents.keySet();
		for(String agentsKey: agentsKeys) {
			_domeo.getLogger().debug(this, "AGENT: " + agentsKey + "-" + agents.get(agentsKey).getName());
		}
		Set<String> agentEntityKeys = entityAgents.keySet();
		for(String agentEntityKey: agentEntityKeys) {
			_domeo.getLogger().debug(this, "AGENTENTITY: " + agentEntityKey + "-" + entityAgents.get(agentEntityKey));
		}
		*/
		
		// Creation of annotation items
		for(int i=0; i<max; i++) {
			_domeo.getLogger().debug(this, "--------------------------------");
			_domeo.getLogger().debug(this, "ANNOTATION: " + (i+1));
//			JavaScriptObject a = null;
//			if(max==1) a = jsSet.getAnnotation();
//			else a = jsSet.getAnnotations().get(i);
			JavaScriptObject a = jsSet.getAnnotations().get(i);
			if(getObjectType(a).equals(IOpenAnnotation.ANNOTATION)) {
				JsOpenAnnotation annotation = (JsOpenAnnotation) a;
				
				// AnnotatedBy
				JsAnnotopiaAgent jsAnnotatedBy = null;
				if(annotation.getAnnotatedBy()!=null && annotation.isAnnotatedByString()) {
					jsAnnotatedBy = agents.get(entityAgents.get("createdBy:"+annotation.getId()));
//					_domeo.getLogger().debug(this, "By string " + annotation.getId() + "-" + agents.get(entityAgents.get("createdBy:"+annotation.getId())).getId() + "-" + agents.get(entityAgents.get("createdBy:"+annotation.getId())).getName());
//					_domeo.getLogger().debug(this, "By string " + jsAnnotatedBy.getId());
				} else if(annotation.getAnnotatedBy()!=null && annotation.isAnnotatedByObject()) {
					jsAnnotatedBy = agents.get(annotation.getAnnotatedByAsObject().getId());
//					_domeo.getLogger().debug(this, "By object " + annotation.getId() + "-" + annotation.getAnnotatedByAsObject().getId() + "-" + agents.get(annotation.getAnnotatedByAsObject().getId()).getName());

				}
				_domeo.getLogger().debug(this, "Motivation: " + getMotivation(annotation));
				_domeo.getLogger().debug(this, "Annotated by: " + jsAnnotatedBy.getId());
				
				// Provenance
				Date annotatedAt = new Date();
				if(annotation.getAnnotatedAt()!=null) {
					_domeo.getLogger().debug(this, "Annotated At:"+ ((JsAnnotationProvenance)a).getFormattedAnnotatedAt());
					annotatedAt = annotation.getFormattedAnnotatedAt();
				}
				
				Date lastSavedOn = null;
				if(((JsAnnotationProvenance)a).getLastSavedOn()!=null && ((JsAnnotationProvenance)a).getLastSavedOn().trim().length()>5) {
					_domeo.getLogger().debug(this, "Last Saved On:"+ ((JsAnnotationProvenance)a).getFormattedLastSavedOn());
					lastSavedOn = ((JsAnnotationProvenance)a).getFormattedLastSavedOn();
				}
				
				// Targets
				ArrayList<MSelector> selectors = new ArrayList<MSelector>();
				//ArrayList<String> resources = new ArrayList<String>(); 
				//ArrayList<JsSpecificResource> specificResources = new ArrayList<JsSpecificResource>();
				boolean multipleTargets = annotation.hasMultipleTargets();
				if(multipleTargets) {
					_domeo.getLogger().debug(this, "Multiple targets");
					JsArray<JavaScriptObject> jsTargets = annotation.getTargets();
					for(int t=0; t<jsTargets.length(); t++) {
						JavaScriptObject jsTarget = jsTargets.get(t);
						if(getObjectType(jsTarget).contains(IOpenAnnotation.SPECIFIC_RESOURCE)) {
							_domeo.getLogger().debug(this, "Specific Resource");
							JsSpecificResource jsSpecificResource = (JsSpecificResource) jsTarget;
							if(getObjectType(jsSpecificResource.getSelector()).contains(IOpenAnnotation.TEXT_QUOTE_SELECTOR)) {
								JsTextQuoteSelector selector = (JsTextQuoteSelector) jsSpecificResource.getSelector();
								
								JsResource resource = null;
								if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceString()) {
									resource = targets.get(targetSources.get(jsSpecificResource.getId()));
								} else if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceObject()) {
									resource = targets.get(jsSpecificResource.getHasSourceAsObject().getId());
								}
								_domeo.getLogger().debug(this, "Resource " + resource.getId());
								
								MSelector sel = AnnotationFactory.createPrefixSuffixTextSelector(
										_domeo.getAgentManager().getUserPerson(), 
										new MGenericResource(resource.getId(),""), 
										selector.getExact(), 
										selector.getPrefix(), 
										selector.getSuffix());
								sel.setUri(jsSpecificResource.getId());
								selectors.add(sel);
							}
						}
					}
				} else {
					_domeo.getLogger().debug(this, "Single Target");
					JavaScriptObject jsTarget = annotation.getTarget();
					if(getObjectType(jsTarget).contains(IOpenAnnotation.SPECIFIC_RESOURCE)) {
						JsSpecificResource jsSpecificResource = (JsSpecificResource) jsTarget;
						if(jsSpecificResource.getSelector()!=null &&
								getObjectType(jsSpecificResource.getSelector())!=null && 
								getObjectType(jsSpecificResource.getSelector()).contains(IOpenAnnotation.TEXT_QUOTE_SELECTOR)) {
							_domeo.getLogger().debug(this, "Specific Resource with Text Quote Selector");
							JsTextQuoteSelector selector = (JsTextQuoteSelector) jsSpecificResource.getSelector();
							JsResource resource = null;
							if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceString()) {
								resource = targets.get(targetSources.get(jsSpecificResource.getId()));
							} else if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceObject()) {
								resource = targets.get(jsSpecificResource.getHasSourceAsObject().getId());
							}
							_domeo.getLogger().debug(this, "Resource " + resource.getId());

							MSelector sel = AnnotationFactory.createPrefixSuffixTextSelector(
									_domeo.getAgentManager().getUserPerson(), 
									new MGenericResource(resource.getId(), ""), 
									selector.getExact(), selector.getPrefix(), selector.getSuffix());
							sel.setUri(jsSpecificResource.getId());
							selectors.add(sel);
						} else if(jsSpecificResource.getSelector()==null) {
							_domeo.getLogger().debug(this, "Specific Resource");
							JsResource resource = null;
							if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceString()) {
								resource = targets.get(targetSources.get(jsSpecificResource.getId()));
							} else if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceObject()) {
								resource = targets.get(jsSpecificResource.getHasSourceAsObject().getId());
							}
							_domeo.getLogger().debug(this, "Resource " + resource.getId());
							
							MSelector sel = AnnotationFactory.createImageSelector(
								_domeo.getAgentManager().getUserPerson(),  
								new MGenericResource(resource.getId(), ""), 
								_domeo.getPersistenceManager().getCurrentResource());
							sel.setUri(jsSpecificResource.getId());
							selectors.add(sel);
						}
					}
				}
				_domeo.getLogger().debug(this, "Number Selectors: " + selectors.size());
				
				AgentsFactory factory = new AgentsFactory();
				MAgentPerson annotatedBy = factory.createAgentPerson(jsAnnotatedBy.getId(), "", "", jsAnnotatedBy.getName(), "", "", "", "");
				
				if(getMotivation(annotation).equals(IOpenAnnotation.MOTIVATION_COMMENTING)) {
					_domeo.getLogger().debug(this, "Comment");
					String bodyText = null;
					boolean multipleBodies = annotation.hasMultipleBodies();
					_domeo.getLogger().debug(this, "Comment a");
					if(!multipleBodies) {
						_domeo.getLogger().debug(this, "Comment b");
						if(getObjectTypes(annotation.getBody()).toString().contains(IOpenAnnotation.CONTENT_AS_TEXT)) {
							_domeo.getLogger().debug(this, "Comment c");
							JsContentAsText body = (JsContentAsText) annotation.getBody();
							bodyText = body.getChars();
						}
					}
					MPostItAnnotation postIt = AnnotationFactory.createPostIt(aSet, annotatedBy, 
							aSet.getCreatedWith(), PostitType.COMMENT_TYPE, bodyText);
					postIt.setHasChanged(false);
					for(MSelector selector: selectors) {
						postIt.addSelector(selector);
					}	
					postIt.setIndividualUri(annotation.getId());
					postIt.setCreatedOn(annotatedAt);
					postIt.setPreviousVersion(((JsAnnotationProvenance) a).getPreviousVersion());
					if(lastSavedOn!=null) postIt.setLastSavedOn(lastSavedOn); 
					if(persist) performAnnotation(postIt);
					
					if(persist) ((AnnotationPersistenceManager)_domeo.getPersistenceManager()).addAnnotation(postIt, aSet);
					else aSet.addAnnotation(postIt);
					
					aSet.setHasChanged(false);
				} else if(getMotivation(annotation).equals(IOpenAnnotation.MOTIVATION_TAGGING)) {
					_domeo.getLogger().debug(this, "--------------------------------");
					_domeo.getLogger().debug(this, "QUALIFIER");	
					
					MQualifierAnnotation qualifier = AnnotationFactory.createQualifier(aSet, annotatedBy, aSet.getCreatedWith());
					qualifier.setHasChanged(false);
					for(MSelector selector: selectors) {
						qualifier.addSelector(selector);
					}	
					qualifier.setIndividualUri(annotation.getId());
					qualifier.setCreatedOn(annotatedAt);
					qualifier.setPreviousVersion(((JsAnnotationProvenance) a).getPreviousVersion());
					if(lastSavedOn!=null) qualifier.setLastSavedOn(lastSavedOn); 
					
					boolean multipleBodies = annotation.hasMultipleBodies();
					if(!multipleBodies) {
						JSONObject tag = new JSONObject(annotation.getBody());
						JSONObject gr = (JSONObject) tag.get(IDublinCoreTerms.source);
						MGenericResource r = ResourcesFactory.createGenericResource(gr.get(IRdfsOntology.id).isString().stringValue(), getRdfLabel(gr));
						MLinkedResource ldr = ResourcesFactory.createTrustedResource(tag.get(IRdfsOntology.id).isString().stringValue(), getRdfLabel(tag), r);
						qualifier.addTerm(ldr);
					} else {
						JSONArray tags = new JSONArray(annotation.getBodies());
						for(int k=0; k<tags.size(); k++) {
							JSONObject tag = (JSONObject) tags.get(k);
							JSONObject gr = (JSONObject) tag.get(IDublinCoreTerms.source);
							MGenericResource r = ResourcesFactory.createGenericResource(gr.get(IRdfsOntology.id).isString().stringValue(), getRdfLabel(gr));
							MLinkedResource ldr = ResourcesFactory.createTrustedResource(tag.get(IRdfsOntology.id).isString().stringValue(), getRdfLabel(tag), r);
							qualifier.addTerm(ldr);
						}
					}
					
					if(persist) performAnnotation(qualifier);
					if(persist)  ((AnnotationPersistenceManager)_domeo.getPersistenceManager()).addAnnotation(qualifier, aSet);
					else aSet.addAnnotation(qualifier);

					
				} else if(getMotivation(annotation).equals(IOpenAnnotation.MOTIVATION_HIGHLIGHTED)) {
					_domeo.getLogger().debug(this, "--------------------------------");
					_domeo.getLogger().debug(this, "HIGHLIGHT");

					MHighlightAnnotation highlight = AnnotationFactory.createHighlight(aSet, annotatedBy, aSet.getCreatedWith());
					highlight.setHasChanged(false);
					for(MSelector selector: selectors) {
						highlight.addSelector(selector);
					}	
					highlight.setIndividualUri(annotation.getId());
					highlight.setCreatedOn(annotatedAt);
					highlight.setPreviousVersion(((JsAnnotationProvenance) a).getPreviousVersion());
					if(lastSavedOn!=null) highlight.setLastSavedOn(lastSavedOn); 
					if(persist) performAnnotation(highlight);
					
					if(persist)  ((AnnotationPersistenceManager)_domeo.getPersistenceManager()).addAnnotation(highlight, aSet);
					else aSet.addAnnotation(highlight);
					
					aSet.setHasChanged(false);
				} else if(getMotivation(annotation).equals(IMicroPublicationsOntology.micropublishing)) {
					_domeo.getLogger().debug(this, "--------------------------------");
					_domeo.getLogger().debug(this, "MICROPUBLICATION");
					
					try {
						JsMicroPublication body = null;
						boolean multipleBodies = annotation.hasMultipleBodies();
						if(!multipleBodies) {
							if(getObjectType(annotation.getBody()).toString().contains(IMicroPublicationsOntology.mpMicropublication)) {
								body = (JsMicroPublication) annotation.getBody();
								cacheMp(body);
								
								_domeo.getLogger().debug(this, "---- Unmarshalling");							
								MMicroPublicationAnnotation mpAnnotation = AnnotationFactory.createMicroPublication(aSet, annotatedBy, aSet.getCreatedWith(), null);
								MMicroPublication mp = MicroPublicationFactory.loadMicroPublication((MTextQuoteSelector)selectors.get(0));
								mp.setId(body.getId());
								
								_domeo.getLogger().debug(this, "Parsing Argument");
								JSONObject argument = null;
								if(body.isArguesString()) {
									argument = assertionsCache.get(body.getArguesAsString());
								} else if(body.isArguesObject()) {
									argument = new JSONObject(body.getArguesAsObject());
								}
								
								if(argument.get(IRdfsOntology.type).isString().stringValue().equals(IMicroPublicationsOntology.mpClaim)) {
									_domeo.getLogger().debug(this, "Argument Claim");
									mp.setType(MMicroPublication.CLAIM);
								} else if(argument.get(IRdfsOntology.type).isString().stringValue().equals(IMicroPublicationsOntology.mpHypothesis)) {
									_domeo.getLogger().debug(this, "Argument Hypothesis");
									mp.setType(MMicroPublication.HYPOTHESIS);
								}			
								mp.setArgues(getStatement(argument));	
								getArguments(mp, argument);
								
								mpAnnotation.setMicroPublication(mp);				
								mpAnnotation.setHasChanged(false);
								for(MSelector selector: selectors) {
									mpAnnotation.addSelector(selector);
								}	
								mpAnnotation.setIndividualUri(annotation.getId());
								mpAnnotation.setCreatedOn(annotatedAt);
								mpAnnotation.setPreviousVersion(((JsAnnotationProvenance) a).getPreviousVersion());
								if(lastSavedOn!=null) mpAnnotation.setLastSavedOn(lastSavedOn); 
								if(persist) performAnnotation(mpAnnotation);
								
								if(persist) ((AnnotationPersistenceManager)_domeo.getPersistenceManager()).addAnnotation(mpAnnotation, aSet);
								else aSet.addAnnotation(mpAnnotation);
								
								aSet.setHasChanged(false);
							}
						}	
					} catch(Exception e) {
						_domeo.getLogger().exception(this, e.getMessage());
					}
				}
			}
		}
	}
	
	private void getArguments(MMicroPublication mp, JSONObject json) {
		if(json.containsKey(IMicroPublicationsOntology.mpQualifiedBy)) {
			_domeo.getLogger().debug(this, "Detected Qualifier");
			if(json.get(IMicroPublicationsOntology.mpQualifiedBy).isArray()!=null) {
				JSONArray array = json.get(IMicroPublicationsOntology.mpQualifiedBy).isArray();
				for(int i=0; i<array.size(); i++) {
					getQualifiers(mp, array.get(i));
				}
			} else {
				getQualifiers(mp, json.get(IMicroPublicationsOntology.mpQualifiedBy));
			}
		}
		if(json.containsKey(IMicroPublicationsOntology.mpSupportedBy)) {
			_domeo.getLogger().debug(this, "Detected Support");
			if(json.get(IMicroPublicationsOntology.mpSupportedBy).isArray()!=null) {
				JSONArray array = json.get(IMicroPublicationsOntology.mpSupportedBy).isArray();
				for(int i=0; i<array.size(); i++) {
					getArgument(true, mp, array.get(i));
				}
			} else {
				getArgument(true, mp, json.get(IMicroPublicationsOntology.mpSupportedBy));
			}
		}
		if(json.containsKey(IMicroPublicationsOntology.mpChallengedBy)) {
			_domeo.getLogger().debug(this, "Detected Challenge");
			if(json.get(IMicroPublicationsOntology.mpChallengedBy).isArray()!=null) {
				JSONArray array = json.get(IMicroPublicationsOntology.mpChallengedBy).isArray();
				for(int i=0; i<array.size(); i++) {
					getArgument(false, mp, array.get(i));
				}
			} else {
				getArgument(false, mp, json.get(IMicroPublicationsOntology.mpChallengedBy));
			}
		}
	}
	
	private void getQualifiers(MMicroPublication mp, JSONValue json) {
		_domeo.getLogger().debug(this, "Parsing Qualifier " + json.getClass().getName());
		if(json instanceof JSONString) {
			getQualifier(mp, assertionsCache.get(json.toString()));
		} else if(json instanceof JSONObject) {
			getQualifier(mp, (JSONObject) json);
		}
	}
	
	private void getQualifier(MMicroPublication mp, JSONValue json) {
		_domeo.getLogger().debug(this, "Parsing Qualifier " + json.toString());
		MGenericResource source = ResourcesFactory.createGenericResource(
				((JSONObject)((JSONObject)json).get(IDublinCoreTerms.source)).get(IRdfsOntology.id).isString().stringValue(), 
				getRdfLabel(((JSONObject)((JSONObject)json).get(IDublinCoreTerms.source))));
		_domeo.getLogger().debug(this, "getQualifier 1" );
		MLinkedResource ldr = ResourcesFactory.createTrustedResource(((JSONObject)json).get(IRdfsOntology.id).isString().stringValue(), getRdfLabel((JSONObject)json), source);
		ldr.setDescription(getRdfLabel((JSONObject)json));
		_domeo.getLogger().debug(this, "getQualifier 2" );
		MMpQualifier q = new MMpQualifier();
		q.setQualifier(ldr);


		MMpRelationship rr = new MMpRelationship(q, IMicroPublicationsOntology.mpQualifiedBy);
		//rr.setCreator(_domeo.getAgentManager().getAgentByUri(rel.getCreatedBy()));
		//rr.setId(rel.getId());
		//rr.setCreationDate(rel.getFormattedCreatedOn());
		mp.getQualifiers().add(rr);
	}
	
	private String getRdfLabel(JSONObject json) {
		if(((JSONObject)json).get(IRdfsOntology.rdfLabel)!=null && ((JSONObject)json).get(IRdfsOntology.rdfLabel).isString()!=null) {
			return ((JSONObject)json).get(IRdfsOntology.rdfLabel).isString().stringValue();
		} else if(((JSONObject)json).get(IRdfsOntology.label)!=null && ((JSONObject)json).get(IRdfsOntology.label).isString()!=null) {
			return ((JSONObject)json).get(IRdfsOntology.label).isString().stringValue();
		} 
		return null;
	}
	
	private void getArgument(boolean support, MMicroPublication mp, JSONValue json) {
		_domeo.getLogger().debug(this, "Parsing Argument");
		if(json instanceof JSONString) {
			mp.addEvidence(getRelationship(support, assertionsCache.get(json.toString())));
		} else if(json instanceof JSONObject) {
			mp.addEvidence(getRelationship(support, (JSONObject) json));
		}
	}
	
	private MMpRelationship getRelationship(boolean supporting, JSONObject json) {
		MMpElement element = getElement(json);
		if(supporting) {
			return new MMpRelationship(element, IMicroPublicationsOntology.mpSupportedBy);
		} else {
			return new MMpRelationship(element, IMicroPublicationsOntology.mpChallengedBy);
		}
	}
	
	private MMpElement getElement(JSONObject json) {
		if(json.containsKey(IRdfsOntology.type)) {
			if(json.get(IRdfsOntology.type).isString().stringValue().equals(IMicroPublicationsOntology.mpReference)) {
				_domeo.getLogger().debug(this, "Parsing Reference");
				return getReference(json);
			} else if(json.get(IRdfsOntology.type).isString().stringValue().equals(IMicroPublicationsOntology.mpDataImage)) {
				_domeo.getLogger().debug(this, "Parsing Data Image");
				return getDataImage(json);
			}
		} 
		throw new IllegalArgumentException("Micropublication statement type not detected " + json);
	}
	
	private MMpDataImage getDataImage(JSONObject j) {
		MImageInDocumentSelector selector = new MImageInDocumentSelector();
		_domeo.getLogger().debug(this, "Parsing Data Image 1 " + j);
		selector.setLocalId(0L);
		selector.setUuid("");
		_domeo.getLogger().debug(this, "Parsing Data Image 2");
		selector.setUri(((JSONObject)j.get("hasTarget")).get(IRdfsOntology.id).isString().stringValue());
		//sel.setCreatedOn(createdOn);
		
		_domeo.getLogger().debug(this, "Parsing Data Image 3");
		
		MOnlineImage oi = new MOnlineImage();
		_domeo.getLogger().debug(this, "Parsing Data Image 3 " + ((JSONObject)((JSONObject)j.get("hasTarget")).get("hasSource")).get(IRdfsOntology.id).toString());
		oi.setUrl(((JSONObject)((JSONObject)j.get("hasTarget")).get("hasSource")).get(IRdfsOntology.id).isString().stringValue());
		oi.setDisplayUrl(((JSONObject)((JSONObject)j.get("hasTarget")).get("hasSource")).get(IRdfsOntology.id).isString().stringValue());
		_domeo.getLogger().debug(this, "Parsing Data Image 3b");
		selector.setTarget(oi);
		_domeo.getLogger().debug(this, "Parsing Data Image 4 " + ((JSONObject)j.get("hasTarget")).get("hasScope").toString());
		
		String scope = null;
		if(((JSONObject)j.get("hasTarget")).get("hasScope").isString()!=null) {
			scope = ((JSONObject)j.get("hasTarget")).get("hasScope").isString().stringValue();
		} else if(((JSONObject)j.get("hasTarget")).get("hasScope").isObject()!=null) {
			scope = ((JSONObject)j.get("hasTarget")).get("hasScope").isObject().get("@id").isString().stringValue();
		}
		
		MGenericResource gr = new MGenericResource();
		gr.setUrl(scope);
		gr.setLabel(scope);
		selector.setContext(gr);
		_domeo.getLogger().debug(this, "Parsing Data Image 5");
		return new MMpDataImage(j.get(IRdfsOntology.id).isString().stringValue(), selector);
	}
	
	private MMpReference getReference(JSONObject j) {
		MPublicationArticleReference publicationReference = new MPublicationArticleReference();
		if(j.containsKey("http://purl.org/vocab/frbr/core#embodimentOf")) {
			JSONObject json = (JSONObject) j.get("http://purl.org/vocab/frbr/core#embodimentOf");
			if(json.containsKey(IRdfsOntology.id))
				publicationReference.setId(json.get(IRdfsOntology.id).isString().stringValue());
			if(json.containsKey("http://purl.org/spar/fabio#hasPubMedId"))
				publicationReference.setPubMedId(json.get("http://purl.org/spar/fabio#hasPubMedId").isString().stringValue());
			if(json.containsKey("http://purl.org/spar/fabio#hasPubMedCentralId"))
				publicationReference.setPubMedCentralId(json.get("http://purl.org/spar/fabio#hasPubMedCentralId").isString().stringValue());
			if(json.containsKey("http://prismstandard.org/namespaces/basic/2.0/doi"))
				publicationReference.setDoi(json.get("http://prismstandard.org/namespaces/basic/2.0/doi").isString().stringValue());	
			if(json.containsKey("http://purl.org/spar/fabio#hasPII"))
				publicationReference.setPublisherItemId(json.get("http://purl.org/spar/fabio#hasPII").isString().stringValue());
			if(json.containsKey("http://purl.org/dc/terms/title")) {
				publicationReference.setTitle(json.get("http://purl.org/dc/terms/title").isString().stringValue());
			}
			if(json.containsKey("dcterms:title")) {
				publicationReference.setTitle(json.get("dcterms:title").isString().stringValue());
			}
			if(json.containsKey("http://example.org/authorNames"))
				publicationReference.setAuthorNames(json.get("http://example.org/authorNames").isString().stringValue());
			if(json.containsKey("http://example.org/publicationInfo"))
				publicationReference.setJournalPublicationInfo(json.get("http://example.org/publicationInfo").isString().stringValue());
		} else {
			if(j.containsKey(IMicroPublicationsOntology.mpCitation)) {
				publicationReference.setUnrecognized(j.get(IMicroPublicationsOntology.mpCitation).isString().stringValue());
			}
		}
		_domeo.getLogger().debug(this, "Parsed" + COLUMN + PubMedCitationPainter.getFullCitationPlainString(publicationReference, _domeo));
		
		MMpReference reference = new MMpReference();
		reference.setReference(publicationReference);
		return reference;
	}
	
	private MMpStatement getStatement(JSONObject json) {
		if(json.containsKey(IRdfsOntology.id) && json.containsKey(IMicroPublicationsOntology.mpStatement)) {
			MMpStatement argues = new MMpStatement();
			argues.setId(json.get(IRdfsOntology.id).isString().stringValue());
			argues.setText(json.get(IMicroPublicationsOntology.mpStatement).isString().stringValue());
			return argues;
		} else {
			boolean flag = false;
			StringBuffer sb = new StringBuffer();
			sb.append("MMpStatement" + COLUMN);
			if(!json.containsKey(IRdfsOntology.id)) {
				sb.append(IRdfsOntology.id);
				flag = true;
			} else {
				if(flag) sb.append(COMMA);
				sb.append(IMicroPublicationsOntology.mpStatement);
			}
			throw new IllegalArgumentException(sb.append(COMMA).append(json).toString());
		}
	}
	
	private Map<String, JavaScriptObject> asserts = new HashMap<String, JavaScriptObject>();
	private String arguesId;

	// ------------------------------------------------------------------------
	//  MICROPUBLICATIONS CACHING
	// ------------------------------------------------------------------------	
	private void cacheMp(JsMicroPublication mp) {
		_domeo.getLogger().debug(this, "---- Caching");
		cacheMpArgues(mp);
		cachingMpElements(mp);
		_domeo.getLogger().debug(this, "Caching completed with " + assertionsCache.size() + " items");
	}
	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	
	// ------------------------------------------------------------------------
	//  ARGUES MICROPUBLICATIONS CACHING
	// ------------------------------------------------------------------------
	private void cacheMpArgues(JsMicroPublication mp) {
		_domeo.getLogger().debug(this, "Caching Argues...");
		if(mp.isArguesString()) {
			_domeo.getLogger().debug(this, "* skipping: " + mp.getArguesAsString());
		} else if(mp.isArguesObject()) {
			cacheMpAssertion(new JSONObject(mp.getArguesAsObject()));
		}
	}	
	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	
	// ------------------------------------------------------------------------
	//  ASSERTS MICROPUBLICATIONS CACHING
	// ------------------------------------------------------------------------
	private void cacheMpAsserts(JsMicroPublication mp) {
		_domeo.getLogger().debug(this, "Caching Asserts...");
		if(mp.isAssertString()) {
			_domeo.getLogger().debug(this, "* skipping: " + mp.getAssertAsString());
		} else if(mp.isAssertObject()) {
			cacheMpAssertion(new JSONObject(mp.getAssertAsObject()));
		} else {
			_domeo.getLogger().debug(this, "Detected list");
			cacheMpAssertions(new JSONArray(mp.getAssertsAsObject()));			
		}
	}
	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	
	
	// ------------------------------------------------------------------------
	//  GENERIC MICROPUBLICATIONS CACHING
	// ------------------------------------------------------------------------
	private Map<String, JSONObject> assertionsCache = new HashMap<String, JSONObject>();
	
	private void cacheMpAssertion(JSONObject assertion) {
		_domeo.getLogger().debug(this, "* caching: " + assertion.get(IRdfsOntology.id) + " of type " + assertion.get(IRdfsOntology.type));
		assertionsCache.put(assertion.get(IRdfsOntology.id).toString(), assertion);
		if(assertion.get(IMicroPublicationsOntology.mpSupportedBy)!=null) 
			cacheMpAssertions(assertion.get(IMicroPublicationsOntology.mpSupportedBy));
		if(assertion.get(IMicroPublicationsOntology.mpChallengedBy)!=null) 
			cacheMpAssertions(assertion.get(IMicroPublicationsOntology.mpChallengedBy));
	}
	
	private void cacheMpAssertions(JSONValue assertions) {
		if(assertions.isObject()!=null) {
			cacheMpAssertion(assertions.isObject());
		} else if(assertions.isArray()!=null) {
			cacheMpAssertion(assertions.isArray());
		}
	}
	
	private void cacheMpAssertion(JSONArray assertions) {
		for(int x = 0; x<assertions.size(); x++ ) {
			if(assertions.get(x).isObject()!=null) {
				cacheMpAssertion(assertions.get(x).isObject());
			} else {
				_domeo.getLogger().debug(this, "* skipping: " + assertions.get(x).getClass().getName());
			}
		}
	}
	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	
	private void cachingMpElements(JsMicroPublication mp) {
		_domeo.getLogger().debug(this, "Caching Asserts...");
		if(mp.isAssertString()) {
			_domeo.getLogger().debug(this, "Skipping Assert String...");
		} else if(mp.isAssertObject()) {
			_domeo.getLogger().debug(this, "Caching Assert as Object");
			asserts.put(mp.getAssertAsObject().getId(), mp.getAssertAsObject());
		} else {
			_domeo.getLogger().debug(this, "Caching Asserts as list");
			cacheMpAssertions(new JSONArray(mp.getAssertsAsObject()));			
		}
	}
	
	private void performAnnotation(MAnnotation ann) {
		for(int z=0; z<ann.getSelectors().size(); z++) {
			try {
				if(ann.getSelectors().get(z) instanceof MTextQuoteSelector) {
					HtmlUtils.performAnnotation(Long.toString(ann.getLocalId())+((ann.getSelectors().size()>1)?(":"+ann.getSelectors().get(z).getLocalId()):""), 
							((MTextQuoteSelector)ann.getSelectors().get(z)).getExact(), 
							((MTextQuoteSelector)ann.getSelectors().get(z)).getPrefix(), 
							((MTextQuoteSelector)ann.getSelectors().get(z)).getSuffix(), 
							HtmlUtils.createSpan(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement(), 0L),
							_domeo.getCssManager().getStrategy().getObjectStyleClass(ann));
				} 
				
				/*
				else if(ann.getSelectors().get(z) instanceof MImageInDocumentSelector) {
					// Place the annotation???
					_domeo.getLogger().info(this, "image;;;" + ((MOnlineImage)((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget()).getUrl());
					Element image = HtmlUtils.getImage(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement(), 
							((MOnlineImage)((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget()).getUrl(), 
							((MOnlineImage)((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget()).getXPath());
					if(image!=null) {
						ann.setY(((com.google.gwt.user.client.Element) image).getAbsoluteTop());
						((MOnlineImage)((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget()).setImage((com.google.gwt.user.client.Element) image);
						_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(ann, 
								((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget(), (com.google.gwt.user.client.Element) image);
						_domeo.getLogger().info(this, "caching annotation for image;;;" + ((MOnlineImage)((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget()).getUrl());
						_domeo.getAnnotationPersistenceManager().cacheAnnotationOfImage(((MOnlineImage)((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget()).getUrl(), ann);
					} else {
						_domeo.getLogger().exception(this, "Image element not found!");
					}

					// TODO add to the image cache
					
				} else if(ann.getSelectors().get(z) instanceof MAnnotationSelector) {
					//Window.alert("Individual url: " + ((MAnnotationSelector) ann.getSelectors().get(z)).getAnnotation().getIndividualUri() + " - " + ann.getIndividualUri());
					//this.cacheForLazyBinding(((MAnnotationSelector) ann.getSelectors().get(z)).getAnnotation().getIndividualUri(), 
					//		ann, (MAnnotationSelector) ann.getSelectors().get(z));
				}
				*/
			} catch(Exception e) {
				_domeo.getLogger().exception(this, e.getMessage());
			}
		}
	}
	
	// ------------------------------------------------------------------------
	//  General: Identifier and Types
	// ------------------------------------------------------------------------
	private final native String getObjectId(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
	private final native String getObjectType(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;
	private final native boolean hasMultipleTypes(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType] instanceof Array; 
	}-*/;	
	private final native JsArrayString getObjectTypes(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  Open Annotation
	// ------------------------------------------------------------------------
	private final native String getMotivation(Object obj) /*-{ 
		return obj.motivatedBy; 
	}-*/;
	
	private final native boolean isObjectString(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType].constructor === String; 
	}-*/;
}
