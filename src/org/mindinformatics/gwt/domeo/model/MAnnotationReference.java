package org.mindinformatics.gwt.domeo.model;

import org.mindinformatics.gwt.framework.model.references.MReference;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MAnnotationReference extends MAnnotation {
	
	public static final String LABEL = "Reference";
	public static final String TYPE = "ao:Reference";
	
	private MReference reference;

	public MReference getReference() {
		return reference;
	}
	public void setReference(MReference reference) {
		this.reference = reference;
	}
	
	@Override
	public String getLabel() {
		return LABEL;
	}
	
	@Override
	public String getAnnotationType() {
		return TYPE;
	}
}
