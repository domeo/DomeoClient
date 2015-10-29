package org.mindinformatics.gwt.domeo.plugins.annotation.curation.model;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonAnnotationSerializer;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonSerializerManager;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonAnnotationCurationSerializer extends JsonAnnotationSerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MCurationToken ann = (MCurationToken) obj;
		JSONObject annotation = initializeAnnotation(manager, ann);

		JSONArray bodies = new JSONArray();
		JSONObject body = new JSONObject();
		body.put(IRdfsOntology.rdfValue, new JSONString(ann.getStatus()));
		body.put(IDublinCoreTerms.description, nullable(ann.getDescription()));

		bodies.set(0, body); 
		annotation.put(IDomeoOntology.content, bodies);
		
		return annotation;
	}
}
