package org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.tile;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileGenerator;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class SPLsTileProvider implements ITileGenerator {

	// By contract 
	private IDomeo _domeo;
	
	public SPLsTileProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isTileSupported(String annotationName) {
		if(annotationName.equals(MSPLsAnnotation.class.getName())) return true;
		return false;
	}

	@Override
	public ITileComponent getTile(String annotationName, IAnnotationEditListener listener) {
		if(isTileSupported(annotationName)) return new TSPLsTile(_domeo, listener);
		return null;
	}
}
