package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui;

import org.mindinformatics.gwt.domeo.client.Resources;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;

public interface IMpResources extends Resources {

	@NotStrict
	@Source("org/mindinformatics/gwt/domeo/plugins/annotation/micropubs/ui/MicroPublications.css")
	PluginCssResource pluginCss();
	
	public interface PluginCssResource extends CssResource {

		@ClassName("af-curationPopup")
		String curationPopup();
	}
	
	@Source("org/mindinformatics/gwt/domeo/plugins/annotation/micropubs/ui/icons/double-arrow-green.gif")
	ImageResource mpSupportiveIcon();
	
	@Source("org/mindinformatics/gwt/domeo/plugins/annotation/micropubs/ui/icons/double-arrow-red.gif")
	ImageResource mpChallengingIcon();
}
