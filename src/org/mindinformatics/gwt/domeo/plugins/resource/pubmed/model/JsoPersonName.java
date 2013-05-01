package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoPersonName extends JavaScriptObject implements IPersonName {

	protected JsoPersonName() {}
	
	public final native String getFirstName() /*-{ return this.firstname; }-*/;
	public final native String getMiddleName() /*-{ return this.middlename; }-*/;
	public final native String getLastName() /*-{ return this.lastname; }-*/;
	public final native String getFullName() /*-{ return this.fullname; }-*/;
}
