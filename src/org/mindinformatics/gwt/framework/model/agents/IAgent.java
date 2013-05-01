package org.mindinformatics.gwt.framework.model.agents;

public interface IAgent {

	public String getUri() ;
	public String getUuid();
	public String getName();
	public String getHomepage();
	
	public abstract String getAgentType();
}
