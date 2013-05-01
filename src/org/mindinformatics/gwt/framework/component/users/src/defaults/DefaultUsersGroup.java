package org.mindinformatics.gwt.framework.component.users.src.defaults;

import java.util.Date;
import java.util.HashSet;

import org.mindinformatics.gwt.framework.component.users.model.MUser;
import org.mindinformatics.gwt.framework.component.users.model.MUserGroup;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class DefaultUsersGroup extends MUserGroup {

	private HashSet<MUser> _users = new HashSet<MUser>();
	
	public DefaultUsersGroup() {
		super("Guests", "http://www.commonsemantics.com/group/Guests", "Guests", "Guests Group", new Date(), "1", true, true);
	}
	
	public void addUser(MUser user) {
		_users.add(user);
	}
}
