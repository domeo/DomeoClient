package org.mindinformatics.gwt.framework.component.qualifiers.ui;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ISearchTermsContainer {

	public String getTextContent();
	public void addAssociatedTerm(MLinkedResource term);
	public ArrayList<MLinkedResource> getItems();
}
