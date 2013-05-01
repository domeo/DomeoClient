package org.mindinformatics.gwt.domeo.plugins.annotation.curation.model;

import com.google.gwt.core.client.JavaScriptObject;

public class JsCurationToken extends JavaScriptObject {

	protected JsCurationToken() {}
	
	public final native String getValue() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology::value]; 
	}-*/;
	public final native String getDescription() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms::description]; 
	}-*/;
}
