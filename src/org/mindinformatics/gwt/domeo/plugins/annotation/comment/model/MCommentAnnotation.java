package org.mindinformatics.gwt.domeo.plugins.annotation.comment.model;

import org.mindinformatics.gwt.domeo.model.MAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MCommentAnnotation extends MAnnotation {

	public static final String LABEL = "Comment";
	public static final String TYPE = "ao:Comment";
	
	private String title;
	private String text;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
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
