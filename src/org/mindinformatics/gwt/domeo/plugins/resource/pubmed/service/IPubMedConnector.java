package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service;

import java.util.List;

import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingBibliographySetHandler;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IPubMedConnector {
	
	public void retrieveExistingBibliographySetByUrl(final IRetrieveExistingBibliographySetHandler handler, String url)
			throws IllegalArgumentException;
	
	public void retrieveExistingBibliographySet(IRetrieveExistingBibliographySetHandler completionCallback, int level) 
			throws IllegalArgumentException;
	
	public void getBibliographicObject(IPubMedItemsRequestCompleted completionCallback,
			String typeQuery, String textQuery) throws IllegalArgumentException;

	public void getBibliographicObjects(IPubMedItemsRequestCompleted completionCallback,
			String typeQuery, final List<String> textQuery, List<String> elements) throws IllegalArgumentException;
	
	public void searchBibliographicObjects(IPubMedPaginatedItemsRequestCompleted completionCallback,
			String typeQuery, final String textQuery, int maxResults, int offset) throws IllegalArgumentException;
}
