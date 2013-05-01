package org.mindinformatics.gwt.framework.src.commands;

import org.mindinformatics.gwt.framework.component.agents.src.IAgentManager;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface InitAgentManagerCommandCallback {

	public boolean isAgentManagerDefined();
	public IAgentManager getAgentManager();
	public void setAgentManager(IAgentManager agentManager);
	public IAgentManager selectAgentManager(ICommandCompleted completionCallback);
}
