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
import java.util.HashSet;
import java.util.List;

import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.IAnnotopia;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaAnnotationSetGraphs;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaAnnotationSetSummary;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaSetsResultWrapper;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.MAnnotopiaAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.MAnnotopiaPerson;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotopiaUnmarshaller {

	/**
	 * Unmarshalls the annotation sets list summaries ignoring
	 * the sets details.
	 * @return Annotation Sets list summary
	 */
	public List<MAnnotopiaAnnotationSet> unmarshallAnnotationSetsList(JsAnnotopiaSetsResultWrapper wrapper) {
		List<MAnnotopiaAnnotationSet> sets = new ArrayList<MAnnotopiaAnnotationSet>();
		
		
		MAnnotopiaAnnotationSet set = new MAnnotopiaAnnotationSet();
		JsArray<JsAnnotopiaAnnotationSetGraphs>  jsSets = wrapper.getResult().getSets();
		for(int i=0; i<jsSets.length(); i++) {
			JsArray<JavaScriptObject> graphs = jsSets.get(i).getGraphs();
			for(int j=0; j<graphs.length(); j++) {	
				JavaScriptObject jsItem = graphs.get(j);
				
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
				
				if(typesSet.contains("at:AnnotationSet")) {
					JsAnnotopiaAnnotationSetSummary jsSet = (JsAnnotopiaAnnotationSetSummary) jsItem;
					set.setId(getObjectId(jsItem));
					
					set.setLabel(jsSet.getLabel());
					set.setDescription(jsSet.getDescription());
					set.setNumberAnnoations(jsSet.getNumberOfAnnotationItems());
					set.setCreatedOn(jsSet.getFormattedCreatedOn());
					MAnnotopiaPerson createdBy = new MAnnotopiaPerson();
					createdBy.setId(jsSet.getCreatedBy().getId());
					createdBy.setName(jsSet.getCreatedBy().getName());
					set.setCreatedBy(createdBy);
				}
			}
			
			set.setType(IAnnotopia.ANNOTATION_SET_ID);
			set.setLocked(false);
			set.setVisible(true);
			
			
//			set.setCreatedBy(createdBy);
//			set.setCreatedWith(createdWith);
//			set.setCreatedOn(createdOn);
//			set.setLineageUri(lineageUri);
//			set.setVersionNumber(versionNumber);
//			set.setPreviousVersion(previousVersion);
			sets.add(set);
		}		
		return sets;
	}
	
	// ------------------------------------------------------------------------
	//  General
	// ------------------------------------------------------------------------
	private final native String getObjectId(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
	
	private final native boolean hasMultipleTypes(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType] instanceof Array; 
	}-*/;
	private final native String getObjectType(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;
	private final native JsArrayString getObjectTypes(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;
	
}
