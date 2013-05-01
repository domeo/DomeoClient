package org.mindinformatics.gwt.domeo.client.ui.annotation.tiles;

import org.mindinformatics.gwt.domeo.model.MAnnotation;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ITileComponent  {

	public void initializeLens(MAnnotation annotation);
	public Widget getTile();
	public void refresh();
}
