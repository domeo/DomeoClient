package org.mindinformatics.gwt.domeo.model;

import java.io.Serializable;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MTargetSelector;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class MBibliographicSet extends MAnnotationSet implements Serializable,
		IsSerializable {

	private int level = 0;
	
	public void addSelfReference(MAnnotation annotation) {
		if(level==0) level = 1;
		addAnnotation(annotation);
    }
	
	public void addReference(MAnnotation annotation) {
		level = 2;
		addAnnotation(annotation);
	}
	
	public void acknowledgeReference(MAnnotation annotation) {
		level = 2;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public MAnnotation getSelfReference() {
		for(MAnnotation ann: annotations) {
			if(ann.getSelector() instanceof MTargetSelector) return ann; 
		}
		return null;
	}
	
	public String getType() {
		return IDomeoOntology.bibliographySet;
	}
}
