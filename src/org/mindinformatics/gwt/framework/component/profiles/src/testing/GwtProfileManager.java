package org.mindinformatics.gwt.framework.component.profiles.src.testing;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.profiles.model.MProfile;
import org.mindinformatics.gwt.framework.component.profiles.service.ProfilesService;
import org.mindinformatics.gwt.framework.component.profiles.service.ProfilesServiceAsync;
import org.mindinformatics.gwt.framework.component.profiles.src.AProfileManager;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class GwtProfileManager extends AProfileManager {

	public GwtProfileManager(IApplication application, ICommandCompleted callbackCompleted) {
		super(application, callbackCompleted);
	}

	@Override
	public void retrieveUserProfiles() {
		final AsyncCallback<ArrayList<MProfile>> userServiceCallback = new AsyncCallback<ArrayList<MProfile>>() {
			public void onFailure(Throwable caught) {
				Window.alert("Failed to get response from server" + caught.getMessage());
			}

			public void onSuccess(ArrayList<MProfile> userProfiles) {
				setProfiles(userProfiles);
				stageCompleted();
			}
		};

		final ProfilesServiceAsync profileService = GWT.create(ProfilesService.class);
		((ServiceDefTarget) profileService).setServiceEntryPoint(GWT.getModuleBaseURL() + "profiles");
		profileService.getUserAvailableProfiles(_application.getAgentManager().getUserPerson().getEmail(), userServiceCallback);
	}

	@Override
	public void retrieveUserCurrentProfile() {
		final AsyncCallback<MProfile> userServiceCallback = new AsyncCallback<MProfile>() {
			public void onFailure(Throwable caught) {
				Window.alert("Failed to get response from server" + caught.getMessage());
			}

			public void onSuccess(MProfile userProfile) {
				setCurrentProfile(userProfile);
				stageCompleted();
			}
		};

		final ProfilesServiceAsync profileService = GWT.create(ProfilesService.class);
		((ServiceDefTarget) profileService).setServiceEntryPoint(GWT.getModuleBaseURL() + "profiles");
		profileService.getUserCurrentProfile(_application.getAgentManager().getUserPerson().getEmail(), userServiceCallback);
	}

	@Override
	public MProfile saveUserProfile(MProfile newProfile,
			IUpdateProfileCallback callback) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MProfile saveCurrentProfile(MProfile currentProfile,
			IUpdateProfileCallback callback) {
		// TODO Auto-generated method stub
		return null;
	}

}
