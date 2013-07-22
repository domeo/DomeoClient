package org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.form;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.IFormGenerator;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class SPLsFormProvider implements IFormGenerator {

	// By contract 
	private IDomeo _domeo;

	public SPLsFormProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isFormSupported(String annotationName) {
		if(annotationName.equals(MSPLsAnnotation.class.getName())) return true;
		return false;
	}
	
	@Override
	public AFormComponent getForm(AFormsManager manager) {
		return new FSPLsForm(_domeo, manager);
	}
	
	@Override
	public AFormComponent getForm(AFormsManager manager, MAnnotation annotation) {
		return new FSPLsForm(_domeo,  manager, (MSPLsAnnotation) annotation);
	}

	@Override
	public AFormComponent getForm(String annotationName, AFormsManager manager) {
		if(isFormSupported(annotationName)) return new FSPLsForm(_domeo,  manager);
		return null;
	}
}
