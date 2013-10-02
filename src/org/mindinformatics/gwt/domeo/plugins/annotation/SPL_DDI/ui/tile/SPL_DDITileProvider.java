package org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.ui.tile;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileGenerator;
import org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.model.MSPL_DDIAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class SPL_DDITileProvider implements ITileGenerator {

	// By contract 
	private IDomeo _domeo;
	
	public SPL_DDITileProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isTileSupported(String annotationName) {
		if(annotationName.equals(MSPL_DDIAnnotation.class.getName())) return true;
		return false;
	}

	@Override
	public ITileComponent getTile(String annotationName, IAnnotationEditListener listener) {
		if(isTileSupported(annotationName)) return new TSPL_DDITile(_domeo, listener);
		return null;
	}
}
