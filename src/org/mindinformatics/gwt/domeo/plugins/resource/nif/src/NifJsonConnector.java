package org.mindinformatics.gwt.domeo.plugins.resource.nif.src;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.component.textmining.src.ITextminingRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibody;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.model.JsoNifDataEntry;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.model.JsoNifDataSearchResultsWrapper;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.service.INifConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.service.INifDataRequestCompleted;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
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
public class NifJsonConnector implements INifConnector {
	
	protected IApplication _application;
	//private IBioPortalItemsRequestCompleted _callback;

	public NifJsonConnector(IApplication application) {
		_application = application;
		//_callback = callbackCompleted;
	}
	
	public static native JavaScriptObject parseJson(String jsonStr) /*-{
	  	return eval('('+jsonStr+')');
	}-*/;

//	@Override
//	public void searchTerm(final IBioPortalItemsRequestCompleted completionCallback,
//			String textQuery, String virtualIds)
//			throws IllegalArgumentException {
//		
//		String url = GWT.getModuleBaseURL() + "bioportal/search?format=json&textQuery=" + textQuery + "&ontologies=" + virtualIds;
//		if(!_application.isHostedMode())
//			url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL()) + "bioportal/search?format=json&textQuery=" + textQuery + "&ontologies=" + virtualIds;
//	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
//
//		try {
//			Request request = builder.sendRequest(null, new RequestCallback() {
//				public void onError(Request request, Throwable exception) {
//					_application.getInitializer().addException("Couldn't retrieve BioPortal terms JSON");
//				}
//
//				public void onResponseReceived(Request request, Response response) {
//					if (200 == response.getStatusCode()) {
//						JsoBioPortalSearchResultsWrapper bioportalSearchResultsWrapper = (JsoBioPortalSearchResultsWrapper) parseJson(response.getText());
//						JsArray bioportalEntries = bioportalSearchResultsWrapper.getResults();
//						ArrayList<MLinkedDataResource> terms = new ArrayList<MLinkedDataResource>();
//						if(bioportalEntries!=null) {
//							for(int i=0; i<bioportalEntries.length(); i++) {
//								JsoBioPortalEntry bioportalEntry = (JsoBioPortalEntry) bioportalEntries.get(i);
//								GenericResource source = new GenericResource();
//								source.setUrl(bioportalEntry.getSourceUri());
//								source.setLabel(bioportalEntry.getSourceLabel());
//								
//								MLinkedDataResource term = new MLinkedDataResource();
//								term.setUrl(bioportalEntry.getUri());
//								term.setLabel(bioportalEntry.getLabel());
//								term.setDescription(bioportalEntry.getDescription());
//								term.setSource(source);
//								terms.add(term);
//							}
//						}
//						
//						/*
//						int pageNumber = Integer.parseInt(bioportalSearchResultsWrapper.getPageNumber())>0 ? Integer.parseInt(bioportalSearchResultsWrapper.getPageNumber()): -1;
//						int totalPages = Integer.parseInt(bioportalSearchResultsWrapper.getTotalPages())>=0 ? Integer.parseInt(bioportalSearchResultsWrapper.getTotalPages()): -1;
//						int pageSize = Integer.parseInt(bioportalSearchResultsWrapper.getPageSize())>=0 ? Integer.parseInt(bioportalSearchResultsWrapper.getPageSize()): -1;
//						*/
//						
//						completionCallback.returnTerms(terms);
//					} else {
//						_application.getInitializer().addException("Couldn't retrieve BioPortal terms JSON ("
//								+ response.getStatusText() + ")");
//					}
//				}
//			});
//		} catch (RequestException e) {
//			_application.getInitializer().addException("Couldn't retrieve BioPortal terms JSON");
//		}	
//	}
//
//	@Override
//	public void searchTerm(final IBioPortalItemsRequestCompleted completionCallback,
//			String textQuery, String virtualIds, int pageNumber, int pageSize)
//			throws IllegalArgumentException {
//		
//		String url = GWT.getModuleBaseURL() + "bioportal/search?format=json&textQuery=" + textQuery + "&ontologies=" + virtualIds +
//				"&pagenumber=" + pageNumber + "&pagesize=" + pageSize;
//		if(!_application.isHostedMode())
//			url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL()) + "bioportal/search?format=json&textQuery=" + textQuery + "&ontologies=" + virtualIds +
//					"&pagenumber=" + pageNumber + "&pagesize=" + pageSize;
//	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
//
//		try {
//			Request request = builder.sendRequest(null, new RequestCallback() {
//				public void onError(Request request, Throwable exception) {
//					_application.getInitializer().addException("Couldn't retrieve BioPortal terms JSON");
//				}
//
//				public void onResponseReceived(Request request, Response response) {
//					if (200 == response.getStatusCode()) {
//						JsoBioPortalSearchResultsWrapper bioportalSearchResultsWrapper = (JsoBioPortalSearchResultsWrapper) parseJson(response.getText());
//						JsArray bioportalEntries = bioportalSearchResultsWrapper.getResults();
//						ArrayList<MLinkedDataResource> terms = new ArrayList<MLinkedDataResource>();
//						for(int i=0; i<bioportalEntries.length(); i++) {
//							JsoBioPortalEntry bioportalEntry = (JsoBioPortalEntry) bioportalEntries.get(i);
//							MLinkedDataResource term = new MLinkedDataResource();
//							term.setUrl(bioportalEntry.getUri());
//							term.setLabel(bioportalEntry.getLabel());
//							terms.add(term);
//						}
//						
//						int pageNumber = Integer.parseInt(bioportalSearchResultsWrapper.getPageNumber())>0 ? Integer.parseInt(bioportalSearchResultsWrapper.getPageNumber()): -1;
//						int totalPages = Integer.parseInt(bioportalSearchResultsWrapper.getTotalPages())>=0 ? Integer.parseInt(bioportalSearchResultsWrapper.getTotalPages()): -1;
//						int pageSize = Integer.parseInt(bioportalSearchResultsWrapper.getPageSize())>=0 ? Integer.parseInt(bioportalSearchResultsWrapper.getPageSize()): -1;
//						
//						completionCallback.returnTerms(totalPages, pageSize, pageNumber, terms);
//					} else {
//						_application.getInitializer().addException("Couldn't retrieve BioPortal terms JSON ("
//								+ response.getStatusText() + ")");
//					}
//				}
//			});
//		} catch (RequestException e) {
//			_application.getInitializer().addException("Couldn't retrieve BioPortal terms JSON");
//		}	
//	}
	
	@Override
	public void searchData(final INifDataRequestCompleted completionCallback,
			String textQuery, String type, String vendor, String resource, int pageNumber, int pageSize)
			throws IllegalArgumentException {
	    
	   if(resource.equals("nif-0000-07730-1")) {
	    
    		String url = GWT.getModuleBaseURL() + "nif/data?format=json&resource=" + resource + "&query=" + URL.encode(textQuery) + "&type=" + URL.encode(type) + "&vendor=" + URL.encode(vendor);
    		if(!_application.isHostedMode())
    			url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL()) + "nif/data?format=json&resource=" + resource + "&query=" + URL.encode(textQuery) + "&type=" + URL.encode(type) + "&vendor=" + URL.encode(vendor);
    	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
    	    builder.setTimeoutMillis(10000);
    	    _application.getLogger().debug(this, "Search Data with url" + url);
    	    try {
    			Request request = builder.sendRequest(null, new RequestCallback() {
    				public void onError(Request request, Throwable exception) {
    					if(exception instanceof RequestTimeoutException) {
    						_application.getLogger().exception(this, "Couldn't reach NIF data search (timeout) " + exception.getMessage());
    						completionCallback.reportException("Timeout");
    					} else {
    						_application.getLogger().exception(this, "Couldn't retrieve NIF data JSON " + exception.getMessage());
    						completionCallback.reportException();
    					}
    				}
    
    				public void onResponseReceived(Request request, Response response) {
    					if (200 == response.getStatusCode()) {
    						ArrayList<MGenericResource> data = new ArrayList<MGenericResource>();
    						JsoNifDataSearchResultsWrapper results = (JsoNifDataSearchResultsWrapper) parseJson(response.getText());
    						JsArray<JsoNifDataEntry> res = results.getResults();
    						for(int i=0; i<res.length(); i++) {
    							JsoNifDataEntry entry = res.get(i);
    							
    							MGenericResource source = ResourcesFactory.createGenericResource(entry.getSourceUri(), entry.getSourceLabel());
    							MGenericResource normalizedResource = _application.getResourcesManager().cacheResource(source);
    							if(source==normalizedResource) _application.getLogger().debug(this, "Resource cache size (after insert): " + _application.getResourcesManager().getResourceCacheSize());
    							
    							MAntibody antibody = new MAntibody(entry.getUri(), entry.getLabel(), normalizedResource);
    							antibody.setCloneId(entry.getCloneId());
    							antibody.setVendor(entry.getVendor());
    							antibody.setOrganism(entry.getOrganism());
    							antibody.setType(entry.getType());
    							antibody.setCatalog(entry.getCatalog());
    							//antibody.setSource(normalizedResource);
    							data.add(antibody);
    						}
    						_application.getLogger().debug(this, "Number NIF Data results: " + data.size());
    						completionCallback.returnData(data);
    					} else if (503 == response.getStatusCode()) {
    						_application.getLogger().exception(this, "503: " + response.getText());
    						completionCallback.reportException("Couldn't run Nif data search (503)");
    					} else {
    						_application.getLogger().exception(this, response.getStatusCode() + ": "+ response.getText());
    						completionCallback.reportException("Couldn't run Nif data search " + response.getStatusCode());
    					} 
    				}
    			});
    		} catch (RequestException e) {
    			_application.getLogger().exception(this, "Couldn't retrieve NIF data JSON");
    			completionCallback.reportException();
    		}	
	   } else if(resource.equals("nlx_144509-1") || resource.equals("nif-0000-08137-1")) {
	       String url = GWT.getModuleBaseURL() + "nif/data?format=json&resource=" + resource + "&query=" + URL.encode(textQuery) + "&type=" + URL.encode(type);
           if(!_application.isHostedMode())
               url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL()) + "nif/data?format=json&resource=" + resource + "&query=" + URL.encode(textQuery) + "&type=" + URL.encode(type);
           RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
           builder.setTimeoutMillis(10000);
           _application.getLogger().debug(this, "Search NIF resources with url" + url);
           try {
               Request request = builder.sendRequest(null, new RequestCallback() {
                   public void onError(Request request, Throwable exception) {
                       if(exception instanceof RequestTimeoutException) {
                           _application.getLogger().exception(this, "Couldn't reach NIF resources search (timeout) " + exception.getMessage());
                           completionCallback.reportException("Timeout");
                       } else {
                           _application.getLogger().exception(this, "Couldn't retrieve NIF resources JSON " + exception.getMessage());
                           completionCallback.reportException();
                       }
                   }
   
                   public void onResponseReceived(Request request, Response response) {
                       if (200 == response.getStatusCode()) {
                           ArrayList<MGenericResource> data = new ArrayList<MGenericResource>();
                           JsoNifDataSearchResultsWrapper results = (JsoNifDataSearchResultsWrapper) parseJson(response.getText());
                           JsArray<JsoNifDataEntry> res = results.getResults();
                           for(int i=0; i<res.length(); i++) {
                               JsoNifDataEntry entry = res.get(i);
                               
                               MGenericResource source = ResourcesFactory.createGenericResource(entry.getSourceUri(), entry.getSourceLabel());
                               _application.getLogger().debug(this, "Resource cache size: " + _application.getResourcesManager().getResourceCacheSize());
                               MGenericResource normalizedResource = _application.getResourcesManager().cacheResource(source);
                               _application.getLogger().debug(this, "Resource cache size: " + _application.getResourcesManager().getResourceCacheSize());
                               
                               MLinkedResource resource = new MLinkedResource(entry.getUri(), entry.getLabel(), entry.getDescription());
                               resource.setSource(normalizedResource);
                               data.add(resource);
                           }
                           _application.getLogger().debug(this, "Number NIF resources results: " + data.size());
                           completionCallback.returnData(data);
                       } else if (503 == response.getStatusCode()) {
                           _application.getLogger().exception(this, "503: " + response.getText());
                           completionCallback.reportException("Couldn't run Nif resources search (503)");
                       } else {
                           _application.getLogger().exception(this, response.getStatusCode() + ": "+ response.getText());
                           completionCallback.reportException("Couldn't run Nif resources search " + response.getStatusCode());
                       } 
                   }
               });
           } catch (RequestException e) {
               _application.getLogger().exception(this, "Couldn't retrieve NIF resources JSON");
               completionCallback.reportException();
           }   
	   }
	}

	@Override
	public void annotate(
			final ITextminingRequestCompleted completionCallback,
			String source, String textContent, String include, String exclude, int minLength, 
			boolean longestOnly, boolean includeAbbrev, 
			boolean includeAcronym, boolean includeNumbers)
			throws IllegalArgumentException {
		
		String url = GWT.getModuleBaseURL() + "nif/annotate?format=json&content=" + URL.encode(textContent) + 
				"&url=" + source + "&categoriesIn=" + include + "&categoriesOut=" + exclude + "&longestOnly=" + longestOnly + 
				"&includeAbbrev=" + includeAbbrev + "&includeAcronym=" + includeAcronym + "&includeNumbers=" + includeNumbers;
		if(!_application.isHostedMode())
			url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL()) + "nif/annotate?format=json&content=" + URL.encode(textContent) + 
				"&url=" + source + "&categoriesIn=" + include  + "&categoriesOut=" + exclude + "&longestOnly=" + longestOnly + 
				"&includeAbbrev=" + includeAbbrev + "&includeAcronym=" + includeAcronym + "&includeNumbers=" + includeNumbers;
	   
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		builder.setTimeoutMillis(10000);
		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					if(exception instanceof RequestTimeoutException) {
						_application.getLogger().exception(this, "Couldn't reach NIF annotator (timeout) " + exception.getMessage());
						completionCallback.textMiningNotCompleted("Timeout");
					} else {
						_application.getLogger().exception(this, "Couldn't retrieve NIF annotator JSON");
						completionCallback.textMiningNotCompleted();
					}
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						JsAnnotationSet set = (JsAnnotationSet) parseJson(response.getText());
						completionCallback.returnTextminingResults(set);
					} else if (503 == response.getStatusCode()) {
						_application.getLogger().exception(this, "503: " + response.getText());
						completionCallback.textMiningNotCompleted("Couldn't run Nif Annotator (503)");
					} else {
						_application.getLogger().exception(this,  response.getStatusCode() + ": "+ response.getText());
						completionCallback.textMiningNotCompleted("Couldn't run Nif Annotator " + response.getStatusCode());
					}
				}
			});
		} catch (RequestException e) {
			_application.getLogger().exception(this, "Couldn't retrieve NIF annotator JSON");
			completionCallback.textMiningNotCompleted();
		}	
	}	
}
