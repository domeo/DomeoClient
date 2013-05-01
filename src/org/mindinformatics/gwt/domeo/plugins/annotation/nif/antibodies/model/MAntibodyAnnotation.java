package org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model;

import java.util.Set;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MAntibodyAnnotation extends MAnnotation {

	protected static final String LABEL = "Antibody Annotation";
	public static final String TYPE = "ao:AntibodyAnnotation";
	public static final String BODY_TYPE = "domeo:AntibodyUsage";
	
	private MAntibodyUsage antibodyUsage;

	public MAntibodyUsage getAntibodyUsage() {
		return antibodyUsage;
	}

	public void setAntibodyUsage(MAntibodyUsage antibodyUsage) {
		this.antibodyUsage = antibodyUsage;
	}

	public String getComment() {
		return antibodyUsage.getComment();
	}

	public void setComment(String comment) {
		antibodyUsage.setComment(comment);
	}

	public String getText() {
		return antibodyUsage.getAntibody().getLabel();
	}
	
	public Set<MLinkedResource> getProtocols(){
        return antibodyUsage.getProtocols();
    }
    public void addProtocol(MLinkedResource protocolAnnotationTerm){
    	antibodyUsage.getProtocols().add(protocolAnnotationTerm);
    }
    public void addProtocols(Set<MLinkedResource> protocols) {
    	antibodyUsage.getProtocols().addAll(protocols);
    }
    public MLinkedResource getSubject(){
        return antibodyUsage.getSubject();
    }
    public void setSubject(MLinkedResource subject){
    	antibodyUsage.setSubject(subject);
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
