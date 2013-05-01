package org.mindinformatics.gwt.framework.widget;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

public interface WidgetUtilsResources extends ClientBundle {

	@NotStrict
	@Source("org/mindinformatics/gwt/framework/widget/WidgetUtils.css")
	WidgetCssResource widgetCss();
	
	public interface WidgetCssResource extends CssResource {
		@ClassName("fw_TableOddRow")
		String tableOddRow();
		
		@ClassName("fw_TableEvenRow")
		String tableEvenRow();
		
		@ClassName("fw_curationButtons")
		String curationButton();
		
		@ClassName("fw_curationButtonsBold")
        String curationButtonBold();
		
	    @ClassName("fw_curationButtonsRed")
	    String curationButtonRed();
		
		@ClassName("fw_curationButtonsBoldRed")
	    String curationButtonBoldRed();
		
        @ClassName("fw_curationButtonsGreen")
        String curationButtonGreen();		
		
		@ClassName("fw_curationButtonsBoldGreen")
        String curationButtonBoldGreen();
	}
}
