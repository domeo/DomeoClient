package org.mindinformatics.gwt.domeo.client.ui.annotation.search;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ISearchGenerator {

	public boolean isSearchSupported(String annotationName);
	public ASearchComponent getSearchComponent(String annotationName);
}
