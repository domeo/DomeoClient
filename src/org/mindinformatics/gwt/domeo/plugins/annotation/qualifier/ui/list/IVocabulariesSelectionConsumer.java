package org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.list;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

public interface IVocabulariesSelectionConsumer {
	public void addVocabulary(MLinkedResource term);
	public ArrayList<MLinkedResource> getVocabulariesList();
}
