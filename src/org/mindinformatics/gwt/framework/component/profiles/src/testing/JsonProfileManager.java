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
package org.mindinformatics.gwt.framework.component.profiles.src.testing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.mindinformatics.gwt.framework.component.agents.model.JsoAgent;
import org.mindinformatics.gwt.framework.component.agents.src.AgentsFactory;
import org.mindinformatics.gwt.framework.component.profiles.model.JsoProfile;
import org.mindinformatics.gwt.framework.component.profiles.model.JsoProfileEntry;
import org.mindinformatics.gwt.framework.component.profiles.model.MProfile;
import org.mindinformatics.gwt.framework.component.profiles.src.AProfileManager;
import org.mindinformatics.gwt.framework.model.agents.IPerson;
import org.mindinformatics.gwt.framework.src.Utils;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonProfileManager extends AProfileManager {

	public JsonProfileManager(IApplication application, ICommandCompleted callbackCompleted) {
		super(application, callbackCompleted);
	}	

	@Override
	public void retrieveUserProfiles() {
		String url = GWT.getModuleBaseURL() + "profile/"+_application.getUserManager().getUser().getUserName()+"/all?format=json";
		if(!_application.isHostedMode())
			url = Utils.getUrlBase(GWT.getModuleBaseURL()) + "profile/"+_application.getUserManager().getUser().getUserName()+"/all?format=json";
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
	    builder.setHeader("Content-type", "application/json");

		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					_application.getInitializer().addException("Couldn't retrieve User Profiles JSON");
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						ArrayList<MProfile> userProfiles = new ArrayList<MProfile>();
 						JsArray profiles = (JsArray) parseJson(response.getText());
						for(int j=0; j<profiles.length(); j++) {
							JsoProfile jsoProfile = (JsoProfile)profiles.get(j);
							
							MProfile profile = new MProfile();
							profile.setUuid(jsoProfile.getUuid());
							profile.setName(jsoProfile.getName());
							profile.setDescription(jsoProfile.getDescription());
							profile.setLastSavedOn(jsoProfile.getCreatedOn());
							
							JsArray<JavaScriptObject> creators = jsoProfile.getCreatedBy();
							if(getObjectType(creators.get(0)).equals(IPerson.TYPE)) {
								AgentsFactory factory = new AgentsFactory();
								JsoAgent jperson = (JsoAgent) creators.get(0);
								profile.setLastSavedBy((IPerson)factory.createAgent(jperson));
							}
							
							JsArray<JsoProfileEntry> plugins = jsoProfile.getStatusPlugins();
							for(int i=0; i<plugins.length(); i++) {
								JsoProfileEntry entry = plugins.get(i);
								profile.getPlugins().put(entry.getName(), entry.getStatus());
							}
							
							JsArray<JsoProfileEntry> features = jsoProfile.getStatusFeatures();
							for(int i=0; i<features.length(); i++) {
								JsoProfileEntry entry = features.get(i);
								profile.getFeatures().put(entry.getName(), entry.getStatus());
							}
							
							userProfiles.add(profile);
						}

						setProfiles(userProfiles);
						stageCompleted();
					} else {
						_application.getInitializer().addException("Couldn't retrieve User Profiles JSON ("
								+ response.getStatusText() + ")");
					}
				}
			});
		} catch (RequestException e) {
			_application.getInitializer().addException("Couldn't retrieve User Profiles JSON");
		}
	}
	
	private final native String getObjectType(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;

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
	    		.replace(/\\'/g, "\\'");
		  	return JSON.parse(jsonStr);
		} catch (e) {
			alert("Error while parsing the JSON message: " + e);
		}
	}-*/;

	@Override
	public void retrieveUserCurrentProfile() {
		
		String url = GWT.getModuleBaseURL() + "profile/"+_application.getUserManager().getUser().getUserName()+"/info?format=json";
		if(!_application.isHostedMode())
			url = Utils.getUrlBase(GWT.getModuleBaseURL()) + "profile/"+_application.getUserManager().getUser().getUserName()+"/info?format=json";
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
	    builder.setHeader("Content-type", "application/json");

		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					_application.getInitializer().addException("Couldn't retrieve User Profile JSON");
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						JsoProfile jsoProfile = (JsoProfile)((JsArray) parseJson(response.getText())).get(0);
						
						MProfile profile = new MProfile();
						profile.setUuid(jsoProfile.getUuid());
						profile.setName(jsoProfile.getName());
						profile.setDescription(jsoProfile.getDescription());
						profile.setLastSavedOn(jsoProfile.getCreatedOn());
						
						AgentsFactory factory = new AgentsFactory();
						JsArray<JavaScriptObject> creators = jsoProfile.getCreatedBy();
						if(getObjectType(creators.get(0)).equals(IPerson.TYPE)) {
							JsoAgent person = (JsoAgent) creators.get(0);
							profile.setLastSavedBy((IPerson) factory.createAgent(person));
						}
						
						JsArray<JsoProfileEntry> plugins = jsoProfile.getStatusPlugins();
						for(int i=0; i<plugins.length(); i++) {
							JsoProfileEntry entry = plugins.get(i);
							profile.getPlugins().put(entry.getName(), entry.getStatus());
						}
						
						JsArray<JsoProfileEntry> features = jsoProfile.getStatusFeatures();
						for(int i=0; i<features.length(); i++) {
							JsoProfileEntry entry = features.get(i);
							profile.getFeatures().put(entry.getName(), entry.getStatus());
						}
						
						setCurrentProfile(profile);
						stageCompleted();
					} else {
						_application.getInitializer().addException("Couldn't retrieve User Profile JSON ("
								+ response.getStatusText() + ")");
					}
				}
			});
		} catch (RequestException e) {
			_application.getInitializer().addException("Couldn't retrieve User Profile JSON");
		}
	}

	@Override
	public MProfile saveUserProfile(MProfile newProfile, final IUpdateProfileCallback callback) {
		String url = GWT.getModuleBaseURL() + "profile/"+_application.getUserManager().getUser().getUserName()+"/info?format=json";
	    RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
	    builder.setHeader("Content-type", "application/json");

	    HashMap<String, String> pluginsStatus = callback.getPluginsStatus();
	    HashMap<String, String> featuresStatus = callback.getFeaturesStatus();
	    
	    StringBuffer postData=new StringBuffer();
		postData.append("["); 
		postData.append("  {"); 
		postData.append("    \"uuid\": \"");
		postData.append(newProfile.getUuid());
		postData.append("\",");
		postData.append("    \"name\": \""); 
		postData.append(newProfile.getName());
		postData.append("\",");
		postData.append("    \"description\": \""); 
		postData.append(newProfile.getDescription());
		postData.append("\",");
		postData.append("    \"createdon\": \"");
		DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy HH:mm:ss Z");
		postData.append(fmt.format(new Date()));
		postData.append("\",");
		postData.append("    \"createdby\": [");
		postData.append("      {");
		postData.append("        \"@id\": \"");
		postData.append(_application.getAgentManager().getUserPerson().getUri());
		postData.append("\",");
		postData.append("        \"@type\": \"");
		postData.append("foafx:Person");
		postData.append("\",");
		postData.append("        \"foafx:name\": \"");
		postData.append(_application.getAgentManager().getUserPerson().getName());
		postData.append("\"");
		postData.append("      }");
		postData.append("     ],");
		
		postData.append("    \"statusplugins\": [");
		for(String key: pluginsStatus.keySet()) {
			postData.append("      {");
			postData.append("        \"uuid\": \"");
			postData.append(key);
			postData.append("\",");
			postData.append("        \"status\": \"");
			postData.append(pluginsStatus.get(key));
			postData.append("\"");
			postData.append("      },");
		}	
		postData.append("     ],");
		
		postData.append("    \"statusfeatures\": [");
		for(String key: featuresStatus.keySet()) {
			postData.append("      {");
			postData.append("        \"uuid\": \"");
			postData.append(key);
			postData.append("\",");
			postData.append("        \"status\": \"");
			postData.append(featuresStatus.get(key));
			postData.append("\"");
			postData.append("      },");
		}	
		postData.append("     ]");
		
		postData.append("  }"); 
		postData.append("]");
	    
		try {
			Request request = builder.sendRequest(postData.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					_application.getInitializer().addException("Couldn't retrieve User Profile JSON");
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {

						JsoProfile jsoProfile = (JsoProfile)((JsArray) parseJson(response.getText())).get(0);
						
						MProfile profile = new MProfile();
						profile.setUuid(jsoProfile.getUuid());
						profile.setName(jsoProfile.getName());
						profile.setDescription(jsoProfile.getDescription());
						profile.setLastSavedOn(jsoProfile.getCreatedOn());
						
						AgentsFactory factory = new AgentsFactory();
						JsArray<JavaScriptObject> creators = jsoProfile.getCreatedBy();
						if(getObjectType(creators.get(0)).equals(IPerson.TYPE)) {
							JsoAgent person = (JsoAgent) creators.get(0);
							profile.setLastSavedBy((IPerson) factory.createAgent(person));
						}
						
						JsArray<JsoProfileEntry> plugins = jsoProfile.getStatusPlugins();
						for(int i=0; i<plugins.length(); i++) {
							JsoProfileEntry entry = plugins.get(i);
							profile.getPlugins().put(entry.getName(), entry.getStatus());
						}
						
						JsArray<JsoProfileEntry> features = jsoProfile.getStatusFeatures();
						for(int i=0; i<features.length(); i++) {
							JsoProfileEntry entry = features.get(i);
							profile.getFeatures().put(entry.getName(), entry.getStatus());
						}
						
						setCurrentProfile(profile);
						callback.updateCurrentProfile();
					} else {
						_application.getInitializer().addException("Couldn't retrieve User Profile JSON ("
								+ response.getStatusText() + ")");
					}
				}
			});
		} catch (RequestException e) {
			_application.getInitializer().addException("Couldn't retrieve User Profile JSON");
		}
		return newProfile;
	}

	@Override
	public void saveCurrentProfile(MProfile currentProfile, final IUpdateProfileCallback callback) {
		String url = GWT.getModuleBaseURL() + "profile/"+_application.getUserManager().getUser().getUserName()+"/save?format=json";
		if(!_application.isHostedMode())
			url = Utils.getUrlBase(GWT.getModuleBaseURL()) + "profile/"+_application.getUserManager().getUser().getUserName()+"/save?format=json";
		
	    RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
	    builder.setHeader("Content-type", "application/json");
	    
	    HashMap<String, String> pluginsStatus = callback.getPluginsStatus();
	    HashMap<String, String> featuresStatus = callback.getFeaturesStatus();
	    
	    StringBuffer postData=new StringBuffer();
		postData.append("["); 
		postData.append("  {"); 
		postData.append("    \"uuid\": \"");
		postData.append(currentProfile.getUuid());
		postData.append("\",");
		postData.append("    \"name\": \""); 
		postData.append(currentProfile.getName());
		postData.append("\",");
		postData.append("    \"description\": \""); 
		postData.append(currentProfile.getDescription());
		postData.append("\",");
		postData.append("    \"createdOn\": \"");
		DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy HH:mm:ss Z");
		postData.append(fmt.format(new Date()));
		postData.append("\",");
		postData.append("    \"createdBy\": [");
		postData.append("      {");
		postData.append("        \"@id\": \"");
		//Window.alert(""+currentProfile.getLastSavedBy());
		postData.append(currentProfile.getLastSavedBy().getUri());
		postData.append("\",");
		postData.append("        \"@type\": \"");
		postData.append("foafx:Person");
		postData.append("\",");
		postData.append("        \"foafx:name\": \"");
		postData.append(currentProfile.getLastSavedBy().getName());
		postData.append("\"");
		postData.append("      }");
		postData.append("     ],");
		postData.append("    \"plugins\": [");
		
		for(String key: pluginsStatus.keySet()) {
			postData.append("      {");
			postData.append("        \"uuid\": \"");
			postData.append(key);
			postData.append("\",");
			postData.append("        \"status\": \"");
			postData.append(pluginsStatus.get(key));
			postData.append("\"");
			postData.append("      },");
		}
		postData.append("     ],");
		
		postData.append("    \"statusfeatures\": [");
		for(String key: featuresStatus.keySet()) {
			postData.append("      {");
			postData.append("        \"uuid\": \"");
			postData.append(key);
			postData.append("\",");
			postData.append("        \"status\": \"");
			postData.append(featuresStatus.get(key));
			postData.append("\"");
			postData.append("      },");
		}	
		postData.append("     ]");
		
		postData.append("  }"); 
		postData.append("]");
	    
	    try {
			Request request = builder.sendRequest(postData.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					_application.getInitializer().addException("Couldn't retrieve User Profile JSON");
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {

						/*
						JsoProfile jsoProfile = (JsoProfile)((JsArray) parseJson(response.getText())).get(0);
						
						MProfile profile = new MProfile();
						profile.setUuid(jsoProfile.getUuid());
						profile.setName(jsoProfile.getName());
						profile.setDescription(jsoProfile.getDescription());
						profile.setLastSavedOn(jsoProfile.getCreatedOn());
						
						AgentsFactory factory = new AgentsFactory();
						JsArray<JavaScriptObject> creators = jsoProfile.getCreatedBy();
						if(getObjectType(creators.get(0)).equals(IPerson.TYPE)) {
							JsoAgent person = (JsoAgent) creators.get(0);
							profile.setLastSavedBy((IPerson) factory.createAgent(person));
						}
						
						JsArray<JsoProfileEntry> plugins = jsoProfile.getStatusPlugins();
						for(int i=0; i<plugins.length(); i++) {
							JsoProfileEntry entry = plugins.get(i);
							profile.getPlugins().put(entry.getName(), entry.getStatus());
						}
						
						setCurrentProfile(profile);
						*/
						
						callback.updateCurrentProfile();
					} else {
						_application.getInitializer().addException("Couldn't retrieve User Profile JSON ("
								+ response.getStatusText() + ")");
					}
				}
			});
		} catch (RequestException e) {
			_application.getInitializer().addException("Couldn't retrieve User Profile JSON");
			callback.updateCurrentProfile();
		}
	}
}
