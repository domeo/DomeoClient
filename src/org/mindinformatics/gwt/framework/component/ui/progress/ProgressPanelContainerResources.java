package org.mindinformatics.gwt.framework.component.ui.progress;

import org.mindinformatics.gwt.framework.component.ui.glass.GlassResources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ProgressPanelContainerResources extends ClientBundle, GlassResources {

	@NotStrict
	@Source("org/mindinformatics/gwt/framework/component/ui/stylesheets/ProgressPanel.css")
	ProgressPanelCssResource progressGlassPanelCss();
	
	public interface ProgressPanelCssResource extends CssResource {
		@ClassName("domeo_panel")
		String progressPanel();
	}
}
