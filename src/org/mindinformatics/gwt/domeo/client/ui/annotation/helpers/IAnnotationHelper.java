package org.mindinformatics.gwt.domeo.client.ui.annotation.helpers;

import java.util.List;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IAnnotationHelper {

	public List<MLinkedResource> getTerms(MAnnotation annotation);
}
