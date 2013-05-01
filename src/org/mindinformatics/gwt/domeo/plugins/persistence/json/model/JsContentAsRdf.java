package org.mindinformatics.gwt.domeo.plugins.persistence.json.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 *
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsContentAsRdf extends JavaScriptObject {

	protected JsContentAsRdf() {}
	
	// ------------------------------------------------------------------------
	//  Identity
	// ------------------------------------------------------------------------
	public final native String getId() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
	public final native String getType() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms::format]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  Content As RDF
	// ------------------------------------------------------------------------	
	public final native String getChars() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.ICntOntology::chars]; 
	}-*/;
	
	public final native String getFormat() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms::format]; 
	}-*/;
}
