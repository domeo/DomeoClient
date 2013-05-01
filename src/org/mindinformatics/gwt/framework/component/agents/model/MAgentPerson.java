package org.mindinformatics.gwt.framework.component.agents.model;

import java.io.Serializable;

import org.mindinformatics.gwt.framework.model.agents.IPerson;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MAgentPerson extends MAgent implements IPerson, Serializable,IsSerializable {
	
	private String title;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String picture;

    public MAgentPerson() {}
    
    protected MAgentPerson(String url, String email, String title, String name, String firstName, String lastName) {
		super();
		this.url = url;
		this.title = title;
		this.name = name;
		this.firstName = firstName;
		this.lastName = lastName;
	}
    
    public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
	public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
	public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public String getPicture() { return picture; }
	public void setPicture(String picture) { this.picture = picture; }

	@Override
	public String getAgentType() {
		return TYPE;
	}
}
