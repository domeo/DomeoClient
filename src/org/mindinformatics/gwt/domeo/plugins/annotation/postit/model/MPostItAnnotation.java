package org.mindinformatics.gwt.domeo.plugins.annotation.postit.model;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.contentasrdf.model.MContentAsRdf;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MPostItAnnotation extends MAnnotation {

	protected static final String LABEL = "Post it";
	public static final String TYPE = "ao:PostIt";
	
	private PostitType type;
	private MContentAsRdf body;

	public MContentAsRdf getBody() {
		return body;
	}
	public void setBody(MContentAsRdf body) {
		this.body = body;
	}
	public PostitType getType() {
		return type;
	}
	public void setType(PostitType type) {
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
