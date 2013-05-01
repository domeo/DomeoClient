package org.mindinformatics.gwt.domeo.plugins.annotation.highlight.model;

import org.mindinformatics.gwt.domeo.model.MAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MHighlightAnnotation extends MAnnotation {

	public static final String LABEL = "Highlight";
	public static final String TYPE = "ao:Highlight";
	
	private String color;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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
