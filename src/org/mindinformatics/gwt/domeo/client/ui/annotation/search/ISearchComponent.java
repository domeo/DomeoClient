package org.mindinformatics.gwt.domeo.client.ui.annotation.search;

import org.mindinformatics.gwt.domeo.model.MAnnotation;

/**
 * List of methods to be implemented by any annotation search component.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ISearchComponent {

	public boolean filterBySet(MAnnotation annotation, Long setFilter);
	public boolean filterByAccess(MAnnotation annotation, String accessFilter);
	public boolean filterByType(MAnnotation annotation, String typeFilter);
	public boolean filterByText(MAnnotation annotation, String textSearch);
}
