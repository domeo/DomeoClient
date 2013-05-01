package org.mindinformatics.gwt.framework.component.users.src;

import java.util.Date;

import org.mindinformatics.gwt.framework.component.users.model.MUser;
import org.mindinformatics.gwt.framework.component.users.model.MUserGroup;
import org.mindinformatics.gwt.framework.model.users.IUser;

import com.google.gwt.user.client.Window;

public class UsersFactory {

	public MUserGroup createGroup(String uuid, String uri, String name, String description, Date registeredOn, 
			String groupLink, boolean readPermission, boolean writePermission) {
		MUserGroup g = new MUserGroup();
		g.setUuid(uuid);
		g.setUri(uri);
		g.setName(name);
		g.setDescription(description);
		g.setCreatedOn(registeredOn);
		g.setGroupLink(groupLink);
		g.setReadPermission(readPermission);
		g.setWritePermission(writePermission);
		return g;
	}
	
	public IUser createUser(String username, String uri, String fullname, Date userSince) {
		MUser u = new MUser();
		u.setUri(uri);
		u.setUserName(username);
		u.setScreenName(fullname);
		u.setUserSince(userSince);
		return u;
	}
}
