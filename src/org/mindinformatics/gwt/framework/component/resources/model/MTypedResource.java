package org.mindinformatics.gwt.framework.component.resources.model;

import java.util.HashSet;
import java.util.Set;


/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MTypedResource extends MLinkedResource implements ITypedResource{

	Set<String> typesUris;	
	
	public MTypedResource(String url, String label) {
		super(url, label);
	}
	
	public Set<String> getTypesUris() {
		return typesUris;
	}
	public void addTypeUri(String type) {
		if(typesUris==null) typesUris = new HashSet<String>();
		typesUris.add(type);
	}
	public void setTypesUris(Set<String> typesUris) {
		this.typesUris = typesUris;
	}
}
