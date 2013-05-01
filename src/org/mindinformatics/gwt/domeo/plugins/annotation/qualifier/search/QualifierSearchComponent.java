package org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.search;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.search.ASearchComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class QualifierSearchComponent extends ASearchComponent {

	public QualifierSearchComponent(IDomeo domeo) {
		super(domeo);
	}
	
	public boolean filterByText(MAnnotation annotation, String textSearch) {
		if(annotation instanceof MQualifierAnnotation) {
			boolean hit = false;
			for(MLinkedResource term : ((MQualifierAnnotation)annotation).getTerms()) {
				if(term.getLabel().contains(textSearch)) {
					hit = true;
					break;
				}
			}
			return hit;
		}
		return false;
	}
}
