package org.mindinformatics.gwt.framework.model.users;

import java.util.Date;

public interface IUserGroup {

	public String getUri();
	public String getUuid();
	public String getName();
	public String getDescription();
	public Date getCreatedOn();
	public String getGroupLink();
	public Date getMemberSince();
	public boolean isReadPermission();
	public boolean isWritePermission();
}
