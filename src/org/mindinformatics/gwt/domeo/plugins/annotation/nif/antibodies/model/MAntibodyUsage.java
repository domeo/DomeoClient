package org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model;

import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class MAntibodyUsage {

	private Long localId;				// sent to the server for matching saved items
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
	
	MAntibody antibody;
	
	public MAntibody getAntibody() {
		return antibody;
	}
	public void setAntibody(MAntibody antibody) {
		this.antibody = antibody;
	}

	private String comment;
	Set<MLinkedResource> protocols  = new HashSet<MLinkedResource>();
	MLinkedResource subject;
	
	
	public void addProtocol(MLinkedResource protocol) {
		protocols.add(protocol);
	}
	public Set<MLinkedResource> getProtocols() {
		return protocols;
	}
	public void setProtocols(Set<MLinkedResource> protocols) {
		this.protocols = protocols;
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
