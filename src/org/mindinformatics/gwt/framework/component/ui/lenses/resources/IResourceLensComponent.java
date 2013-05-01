package org.mindinformatics.gwt.framework.component.ui.lenses.resources;

import java.util.HashMap;

import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;

public interface IResourceLensComponent {

	public void initializeLens(MGenericResource resource, HashMap<String, String> parameters);
}
