package org.mindinformatics.gwt.framework.src.commands;

import org.mindinformatics.gwt.framework.component.reporting.src.IReportsManager;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface InitReportManagerCommandCallback {

	public void setReportManager(IReportsManager reportsManager);
	public IReportsManager selectReportManager(ICommandCompleted completionCallback);
}
