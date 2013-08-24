package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service;

import org.mindinformatics.gwt.domeo.component.textmining.src.ITextminingRequestCompleted;



/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IBioPortalConnector {

	public void searchTerm(IBioPortalItemsRequestCompleted completionCallback,
			final String textQuery, final String virtualIds) throws IllegalArgumentException;
	
	public void searchTerm(IBioPortalItemsRequestCompleted completionCallback,
			final String textQuery, final String virtualIds, final int pageNumber, final int pageSize) throws IllegalArgumentException;
	
	public void textmine(ITextminingRequestCompleted completionCallback, final String url,
			final String textContent, final String virtualIds) throws IllegalArgumentException;
}
