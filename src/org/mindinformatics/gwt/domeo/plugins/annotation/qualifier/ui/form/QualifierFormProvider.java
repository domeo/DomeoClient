package org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.form;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.IFormGenerator;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class QualifierFormProvider implements IFormGenerator {

	// By contract 
	private IDomeo _domeo;

	public QualifierFormProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isFormSupported(String annotationName) {
		if(annotationName.equals(MQualifierAnnotation.class.getName())) return true;
		return false;
	}
	
	@Override
	public AFormComponent getForm(AFormsManager manager) {
		return new FQualifierForm2(_domeo, manager, true);
	}
	
	@Override
	public AFormComponent getForm(AFormsManager manager, MAnnotation annotation) {
		return new FQualifierForm2(_domeo,  manager, (MQualifierAnnotation) annotation);
	}

	@Override
	public AFormComponent getForm(String annotationName, AFormsManager manager) {
		if(isFormSupported(annotationName)) return new FQualifierForm2(_domeo,  manager, true);
		return null;
	}
}
