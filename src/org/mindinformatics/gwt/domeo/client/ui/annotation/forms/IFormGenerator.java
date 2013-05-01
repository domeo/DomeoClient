package org.mindinformatics.gwt.domeo.client.ui.annotation.forms;

import org.mindinformatics.gwt.domeo.model.MAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IFormGenerator {

	public boolean isFormSupported(String annotationName);
	public AFormComponent getForm(AFormsManager manager);
	public AFormComponent getForm(AFormsManager manager, MAnnotation annotation);
	public AFormComponent getForm(String annotationName, AFormsManager manager);
}
