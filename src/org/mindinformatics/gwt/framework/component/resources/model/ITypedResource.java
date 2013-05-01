package org.mindinformatics.gwt.framework.component.resources.model;

import java.util.Set;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ITypedResource {

	public Set<String> getTypesUris();
	public void addTypeUri(String type);
	public void setTypesUris(Set<String> typesUris);
}
