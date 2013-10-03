package org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.ui.form;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.IFormGenerator;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.model.MSPL_DDIAnnotation;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class SPL_DDIFormProvider implements IFormGenerator {

	// By contract 
	private IDomeo _domeo;

	public SPL_DDIFormProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isFormSupported(String annotationName) {
		if(annotationName.equals(MSPL_DDIAnnotation.class.getName())) 
				return true;
		else
			return false;
	}
	
	@Override
	public AFormComponent getForm(AFormsManager manager) {
		return new FSPL_DDIForm(_domeo, manager);
	}
	
	@Override
	public AFormComponent getForm(AFormsManager manager, MAnnotation annotation) {
		return new FSPL_DDIForm(_domeo,  manager, (MSPL_DDIAnnotation) annotation);
	}

	@Override
	public AFormComponent getForm(String annotationName, AFormsManager manager) {
		if(isFormSupported(annotationName)) 
			return new FSPL_DDIForm(_domeo,  manager);
		else 
			return null;
	}
}
