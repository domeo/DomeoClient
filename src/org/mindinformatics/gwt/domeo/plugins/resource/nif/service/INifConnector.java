package org.mindinformatics.gwt.domeo.plugins.resource.nif.service;

import org.mindinformatics.gwt.domeo.component.textmining.src.ITextminingRequestCompleted;

public interface INifConnector {

	public void searchData(final INifDataRequestCompleted completionCallback,
			String textQuery, String type, String vendor, String resource, int pageNumber, int pageSize)
			throws IllegalArgumentException;
	
	public void annotate(ITextminingRequestCompleted completionCallback, final String url,
			final String textContent, final String includeCat, final String excludeCat,
			final int minLength, final boolean longestOnly, final boolean includeAbbrev, 
			final boolean includeAcronym, final boolean includeNumbers) throws IllegalArgumentException;
}
