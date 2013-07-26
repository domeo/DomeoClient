package org.mindinformatics.gwt.domeo.plugins.annotation.spls.model;

import java.util.Set;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
    public class MSPLsAnnotation extends MAnnotation {

	protected static final String LABEL = "SPL Annotation";
	public static final String TYPE = "ao:SPLAnnotation";
	public static final String BODY_TYPE = "domeo:PharmgxUsage";
	
	private MSPLPharmgxUsage pharmgxUsage;

	public MSPLPharmgxUsage getPharmgxUsage() {
	    return pharmgxUsage;
	}

	public void setPharmgxUsage(MSPLPharmgxUsage pharmgxUsage) {
	    this.pharmgxUsage = pharmgxUsage;
	}

	public String getComment() {
	    return pharmgxUsage.getComment();
	}

	public void setComment(String comment) {
	    pharmgxUsage.setComment(comment);
	}

	public String getText() {
	    return pharmgxUsage.getPharmgx().getLabel();
	}
	
	public Set<MLinkedResource> getSioDescriptions(){
	    return pharmgxUsage.getSioDescriptions();
	}

	public void addSioDescription(MLinkedResource sioDescriptionAnnot){
	    pharmgxUsage.getSioDescriptions().add(sioDescriptionAnnot);
	}

	public void addSioDescriptions(Set<MLinkedResource> sioDescriptions) {
	    pharmgxUsage.getSioDescriptions().addAll(sioDescriptions);
	}

	public MLinkedResource getSubject(){
	    return pharmgxUsage.getSubject();
	}

	public void setSubject(MLinkedResource subject){
	    pharmgxUsage.setSubject(subject);
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
