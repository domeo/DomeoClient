package org.mindinformatics.gwt.domeo.plugins.annotopia.pubmed.src;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.service.AnnotationPersistenceServiceFacade;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingBibliographySetHandler;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors.APubMedBibliograhyExtractorCommand;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.identities.EPubMedDatabase;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model.JsoPubMedEntry;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model.JsoPubmedSearchResultsWrapper;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.IPubMedConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.IPubMedItemsRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.IPubMedPaginatedItemsRequestCompleted;
import org.mindinformatics.gwt.framework.component.ui.dialog.ProgressMessagePanel;
import org.mindinformatics.gwt.framework.component.ui.glass.DialogGlassPanel;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.src.ApplicationUtils;
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
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotopiaPubMedConnector implements IPubMedConnector {

	public String URL = "http://127.0.0.1:8090/";
	
	protected IApplication _application;
	private IPubMedItemsRequestCompleted _callback;

	public AnnotopiaPubMedConnector(IApplication application, IPubMedItemsRequestCompleted callbackCompleted) {
		_application = application;
		_callback = callbackCompleted;
	}
	
//	public static native JavaScriptObject parseJson(String jsonStr) /*-{
//	  	return eval('('+jsonStr+')');
//	}-*/;
	
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
	
	/** Return the user Annotopia OAuth token if it is enabled.
	 * @return The user Annotopia OAuth token if it is enabled. */
	private Properties getAnnotopiaOAuthToken( ) {
		if(ApplicationUtils.getAnnotopiaOauthEnabled( ).equalsIgnoreCase("true")) {
			return Properties.create("Authorization: Bearer " + ApplicationUtils.getAnnotopiaOauthToken( ));
		} else {
			//return Properties.create("Authorization: Bearer none");
			return Properties.create();
		}
	}
	
	@Override
	public void getBibliographicObject(
			final IPubMedItemsRequestCompleted completionCallback, String typeQuery,
			String textQuery) throws IllegalArgumentException {
	
		
		JsUtils.JsUtilsImpl utils = new JsUtils.JsUtilsImpl();
		Properties v = utils.parseJSON("{\"apiKey\":\""+ ApplicationUtils.getAnnotopiaApiKey() +  "\",\"format\":\"domeo\",\"typeQuery\":\"" + typeQuery + "\",\"textQuery\":\"" + textQuery + "\"}");
		try {
			Ajax.ajax(Ajax.createSettings()
				.setUrl(URL+"cn/pubmed/entry")
				.setHeaders(getAnnotopiaOAuthToken( ))
		        .setDataType("json") // txt, json, jsonp, xml
		        .setType("post")      // post, get
		        .setData(v) // parameters for the query-string setData(GQuery.$$("apiKey: testkey, set: " + value))
		        .setTimeout(10000)
		        .setSuccess(new Function(){ // callback to be run if the request success
		        	public void f() {
						JsoPubMedEntry pubmedEntry = (JsoPubMedEntry) ((JsArray)parseJson(getDataProperties( ).toJsonString( ))).get(0);
						try {
							ArrayList<MPublicationArticleReference> references = new ArrayList<MPublicationArticleReference>();
	
							MPublicationArticleReference reference = new MPublicationArticleReference();
							reference.setUrl(pubmedEntry.getUrl());
							reference.setDoi(pubmedEntry.getDoi());
							reference.setPubMedId(pubmedEntry.getPmId());
							reference.setPubMedCentralId(pubmedEntry.getPmcId());
							reference.setTitle(pubmedEntry.getTitle());
							reference.setAuthorNames(pubmedEntry.getPublicationAuthors());
							reference.setJournalPublicationInfo(pubmedEntry.getPublicationInfo());
							reference.setPublicationDate(pubmedEntry.getPublicationDate());
							reference.setSource(EPubMedDatabase.getInstance());
							reference.setJournalName(pubmedEntry.getJournalName());
							reference.setJournalIssn(pubmedEntry.getJournalIssn());							
							reference.setCreationDate(new Date());
			
							references.add(reference);
						
							completionCallback.returnBibliographicObject(references);
						} catch (Exception e) {
							_application.getLogger().exception(this, "Bibliographic object retrieval: " + e.getMessage());
							((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve bibliographic object");
							completionCallback.bibliographyObjectNotFound("Bibliographic object retrieval: " + e.getMessage());
						}
		        	}
	        	})
		        .setError(new Function(){ // callback to be run if the request fails
		        	public void f() {
		        		 Window.alert("There was an error whilre retrieving the bibliographic object" + getDataObject());

		        		_application.getLogger().exception(this, 
		        			"Couldn't complete existing annotation sets list saving");
		        		_application.getProgressPanelContainer().setErrorMessage(
							"Couldn't complete existing annotation sets list saving");
		        	}
		        })
		     );
		} catch (Exception e) {
			e.printStackTrace();
			_application.getLogger().exception(this, "Couldn't complete Pubmed reference retrieval");
		}
	}
	
	public void getBibliographicObjects(
			final IPubMedItemsRequestCompleted completionCallback, String typeQuery,
			List<String> textQuery, List<String> elements)
			throws IllegalArgumentException {

		StringBuffer ids = new StringBuffer();
		for(int i=0; i<textQuery.size(); i++) {
			ids.append(textQuery.get(i));
			if(i<textQuery.size()-1) ids.append(",");
		}
		
		JsUtils.JsUtilsImpl utils = new JsUtils.JsUtilsImpl();
		Properties v = utils.parseJSON("{\"apiKey\":\""+ ApplicationUtils.getAnnotopiaApiKey() +  "\",\"format\":\"domeo\",\"typeQuery\":\"" + typeQuery + "\",\"textQuery\":\"" + ids.toString() + "\"}");
		try {
			Ajax.ajax(Ajax.createSettings()
				.setUrl(URL+"cn/pubmed/entries")
				.setHeaders(getAnnotopiaOAuthToken( ))
		        .setDataType("json") // txt, json, jsonp, xml
		        .setType("post")      // post, get
		        .setData(v) // parameters for the query-string setData(GQuery.$$("apiKey: testkey, set: " + value))
		        .setTimeout(10000)
		        .setSuccess(new Function(){ // callback to be run if the request success
		        	public void f() {
						try {
							JsArray pubmedEntries = ((JsArray) parseJson(getDataProperties( ).toJsonString( )));
							ArrayList<MPublicationArticleReference> references = new ArrayList<MPublicationArticleReference>();
							for(int i=0; i<pubmedEntries.length(); i++) {
								
								try {
									JsoPubMedEntry  pubmedEntry = (JsoPubMedEntry) pubmedEntries.get(i);
									MPublicationArticleReference reference = new MPublicationArticleReference();
									reference.setUrl(pubmedEntry.getUrl());
									reference.setDoi(pubmedEntry.getDoi());
									reference.setPubMedId(pubmedEntry.getPmId());
									reference.setPubMedCentralId(pubmedEntry.getPmcId());
									reference.setTitle(pubmedEntry.getTitle());
									reference.setAuthorNames(pubmedEntry.getPublicationAuthors());
									reference.setJournalPublicationInfo(pubmedEntry.getPublicationInfo());
									reference.setPublicationDate(pubmedEntry.getPublicationDate());
									reference.setSource(EPubMedDatabase.getInstance());
									reference.setJournalName(pubmedEntry.getJournalName());
									reference.setJournalIssn(pubmedEntry.getJournalIssn());
									references.add(reference);;
								} catch(Exception e) {
									MPublicationArticleReference reference = new MPublicationArticleReference();
									reference.setUnrecognized(APubMedBibliograhyExtractorCommand.UNRECOGNIZED);
									references.add(reference);
								}
							}
						
							completionCallback.returnBibliographicObject(references);
						} catch (Exception e) {
							_application.getLogger().exception(this, "Bibliographic object retrieval: " + e.getMessage());
							((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve bibliographic object");
							completionCallback.bibliographyObjectNotFound("Bibliographic object retrieval: " + e.getMessage());
						}
		        	}
	        	})
		        .setError(new Function(){ // callback to be run if the request fails
		        	public void f() {
		        		 Window.alert("There was an error whilre retrieving the bibliographic object" + getDataObject());

		        		_application.getLogger().exception(this, 
		        			"Couldn't complete existing annotation sets list saving");
		        		_application.getProgressPanelContainer().setErrorMessage(
							"Couldn't complete existing annotation sets list saving");
		        	}
		        })
		     );
		} catch (Exception e) {
			e.printStackTrace();
			_application.getLogger().exception(this, "Couldn't complete Pubmed references retrieval");
		}
	}
	
	@Override
	public void searchBibliographicObjects(
			final IPubMedPaginatedItemsRequestCompleted completionCallback, String typeQuery,
			String textQuery, int maxResults, int offset) throws IllegalArgumentException {
		
		JsUtils.JsUtilsImpl utils = new JsUtils.JsUtilsImpl();
		Properties v = utils.parseJSON("{\"apiKey\":\""+ ApplicationUtils.getAnnotopiaApiKey() +  "\",\"format\":\"domeo\",\"typeQuery\":\"" + typeQuery + "\",\"textQuery\":\"" + textQuery + "\",\"offset\":\"" + offset + "\",\"maxResults\":\"" + maxResults + "\"}");
		try {
			Ajax.ajax(Ajax.createSettings()
				.setUrl(URL+"cn/pubmed/search")
				.setHeaders(getAnnotopiaOAuthToken( ))
		        .setDataType("json") // txt, json, jsonp, xml
		        .setType("get")      // post, get
		        .setData(v) // parameters for the query-string setData(GQuery.$$("apiKey: testkey, set: " + value))
		        .setTimeout(10000)
		        .setSuccess(new Function(){ // callback to be run if the request success
		        	public void f() {
						try {
							JsoPubmedSearchResultsWrapper pubmedSearchResultsWrapper = (JsoPubmedSearchResultsWrapper) parseJson(getDataProperties( ).toJsonString( ));
							JsArray pubmedEntries = pubmedSearchResultsWrapper.getResults();
							if(pubmedSearchResultsWrapper.getResults().length()>0 && (pubmedSearchResultsWrapper.getException()==null || pubmedSearchResultsWrapper.getException().trim().length()==0)) {
								ArrayList<MPublicationArticleReference> references = new ArrayList<MPublicationArticleReference>();
								for(int i=0; i<pubmedEntries.length(); i++) {
									try {
										JsoPubMedEntry  pubmedEntry = (JsoPubMedEntry) pubmedEntries.get(i);
										MPublicationArticleReference reference = new MPublicationArticleReference();
										reference.setUrl(pubmedEntry.getUrl());
										reference.setDoi(pubmedEntry.getDoi());
										reference.setPubMedId(pubmedEntry.getPmId());
										reference.setPubMedCentralId(pubmedEntry.getPmcId());
										reference.setTitle(pubmedEntry.getTitle());
										reference.setAuthorNames(pubmedEntry.getPublicationAuthors());
										reference.setJournalPublicationInfo(pubmedEntry.getPublicationInfo());
										reference.setPublicationDate(pubmedEntry.getPublicationDate());
										reference.setSource(EPubMedDatabase.getInstance());
										reference.setJournalName(pubmedEntry.getJournalName());
										reference.setJournalIssn(pubmedEntry.getJournalIssn());
										references.add(reference);
									} catch(Exception e) {
										MPublicationArticleReference reference = new MPublicationArticleReference();
										reference.setUnrecognized(APubMedBibliograhyExtractorCommand.UNRECOGNIZED);
										references.add(reference);
									}
								}
							
								int range = Integer.parseInt(pubmedSearchResultsWrapper.getRange())>0 ? Integer.parseInt(pubmedSearchResultsWrapper.getRange()): -1;
								int offset = Integer.parseInt(pubmedSearchResultsWrapper.getOffset())>=0 ? Integer.parseInt(pubmedSearchResultsWrapper.getOffset()): -1;
								int total = Integer.parseInt(pubmedSearchResultsWrapper.getTotal())>=0 ? Integer.parseInt(pubmedSearchResultsWrapper.getTotal()): -1;
								
								completionCallback.returnBibliographicObject(total, offset, range, references);
							} else {
								_application.getLogger().exception(this, "Could not complete bibliographic search: " + pubmedSearchResultsWrapper.getException());
								completionCallback.bibliographicSearchNotCompleted("Could not complete bibliographic search: " + pubmedSearchResultsWrapper.getException());
							}		
						} catch (Exception e) {
							_application.getLogger().exception(this, "Bibliographic object retrieval: " + e.getMessage());
							((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve bibliographic object");
							completionCallback.bibliographicSearchNotCompleted("Bibliographic object retrieval: " + e.getMessage());
						}
		        	}
	        	})
		        .setError(new Function(){ // callback to be run if the request fails
		        	public void f() {
		        		 Window.alert("There was an error whilre retrieving the bibliographic object" + getDataObject());

		        		_application.getLogger().exception(this, 
		        			"Couldn't complete existing annotation sets list saving");
		        		_application.getProgressPanelContainer().setErrorMessage(
							"Couldn't complete existing annotation sets list saving");
		        	}
		        })
		     );
		} catch (Exception e) {
			e.printStackTrace();
			_application.getLogger().exception(this, "Couldn't complete Pubmed references retrieval");
		}
	}

	
	public void retrieveExistingBibliographySetByUrl(final IRetrieveExistingBibliographySetHandler handler, String url) {
		
		_application.getLogger().debug(this, "Beginning retrieving bibliography for url " + url);
		if(_application.isHostedMode()) { // No bibliography retrieved in hosted mode
			AnnotationPersistenceServiceFacade f = new AnnotationPersistenceServiceFacade();
			handler.setExistingBibliographySetList(f.retrieveBibliographyByDocumentUrl(((IDomeo)_application).getPersistenceManager().getCurrentResource().getUrl()), true);
			return;
		}
		String requestUrl = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL())+ "persistence/retrieveExistingBibliographicSets?format=json";

//		try {
//			RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, requestUrl);
//			builder.setHeader("Content-Type", "application/json");
//
//			JSONObject request = new JSONObject();
//			request.put("url", new JSONString(url));
//			
//			JSONArray messages = new JSONArray();
//			messages.set(0, request);
//			
//			builder.setTimeoutMillis(10000);
//			builder.setRequestData(messages.toString());
//			builder.setCallback(new RequestCallback() {
//				public void onError(Request request, Throwable exception) {
//					if(exception instanceof RequestTimeoutException) {
//						_application.getLogger().exception(this, "Couldn't load existing bibliographic set (timeout) " + exception.getMessage());
//						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not load existing bibliography  (timeout)");
//						handler.bibliographySetListNotCreated("Could not load existing bibliography  (timeout)");
//					} else {
//						_application.getLogger().exception(this, "Couldn't load existing bibliographic set");
//						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not load existing bibliography (onError)");
//						handler.bibliographySetListNotCreated("Could not load existing bibliography (onError)");
//					}
//				}
//
//				public void onResponseReceived(Request request, Response response) {
//					if (200 == response.getStatusCode()) {
//						try {
//							_application.getLogger().debug(this, response.getText());
//							JsArray responseOnSets = (JsArray) parseJson(response.getText());
//							handler.setExistingBibliographySetList(responseOnSets, true);			
//						} catch(Exception e) {
//							_application.getLogger().exception(this, "Could not parse existing bibliography " + e.getMessage());
//							((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not parse existing bibliography  " + e.getMessage());
//							handler.bibliographySetListNotCreated("Could not parse existing bibliography " + e.getMessage() + " - "+ response.getText());
//						}
//					} else if (503 == response.getStatusCode()) {
//						_application.getLogger().exception(this, "Existing bibliography by url 503: " + response.getText());
//						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve existing bibliography  " + response.getStatusCode());
//						handler.bibliographySetListNotCreated("Could not retrieve existing bibliography " + response.getStatusCode() + " - "+ response.getText());
//						//completionCallback.textMiningNotCompleted(response.getText());
//					} else {
//						_application.getLogger().exception(this,  "Existing bibliography by url " + response.getStatusCode() + ": "+ response.getText());
//						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve existing bibliography  " + response.getStatusCode());
//						handler.bibliographySetListNotCreated("Could not retrieve existing bibliography " + response.getStatusCode() + " - "+ response.getText());
//						//handler.setExistingBibliographySetList(new JsArray(), true);
//						//completionCallback.textMiningNotCompleted(response.getText());
//					}
//				}
//			});
//			builder.send();
//			
//		} catch (RequestException e) {
//			_application.getLogger().exception(this, "Couldn't save annotation");
//		}
	}
	
	public void retrieveExistingBibliographySetByPmcId(final IRetrieveExistingBibliographySetHandler handler, HashMap<String, String> ids) {
		
	}

	public void retrieveExistingBibliographySet(final IRetrieveExistingBibliographySetHandler handler, int level) {
		_application.getLogger().debug(this, "Beginning retrieving bibliography...");
		if(_application.isHostedMode()) {
			AnnotationPersistenceServiceFacade f = new AnnotationPersistenceServiceFacade();
			handler.setExistingBibliographySetList(f.retrieveBibliographyByDocumentUrl(((IDomeo)_application).getPersistenceManager().getCurrentResource().getUrl()), true);
			return;
		}
		
		
		String url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL())+ "persistence/retrieveExistingBibliographicSets?format=json";

		try {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
			builder.setHeader("Content-Type", "application/json");
			builder.setTimeoutMillis(10000);
			
			IDomeo _domeo = ((IDomeo)_application);
			MDocumentResource _resource = (MDocumentResource) _domeo.getPersistenceManager().getCurrentResource();
			MPublicationArticleReference _reference = (MPublicationArticleReference)_resource.getSelfReference().getReference();

			int counter = 0;
			JSONArray ids = new JSONArray();
			if(_reference.getDoi()!=null) {
				JSONObject id1 = new JSONObject();
				id1.put("idLabel", new JSONString("doi"));
				id1.put("idValue", new JSONString(_reference.getDoi()));
				ids.set(counter++, id1);
			}
			if(_reference.getPubMedId()!=null) {
				JSONObject id2 = new JSONObject();
				id2.put("idLabel", new JSONString("pmid"));
				id2.put("idValue", new JSONString(_reference.getPubMedId()));
				ids.set(counter++, id2);
			}
			if(_reference.getPubMedCentralId()!=null) {
				JSONObject id3 = new JSONObject();
				id3.put("idLabel", new JSONString("pmcid"));
				id3.put("idValue", new JSONString(_reference.getPubMedCentralId()));
				ids.set(counter++, id3);
			}
			
			JSONObject request = new JSONObject();
			request.put("url", new JSONString(_resource.getUrl()));
			request.put("ids", ids);
			
			JSONArray messages = new JSONArray();
			messages.set(0, request);
			
			builder.setRequestData(messages.toString());
			builder.setCallback(new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					if(exception instanceof RequestTimeoutException) {
						_application.getLogger().exception(this, "Couldn't load existing bibliographic set (timeout) " + exception.getMessage());
						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not load existing bibliography  (timeout)");
						handler.bibliographySetListNotCreated("Could not load existing bibliography  (timeout)");
					} else {
						_application.getLogger().exception(this, "Couldn't load existing bibliographic set");
						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not load existing bibliography (onError)");
						handler.bibliographySetListNotCreated("Could not load existing bibliography (onError)");
					}
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						try {
							_application.getLogger().debug(this, response.getText());
							JsArray responseOnSets = (JsArray) parseJson(response.getText());
							handler.setExistingBibliographySetList(responseOnSets, true);			
						} catch(Exception e) {
							_application.getLogger().exception(this, "Could not parse existing bibliography " + e.getMessage());
							((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not parse existing bibliography  " + e.getMessage());
							handler.bibliographySetListNotCreated("Could not parse existing bibliography " + e.getMessage() + " - "+ response.getText());
						}
					} else if (503 == response.getStatusCode()) {
						_application.getLogger().exception(this, "Existing bibliography retrieval 503: " + response.getText());
						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve existing bibliography  " + response.getStatusCode());
						handler.bibliographySetListNotCreated("Could not retrieve existing bibliography " + response.getStatusCode() + " - "+ response.getText());
					} else {
						_application.getLogger().exception(this,  "Retrieving existing bibliography " + response.getStatusCode() + ": "+ response.getText());
						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve existing bibliography  " + response.getStatusCode());
						handler.bibliographySetListNotCreated("Could not retrieve existing bibliography " + response.getStatusCode() + " - "+ response.getText());
					}
				}
			});
			builder.send();
		} catch (RequestException e) {
			_application.getLogger().exception(this, "Couldn't retrieve annotation");
		}
	}
	

	

	


}
