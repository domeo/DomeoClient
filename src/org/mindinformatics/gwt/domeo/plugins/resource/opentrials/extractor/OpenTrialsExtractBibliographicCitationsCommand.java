package org.mindinformatics.gwt.domeo.plugins.resource.opentrials.extractor;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;
import org.mindinformatics.gwt.framework.src.commands.InitUserManagerCommandCallback;

public class OpenTrialsExtractBibliographicCitationsCommand implements ICommand {

	IDomeo _application;
	InitUserManagerCommandCallback _callback;
	ICommandCompleted _completionCallback;
	
	public OpenTrialsExtractBibliographicCitationsCommand(IDomeo application, InitUserManagerCommandCallback callback,  
			ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_application = application;
		_callback = callback;
		
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub	
		_completionCallback.notifyStageCompletion(); 
	}
}
