package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;

import com.google.gwt.json.client.JSONObject;

/**
 * Generic Software JSON serializer. 
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonAgentSoftwareSerializer extends JsonAgentSerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {	
		MAgentSoftware software = (MAgentSoftware) obj;
		JSONObject jsonSoftware = initializeAgent(manager, software);
		jsonSoftware.put(IFoafxOntology.version, nullable(software.getVersion()));
		jsonSoftware.put(IFoafxOntology.build, nullable(software.getBuild()));
		return jsonSoftware;
	}
}