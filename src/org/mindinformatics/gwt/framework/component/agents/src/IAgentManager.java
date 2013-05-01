package org.mindinformatics.gwt.framework.component.agents.src;

import org.mindinformatics.gwt.framework.component.agents.model.JsoAgent;
import org.mindinformatics.gwt.framework.model.agents.IAgent;
import org.mindinformatics.gwt.framework.model.agents.IPerson;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IAgentManager {

	public ISoftware getSoftware();
	public IPerson getUserPerson();
	public void retrieveSoftware(String name, String version);
	public void retrievePerson(String email);
	public void stageCompleted();
	
	public void addAgent(JsoAgent agent);
	public void addAgent(IAgent agent);
	public IAgent getAgentByUri(String id);
	public String getNumberOfAgents();
}
