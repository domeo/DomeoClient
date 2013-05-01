package org.mindinformatics.gwt.framework.component.agents.model;

import java.io.Serializable;

import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.model.agents.IAgent;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public abstract class MAgent extends MGenericResource implements IAgent, Serializable,IsSerializable {
    
	protected String uri;
	protected String uuid;
	protected String name;
	protected String homepage;
    
	public abstract String getAgentType();
	
    public String getUri() { return uri; }
	public void setUri(String uri) { this.uri = uri; }
	public String getUuid() { return uuid; }
	public void setUuid(String uuid) { this.uuid = uuid; }
	public String getHomepage() { return homepage; }
    public void setHomepage(String homepage) { this.homepage = homepage; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
