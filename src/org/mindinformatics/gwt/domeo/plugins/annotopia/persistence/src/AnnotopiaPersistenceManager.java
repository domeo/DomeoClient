package org.mindinformatics.gwt.domeo.plugins.annotopia.persistence.src;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.APersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IPersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingAnnotationSetHandler;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingAnnotationSetListHandler;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingBibliographySetHandler;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaAnnotationSetGraph;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaSetResultWrapper;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.JsAnnotopiaSetsResultWrapper;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.model.MAnnotopiaAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.serializers.AnnotopiaSerializerManager;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.src.AnnotopiaConverter;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.ui.existingsets.ExistingAnnotationViewerPanel;
import org.mindinformatics.gwt.framework.component.ui.glass.EnhancedGlassPanel;
import org.mindinformatics.gwt.framework.src.ApplicationUtils;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotopiaPersistenceManager extends APersistenceManager implements IPersistenceManager {

	private static final String API_KEY = "annotopia-api-key";
	private static final String POST = "post", PUT = "put", GET = "get", DELETE = "delete", JSON = "json";
	private static final String PREFIX = "s/annotationset";
	
	public String URL = "http://localhost:8090/";
	
	/**
	 * @param domeo		Pointer to main application
	 * @param url		The URL of the Annotopia server
	 * @param callback  The pointer to the entity that will be triggered
	 */
	public AnnotopiaPersistenceManager(IDomeo domeo, String url, ICommandCompleted callback) {
		super(domeo, callback);
		if(url!=null) URL = url;
	}

	@Override
	public void saveAnnotation() {
		_application.getLogger().debug(this, "Saving annotation sets...");
		_application.getProgressPanelContainer().setProgressMessage("Saving annotation sets to Annotopia...");
		
		ArrayList<MAnnotationSet> setToSerialize = new ArrayList<MAnnotationSet>();
		for(MAnnotationSet set: ((IDomeo)_application).getAnnotationPersistenceManager().getAllDiscussionSets()) {
			if(set.getHasChanged() && set.getAnnotations().size()>0) 
				setToSerialize.add(set);
		}
		for(MAnnotationSet set: ((IDomeo)_application).getAnnotationPersistenceManager().getAllUserSets()) {
			if(set.getHasChanged() && set.getAnnotations().size()>0) 
				setToSerialize.add(set);
		}
		
		for(MAnnotationSet annotationSet: setToSerialize) {
			if((annotationSet.getVersionNumber()==null || annotationSet.getVersionNumber().isEmpty())) {
				postAnnotationSet(annotationSet);
			} else {
				putAnnotationSet(annotationSet);
			}
		}
		
	}
	
	/**
	 * Posts a new Annotation Set
	 */
	private void postAnnotationSet(MAnnotationSet set) {
		_application.getLogger().debug(this, "Posting new annotation set (new)");
		
		try {
			Ajax.ajax(Ajax.createSettings()
				.setUrl(URL + PREFIX)	
				.setHeaders(getHeaders())
				.setDataType(JSON)
				.setType(POST)    
		        .setData(new JsUtils.JsUtilsImpl().parseJSON(AnnotopiaSerializerManager.getInstance((IDomeo)_application).serialize(set).toString()))
		        .setTimeout(10000)
		        .setSuccess(new Function(){ // callback to be run if the request success
		    		public void f() {
		    			IDomeo _domeo = ((IDomeo)_application);
		    			JavaScriptObject jsSet = ApplicationUtils.parseJson(getDataProperties().toJsonString());
		    			AnnotopiaConverter unmarshaller = new AnnotopiaConverter(_domeo);
		    			
		    			MAnnotationSet savedSet = unmarshaller.unmarshallBasicAnnotationSet(jsSet, false);	    
		    			if(savedSet==null) {
		    				_application.getLogger().exception(this, "Annotation set not saved correctly");
		    				_application.getProgressPanelContainer().setErrorMessage("Annotation set not saved correctly");
		    			} else {
		    				MAnnotationSet currentSet = _domeo.getPersistenceManager().getAnnotationSetById(savedSet.getPreviousVersion());	
		    				currentSet.setIndividualUri(savedSet.getIndividualUri());
		    				currentSet.setLastSavedOn(savedSet.getLastSavedOn());
		    				currentSet.setVersionNumber(savedSet.getVersionNumber());
		    				currentSet.setPreviousVersion(savedSet.getPreviousVersion());
		    				currentSet.setHasChanged(false);
		    				
		    				_application.getLogger().info(this, "Set minted URI: " +currentSet.getIndividualUri());

		    				int matched = 0;
		    				for(MAnnotation annotation: savedSet.getAnnotations()) {
		    					for(MAnnotation currentAnnotation: currentSet.getAnnotations()) {
		    						if(currentAnnotation.getIndividualUri().equals(annotation.getPreviousVersion())) {		 
		    							_application.getLogger().info(this, "Matched " + currentAnnotation.getIndividualUri());
		    							matched++;		    							
		    							currentAnnotation.setIndividualUri(annotation.getIndividualUri());
		    							currentAnnotation.setLastSavedOn(annotation.getLastSavedOn());
		    							currentAnnotation.setVersionNumber(annotation.getVersionNumber());
		    							currentAnnotation.setPreviousVersion(annotation.getPreviousVersion());
		    							currentAnnotation.setHasChanged(false);
		    							// TODO: Assumes one target
		    							currentAnnotation.getSelector().setUri(annotation.getSelector().getUri());
		    							break;
		    						}
		    					}
		    				}
		    				_application.getLogger().debug(this,  "Matched: " + matched);
		    				_application.getProgressPanelContainer().hide();
		    				_application.getLogger().debug(this, "Completed saving of Annotation Set in " + (System.currentTimeMillis()-((IDomeo)_application).getDocumentPipelineTimer())+ "ms");
		    			}
		    		}
		        }).setError(new Function(){ // callback to be run if the request fails
		        	public void f() {
		        		 Window.alert("There was an error " + getDataObject());

		        		_application.getLogger().exception(this, 
		        			"Couldn't complete existing annotation sets list saving");
		        		_application.getProgressPanelContainer().setErrorMessage(
							"Couldn't complete existing annotation sets list saving");
		        	}
		        })
		    );
		} catch (Exception e) {
			_application.getLogger().exception(this, "Couldn't complete annotation set post");
		}
	}
	
	/**
	 * Updates an existing Annotation Set
	 */
	private void putAnnotationSet(MAnnotationSet set) {
		_application.getLogger().debug(this, "Updating annotation set " + set.getUuid());
		try {
			Ajax.ajax(Ajax.createSettings()
				.setUrl(set.getIndividualUri())
				.setHeaders(getHeaders()).setDataType(JSON).setType(PUT)    
		        .setData(new JsUtils.JsUtilsImpl().parseJSON(AnnotopiaSerializerManager.getInstance((IDomeo)_application).serialize(set).toString()))
		        .setTimeout(10000)
		        .setSuccess(new Function(){ // callback to be run if the request success
		    		public void f() {
		    			IDomeo _domeo = ((IDomeo)_application);
		    			JavaScriptObject jsSet = ApplicationUtils.parseJson(getDataProperties().toJsonString());
		    			AnnotopiaConverter unmarshaller = new AnnotopiaConverter(_domeo);
		    			
		    			MAnnotationSet savedSet = unmarshaller.unmarshallBasicAnnotationSet(jsSet, false);	 
		    			if(savedSet==null) {
		    				_application.getLogger().exception(this, "Annotation set not updated correctly");
		    				_application.getProgressPanelContainer().setErrorMessage("Annotation set not updated correctly");
		    			} else {
		    				// As this is a set update, the matching URI is the existing one ???
		    				
		    				MAnnotationSet currentSet = _domeo.getPersistenceManager().getAnnotationSetById(savedSet.getPreviousVersion());	
		    				currentSet.setIndividualUri(savedSet.getIndividualUri());
		    				currentSet.setLastSavedOn(savedSet.getLastSavedOn());
		    				currentSet.setVersionNumber(savedSet.getVersionNumber());
		    				currentSet.setPreviousVersion(savedSet.getPreviousVersion());
		    				currentSet.setHasChanged(false);
		    				
		    				_application.getLogger().info(this, "Set updated URI: " +currentSet.getIndividualUri());

		    				int matched = 0;
		    				for(MAnnotation annotation: savedSet.getAnnotations()) {
		    					for(MAnnotation currentAnnotation: currentSet.getAnnotations()) {
		    						if(currentAnnotation.getIndividualUri().equals(annotation.getPreviousVersion())) {		 
		    							matched++;		    							
		    							currentAnnotation.setIndividualUri(annotation.getIndividualUri());
		    							currentAnnotation.setLastSavedOn(annotation.getLastSavedOn());
		    							currentAnnotation.setVersionNumber(annotation.getVersionNumber());
		    							currentAnnotation.setPreviousVersion(annotation.getPreviousVersion());
		    							currentAnnotation.setHasChanged(false);
		    							// TODO: Assumes one target
		    							currentAnnotation.getSelector().setUri(annotation.getSelector().getUri());
		    							break;
		    						}
		    					}
		    				}
		    				_application.getLogger().debug(this,  "Matched: " + matched);
		    				_application.getProgressPanelContainer().hide();
		    				_application.getLogger().debug(this, "Completed saving of Annotation Set in " + (System.currentTimeMillis()-((IDomeo)_application).getDocumentPipelineTimer())+ "ms");
		    			}
		    		}
		        })
		    );
		} catch (Exception e) {
			_application.getLogger().exception(this, "Couldn't complete annotation set post");
		}		
	}
	
	/**
	 * Deletes an existing Annotation Set
	 */
	private void deleteAnnotationSet(MAnnotationSet set) {
		try {
			Ajax.ajax(Ajax.createSettings()
				.setUrl(URL + PREFIX + "/" + set.getUuid())
				.setHeaders(getHeaders()).setDataType(JSON).setType(DELETE)    
		        .setTimeout(10000)
		        .setSuccess(new Function(){ // callback to be run if the request success
		    		public void f() {
		    			
		    		}
		        })
		    );
		} catch (Exception e) {
			_application.getLogger().exception(this, "Couldn't complete annotation set post");
		}	
	}
	
	/**
	 * Posts a new annotation to an existing set
	 */
	private void postAnnotationInSet(MAnnotation annotation, MAnnotationSet set) {
		
	}
	
	/**
	 * Updates an existing annotation in an existing set
	 */
	private void putAnnotationInSet(MAnnotation annotation, MAnnotationSet set) {
		
	}
	
	/**
	 * Deletes an existing annotation to an existing set
	 */
	private void deleteAnnotationInSet(MAnnotation annotation, MAnnotationSet set) {
		
	}

	@Override
	public void retrieveExistingBibliographySet(
			IRetrieveExistingBibliographySetHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retrieveExistingAnnotationSets(List<String> urls, IRetrieveExistingAnnotationSetHandler handler) {
		_application.getLogger().debug(this, "Retrieving requested annotation sets...");
		_application.getProgressPanelContainer().setProgressMessage("Retrieving requested annotation sets from Annotopia...");

		for(String url: urls) {
			try {
				Ajax.ajax(Ajax.createSettings()
					.setUrl(url)
					.setHeaders(getHeaders())
			        .setDataType("json") // txt, json, jsonp, xml
			        .setType("get")      // post, get
			        .setTimeout(10000)
			        .setSuccess(new Function(){ // callback to be run if the request success
			    		public void f() {
			    			IDomeo _domeo = ((IDomeo)_application);
//			    			JsAnnotopiaAnnotationSetGraph wrapper = 
//			    				(JsAnnotopiaAnnotationSetGraph) parseJson(getDataProperties().toJsonString());
			    			
			    			JavaScriptObject jsSet = ApplicationUtils.parseJson(getDataProperties().toJsonString());
			    			AnnotopiaConverter unmarshaller = new AnnotopiaConverter(_domeo);
			    			
			    			MAnnotationSet set = unmarshaller.unmarshallBasicAnnotationSet(jsSet, true);	    			
			    			if(set==null) {
			    				// TODO message no annotation found
			    				_application.getLogger().info(this, "No annotation set found");
			    				_application.getProgressPanelContainer().setCompletionMessage("Annotation Set not found");
			    			} else {	
			    				((AnnotationPersistenceManager) _domeo.getPersistenceManager()).loadAnnotationSet(set);
			    				_application.getProgressPanelContainer().hide();
			    				_application.getLogger().debug(this, "Completed Execution of retrieveExistingAnnotationSets() in " + (System.currentTimeMillis()-((IDomeo)_application).getDocumentPipelineTimer())+ "ms");
			    				_domeo.refreshAllComponents();
			    			}
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
				_application.getLogger().exception(this, "Couldn't complete existing annotation sets list retrieval");
			}
		}
	}

	@Override
	public void retrieveExistingAnnotationSetList(IRetrieveExistingAnnotationSetListHandler handler) {
		_application.getLogger().debug(this, "Retrieving list of existing annotation sets...");
		_application.getProgressPanelContainer().setProgressMessage("Retrieving list of existing annotation sets from Annotopia...");
		
		try {
			Ajax.ajax(Ajax.createSettings()
				.setUrl(URL + PREFIX)	
				.setHeaders(getHeaders())
				.setDataType(JSON)
				.setType(GET)    
		        .setData(new JsUtils.JsUtilsImpl().parseJSON("{\"tgtUrl\":\""+((IDomeo)_application).getPersistenceManager().getCurrentResource().getUrl()+ "\"}"))
		        .setTimeout(10000)
		        .setSuccess(new Function(){ // callback to be run if the request success
		    		public void f() {
		    			IDomeo _domeo = ((IDomeo)_application);
		    			JsAnnotopiaSetsResultWrapper wrapper = 
		    				(JsAnnotopiaSetsResultWrapper) ApplicationUtils.parseJson(getDataProperties().toJsonString());
		    			AnnotopiaConverter unmarshaller = new AnnotopiaConverter(_domeo);
		    			List<MAnnotopiaAnnotationSet> sets = unmarshaller.unmarshallAnnotationSetsList(wrapper);	    			
		    			_application.getLogger().debug(this, "Completed Execution of retrieveExistingAnnotationSetList() in " + (System.currentTimeMillis()-((IDomeo)_application).getDocumentPipelineTimer())+ "ms");

		    			if(sets.size()==0) {
		    				// TODO message no annotation found
		    				_application.getLogger().info(this, "No annotation sets found");
		    				_application.getProgressPanelContainer().setCompletionMessage("No annotation exist for this document");
		    			} else {
		    				_application.getProgressPanelContainer().hide();
		    				try {
		    					ExistingAnnotationViewerPanel lwp = new ExistingAnnotationViewerPanel((IDomeo)_application, sets);
		    					new EnhancedGlassPanel((IDomeo)_application, lwp, lwp.getTitle(), false, false, false);
		    				} catch (Exception e) {
		    					_application.getLogger().exception(this, "Exeption in visualizing existing annotation");
		    				}		
		    			}
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
		    //.setData(GQuery.$$("apiKey: " + ApplicationUtils.getAnnotopiaApiKey() + ",outCmd:frame,tgtUrl:"+((IDomeo)_application).getPersistenceManager().getCurrentResource().getUrl())) // parameters for the query-string
		} catch (Exception e) {
			_application.getLogger().exception(this, "Couldn't complete existing annotation sets list retrieval");
		}
	}

	@Override
	public void saveBibliography() {
		// TODO Auto-generated method stub		
	}
	
	/**
	 * Get the properties for the HTTP headers
	 * @return The list of properties for the header
	 */
	private Properties getHeaders() {
		Properties props = ApplicationUtils.getAnnotopiaOAuthToken();
		if(!ApplicationUtils.getAnnotopiaOauthEnabled().equalsIgnoreCase("true")) props.set("Authorization", "annotopia-api-key " + ApplicationUtils.getAnnotopiaApiKey());
		return props;
	}
	

}
