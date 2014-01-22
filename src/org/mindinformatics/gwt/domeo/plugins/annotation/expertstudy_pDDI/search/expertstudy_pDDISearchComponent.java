package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.search;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.search.ASearchComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIAnnotation;

/**
 * @author Richard Boyce  <rdb20@pitt.edu>
 */
public class expertstudy_pDDISearchComponent extends ASearchComponent {

	public expertstudy_pDDISearchComponent(IDomeo domeo) {
		super(domeo);
	}
	
	
	/**
	 * The search on the text match is NOT case sensitive.
	 */
	public boolean filterByText(MAnnotation annotation, String textSearch) {
		if(super.filterByText(annotation, textSearch)) return true;
		if(annotation instanceof Mexpertstudy_pDDIAnnotation) {
			return ((Mexpertstudy_pDDIAnnotation)annotation).getText().toLowerCase()
					.contains(textSearch.toLowerCase());
		}
		return false;
	}
}
