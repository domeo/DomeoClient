package org.mindinformatics.gwt.framework.component.users.src;

import java.util.Set;

import org.mindinformatics.gwt.framework.model.users.IUser;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public  abstract class AUserManager implements IUserManager {

	protected IUser user;
	protected Set<IUserGroup> groups;

	protected IApplication _application;
	private ICommandCompleted _callback;
	
	public AUserManager(IApplication application, ICommandCompleted callback) {
		_application = application;
		_callback = callback;
	}
	
	protected void setUser(IUser user) {
		_application.getInitializer().addCompletionMessage("User's info initialization... ");
		this.user = user;
	}
	
	public void stageCompleted() {
		_callback.notifyStageCompletion();
	}
	
	public IUser getUser() {
		return user;
	}

	public Set<IUserGroup> getUsersGroups() {
		return groups;
	}

	public void setUsersGroups(Set<IUserGroup> groups) {
		_application.getInitializer().addCompletionMessage("User's groups initialization... ");
		this.groups = groups;
	}
	
	public IUserGroup getUserGroup(String uuid) {
		for(IUserGroup group: groups) {
			if(group.getUuid().equals(uuid)) return group; 
		}
		return null;
	}
}
