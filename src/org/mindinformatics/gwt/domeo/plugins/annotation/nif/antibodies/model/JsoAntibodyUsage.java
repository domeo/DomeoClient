package org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model;

import org.mindinformatics.gwt.framework.component.resources.serialization.JsonGenericResource;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsoAntibodyUsage extends JavaScriptObject {
	
	protected JsoAntibodyUsage() {}
	
	// ------------------------------------------------------------------------
	//  Identity
	// ------------------------------------------------------------------------
	public final native String getId() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  Comment
	// ------------------------------------------------------------------------
	public final native String getComment() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms::description]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  Subject
	// ------------------------------------------------------------------------
	public final native JsonGenericResource getSubject() /*-{ 
		return this['domeo:model']; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  Protocols
	// ------------------------------------------------------------------------
	public final native JsArray<JsonGenericResource> getProtocols() /*-{ 
		return this['domeo:protocol']; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  Antibody
	// ------------------------------------------------------------------------
	public final native JsArray<JsoAntibody> getAntibody() /*-{ 
		return this['domeo:antibody']; 
	}-*/;
}
