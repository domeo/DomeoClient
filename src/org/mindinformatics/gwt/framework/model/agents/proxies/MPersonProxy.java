package org.mindinformatics.gwt.framework.model.agents.proxies;

import java.io.Serializable;
import java.util.Date;

import org.mindinformatics.gwt.framework.component.agents.model.MAgent;
import org.mindinformatics.gwt.framework.model.agents.IPerson;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class MPersonProxy implements Serializable, IsSerializable {

	private String _id;
    private Date _createdOn;
    private MAgent _creator;
	private IPerson _personProxy;
	
	// IPerson methods
	public String getFirstName() { return _personProxy.getFirstName(); }
	public String getLastName() { return _personProxy.getLastName(); }
	public String getMiddleName() { return _personProxy.getMiddleName(); }
	public void setPerson(IPerson personProxy) { _personProxy = personProxy; }
	public IPerson getPerson() { return _personProxy; }
	
	// Proxies getters
	public MPersonNameProxy getPersonNameProxy() { 
		if(_personProxy instanceof MPersonNameProxy) 
			return (MPersonNameProxy) _personProxy; 
		return null;
	}
	public MAgentPersonProxy getPersonProxy() { 
		if(_personProxy instanceof MAgentPersonProxy) 
			return (MAgentPersonProxy) _personProxy; 
		return null;
	}
	
	// Standard getter and setters
	public String getId() { return _id; }
	public void setId(String id) { _id = id; }
	public Date getCreatedOn() { return _createdOn; }
	public void setCreatedOn(Date createdOn) { _createdOn = createdOn; }
	public MAgent getCreator() { return _creator; }
	public void setCreator(MAgent creator) { _creator = creator; }	
}
