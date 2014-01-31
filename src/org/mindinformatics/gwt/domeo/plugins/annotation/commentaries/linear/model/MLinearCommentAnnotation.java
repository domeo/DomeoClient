package org.mindinformatics.gwt.domeo.plugins.annotation.commentaries.linear.model;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MLinearCommentAnnotation extends MAnnotation {

	public static final String LABEL = "Comment";
	public static final String TYPE = "ao:LinearComment";
	
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
	public void addSelector(MSelector selector) {
		throw new RuntimeException("MLinearCommentAnnotation:addSelector(): Linear comments allow for only one target");
	}
//	@Override
//	public ArrayList<MSelector> getSelectors() {
//		throw new RuntimeException("MLinearCommentAnnotation:getSelectors(): Linear comments allow for only one target");
//	}	
	
	@Override
	public String getLabel() {
		return LABEL;
	}
	@Override
	public String getAnnotationType() {
		return TYPE;
	}
}
