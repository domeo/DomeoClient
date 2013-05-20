package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import java.util.List;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;
import org.mindinformatics.gwt.framework.model.agents.IAgent;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * This class provides the serialization basis for all Annotation Set in JSON format.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonAnnotationSerializer extends ASerializer implements ISerializer {

	private JSONValue serializeAgent(JsonSerializerManager manager, IAgent agent) {
		if(agent instanceof MAgentPerson) {
			MAgentPerson person = (MAgentPerson) agent;
			JSONValue json = manager.serialize(person);
			if(json!=null) return json;
		} else if(agent instanceof MAgentSoftware) {
			MAgentSoftware software = (MAgentSoftware) agent;
			JSONValue json = manager.serialize(software);
			if(json!=null) return json;
		}
		return new JSONString("");
	}
	
	/**
	 * Allows to initialize the generic properties of every Annotation. This method is
	 * used by all the extensions of Annotation.
	 * @param manager	The serializer manager
	 * @param ann		The annotation to serialize
	 * @return The Annotation in JSON format
	 */
	protected JSONObject initializeAnnotation(JsonSerializerManager manager, MAnnotation ann) {
		JSONObject jsonAnnotation = new JSONObject();
		jsonAnnotation.put(IRdfsOntology.type, new JSONString(ann.getAnnotationType()));
		
		jsonAnnotation.put(IRdfsOntology.id, nonNullable(ann.getIndividualUri()));
		jsonAnnotation.put(IDomeoOntology.transientLocalId, nonNullable(ann.getLocalId()));
		
		jsonAnnotation.put(IPavOntology.createdBy, new JSONString(ann.getCreator()!=null?ann.getCreator().getUri():""));
		//jsonAnnotation.put(IPavOntology.createdBy, serializeAgent(manager, ann.getCreator()));
		if(ann.getCreator()!=null) manager.addAgentToSerialize(ann.getCreator());
		
		jsonAnnotation.put(IPavOntology.createdOn, nonNullable(ann.getCreatedOn()));
		
		jsonAnnotation.put(IPavOntology.createdWith, new JSONString(ann.getTool()!=null?ann.getTool().getUri():""));
		//jsonAnnotation.put(IPavOntology.createdWith, serializeAgent(manager, ann.getTool()));
		if(ann.getTool()!=null) manager.addAgentToSerialize(ann.getTool());
			
		jsonAnnotation.put(IPavOntology.lastSavedOn, nullable(ann.getLastSavedOn()));
		//annotationJson.put(IPavOntology.createdWith, new JSONString(ann.getTool()!=null?ann.getTool().getUri():""));
		//if(ann.getTool()!=null) manager.addAgentToSerialize(ann.getTool());
		//annotationJson.put(IPavOntology.createdWith, new JSONString(ann.getTool()!=null?ann.getTool().getUri():""));
		
		// These translate null values into blank strings
		jsonAnnotation.put(IRdfsOntology.label, nullable(ann.getLabel()));	
		jsonAnnotation.put(IPavOntology.lineageUri, nullable(ann.getLineageUri()));
		jsonAnnotation.put(IPavOntology.versionNumber, nullable(ann.getVersionNumber()));
		jsonAnnotation.put(IPavOntology.previousVersion, nullable(ann.getPreviousVersion()));
	
		jsonAnnotation.put(IDomeoOntology.transientHasChanged, new JSONString(Boolean.toString(ann.getHasChanged())));
		jsonAnnotation.put(IDomeoOntology.transientNewVersion, new JSONString(Boolean.toString(ann.getNewVersion())));
		jsonAnnotation.put(IDomeoOntology.transientHasChanged, new JSONString(ann.getHasChanged()+"")); //???
		
		jsonAnnotation.put(IDomeoOntology.hasTarget, encodeSelectors(manager, ann));
		return jsonAnnotation;
	}
	
	/**
	 * Encodes selectors in JSON format.
	 * @param manager	The serializer manager
	 * @param ann		The annotation to serialize
	 * @return The Selectors in JSON format
	 */
	private JSONArray encodeSelectors(JsonSerializerManager manager, MAnnotation ann) {
		JSONArray jsonSelectors = new JSONArray();
		List<MSelector> selectorsList = ann.getSelectors();
		for(int i=0; i<selectorsList.size(); i++) {
			jsonSelectors.set(i, manager.serialize(selectorsList.get(i)));
		}
		return jsonSelectors;
	}
	
	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		JSONObject jsonAnnotation = initializeAnnotation(manager, (MAnnotation) obj);		
		return jsonAnnotation;
	}
	
	public JSONObject serializeWhatChanged(JsonSerializerManager manager, Object obj) {
		return serialize(manager, obj); 
	}
}
