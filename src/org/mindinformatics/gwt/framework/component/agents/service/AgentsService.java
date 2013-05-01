package org.mindinformatics.gwt.framework.component.agents.service;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@RemoteServiceRelativePath("users")
public interface AgentsService extends RemoteService {
	MAgentSoftware getSoftwareInfo(String name, String version) throws IllegalArgumentException;
	MAgentPerson getPersonInfo(String email) throws IllegalArgumentException;
}
