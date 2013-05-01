package org.mindinformatics.gwt.framework.model.agents;

public interface ISoftware extends IAgent {

	public static final String TYPE = "foafx:Software";
	
	public String getVersion();
	public String getBuild();
	
	public String getAgentType();
}
