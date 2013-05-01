package org.mindinformatics.gwt.framework.src.commands;

import org.mindinformatics.gwt.framework.component.users.src.HTMLUserUtils;
import org.mindinformatics.gwt.framework.component.users.src.IUserManager;
import org.mindinformatics.gwt.framework.component.users.src.defaults.DefaultUserManager;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class InitUserInfoCommand implements ICommand {

	IApplication _application;
	InitUserManagerCommandCallback _callback;
	ICommandCompleted _completionCallback;
	
	public InitUserInfoCommand(IApplication application, InitUserManagerCommandCallback callback,  
			ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_application = application;
		_callback = callback;
	}
	
	@Override
	public void execute() {
		_application.getInitializer()
			.updateMessage("Initializing user info...");
		_application.getLogger().debug(this.getClass().getName(), 
			"Initializing user info...");
		
		IUserManager userManager = _callback.selectUserManager(_completionCallback);
		if(userManager!=null) {
			if (HTMLUserUtils.doesUsernameExist()) {
				userManager.retrieveUser(HTMLUserUtils.getUsername());
			} else {
				userManager.retrieveUser("");
			}
			// Stage completion notified by asynchronous service
		} else {
			_application.getLogger().debug(this.getClass().getName(), "No user manager found, loading default user manager");
			_application.getInitializer().addMessage("No user manager found, loading default user manager");
			userManager = new DefaultUserManager(_application, _completionCallback);
			_callback.setUserManager(userManager);
			userManager.retrieveUser("");
			userManager.stageCompleted(); // Necessary as synchronous
		}
	}
}
