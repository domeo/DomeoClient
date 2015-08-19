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
	
	// object formulation
	public final native String getObjectFormulation() /*-{
	    return this['dikbD2R:objectFormulation'];
	}-*/;
	
	// object duration
	public final native String getObjectDuration() /*-{
	    return this['dikbD2R:objectDuration'];
	}-*/;
	
	// object regimen
	public final native String getObjectRegimen() /*-{
	    return this['dikbD2R:objectRegimens'];
	}-*/;
	
	// precipt formulation
	public final native String getPreciptFormulation() /*-{
	    return this['dikbD2R:preciptFormulation'];
	}-*/;
	
	// precipt duration
	public final native String getPreciptDuration() /*-{
	    return this['dikbD2R:preciptDuration'];
	}-*/;
	
	// precipt regimen
	public final native String getPreciptRegimen() /*-{
	    return this['dikbD2R:preciptRegimens'];
	}-*/;
	
	// increase AUC
	public final native String getIncreaseAUC() /*-{
	    return this['dikbD2R:auc'];
	}-*/;
	
	// AUC direction
	public final native String getAucDirection() /*-{
	    return this['dikbD2R:aucDirection'];
	}-*/;
	
	// AUC type
	public final native String getAucType() /*-{
	    return this['dikbD2R:aucType'];
	}-*/;
	
	// cl
	public final native String getCl() /*-{
	    return this['dikbD2R:cl'];
	}-*/;
	
	// cl direction
	public final native String getClDirection() /*-{
	    return this['dikbD2R:clDirection'];
	}-*/;
	
	// cl type
	public final native String getClType() /*-{
	    return this['dikbD2R:clType'];
	}-*/;
	
	
	// cmax
	public final native String getCmax() /*-{
	    return this['dikbD2R:cmax'];
	}-*/;
	
	// cmax direction
	public final native String getCmaxDirection() /*-{
	    return this['dikbD2R:cmaxDirection'];
	}-*/;
	
	// cmax type
	public final native String getCmaxType() /*-{
	    return this['dikbD2R:cmaxType'];
	}-*/;
	
	// t1/2
	public final native String getT12() /*-{
	    return this['dikbD2R:t12'];
	}-*/;
	
	// t12 direction
	public final native String getT12Direction() /*-{
	    return this['dikbD2R:t12Direction'];
	}-*/;
	
	// t12 type
	public final native String getT12Type() /*-{
	    return this['dikbD2R:t12Type'];
	}-*/;
	
}
