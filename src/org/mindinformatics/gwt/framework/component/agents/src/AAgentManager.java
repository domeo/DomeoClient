package org.mindinformatics.gwt.framework.component.agents.src;

import java.util.HashMap;

import org.mindinformatics.gwt.framework.component.agents.model.JsoAgent;
import org.mindinformatics.gwt.framework.model.agents.IAgent;
import org.mindinformatics.gwt.framework.model.agents.IPerson;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public  abstract class AAgentManager implements IAgentManager {

	protected ISoftware _software;
	protected IPerson _userPerson;

	protected HashMap<String, IAgent> agents = new HashMap<String, IAgent>();
	
	protected IApplication _application;
	private ICommandCompleted _callback;
	
	public AAgentManager(IApplication application, ICommandCompleted callback) {
		_application = application;
		_callback = callback;
	}
	
	public String getNumberOfAgents() {
		return Integer.toString(agents.size());
	}
	
	private final native String getObjectId(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
	
	public void addAgent(JsoAgent agent) {
		AgentsFactory factory = new AgentsFactory();
		String id = getObjectId(agent);
		if(!agents.containsKey(id)) {
			IAgent a = factory.createAgent(agent);
			agents.put(a.getUri(), a);
		}
		_application.getLogger().debug(this, "Added agent " + agent.getName() + " (total agents " + agents.size() + ")");
	}
	
	public void addAgent(IAgent agent) {
		agents.put(agent.getUri(), agent);
		_application.getLogger().debug(this, "Added agent " + agent.getName() + " (total agents " + agents.size() + ")");
	}
	
	public IAgent getAgentByUri(String uri) {
		if(_userPerson.getUri().equals(uri)) return _userPerson;
		if(_software.getUri().equals(uri)) return _software;
		return agents.get(uri);
	}
	
	protected void setSoftware(ISoftware software) {
		_application.getInitializer().addCompletionMessage("Agent Software initialization... " + software.getName());
		_application.getLogger().debug(this, "Added software " + software.getVersion() + "-"  + software.getUri() + "-" + software.getName() + " (total agents " + agents.size() + ")");
		_software = software;
	}
	
	public ISoftware getSoftware() {
		return _software;
	}
	
	protected void setUserPerson(IPerson userPerson) {
		_application.getInitializer().addCompletionMessage("Agent Person initialization... " + userPerson.getName());
		_application.getLogger().debug(this, "Added person " + userPerson.getUri() + "-" + userPerson.getName() + " (total agents " + agents.size() + ")");
		_userPerson = userPerson;
	}
	
	public IPerson getUserPerson() {
		return _userPerson;
	}
	
	public void stageCompleted() {
		_callback.notifyStageCompletion();
	}
}
