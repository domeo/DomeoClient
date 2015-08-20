package org.mindinformatics.gwt.framework.src.commands;

import org.mindinformatics.gwt.framework.component.profiles.src.IProfileManager;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class InitUserCurrentProfileCommand implements ICommand {

	IApplication _application;
	InitProfileManagerCommandCallback _callback;
	ICommandCompleted _completionCallback;
	
	public InitUserCurrentProfileCommand(IApplication application, InitProfileManagerCommandCallback callback,  
			ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_application = application;
		_callback = callback;
		
	}
	
	@Override
	public void execute() {
		_application.getInitializer()
			.updateMessage("Initializing user profile info...");
		_application.getLogger().debug(this.getClass().getName(), 
			"Initializing user profile info...");
		
		IProfileManager profileManager = _callback.selectProfileManager(_completionCallback);
		if(profileManager!=null) {
			profileManager.retrieveAndCacheUserCurrentProfile();
			// Stage completion notified by asynchronous service
		} else {
			_application.getInitializer().addException("Profile manager not initialized!");
		}
	}
}
