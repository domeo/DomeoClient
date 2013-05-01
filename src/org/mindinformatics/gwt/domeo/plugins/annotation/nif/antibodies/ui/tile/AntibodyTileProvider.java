package org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.ui.tile;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileGenerator;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibodyAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AntibodyTileProvider implements ITileGenerator {

	// By contract 
	private IDomeo _domeo;
	
	public AntibodyTileProvider(IDomeo domeo) {
		_domeo = domeo;
	} 
	
	@Override
	public boolean isTileSupported(String annotationName) {
		if(annotationName.equals(MAntibodyAnnotation.class.getName())) return true;
		return false;
	}

	@Override
	public ITileComponent getTile(String annotationName, IAnnotationEditListener listener) {
		if(isTileSupported(annotationName)) return new TAntibodyTile(_domeo, listener);
		return null;
	}
}
