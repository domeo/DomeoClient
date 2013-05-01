package org.mindinformatics.gwt.framework.component.resources.management;

import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;


/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */ 
public interface IResourcesManager {

	public MGenericResource cacheResource(MGenericResource resource);
	public MGenericResource findResourceByUri(String uri);
}
