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

        // The MPharmgx instance that will hold tagging data
	MPharmgx pharmgx;
	
	public MPharmgx getPharmgx() {
		return pharmgx;
	}
	public void setPharmgx(MPharmgx pharmgx) {
		this.pharmgx = pharmgx;
	}
   
        // tags in the pharmgx model are the object of SIO predicates
	private String comment;
	MLinkedResource subject;
	Set<MLinkedResource> sioDescriptions  = new HashSet<MLinkedResource>();


	public void addSioDescriptions(MLinkedResource sioDescription) {
		sioDescriptions.add(sioDescription);
	}
	public Set<MLinkedResource> getSioDescriptions() {
		return sioDescriptions;
	}
	public void setSioDescriptions(Set<MLinkedResource> sioDescriptions) {
		this.sioDescriptions = sioDescriptions;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public MLinkedResource getSubject() {
		return subject;
	}
	public void setSubject(MLinkedResource subject) {
		this.subject = subject;
	}
}
