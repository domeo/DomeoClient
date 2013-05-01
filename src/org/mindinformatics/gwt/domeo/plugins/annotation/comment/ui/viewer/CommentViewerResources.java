package org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.viewer;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface CommentViewerResources extends ClientBundle {

	@NotStrict
	@Source("org/mindinformatics/gwt/domeo/plugins/annotation/comment/ui/viewer/CommentViewer.css")
	CommentViewerCssResources css();
	
	public interface CommentViewerCssResources extends CssResource {

		@ClassName("domeo_commentFrame")
		String commentFrame();
		
		@ClassName("domeo_titleFrame")
		String titleFrame();
	}
}
