package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model;

import java.util.Set;

import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTrustedResource;

/**
 * @author Andres Hernandez <amh211@pitt.edu>
 */
@SuppressWarnings("serial")
public class Mexpertstudy_pDDI extends MTrustedResource {

	MLinkedResource drug1, drug2, type1, role1, type2, role2, statement, modality;
	String comment;

	public MLinkedResource getStatement() {
		return statement;
	}

	public void setStatement(MLinkedResource statement) {
		this.statement = statement;
	}

	public MLinkedResource getDrug1() {
		return drug1;
	}

	public void setDrug1(MLinkedResource drug1) {
		this.drug1 = drug1;
	}

	public MLinkedResource getDrug2() {
		return drug2;
	}

	public void setDrug2(MLinkedResource drug2) {
		this.drug2 = drug2;
	}

	public MLinkedResource getType1() {
		return type1;
	}

	public void setType1(MLinkedResource type1) {
		this.type1 = type1;
	}

	public MLinkedResource getRole1() {
		return role1;
	}

	public void setRole1(MLinkedResource role1) {
		this.role1 = role1;
	}

	public MLinkedResource getType2() {
		return type2;
	}

	public void setType2(MLinkedResource type2) {
		this.type2 = type2;
	}

	public MLinkedResource getRole2() {
		return role2;
	}

	public void setRole2(MLinkedResource role2) {
		this.role2 = role2;
	}

	public MLinkedResource getModality() {
		return modality;
	}

	public void setModality(MLinkedResource modality) {
		this.modality = modality;
	}

	public String getComment() {
		return comment;
	}



	public void setComment(String comment) {
		this.comment = comment;
	}

	public Mexpertstudy_pDDI(String url, String label, MGenericResource source) {
		super(url, label, source);
	}

}
