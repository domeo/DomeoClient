package org.mindinformatics.gwt.framework.component.reporting.src;

import com.google.gwt.user.client.ui.Widget;

public interface IReportsManager {

	public void displayWidget(Widget widget);
	public void sendWidgetAsEmail(String sourceClass, String title, Widget message, String url);
	public String recordPathEntry(String sourceFrom, String sourceTo);
	public void stageCompleted();
}
