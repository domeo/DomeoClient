package org.mindinformatics.gwt.domeo.plugins.annotopia.nif.src;

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

import com.google.gwt.core.client.JavaScriptObject;
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
public class AnnotopiaNifConnector implements INifConnector {
	
	public String URL = "http://127.0.0.1:8090/";
	
	private static final String ANTIBODIES_SEARCH = "nif-0000-07730-1";
	
	private static final String ORGANISATION_SEARCH = "nlx_144509-1";
	
	private static final String INTEGRATED_ANIMAL_SEARCH = "nif-0000-08137-1";
	
	protected IApplication _application;

	public AnnotopiaNifConnector(IApplication application, String url) {
		_application = application;
		if(url!=null) URL = url;
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
	
	public static native  String stringify(JavaScriptObject obj) /*-{
		return JSON.stringify(obj);
	}-*/;

//	@Override
//	public void searchTerm(final IBioPortalItemsRequestCompleted completionCallback,
//			String textQuery, String virtualIds)
//			throws IllegalArgumentException {
//		_application.getLogger().debug(this, "Searching term: " + textQuery);
//		_application.getProgressPanelContainer().setProgressMessage("Searching term: " + textQuery);
//		
//		//Window.alert(URL+"cn/bioportal/search " +  ApplicationUtils.getAnnotopiaApiKey());
//		
//		try {
//			Ajax.ajax(Ajax.createSettings()
//				.setUrl(URL+"cn/nif/search")
//				.setHeaders(getAnnotopiaOAuthToken( ))
//		        .setDataType("json") // txt, json, jsonp, xml
//		        .setType("get")      // post, get
//		        .setData(GQuery.$$("apiKey: " + ApplicationUtils.getAnnotopiaApiKey() + ",format:domeo,q:"+textQuery)) // parameters for the query-string
//		        .setTimeout(10000)
//		        .setSuccess(new Function(){ // callback to be run if the request success
//		    		public void f() {
//		    			JsoBioPortalSearchResultsWrapper bioportalSearchResultsWrapper = (JsoBioPortalSearchResultsWrapper) parseJson(getDataProperties().toJsonString());
//						JsArray bioportalEntries = bioportalSearchResultsWrapper.getResults();
//						ArrayList<MLinkedResource> terms = new ArrayList<MLinkedResource>();
//						if(bioportalEntries!=null) {
//							for(int i=0; i<bioportalEntries.length(); i++) {
//								JsoBioPortalEntry bioportalEntry = (JsoBioPortalEntry) bioportalEntries.get(i);
//								MGenericResource source = ResourcesFactory.createGenericResource(bioportalEntry.getSourceUri(), 
//										bioportalEntry.getSourceLabel());
//								
//								MTrustedResource term =  ResourcesFactory.createTrustedResource(bioportalEntry.getUri(), bioportalEntry.getLabel(), source);
//								term.setDescription(bioportalEntry.getDescription());
//								terms.add(term);
//							}
//						}						
//						completionCallback.returnTerms(terms);
//		    		}
//		        })
//		        .setError(new Function(){ // callback to be run if the request fails
//		        	public void f() {
//		        		_application.getLogger().exception(this, 
//		        			"Couldn't complete BioPortal search process");
//		        		_application.getProgressPanelContainer().setErrorMessage(
//							"Couldn't complete BioPortal search process");
//		        	}
//		        })
//		     );
//		} catch (Exception e) {
//			e.printStackTrace();
//			_application.getLogger().exception(this, "Couldn't complete Bio Portal search " + e.getMessage());
//		}
//	}
//
//	@Override
//	public void searchTerm(final IBioPortalItemsRequestCompleted completionCallback,
//			String textQuery, String virtualIds, int pageNumber, int pageSize)
//			throws IllegalArgumentException {
//		
//		_application.getLogger().debug(this, "Searching term: " + textQuery);
//		_application.getProgressPanelContainer().setProgressMessage("Searching term: " + textQuery);
//
//		try {
//			Ajax.ajax(Ajax.createSettings()
//				.setUrl(URL+"cn/nif/search")
//				.setHeaders(getAnnotopiaOAuthToken( ))
//		        .setDataType("json") // txt, json, jsonp, xml
//		        .setType("get")      // post, get
//		        .setData(GQuery.$$("apiKey: " + ApplicationUtils.getAnnotopiaApiKey() + 
//		        		",format:domeo,pagenumber:" + pageNumber + ",pagezie:" + pageSize + "q:"+textQuery)) // parameters for the query-string
//		        .setTimeout(10000)
//		        .setSuccess(new Function(){ // callback to be run if the request success
//		    		public void f() {
//		    			JsoBioPortalSearchResultsWrapper bioportalSearchResultsWrapper = (JsoBioPortalSearchResultsWrapper) parseJson(getDataProperties().toJsonString());
//						JsArray bioportalEntries = bioportalSearchResultsWrapper.getResults();
//						ArrayList<MLinkedResource> terms = new ArrayList<MLinkedResource>();
//						if(bioportalEntries!=null) {
//							for(int i=0; i<bioportalEntries.length(); i++) {
//								JsoBioPortalEntry bioportalEntry = (JsoBioPortalEntry) bioportalEntries.get(i);
//								MGenericResource source = ResourcesFactory.createGenericResource(bioportalEntry.getSourceUri(), 
//										bioportalEntry.getSourceLabel());
//								
//								MTrustedResource term =  ResourcesFactory.createTrustedResource(bioportalEntry.getUri(), bioportalEntry.getLabel(), source);
//								term.setDescription(bioportalEntry.getDescription());
//								terms.add(term);
//							}
//						}					
//						
//						int pageNumber = Integer.parseInt(bioportalSearchResultsWrapper.getPageNumber())>0 ? Integer.parseInt(bioportalSearchResultsWrapper.getPageNumber()): -1;
//						int totalPages = Integer.parseInt(bioportalSearchResultsWrapper.getTotalPages())>=0 ? Integer.parseInt(bioportalSearchResultsWrapper.getTotalPages()): -1;
//						int pageSize = Integer.parseInt(bioportalSearchResultsWrapper.getPageSize())>=0 ? Integer.parseInt(bioportalSearchResultsWrapper.getPageSize()): -1;
//
//						//completionCallback.returnTerms(terms);
//						completionCallback.returnTerms(totalPages, pageSize, pageNumber, terms);
//		    		}
//		        })
//		        .setError(new Function(){ // callback to be run if the request fails
//		        	public void f() {
//		        		_application.getLogger().exception(this, 
//		        			"Couldn't complete BioPortal search process");
//		        		_application.getProgressPanelContainer().setErrorMessage(
//							"Couldn't complete BioPortal search process");
//		        	}
//		        })
//		     );
//		} catch (Exception e) {
//			e.printStackTrace();
//			_application.getLogger().exception(this, "Couldn't complete Bio Portal search " + e.getMessage());
//		}
//	}

	@Override
	public void annotate(final ITextminingRequestCompleted completionCallback, final String url,
			final String textContent, final String includeCat, final String excludeCat,
			final int minLength, final boolean longestOnly, final boolean includeAbbrev, 
			final boolean includeAcronym, final boolean includeNumbers)
	throws IllegalArgumentException {
		
		JsUtils.JsUtilsImpl utils = new JsUtils.JsUtilsImpl();
		Properties v = utils.parseJSON("{\"apiKey\":\""+ ApplicationUtils.getAnnotopiaApiKey() +  "\",\"format\":\"domeo\",\"text\":\"" + textContent + "\"}");
		try {
			Ajax.ajax(Ajax.createSettings()
				.setUrl(URL+"cn/nif/textmine")
				.setHeaders(getAnnotopiaOAuthToken( ))
		        .setDataType("json") // txt, json, jsonp, xml
		        .setType("post")      // post, get
		        .setData(v) // parameters for the query-string setData(GQuery.$$("apiKey: testkey, set: " + value))
		        .setTimeout(10000)
		        .setSuccess(new Function(){ // callback to be run if the request success
		        	public void f() {
		        		JsAnnotationSet set = (JsAnnotationSet) parseJson(getDataProperties().toJsonString());
						completionCallback.returnTextminingResults(set, false);
		        	}
	        	})
		        .setError(new Function(){ // callback to be run if the request fails
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
			e.printStackTrace();
			_application.getLogger().exception(this, "Couldn't complete nif text mining");
		}
		        	
		
//		String virtualIds = "";
//		String url = GWT.getModuleBaseURL() + "bioPortal/textmine?format=json&textContent=" + URL.encode(textContent) + "&url=" + source + "&ontologies=" + virtualIds+ "&longestOnly=" + longestOnly + "&wholeWordOnly=" + wholeWordOnly + "&filterNumbers=" + filterNumbers + 
//				"&withDefaultStopWords=" + withDefaultStopWords + "&isStopWordsCaseSensitive=" + isStopWordsCaseSensitive + "&scored=" + scored + 
//				"&withSynonyms=" + withSynonyms;
//		if(!_application.isHostedMode())
//			url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL()) + "ncbo/textmine?format=json&textContent=" + URL.encode(textContent) + "&url=" + source + 
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
	public void searchData(final INifDataRequestCompleted completionCallback,
			String textQuery, final String type, final String vendor, final String resource,
			final int pageNumber, final int pageSize) throws IllegalArgumentException
	{
		_application.getLogger( ).debug(this, "Searching data: " + textQuery);
		_application.getProgressPanelContainer( ).setProgressMessage("Searching data: " + textQuery);
		
		try {
			Ajax.ajax(Ajax.createSettings( )
					.setUrl(URL + "cn/nif/search")
					.setHeaders(getAnnotopiaOAuthToken( ))
					.setDataType("json")
					.setType("get")
					.setData(GQuery.$$(
							"apiKey: " + ApplicationUtils.getAnnotopiaApiKey( ) + ","
							+ "format: domeo,"
							+ "q: " + textQuery + ","
							+ "resource: " + resource + ","
							+ "type: " + type + ","
							+ "vendor: " + vendor
					))
					.setTimeout(10000)
					.setSuccess(new Function( ) {
						public void f( ) {
							JsoNifDataSearchResultsWrapper result = (JsoNifDataSearchResultsWrapper)parseJson(getDataProperties( ).toJsonString( ));
							JsArray<JsoNifDataEntry> entries = result.getResults( );
							ArrayList<MGenericResource> data = new ArrayList<MGenericResource>( );
							if(entries != null) {
								for(int i = 0; i < entries.length( ); i++) {
									JsoNifDataEntry entry = entries.get(i);
									MGenericResource source = ResourcesFactory.createGenericResource(
											entry.getSourceUri( ),
											entry.getSourceLabel( )
									);
									MGenericResource normalised = _application.getResourcesManager( ).cacheResource(source);
									
									// create the result based on the resource
									if(resource.equals(ANTIBODIES_SEARCH)) {
										MAntibody antibody = new MAntibody(
												entry.getUri( ),
												entry.getLabel( ),
												normalised
										);
										antibody.setCloneId(entry.getCloneId( ));
										antibody.setVendor(entry.getVendor( ));
										antibody.setOrganism(entry.getOrganism( ));
										antibody.setType(entry.getType( ));
										antibody.setCatalog(entry.getCatalog( ));
										data.add(antibody);
									} else if(resource.equals(ORGANISATION_SEARCH) || resource.equals(INTEGRATED_ANIMAL_SEARCH)) {
										MLinkedResource linkedResource = new MLinkedResource(
												entry.getUri( ), 
												entry.getLabel( ), 
												entry.getDescription( )
										);
										linkedResource.setSource(normalised);
										data.add(linkedResource);
									}									
								}
							}
							
							// return the results
							_application.getLogger( ).debug(this, "Number of NIF results: " + data.size( ));
							completionCallback.returnData(data);
						}
					})
					.setError(new Function( ) {
						public void f( ) {
							_application.getLogger( ).exception(this, "Couldn't complete NIF search process");
				        	_application.getProgressPanelContainer( ).setErrorMessage("Couldn't complete NIF search process");
						}
					})
			);
		} catch(Exception e) {
			e.printStackTrace( );
			_application.getLogger().exception(this, "Couldn't complete NIF search " + e.getMessage());
		}
	}
}
