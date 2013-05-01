package org.mindinformatics.gwt.framework.component.profiles.service;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.profiles.model.MProfile;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>ProfilesService</code>.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ProfilesServiceAsync {
	void getUserAvailableProfiles(String email, AsyncCallback<ArrayList<MProfile>> callback) 
			throws IllegalArgumentException;
	void getUserCurrentProfile(String email, AsyncCallback<MProfile> callback) 
		throws IllegalArgumentException;
}

