package org.mindinformatics.gwt.domeo.plugins.annotation.spls.model;

import org.mindinformatics.gwt.framework.component.resources.serialization.JsonGenericResource;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class JsoPharmgxUsage extends JavaScriptObject {
	
	protected JsoPharmgxUsage() {}
	
	// ------------------------------------------------------------------------
	//  Identity
	// ------------------------------------------------------------------------
	public final native String getId() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  General (RDFS and Dublin Core Terms)
	// ------------------------------------------------------------------------
	public final native String getLabel() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology::label]; 
	}-*/;

	public final native String getDescription() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms::description]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  SPL Claims Ontology
	// ------------------------------------------------------------------------
		
	//TODO: add all of the other types in the model using the SPLs ontology for namespaces
	// pharmacokinetic impact
	public final native String getPKImpact() /*-{ 
		return this['poc:PharmacokineticImpact']; 
	}-*/;
	 
}
