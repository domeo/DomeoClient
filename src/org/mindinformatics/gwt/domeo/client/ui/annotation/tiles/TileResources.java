package org.mindinformatics.gwt.domeo.client.ui.annotation.tiles;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface TileResources extends ClientBundle {

	@NotStrict
	@Source("org/mindinformatics/gwt/domeo/client/ui/annotation/tiles/Tiles.css")
	TileCssResource css();
	
	public interface TileCssResource extends CssResource {

		@ClassName("fw_Button")
		String button();
		
		@ClassName("fw_Inline")
		String inline();
	}
}
