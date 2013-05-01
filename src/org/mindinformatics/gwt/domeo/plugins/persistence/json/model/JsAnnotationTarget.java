package org.mindinformatics.gwt.domeo.plugins.persistence.json.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * JavaScriptObject that grants access to the SpecificResource
 * that points to the target (source) and can specify a selector.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsAnnotationTarget extends JavaScriptObject {

	protected JsAnnotationTarget() {}
	
	// ------------------------------------------------------------------------
	//  Identity
	// ------------------------------------------------------------------------
	public final native String getId() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
	public final native String getType() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  Annotation Ontology
	// ------------------------------------------------------------------------
	public final native String getSource() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::source]; 
	}-*/;
	public final native JavaScriptObject getSelector() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::selector]; 
	}-*/;
}
