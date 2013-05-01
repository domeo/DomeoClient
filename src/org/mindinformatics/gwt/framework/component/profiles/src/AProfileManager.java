package org.mindinformatics.gwt.framework.component.profiles.src;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.profiles.model.MProfile;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */ 
public abstract class AProfileManager implements IProfileManager {

	/**
	 * List of logs
	 */
	private ArrayList<MProfile> profiles = new ArrayList<MProfile>();
	private MProfile currentProfile;
	
	protected IApplication _application;
	private ICommandCompleted _callback;
	
	public AProfileManager(IApplication application, ICommandCompleted callback) {
		_application = application;
		_callback = callback;
	}
	
	/**
	 * Returns the list of messages in the trace list
	 * @return	The list of trace messages
	 */
	public ArrayList<MProfile> getUserProfiles() {
		return profiles;
	}
	
	public MProfile getUserCurrentProfile() {
		return currentProfile;
	}

	public void setCurrentProfile(MProfile currentProfile) {
		_application.getInitializer().addCompletionMessage("Agent Profile initialization... ");
		this.currentProfile = currentProfile;
	}
	
	public ArrayList<MProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(ArrayList<MProfile> profiles) {
		_application.getInitializer().addCompletionMessage("Agent Available Profiles initialization... ");
		this.profiles = profiles;
	}

	public void stageCompleted() {
		_callback.notifyStageCompletion();
	}
}
