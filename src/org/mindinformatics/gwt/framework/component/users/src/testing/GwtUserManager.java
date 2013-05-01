package org.mindinformatics.gwt.framework.component.users.src.testing;

import java.util.Set;

import org.mindinformatics.gwt.framework.component.users.service.UsersService;
import org.mindinformatics.gwt.framework.component.users.service.UsersServiceAsync;
import org.mindinformatics.gwt.framework.component.users.src.AUserManager;
import org.mindinformatics.gwt.framework.model.users.IUser;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class GwtUserManager extends AUserManager {

	public GwtUserManager(IApplication application, ICommandCompleted callbackCompleted) {
		super(application, callbackCompleted);
	}
	
	@Override
	public void retrieveUser(String username) {
		final AsyncCallback<IUser> userServiceCallback = new AsyncCallback<IUser>() {
			public void onFailure(Throwable caught) {
				Window.alert("Failed to get response from server" + caught.getMessage());
			}

			public void onSuccess(IUser userFound) {
				setUser(userFound);
				stageCompleted();
			}
		};

		final UsersServiceAsync userService = GWT.create(UsersService.class);
		((ServiceDefTarget) userService).setServiceEntryPoint(GWT.getModuleBaseURL() + "users");
		userService.getUserInfo(username, userServiceCallback);
	}

	@Override
	public void retrieveUserGroups(String email) { 
		final AsyncCallback<Set<IUserGroup>> userServiceCallback = new AsyncCallback<Set<IUserGroup>>() {
			public void onFailure(Throwable caught) {
				Window.alert("Failed to get response from server" + caught.getMessage());
			}

			public void onSuccess(Set<IUserGroup> groups) {
				setUsersGroups(groups);
				stageCompleted();
			}
		};
		final UsersServiceAsync usersService = GWT.create(UsersService.class);
		((ServiceDefTarget) usersService).setServiceEntryPoint(GWT.getModuleBaseURL() + "users");
		usersService.getUserGroups(email, userServiceCallback);
	}
}
