package org.mindinformatics.gwt.framework.component.resources.serialization;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * JavaScriptObject allowing access to a Generic Resource in JSON format.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonGenericResource extends JavaScriptObject {

	protected JsonGenericResource() {}
	
	public final native String getUrl() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId];
	}-*/;
	public final native String getLabel() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology::rdfLabel];
	}-*/;
}
