package org.mindinformatics.gwt.framework.component.resources.serialization;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * JavaScriptObject allowing access to a Generic Resource in JSON format.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonResource extends JavaScriptObject {

	protected JsonResource() {}
	
	public final native String getUrl() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId];
	}-*/;
	public final native String getLabel() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology::label];
	}-*/;
	public final native String getDescription() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms::description];
	}-*/;
	public final native JsonGenericResource getSource() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms::source];
	}-*/;		
}
