package org.mindinformatics.gwt.framework.component.resources.model;

import java.util.HashSet;
import java.util.Set;


/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MTrustedTypedResource extends MTrustedResource implements ITypedResource {

	Set<String> typesUris;	
	
	public MTrustedTypedResource(String url, String label, MGenericResource source) {
		super(url, label, source);
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
