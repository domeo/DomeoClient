package org.mindinformatics.gwt.domeo.model.selectors;

import org.mindinformatics.gwt.domeo.model.MAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */ 
@SuppressWarnings("serial")
public class MAnnotationSelector extends MSelector {

	public static final String TYPE = "domeo:AnnotationSelector";
	public static final String TARGET = "ao:annotation";
	
	private MAnnotation annotation;
	
	public MAnnotation getAnnotation() {
		return annotation;
	}

	public void setAnnotation(MAnnotation annotation) {
		this.annotation = annotation;
	}

    public String getSelectorType() {
    	return TYPE;
    }
}
