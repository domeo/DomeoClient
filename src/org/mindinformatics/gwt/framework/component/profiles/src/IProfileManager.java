package org.mindinformatics.gwt.framework.component.profiles.src;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.profiles.model.MProfile;
import org.mindinformatics.gwt.framework.component.profiles.src.testing.IUpdateProfileCallback;

public interface IProfileManager {

	public ArrayList<MProfile> getUserProfiles();
	public MProfile getUserCurrentProfile();
	public void retrieveUserProfiles();
	public void retrieveUserCurrentProfile();
	public void setCurrentProfile(MProfile currentProfile);
	
	public MProfile saveUserProfile(MProfile newProfile, IUpdateProfileCallback callback);
}
