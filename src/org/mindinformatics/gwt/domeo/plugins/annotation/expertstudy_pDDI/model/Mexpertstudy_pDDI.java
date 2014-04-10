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

	MLinkedResource  comment, type, role, statement, modality, extraResource;
	
	public MLinkedResource getExtraResource() {
		return extraResource;
	}

	public MLinkedResource getType() {
		return type;
	}

	public void setType(MLinkedResource type) {
		this.type = type;
	}

	public MLinkedResource getRole() {
		return role;
	}

	public void setRole(MLinkedResource role) {
		this.role = role;
	}

	public MLinkedResource getStatement() {
		return statement;
	}

	public void setStatement(MLinkedResource statement) {
		this.statement = statement;
	}

	public MLinkedResource getModality() {
		return modality;
	}

	public void setModality(MLinkedResource modality) {
		this.modality = modality;
	}

	public Set<MLinkedResource> getStatementsResource() {
		return statementsResource;
	}

	public void setStatementsResource(Set<MLinkedResource> statementsResource) {
		this.statementsResource = statementsResource;
	}

	public void setExtraResource(MLinkedResource extraResource) {
		this.extraResource = extraResource;
	}

	public MLinkedResource getComment() {
		return comment;
	}

	public void setComment(MLinkedResource comment) {
		this.comment = comment;
	}

	Set<MLinkedResource> statementsResource;

	public Mexpertstudy_pDDI(String url, String label, MGenericResource source) {
		super(url, label, source);
	}

}
