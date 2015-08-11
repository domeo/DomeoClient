package org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model;

import java.util.Set;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class MddiUsage {

	private Long localId; // sent to the server for matching saved items
	private String individualUri; // Individual Uniform Resource Identifier

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

	// The Mddi instance is a "normalized" instance that holds the
	// model values as strings
	Mddi MpDDI = new Mddi("", "", null);

	public Mddi getMpDDI() {
		return MpDDI;
	}

	public void setMpDDI(Mddi MpDDI) {
		this.MpDDI = MpDDI;
	}

	// tags in the ddi model are the object of SIO predicates
	//private String comment;

	public String getComment() {
		return MpDDI.getComment();
	}

	public void setComment(String comment) {
		MpDDI.setComment(comment);
	}

	// drug 1 and drug 2

	public MLinkedResource getDrug1() {
		return MpDDI.getDrug1();
	}

	public void setDrug1(MLinkedResource drug1) {
		MpDDI.setDrug1(drug1);
	}

	public MLinkedResource getDrug2() {
		return MpDDI.getDrug2();
	}

	public void setDrug2(MLinkedResource drug2) {
		MpDDI.setDrug2(drug2);
	}

	// DDI role of drug1 as role1 and role of drug2 as role2

	public MLinkedResource getRole1() {
		return MpDDI.getRole1();
	}

	public void setRole1(MLinkedResource role1) {
		MpDDI.setRole1(role1);
	}

	public MLinkedResource getRole2() {
		return MpDDI.getRole2();
	}

	public void setRole2(MLinkedResource role2) {
		MpDDI.setRole2(role2);
	}

	// DDI type of drug
	public MLinkedResource getType1() {
		return MpDDI.getType1();
	}

	public void setType1(MLinkedResource type1) {
		MpDDI.setType1(type1);
	}

	public MLinkedResource getType2() {
		return MpDDI.getType2();
	}

	public void setType2(MLinkedResource type2) {
		MpDDI.setType2(type2);
	}

	// DDI statement
	public MLinkedResource getStatement() {
		return MpDDI.getStatement();
	}

	public void setStatement(MLinkedResource statement) {
		MpDDI.setStatement(statement);
	}

	// DDI modality
	public MLinkedResource getModality() {
		return MpDDI.getModality();
	}

	public void setModality(MLinkedResource modality) {
		MpDDI.setModality(modality);
	}

	// increase Auc fields

	public MLinkedResource getNumOfparcipitants() {
		return MpDDI.getNumOfparcipitants();
	}

	public void setNumOfparcipitants(MLinkedResource numOfparcipitants) {
		MpDDI.setNumOfparcipitants(numOfparcipitants);
	}

	public MLinkedResource getIncreaseAuc() {
		return MpDDI.getIncreaseAuc();
	}

	public void setIncreaseAuc(MLinkedResource increaseAuc) {
		MpDDI.setIncreaseAuc(increaseAuc);
	}

	public MLinkedResource getEvidenceType() {
		return MpDDI.getEvidenceType();
	}

	public void setEvidenceType(MLinkedResource evidenceType) {
		MpDDI.setEvidenceType(evidenceType);
	}

	public MLinkedResource getObjectDose() {
		return MpDDI.getObjectDose();
	}

	public void setObjectDose(MLinkedResource objectDose) {
		MpDDI.setObjectDose(objectDose);
	}

	public MLinkedResource getPreciptDose() {
		return MpDDI.getPreciptDose();
	}

	public void setPreciptDose(MLinkedResource preciptDose) {
		MpDDI.setPreciptDose(preciptDose);
	}

	public MLinkedResource getAssertType() {
		return MpDDI.getAssertType();
	}

	public void setAssertType(MLinkedResource assertType) {
		MpDDI.setAssertType(assertType);
	}
	
	public MLinkedResource getT12() {
		return MpDDI.getT12();
	}

	public void setT12(MLinkedResource t12) {
		MpDDI.setT12(t12);
	}

	public MLinkedResource getCmax() {
		return MpDDI.getCmax();
	}

	public void setCmax(MLinkedResource cmax) {
		MpDDI.setCmax(cmax);
	}

	public MLinkedResource getObjectRegimen() {
		return MpDDI.getObjectRegimen();
	}

	public void setObjectRegimen(MLinkedResource objectRegimen) {
		MpDDI.setObjectRegimen(objectRegimen);
	}

	public MLinkedResource getPreciptRegimen() {
		return MpDDI.getPreciptRegimen();
	}

	public void setPreciptRegimen(MLinkedResource preciptRegimen) {
		MpDDI.setPreciptRegimen(preciptRegimen);
	}
	
	public MLinkedResource getAucDirection() {
		return MpDDI.getAucDirection();
	}

	public void setAucDirection(MLinkedResource aucDirection) {
		MpDDI.setAucDirection(aucDirection);
	}

	public MLinkedResource getClDirection() {
		return MpDDI.getClDirection();
	}

	public void setClDirection(MLinkedResource clDirection) {
		MpDDI.setClDirection(clDirection);
	}

	public MLinkedResource getAucType() {
		return MpDDI.getAucType();
	}

	public void setAucType(MLinkedResource aucType) {
		MpDDI.setAucType(aucType);
	}

	public MLinkedResource getClType() {
		return MpDDI.getClType();
	}

	public void setClType(MLinkedResource clType) {
		MpDDI.setClType(clType);
	}

	public MLinkedResource getPreciptDuration() {
		return MpDDI.getPreciptDuration();
	}

	public void setPreciptDuration(MLinkedResource preciptDuration) {
		MpDDI.setPreciptDuration(preciptDuration);
	}

	public MLinkedResource getObjectDuration() {
		return MpDDI.getObjectDuration();
	}

	public void setObjectDuration(MLinkedResource objectDuration) {
		MpDDI.setObjectDuration(objectDuration);
	}

	public MLinkedResource getCl() {
		return MpDDI.getCl();
	}

	public void setCl(MLinkedResource cl) {
		MpDDI.setCl(cl);
	}
}
