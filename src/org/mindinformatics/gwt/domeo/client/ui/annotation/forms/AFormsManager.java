package org.mindinformatics.gwt.domeo.client.ui.annotation.forms;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.src.IContainerPanel;

import com.google.gwt.user.client.ui.Composite;

public abstract class AFormsManager extends Composite {

	public abstract IContainerPanel getContainer();
	public abstract void displayMessage(String message);
	public abstract void clearMessage();
	public abstract MGenericResource getResource();
	public abstract ArrayList<MAnnotation> getTargets();
	public abstract void hideContainer();
}
