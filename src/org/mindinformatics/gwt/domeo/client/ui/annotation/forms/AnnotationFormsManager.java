package org.mindinformatics.gwt.domeo.client.ui.annotation.forms;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationFormsManager {

	private HashMap<String, IFormGenerator> 
		annotationForms = new HashMap<String, IFormGenerator>();
	
	public boolean registerAnnotationForm(String annotationName, IFormGenerator formGenerator) {
		if(!annotationForms.containsKey(annotationName)) {
			annotationForms.put(annotationName, formGenerator);
			return true;
		}
		return false;
	}
	
	public IFormGenerator getAnnotationForm(String annotationName) {
		if(annotationForms.containsKey(annotationName)) {
			return annotationForms.get(annotationName);
		} else
			return null; //TODO define a default tail
	}
	
	public Collection<IFormGenerator> getAnnotationFormGenerators() {
		return annotationForms.values();
	}
}
