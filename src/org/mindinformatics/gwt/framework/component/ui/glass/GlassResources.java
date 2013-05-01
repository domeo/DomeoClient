package org.mindinformatics.gwt.framework.component.ui.glass;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface GlassResources extends ClientBundle {

	@NotStrict
	@Source("org/mindinformatics/gwt/framework/component/ui/stylesheets/Glass.css")
	GlassCssResource glassCss();
	
	public interface GlassCssResource extends CssResource {
		@ClassName("fw_GlassPanel")
		String glassPanel();
	}
	
	@Source("org/mindinformatics/gwt/framework/icons/cross16x16.png")
	ImageResource crossLittleIcon();
	
	/*
	@Source("org/mindinformatics/gwt/annotator/icons/ui/information.gif")
	ImageResource info();
	
	@Source("org/mindinformatics/gwt/annotator/icons/ui/preview.png")
	ImageResource viewIcon();
	*/
}
