package org.mindinformatics.gwt.domeo.plugins.resource.nif.service;

import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.IBioPortalTextminingRequestCompleted;

public interface INifConnector {

	public void searchData(final INifDataRequestCompleted completionCallback,
			String textQuery, String type, String vendor, String resource, int pageNumber, int pageSize)
			throws IllegalArgumentException;
	
	public void annotate(IBioPortalTextminingRequestCompleted completionCallback, final String url,
			final String textContent, final String include, final String exclude) throws IllegalArgumentException;
}
