package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model;

import java.util.Set;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
    public class Mexpertstudy_pDDIAnnotation extends MAnnotation {

	protected static final String LABEL = "SPL Annotation";
	public static final String TYPE = "ao:SPLAnnotation";
	public static final String BODY_TYPE = "domeo:expertstudy_pDDIUsage";
	
	private Mexpertstudy_pDDIUsage expertstudy_pDDIUsage;

	public Mexpertstudy_pDDIUsage getexpertstudy_pDDIUsage() {
	    return expertstudy_pDDIUsage;
	}

	public void setexpertstudy_pDDIUsage(Mexpertstudy_pDDIUsage expertstudy_pDDIUsage) {
	    this.expertstudy_pDDIUsage = expertstudy_pDDIUsage;
	}

	public String getComment() {
	    return expertstudy_pDDIUsage.getComment();
	}

	public void setComment(String comment) {
	    expertstudy_pDDIUsage.setComment(comment);
	}

	public String getText() {
	    return expertstudy_pDDIUsage.getexpertstudy_pDDI().getLabel();
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
