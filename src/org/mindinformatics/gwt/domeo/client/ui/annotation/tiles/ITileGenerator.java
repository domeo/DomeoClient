package org.mindinformatics.gwt.domeo.client.ui.annotation.tiles;

import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ITileGenerator {

	public boolean isTileSupported(String annotationName);
	public ITileComponent getTile(String annotationName, IAnnotationEditListener listener);
}
