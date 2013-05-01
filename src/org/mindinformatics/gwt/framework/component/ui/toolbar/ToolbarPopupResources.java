package org.mindinformatics.gwt.framework.component.ui.toolbar;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ToolbarPopupResources extends ClientBundle {

	@NotStrict
	@Source("org/mindinformatics/gwt/framework/component/ui/toolbar/ToolbarPopup.css")
	PopupCssResource popupCss();
	
	public interface PopupCssResource extends CssResource {
		@ClassName("fw_ToolbarPopupTitlePanel")
		String toolbarPopupTitlePanel();
		
		@ClassName("fw_ToolbarPopupTitleLabel")
		String toolbarPopupTitleLabel();
		
		@ClassName("fw_ToolbarPopupButtonPanel")
		String toolbarPopupButtonPanel();
		
		@ClassName("fw_ToolbarPopupButtonLabel")
		String toolbarPopupButtonLabel();
	}
	
	/*
	@Source("org/mindinformatics/gwt/annotator/icons/ui/information.gif")
	ImageResource info();
	
	@Source("org/mindinformatics/gwt/annotator/icons/ui/preview.png")
	ImageResource viewIcon();
	*/
}
