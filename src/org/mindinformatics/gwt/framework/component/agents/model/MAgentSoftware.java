package org.mindinformatics.gwt.framework.component.agents.model;

import java.io.Serializable;

import org.mindinformatics.gwt.framework.model.agents.ISoftware;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MAgentSoftware extends MAgent implements ISoftware, Serializable, IsSerializable {

    private String version;
    private String build;

	public MAgentSoftware() {}
	
	protected MAgentSoftware(String url, String name, String version, String build) {
		super();
		this.url = url;
		this.uri = url;
		this.name = name;
		this.build = build;
		this.version = version;
	}
    
	public String getVersion() { return version; }
	public void setVersion(String version) { this.version = version; }
	public String getBuild() { return build; }
	public void setBuild(String build) { this.build = build; }

	public String toString() {
		return "id: " + url + ", name: " + name + ", version: " + version + ", build: " + build;
	}
	
	@Override
	public String getAgentType() {
		return TYPE;
	}
}
