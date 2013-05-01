package org.mindinformatics.gwt.framework.component.agents.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MAgentGroup extends MAgent implements Serializable, IsSerializable {
	
	private String description;
	
	public MAgentGroup() {}
	
	public MAgentGroup(String url, String name, String description) {
		super();
		this.url = url;
		this.name = name;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String getAgentType() {
		return "foafx:Group";
	}
}
