package org.mindinformatics.gwt.framework.model.references;

import org.mindinformatics.gwt.domeo.model.MAnnotationReference;


/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ISelfReference {

	public void setSelfReference(MAnnotationReference citation);
	public MAnnotationReference getSelfReference();
}
