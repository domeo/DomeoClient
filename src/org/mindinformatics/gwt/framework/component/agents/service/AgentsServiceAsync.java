package org.mindinformatics.gwt.framework.component.agents.service;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>UsersService</code>.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface AgentsServiceAsync {
	void  getSoftwareInfo(String name, String version,  AsyncCallback<MAgentSoftware> callback)
			throws IllegalArgumentException;
	
	void getPersonInfo(String email, AsyncCallback<MAgentPerson> callback) 
			throws IllegalArgumentException;
}

