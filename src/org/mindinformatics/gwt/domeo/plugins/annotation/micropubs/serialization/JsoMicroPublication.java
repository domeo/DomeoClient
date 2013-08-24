package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.serialization;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsoMicroPublication extends JavaScriptObject {
	
	protected JsoMicroPublication() {}
	
	// ------------------------------------------------------------------------
	//  Identity
	// ------------------------------------------------------------------------
	public final native String getId() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  Protocols
	// ------------------------------------------------------------------------
	public final native JsoMpStatement getArgues() /*-{ 
		return this['mp:argues']; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  Antibody
	// ------------------------------------------------------------------------
	//public final native JsArray<JsoAntibody> getAntibody() /*-{ 
	//	return this['domeo:antibody']; 
	//}-*/;
}
