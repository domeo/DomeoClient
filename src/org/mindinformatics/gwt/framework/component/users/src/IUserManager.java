package org.mindinformatics.gwt.framework.component.users.src;

import java.util.Set;

import org.mindinformatics.gwt.framework.model.users.IUser;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IUserManager {

	public IUser getUser();
	public Set<IUserGroup> getUsersGroups();
	public IUserGroup getUserGroup(String uuid);
	public void stageCompleted();
	public void retrieveUser(String username);
	public void retrieveUserGroups(String username);
	
}
