package org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.tile;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileGenerator;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class QualifierTileProvider implements ITileGenerator {

	// By contract 
	private IDomeo _domeo;
	
	public QualifierTileProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isTileSupported(String annotationName) {
		if(annotationName.equals(MQualifierAnnotation.class.getName())) return true;
		return false;
	}

	@Override
	public ITileComponent getTile(String annotationName, IAnnotationEditListener listener) {
		if(isTileSupported(annotationName)) return new TQualifierTile(_domeo, listener);
		return null;
	}
}
