package org.mindinformatics.gwt.framework.component.users.service;

import java.util.Set;

import org.mindinformatics.gwt.framework.model.users.IUser;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@RemoteServiceRelativePath("users")
public interface UsersService extends RemoteService {
	IUser getUserInfo(String username) throws IllegalArgumentException;
	Set<IUserGroup> getUserGroups(String username) throws IllegalArgumentException;
}
