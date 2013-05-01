package org.mindinformatics.gwt.domeo.client.ui.popup;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

public interface CurationPopupResources extends ClientBundle {

	@NotStrict
	@Source("org/mindinformatics/gwt/domeo/client/ui/popup/CurationPopup.css")
	PopupCssResource popupCss();
	
	public interface PopupCssResource extends CssResource {
		@ClassName("fw_PopupPanel")
		String popupPanel();
		
		@ClassName("fw_ScrollPanel")
		String scrollPanel();
		
		@ClassName("fw_containerPanelWithPadding")
		String containerPanelWithPadding();
	}
}
