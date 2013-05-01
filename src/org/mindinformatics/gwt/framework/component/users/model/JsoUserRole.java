package org.mindinformatics.gwt.framework.component.users.model;

import org.mindinformatics.gwt.framework.model.users.IUserRole;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoUserRole extends JavaScriptObject implements IUserRole {

	protected JsoUserRole() {};

	public final native String getUuid() /*-{ return this.uuid; }-*/;
	public final native String getName() /*-{ return this.name; }-*/;
}
