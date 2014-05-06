package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoDDI_DrugEntityUsage extends JavaScriptObject {
	protected JsoDDI_DrugEntityUsage() {
	}

	// ------------------------------------------------------------------------
	// Identity
	// ------------------------------------------------------------------------
	public final native String getId() /*-{
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId];
	}-*/;

	// ------------------------------------------------------------------------
	// General (RDFS and Dublin Core Terms)
	// ------------------------------------------------------------------------
	public final native String getLabel() /*-{
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology::label];
	}-*/;

	public final native String getDescription() /*-{
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms::description];
	}-*/;

	// drug has role
	public final native String getRole() /*-{
		return this['sio:SIO_000228'];
	}-*/;

	// drug has type
	public final native String getType() /*-{
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType];
	}-*/;
}
