package org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model;

import org.mindinformatics.gwt.framework.component.resources.serialization.JsonGenericResource;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoAntibody extends JavaScriptObject {

	protected JsoAntibody() {}
	
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
	//  General (RDFS and Dublin Core Terms
	// ------------------------------------------------------------------------
	public final native String getLabel() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology::rdfLabel]; 
	}-*/;
	
	public final native String getVendor() /*-{ 
		return this["domeo:vendor"]; 
	}-*/;
	
	public final native String getCatalog() /*-{ 
		return this["domeo:catalog"]; 
	}-*/;
	
	public final native String getCloneId() /*-{ 
		return this["domeo:cloneId"]; 
	}-*/;
	
	public final native String getOrganism() /*-{ 
	return this["domeo:organism"]; 
}-*/;
	
	public final native JsonGenericResource getSource() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology::importedFrom];
	}-*/;	
}
