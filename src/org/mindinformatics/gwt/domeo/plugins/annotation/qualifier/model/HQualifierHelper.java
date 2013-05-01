package org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.ui.annotation.helpers.IAnnotationHelper;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class HQualifierHelper implements IAnnotationHelper {

	public List<MLinkedResource> getTerms(MAnnotation annotation) {
		ArrayList<MLinkedResource> terms = new ArrayList<MLinkedResource>();
		terms.addAll(((MQualifierAnnotation)annotation).getTerms());
		return terms;
	}
}
