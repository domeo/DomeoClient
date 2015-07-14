package org.mindinformatics.gwt.framework.component.reporting.src.testing;

import org.mindinformatics.gwt.framework.component.preferences.src.BooleanPreference;
import org.mindinformatics.gwt.framework.component.reporting.src.AReportsManager;
import org.mindinformatics.gwt.framework.src.Application;
import org.mindinformatics.gwt.framework.src.Utils;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class JsonReportManager extends AReportsManager {

	public JsonReportManager(IApplication application, ICommandCompleted callbackCompleted) {
		super(application, callbackCompleted);
	}
	
	public void sendWidgetAsEmail(String sourceComponent, String title, Widget message, String sourceUrl) {
		
		if(((BooleanPreference)_application.getPreferences().
				getPreferenceItem(Application.class.getName(), Application.PREF_AUTOMATICALLY_REPORT_ISSUES))==null
				|| (((BooleanPreference)_application.getPreferences().getPreferenceItem(Application.class.getName(), Application.PREF_AUTOMATICALLY_REPORT_ISSUES))!=null 
				&& !((BooleanPreference)_application.getPreferences().getPreferenceItem(Application.class.getName(), Application.PREF_AUTOMATICALLY_REPORT_ISSUES)).getValue())) {
			_application.getLogger().warn(LOG_ANOMALY_NOT_REPORTED, sourceComponent, message.getElement().getInnerText());
			return;
		}
		
		_application.getLogger().warn(LOG_ANOMALY, sourceComponent, message.getElement().getInnerText());
		
		String url = GWT.getModuleBaseURL() + "reporting/email?format=json";
		if(!_application.isHostedMode())
			url = Utils.getUrlBase(GWT.getModuleBaseURL()) + "reporting/email?format=json";

	    RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
	    builder.setHeader("Content-type", "application/json");
		
	    StringBuffer postData = new StringBuffer();
	    postData.append("{");
	    postData.append("\"user\": \"" + _application.getUserManager().getUser().getUserName() + "\",");
	    postData.append("\"subject\": \"[Domeo Test Instance] " + title + "\",");
	    postData.append("\"component\": \"" + sourceComponent + "\",");
	    postData.append("\"resource\": \"" + sourceUrl + "\",");
	    postData.append("\"message\": \"" + message.getElement().getInnerText() + "\"");
	    postData.append("}");
	    
		try {
			Request request = builder.sendRequest(postData.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					_application.getInitializer().addException("Couldn't retrieve User Profile JSON");
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {

						/*
						JsoProfile jsoProfile = (JsoProfile)((JsArray) parseJson(response.getText())).get(0);
						
						MProfile profile = new MProfile();
						profile.setUuid(jsoProfile.getUuid());
						profile.setName(jsoProfile.getName());
						profile.setDescription(jsoProfile.getDescription());
						profile.setLastSavedOn(jsoProfile.getCreatedOn());
						
						JsArray<JsoAgent> creators = jsoProfile.getCreatedBy();
						
						JsoPerson jperson = (JsoPerson) creators.get(0);
						MAgentPerson person = new MAgentPerson();
						person.setUuid(jperson.getUuid());
						person.setName(jperson.getName());
						profile.setLastSavedBy(person);
						
						JsArray<JsoProfileEntry> plugins = jsoProfile.getStatusPlugins();
						for(int i=0; i<plugins.length(); i++) {
							JsoProfileEntry entry = plugins.get(i);
							profile.getPlugins().put(entry.getUuid(), entry.getStatus());
						}
						
						setCurrentProfile(profile);
						callback.updateCurrentProfile();
						*/
					} else {
						_application.getInitializer().addException("Couldn't retrieve User Profile JSON ("
								+ response.getStatusText() + ")");
					}
				}
			});
		} catch (RequestException e) {
			_application.getInitializer().addException("Couldn't retrieve User Profile JSON");
		}
	}

	@Override
	public String recordPathEntry(String sourceFrom, String sourceTo) {
		// TODO Auto-generated method stub		
		_application.getLogger().warn(LOG_ANOMALY,"Recording path from " + sourceFrom + " to " + sourceTo);
		
		String url = GWT.getModuleBaseURL() + "reporting/path?format=json";
		if(!_application.isHostedMode())
			url = Utils.getUrlBase(GWT.getModuleBaseURL()) + "reporting/path?format=json";

	    RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
	    builder.setHeader("Content-type", "application/json");
		
	    StringBuffer postData = new StringBuffer();
	    postData.append("{");
	    postData.append("\"user\": \"" + _application.getUserManager().getUser().getUserName() + "\",");
	    postData.append("\"resourceFrom\": \"" + sourceFrom + "\",");
	    postData.append("\"resourceTo\": \"" + sourceTo + "\",");
	    postData.append("}");
	    
		try {
			Request request = builder.sendRequest(postData.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					_application.getInitializer().addException("Couldn't retrieve User Profile JSON");
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {

						/*
						JsoProfile jsoProfile = (JsoProfile)((JsArray) parseJson(response.getText())).get(0);
						
						MProfile profile = new MProfile();
						profile.setUuid(jsoProfile.getUuid());
						profile.setName(jsoProfile.getName());
						profile.setDescription(jsoProfile.getDescription());
						profile.setLastSavedOn(jsoProfile.getCreatedOn());
						
						JsArray<JsoAgent> creators = jsoProfile.getCreatedBy();
						
						JsoPerson jperson = (JsoPerson) creators.get(0);
						MAgentPerson person = new MAgentPerson();
						person.setUuid(jperson.getUuid());
						person.setName(jperson.getName());
						profile.setLastSavedBy(person);
						
						JsArray<JsoProfileEntry> plugins = jsoProfile.getStatusPlugins();
						for(int i=0; i<plugins.length(); i++) {
							JsoProfileEntry entry = plugins.get(i);
							profile.getPlugins().put(entry.getUuid(), entry.getStatus());
						}
						
						setCurrentProfile(profile);
						callback.updateCurrentProfile();
						*/
					} else {
						_application.getInitializer().addException("Couldn't retrieve User Profile JSON ("
								+ response.getStatusText() + ")");
					}
				}
			});
		} catch (RequestException e) {
			_application.getInitializer().addException("Couldn't retrieve User Profile JSON");
		}
		return null;
	}
}
