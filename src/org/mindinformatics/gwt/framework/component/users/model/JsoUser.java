package org.mindinformatics.gwt.framework.component.users.model;

import java.util.Date;

import org.mindinformatics.gwt.framework.model.users.IUser;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsoUser extends JavaScriptObject implements IUser {

	protected JsoUser() {}
	
	public final native String getUri() /*-{ return this.uri; }-*/;
	public final native String getUserName() /*-{ return this.username; }-*/;
	public final native String getScreenName() /*-{ return this.screenname; }-*/;
	
	public final native String getUserSinceAsString() /*-{ return this.userSince; }-*/;
	public final Date getUserSince() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy.MM.dd HH:mm:ss Z");
		return fmt.parse(getUserSinceAsString());
	}
}
