package org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src;

import com.google.gwt.core.client.JsArray;

public interface IRetrieveExistingBibliographySetHandler {

	public void setExistingBibliographySetList(JsArray responseOnSets, boolean isVirtual);
	public void bibliographySetListNotCreated();
	public void bibliographySetListNotCreated(String message);
}
