package org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.search;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.search.ASearchComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.model.MSPL_DDIAnnotation;

/**
 * @author Richard Boyce  <rdb20@pitt.edu>
 */
public class SPL_DDISearchComponent extends ASearchComponent {

	public SPL_DDISearchComponent(IDomeo domeo) {
		super(domeo);
	}
	
	
	/**
	 * The search on the text match is NOT case sensitive.
	 */
	public boolean filterByText(MAnnotation annotation, String textSearch) {
		if(super.filterByText(annotation, textSearch)) return true;
		if(annotation instanceof MSPL_DDIAnnotation) {
			return ((MSPL_DDIAnnotation)annotation).getText().toLowerCase()
					.contains(textSearch.toLowerCase());
		}
		return false;
	}
}
