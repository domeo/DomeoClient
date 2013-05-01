package org.mindinformatics.gwt.framework.component.agents.src.defaults;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class DefaultPerson extends MAgentPerson  {

	public DefaultPerson() {
		super("http://www.example.com/defaultperson", "guest@example.com", "Mr.", "Guest", "John", "Doe");
	}
}
