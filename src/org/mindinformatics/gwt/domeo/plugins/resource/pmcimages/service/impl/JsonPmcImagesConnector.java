/*
 * Copyright 2013 Massachusetts General Hospital
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
package org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.service.impl;

import java.util.HashMap;

import org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.model.JsPmcImage;
import org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.service.IPmcImagesConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.service.IPmcImagesRequestCompleted;
import org.mindinformatics.gwt.framework.src.Utils;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonPmcImagesConnector implements IPmcImagesConnector {

	protected IApplication _application;
	
	public JsonPmcImagesConnector(IApplication application) {
		_application = application;
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
	public void retrievePmcImagesData(final IPmcImagesRequestCompleted completionCallback,
			String pmid, String pmcid, String doi)
			throws IllegalArgumentException {

		
		if(_application.isHostedMode()) { 
			String response = "[{\"uysie:hasCaption\": \"Representative self-terminating radical reactions.\"," +
					"\"uysie:hasFullText\": \"Most organic radical reactions occur through a cascade of two or more individual steps [1,2]. Knowledge of the nature and rates of these steps in other words, the mechanism of the reaction is of fundamental interest and is also important in synthetic planning. In synthesis, both the generation of the initial radical of the cascade and the removal of the final radical are crucial events [3]. Many useful radical reactions occur through chains that provide a naturally coupled regulation of radical generation and removal. Among the non-chain methods, generation and removal of radicals by oxidation and reduction are important, as is the\"," +
					"\"uysie:hasFileName\": \"nihms28314f1\"," +
					"\"uysie:hasTitle\": \"Do alpha-acyloxy and alpha-alkoxycarbonyloxy radicals fragment to form acyl and alkoxycarbonyl radicals?\"}]";
			
			@SuppressWarnings("unchecked")
			JsArray<JsPmcImage> responseOnSets = (JsArray<JsPmcImage>) parseJson(response);
			HashMap<String, JsPmcImage> images = new HashMap<String, JsPmcImage>();
			for(int i=0; i<responseOnSets.length(); i++) {
				images.put(responseOnSets.get(i).getName(), responseOnSets.get(i));
			}
			
			completionCallback.returnPmcImagesData(images);
			return;
		}
			
		String requestUrl = Utils.getUrlBase(GWT.getModuleBaseURL())+ "yaleImageFinder/retrievePmcImagesData?format=json";
		try {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, requestUrl);
			builder.setHeader("Content-Type", "application/json");
	
			JSONObject request = new JSONObject();
			if(pmid!=null) request.put("pmid", new JSONString(pmid));
			if(pmcid!=null) request.put("pmcid", new JSONString(pmcid));
			if(doi!=null) request.put("doi", new JSONString(doi));
			
			JSONArray messages = new JSONArray();
			messages.set(0, request);
			
			builder.setTimeoutMillis(20000);
			builder.setRequestData(messages.toString());
			builder.setCallback(new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					if(exception instanceof RequestTimeoutException) {
						_application.getLogger().exception(this, "Couldn't load images metadata (timeout) " + exception.getMessage());
						completionCallback.pmcImagesDataNotFound();
//						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not load images metadata  (timeout)");
//						handler.bibliographySetListNotCreated("Could not load existing bibliography  (timeout)");
					} else {
						_application.getLogger().exception(this, "Couldn't load images metadata");
						completionCallback.pmcImagesDataNotFound();
//						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not load existing bibliography (onError)");
//						handler.bibliographySetListNotCreated("Could not load existing bibliography (onError)");
					}
				}
	
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						try {
							_application.getLogger().debug(this, response.getText());
							@SuppressWarnings("unchecked")
							JsArray<JsPmcImage> responseOnSets = (JsArray<JsPmcImage>) parseJson(response.getText());
							HashMap<String, JsPmcImage> images = new HashMap<String, JsPmcImage>();
							for(int i=0; i<responseOnSets.length(); i++) {
								images.put(responseOnSets.get(i).getName(), responseOnSets.get(i));
							}					
							completionCallback.returnPmcImagesData(images);			
						} catch(Exception e) {
							_application.getLogger().exception(this, "Could not parse images metadata " + e.getMessage());
							completionCallback.pmcImagesDataNotFound();
//							((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not parse existing bibliography  " + e.getMessage());
//							handler.bibliographySetListNotCreated("Could not parse existing bibliography " + e.getMessage() + " - "+ response.getText());
						}
					} else if (503 == response.getStatusCode()) {
						_application.getLogger().exception(this, "Existing bibliography by url 503: " + response.getText());
						completionCallback.pmcImagesDataNotFound();
//						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve existing bibliography  " + response.getStatusCode());
//						handler.bibliographySetListNotCreated("Could not retrieve existing bibliography " + response.getStatusCode() + " - "+ response.getText());
						//completionCallback.textMiningNotCompleted(response.getText());
					} else {
						_application.getLogger().exception(this,  "Load images metadata " + response.getStatusCode() + ": "+ response.getText());
//						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve existing bibliography  " + response.getStatusCode());
//						handler.bibliographySetListNotCreated("Could not retrieve existing bibliography " + response.getStatusCode() + " - "+ response.getText());
						//handler.setExistingBibliographySetList(new JsArray(), true);
						//completionCallback.textMiningNotCompleted(response.getText());
						completionCallback.pmcImagesDataNotFound();
					}
				}
			});
			builder.send();
			
		} catch (RequestException e) {
			_application.getLogger().exception(this, "Couldn't save annotation");
			completionCallback.pmcImagesDataNotFound();
			
		}	
	}
}
