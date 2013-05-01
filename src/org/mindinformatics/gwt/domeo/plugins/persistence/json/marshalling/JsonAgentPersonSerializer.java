package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Person JSON serializer. 
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonAgentPersonSerializer extends JsonAgentSerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MAgentPerson person = (MAgentPerson) obj;
		JSONObject jsonPerson = initializeAgent(manager, person);
		jsonPerson.put(IFoafxOntology.title, new JSONString(person.getTitle()));
		jsonPerson.put(IFoafxOntology.email, new JSONString(person.getEmail()));
		jsonPerson.put(IFoafxOntology.firstname, new JSONString(person.getFirstName()));
		jsonPerson.put(IFoafxOntology.middlename, new JSONString(person.getMiddleName()));
		jsonPerson.put(IFoafxOntology.lastname, new JSONString(person.getLastName()));
		jsonPerson.put(IFoafxOntology.picture, new JSONString(person.getPicture()));
		return jsonPerson;
	}
}