package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

public interface IPubMedPaginatedItemsRequestCompleted {
	public void returnBibliographicObject(int total, int offset, int range, ArrayList<MPublicationArticleReference> reference);
	public void bibliographicSearchNotCompleted();
	public void bibliographicSearchNotCompleted(String message);
}
