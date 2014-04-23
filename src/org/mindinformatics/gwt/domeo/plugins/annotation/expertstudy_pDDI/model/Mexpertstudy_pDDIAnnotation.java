package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model;

import java.util.Set;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
public class Mexpertstudy_pDDIAnnotation extends MAnnotation implements Iexpertstudy_pDDI{
	// Plugin builder error
	public static final String LABEL = "expertstudy Annotation"; 
	public static final String TYPE = "ao:expertstudyAnnotation"; 
	
	 public static final String BODY_TYPE = "domeo:expertstudy_pDDIUsage";
	 private Mexpertstudy_pDDIUsage MpDDIUsage;

	

	public Mexpertstudy_pDDIUsage getMpDDIUsage() {
		return MpDDIUsage;
	}

	public void setMpDDIUsage(Mexpertstudy_pDDIUsage MpDDI) {
		this.MpDDIUsage = MpDDI;
	}

	public String getComment() {
		return MpDDIUsage.getComment();
	}

	public void setComment(String comment) {
		MpDDIUsage.setComment(comment);
	}
	
	//DDI drug 1 and drug 2
	/*
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
		MpDDIUsage.setDrug1(drug2);
	}*/
	
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


	@Override
	public String getLabel() {
		return LABEL;
	}

	@Override
	public String getAnnotationType() {
		return TYPE;
	}

	public String getText() {
		// TODO Auto-generated method stub
		return "text";
	}
}
