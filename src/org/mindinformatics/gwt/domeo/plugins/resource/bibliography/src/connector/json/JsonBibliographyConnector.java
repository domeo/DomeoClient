package org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.json;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.IBibliographyConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.IStarringRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;
import org.mindinformatics.gwt.framework.src.ApplicationUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonBibliographyConnector implements IBibliographyConnector {

	private IDomeo _domeo;
	public JsonBibliographyConnector(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public void starResource(MDocumentResource documentResource, final IStarringRequestCompleted completionHandler) {
		String url = GWT.getModuleBaseURL() + "bibliography/star";
		if(!_domeo.isHostedMode())
			url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL()) + "bibliography/star";
	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
	    builder.setHeader("Content-Type", "application/json");
	    builder.setTimeoutMillis(10000);
	    
	    try {
	    	JSONObject r = new JSONObject();
			r.put("url", new JSONString(documentResource.getUrl()));
			r.put("label", new JSONString(documentResource.getLabel()));
			
			Request request = builder.sendRequest(r.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					Window.alert("error");
				}
				
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						completionHandler.documentResourceStarred();
					} else if (503 == response.getStatusCode()) {
						_domeo.getLogger().exception(this, "503: " + response.getText());
						Window.alert("503");
						//completionCallback.reportException("Couldn't perform BioPortal term search (503)");
					} else {
						_domeo.getLogger().exception(this,  response.getStatusCode() + "");
						Window.alert("code");
						//completionCallback.reportException("Couldn't perform BioPortal term search " + response.getStatusCode());
					}
				}
			});
		} catch (RequestException e) {
			_domeo.getLogger().exception(this, "Couldn't complete the starring of bibliography request");
			//completionCallback.reportException();
		}	
	}
	
	@Override
	public void unstarResource(MDocumentResource resource, final IStarringRequestCompleted completionHandler) {
		// TODO Auto-generated method stub
		
	}	
}
