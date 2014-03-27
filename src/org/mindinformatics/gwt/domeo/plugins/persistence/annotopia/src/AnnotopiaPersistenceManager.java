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

import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.APersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IPersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingAnnotationSetHandler;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingAnnotationSetListHandler;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingBibliographySetHandler;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaSetsResultWrapper;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.MAnnotopiaAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.ui.existingsets.ExistingAnnotationViewerPanel;
import org.mindinformatics.gwt.framework.component.ui.glass.EnhancedGlassPanel;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.plugins.ajax.Ajax;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotopiaPersistenceManager extends APersistenceManager implements IPersistenceManager {

	public AnnotopiaPersistenceManager(IDomeo domeo, ICommandCompleted callback) {
		super(domeo, callback);
	}
	
	public static native JavaScriptObject parseJson(String jsonStr) /*-{
		try {
			var jsonStr = jsonStr      
	    		.replace(/[\\]/g, '\\\\')
	    		.replace(/[\/]/g, '\\/')
	    		.replace(/[\b]/g, '\\b')
	    		.replace(/[\f]/g, '\\f')
	    		.replace(/[\n]/g, '\\n')
	    		.replace(/[\r]/g, '\\r')
	    		.replace(/[\t]/g, '\\t')
	    		.replace(/[\\][\"]/g, '\\\\\"')
	    		.replace(/\\'/g, "\\'");
	    	//alert(jsonStr);
		  	return JSON.parse(jsonStr);
		} catch (e) {
			alert("Error while parsing the JSON message: " + e);
		}
	}-*/;

	@Override
	public void retrieveExistingAnnotationSetList(IRetrieveExistingAnnotationSetListHandler handler) {
		_application.getLogger().debug(this, "Beginning retrieving list of existing annotation sets...");
		_application.getProgressPanelContainer().setProgressMessage("Retrieving list of existing annotation sets from Annotopia...");
		
		String url = "http://127.0.0.1:8080/s/annotationset";
		
		try {
			Ajax.ajax(Ajax.createSettings()
				.setUrl(url)
		        .setDataType("json") // txt, json, jsonp, xml
		        .setType("get")      // post, get
		        .setData(GQuery.$$("apiKey: testkey")) // parameters for the query-string
		        .setTimeout(10000)
		        .setSuccess(new Function(){ // callback to be run if the request success
		    		public void f() {
		    			IDomeo _domeo = ((IDomeo)_application);
		    			//Window.alert(getDataProperties().toJsonString());
		    			JsAnnotopiaSetsResultWrapper wrapper = 
		    				(JsAnnotopiaSetsResultWrapper) parseJson(getDataProperties().toJsonString());
		    			AnnotopiaUnmarshaller unmarshalelr = new AnnotopiaUnmarshaller();
		    			List<MAnnotopiaAnnotationSet> sets = unmarshalelr.unmarshallAnnotationSetsList(wrapper);
		    			
		    			//_application.getLogger().debug(this, "Completed Execution of checkForExistingAnnotationSets() in " + (System.currentTimeMillis()-documentPipelineTimer)+ "ms");

		    			if(sets.size()==0) {
		    				// TODO message no annotation found
		    				_application.getLogger().info(this, "No annotation sets found");
		    				//this.getProgressPanelContainer().setCompletionMessage("No annotation exist for this document");
		    			} else {
		    				//this.getProgressPanelContainer().hide();
		    				try {
		    					ExistingAnnotationViewerPanel lwp = new ExistingAnnotationViewerPanel((IDomeo)_application, sets);
		    					new EnhancedGlassPanel((IDomeo)_application, lwp, lwp.getTitle(), false, false, false);
		    					//_dialogPanel.hide();
		    				} catch (Exception e) {
		    					_application.getLogger().exception(this, "Exeption in visualizing existing annotation");
		    					//_dialogPanel.hide();
		    				}		
		    			}
		    			//Window.alert(responseOnSets.getStatus() + " " + responseOnSets.getResult().getDuration());
		    		}
		        })
		        .setError(new Function(){ // callback to be run if the request fails
		        	public void f() {
		        		_application.getLogger().exception(this, 
		        			"Couldn't complete existing annotation sets list retrieval process");
		        		_application.getProgressPanelContainer().setErrorMessage(
							"Couldn't complete existing annotation sets list retrieval process");
		        	}
		        })
		     );
		} catch (Exception e) {
			_application.getLogger().exception(this, "Couldn't complete existing annotation sets list retireval");
		}
	}
	
	@Override
	public void retrieveExistingAnnotationSets(List<String> ids, IRetrieveExistingAnnotationSetHandler handler) {
		// TODO Auto-generated method stub	
	}
	
	@Override
	public void saveAnnotation() {
		// TODO Auto-generated method stub
	}

	@Override
	public void retrieveExistingBibliographySet(IRetrieveExistingBibliographySetHandler handler) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void saveBibliography() {
		// TODO Auto-generated method stub		
	}
}
