package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.framework.component.agents.model.JsoAgent;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonJsoAgentSerializer extends ASerializer implements ISerializer {

	DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss Z");
	
	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		JsoAgent agent = (JsoAgent) obj;
		JSONObject json = new JSONObject();
		json.put(IDomeoOntology.generalId, new JSONString(agent.getUri()!=null?agent.getUri():""));
		json.put(IDomeoOntology.generalType, new JSONString(agent.getType()));
		
		json.put(IRdfsOntology.rdfLabel, new JSONString(agent.getName()!=null?agent.getName():""));	
		json.put(IFoafxOntology.name, new JSONString(agent.getName()!=null?agent.getName():""));
		json.put(IFoafxOntology.homepage, new JSONString(agent.getHomepage()!=null?agent.getHomepage():""));
		json.put(IDomeoOntology.uuid, new JSONString(agent.getUuid()!=null?agent.getUuid():"<unknown-fix>"));

		if(getObjectType(agent).equals("foafx:Person")) {
			json.put(IFoafxOntology.title, new JSONString(agent.getTitle()));
			json.put(IFoafxOntology.email, new JSONString(agent.getEmail()));
			json.put(IFoafxOntology.firstname, new JSONString(agent.getFirstName()));
			json.put(IFoafxOntology.middlename, new JSONString(agent.getMiddleName()));
			json.put(IFoafxOntology.lastname, new JSONString(agent.getLastName()));
			json.put(IFoafxOntology.picture, new JSONString(agent.getPicture()));
		} else if(getObjectType(agent).equals("foafx:Software")) {
			json.put(IFoafxOntology.version, new JSONString(agent.getVersion()));
			json.put(IFoafxOntology.build, new JSONString(agent.getBuild()));
		}	
		return json;
	}
	
	private final native String getObjectType(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;

	@Override
	public JSONObject serializeWhatChanged(JsonSerializerManager manager,
			Object obj) {
		return new JSONObject();
	}
}
