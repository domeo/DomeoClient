package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model;

import com.google.gwt.core.client.JavaScriptObject;

public class JsMpAssertion extends JavaScriptObject {

	protected JsMpAssertion() {}
	
	public final native String getId() /*-{ return this['@id'];  }-*/;
	public final native String getType() /*-{ return this['@type']; }-*/;

	public final native String getStatement() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpStatement]; 
	}-*/;
}
