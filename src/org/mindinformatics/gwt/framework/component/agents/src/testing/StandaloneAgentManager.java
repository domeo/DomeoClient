package org.mindinformatics.gwt.framework.component.agents.src.testing;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;
import org.mindinformatics.gwt.framework.component.agents.src.AAgentManager;
import org.mindinformatics.gwt.framework.component.agents.src.AgentsFactory;
import org.mindinformatics.gwt.framework.component.agents.src.defaults.DefaultPerson;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class StandaloneAgentManager extends AAgentManager {

	public StandaloneAgentManager(IApplication application, ICommandCompleted callbackCompleted) {
		super(application, callbackCompleted);
	}
	
	@Override
	public void retrieveSoftware(String name, String version) {
		AgentsFactory agentsFactory = new AgentsFactory();
		MAgentSoftware software = agentsFactory.createAgentSoftware(name, version);
		setSoftware(software);
		stageCompleted();
	}
	
	@Override
	public void retrievePerson(String username) {
		AgentsFactory agentsFactory = new AgentsFactory();
		MAgentPerson person = null;
		if(username.equals("paolo.ciccarese")) {
			person = agentsFactory.createAgentPerson(
				"http://www.commonsemantics.com/account/paolociccarese", 
				"paolo.ciccarese@gmail.com", "Dr.", "Paolo Ciccarese", "Paolo", "Nunzio", "Ciccarese", 
				"http://www.hcklab.org/images/me/paolo%20ciccarese-boston.jpg");
		} else {
			person = new DefaultPerson();
		}
		setUserPerson(person);
		stageCompleted();
	}
}
