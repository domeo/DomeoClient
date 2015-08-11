package org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model;

import java.util.Set;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.contentasrdf.model.MContentAsRdf;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.SPLType;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
public class MddiAnnotation extends MAnnotation implements
		Iddi {
	// Plugin builder error
	private ddiType type;

	public ddiType getType() {
		return type;
	}

	public void setType(ddiType type) {
		this.type = type;
	}

	public MContentAsRdf getBody() {
		return body;
	}

	public void setBody(MContentAsRdf body) {
		this.body = body;
	}

	private MContentAsRdf body;
	private MddiUsage MpDDIUsage;

	public MddiUsage getMpDDIUsage() {
		return MpDDIUsage;
	}

	public void setMpDDIUsage(MddiUsage MpDDI) {
		this.MpDDIUsage = MpDDI;
	}

	public String getComment() {
		return MpDDIUsage.getComment();
	}

	public void setComment(String comment) {
		MpDDIUsage.setComment(comment);
	}

	// DDI drug 1 and drug 2

	public MLinkedResource getDrug1() {
		return MpDDIUsage.getDrug1();
	}

	public void setDrug1(MLinkedResource drug1) {
		MpDDIUsage.setDrug1(drug1);
	}

	public MLinkedResource getDrug2() {
		return MpDDIUsage.getDrug2();
	}

	public void setDrug2(MLinkedResource drug2) {
		MpDDIUsage.setDrug2(drug2);
	}

	// DDI type of drug1 as type1 and type of drug2 as type2

	public MLinkedResource getType1() {
		return MpDDIUsage.getType1();
	}

	public void setType1(MLinkedResource Type1) {
		MpDDIUsage.setType1(Type1);
	}

	public MLinkedResource getType2() {
		return MpDDIUsage.getType2();
	}

	public void setType2(MLinkedResource Type2) {
		MpDDIUsage.setType2(Type2);
	}

	// DDI role of drug1 as role1 and role of drug2 as role2

	public MLinkedResource getRole1() {
		return MpDDIUsage.getRole1();
	}

	public void setRole1(MLinkedResource role1) {
		MpDDIUsage.setRole1(role1);
	}

	public MLinkedResource getRole2() {
		return MpDDIUsage.getRole2();
	}

	public void setRole2(MLinkedResource role2) {
		MpDDIUsage.setRole2(role2);
	}

	// statement

	public MLinkedResource getStatement() {
		return MpDDIUsage.getStatement();
	}

	public void setStatement(MLinkedResource statement) {
		MpDDIUsage.setStatement(statement);
	}

	// modality

	public MLinkedResource getModality() {
		return MpDDIUsage.getModality();
	}

	public void setModality(MLinkedResource modality) {
		MpDDIUsage.setModality(modality);
	}

	// increase Auc fields

	public MLinkedResource getNumOfparcipitants() {
		return MpDDIUsage.getNumOfparcipitants();
	}

	public void setNumOfparcipitants(MLinkedResource numOfparcipitants) {
		MpDDIUsage.setNumOfparcipitants(numOfparcipitants);
	}

	public MLinkedResource getIncreaseAuc() {
		return MpDDIUsage.getIncreaseAuc();
	}

	public void setIncreaseAuc(MLinkedResource increaseAuc) {
		MpDDIUsage.setIncreaseAuc(increaseAuc);
	}

	public MLinkedResource getEvidenceType() {
		return MpDDIUsage.getEvidenceType();
	}

	public void setEvidenceType(MLinkedResource evidenceType) {
		MpDDIUsage.setEvidenceType(evidenceType);
	}

	public MLinkedResource getObjectDose() {
		return MpDDIUsage.getObjectDose();
	}

	public void setObjectDose(MLinkedResource objectDose) {
		MpDDIUsage.setObjectDose(objectDose);
	}

	public MLinkedResource getPreciptDose() {
		return MpDDIUsage.getPreciptDose();
	}

	public void setPreciptDose(MLinkedResource preciptDose) {
		MpDDIUsage.setPreciptDose(preciptDose);
	}

	public MLinkedResource getAssertType() {
		return MpDDIUsage.getAssertType();
	}

	public void setAssertType(MLinkedResource assertType) {
		MpDDIUsage.setAssertType(assertType);
	}
	
	
	public MLinkedResource getT12() {
		return MpDDIUsage.getT12();
	}

	public void setT12(MLinkedResource t12) {
		MpDDIUsage.setT12(t12);
	}

	public MLinkedResource getCmax() {
		return MpDDIUsage.getCmax();
	}

	public void setCmax(MLinkedResource cmax) {
		MpDDIUsage.setCmax(cmax);
	}

	public MLinkedResource getObjectRegimen() {
		return MpDDIUsage.getObjectRegimen();
	}

	public void setObjectRegimen(MLinkedResource objectRegimen) {
		MpDDIUsage.setObjectRegimen(objectRegimen);
	}

	public MLinkedResource getPreciptRegimen() {
		return MpDDIUsage.getPreciptRegimen();
	}

	public void setPreciptRegimen(MLinkedResource preciptRegimen) {
		MpDDIUsage.setPreciptRegimen(preciptRegimen);
	}
	
	public MLinkedResource getAucDirection() {
		return MpDDIUsage.getAucDirection();
	}

	public void setAucDirection(MLinkedResource aucDirection) {
		MpDDIUsage.setAucDirection(aucDirection);
	}

	public MLinkedResource getClDirection() {
		return MpDDIUsage.getClDirection();
	}

	public void setClDirection(MLinkedResource clDirection) {
		MpDDIUsage.setClDirection(clDirection);
	}

	public MLinkedResource getAucType() {
		return MpDDIUsage.getAucType();
	}

	public void setAucType(MLinkedResource aucType) {
		MpDDIUsage.setAucType(aucType);
	}

	public MLinkedResource getClType() {
		return MpDDIUsage.getClType();
	}

	public void setClType(MLinkedResource clType) {
		MpDDIUsage.setClType(clType);
	}

	public MLinkedResource getPreciptDuration() {
		return MpDDIUsage.getPreciptDuration();
	}

	public void setPreciptDuration(MLinkedResource preciptDuration) {
		MpDDIUsage.setPreciptDuration(preciptDuration);
	}

	public MLinkedResource getObjectDuration() {
		return MpDDIUsage.getObjectDuration();
	}

	public void setObjectDuration(MLinkedResource objectDuration) {
		MpDDIUsage.setObjectDuration(objectDuration);
	}

	public MLinkedResource getCl() {
		return MpDDIUsage.getCl();
	}

	public void setCl(MLinkedResource cl) {
		MpDDIUsage.setCl(cl);
	}
	

	@Override
	public String getLabel() {
		return LABEL;
	}

	@Override
	public String getAnnotationType() {
		return TYPE;
	}

	public String getText() {
		return "text";
	}
}
