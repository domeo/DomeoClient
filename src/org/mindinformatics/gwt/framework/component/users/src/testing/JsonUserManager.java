package org.mindinformatics.gwt.framework.component.users.src.testing;

import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.framework.component.users.model.JsoUser;
import org.mindinformatics.gwt.framework.component.users.model.JsoUserGroup;
import org.mindinformatics.gwt.framework.component.users.model.JsoUserRole;
import org.mindinformatics.gwt.framework.component.users.model.MUserGroup;
import org.mindinformatics.gwt.framework.component.users.model.MUserRole;
import org.mindinformatics.gwt.framework.component.users.src.AUserManager;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;
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

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonUserManager extends AUserManager {

	public JsonUserManager(IApplication application, ICommandCompleted callbackCompleted) {
		super(application, callbackCompleted);
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
	    		.replace(/\\'/g, "\\'");
		  	return JSON.parse(jsonStr);
		} catch (e) {
			alert("Error while parsing the JSON message: " + e);
		}
	}-*/;
	
	@Override
	public void retrieveUser(String username) {
		String url = GWT.getModuleBaseURL() + "users/"+username+"/info?format=json";
		if(!_application.isHostedMode())
			url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL())+ "users/"+username+"/info?format=json";
	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					_application.getInitializer().addException("Couldn't retrieve User JSON");
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						try {
							JsoUser user = (JsoUser)((JsArray) parseJson(response.getText())).get(0);
							setUser(user);
							stageCompleted();
						} catch(Exception e) {
							_application.getInitializer().addException("Couldn't parse User JSON ("
									+ response.getText() + ")");
						}
					} else {
						_application.getInitializer().addException("Couldn't retrieve User JSON ("
								+ response.getStatusText() + ")");
					}	
				}
			});
		} catch (RequestException e) {
			_application.getInitializer().addException("Couldn't retrieve User JSON");
		}
	}

	@Override
	public void retrieveUserGroups(String username) { 
		String url = GWT.getModuleBaseURL() + "users/"+username+"/groups?format=json";
		if(!_application.isHostedMode())
			url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL())+ "users/"+username+"/groups?format=json";
	    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

		try {
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					_application.getInitializer().addException("Couldn't retrieve User's Groups JSON");
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (200 == response.getStatusCode()) {
						try {
							JsArray groups = ((JsArray) parseJson(response.getText()));
							Set<IUserGroup> userGroups = new HashSet<IUserGroup>();
							for(int i=0; i<groups.length(); i++) {
								
								JsoUserGroup jsoUserGroup = (JsoUserGroup)groups.get(i);
			
								MUserGroup group = new MUserGroup();
								group.setUuid(jsoUserGroup.getUuid());
								group.setUri(jsoUserGroup.getUri());
								group.setName(jsoUserGroup.getName());
								group.setDescription(jsoUserGroup.getDescription());
								group.setCreatedOn(jsoUserGroup.getCreatedOn());
								group.setGroupLink(jsoUserGroup.getGroupLink());
								group.setReadPermission(jsoUserGroup.isReadPermission());
								group.setWritePermission(jsoUserGroup.isWritePermission());
												
								JsArray<JsoUserRole> roles = jsoUserGroup.getRoles();
								for(int j=0; j<roles.length(); j++) {
									JsoUserRole entry = roles.get(j);
									
									MUserRole role = new MUserRole();
									role.setUuid(entry.getUuid());
									role.setName(entry.getName());
									
									group.getRoles().add(role);
								}
								userGroups.add(group);
							}

							//IUserGroup usersGroup = (IUserGroup) parseJson(response.getText());
							setUsersGroups(userGroups);
							stageCompleted();
						} catch(Exception e) {
							_application.getInitializer().addException("Couldn't parse User's Groups JSON ("
									+ response.getText() + ") " + e);
						}
					} else {
						_application.getInitializer().addException("Couldn't retrieve User's Groups JSON ("
								+ response.getStatusText() + ")");
					}
				}
			});
		} catch (RequestException e) {
			_application.getInitializer().addException("Couldn't retrieve JSON");
		}
	}
}
