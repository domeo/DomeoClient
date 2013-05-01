package org.mindinformatics.gwt.domeo.plugins.annotation.postit.ui.form;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.IFormGenerator;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PostItFormProvider implements IFormGenerator {

	// By contract 
	private IDomeo _domeo;

	public PostItFormProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isFormSupported(String annotationName) {
		if(annotationName.equals(MPostItAnnotation.class.getName())) return true;
		return false;
	}
	
	@Override
	public AFormComponent getForm(AFormsManager manager) {
		return new FPostItForm(_domeo, manager);
	}
	
	@Override
	public AFormComponent getForm(AFormsManager manager, MAnnotation annotation) {
		return new FPostItForm(_domeo,  manager, (MPostItAnnotation) annotation);
	}

	@Override
	public AFormComponent getForm(String annotationName, AFormsManager manager) {
		if(isFormSupported(annotationName)) return new FPostItForm(_domeo,  manager);
		return null;
	}
}
