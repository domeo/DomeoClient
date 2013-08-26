package org.mindinformatics.gwt.domeo.plugins.annotation.spls.model;

import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class MSPLPharmgxUsage {

	private Long localId;			// sent to the server for matching saved items
	private String individualUri;		// Individual Uniform Resource Identifier
	
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
	private String comment;
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
    // The MPharmgx instance is a "normalized" instance that holds the model values as strings
	MPharmgx pharmgx = new MPharmgx("","",null);
	
	public MPharmgx getPharmgx() {
		return pharmgx;
	}
	public void setPharmgx(MPharmgx pharmgx) {
		this.pharmgx = pharmgx;
	}
    
	// These are the linked resources associated with the pharmgx annotation
	MLinkedResource pkImpactResource, pdImpactResource, doseRecResource, drugRecResource, monitRecResource, testRecResource;
			
	public MLinkedResource getPkImpact() {
		return pkImpactResource;
	}
	public void setPkImpact(MLinkedResource pkImpact) {
		this.pkImpactResource = pkImpact;
		this.pharmgx.setPkImpact(pkImpact.getLabel());
	}

	public MLinkedResource getPdImpact() {
		return this.pdImpactResource;
	}
	public void setPdImpact(MLinkedResource pdImpact) {
		this.pdImpactResource = pdImpact;
		this.pharmgx.setPdImpact(pdImpact.getLabel());
	}

	public MLinkedResource getDoseRec() {
		return this.doseRecResource;
	}
	public void setDoseRec(MLinkedResource doseRec) {
		this.doseRecResource = doseRec;
		this.pharmgx.setDoseRec(doseRec.getLabel());
	}

	public MLinkedResource getDrugRec() {
		return this.drugRecResource;
	}
	public void setDrugRec(MLinkedResource drugRec) {
		this.drugRecResource = drugRec;
		this.pharmgx.setDrugRec(drugRec.getLabel());
	}

	public MLinkedResource getMonitRec() {
		return this.monitRecResource;
	}
	public void setMonitRec(MLinkedResource monitRec) {
		this.monitRecResource = monitRec;
		this.pharmgx.setMonitRec(monitRec.getLabel());
	}

	public MLinkedResource getTestRec() {
		return this.testRecResource;
	}
	public void setTestRec(MLinkedResource testRec) {
		this.testRecResource = testRec;
		this.pharmgx.setTestRec(testRec.getLabel());
	}
}

