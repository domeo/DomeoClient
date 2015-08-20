package org.mindinformatics.gwt.framework.src.commands;

import org.mindinformatics.gwt.framework.component.profiles.src.IProfileManager;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * However if the global
 * variable 'allProfilesAvailable' is set as true in the page loading
 * Domeo, all the available profiles will be made available to the user.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class InitUserAvailableProfilesCommand implements ICommand {

	IApplication _application;
	InitProfileManagerCommandCallback _callback;
	ICommandCompleted _completionCallback;
	
	public InitUserAvailableProfilesCommand(IApplication application, InitProfileManagerCommandCallback callback,  
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
			profileManager.retrieveAndCacheUserAvailableProfiles();
		} else {
			_application.getInitializer().addException("Profile manager not initialized!");
		}
	}
}
