package org.mindinformatics.gwt.framework.component.qualifiers.ui;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

public interface ISearchQualifiersContainer {

	public void addQualifiers(MLinkedResource term);
	public ArrayList<MLinkedResource> getQualifiers();
	public ArrayList<MLinkedResource> getQualifierAnnotations();
}
