package org.mindinformatics.gwt.framework.src.commands;

import org.mindinformatics.gwt.framework.component.reporting.src.IReportsManager;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class InitReportCommand implements ICommand {

	IApplication _application;
	InitReportManagerCommandCallback _callback;
	ICommandCompleted _completionCallback;
	
	public InitReportCommand(IApplication application, InitReportManagerCommandCallback callback,  
			ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_application = application;
		_callback = callback;
		
	}
	
	@Override
	public void execute() {
		_application.getInitializer()
			.updateMessage("Initializing report manager...");
		_application.getLogger().debug(this.getClass().getName(), 
			"Initializing report manager...");
		
		IReportsManager reportManager = _callback.selectReportManager(_completionCallback);
		if(reportManager!=null) {
			//reportManager.retrieveUserProfiles();
			//profileManager.retrieveUserCurrentProfile();

			reportManager.stageCompleted();
		} else {
			Window.alert("InitReportCommand.execute() else");
			/*
			_application.getLogger().debug(this.getClass().getName(), 
			"No user manager found, loading default user manager");
			profileManager = new DefaultUserManager(_application, _completionCallback);
			_callback.setUserManager(profileManager);
			profileManager.retrieveUser("");
			profileManager.stageCompleted(); // Necessary as synchronous
			*/
		}
	}
}
