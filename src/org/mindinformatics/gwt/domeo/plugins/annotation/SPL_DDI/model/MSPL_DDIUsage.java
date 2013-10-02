package org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.model;

import java.util.Set;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class MSPL_DDIUsage {

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

	// tags in the SPL_DDI model are the object of SIO predicates
	private String comment;
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
    // The MSPL_DDI instance is a "normalized" instance that holds the model values as strings
	MSPL_DDI SPL_DDI = new MSPL_DDI("","",null);
	
	public MSPL_DDI getSPL_DDI() {
		return SPL_DDI;
	}
	public void setSPL_DDI(MSPL_DDI SPL_DDI) {
		this.SPL_DDI = SPL_DDI;
	}
    
	
	
}

