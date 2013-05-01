package org.mindinformatics.gwt.domeo.client.ui.toolbar.addressbar;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;

public interface AddressBarResources extends ClientBundle {
	
	@NotStrict
	@Source("AddressBarPanel.css")
	AddressBarCssResource addressBarCss();
	
	public interface AddressBarCssResource extends CssResource {
		@ClassName("fw-Panel")
		String addressBarPanel();
		
		@ClassName("fw-Button")
		String button();
		
		@ClassName("fw-AddressTextfield")
		String addressTextField();
	}
	
	@Source("load.gif")
	ImageResource load();

	@Source("left-address-bar.gif")
	ImageResource leftSideAddressBar();
	
	@Source("right-address-bar.gif")
	ImageResource rightSideAddressBar();
}