package org.mindinformatics.gwt.framework.component.resources.model;


/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MClassIdentifiedByUri extends MLinkedResource {

	String[] typesUris;	
	
	public MClassIdentifiedByUri(String url, String label) {
		super(url, label);
	}
	
	public String[] getTypesUris() {
		return typesUris;
	}
	public void setTypesUris(String[] typesUris) {
		this.typesUris = typesUris;
	}
}
