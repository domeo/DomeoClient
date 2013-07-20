package org.mindinformatics.gwt.domeo.plugins.annotation.spls.model;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.contentasrdf.model.MContentAsRdf;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Richard Boyce  <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
public class MSPLsAnnotation extends MAnnotation {

	protected static final String LABEL = "SPL Annotation";
	public static final String TYPE = "ao:SPL_Annotation";
	
	private SPLType type;
	private MContentAsRdf body;

	public MContentAsRdf getBody() {
		return body;
	}
	public void setBody(MContentAsRdf body) {
		this.body = body;
	}
	public SPLType getType() {
		return type;
	}
	public void setType(SPLType type) {
		this.type = type;
	}
	public String getText() {
		return body.getChars();
	}
	public void setText(String text) {
		body.setChars(text);
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
