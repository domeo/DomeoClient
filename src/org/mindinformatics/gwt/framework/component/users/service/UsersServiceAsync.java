package org.mindinformatics.gwt.framework.component.users.service;

import java.util.Set;

import org.mindinformatics.gwt.framework.model.users.IUser;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>UsersService</code>.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface UsersServiceAsync {
	void getUserInfo(String username, AsyncCallback<IUser> callback)
			throws IllegalArgumentException;
	void getUserGroups(String username, AsyncCallback<Set<IUserGroup> > callback)
		throws IllegalArgumentException;
}

