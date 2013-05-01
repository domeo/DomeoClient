package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.model;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoBioPortalEntry extends JavaScriptObject {

	protected JsoBioPortalEntry() {}
	
	public final native String getUri() /*-{ return this.termUri; }-*/;
	public final native String getLabel() /*-{ return this.termLabel; }-*/;
	public final native String getDescription() /*-{ return this.description; }-*/;
	public final native String getSourceUri() /*-{ return this.sourceUri; }-*/;
	public final native String getSourceLabel() /*-{ return this.sourceLabel; }-*/;
	//public final native JsArray<JsoPerson> getAuthorNames() /*-{ return this.authorNames; }-*/;
}
