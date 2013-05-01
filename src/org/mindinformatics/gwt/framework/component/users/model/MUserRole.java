package org.mindinformatics.gwt.framework.component.users.model;

import org.mindinformatics.gwt.framework.model.users.IUserRole;

public class MUserRole implements IUserRole {

	private String uuid;
	private String name;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
