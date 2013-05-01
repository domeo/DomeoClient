package org.mindinformatics.gwt.framework.model.agents.proxies;

import java.io.Serializable;
import java.util.Date;

import org.mindinformatics.gwt.framework.component.agents.model.MAgent;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class AProxy implements Serializable, IsSerializable {

	private String _id;
    private Date _createdOn;
    private MAgent _creator;
    
	public String getId() { return _id; }
	public void setId(String id) { _id = id; }
	public Date getCreatedOn() { return _createdOn; }
	public void setCreatedOn(Date createdOn) { _createdOn = createdOn; }
	public MAgent getCreator() { return _creator; }
	public void setCreator(MAgent creator) { _creator = creator; }
}
