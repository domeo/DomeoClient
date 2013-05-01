package org.mindinformatics.gwt.framework.component.users.model;

import java.util.Date;

import org.mindinformatics.gwt.framework.model.users.IUserGroup;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.i18n.client.DateTimeFormat;

public class JsoUserGroup extends JavaScriptObject implements IUserGroup {

	protected JsoUserGroup() {}
	
	public final native String getUri() /*-{ return this.uri; }-*/;
	public final native String getUuid() /*-{ return this.uuid; }-*/;
	public final native String getName() /*-{ return this.name; }-*/;
	public final native String getDescription() /*-{ return this.description; }-*/;
	public final native String getGroupLink() /*-{ return this.grouplink; }-*/;
	
	public final native String getCreatedOnAsString() /*-{ return this.createdOn; }-*/;
	public final Date getCreatedOn() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy.MM.dd HH:mm:ss Z");
		if(getCreatedOnAsString()!=null)
			return fmt.parse(getCreatedOnAsString());
		else return null;
	}
	
	public final native String getMemberSinceAsString() /*-{ return this.memberSince; }-*/;
	public final Date getMemberSince() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy.MM.dd HH:mm:ss Z");
		return fmt.parse(getMemberSinceAsString());
	}
	
	// Roles map
	public final native JsArray<JsoUserRole> getRoles() /*-{
	    return this.roles || [];
	}-*/;
	
	public final boolean isReadPermission() {
		return Boolean.parseBoolean(isReadPermissionAsString());
	}
	public final native String isReadPermissionAsString() /*-{ return this.permissionread; }-*/;
	
	public final boolean isWritePermission() {
		return Boolean.parseBoolean(isWritePermissionAsString());
	}
	public final native String isWritePermissionAsString() /*-{ return this.permissionwrite; }-*/;
}
