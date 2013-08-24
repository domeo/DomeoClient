package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.picker;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;

public interface IStatementPaginatedItemsRequestCompleted {
	public void returnMicroPublicationObject(int total, int offset, int range, ArrayList<MMicroPublicationAnnotation> reference);
	public void microPublicationSearchNotCompleted();
	public void microPublicationSearchNotCompleted(String message);
}
