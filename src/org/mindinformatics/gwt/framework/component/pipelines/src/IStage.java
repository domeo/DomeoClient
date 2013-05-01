package org.mindinformatics.gwt.framework.component.pipelines.src;

import org.mindinformatics.gwt.framework.src.ICommand;


public interface IStage {

	public ICommand getCommand();
	public void setExecutable(boolean executable);
	public boolean isExecutable();
	public void execute();
}
