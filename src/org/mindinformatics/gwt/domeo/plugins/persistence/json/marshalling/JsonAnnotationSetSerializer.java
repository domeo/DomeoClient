package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import java.util.List;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;
import org.mindinformatics.gwt.framework.model.agents.IAgent;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * This class serializes the Annotation Set in JSON format.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonAnnotationSetSerializer extends ASerializer implements ISerializer {

	/**
	 * Allows to initialize the generic properties of every Annotation Set. This method is
	 * used by all the extensions of Annotation Set.
	 * @param manager	The serializer manager
	 * @param ann		The Annotation Set to serialize
	 * @return The Annotation Set in JSON format
	 */
	protected JSONObject initAnnotationSetSerialization(JsonSerializerManager manager, MAnnotationSet annotationSet) {
		JSONObject annotationSetJson = new JSONObject();
		annotationSetJson.put(IRdfsOntology.type, new JSONString(annotationSet.getType()));
		
		// These have to exist and defined
		// TODO HIGH track exception when any of these is null or blank
		annotationSetJson.put(IRdfsOntology.id, new JSONString(annotationSet.getIndividualUri()));
		annotationSetJson.put(IDomeoOntology.transientLocalId, new JSONString(Long.toString(annotationSet.getLocalId())));
		annotationSetJson.put(IDomeoOntology.annotates, new JSONString(annotationSet.getTargetResource().getUrl()));
		manager.addResourceToSerialize(annotationSet.getTargetResource());
		
		
		//  Creation
		// --------------------------------------------------------------------
		if(annotationSet.getCreatedBy()!=null) {
			manager.addAgentToSerialize(annotationSet.getCreatedBy());
			annotationSetJson.put(IPavOntology.createdBy, new JSONString(annotationSet.getCreatedBy().getUri()));
			//annotationSetJson.put(IPavOntology.createdBy, serializeAgent(manager, annotationSet.getCreatedBy()));
		} else {
			// Warning/Exception?
		}

		if(annotationSet.getCreatedOn()!=null) {
			annotationSetJson.put(IPavOntology.createdOn, new JSONString(dateFormatter.format(annotationSet.getCreatedOn())));
		} else {
			// Warning/Exception?
		}
		if(annotationSet.getCreatedWith()!=null) {
			manager.addAgentToSerialize(annotationSet.getCreatedWith());
			annotationSetJson.put(IPavOntology.createdWith, new JSONString(annotationSet.getCreatedWith().getUri()));
			//annotationSetJson.put(IPavOntology.createdWith, serializeAgent(manager, annotationSet.getCreatedWith()));
		} else {
			// Warning/Exception?
		}

		if(annotationSet.getLastSavedOn()!=null) {
			annotationSetJson.put(IPavOntology.lastSavedOn, new JSONString(dateFormatter.format(annotationSet.getLastSavedOn())));
		}
		
		//  Imports
		// --------------------------------------------------------------------
		if(annotationSet.getImportedFrom()!=null) {
			manager.addAgentToSerialize(annotationSet.getImportedFrom());
			annotationSetJson.put(IPavOntology.importedFrom, new JSONString(annotationSet.getImportedFrom().getUri()));
			//annotationSetJson.put(IPavOntology.importedFrom, serializeAgent(manager, annotationSet.getImportedFrom()));
		}
		if(annotationSet.getImportedBy()!=null) {
			manager.addAgentToSerialize(annotationSet.getImportedBy());
			annotationSetJson.put(IPavOntology.importedBy, new JSONString(annotationSet.getImportedBy().getUri()));
			//annotationSetJson.put(IPavOntology.importedBy, serializeAgent(manager, annotationSet.getImportedBy()));
		}
		if(annotationSet.getImportedOn()!=null) {
			annotationSetJson.put(IPavOntology.importedOn, new JSONString(dateFormatter.format(annotationSet.getImportedOn())));
		}
		// These translate null values into blank strings
		annotationSetJson.put(IRdfsOntology.label, nullable(annotationSet.getLabel()));
		annotationSetJson.put(IDublinCoreTerms.description, nullable(annotationSet.getDescription()));
		annotationSetJson.put(IPavOntology.lineageUri, nullable(annotationSet.getLineageUri()));
		annotationSetJson.put(IPavOntology.versionNumber, nullable(annotationSet.getVersionNumber()));
		annotationSetJson.put(IPavOntology.previousVersion, nullable(annotationSet.getPreviousVersion()));
		
		return annotationSetJson;
	}
	
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
	
	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MAnnotationSet annotationSet = (MAnnotationSet) obj;
		JSONObject annotationSetJson = initAnnotationSetSerialization(manager, annotationSet);

		// Serialization of the annotation items
		JSONArray annotations = new JSONArray();
		List<MAnnotation> annotationsList = annotationSet.getAnnotations();
		for(int i=0; i<annotationsList.size(); i++) {
			JSONValue ann = manager.serialize(annotationsList.get(i));
			if(ann!=null) annotations.set(i, ann);
		}
		annotationSetJson.put(IDomeoOntology.annotations, annotations);
		return annotationSetJson;
	}
	
	// Meant to be used with bibliography. Still not sure that is the case
	public JSONObject serializeWhatChanged(JsonSerializerManager manager, Object obj) {
		MAnnotationSet annotationSet = (MAnnotationSet) obj;
		JSONObject annotationSetJson = initAnnotationSetSerialization(manager, annotationSet);	
		
		// Serialization of the annotation items that have changed
		JSONArray annotations = new JSONArray();
		List<MAnnotation> annotationsList = annotationSet.getAnnotations();
		for(int i=0; i<annotationsList.size(); i++) {
			if(annotationsList.get(i).getHasChanged())
				annotations.set(i, manager.serialize(annotationsList.get(i)));
		}
		annotationSetJson.put(IDomeoOntology.annotations, annotations);
		return annotationSetJson;
	}
}
