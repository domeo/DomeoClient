package org.mindinformatics.gwt.framework.model.agents.proxies;

import java.io.Serializable;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MAgentPersonProxy extends AProxy implements Serializable, IsSerializable {
	
    private MAgentPerson _person;

	public MAgentPerson getPerson() { return _person; }
	public void setPerson(MAgentPerson person) {_person = person; }
}
