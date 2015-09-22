package org.mindinformatics.gwt.domeo.plugins.annotation.persistence.testing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.service.AnnotationPersistenceServiceFacade;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.APersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IPersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingAnnotationSetHandler;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingAnnotationSetListHandler;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingBibliographySetHandler;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsoAnnotationSetSummary;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsoAnnotationSummary;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonSerializerManager;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model.MPubMedDocument;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentDatabase;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.model.agents.IAgent;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.src.ApplicationUtils;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;

public class JsonPersistenceManager extends APersistenceManager implements IPersistenceManager {

    boolean ELASTICO = true;
	DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss Z"); // "2012-09-24T19:57:13Z"
	
	public JsonPersistenceManager(IDomeo domeo, ICommandCompleted callback) {
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
	
	public void saveBibliography() {
		_application.getLogger().debug(this, "Beginning saving bibliography...");
		_application.getProgressPanelContainer().setProgressMessage("Saving bibliography...");
		
		if(_application.isHostedMode()) {

			Window.alert("Saving not supported in Hosted Mode");

			JsonSerializerManager jsonSerializerManager = JsonSerializerManager.getInstance(((IDomeo)_application));
			if(((IDomeo)_application).getAnnotationPersistenceManager().getBibliographicSet().getHasChanged()) {
				MAnnotationSet set = (((IDomeo)_application).getAnnotationPersistenceManager().getBibliographicSet());
				Window.alert(jsonSerializerManager.serialize(set).toString());
				((IDomeo)_application).getAnnotationPersistenceManager().getBibliographicSet().setHasChanged(false);
				((IDomeo)_application).getAnnotationPersistenceManager().getBibliographicSet().setVersionNumber("1");
				((IDomeo)_application).getAnnotationPersistenceManager().getBibliographicSet().setLastSavedOn(new Date());
				for(MAnnotation ann: set.getAnnotations()) {
					ann.setHasChanged(false);
				}
				_application.getLogger().info(this, "Bibliography saved");
				((IDomeo)_application).refreshAllComponents();
			}						
			
			return;
		}
		
		JsonSerializerManager jsonSerializerManager = JsonSerializerManager.getInstance(((IDomeo)_application));
		String url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL())+ "persistence/saveBibliography?format=json"; 

		try {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
			builder.setHeader("Content-Type", "application/json");
			builder.setTimeoutMillis(40000);
			
			if(((IDomeo)_application).getAnnotationPersistenceManager().getBibliographicSet().getHasChanged()) {
				
				JSONObject bibliographicSet = jsonSerializerManager.serialize(((IDomeo)_application).getAnnotationPersistenceManager().getBibliographicSet());
				
				int jsonCounter = 0;
				JSONArray agents = new JSONArray();
				for(IAgent agent: jsonSerializerManager.getAgentsToSerialize()) {
					if(agent instanceof MAgentPerson) {
						MAgentPerson person = (MAgentPerson) agent;
						JSONValue json = jsonSerializerManager.serialize(person);
						if(json!=null) agents.set(jsonCounter++, json);
					} else if(agent instanceof MAgentSoftware) {
						MAgentSoftware software = (MAgentSoftware) agent;
						JSONValue json = jsonSerializerManager.serialize(software);
						if(json!=null) agents.set(jsonCounter++, json);
					} else if(agent instanceof MAgentDatabase) {
						MAgentDatabase software = (MAgentDatabase) agent;
						JSONValue json = jsonSerializerManager.serialize(software);
						if(json!=null) agents.set(jsonCounter++, json);
					}
				}
				bibliographicSet.put(IDomeoOntology.agents, agents);
				
				jsonSerializerManager.clearAgentsToSerialize();
				
				// TODO save only changed items
				builder.setRequestData(bibliographicSet.toString());
				builder.setCallback(new RequestCallback() {
					public void onError(Request request, Throwable exception) {
						if(exception instanceof RequestTimeoutException) {
							_application.getLogger().exception(this, "Bibliography not saved (timeout) " + exception.getMessage());
							_application.getProgressPanelContainer().setErrorMessage("Bibliography not saved (timeout)!");
						} else {
							_application.getLogger().exception(this, "Bibliography not saved " + exception.getMessage());
							_application.getProgressPanelContainer().setErrorMessage("Bibliography not saved!");
						}
					}
	
					public void onResponseReceived(Request request, Response response) {
						if (200 == response.getStatusCode()) {
							try {

								IDomeo _domeo = ((IDomeo)_application);
								//Window.alert(response.getText());
								JsArray responseOnSets = (JsArray) parseJson(response.getText());
								//Window.alert("Sets #: "+responseOnSets.length());

								if(responseOnSets.length()==1) {
									JsoAnnotationSetSummary summary = (JsoAnnotationSetSummary) responseOnSets.get(0);
									MAnnotationSet set = _domeo.getAnnotationPersistenceManager().getBibliographicSet();
									if(set!=null) {
										//if(summary.getNewId()!=null && !summary.getNewId().equals("undefined")) {
										//	set.setIndividualUri(summary.getId());
										//}
										set.setIndividualUri(summary.getId());
										set.setLineageUri(summary.getLineageUri());
										set.setPreviousVersion(summary.getPreviousVersion());
										set.setVersionNumber(summary.getVersionNumber());
										set.setLastSavedOn(fmt.parse(summary.getLastSaved()));
										set.setHasChanged(false);

										/*
										JsArray<JsoAnnotationSummary> annSummaries = summary.getAnnotationsSummary();
										for(int j=0; j<annSummaries.length(); j++) {
											JsoAnnotationSummary annSummary = annSummaries.get(j);
											MAnnotation ann = _domeo.getAnnotationPersistenceManager().getAnnotationByLocalId(Long.parseLong(annSummary.getLocalId()));
											ann.setLineageUri(summary.getLineageUri());
											ann.setPreviousVersion(summary.getPreviousVersion());
											ann.setVersionNumber(summary.getVersionNumber());
											ann.setLastSavedOn(fmt.parse(summary.getLastSaved()));
											ann.setHasChanged(false);
											ann.setNewVersion(false);
										}
										*/
										;
									} else {
										_application.getLogger().exception(this, "Couldn't match Annotation Set (" + response.getText() + ")");
									}
								}
								_application.getProgressPanelContainer().setCompletionMessage("Bibliography saved!");
								_application.getLogger().debug(this, "Completed saving bibliography!");
								((IDomeo)_application).refreshAllComponents();
							} catch(Exception e) {
								_application.getLogger().exception(this, "Couldn't parse User JSON ("
										+ response.getText() + ")");
							}
						} else if (500 == response.getStatusCode()) {
							if(response.getHeader("Content-Type").contains("application/json")) {							
								String ticket = "";
								String message = "";
								JsArray responseOnSets = (JsArray) parseJson(response.getText());
								if(responseOnSets.length()==1) {
									JSONObject obj = new JSONObject(responseOnSets.get(0));
									if(obj.get("@type").isString().stringValue().equals("Exception")) {
										ticket = obj.get("ticket").isString().stringValue();
										message = obj.get("message").isString().stringValue();
									}
								} else {
									_application.getLogger().warn(this, "Error message not formatted properly");
								}
								_application.getLogger().exception(this, "Couldn't complete bibliography saving process (ticket: " + ticket + "); " + message);
								_application.getProgressPanelContainer().setErrorMessage("Bibliography not saved!");
							} else {
								_application.getLogger().exception(this, "Couldn't complete bibliography saving process (" + response.getStatusCode() + " - " + response.getStatusText() + ")");
								_application.getProgressPanelContainer().setErrorMessage("Bibliography not saved!");
							}
						} else {
							_application.getLogger().exception(this, "Couldn't complete JSON Bibliography saving process (" + response.getStatusCode() + " - " + response.getStatusText() + ")");
							_application.getProgressPanelContainer().setErrorMessage("Bibliography not saved!");
						}	
					}
				});
				builder.send();
			} else {
				_application.getLogger().debug(this, "Bibliography not saved as unchanged");
				_application.getProgressPanelContainer().setCompletionMessage("No bibliography to be saved");
			}
		} catch (RequestException e) {
			_application.getLogger().exception(this, "Couldn't save bibliography");
			_application.getProgressPanelContainer().setErrorMessage("Couldn't save bibliography");
		}
	}
	
//	public void textmine() {
//		IDomeo _domeo = (IDomeo) _application;
//		_application.getLogger().debug(this, "Beginning textminning...");
//		_application.getProgressPanelContainer().setProgressMessage("Textmining selection...");
//		
//		// TODO Hidious!!!!!
//		IFrameElement iframe = IFrameElement.as(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement());
//		final Document frameDocument = iframe.getContentDocument();
//		_domeo.getContentPanel().getAnnotationFrameWrapper().getSelectionText(_domeo.getContentPanel().getAnnotationFrameWrapper(), frameDocument);
//		
//		if(_domeo.getContentPanel().getAnnotationFrameWrapper().matchText!=null && _domeo.getContentPanel().getAnnotationFrameWrapper().matchText.length()>2) { 
//			
//			BioPortalManager bioPortalManager = BioPortalManager.getInstance();
//			bioPortalManager.selectBioPortalConnector(_domeo);
//			bioPortalManager.textmine(new TextMiningManager(_domeo), _domeo.getPersistenceManager().getCurrentResourceUrl(), _domeo.getContentPanel().getAnnotationFrameWrapper().matchText , "");
//			
//			/*
//			NifManager nifManager = NifManager.getInstance();
//			nifManager.selectConnector(_domeo);
//			nifManager.annotate(new TextMiningManager(_domeo), _domeo.getPersistenceManager().getCurrentResourceUrl(), _domeo.getContentPanel().getAnnotationFrameWrapper().matchText , "");
//			*/
//		} else {
//			_application.getLogger().debug(this, "No text to textmine...");
//			_domeo.getContentPanel().getAnnotationFrameWrapper().clearSelection(frameDocument);
//			_domeo.getToolbarPanel().deselectAnalyze();
//			_domeo.getProgressPanelContainer().setWarningMessage("No text has been selected for textmining!");
//		}
//	}

	public void saveAnnotation() {
		_application.getLogger().debug(this, "Beginning saving annotation...");
		_application.getProgressPanelContainer().setProgressMessage("Saving annotation...");
		
		if(_application.isHostedMode()) {
			
			Window.alert("Saving not supported in Hosted Mode");
			JsonSerializerManager jsonSerializerManager = JsonSerializerManager.getInstance(((IDomeo)_application));
			
			ArrayList<MAnnotationSet> setToSerialize = new ArrayList<MAnnotationSet>();
			for(MAnnotationSet set: ((IDomeo)_application).getAnnotationPersistenceManager().getAllDiscussionSets()) {
				if(set.getHasChanged() && set.getAnnotations().size()>0) 
					setToSerialize.add(set);
			}
			for(MAnnotationSet set: ((IDomeo)_application).getAnnotationPersistenceManager().getAllUserSets()) {
				if(set.getHasChanged() && set.getAnnotations().size()>0) 
					setToSerialize.add(set);
			}

			JSONArray sets = new JSONArray();
			for(int i=0; i<setToSerialize.size(); i++) {
				// Add all agents
				JSONObject jsonSet = jsonSerializerManager.serialize(setToSerialize.get(i));
				
				int jsonCounter = 0;
				JSONArray agents = new JSONArray();
				for(IAgent agent: jsonSerializerManager.getAgentsToSerialize()) {
					if(agent instanceof MAgentPerson) {
						MAgentPerson person = (MAgentPerson) agent;
						JSONValue json = jsonSerializerManager.serialize(person);
						if(json!=null) agents.set(jsonCounter++, json);
					} else if(agent instanceof MAgentSoftware) {
						MAgentSoftware software = (MAgentSoftware) agent;
						JSONValue json = jsonSerializerManager.serialize(software);
						if(json!=null) agents.set(jsonCounter++, json);
					} else if(agent instanceof MAgentDatabase) {
						MAgentDatabase software = (MAgentDatabase) agent;
						JSONValue json = jsonSerializerManager.serialize(software);
						if(json!=null) agents.set(jsonCounter++, json);
					}
				}
				jsonSet.put(IDomeoOntology.agents, agents);
				
				JSONValue selfReference = jsonSerializerManager.serializeSelfReference();
				if(selfReference!=null) {
					jsonSet.put("domeo:reference", selfReference);
				}
				
				int jsonResourcesCounter = 0;
				JSONArray resources = new JSONArray();
				for(MGenericResource resource: jsonSerializerManager.getResourcesToSerialize()) {
					 if(resource instanceof MPubMedDocument) {
						 
						MPubMedDocument document = (MPubMedDocument) resource;
						Window.alert(document.getUrl());
						//JSONValue json = jsonSerializerManager.serialize(document);
						JSONObject r = new JSONObject();
						r.put("url", new JSONString(document.getUrl()));
						r.put("label", new JSONString(document.getLabel()));					
						if(document.getSelfReference().getReference() instanceof MPublicationArticleReference) {
							MPublicationArticleReference ref = ((MPublicationArticleReference)document.getSelfReference().getReference());
							if(ref.getPubMedId()!=null && ref.getPubMedId().trim().length()>0) r.put("pmid", new JSONString(ref.getPubMedId()));
							if(ref.getDoi()!=null && ref.getDoi().trim().length()>0) r.put("doi", new JSONString(ref.getDoi()));
						}
						resources.set(jsonResourcesCounter++, r);
					} else if(resource instanceof MGenericResource) {
						MGenericResource gResource = (MGenericResource) resource;
						JSONObject r = new JSONObject();
						r.put("url", new JSONString(gResource.getUrl()));
						r.put("label", new JSONString(gResource.getLabel()));				
						resources.set(jsonResourcesCounter++, r);
					} 
					 
				}
				jsonSet.put(IDomeoOntology.resources, resources);
				
				sets.set(i, jsonSet);
				jsonSerializerManager.clearAgentsToSerialize();
			}
			
			if(setToSerialize.size()>0) {
			    JSONObject res = new JSONObject();
                res.put("sets", sets);
                Window.alert(res.toString());
                
				_application.getLogger().info(this, "Annotation saved");
			} else {
				_application.getLogger().info(this, "No new annotation to be saved");
			}
			return;
		}
		
		JsonSerializerManager jsonSerializerManager = JsonSerializerManager.getInstance(((IDomeo)_application));
		String url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL())+ "persistence/saveAnnotation?format=json";

		try {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
			builder.setHeader("Content-Type", "application/json");
			builder.setTimeoutMillis(40000);
			
			ArrayList<MAnnotationSet> setToSerialize = new ArrayList<MAnnotationSet>();
			for(MAnnotationSet set: ((IDomeo)_application).getAnnotationPersistenceManager().getAllDiscussionSets()) {
				if(set.getHasChanged() && set.getAnnotations().size()>0) 
					setToSerialize.add(set);
			}
			for(MAnnotationSet set: ((IDomeo)_application).getAnnotationPersistenceManager().getAllUserSets()) {
				if(set.getHasChanged() && set.getAnnotations().size()>0) 
					setToSerialize.add(set);
			}
			/*
			if(((IDomeo)_application).getAnnotationPersistenceManager().getBibliographicSet().getHasChanged()) 
				setToSerialize.add(((IDomeo)_application).getAnnotationPersistenceManager().getBibliographicSet());
				*/
			
			JSONArray sets = new JSONArray();
			for(int i=0; i<setToSerialize.size(); i++) {
				// Add all agents
				JSONObject jsonSet = jsonSerializerManager.serialize(setToSerialize.get(i));

				int jsonCounter = 0;
				JSONArray agents = new JSONArray();
				for(IAgent agent: jsonSerializerManager.getAgentsToSerialize()) {
					if(agent instanceof MAgentPerson) {
						MAgentPerson person = (MAgentPerson) agent;
						JSONValue json = jsonSerializerManager.serialize(person);
						if(json!=null) agents.set(jsonCounter++, json);
					} else if(agent instanceof MAgentSoftware) {
						MAgentSoftware software = (MAgentSoftware) agent;
						JSONValue json = jsonSerializerManager.serialize(software);
						if(json!=null) agents.set(jsonCounter++, json);
					}
				}
				jsonSet.put(IDomeoOntology.agents, agents);
				
				JSONValue selfReference = jsonSerializerManager.serializeSelfReference();
				if(selfReference!=null) {
					jsonSet.put("domeo:reference", selfReference);
				}
				
				int jsonResourcesCounter = 0;
				JSONArray resources = new JSONArray();
				for(MGenericResource resource: jsonSerializerManager.getResourcesToSerialize()) {
					 if(resource instanceof MPubMedDocument) {
						MPubMedDocument document = (MPubMedDocument) resource;
						//JSONValue json = jsonSerializerManager.serialize(document);
						JSONObject r = new JSONObject();
						r.put("url", new JSONString(document.getUrl()));
						r.put("label", new JSONString(document.getLabel()));					
						if(document.getSelfReference().getReference() instanceof MPublicationArticleReference) {
							MPublicationArticleReference ref = ((MPublicationArticleReference)document.getSelfReference().getReference());
							if(ref.getPubMedId()!=null && ref.getPubMedId().trim().length()>0) r.put("pmid", new JSONString(ref.getPubMedId()));
							if(ref.getDoi()!=null && ref.getDoi().trim().length()>0) r.put("doi", new JSONString(ref.getDoi()));
						}
						resources.set(jsonResourcesCounter++, r);
					} else if(resource instanceof MGenericResource) {
						MGenericResource gResource = (MGenericResource) resource;
						JSONObject r = new JSONObject();
						r.put("url", new JSONString(gResource.getUrl()));
						r.put("label", new JSONString(gResource.getLabel()));
						resources.set(jsonResourcesCounter++, r);
					} 
				}
				jsonSet.put(IDomeoOntology.resources, resources);
				
				sets.set(i, jsonSet);
				
				jsonSerializerManager.clearAgentsToSerialize();
			}
			
			if(sets.size()>0) { 
			    
			    //if(ELASTICO) {
	                JSONObject res = new JSONObject();
	                res.put("sets", sets);
	                builder.setRequestData(res.toString());
	            //} else {
	            //   builder.setRequestData(sets.toString());
	            //}
			    
				builder.setCallback(new RequestCallback() {
					public void onError(Request request, Throwable exception) {
						if(exception instanceof RequestTimeoutException) {
							_application.getLogger().exception(this, "Could not save the annotation (timeout)");
							_application.getProgressPanelContainer().setErrorMessage("Annotation not saved (timeout)!");
						} else {
							_application.getLogger().exception(this, "Could not save the annotation " + exception.getMessage());
							_application.getProgressPanelContainer().setErrorMessage("Annotation not saved!");
						}
					}
	
					public void onResponseReceived(Request request, Response response) {
						if (200 == response.getStatusCode()) {
							_application.getProgressPanelContainer().setProgressMessage("Received response");
							try {
								IDomeo _domeo = ((IDomeo)_application);
								//Window.alert(response.getText());
								JsArray responseOnSets = (JsArray) parseJson(response.getText());

								//Window.alert("Sets #: "+responseOnSets.length());
								for(int i=0; i<responseOnSets.length(); i++) {
									JsoAnnotationSetSummary summary = (JsoAnnotationSetSummary) responseOnSets.get(i);
									MAnnotationSet set = _domeo.getAnnotationPersistenceManager().getAnnotationSetByLocalId(summary.getLocalId());
									if(set!=null) {
										_application.getLogger().info(this, "Updating set " + summary.getLocalId());
										
										// TODO verify if newId is really necessary
										if(summary.getNewId()!=null && !summary.getNewId().equals("undefined")) {
											_application.getLogger().info(this, "Updating id to " + summary.getNewId());
											set.setIndividualUri(summary.getNewId());
											_application.getLogger().info(this, "Set " + set.getLabel() + " new id " + set.getIndividualUri());
										}
										set.setLineageUri(summary.getLineageUri());
										set.setPreviousVersion(summary.getPreviousVersion());
										set.setVersionNumber(summary.getVersionNumber());
										set.setLastSavedOn(fmt.parse(summary.getLastSaved()));
										set.setHasChanged(false);
										JsArray<JsoAnnotationSummary> annSummaries = summary.getAnnotationsSummary();
										for(int j=0; j<annSummaries.length(); j++) {
											JsoAnnotationSummary annSummary = annSummaries.get(j);
											MAnnotation ann = _domeo.getAnnotationPersistenceManager().getAnnotationByLocalId(Long.parseLong(annSummary.getLocalId()));
											ann.setLineageUri(summary.getLineageUri());
											ann.setPreviousVersion(summary.getPreviousVersion());
											ann.setVersionNumber(summary.getVersionNumber());
											ann.setLastSavedOn(fmt.parse(summary.getLastSaved()));
											ann.setHasChanged(false);
											ann.setNewVersion(false);
										}
									} else {
										_application.getLogger().exception(this, "Couldn't load Annotation Set (" + response.getText() + ")");
										_application.getProgressPanelContainer().setErrorMessage("Couldn't load Annotation Set");
									}
								}
								
								_application.getLogger().debug(this, "Completed saving annotation!");
								_application.getProgressPanelContainer().setCompletionMessage("Annotation saved!");
							} catch(Exception e) {
								_application.getLogger().exception(this, "Couldn't parse Annotation Set save response "
										+ response.getText() + ")" + e.getMessage());
								_application.getProgressPanelContainer().setErrorMessage("Couldn't parse Annotation Set save response " 
										+ e.getMessage());
							}
						} else if (500 == response.getStatusCode()) {
							if(response.getHeader("Content-Type").contains("application/json")) {							
								String ticket = "";
								String message = "";
								JsArray responseOnSets = (JsArray) parseJson(response.getText());
								if(responseOnSets.length()==1) {
									JSONObject obj = new JSONObject(responseOnSets.get(0));
									if(obj.get("@type").isString().stringValue().equals("Exception")) {
										ticket = obj.get("ticket").isString().stringValue();
										message = obj.get("message").isString().stringValue();
									}
								} else {
									_application.getLogger().warn(this, "Error message not formatted properly");
								}
								_application.getLogger().exception(this, "Couldn't complete saving process (ticket: " + ticket + "); " + message);
								_application.getProgressPanelContainer().setErrorMessage("Annotation not saved!");
							} else {
								_application.getLogger().exception(this, "Couldn't complete JSON saving process (" + response.getStatusCode() + " - " + response.getStatusText() + ")");
								_application.getProgressPanelContainer().setErrorMessage("Annotation not saved!");
							}
						} else {
							_application.getLogger().exception(this, "Couldn't complete JSON saving process (" + response.getStatusCode() + " - " + response.getStatusText() + ")");
							_application.getProgressPanelContainer().setErrorMessage("Annotation not saved!");
						}	
					}
				});
				builder.send();
			} else {
				_application.getLogger().info(this, "No new annotation to be saved");
				_application.getProgressPanelContainer().setCompletionMessage("No new annotation to be saved");
			}
		} catch (RequestException e) {
			_application.getLogger().exception(this, "Couldn't save annotation");
			_application.getProgressPanelContainer().setCompletionMessage("Couldn't save annotation");
		}
	}
	
	public void retrieveExistingBibliographySet(IRetrieveExistingBibliographySetHandler handler) {
		_application.getLogger().debug(this, "Beginning retrieving bibliography...");
		if(_application.isHostedMode()) {
			AnnotationPersistenceServiceFacade f = new AnnotationPersistenceServiceFacade();
			handler.setExistingBibliographySetList(f.retrieveBibliographyByDocumentUrl(((IDomeo)_application).getPersistenceManager().getCurrentResource().getUrl()), true);
			return;
		}
	}
	

	public void retrieveExistingAnnotationSets(final List<String> ids, final IRetrieveExistingAnnotationSetHandler handler) {
		
		_application.getLogger().debug(this, "Beginning retrieving annotation sets...");
		if(_application.getProgressPanelContainer()!=null) 
			_application.getProgressPanelContainer().setProgressMessage("[INFO] Retrieving existing annotation...");

		if(_application.isHostedMode()) {
			AnnotationPersistenceServiceFacade f = new AnnotationPersistenceServiceFacade();
			handler.loadExistingAnnotationSetList(f.retrieveAnnotationById("id", "idType", "allowed", "username"), 1);	
			return;
		}
		
		String url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL())+ "persistence/retrieveExistingAnotationSets?format=json";

		try {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
			builder.setHeader("Content-Type", "application/json");
			builder.setTimeoutMillis(40000);
			
			JSONArray idsList = new JSONArray();
			for(int j=0; j<ids.size(); j++) {
				idsList.set(j, new JSONString(ids.get(j)));
			}
			
			JSONObject request = new JSONObject();
			request.put("url", new JSONString(((IDomeo)_application).getPersistenceManager().getCurrentResource().getUrl()));
			request.put("user", new JSONString(((IDomeo)_application).getUserManager().getUser().getUserName()));
			request.put("key", new JSONString("to-be-defined"));
			request.put("ids", idsList);
			
			JSONArray messages = new JSONArray();
			messages.set(0, request);
			
			builder.setRequestData(messages.toString());
			builder.setCallback(new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					if(exception instanceof RequestTimeoutException) {
						_application.getLogger().exception(this, "Could not retrieve the annotation (timeout)");
						if(_application.getProgressPanelContainer()!=null) 
							_application.getProgressPanelContainer().setErrorMessage("Annotation not retrieved (timeout)!");
					} else {
						_application.getLogger().exception(this, "Could not retrieve the annotation " + exception.getMessage());
						if(_application.getProgressPanelContainer()!=null) 
							_application.getProgressPanelContainer().setErrorMessage("Annotation not retrieved!");
					}
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						try {
							if(Domeo.verbose) _application.getLogger().debug(this, "Received: " +response.getText() );
							parseJson(response.getText());
							if(Domeo.verbose) _application.getLogger().debug(this, "Message parsed I");
							
							
							JsArray responseOnSets = (JsArray) parseJson(response.getText());
							if(Domeo.verbose) _application.getLogger().debug(this, "Message parsed II");
							handler.loadExistingAnnotationSetList(responseOnSets, ids.size());
							if(Domeo.verbose) _application.getLogger().debug(this, "Response parsing done!");
						} catch(Exception e) {
							_application.getLogger().exception(this, e.getMessage());
							_application.getLogger().exception(this, "Couldn't complete annotation retrieval process (" + response.getText() + ")");
							if(_application.getProgressPanelContainer()!=null) 
								_application.getProgressPanelContainer().setErrorMessage("Couldn't complete annotation retrieval process ("+ response.getText() + ")");
						}
					} else {
						_application.getLogger().exception(this, "Couldn't complete annotation retrieval process ("
								+ response.getStatusCode() + " - " + response.getStatusText() + ")");
						//_application.getDialogPanel().hideSoon();
						_application.getProgressPanelContainer().setErrorMessage("Couldn't complete annotation retrieval process ("
							+ response.getStatusCode() + " - " + response.getStatusText() + ")");
					}	
				}
			});
			builder.send();
		} catch (RequestException e) {
			_application.getProgressPanelContainer().setProgressMessage("[INFO] Retrieving existing annotation:" + e.getStackTrace());
			
			_application.getLogger().exception(this, "Couldn't load the annotation");
		}
	}
	
	public void retrieveExistingAnnotationSetList(final IRetrieveExistingAnnotationSetListHandler handler) {
		
		_application.getLogger().debug(this, "Beginning retrieving annotation...");
		_application.getProgressPanelContainer().setProgressMessage("Retrieving list of existing annotation...");
		
		if(_application.isHostedMode()) {
			AnnotationPersistenceServiceFacade f = new AnnotationPersistenceServiceFacade();
			handler.setExistingAnnotationSetList(f.retrieveAnnotationByDocumentUrl(((IDomeo)_application).getPersistenceManager().getCurrentResource().getUrl()));
			return;
		}
		
		JsonSerializerManager jsonSerializerManager = JsonSerializerManager.getInstance(((IDomeo)_application));
		String url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL())+ "persistence/retrieveExistingAnotationSetsList?format=json";

		try {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
			builder.setHeader("Content-Type", "application/json");
			builder.setTimeoutMillis(40000);
			
			JSONObject request = new JSONObject();
			request.put("url", new JSONString(((IDomeo)_application).getPersistenceManager().getCurrentResource().getUrl()));
			request.put("person", new JSONString(((IDomeo)_application).getAgentManager().getUserPerson().getUri()));
			request.put("user", new JSONString(((IDomeo)_application).getUserManager().getUser().getUserName()));
			request.put("key", new JSONString("to-be-defined"));
			
			JSONArray messages = new JSONArray();
			messages.set(0, request);
			
			builder.setRequestData(messages.toString());
			builder.setCallback(new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					if(exception instanceof RequestTimeoutException) {
						_application.getLogger().exception(this, "Could not retrieve the annotation list (timeout)");						
						_application.getProgressPanelContainer().setErrorMessage("Annotation not retrieved (timeout)!");
					} else {
						_application.getLogger().exception(this, "Could not retrieve the annotation list  " + exception.getMessage());
						_application.getProgressPanelContainer().setErrorMessage("Annotation list not retrieved!");
					}
				}

				public void onResponseReceived(Request request, Response response) {
					_application.getLogger().debug(this, "onResponseReceived " + response);
					if (200 == response.getStatusCode()) {
						try {
							IDomeo _domeo = ((IDomeo)_application);
							JsArray responseOnSets = (JsArray) parseJson(response.getText());
							handler.setExistingAnnotationSetList(responseOnSets);
							//Window.alert("Sets #: "+responseOnSets.length());
							
							/*
							ExistingAnnotationViewerPanel
							
							for(int i=0; i<responseOnSets.length(); i++) {
								JsoAnnotationSetSummary summary = (JsoAnnotationSetSummary) responseOnSets.get(i);								
								
								MAnnotationSet set = _domeo.getAnnotationPersistenceManager().getAnnotationSetById(summary.getId());
								if(summary.getNewId()!=null && !summary.getNewId().equals("undefined")) {
									set.setIndividualUri(summary.getNewId());
								}
								set.setLineageUri(summary.getLineageUri());
								set.setPreviousVersion(summary.getPreviousVersion());
								set.setVersionNumber(summary.getVersionNumber());
								set.setLastSavedOn(fmt.parse(summary.getLastSaved()));
								set.setHasChanged(false);
								
							}
							*/
						} catch(Exception e) {
							_application.getLogger().exception(this, "Couldn't complete existing annotation retrieval process (" + response.getText() + ")");
							_application.getProgressPanelContainer().setErrorMessage("Couldn't complete existing annotation retrieval process ("+ response.getText() + ")");
							//_application.getDialogPanel().hideSoon();
						}
					} else {
						_application.getLogger().exception(this, "Couldn't complete existing annotation retrieval process ("
								+ response.getStatusCode() + " - " + response.getStatusText() + ")");
						//_application.getDialogPanel().hideSoon();
						_application.getProgressPanelContainer().setErrorMessage("Couldn't complete existing annotation retrieval process ("
							+ response.getStatusCode() + " - " + response.getStatusText() + ")");
					}	
				}
			});
			builder.send();
		} catch (RequestException e) {
			_application.getLogger().exception(this, "Couldn't retrieving annotation");
		}
	}
}
