package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.MAnnotationCitationReference;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class JsonCitationReferenceAnnotationSerializer extends JsonAnnotationSerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MAnnotationCitationReference ann = (MAnnotationCitationReference) obj;
		JSONObject annotation = initializeAnnotation(manager, ann);
	
		//manager.serialize(ann.getReference());
		annotation.put("index", new JSONString(ann.getReferenceIndex().toString()));
		annotation.put(IDomeoOntology.content, manager.serialize(ann.getReference()));
		return annotation;
	}
}
