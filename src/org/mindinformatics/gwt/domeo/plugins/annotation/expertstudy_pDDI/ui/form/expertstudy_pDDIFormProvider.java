package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.ui.form;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.IFormGenerator;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIAnnotation;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class expertstudy_pDDIFormProvider implements IFormGenerator {

	// By contract 
	private IDomeo _domeo;

	public expertstudy_pDDIFormProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isFormSupported(String annotationName) {
		if(annotationName.equals(Mexpertstudy_pDDIAnnotation.class.getName())) 
				return true;
		else
			return false;
	}
	
	@Override
	public AFormComponent getForm(AFormsManager manager) {
		return new Fexpertstudy_pDDIForm(_domeo, manager);
	}
	
	@Override
	public AFormComponent getForm(AFormsManager manager, MAnnotation annotation) {
		return new Fexpertstudy_pDDIForm(_domeo,  manager, (Mexpertstudy_pDDIAnnotation) annotation);
	}

	@Override
	public AFormComponent getForm(String annotationName, AFormsManager manager) {
		if(isFormSupported(annotationName)) 
			return new Fexpertstudy_pDDIForm(_domeo,  manager);
		else 
			return null;
	}
}
