package org.mindinformatics.gwt.domeo.plugins.annotation.relationship.model;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MRelationshipAnnotation extends MAnnotation {

	protected static final String LABEL = "Relationship";	
	
	private MQualifierAnnotation subject;
	private MLinkedResource relationship;
	private MQualifierAnnotation object;
	
	public MLinkedResource getRelationship() {
		return relationship;
	}

	public void setRelationship(MLinkedResource relationship) {
		this.relationship = relationship;
	}
	
	public MQualifierAnnotation getSubject() {
		return subject;
	}

	public void setSubject(MQualifierAnnotation subject) {
		this.subject = subject;
	}

	public MQualifierAnnotation getObject() {
		return object;
	}

	public void setObject(MQualifierAnnotation object) {
		this.object = object;
	}

	@Override
	public String getLabel() {
		return LABEL;
	}
	
	@Override
	public String getAnnotationType() {
		return "ao:Relationship";
	}
}
