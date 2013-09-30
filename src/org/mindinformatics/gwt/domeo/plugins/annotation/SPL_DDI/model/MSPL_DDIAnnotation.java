package org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.model;

import java.util.Set;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
    public class MSPL_DDIAnnotation extends MAnnotation {

	protected static final String LABEL = "SPL Annotation";
	public static final String TYPE = "ao:SPLAnnotation";
	public static final String BODY_TYPE = "domeo:SPL_DDIUsage";
	
	private MSPL_DDIUsage SPL_DDIUsage;

	public MSPL_DDIUsage getSPL_DDIUsage() {
	    return SPL_DDIUsage;
	}

	public void setSPL_DDIUsage(MSPL_DDIUsage SPL_DDIUsage) {
	    this.SPL_DDIUsage = SPL_DDIUsage;
	}

	public String getComment() {
	    return SPL_DDIUsage.getComment();
	}

	public void setComment(String comment) {
	    SPL_DDIUsage.setComment(comment);
	}

	public String getText() {
	    return SPL_DDIUsage.getSPL_DDI().getLabel();
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
