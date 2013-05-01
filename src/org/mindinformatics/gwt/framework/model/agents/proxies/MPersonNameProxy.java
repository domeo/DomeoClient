package org.mindinformatics.gwt.framework.model.agents.proxies;

import java.io.Serializable;

import org.mindinformatics.gwt.framework.model.agents.MPersonName;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MPersonNameProxy extends AProxy implements Serializable,IsSerializable {
	 
    private MPersonName _personName; 
	
	public MPersonName getPersonName() { return _personName; }
	public void setPersonName(MPersonName personName) { _personName = personName; }
}
