package org.mindinformatics.gwt.domeo.plugins.annotation.ddi.ui.form;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.IFormGenerator;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.MddiAnnotation;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class ddiFormProvider implements IFormGenerator {

	// By contract 
	private IDomeo _domeo;

	public ddiFormProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isFormSupported(String annotationName) {
		if(annotationName.equals(MddiAnnotation.class.getName())) 
				return true;
		else
			return false;
	}
	
	@Override
	public AFormComponent getForm(AFormsManager manager) {
		return new FddiForm(_domeo, manager);
	}
	
	@Override
	public AFormComponent getForm(AFormsManager manager, MAnnotation annotation) {
		return new FddiForm(_domeo,  manager, (MddiAnnotation) annotation);
	}

	@Override
	public AFormComponent getForm(String annotationName, AFormsManager manager) {
		if(isFormSupported(annotationName)) 
			return new FddiForm(_domeo,  manager);
		else 
			return null;
	}
}
