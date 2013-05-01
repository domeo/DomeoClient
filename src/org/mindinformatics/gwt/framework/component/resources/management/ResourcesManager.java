package org.mindinformatics.gwt.framework.component.resources.management;

import java.util.ArrayList;
import java.util.HashMap;

import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */ 
public class ResourcesManager implements IResourcesManager, IInitializableComponent{

	private ArrayList<MGenericResource> resources = new ArrayList<MGenericResource>();
	private HashMap<String, MGenericResource> cachedResources = new HashMap<String, MGenericResource>();
	
	public MGenericResource cacheResource(MGenericResource resource) {
		if(doesCacheContainsResource(resource)) return findResourceByUri(resource.getUrl());
		resources.add(resource);
		cachedResources.put(resource.getUrl(), resource);
		return resource;
	}
	
	public MGenericResource findResourceByUri(String uri) {
		return cachedResources.get(uri);
	}
	
	public int getResourceCacheSize() {
		return cachedResources.size();
	}
	
	private boolean doesCacheContainsResource(MGenericResource resource) {
		boolean contains = false;
		for(MGenericResource res: resources) {
			if(res.getUrl().equals(resource.getUrl())) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	@Override
	public void init() {
		resources.clear();
		cachedResources.clear();
	}
}
