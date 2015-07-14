package org.mindinformatics.gwt.domeo.plugins.annotopia.ebi.src;

import org.mindinformatics.gwt.domeo.component.textmining.src.ITextminingRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.resource.ebi.service.IEbiConnector;
import org.mindinformatics.gwt.framework.src.Utils;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotopiaEbiConnector implements IEbiConnector {
	
	public String URL = Utils.DEFAULT_URL;
	
	private static final String ANTIBODIES_SEARCH = "nif-0000-07730-1";
	
	private static final String ORGANISATION_SEARCH = "nlx_144509-1";
	private static final String INTEGRATED_ANIMAL_SEARCH = "nlx_154697-1";
	
	protected IApplication _application;

	public AnnotopiaEbiConnector(IApplication application, String url) {
		_application = application;
		if(url!=null) URL = url;
	}

	@Override
	public void textmine(final ITextminingRequestCompleted completionCallback, final String url, String pmcid)
			throws IllegalArgumentException {
		
		JsUtils.JsUtilsImpl utils = new JsUtils.JsUtilsImpl();
		Properties v = utils.parseJSON("{\"apiKey\":\""+ Utils.getAnnotopiaApiKey() +  "\",\"format\":\"domeo\",\"pmcid\":\""+pmcid + "\"}");
		try {
			Ajax.ajax(Ajax.createSettings()
				.setUrl(URL+"cn/ebi/textmine")
				.setHeaders(Utils.getAnnotopiaOAuthToken( ))
		        .setDataType("json") // txt, json, jsonp, xml
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
