package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model;

import com.google.gwt.core.client.JavaScriptObject;

public class JsMpAssertion extends JavaScriptObject {

	protected JsMpAssertion() {}
	
	public final native String getId() /*-{ return this['@id'];  }-*/;
	public final native String getType() /*-{ return this['@type']; }-*/;

	public final native String getStatement() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpStatement]; 
	}-*/;
	
	public final native boolean isSupportedByString() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpSupportedBy].constructor === String;
	}-*/;
	public final native String getSupportedByAsString() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpSupportedBy]; 
	}-*/;
	
	public final native boolean isSupportedByObject() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpSupportedBy].constructor === Object;
	}-*/;
	public final native JsMpAssertion getSupportedByAsObject() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology::mpSupportedBy]; 
	}-*/;

}
