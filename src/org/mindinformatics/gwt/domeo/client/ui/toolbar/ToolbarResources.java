package org.mindinformatics.gwt.domeo.client.ui.toolbar;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ToolbarResources  extends ClientBundle {

	@NotStrict
	@Source("org/mindinformatics/gwt/domeo/client/ui/toolbar/ToolbarPanel.css")
	ToolbarCssResource toolbarCss();
	
	public interface ToolbarCssResource extends CssResource {

		@ClassName("fw_Panel")
		String toolbarPanel();
		
		@ClassName("fw_RightPanel")
		String toolbarRightPanel();
	}
}
