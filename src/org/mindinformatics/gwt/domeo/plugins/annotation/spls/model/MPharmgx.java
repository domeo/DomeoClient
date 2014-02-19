package org.mindinformatics.gwt.domeo.plugins.annotation.spls.model;

import java.util.Set;

import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTrustedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
public class MPharmgx extends MTrustedResource {

	// String pkImpact, pdImpact, doseRec, drugRec, monitRec, testRec;

	MLinkedResource pkImpactResource, pdImpactResource, doseRecResource,
			drugRecResource, monitRecResource, testRecResource, comment,
			Alleles, biomarkers, variant, test, medicalCondition, drugOfInterest;
	
	public MLinkedResource getVariant() {
		return variant;
	}

	public MLinkedResource getDrugOfInterest() {
		return drugOfInterest;
	}

	public void setDrugOfInterest(MLinkedResource drugOfInterest) {
		this.drugOfInterest = drugOfInterest;
	}

	public void setVariant(MLinkedResource variant) {
		this.variant = variant;
	}

	public MLinkedResource getMedicalCondition() {
		return medicalCondition;
	}

	public void setMedicalCondition(MLinkedResource medicalCondition) {
		this.medicalCondition = medicalCondition;
	}

	public MLinkedResource getTest() {
		return test;
	}

	public void setTest(MLinkedResource test) {
		this.test = test;
	}

	Set<MLinkedResource> statementsResource;

	public void setAlleles(MLinkedResource alleles) {
		Alleles = alleles;
	}

	public void setBiomarkers(MLinkedResource biomarkers) {
		this.biomarkers = biomarkers;
	}



	public MLinkedResource getComment() {
		return comment;
	}

	public MLinkedResource getAlleles() {
		return Alleles;
	}

	public MLinkedResource getBiomarkers() {
		return biomarkers;
	}

	public void setComment(MLinkedResource comment) {
		this.comment = comment;
	}

	public MPharmgx(String url, String label, MGenericResource source) {
		super(url, label, source);
	}

	public Set<MLinkedResource> getStatementsResource() {
		return statementsResource;
	}

	public MLinkedResource getPkImpactResource() {
		return pkImpactResource;
	}

	// public String getPkImpactLabel() {
	// return (pkImpactResource!=null) ? pkImpactResource.getLabel():"";
	// }
	public void setPkImpact(MLinkedResource pkImpact) {
		this.pkImpactResource = pkImpact;
	}

	public MLinkedResource getPdImpactResource() {
		return pdImpactResource;
	}

	// public String getPdImpactLabel() {
	// return (pdImpactResource!=null) ? pdImpactResource.getLabel():"";
	// }
	public void setPdImpact(MLinkedResource pdImpact) {
		this.pdImpactResource = pdImpact;
	}

	public void setStatementsResource(Set<MLinkedResource> statementsResource) {
		this.statementsResource = statementsResource;
	}

	public MLinkedResource getDoseRecResource() {
		return doseRecResource;
	}

	// public String getDoseRecLabel() {
	// return (doseRecResource!=null) ? doseRecResource.getLabel():"";
	// }
	public void setDoseRec(MLinkedResource doseRec) {
		this.doseRecResource = doseRec;
	}

	public MLinkedResource getDrugRecResource() {
		return drugRecResource;
	}

	// public String getDrugRecLabel() {
	// return (drugRecResource!=null) ? drugRecResource.getLabel():"";
	// }
	public void setDrugRec(MLinkedResource drugRec) {
		this.drugRecResource = drugRec;
	}

	public MLinkedResource getMonitRecResource() {
		return monitRecResource;
	}

	// public String getMonitRecLabel() {
	// return (monitRecResource!=null) ? monitRecResource.getLabel():"";
	// }
	public void setMonitRec(MLinkedResource monitRec) {
		this.monitRecResource = monitRec;
	}

	public MLinkedResource getTestRecResource() {
		return testRecResource;
	}

	// public String getTestRecLabel() {
	// return (testRecResource!=null) ? testRecResource.getLabel():"";
	// }
	public void setTestRec(MLinkedResource testRec) {
		this.testRecResource = testRec;
	}
}
