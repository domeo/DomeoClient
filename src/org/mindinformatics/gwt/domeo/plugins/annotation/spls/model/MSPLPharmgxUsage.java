package org.mindinformatics.gwt.domeo.plugins.annotation.spls.model;

import java.util.Set;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class MSPLPharmgxUsage {

	private Long localId; // sent to the server for matching saved items
	private String individualUri; // Individual Uniform Resource Identifier
	private String allelesbody;
	private String medconditbody;
	private String comment;
	private String othervariant;
	private String othertest;

	public Long getLocalId() {
		return localId;
	}

	public void setLocalId(Long localId) {
		this.localId = localId;
	}

	public String getIndividualUri() {
		return individualUri;
	}

	public void setIndividualUri(String individualUri) {
		this.individualUri = individualUri;
	}

	// tags in the pharmgx model are the object of SIO predicates

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	// The MPharmgx instance is a "normalized" instance that holds the model
	// values as strings
	MPharmgx pharmgx = new MPharmgx("", "", null);

	public MPharmgx getPharmgx() {
		return pharmgx;
	}

	public void setPharmgx(MPharmgx pharmgx) {
		this.pharmgx = pharmgx;
	}

	// These are the linked resources associated with the pharmgx annotation
	// MLinkedResource pkImpactResource, pdImpactResource, doseRecResource,
	// drugRecResource, monitRecResource, testRecResource;

	public MLinkedResource getVariant() {
		return pharmgx.getVariant();
	}

	public void setVariant(MLinkedResource variant) {
		pharmgx.setVariant(variant);
	}

	public MLinkedResource getTest() {
		return pharmgx.getTest();
	}

	public void setTest(MLinkedResource test) {
		pharmgx.setTest(test);
	}

	public MLinkedResource getDrugOfInterest() {
		return pharmgx.getDrugOfInterest();
	}

	public void setDrugOfInterest(MLinkedResource drugofinterest) {
		pharmgx.setDrugOfInterest(drugofinterest);
	}

	public MLinkedResource getProductLabelSelection() {
		return pharmgx.getProductLabelSelection();
	}

	public void setProductLabelSelection(MLinkedResource productlabelselection) {
		pharmgx.setProductLabelSelection(productlabelselection);
	}

	public MLinkedResource getBiomarkers() {
		return pharmgx.getBiomarkers();
	}

	public void setBiomarkers(MLinkedResource biomarker) {
		pharmgx.setBiomarkers(biomarker);
	}

	public MLinkedResource getPkImpact() {
		return pharmgx.getPkImpactResource();
	}

	public void setPkImpact(MLinkedResource pkImpact) {
		pharmgx.setPkImpact(pkImpact);
	}

	public MLinkedResource getPdImpact() {
		return pharmgx.getPdImpactResource();
	}

	public void setPdImpact(MLinkedResource pdImpact) {
		pharmgx.setPdImpact(pdImpact);
	}

	public MLinkedResource getDoseRec() {
		return pharmgx.getDoseRecResource();
	}

	public void setDoseRec(MLinkedResource doseRec) {
		pharmgx.setDoseRec(doseRec);
	}

	public MLinkedResource getDrugRec() {
		return pharmgx.getDrugRecResource();
	}

	public void setDrugRec(MLinkedResource drugRec) {
		pharmgx.setDrugRec(drugRec);
	}

	public MLinkedResource getMonitRec() {
		return pharmgx.getMonitRecResource();
	}

	public void setMonitRec(MLinkedResource monitRec) {
		pharmgx.setMonitRec(monitRec);
	}

	public MLinkedResource getTestRec() {
		return pharmgx.getTestRecResource();
	}

	public void setTestRec(MLinkedResource testRec) {
		pharmgx.setTestRec(testRec);
	}

	public Set<MLinkedResource> getStatements() {
		return pharmgx.getStatementsResource();
	}

	public void setStatements(Set<MLinkedResource> statements) {
		pharmgx.setStatementsResource(statements);
	}

	public String getAllelesbody() {
		return allelesbody;
	}

	public void setAllelesbody(String allelesbody) {
		this.allelesbody = allelesbody;
	}

	public String getMedconditbody() {
		return medconditbody;
	}

	public void setMedconditbody(String medconditbody) {
		this.medconditbody = medconditbody;
	}

	public String getOtherVariant() {
		return othervariant;
	}

	public void setOtherVariant(String othervariant) {
		this.othervariant = othervariant;
	}

	public String getOtherTest() {
		return othertest;
	}

	public void setOtherTest(String othertest) {
		this.othertest = othertest;
	}

}
