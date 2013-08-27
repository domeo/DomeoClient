package org.mindinformatics.gwt.domeo.plugins.annotation.spls.serialization;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonAnnotationSerializer;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonSerializerManager;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;



/**
 * This class provides the serialization aspects peculiar to the PostIt annotation.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonSPLsAnnotationSerializer extends JsonAnnotationSerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MSPLsAnnotation ann = (MSPLsAnnotation) obj;
		manager._domeo.getLogger().debug(this, "0");
		JSONObject annotation = initializeAnnotation(manager, ann);
		manager._domeo.getLogger().debug(this, "1");
		// Body creation
		JSONArray bodies = new JSONArray();
		JSONObject body = new JSONObject();
		// Paolo content serialization 
		
		//body.put(ICntOntology.chars, new JSONString(ann.getBody().getChars()));
		//body.put(IDublinCoreTerms.format, new JSONString(ann.getBody().getFormat()));
		bodies.set(0, body); 
		annotation.put(IDomeoOntology.content, bodies);
		//annotation.put(IDomeoOntology.content, new JSONString(ann.getText()));
		return annotation;
	}
}
