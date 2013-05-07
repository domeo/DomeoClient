package org.mindinformatics.gwt.framework.component.profiles.model;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoProfileEntry extends JavaScriptObject {

	protected JsoProfileEntry() {}
	
	public final native String getName() /*-{ return this.name; }-*/;
	public final native String getStatus() /*-{ return this.status; }-*/;
}
