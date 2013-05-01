package org.mindinformatics.gwt.framework.component.users.model;

import java.io.Serializable;
import java.util.Date;

import org.mindinformatics.gwt.framework.model.users.IUser;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class MUser implements IUser, Serializable, IsSerializable {

	private static final long serialVersionUID = 1L;
	
	private String uri;
	private String username;
	private String screenname;
	private Date userSince;

	public MUser() {}
	
	public MUser(String username, String uri, String screenname, Date userSince) {
		super();
		this.uri = uri;
		this.username = username;
		this.screenname = screenname;
		this.userSince = userSince;
	}
	
	//  Getters and setters
	// ---------------------
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUserName() {
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public String getScreenName() {
		return screenname;
	}

	public void setScreenName(String screenname) {
		this.screenname = screenname;
	}

	public Date getUserSince() {
		return userSince;
	}

	public void setUserSince(Date userSince) {
		this.userSince = userSince;
	}

	public String toString() {
		return "username: " + username +  ", uri:" + uri + ", username:" + username + ", screenname:" + screenname;
	}
}
