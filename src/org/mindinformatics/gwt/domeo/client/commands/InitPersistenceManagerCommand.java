package org.mindinformatics.gwt.domeo.client.commands;

import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IPersistenceManager;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class InitPersistenceManagerCommand implements ICommand {

	IApplication _application;
	InitPersistenceManagerCommandCallback _callback;
	ICommandCompleted _completionCallback;
	
	public InitPersistenceManagerCommand(IApplication application, InitPersistenceManagerCommandCallback callback,  
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
		
		IPersistenceManager persistenceManager = _callback.selectPersistenceManager(_completionCallback);
		if(persistenceManager!=null) {
			//_callback.setProfileManager(profileManager);

			//profileManager.retrieveUserProfiles();
			//profileManager.retrieveUserCurrentProfile();
			

			_application.getLogger().debug(this.getClass().getName(), 
				"Persistence manager selected " + persistenceManager.getClass().getName());
			
			persistenceManager.stageCompleted();
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
