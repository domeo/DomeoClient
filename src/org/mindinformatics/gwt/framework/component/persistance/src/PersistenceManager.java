package org.mindinformatics.gwt.framework.component.persistance.src;

import org.mindinformatics.gwt.domeo.services.extractors.IContentExtractorCallback;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.src.IApplication;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PersistenceManager implements IInitializableComponent, IContentExtractorCallback {

	protected boolean resourceLoaded;
	protected IApplication _application;
	protected MGenericResource currentResource;

	public PersistenceManager(IApplication application) {
		_application = application;
	}
	
	public void init() {
		resetCurrentResource();
		_application.getLogger().debug(this, "Initializing...");
	}
	
	public void logStatus() {
		_application.getLogger().debug(PersistenceManager.class.getName(), 
				"Document loaded " + resourceLoaded);
		_application.getLogger().info(PersistenceManager.class.getName(), 
				"Current document " + currentResource.getUrl());
	}
	
	protected void resetCurrentResource() {
		currentResource = null;
		resourceLoaded = false;
	}

	public boolean isDocumentAlreadyLoaded(String newDocument) {
		if (currentResource==null || newDocument==null) return false;
		return currentResource.getUrl().equals(newDocument);
	}
	
	public boolean isResourceLoaded() {
		return resourceLoaded;
	}

	public void setResourceLoaded(boolean resourceLoaded) {
		this.resourceLoaded = resourceLoaded;
	}

	public String getCurrentResourceUrl() {
		return currentResource.getUrl();
	}

	public MGenericResource getCurrentResource() {
		return currentResource;
	}

	public void setCurrentResource(MGenericResource currentResource) {
		_application.getLogger().debug(this, 
				"Resource " + currentResource.getClass().getName());
		this.currentResource = currentResource;
	}
}
