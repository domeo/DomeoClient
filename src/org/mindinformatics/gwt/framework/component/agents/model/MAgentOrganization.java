package org.mindinformatics.gwt.framework.component.agents.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MAgentOrganization extends MAgent implements Serializable, IsSerializable {
	
	@Override
	public String getAgentType() {
		return "foafx:Organization";
	}
}
