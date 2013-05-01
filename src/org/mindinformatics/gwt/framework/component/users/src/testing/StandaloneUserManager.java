package org.mindinformatics.gwt.framework.component.users.src.testing;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.framework.component.users.model.MUserGroup;
import org.mindinformatics.gwt.framework.component.users.src.AUserManager;
import org.mindinformatics.gwt.framework.component.users.src.UsersFactory;
import org.mindinformatics.gwt.framework.component.users.src.defaults.DefaultUser;
import org.mindinformatics.gwt.framework.component.users.src.defaults.DefaultUsersGroup;
import org.mindinformatics.gwt.framework.model.users.IUser;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;
import org.mindinformatics.gwt.framework.src.ApplicationUtils;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class StandaloneUserManager extends AUserManager {

	public StandaloneUserManager(IApplication application, ICommandCompleted callbackCompleted) {
		super(application, callbackCompleted);
	}
	
	@Override
	public void retrieveUser(String username) {

		IUser user;
		if(username.equals("paolo.ciccarese")) {
			UsersFactory usersFactory = new UsersFactory(); 
			user = usersFactory.createUser(username, "http://www.commonsemantics.com/account/paolociccarese", 
				"Paolo Ciccarese", ApplicationUtils.day.parse("05/20/2011 -0500"));
			setUser(user);
			stageCompleted();
		} else {
			user = new DefaultUser();
			setUser(user);
			stageCompleted();
		}
	}

	@Override
	public void retrieveUserGroups(String username) {
		UsersFactory usersFactory = new UsersFactory(); 
		Set<MUserGroup> groups = new HashSet<MUserGroup>();
		if(username.equals("paolo.ciccarese")) {
			groups.add(
				usersFactory.createGroup("mind", "http://www.commonsemantics.com/group/mind", 
						"MIND", "MIND Informatics", new Date(), "4", true, true)
			);
			groups.add(
				usersFactory.createGroup("nif", "http://www.commonsemantics.com/group/nif", 
						"NIF", "Neuroscience Information Framework", new Date(), "1", true, true)
			);
		} else {
			groups.add(new DefaultUsersGroup());
		}
		this.groups = new HashSet<IUserGroup>();
		this.groups.addAll(groups);
		stageCompleted();
	}
}
