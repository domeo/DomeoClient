package org.mindinformatics.gwt.domeo.client.ui.annotation.tiles;

import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationTailsManager {

	private HashMap<String, ITileGenerator> 
		annotationTiles = new HashMap<String, ITileGenerator>();
	
	public boolean registerAnnotationTile(String annotationName, ITileGenerator tileGenerator) {
		if(!annotationTiles.containsKey(annotationName)) {
			annotationTiles.put(annotationName, tileGenerator);
			return true;
		}
		return false;
	}

	public ITileComponent getAnnotationTile(String annotationName, IAnnotationEditListener listener) {
		if(annotationTiles.containsKey(annotationName)) {
			return annotationTiles.get(annotationName).getTile(annotationName, listener);
		} else
			return null; //TODO define a default tail
	}
}
