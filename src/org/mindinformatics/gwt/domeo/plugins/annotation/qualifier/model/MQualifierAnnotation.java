package org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MQualifierAnnotation extends MAnnotation {

	public static final String LABEL = "Qualifier";
	public static final String TYPE = "ao:Qualifier";
	
	private ArrayList<MLinkedResource> terms = new ArrayList<MLinkedResource>();
	
	public void addTerm(MLinkedResource term) { terms.add(term); }
	public ArrayList<MLinkedResource> getTerms() { return terms; }
	public MLinkedResource getTerm() { return terms.get(0); }
	
	public void setTerm(MLinkedResource term) {
		terms.clear();
		terms.add(term);
	}

//	private MLinkedDataResource relationship;
//	public MLinkedDataResource getRelationship() {
//		return relationship;
//	}
//
//	public void setRelationship(MLinkedDataResource relationship) {
//		this.relationship = relationship;
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
