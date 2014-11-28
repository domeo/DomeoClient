package org.mindinformatics.gwt.domeo.plugins.annotopia.ebi.src;

import org.mindinformatics.gwt.domeo.component.textmining.src.ITextminingRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.resource.ebi.service.IEbiConnector;
import org.mindinformatics.gwt.framework.src.ApplicationUtils;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotopiaEbiConnector implements IEbiConnector {
	
	public String URL = "http://127.0.0.1:8090/";
	
	private static final String ANTIBODIES_SEARCH = "nif-0000-07730-1";
	
	private static final String ORGANISATION_SEARCH = "nlx_144509-1";
	private static final String INTEGRATED_ANIMAL_SEARCH = "nlx_154697-1";
	
	protected IApplication _application;

	public AnnotopiaEbiConnector(IApplication application, String url) {
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

	@Override
	public void textmine(final ITextminingRequestCompleted completionCallback, final String url, String pmcid)
			throws IllegalArgumentException {
		
		JsUtils.JsUtilsImpl utils = new JsUtils.JsUtilsImpl();
		Properties v = utils.parseJSON("{\"apiKey\":\""+ ApplicationUtils.getAnnotopiaApiKey() +  "\",\"format\":\"domeo\",\"pmcid\":\""+pmcid + "\"}");
		try {
			Ajax.ajax(Ajax.createSettings()
				.setUrl(URL+"cn/ebi/textmine")
				.setHeaders(getAnnotopiaOAuthToken( ))
		        .setDataType("json") // txt, json, jsonp, xml
		        .setType("post")      // post, get
		        .setData(v) // parameters for the query-string setData(GQuery.$$("apiKey: testkey, set: " + value))
		        .setTimeout(10000)
		        .setSuccess(new Function(){ // callback to be run if the request success
		        	public void f() {
		        		JsAnnotationSet set = (JsAnnotationSet) parseJson(getDataProperties().toJsonString());
		        		_application.getProgressPanelContainer().setCompletionMessage("Text mining completed");
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
}
