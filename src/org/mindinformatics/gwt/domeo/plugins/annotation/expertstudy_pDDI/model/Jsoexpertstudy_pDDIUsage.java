package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model;

import org.mindinformatics.gwt.framework.component.resources.serialization.JsonGenericResource;

import com.google.gson.JsonObject;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class Jsoexpertstudy_pDDIUsage extends JavaScriptObject {

	protected Jsoexpertstudy_pDDIUsage() {
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

	// drug 1
	public final native String getDrug1() /*-{
		return this['rxnorm:drug1'];
	}-*/;

	// drug 2
	public final native String getDrug2() /*-{
		return this['rxnorm:drug2'];
	}-*/;

	// role 1
	public final native String getRole1() /*-{
		return this['dikbD2R:object-drug-of-interaction'];
	}-*/;

	// get drug
	public final native JsArray getPKDDI() /*-{
		return this['sio:SIO_000132'];
	}-*/;
	
	
	// statement
	public final native String getStatement() /*-{
		return this['dikbD2R:statement'];
	}-*/;

	// modality
	public final native String getModality() /*-{
		return this['dikbD2R:modality'];
	}-*/;

}
