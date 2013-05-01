package org.mindinformatics.gwt.framework.component.agents.model;

import java.io.Serializable;

import org.mindinformatics.gwt.framework.model.agents.IDatabase;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MAgentDatabase extends MAgent implements IDatabase, Serializable, IsSerializable {

    private String version;

	public MAgentDatabase() {}
	
	protected MAgentDatabase(String url, String name, String homepage, String version) {
		super();
		this.uri = url;
		this.url = url;
		this.name = name;
		this.homepage = homepage;
		this.version = version;
	}
    
	public String getVersion() { return version; }
	public void setVersion(String version) { this.version = version; }
	
	public String toString() {
		return "id: " + url + ", name:" + name + ", homepage:" + homepage + ", version:" + version;
	}
	
	@Override
	public String getAgentType() { 
		return IDatabase.TYPE;
	}
}
