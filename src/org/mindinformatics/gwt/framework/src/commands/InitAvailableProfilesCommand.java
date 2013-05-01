package org.mindinformatics.gwt.framework.src.commands;

import org.mindinformatics.gwt.framework.component.profiles.src.IProfileManager;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class InitAvailableProfilesCommand implements ICommand {

	IApplication _application;
	InitProfileManagerCommandCallback _callback;
	ICommandCompleted _completionCallback;
	
	public InitAvailableProfilesCommand(IApplication application, InitProfileManagerCommandCallback callback,  
			ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_application = application;
		_callback = callback;
		
	}
	
	@Override
	public void execute() {
		_application.getInitializer()
			.updateMessage("Initializing user available profiles info...");
		_application.getLogger().debug(this.getClass().getName(), 
			"Initializing user available profiles info...");
		
		IProfileManager profileManager = _callback.selectProfileManager(_completionCallback);
		if(profileManager!=null) {
			profileManager.retrieveUserProfiles();
			// Stage completion notified by asynchronous service
		} else {
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
