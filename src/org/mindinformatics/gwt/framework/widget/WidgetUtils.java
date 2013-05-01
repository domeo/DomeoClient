package org.mindinformatics.gwt.framework.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

public class WidgetUtils {

	public static final WidgetUtilsResources localResources = 
		GWT.create(WidgetUtilsResources.class);
	
	public void applyDataRowStyles(FlexTable ft) {
		localResources.widgetCss().ensureInjected();
		
		for (int row = 0; row < ft.getRowCount(); ++row) {
			if ((row % 2) != 0) {
				ft.getRowFormatter().addStyleName(row,localResources.widgetCss().tableOddRow());
			} else {
				ft.getRowFormatter().addStyleName(row,localResources.widgetCss().tableEvenRow());
			}
		}
	}
	
	/*
	public void applyDataRowStyles(FixedWidthGrid ft) {
		for (int row = 0; row < ft.getRowCount(); ++row) {
			if ((row % 2) != 0) {
				ft.getRowFormatter().addStyleName(row, _resources.CssCommons().oddRow());
			} else {
				ft.getRowFormatter().addStyleName(row, _resources.CssCommons().evenRow());
			}
		}
	}
	*/
	
	public HTML emptyMessage() {
		HTML emptyMessage = new HTML("&nbsp;None");
		//emptyMessage.setStyleName(_resources.CssCommons().Tail_EmptyMessage());
		return emptyMessage;
	}
}
