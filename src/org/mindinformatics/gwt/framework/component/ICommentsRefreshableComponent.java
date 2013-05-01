package org.mindinformatics.gwt.framework.component;

import java.util.List;

import org.mindinformatics.gwt.domeo.model.MAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ICommentsRefreshableComponent {
	public void refreshFromRoot();
	public void refresh(List<MAnnotation> annotations);
}
