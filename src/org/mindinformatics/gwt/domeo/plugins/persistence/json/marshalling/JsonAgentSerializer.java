package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.framework.component.agents.model.MAgent;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Generic Agent JSON serializer. 
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonAgentSerializer extends ASerializer implements ISerializer {
	
	/**
	 * Allows to initialize the generic properties of every Agent. This method is
	 * used by all the extensions of MAgent.
	 * @param manager	The serializer manager
	 * @param agent		The Agent to serialize
	 * @return The Agent in JSON format
	 */
	protected JSONObject initializeAgent(JsonSerializerManager manager, MAgent agent) {
		JSONObject jsonAgent = new JSONObject();
		jsonAgent.put(IDomeoOntology.generalId, new JSONString(agent.getUri()!=null?agent.getUri():""));
		jsonAgent.put(IDomeoOntology.generalType, new JSONString(agent.getAgentType()));
		//jsonAgent.put(IDomeoOntology.uuid, nonNullable(agent.getUuid())); // Used???
		
		jsonAgent.put(IRdfsOntology.label, nullable(agent.getName()));	
		jsonAgent.put(IFoafxOntology.name, nullable(agent.getName()));	
		jsonAgent.put(IFoafxOntology.homepage, nullable(agent.getHomepage()));	
		return jsonAgent;
	}

	@Override
	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		JSONObject jsonAgent = initializeAgent(manager, (MAgent) obj);		
		return jsonAgent;
	}
	
	@Override
	public JSONObject serializeWhatChanged(JsonSerializerManager manager, Object obj) {
		return new JSONObject();
	}
}
