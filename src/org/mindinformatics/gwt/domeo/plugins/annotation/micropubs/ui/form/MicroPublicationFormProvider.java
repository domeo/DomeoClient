package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.form;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.IFormGenerator;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class MicroPublicationFormProvider implements IFormGenerator {

	// By contract 
	private IDomeo _domeo;

	public MicroPublicationFormProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isFormSupported(String annotationName) {
		if(annotationName.equals(MMicroPublicationAnnotation.class.getName())) return true;
		return false;
	}
	
	@Override
	public AFormComponent getForm(AFormsManager manager) {
		return new FMicroPublicationForm(_domeo, manager, true);
	}
	
	@Override
	public AFormComponent getForm(AFormsManager manager, MAnnotation annotation) {
		return new FMicroPublicationForm(_domeo,  manager, (MMicroPublicationAnnotation) annotation);
	}

	@Override
	public AFormComponent getForm(String annotationName, AFormsManager manager) {
		if(isFormSupported(annotationName)) return new FMicroPublicationForm(_domeo,  manager, true);
		return null;
	}
}
