package org.mindinformatics.gwt.framework.component.reporting.src.testing;

import org.mindinformatics.gwt.framework.component.reporting.src.AReportsManager;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.user.client.Window;

public class StandaloneReportManager extends AReportsManager {

	public StandaloneReportManager(IApplication application, ICommandCompleted callbackCompleted) {
		super(application, callbackCompleted);
	}

	@Override
	public String recordPathEntry(String sourceFrom, String sourceTo) {
		Window.alert("Recording path from " + sourceFrom + " to " + sourceTo);
		return null;
	}
}
