package org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.list;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

public interface ITermsSelectionConsumer {
	public String getFilterValue();
	public void addTerm(MLinkedResource term);
	public ArrayList<MLinkedResource> getTermsList();
}
