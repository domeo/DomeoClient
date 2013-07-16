package org.mindinformatics.gwt.domeo.component.bibliography.ui.listpicker;

import org.mindinformatics.gwt.domeo.component.cache.images.model.ImageProxy;
import org.mindinformatics.gwt.domeo.model.MAnnotationReference;

public interface IReferencesListPickerContainer {

	public void addImageAsData(ImageProxy image);
	public void addBibliographicObject(MAnnotationReference reference);
}
