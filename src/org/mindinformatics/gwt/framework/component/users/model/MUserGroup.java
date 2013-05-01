package org.mindinformatics.gwt.framework.component.users.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mindinformatics.gwt.framework.model.users.IUserGroup;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MUserGroup implements Serializable, IsSerializable, IUserGroup {

	private String uri;
	private String uuid;
	private String name;
	private String description;
	private Date createdOn;
	private Date memberSince;
	private String groupLink;
	private boolean readPermission;
	private boolean writePermission;
	private List<MUserRole> roles = new ArrayList<MUserRole>();
	
	public MUserGroup() {}
	
	public MUserGroup(String uuid, String uri, String name, String description, Date createdOn, String groupLink,
			boolean readPermission, boolean writePermission) {
		this.uri = uri;
		this.uuid = uuid;
		this.name = name;
		this.description = description;
		this.createdOn = createdOn;
		this.groupLink = groupLink;
		this.readPermission = readPermission;
		this.writePermission = writePermission;
	}
	
    public String getUuid() { return uuid; }
	public void setUuid(String uuid) { this.uuid = uuid; }
	public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public Date getCreatedOn() { return createdOn; }
	public Date getMemberSince() { return memberSince; }
	public void setCreatedOn(Date registeredOn) { this.createdOn = registeredOn; }
	public String getGroupLink() { return groupLink; }
	public void setGroupLink(String groupLink) { this.groupLink = groupLink; }
	public List<MUserRole> getRoles() { return roles; }
	public void setRoles(List<MUserRole> roles) { this.roles = roles; }
	public boolean isReadPermission() { return readPermission; }
	public void setReadPermission(boolean readPermission) { this.readPermission = readPermission; }
	public boolean isWritePermission() { return writePermission; }
	public void setWritePermission(boolean writePermission) { this.writePermission = writePermission; }
}
