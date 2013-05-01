package org.mindinformatics.gwt.framework.component.profiles.service;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.profiles.model.MProfile;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@RemoteServiceRelativePath("profiles")
public interface ProfilesService extends RemoteService {
	ArrayList<MProfile> getUserAvailableProfiles(String email) throws IllegalArgumentException;
	MProfile getUserCurrentProfile(String email) throws IllegalArgumentException;
}
