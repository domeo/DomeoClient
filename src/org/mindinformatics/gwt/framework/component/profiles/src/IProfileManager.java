package org.mindinformatics.gwt.framework.component.profiles.src;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.profiles.model.MProfile;
import org.mindinformatics.gwt.framework.component.profiles.src.testing.IUpdateProfileCallback;

/**
 * Profiles are collection of parametrizations that define how the application
 * will behave for a user. Profiles allow to switch features on and off.
 * This interface defines the contract for the implementation of any manager
 * of profiles. 
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IProfileManager {

	/**
	 * Returns all the profiles available to the user.getUserProfiles
	 * @return List of user available profiles
	 */
	public ArrayList<MProfile> getUserProfiles();
	/**
	 * Sets the list of all the profiles available to the user.
	 * @param profiles List of user available profiles
	 */
	public void setProfiles(ArrayList<MProfile> profiles);
	/**
	 * Get the current profile for the user.
	 * @return User's current profile
	 */
	public MProfile getUserCurrentProfile();
	/**
	 * Sets the current profile for the user.
	 * @param profile	User's current profile
	 */
	public void setCurrentProfile(MProfile profile);
	/**
	 * Saves a profile as current for the user.
	 * @param profile Profile to be saved as current
	 * @param callback The listener to be called to complete the action
	 */
	public void saveCurrentProfile(MProfile profile, IUpdateProfileCallback callback);
	/**
	 * Saves a newly defined profile (normally resulted from an edit of an existing one)
	 * @param profile	The newly created profile
	 * @param callback	The listener to be called to complete the action
	 * @return
	 */
	public MProfile saveUserProfile(MProfile profile, IUpdateProfileCallback callback);
	
	/**
	 * Retrieves all the existing profiles.
	 */
	public void retrieveAndCacheAllProfiles();
	/**
	 * Retrieves all the user available profiles.
	 */
	public void retrieveAndCacheUserAvailableProfiles();
	/**
	 * Retrieves the user current profile.
	 */
	public void retrieveAndCacheUserCurrentProfile();
}
