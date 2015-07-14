package org.mindinformatics.gwt.domeo.plugins.annotopia.bioportal.src;

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
import org.mindinformatics.gwt.framework.src.Utils;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotopiaBioPortalConnector implements IBioPortalConnector {
	
	public String URL = Utils.DEFAULT_URL;
	
	protected IApplication _application;

	public AnnotopiaBioPortalConnector(IApplication application, String url) {
		_application = application;
		if(url!=null) URL = url;
	}

	@Override
	public void searchTerm(final IBioPortalItemsRequestCompleted completionCallback,
			String textQuery, String virtualIds)
			throws IllegalArgumentException {
		_application.getLogger().debug(this, "Searching term: " + textQuery);
		_application.getProgressPanelContainer().setProgressMessage("Searching term: " + textQuery);
		
		//Window.alert(URL+"cn/bioportal/search " +  Utils.getAnnotopiaApiKey());
		
		try {
			Ajax.ajax(Ajax.createSettings()
				.setUrl(URL+"cn/bioportal/search")
				/*.setHeaders(getAnnotopiaOAuthToken( ))*/
		        .setDataType("json") // txt, json, jsonp, xml
		        .setType("get")      // post, get
		        .setData(GQuery.$$(
		        		"apiKey: " + Utils.getAnnotopiaApiKey()  + ","
		        		+ "format:domeo,q:"+textQuery)) // parameters for the query-string
		        .setTimeout(10000)
		        .setSuccess(new Function(){ // callback to be run if the request success
		    		public void f() {
		    			JsoBioPortalSearchResultsWrapper bioportalSearchResultsWrapper = (JsoBioPortalSearchResultsWrapper) Utils.parseJson(getDataProperties().toJsonString());
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
						_application.getProgressPanelContainer().setCompletionMessage(terms.size() + " Results");
						completionCallback.returnTerms(terms);
		    		}
		        })
		        .setError(new Function(){ // callback to be run if the request fails
		        	public void f() {
		        		Window.alert(getDataObject()+"");
		        		_application.getLogger().exception(this, 
		        			"Couldn't complete BioPortal search process: " + getDataObject());
		        		_application.getProgressPanelContainer().setErrorMessage(
							"Couldn't complete BioPortal search process: " + getDataObject());
		        	}
		        })
		     );
		} catch (Exception e) {
			e.printStackTrace();
			_application.getLogger().exception(this, "Couldn't complete Bio Portal search " + e.getMessage());
		}
	}

	@Override
	public void searchTerm(final IBioPortalItemsRequestCompleted completionCallback,
			String textQuery, String virtualIds, int pageNumber, int pageSize)
			throws IllegalArgumentException {
		
		_application.getLogger().debug(this, "Searching term: " + textQuery);
		_application.getProgressPanelContainer().setProgressMessage("Searching term: " + textQuery);

		try {
			Ajax.ajax(Ajax.createSettings()
				.setUrl(URL+"cn/bioportal/search")
				/*.setHeaders(getAnnotopiaOAuthToken( ))*/
		        .setDataType("json") // txt, json, jsonp, xml
		        .setType("get")      // post, get
		        .setData(GQuery.$$("apiKey: " + Utils.getAnnotopiaApiKey() + 
		        		",format:domeo,pagenumber:" + pageNumber + ",pagezie:" + pageSize + "q:"+textQuery)) // parameters for the query-string
		        .setTimeout(10000)
		        .setSuccess(new Function(){ // callback to be run if the request success
		    		public void f() {
		    			JsoBioPortalSearchResultsWrapper bioportalSearchResultsWrapper = (JsoBioPortalSearchResultsWrapper) Utils.parseJson(getDataProperties().toJsonString());
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
						
						int pageNumber = Integer.parseInt(bioportalSearchResultsWrapper.getPageNumber())>0 ? Integer.parseInt(bioportalSearchResultsWrapper.getPageNumber()): -1;
						int totalPages = Integer.parseInt(bioportalSearchResultsWrapper.getTotalPages())>=0 ? Integer.parseInt(bioportalSearchResultsWrapper.getTotalPages()): -1;
						int pageSize = Integer.parseInt(bioportalSearchResultsWrapper.getPageSize())>=0 ? Integer.parseInt(bioportalSearchResultsWrapper.getPageSize()): -1;

						//completionCallback.returnTerms(terms);
						_application.getProgressPanelContainer().setCompletionMessage(terms.size() + " Results");
						completionCallback.returnTerms(totalPages, pageSize, pageNumber, terms);
		    		}
		        })
		        .setError(new Function(){ // callback to be run if the request fails
		        	public void f() {
		        		_application.getLogger().exception(this, 
		        			"Couldn't complete BioPortal search process: " + getDataObject());
		        		_application.getProgressPanelContainer().setErrorMessage(
							"Couldn't complete BioPortal search process:" + getDataObject());
		        	}
		        })
		     );
		} catch (Exception e) {
			e.printStackTrace();
			_application.getLogger().exception(this, "Couldn't complete Bio Portal search " + e.getMessage());
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
		
		JsUtils.JsUtilsImpl utils = new JsUtils.JsUtilsImpl();
		Properties v = utils.parseJSON("{\"apiKey\":\""+ Utils.getAnnotopiaApiKey() +  "\",\"format\":\"domeo\",\"text\":\"" + textContent + "\"}");
		try {
			Ajax.ajax(Ajax.createSettings()
				.setUrl(URL+"cn/bioportal/textmine")
				/*.setHeaders(getAnnotopiaOAuthToken( ))*/
		        .setDataType("json") // txt, json, jsonp, xml */
		        .setType("post")      // post, get
		        .setData(v) // parameters for the query-string setData(GQuery.$$("apiKey: testkey, set: " + value))
		        .setTimeout(10000)
		        .setSuccess(new Function(){ // callback to be run if the request success
		        	public void f() {
		        		JsAnnotationSet set = (JsAnnotationSet) Utils.parseJson(getDataProperties().toJsonString());
		        		_application.getProgressPanelContainer().setCompletionMessage("Text mining completed");
						completionCallback.returnTextminingResults(set, false);
		        	}
	        	})
		        .setError(new Function(){ // callback to be run if the request fails
		        	public void f() {
		        		_application.getLogger().exception(this, 
		        			"Couldn't complete existing annotation sets list saving: " + getDataObject());
		        		_application.getProgressPanelContainer().setErrorMessage(
							"Couldn't complete existing annotation sets list saving:" + getDataObject());
		        	}
		        })
		     );
		} catch (Exception e) {
			_application.getLogger().exception(this, "Couldn't complete existing annotation sets list saving");
		}
		        	
		
//		String virtualIds = "";
//		String url = GWT.getModuleBaseURL() + "bioPortal/textmine?format=json&textContent=" + URL.encode(textContent) + "&url=" + source + "&ontologies=" + virtualIds+ "&longestOnly=" + longestOnly + "&wholeWordOnly=" + wholeWordOnly + "&filterNumbers=" + filterNumbers + 
//				"&withDefaultStopWords=" + withDefaultStopWords + "&isStopWordsCaseSensitive=" + isStopWordsCaseSensitive + "&scored=" + scored + 
//				"&withSynonyms=" + withSynonyms;
//		if(!_application.isHostedMode())
//			url = Utils.getUrlBase(GWT.getModuleBaseURL()) + "ncbo/textmine?format=json&textContent=" + URL.encode(textContent) + "&url=" + source + 
//				"&ontologies=" + virtualIds + "&longestOnly=" + longestOnly + "&wholeWordOnly=" + wholeWordOnly + "&filterNumbers=" + filterNumbers + 
//				"&withDefaultStopWords=" + withDefaultStopWords + "&isStopWordsCaseSensitive=" + isStopWordsCaseSensitive + "&scored=" + scored + 
//				"&withSynonyms=" + withSynonyms;
//	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
//	    builder.setTimeoutMillis(10000);
//
//		try {
//			final Request request = builder.sendRequest(null, new RequestCallback() {
//				public void onError(Request request, Throwable exception) {
//					if(exception instanceof RequestTimeoutException) {
//						_application.getLogger().exception(this, "Couldn't reach BioPortal Annotator (timeout) " + exception.getMessage());
//						completionCallback.textMiningNotCompleted("Timeout");
//					} else {
//						_application.getLogger().exception(this, "Couldn't run BioPortal Annotator " + exception.getMessage());
//						completionCallback.textMiningNotCompleted();
//					}
//				}
//
//				public void onResponseReceived(Request request, Response response) {
//					if (200 == response.getStatusCode()) {
//						JsAnnotationSet set = (JsAnnotationSet) parseJson(response.getText());
//						completionCallback.returnTextminingResults(set, false);
//					} else if (503 == response.getStatusCode()) {
//						_application.getLogger().exception(this, "503: " + response.getText());
//						completionCallback.textMiningNotCompleted("Couldn't run BioPortal Annotator (503)");
//					} else {
//						_application.getLogger().exception(this,  response.getStatusCode() + ": "+ response.getText());
//						completionCallback.textMiningNotCompleted("Couldn't run BioPortal Annotator " + response.getStatusCode());
//					}
//				}
//			});
//		} catch (RequestException e) {
//			_application.getLogger().exception(this, "Couldn't retrieve BioPortal terms JSON");
//			completionCallback.textMiningNotCompleted();
//		}	
	}	

}
