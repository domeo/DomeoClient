package org.mindinformatics.gwt.framework.component.profiles.model;

import java.util.Date;


import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.i18n.client.DateTimeFormat;

public class JsoProfile extends JavaScriptObject implements IProfile {

	protected JsoProfile() {}
	
	public final native String getUuid() /*-{ return this.uuid; }-*/;
	public final native String getName() /*-{ return this.name; }-*/;
	public final native String getDescription() /*-{ return this.description; }-*/;
	
	// Creation date
	public final native String getCreatedOnAsString() /*-{ return this.createdon; }-*/;
	public final Date getCreatedOn() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy HH:mm:ss Z");
		return fmt.parse(getCreatedOnAsString());
	}
	
	// Creator
	public final native JsArray<JavaScriptObject> getCreatedBy() /*-{
	    return this.createdby || [];
	}-*/;
	
	// Plugins map
	public final native JsArray<JsoProfileEntry> getStatusPlugins() /*-{
	    return this.statusplugins || [];
	}-*/;
}
