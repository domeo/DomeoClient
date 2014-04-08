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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.IAnnotopia;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.IOpenAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaAgent;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaAnnotationSetGraph;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaAnnotationSetGraphs;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaAnnotationSetSummary;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaSetsResultWrapper;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsOpenAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsSpecificResource;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.MAnnotopiaAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.MAnnotopiaPerson;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.MAnnotopiaSoftware;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotopiaUnmarshaller {

	IDomeo _domeo;
	
	/**
	 * Constructor
	 * @param domeo	The main application pointer
	 */
	public AnnotopiaUnmarshaller(IDomeo domeo) {
		_domeo = domeo;
	}
	
	/**
	 * Unmarshalls the annotation sets list summaries ignoring
	 * the sets details.
	 * @return Annotation Sets list summary
	 */
	public List<MAnnotopiaAnnotationSet> unmarshallAnnotationSetsList(JsAnnotopiaSetsResultWrapper wrapper) {
		List<MAnnotopiaAnnotationSet> sets = new ArrayList<MAnnotopiaAnnotationSet>();
		
		JsArray<JsAnnotopiaAnnotationSetGraphs>  jsSets = wrapper.getResult().getSets();
		for(int i=0; i<jsSets.length(); i++) {			
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
					MAnnotopiaAnnotationSet set = unmarshallAnnotationSet(jsItem, typesSet);
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
	
	public MAnnotationSet unmarshallAnnotationSet(JsAnnotopiaAnnotationSetGraph wrapper) {		
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
				MAnnotopiaAnnotationSet set = unmarshallAnnotationSet(jsItem, typesSet);
				
				if(set!=null) {
					unmarshallAnnotations(jsItem);
					
					// Translation to the old internal formats
					MAgentSoftware createdWith = new MAgentSoftware();
					createdWith.setUri(set.getCreatedWith().getId());
					createdWith.setName(set.getCreatedWith().getName());
					
					MAgentPerson createdBy = new MAgentPerson();
					createdBy.setUri(set.getCreatedBy().getId());
					createdBy.setName(set.getCreatedBy().getName());
					
					// Annotation Set
					// Note: currently the lastSavedOn is forcing createdOn
					MAnnotationSet aSet = AnnotationFactory.createAnnotationSet(set.getId(), set.getId(), set.getCreatedOn(), set.getVersionNumber(), set.getPreviousVersion(),
							 _domeo.getPersistenceManager().getCurrentResource(), set.getLabel(), set.getDescription());		
					aSet.setCreatedWith(createdWith);
					aSet.setCreatedBy(createdBy);
					aSet.setCreatedOn(set.getCreatedOn());
					return aSet;
				}
			} catch(Exception e) {
				_domeo.getLogger().exception(this, e.getMessage());
			}
		} else {
			_domeo.getLogger().exception(this, "Unrecognized format");
		}	
		return null;
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
	
	private void unmarshallAnnotations(JavaScriptObject jsItem) {
		HashMap<String, JsAnnotopiaAgent> agents = new HashMap<String, JsAnnotopiaAgent>();
		HashMap<String, String> annotationAgents = new HashMap<String, String>();
		
		HashMap<String, JsAnnotopiaAgent> targets = new HashMap<String, JsAnnotopiaAgent>();
		HashMap<String, String> targetSources = new HashMap<String, String>();
		
		JsAnnotopiaAnnotationSetSummary jsSet = (JsAnnotopiaAnnotationSetSummary) jsItem;
		
		// Unmarshalling agents
		for(int i=0; i<jsSet.getAnnotations().length(); i++) {
			JavaScriptObject a = jsSet.getAnnotations().get(i);
			if(getObjectType(a).equals(IOpenAnnotation.ANNOTATION)) {
				// Unmarshall annotatedBy
				JsOpenAnnotation annotation = (JsOpenAnnotation) a;
				if(annotation.getAnnotatedBy()!=null && annotation.isAnnotatedByString()) {
					annotationAgents.put(annotation.getId(), annotation.getAnnotatedByAsString());
				} else if(annotation.getAnnotatedBy()!=null && annotation.isAnnotatedByObject()) {
					agents.put(annotation.getAnnotatedByAsObject().getId(), annotation.getAnnotatedByAsObject());
				}
				
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
		
		// Creation of annotation items
		for(int i=0; i<jsSet.getAnnotations().length(); i++) {
			JavaScriptObject a = jsSet.getAnnotations().get(i);
			if(getObjectType(a).equals(IOpenAnnotation.ANNOTATION)) {
				JsOpenAnnotation annotation = (JsOpenAnnotation) a;
				
				// AnnotatedBy
				JsAnnotopiaAgent annotatedBy = null;
				if(annotation.getAnnotatedBy()!=null && annotation.isAnnotatedByString()) {
					annotatedBy = agents.get(annotationAgents.get(annotation.getId()));
				} else if(annotation.getAnnotatedBy()!=null && annotation.isAnnotatedByObject()) {
					annotatedBy = agents.get(annotationAgents.get(annotation.getAnnotatedByAsObject().getId()));
				}
				
				// Unmarshall targets
				ArrayList<JsSpecificResource> specificResources = new ArrayList<JsSpecificResource>();
				boolean multipleTargets = annotation.hasMultipleTargets();
				if(multipleTargets) {
					JsArray<JavaScriptObject> jsTargets = annotation.getTargets();
					for(int t=0; t<jsTargets.length(); t++) {
						JavaScriptObject jsTarget = jsTargets.get(t);
						if(getObjectType(jsTarget).contains(IOpenAnnotation.SPECIFIC_RESOURCE)) {
							JsSpecificResource jsSpecificResource = (JsSpecificResource) jsTarget;
							if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceString()) {
								targets.get(targetSources.get(jsSpecificResource.getId()));
							} else if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceObject()) {
								targets.get(targetSources.get(jsSpecificResource.getHasSourceAsObject().getId()));
							}
						}
					}
				} else {
					JavaScriptObject jsTarget = annotation.getTarget();
					if(getObjectType(jsTarget).contains(IOpenAnnotation.SPECIFIC_RESOURCE)) {
						JsSpecificResource jsSpecificResource = (JsSpecificResource) jsTarget;
						if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceString()) {
							targets.get(targetSources.get(jsSpecificResource.getId()));
						} else if(jsSpecificResource.getHasSource()!=null && jsSpecificResource.isHasSourceObject()) {
							targets.get(targetSources.get(jsSpecificResource.getHasSourceAsObject().getId()));
						}
					}
				}
			}
		}
		
		Window.alert("Agents: " + agents.size());
		Window.alert("Sources: " + targets.size());
	}
	
	private MAnnotopiaAnnotationSet unmarshallAnnotationSet(JavaScriptObject jsItem, HashSet<String> typesSet) {
		if(typesSet.contains(IAnnotopia.ANNOTATION_SET_NS)) {
			MAnnotopiaAnnotationSet set = new MAnnotopiaAnnotationSet();
			set.setType(IAnnotopia.ANNOTATION_SET_ID);
			set.setLocked(false);
			set.setVisible(true);
			
			JsAnnotopiaAnnotationSetSummary jsSet = (JsAnnotopiaAnnotationSetSummary) jsItem;
			set.setId(notNullableString("id", jsSet.getId()));	
			set.setLabel(notNullableString("label", jsSet.getLabel()));
			set.setDescription(nullableString("description", jsSet.getDescription()));
			
			set.setNumberAnnotations(jsSet.getNumberOfAnnotationItems());
			
			// Created on
			if(jsSet.getCreatedOn()!=null && !jsSet.getCreatedOn().isEmpty()) {
				try {
					set.setCreatedOn(jsSet.getFormattedCreatedOn());
				} catch (Exception e) {
					_domeo.getLogger().exception(this, "Problems in parsing createdOn " 
						+ jsSet.getCreatedOn() + " - " + e.getMessage());
				}
			}
			// Created by
			if(jsSet.getCreatedBy()!=null
					&& jsSet.getCreatedBy().getId()!=null && !jsSet.getCreatedBy().getId().isEmpty()
					&& jsSet.getCreatedBy().getName()!=null && !jsSet.getCreatedBy().getName().isEmpty()) {
				MAnnotopiaPerson createdBy = new MAnnotopiaPerson();
				createdBy.setId(jsSet.getCreatedBy().getId());
				createdBy.setName(jsSet.getCreatedBy().getName());
				set.setCreatedBy(createdBy);
			} else {
				_domeo.getLogger().exception(this, "No createdOn detected for set " + jsSet.getId());
			}
			
			// Created with
			if(jsSet.getCreatedWith()!=null
					&& jsSet.getCreatedWith().getId()!=null && !jsSet.getCreatedWith().getId().isEmpty()
					&& jsSet.getCreatedWith().getName()!=null && !jsSet.getCreatedWith().getName().isEmpty()) {
				MAnnotopiaSoftware createdWith = new MAnnotopiaSoftware();
				createdWith.setId(jsSet.getCreatedWith().getId());
				createdWith.setName(jsSet.getCreatedWith().getName());
				set.setCreatedWith(createdWith);
			} else {
				_domeo.getLogger().exception(this, "No createdWith detected for set " + jsSet.getId());
			}
			
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
			// Last saved by
			if(jsSet.getLastSavedBy()!=null
					&& jsSet.getLastSavedBy().getId()!=null && !jsSet.getLastSavedBy().getId().isEmpty()
					&& jsSet.getLastSavedBy().getName()!=null && !jsSet.getLastSavedBy().getName().isEmpty()) {
				MAnnotopiaPerson lastSavedBy = new MAnnotopiaPerson();
				lastSavedBy.setId(jsSet.getLastSavedBy().getId());
				lastSavedBy.setName(jsSet.getLastSavedBy().getName());
				set.setLastSavedBy(lastSavedBy);
			} else {
				_domeo.getLogger().warn(this, "No lastSavedBy detected for set " + jsSet.getId() + " - using createdBy (TODO ?)");
			}

			// Versioning
			set.setPreviousVersion(jsSet.getPreviousVersion());
			set.setVersionNumber(jsSet.getVersionNumber());
			return set;
		} else {
			_domeo.getLogger().warn(this, "Item does not contain a recognized Set type " + typesSet);
			return null;
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
}
