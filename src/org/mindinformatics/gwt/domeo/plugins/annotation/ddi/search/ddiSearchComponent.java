package org.mindinformatics.gwt.domeo.plugins.annotation.ddi.search;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.search.ASearchComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.MddiAnnotation;

/**
 * @author Richard Boyce  <rdb20@pitt.edu>
 */
public class ddiSearchComponent extends ASearchComponent {

	public ddiSearchComponent(IDomeo domeo) {
		super(domeo);
	}
	
	
	/**
	 * The search on the text match is NOT case sensitive.
	 */
	public boolean filterByText(MAnnotation annotation, String textSearch) {
		if(super.filterByText(annotation, textSearch)) return true;
		if(annotation instanceof MddiAnnotation) {
			return ((MddiAnnotation)annotation).getText().toLowerCase()
					.contains(textSearch.toLowerCase());
		}
		return false;
	}
}
