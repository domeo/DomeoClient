package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.ui.tile;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileGenerator;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class expertstudy_pDDITileProvider implements ITileGenerator {

	// By contract 
	private IDomeo _domeo;
	
	public expertstudy_pDDITileProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isTileSupported(String annotationName) {
		if(annotationName.equals(Mexpertstudy_pDDIAnnotation.class.getName())) return true;
		return false;
	}

	@Override
	public ITileComponent getTile(String annotationName, IAnnotationEditListener listener) {
		if(isTileSupported(annotationName)) return new Texpertstudy_pDDITile(_domeo, listener);
		return null;
	}
}
