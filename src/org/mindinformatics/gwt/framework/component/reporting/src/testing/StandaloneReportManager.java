package org.mindinformatics.gwt.framework.component.reporting.src.testing;

import org.mindinformatics.gwt.framework.component.reporting.src.AReportsManager;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

public class StandaloneReportManager extends AReportsManager {

	public StandaloneReportManager(IApplication application, ICommandCompleted callbackCompleted) {
		super(application, callbackCompleted);
	}
}
