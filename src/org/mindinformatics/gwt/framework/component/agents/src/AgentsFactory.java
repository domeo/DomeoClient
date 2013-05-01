package org.mindinformatics.gwt.framework.component.agents.src;

import org.mindinformatics.gwt.framework.component.agents.model.JsoAgent;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentDatabase;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentGroup;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentOrganization;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;
import org.mindinformatics.gwt.framework.model.agents.IAgent;
import org.mindinformatics.gwt.framework.model.agents.IDatabase;
import org.mindinformatics.gwt.framework.model.agents.IPerson;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

import com.google.gwt.user.client.Window;

public class AgentsFactory {

	public MAgentGroup createGroup(String url, String name, String description) {
		MAgentGroup g = new MAgentGroup();
		g.setUrl(url);
		g.setName(name);
		g.setDescription(description);
		return g;
	}
	
	public MAgentGroup createGroup(String url, String name, String description, String homepage) {
		MAgentGroup g = new MAgentGroup();
		g.setUrl(url);
		g.setName(name);
		g.setDescription(description);
		g.setHomepage(homepage);
		return g;
	}
	
	public MAgentPerson createAgentPerson(String url, String email, String title, 
			String name, String firstName, String middleName, 
			String lastName, String picture) {
		MAgentPerson a = new MAgentPerson();
		a.setUri(url);
		a.setEmail(email);
		a.setTitle(title);
		a.setName(name);
		a.setFirstName(firstName);
		a.setMiddleName(middleName);
		a.setLastName(lastName);
		a.setPicture(picture);
		return a;
	}
	
	public IAgent createAgent(JsoAgent agent) {
		if(getObjectType(agent).equals(IPerson.TYPE)) {
			MAgentPerson a = new MAgentPerson();
			a.setUri(agent.getUri());
			a.setEmail(agent.getEmail());
			a.setTitle(agent.getTitle());
			a.setName(agent.getName());
			a.setFirstName(agent.getFirstName());
			a.setMiddleName(agent.getMiddleName());
			a.setLastName(agent.getLastName());
			a.setPicture(agent.getPicture());
			return a;
		} else if(getObjectType(agent).equals(ISoftware.TYPE)) {
			MAgentSoftware s = new MAgentSoftware();
			s.setUri(agent.getUri());
			s.setName(agent.getName());
			s.setHomepage(agent.getHomepage());
			s.setVersion(agent.getVersion());
			s.setBuild(agent.getBuild());
			return s;
		} else if(getObjectType(agent).equals(IDatabase.TYPE)) {
			MAgentDatabase d = new MAgentDatabase();
			d.setUri(agent.getUri());
			d.setName(agent.getName());
			d.setHomepage(agent.getHomepage());
			d.setVersion(agent.getVersion());
			return d;
		} else {
			Window.alert("To request: " + getObjectType(agent));
			return null;
		}
	}
	
	private final native String getObjectType(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;
	
	public MAgentPerson createAgentPerson(String email, String firstName, String middleName, 
			String lastName, String picture) {
		MAgentPerson a = new MAgentPerson();
		a.setEmail(email);
		a.setFirstName(firstName);
		a.setMiddleName(middleName);
		a.setLastName(lastName);
		a.setPicture(picture);
		return a;
	}
	
	public MAgentPerson createAgentPerson(String email, String firstName, String middleName, 
			String lastName) {
		MAgentPerson a = new MAgentPerson();
		a.setEmail(email);
		a.setFirstName(firstName);
		a.setMiddleName(middleName);
		a.setLastName(lastName);
		return a;
	}
	
	public MAgentSoftware createAgentSoftware(String name, String version) {
		MAgentSoftware s = new MAgentSoftware();
		s.setName(name);
		s.setVersion(version);
		return s;
	}
	
	public MAgentSoftware createAgentSoftware(String name, String homepage, String version) {
		MAgentSoftware s = new MAgentSoftware();
		s.setName(name);
		s.setHomepage(homepage);
		s.setVersion(version);
		return s;
	}
	
	public MAgentOrganization createAgentOrganizationn(String name, String homepage) {
		MAgentOrganization o = new MAgentOrganization();
		o.setName(name);
		o.setHomepage(homepage);
		return o;
	}
}
