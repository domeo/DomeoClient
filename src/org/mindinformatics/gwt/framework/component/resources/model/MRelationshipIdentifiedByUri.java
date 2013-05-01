package org.mindinformatics.gwt.framework.component.resources.model;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MRelationshipIdentifiedByUri extends MLinkedResource {
	
	private MLinkedResource[] domain;
	private MLinkedResource[] range;
	
	public MRelationshipIdentifiedByUri(String url, String label) {
		super(url, label);
	}
	
	public MLinkedResource[] getDomain() {
		return domain;
	}
	public void setDomain(MLinkedResource[] domain) {
		this.domain = domain;
	}
	public MLinkedResource[] getRange() {
		return range;
	}
	public void setRange(MLinkedResource[] range) {
		this.range = range;
	}
}
