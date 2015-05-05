package org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class JsoDDI_PKDDIUsage extends JavaScriptObject{
	protected JsoDDI_PKDDIUsage() {
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

	
	// drug participant in PK_DDI
	public final native JsArray<JsoDDI_DrugEntityUsage> getDrugs() /*-{
		return this['sio:SIO_000132'];
	}-*/;
	
	// number of participants
	public final native String getNumOfParticipants() /*-{
	    return this['dikbD2R:numOfParticipants'];
	}-*/;
	
	// increase AUC
	public final native String getIncreaseAUC() /*-{
	    return this['dikbD2R:auc'];
	}-*/;
	
	
}