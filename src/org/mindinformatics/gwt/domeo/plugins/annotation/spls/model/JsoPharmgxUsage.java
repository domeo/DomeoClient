package org.mindinformatics.gwt.domeo.plugins.annotation.spls.model;

import org.mindinformatics.gwt.framework.component.resources.serialization.JsonGenericResource;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class JsoPharmgxUsage extends JavaScriptObject {

	protected JsoPharmgxUsage() {
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
	// SPL Claims Ontology
	// ------------------------------------------------------------------------

	// TODO: add all of the other types in the model using the SPLs ontology for
	// namespaces
	// pharmacokinetic impact
	public final native String getPKImpact() /*-{
		return this['poc:PharmacokineticImpact'];
	}-*/;

	// pharmacodynamic impact
	public final native String getPDImpact() /*-{
		return this['poc:PharmacodynamicImpact'];
	}-*/;

	// drug selection recommendation
	public final native String getDrugRec() /*-{
		return this['poc:DrugSelectionRecommendation'];
	}-*/;

	// dose selection recommendation
	public final native String getDoseRec() /*-{
		return this['poc:DoseSelectionRecommendation'];
	}-*/;

	// monitoring recommendation
	public final native String getMonitRec() /*-{
		return this['poc:MonitoringRecommendation'];
	}-*/;

	// test recommendation
	public final native String getTestRec() /*-{
		return this['poc:TestRecommendation'];
	}-*/;

	// common variant
	public final native String getVariant() /*-{
		return this['poc:Variant'];
	}-*/;

	// common test
	public final native String getTest() /*-{
		return this['poc:TestResult'];
	}-*/;

	// biomarkers of interest
	public final native String getBiomarkers() /*-{
		return this['dailymed:pharmgxBiomarker'];
	}-*/;

	// drug of interest
	public final native String getDrug() /*-{
		return this['dailymed:pharmgxDrug'];
	}-*/;

	// product Label Section
	public final native String getProductLabelSection() /*-{
		return this['poc:productLabelSection'];
	}-*/;

	// Concepts that the Statement Refers To: Active ingredient &
	// Concomitant medication concern & Variant Frequency
	// variant frequency
	public final native String getVariantFrequency() /*-{
		return this['poc:VariantFrequency'];
	}-*/;

	// medication concern
	public final native String getMedicatConcern() /*-{
		return this['dailymed:ConcomitantMedicationOfConcern'];
	}-*/;

	// active ingredient
	public final native String getActiveIngredient() /*-{
		return this['dailymed:ActiveMoiety'];
	}-*/;

	// medical condition
	public final native String getMedicalCondition() /*-{
		return this['poc:MedicalCondition'];
	}-*/;

	// alleles
	public final native String getAlleles() /*-{
		return this['ncit:Alleles'];
	}-*/;

	// comment
	public final native String getComment() /*-{
		return this['poc:Comment'];
	}-*/;

}
