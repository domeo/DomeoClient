package org.mindinformatics.gwt.framework.component.profiles.src;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.profiles.model.MProfile;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * Provides a basic implementation of the profile manager. Every user
 * has access to a list of predefined profiles. 
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */ 
public abstract class AProfileManager implements IProfileManager {

	protected IApplication _app;
	private ICommandCompleted _callback;
	
	/**
	 * All the profiles available to the current user
	 */
	private ArrayList<MProfile> userProfiles = new ArrayList<MProfile>();
	/**
	 * Current profile for the current user
	 */
	private MProfile currentProfile;
	
	public AProfileManager(IApplication application, ICommandCompleted callback) {
		_app = application;
		_callback = callback;
	}

	@Override
	public ArrayList<MProfile> getUserProfiles() {
		return userProfiles;
	}
	
	@Override
	public void setProfiles(ArrayList<MProfile> profiles) {
		_app.getInitializer().addCompletionMessage("Agent Available Profiles [" + profiles.size() + "] initialization... ");
		userProfiles = profiles;
	}

	@Override
	public MProfile getUserCurrentProfile() {
		return currentProfile;
	}

	@Override
	public void setCurrentProfile(MProfile profile) {
		_app.getInitializer().addCompletionMessage("Current  Profile [" + profile.getName() + "] initialization... ");
		currentProfile = profile;
	}

	protected void notifyActionCompletion() {
		_callback.notifyStageCompletion();
	}
}
