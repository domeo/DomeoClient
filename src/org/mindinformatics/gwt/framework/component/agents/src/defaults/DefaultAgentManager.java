package org.mindinformatics.gwt.framework.component.agents.src.defaults;

import org.mindinformatics.gwt.framework.component.agents.src.AAgentManager;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class DefaultAgentManager extends AAgentManager {
	
	public DefaultAgentManager(IApplication application, ICommandCompleted callback) {
		super(application, callback);
	}
	
	@Override
	public void retrieveSoftware(String name, String version) {
		DefaultSoftware software = new DefaultSoftware();
		setSoftware(software);
		stageCompleted();
	}
	
	@Override
	public void retrievePerson(String email) {
		DefaultPerson person = new DefaultPerson();
		setUserPerson(person);
		stageCompleted();
	}
}
