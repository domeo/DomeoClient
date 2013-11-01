package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.src;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.component.textmining.src.ITextminingRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.model.JsoBioPortalEntry;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.model.JsoBioPortalSearchResultsWrapper;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.IBioPortalConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.IBioPortalItemsRequestCompleted;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTrustedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
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
import com.google.gwt.http.client.URL;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonBioPortalConnector implements IBioPortalConnector {
	
	protected IApplication _application;
	//private IBioPortalItemsRequestCompleted _callback;

	public JsonBioPortalConnector(IApplication application) {
		_application = application;
		//_callback = callbackCompleted;
	}
	
	public static native JavaScriptObject parseJson(String jsonStr) /*-{
	  	return eval('('+jsonStr+')');
	}-*/;

	@Override
	public void searchTerm(final IBioPortalItemsRequestCompleted completionCallback,
			String textQuery, String virtualIds)
			throws IllegalArgumentException {
		
		String url = GWT.getModuleBaseURL() + "bioportal/search?format=json&textQuery=" + URL.encode(textQuery) + "&ontologies=" + virtualIds;
		if(!_application.isHostedMode())
			url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL()) + "bioportal/search?format=json&textQuery=" + URL.encode(textQuery) + "&ontologies=" + virtualIds;
	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
	    builder.setTimeoutMillis(10000);

		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					if(exception instanceof RequestTimeoutException) {
						_application.getLogger().exception(this, "Couldn't retrieve BioPortal (timeout) " + exception.getMessage());
						completionCallback.reportException("Timeout");
					} else {
						_application.getLogger().exception(this, "Couldn't retrieve BioPortal terms JSON " + exception.getMessage());
						completionCallback.reportException();
					}
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						JsoBioPortalSearchResultsWrapper bioportalSearchResultsWrapper = (JsoBioPortalSearchResultsWrapper) parseJson(response.getText());
						JsArray bioportalEntries = bioportalSearchResultsWrapper.getResults();
						ArrayList<MLinkedResource> terms = new ArrayList<MLinkedResource>();
						if(bioportalEntries!=null) {
							for(int i=0; i<bioportalEntries.length(); i++) {
								JsoBioPortalEntry bioportalEntry = (JsoBioPortalEntry) bioportalEntries.get(i);
								MGenericResource source = ResourcesFactory.createGenericResource(bioportalEntry.getSourceUri(), 
										bioportalEntry.getSourceLabel());
								
								MTrustedResource term =  ResourcesFactory.createTrustedResource(bioportalEntry.getUri(), bioportalEntry.getLabel(), source);
								term.setDescription(bioportalEntry.getDescription());
								terms.add(term);
							}
						}
						
						/*
						int pageNumber = Integer.parseInt(bioportalSearchResultsWrapper.getPageNumber())>0 ? Integer.parseInt(bioportalSearchResultsWrapper.getPageNumber()): -1;
						int totalPages = Integer.parseInt(bioportalSearchResultsWrapper.getTotalPages())>=0 ? Integer.parseInt(bioportalSearchResultsWrapper.getTotalPages()): -1;
						int pageSize = Integer.parseInt(bioportalSearchResultsWrapper.getPageSize())>=0 ? Integer.parseInt(bioportalSearchResultsWrapper.getPageSize()): -1;
						*/
						
						completionCallback.returnTerms(terms);
					} else if (503 == response.getStatusCode()) {
						_application.getLogger().exception(this, "503: " + response.getText());
						completionCallback.reportException("Couldn't perform BioPortal term search (503)");
					} else {
						_application.getLogger().exception(this,  response.getStatusCode() + "");
						completionCallback.reportException("Couldn't perform BioPortal term search " + response.getStatusCode());
					}
				}
			});
		} catch (RequestException e) {
			_application.getLogger().exception(this, "Couldn't retrieve BioPortal terms JSON");
			completionCallback.reportException();
		}	
	}

	@Override
	public void searchTerm(final IBioPortalItemsRequestCompleted completionCallback,
			String textQuery, String virtualIds, int pageNumber, int pageSize)
			throws IllegalArgumentException {
		
		String url = GWT.getModuleBaseURL() + "bioportal/search?format=json&textQuery=" + URL.encode(textQuery) + "&ontologies=" + virtualIds +
				"&pagenumber=" + pageNumber + "&pagesize=" + pageSize;
		if(!_application.isHostedMode())
			url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL()) + "bioportal/search?format=json&textQuery=" + URL.encode(textQuery) + "&ontologies=" + virtualIds +
					"&pagenumber=" + pageNumber + "&pagesize=" + pageSize;
	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
	    builder.setTimeoutMillis(10000);

		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					if(exception instanceof RequestTimeoutException) {
						_application.getLogger().exception(this, "Couldn't reach BioPortal (timeout) " + exception.getMessage());
						completionCallback.reportException("Timeout");
					} else {
						_application.getLogger().exception(this, "Couldn't retrieve BioPortal terms JSON " + exception.getMessage());
						completionCallback.reportException();
					}
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						JsoBioPortalSearchResultsWrapper bioportalSearchResultsWrapper = (JsoBioPortalSearchResultsWrapper) parseJson(response.getText());
						JsArray bioportalEntries = bioportalSearchResultsWrapper.getResults();
						ArrayList<MLinkedResource> terms = new ArrayList<MLinkedResource>();
						for(int i=0; i<bioportalEntries.length(); i++) {
							JsoBioPortalEntry bioportalEntry = (JsoBioPortalEntry) bioportalEntries.get(i);
							
							MLinkedResource term = ResourcesFactory.createLinkedResource(bioportalEntry.getUri(), bioportalEntry.getLabel());
							terms.add(term);
						}
						
						int pageNumber = Integer.parseInt(bioportalSearchResultsWrapper.getPageNumber())>0 ? Integer.parseInt(bioportalSearchResultsWrapper.getPageNumber()): -1;
						int totalPages = Integer.parseInt(bioportalSearchResultsWrapper.getTotalPages())>=0 ? Integer.parseInt(bioportalSearchResultsWrapper.getTotalPages()): -1;
						int pageSize = Integer.parseInt(bioportalSearchResultsWrapper.getPageSize())>=0 ? Integer.parseInt(bioportalSearchResultsWrapper.getPageSize()): -1;
						
						completionCallback.returnTerms(totalPages, pageSize, pageNumber, terms);
					} else if (503 == response.getStatusCode()) {
						_application.getLogger().exception(this, "503: " + response.getText());
						completionCallback.reportException("Couldn't perform BioPortal term search (503)");
					} else {
						_application.getLogger().exception(this,  response.getStatusCode() + ": "+ response.getText());
						completionCallback.reportException("Couldn't perform BioPortal term search " + response.getStatusCode());
					}
				}
			});
		} catch (RequestException e) {
			_application.getLogger().exception(this, "Couldn't retrieve BioPortal terms JSON");
			completionCallback.reportException();
		}	
	}

	@Override
	public void textmine(
			final ITextminingRequestCompleted completionCallback,
			String source, String textContent, boolean longestOnly,
			boolean wholeWordOnly, boolean filterNumbers,
			boolean withDefaultStopWords, boolean isStopWordsCaseSensitive,
			boolean scored, boolean withSynonyms)
			throws IllegalArgumentException {
		String virtualIds = "";
		String url = GWT.getModuleBaseURL() + "bioportal/textmine?format=json&textContent=" + URL.encode(textContent) + "&url=" + source + "&ontologies=" + virtualIds+ "&longestOnly=" + longestOnly + "&wholeWordOnly=" + wholeWordOnly + "&filterNumbers=" + filterNumbers + 
				"&withDefaultStopWords=" + withDefaultStopWords + "&isStopWordsCaseSensitive=" + isStopWordsCaseSensitive + "&scored=" + scored + 
				"&withSynonyms=" + withSynonyms;
		if(!_application.isHostedMode())
			url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL()) + "bioportal/textmine?format=json&textContent=" + URL.encode(textContent) + "&url=" + source + 
				"&ontologies=" + virtualIds + "&longestOnly=" + longestOnly + "&wholeWordOnly=" + wholeWordOnly + "&filterNumbers=" + filterNumbers + 
				"&withDefaultStopWords=" + withDefaultStopWords + "&isStopWordsCaseSensitive=" + isStopWordsCaseSensitive + "&scored=" + scored + 
				"&withSynonyms=" + withSynonyms;
	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
	    builder.setTimeoutMillis(10000);

		try {
			final Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					if(exception instanceof RequestTimeoutException) {
						_application.getLogger().exception(this, "Couldn't reach BioPortal Annotator (timeout) " + exception.getMessage());
						completionCallback.textMiningNotCompleted("Timeout");
					} else {
						_application.getLogger().exception(this, "Couldn't run BioPortal Annotator " + exception.getMessage());
						completionCallback.textMiningNotCompleted();
					}
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						JsAnnotationSet set = (JsAnnotationSet) parseJson(response.getText());
						completionCallback.returnTextminingResults(set);
					} else if (503 == response.getStatusCode()) {
						_application.getLogger().exception(this, "503: " + response.getText());
						completionCallback.textMiningNotCompleted("Couldn't run BioPortal Annotator (503)");
					} else {
						_application.getLogger().exception(this,  response.getStatusCode() + ": "+ response.getText());
						completionCallback.textMiningNotCompleted("Couldn't run BioPortal Annotator " + response.getStatusCode());
					}
				}
			});
		} catch (RequestException e) {
			_application.getLogger().exception(this, "Couldn't retrieve BioPortal terms JSON");
			completionCallback.textMiningNotCompleted();
		}	
	}	
}
