package org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.json;

import com.google.gwt.core.client.JavaScriptObject;

public class JsonIsResourceStarred  extends JavaScriptObject {

	protected JsonIsResourceStarred() {}
	
	public final native String getUrl() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId];
	}-*/;
	public final native String getLabel() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology::rdfLabel];
	}-*/;
	public final native String isStarred() /*-{ 
		return this["starred"];
	}-*/;	
}