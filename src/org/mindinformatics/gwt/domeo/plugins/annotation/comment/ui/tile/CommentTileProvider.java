package org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.tile;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileGenerator;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CommentTileProvider implements ITileGenerator {

	// By contract 
	private IDomeo _domeo;
	
	public CommentTileProvider(IDomeo domeo) {
		_domeo = domeo;
	} 
	
	@Override
	public boolean isTileSupported(String annotationName) {
		if(annotationName.equals(MCommentAnnotation.class.getName())) return true;
		return false;
	}

	@Override
	public ITileComponent getTile(String annotationName, IAnnotationEditListener listener) {
		if(isTileSupported(annotationName)) return new TCommentTile(_domeo, listener);
		return null;
	}
}
