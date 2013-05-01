package org.mindinformatics.gwt.framework.component.ui.glass;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface EnhancedGlassResources extends ClientBundle, GlassResources {

	@NotStrict
	@Source("org/mindinformatics/gwt/framework/component/ui/stylesheets/EnhancedGlass.css")
	EnhancedGlassCssResource enhancedGlassCss();
	
	public interface EnhancedGlassCssResource extends CssResource {
		@ClassName("fw_Panel")
		String enhancedGlassPanel();
		
		@ClassName("fw_PanelTitle")
		String enhancedGlassPanelTitle();
		
		@ClassName("fw_PanelContent")
		String enhancedGlassPanelContent();
		
		@ClassName("fw_PanelIcon")
		String enhancedGlassPanelIcon();
		
		@ClassName("fw_PanelIconImage")
		String enhancedGlassPanelIconImage();
	}
}
