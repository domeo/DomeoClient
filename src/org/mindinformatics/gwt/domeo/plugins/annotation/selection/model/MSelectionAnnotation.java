package org.mindinformatics.gwt.domeo.plugins.annotation.selection.model;

import org.mindinformatics.gwt.domeo.model.MAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MSelectionAnnotation extends MAnnotation {

	public static final String LABEL = "Selection";
	public static final String TYPE = "ao:Selection";
	
	@Override
	public String getLabel() {
		return LABEL;
	}
	
	@Override
	public String getAnnotationType() {
		return TYPE;
	}
}
