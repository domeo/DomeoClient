package org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class JsoddiUsage extends JavaScriptObject {

	protected JsoddiUsage() {
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

	// ------------------------------------------------------------------------
	// DDI Claims Ontology
	// ------------------------------------------------------------------------

	// TODO: add all of the other types in the model using the DDI ontology for
	// namespaces



	// get drug
	public final native JsArray<JsoDDI_PKDDIUsage> getPKDDI() /*-{
		return this['sio:SIO_000628'];
	}-*/;
	
	
	// statement
	public final native String getStatement() /*-{
		return this['dikbD2R:statement'];
	}-*/;

	// modality
	public final native String getModality() /*-{
	    return this['dikbD2R:modality'];
	}-*/;
	
	// modality
	public final native String getComment() /*-{
	    return this['dikbD2R:comment'];
	}-*/;


}
