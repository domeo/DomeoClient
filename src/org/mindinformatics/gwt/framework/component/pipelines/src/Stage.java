package org.mindinformatics.gwt.framework.component.pipelines.src;

import org.mindinformatics.gwt.framework.src.ICommand;

public class Stage implements IStage {

	private boolean _executable = true;
	private ICommand _command;
	
	public Stage(ICommand command) {
		_command = command;
		_executable = true;
	}
	
	public ICommand getCommand() {
		return _command;
	}
	
	public void setExecutable(boolean executable) {
		_executable = executable;
	}

	public boolean isExecutable() {
		return _executable;
	}
	
	@Override
	public void execute() {
		_command.execute();
	}

}
