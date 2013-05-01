package org.mindinformatics.gwt.framework.src.commands;

import org.mindinformatics.gwt.framework.component.agents.src.IAgentManager;
import org.mindinformatics.gwt.framework.component.agents.src.defaults.DefaultAgentManager;
import org.mindinformatics.gwt.framework.component.users.src.HTMLUserUtils;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class InitSoftwareCommand implements ICommand {

	IApplication _application;
	InitAgentManagerCommandCallback _callback;
	ICommandCompleted _completionCallback;
	
	public InitSoftwareCommand(IApplication application, InitAgentManagerCommandCallback callback,  
			ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_application = application;
		_callback = callback;	
	}
	
	@Override
	public void execute() {
		_application.getInitializer()
			.updateMessage("Initializing software info...");
		_application.getLogger().debug(this.getClass().getName(), 
			"Initializing software info...");
		
		IAgentManager agentManager = _callback.selectAgentManager(_completionCallback);
		if(agentManager!=null) {
			agentManager.retrieveSoftware(HTMLUserUtils.getServerName(), _application.getApplicationVersion());
			// Stage completion notified by asynchronous service
		} else {
			_application.getLogger().debug(this.getClass().getName(), 
				"No software found, loading default agent manager");
			agentManager = new DefaultAgentManager(_application, _completionCallback);
			_callback.setAgentManager(agentManager);
			agentManager.retrieveSoftware(_application.getApplicationName(), _application.getApplicationVersion());
			agentManager.stageCompleted(); // Necessary as synchronous
		}
	}
}
