package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.src;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.service.AnnotationPersistenceServiceFacade;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingBibliographySetHandler;
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

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonPubMedConnector implements IPubMedConnector {
	
	protected IApplication _application;
	private IPubMedItemsRequestCompleted _callback;

	public JsonPubMedConnector(IApplication application, IPubMedItemsRequestCompleted callbackCompleted) {
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

	
	public void retrieveExistingBibliographySetByUrl(final IRetrieveExistingBibliographySetHandler handler, String url) {
		
		_application.getLogger().debug(this, "Beginning retrieving bibliography for url " + url);
		if(_application.isHostedMode()) { // No bibliography retrieved in hosted mode
			AnnotationPersistenceServiceFacade f = new AnnotationPersistenceServiceFacade();
			handler.setExistingBibliographySetList(f.retrieveBibliographyByDocumentUrl(((IDomeo)_application).getPersistenceManager().getCurrentResource().getUrl()), true);
			return;
		}
		String requestUrl = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL())+ "persistence/retrieveExistingBibliographicSets?format=json";

		try {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, requestUrl);
			builder.setHeader("Content-Type", "application/json");

			JSONObject request = new JSONObject();
			request.put("url", new JSONString(url));
			
			JSONArray messages = new JSONArray();
			messages.set(0, request);
			
			builder.setTimeoutMillis(10000);
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
						_application.getLogger().exception(this, "Existing bibliography by url 503: " + response.getText());
						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve existing bibliography  " + response.getStatusCode());
						handler.bibliographySetListNotCreated("Could not retrieve existing bibliography " + response.getStatusCode() + " - "+ response.getText());
						//completionCallback.textMiningNotCompleted(response.getText());
					} else {
						_application.getLogger().exception(this,  "Existing bibliography by url " + response.getStatusCode() + ": "+ response.getText());
						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve existing bibliography  " + response.getStatusCode());
						handler.bibliographySetListNotCreated("Could not retrieve existing bibliography " + response.getStatusCode() + " - "+ response.getText());
						//handler.setExistingBibliographySetList(new JsArray(), true);
						//completionCallback.textMiningNotCompleted(response.getText());
					}
				}
			});
			builder.send();
			
		} catch (RequestException e) {
			_application.getLogger().exception(this, "Couldn't save annotation");
		}
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
	
	@Override
	public void getBibliographicObject(
			final IPubMedItemsRequestCompleted completionCallback, String typeQuery,
			String textQuery) throws IllegalArgumentException {
	
		String url = GWT.getModuleBaseURL() + "pubmed/entry?format=json&typeQuery=" + typeQuery + "&textQuery=" + textQuery;
		if(!_application.isHostedMode())
			url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL()) + "pubmed/entry?format=json&typeQuery=" + typeQuery + "&textQuery=" + textQuery;
	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
	    builder.setTimeoutMillis(10000);
		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					if(exception instanceof RequestTimeoutException) {
						_application.getLogger().exception(this, "Couldn't load bibliographic object (timeout) " + exception.getMessage());
						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Couldn't load bibliographic object (timeout)");
						completionCallback.bibliographyObjectNotFound("Couldn't load bibliographic object (timeout) " + exception.getMessage());
					} else {
						_application.getLogger().exception(this, "Couldn't load bibliographic object (error)");
						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Couldn't load bibliographic object (error)");
						completionCallback.bibliographyObjectNotFound("Couldn't load bibliographic object (error) " + exception.getMessage());
					}
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						//Window.alert("back");
						JsoPubMedEntry pubmedEntry = (JsoPubMedEntry) ((JsArray)parseJson(response.getText())).get(0);
						//Window.alert("" + pubmedEntry);
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
							((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve bibliographic object " + response.getStatusCode());
							completionCallback.bibliographyObjectNotFound("Bibliographic object retrieval: " + e.getMessage());
						}
					} else if (503 == response.getStatusCode()) {
						_application.getLogger().exception(this, "Bibliographic object retrieval 503: " + response.getText());
						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve bibliographic object " + response.getStatusCode());
						completionCallback.bibliographyObjectNotFound("Bibliographic object retrieval 503: " + response.getText());
					} else {
						_application.getLogger().exception(this, "Bibliographic object retrieval: " + response.getStatusCode() + ": "+ response.getText());
						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve bibliographic object  " + response.getStatusCode());
						completionCallback.bibliographyObjectNotFound("Bibliographic object not retrieved: " + response.getText());
					}
				}
			});
		} catch (RequestException e) {
			_application.getInitializer().addException("Couldn't retrieve Bibliographic Object");
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
		
		String url = GWT.getModuleBaseURL() + "pubmed/entries?format=json&typeQuery=" + typeQuery + "&textQuery=" + ids;
		if(!_application.isHostedMode()) url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL()) + "pubmed/entries?format=json&typeQuery=" + typeQuery + "&textQuery=" + ids;
	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
	    builder.setTimeoutMillis(10000);
		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					if(exception instanceof RequestTimeoutException) {
						_application.getLogger().exception(this, "Couldn't load bibliographic objects (timeout) " + exception.getMessage());
						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Couldn't load bibliographic objects (timeout)");
						completionCallback.bibliographyObjectNotFound("Couldn't load bibliographic objects (timeout) " + exception.getMessage());
					} else {
						_application.getLogger().exception(this, "Couldn't load bibliographic objects (error)");
						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Couldn't load bibliographic objects (error)");
						completionCallback.bibliographyObjectNotFound("Couldn't load bibliographic objects (error) " + exception.getMessage());
					}
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						JsArray pubmedEntries = ((JsArray) parseJson(response.getText()));
						ArrayList<MPublicationArticleReference> references = new ArrayList<MPublicationArticleReference>();
						for(int i=0; i<pubmedEntries.length(); i++) {
							if(pubmedEntries.get(i).equals(APubMedBibliograhyExtractorCommand.UNRECOGNIZED)) {
								MPublicationArticleReference reference = new MPublicationArticleReference();
								reference.setUnrecognized(APubMedBibliograhyExtractorCommand.UNRECOGNIZED);
								references.add(reference);
							} else if(pubmedEntries.get(i) instanceof Object) {
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
							} 
						}
						completionCallback.returnBibliographicObject(references);
					} else if (503 == response.getStatusCode()) {
						_application.getLogger().exception(this, "Bibliographic objects retrieval 503: " + response.getText());
						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve bibliographic objects " + response.getStatusCode());
						completionCallback.bibliographyObjectNotFound("Bibliographic objects retrieval 503: " + response.getText());
					} else {
						_application.getLogger().exception(this, "Bibliographic objects retrieval: " + response.getStatusCode() + ": "+ response.getText());
						((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not retrieve bibliographic objects  " + response.getStatusCode());
						completionCallback.bibliographyObjectNotFound("Bibliographic objects not retrieved: " + response.getText());
					}
				}
			});
		} catch (RequestException e) {
			_application.getInitializer().addException("Couldn't retrieve Agent Person JSON");
		}	
	}
	
	@Override
	public void searchBibliographicObjects(
			final IPubMedPaginatedItemsRequestCompleted completionCallback, String typeQuery,
			String textQuery, int maxResults, int offset) throws IllegalArgumentException {
		
		String url = GWT.getModuleBaseURL() + "pubmed/search?format=json&typeQuery=" + typeQuery + "&textQuery=" + textQuery + "&maxResults=" + maxResults + "&offset=" + offset;
		if(!_application.isHostedMode()) url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL()) + "pubmed/search?format=json&typeQuery=" + typeQuery + "&textQuery=" + textQuery + "&maxResults=" + maxResults + "&offset=" + offset;
	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
	    builder.setTimeoutMillis(10000);
		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					if(exception instanceof RequestTimeoutException) {
						_application.getLogger().exception(this, "Couldn't complete bibliographic search (timeout) " + exception.getMessage());
						//((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Couldn't complete bibliographic search (timeout)");
						completionCallback.bibliographicSearchNotCompleted("Couldn't complete bibliographic search (timeout) " + exception.getMessage());
					} else {
						_application.getLogger().exception(this, "Couldn't complete bibliographic search (error)");
						//((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Couldn't complete bibliographic search (error)");
						completionCallback.bibliographicSearchNotCompleted("Couldn't complete bibliographic search (error) " + exception.getMessage());
					}
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						JsoPubmedSearchResultsWrapper pubmedSearchResultsWrapper = (JsoPubmedSearchResultsWrapper) parseJson(response.getText());
						JsArray pubmedEntries = pubmedSearchResultsWrapper.getResults();
						if(pubmedSearchResultsWrapper.getResults().length()>0 && (pubmedSearchResultsWrapper.getException()==null || pubmedSearchResultsWrapper.getException().trim().length()==0)) {
							ArrayList<MPublicationArticleReference> references = new ArrayList<MPublicationArticleReference>();
							for(int i=0; i<pubmedEntries.length(); i++) {
								if(pubmedEntries.get(i).equals(APubMedBibliograhyExtractorCommand.UNRECOGNIZED)) {
									MPublicationArticleReference reference = new MPublicationArticleReference();
									reference.setUnrecognized(APubMedBibliograhyExtractorCommand.UNRECOGNIZED);
									references.add(reference);
								} else if(pubmedEntries.get(i) instanceof Object) {
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
					} else if (503 == response.getStatusCode()) {
						_application.getLogger().exception(this, "Could not complete bibliographic search (503): " + response.getText());
						//((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not complete bibliographic search " + response.getStatusCode());
						completionCallback.bibliographicSearchNotCompleted("Could not complete bibliographic search (503)");
						//_application.getLogger().exception(this, "PubMed Search 503: " + response.getText());
						//completionCallback.textMiningNotCompleted(response.getText());
					} else {
						_application.getLogger().exception(this, "Could not complete bibliographic search: " + response.getStatusCode() + ": "+ response.getText());
						//((ProgressMessagePanel)((DialogGlassPanel)_application.getDialogPanel()).getPanel()).addErrorMessage("Could not complete bibliographic search  " + response.getStatusCode());
						completionCallback.bibliographicSearchNotCompleted("Could not complete bibliographic search: " + response.getStatusCode());
						//_application.getLogger().exception(this,  "PubMed Search " + response.getStatusCode() + ": "+ response.getText());
						//completionCallback.textMiningNotCompleted(response.getText());
					}
				}
			});
		} catch (RequestException e) {
			_application.getInitializer().addException("Couldn't execute Pubmed Search JSON (2)");
		}	
	}
}
