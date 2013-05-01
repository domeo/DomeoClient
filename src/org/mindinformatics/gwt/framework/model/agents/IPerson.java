package org.mindinformatics.gwt.framework.model.agents;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IPerson extends IAgent {
	
	public static final String TYPE = "foafx:Person";
	
	public String getEmail();
	public String getTitle();
	public String getFirstName();
	public String getLastName();
	public String getMiddleName();
	public String getPicture();
	
	public String getAgentType();
}
