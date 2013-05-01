package org.mindinformatics.gwt.framework.model.agents;

public interface IDatabase extends IAgent {

	public static final String TYPE = "foafx:Database";
	
	public String getVersion();
	
	public String getAgentType();
}
