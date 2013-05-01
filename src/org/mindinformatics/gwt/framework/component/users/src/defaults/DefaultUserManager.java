package org.mindinformatics.gwt.framework.component.users.src.defaults;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.framework.component.users.src.AUserManager;
import org.mindinformatics.gwt.framework.component.users.src.UsersFactory;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class DefaultUserManager extends AUserManager {
	
	public DefaultUserManager(IApplication application, ICommandCompleted callback) {
		super(application, callback);
	}
	
	@Override
	public void retrieveUser(String username) {
		user = new DefaultUser();
		setUser(user);
		//stageCompleted();
	}
	
	@Override
	public void retrieveUserGroups(String username) {
		UsersFactory usersFactory = new UsersFactory(); 
		Set<IUserGroup> groups = new HashSet<IUserGroup>();
		if(username.equals("paolo.ciccarese@gmail.com")) {
			groups.add(
				usersFactory.createGroup("mind", "http://www.commonsemantics.com/group/mind", 
					"MIND", "MIND Informatics", new Date(), "4", true, true)
			);
			groups.add(
				usersFactory.createGroup("nif", "http://www.commonsemantics.com/group/nif", 
					"NIF", "Neruoscience Information Framework", new Date(), "1", true, true)
			);
		} else {
			groups.add(new DefaultUsersGroup());
		}
		setUsersGroups(groups);
		stageCompleted();
	}
}
