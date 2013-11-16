package org.mindinformatics.gwt.framework.component.agents.src.testing;

import org.mindinformatics.gwt.framework.component.agents.model.JsoAgent;
import org.mindinformatics.gwt.framework.component.agents.src.AAgentManager;
import org.mindinformatics.gwt.framework.component.agents.src.AgentsFactory;
import org.mindinformatics.gwt.framework.model.agents.IPerson;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;
import org.mindinformatics.gwt.framework.src.ApplicationUtils;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class JsonAgentManager extends AAgentManager {

	public JsonAgentManager(IApplication application, ICommandCompleted callbackCompleted) {
		super(application, callbackCompleted);
	}
	
//	public static native JavaScriptObject parseJson(String jsonStr) /*-{
//	  	return eval(jsonStr);
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
		  	return JSON.parse(jsonStr);
		} catch (e) {
			alert("Error while parsing the JSON message: " + e);
		}
	}-*/;

	@Override
	public void retrieveSoftware(String name, String version) {
		String url = GWT.getModuleBaseURL() + "agents/"+name.toLowerCase()+"/"+version+"?format=json";
		if(!_application.isHostedMode())
			url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL())+ "agents/"+name.toLowerCase()+"/"+version+"?format=json";
	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					_application.getInitializer().addException("Couldn't retrieve Agent Software JSON");
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						AgentsFactory factory = new AgentsFactory();
						JsoAgent software = (JsoAgent)((JsArray) parseJson(response.getText())).get(0);
						setSoftware((ISoftware)factory.createAgent(software));
						stageCompleted();
					} else {
						_application.getInitializer().addException("Couldn't retrieve Agent Software JSON ("
								+ response.getStatusText() + ")");
					}
				}
			});
		} catch (RequestException e) {
			_application.getInitializer().addException("Couldn't retrieve Agent Software JSON");
		}
	}

	@Override
	public void retrievePerson(String userUuid) {
		String url = GWT.getModuleBaseURL() + "agents/"+userUuid+"/info?format=json";
		if(!_application.isHostedMode())
			url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL()) + "agents/"+userUuid+"/info?format=json";
	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					_application.getInitializer().addException("Couldn't retrieve Agent Person JSON");
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						AgentsFactory factory = new AgentsFactory();
						JsoAgent person = (JsoAgent)((JsArray) parseJson(response.getText())).get(0);
						setUserPerson((IPerson)factory.createAgent(person));
						stageCompleted();
					} else {
						_application.getInitializer().addException("Couldn't retrieve Agent Person JSON ("
								+ response.getStatusText() + ")");
					}
				}
			});
		} catch (RequestException e) {
			_application.getInitializer().addException("Couldn't retrieve Agent Person JSON");
		}
	}
}
