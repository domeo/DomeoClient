package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;

import com.google.gwt.json.client.JSONObject;

public class JsonReferenceAnnotationSerializer extends JsonAnnotationSerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MAnnotationReference ann = (MAnnotationReference) obj;
		JSONObject annotation = initializeAnnotation(manager, ann);
		//manager.serialize(ann.getReference());
		annotation.put(IDomeoOntology.content, manager.serialize(ann.getReference()));
		return annotation;
	}
}
