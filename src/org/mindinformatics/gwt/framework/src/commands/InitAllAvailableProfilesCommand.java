package org.mindinformatics.gwt.framework.src.commands;

import org.mindinformatics.gwt.framework.component.profiles.src.IProfileManager;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;
import org.mindinformatics.gwt.framework.src.Utils;

/**
 * However if the global
 * variable 'allProfilesAvailable' is set as true in the page loading
 * Domeo, all the available profiles will be made available to the user.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class InitAllAvailableProfilesCommand implements ICommand {

	IApplication _application;
	InitProfileManagerCommandCallback _callback;
	ICommandCompleted _completionCallback;
	
	public InitAllAvailableProfilesCommand(IApplication application, InitProfileManagerCommandCallback callback,  
			ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_application = application;
		_callback = callback;		
	}
	
	@Override
	public void execute() {
		_application.getInitializer().updateMessage("Initializing all available profiles info...");
		_application.getLogger().debug(this.getClass().getName(), "Initializing all available profiles info...");
		
		IProfileManager profileManager = _callback.selectProfileManager(_completionCallback);
		if(profileManager!=null) {
			if(profileManager.getUserProfiles().size()==0 && Boolean.TRUE.toString().toLowerCase().equals(Utils.getAllProfilesAvailable().toLowerCase())) {
				profileManager.retrieveAndCacheAllProfiles();
			} else {
				_application.getInitializer().updateMessage("Skipped all available profiles info.");
				_application.getLogger().debug(this.getClass().getName(), "Skipped all available profiles info.");
				_completionCallback.notifyStageCompletion();
			}
		} else {
			_application.getInitializer().addException("Profile manager not initialized!");
		}
	}
}
