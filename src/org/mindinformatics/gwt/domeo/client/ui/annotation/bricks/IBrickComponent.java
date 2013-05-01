package org.mindinformatics.gwt.domeo.client.ui.annotation.bricks;

import org.mindinformatics.gwt.domeo.model.MAnnotation;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IBrickComponent {

	public void initializeBrick(MAnnotation annotation);
	public Widget getBrick();
	public void refresh();
}
