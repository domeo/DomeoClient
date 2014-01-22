package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model;

import java.util.Set;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class Mexpertstudy_pDDIUsage {

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

	// tags in the expertstudy_pDDI model are the object of SIO predicates
	private String comment;
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
    // The Mexpertstudy_pDDI instance is a "normalized" instance that holds the model values as strings
	Mexpertstudy_pDDI expertstudy_pDDI = new Mexpertstudy_pDDI("","",null);
	
	public Mexpertstudy_pDDI getexpertstudy_pDDI() {
		return expertstudy_pDDI;
	}
	public void setexpertstudy_pDDI(Mexpertstudy_pDDI expertstudy_pDDI) {
		this.expertstudy_pDDI = expertstudy_pDDI;
	}
    
	
	
}

