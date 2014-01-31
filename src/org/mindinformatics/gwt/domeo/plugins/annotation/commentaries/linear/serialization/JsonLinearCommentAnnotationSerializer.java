package org.mindinformatics.gwt.domeo.plugins.annotation.commentaries.linear.serialization;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.plugins.annotation.commentaries.linear.model.MLinearCommentAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonAnnotationSerializer;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonSerializerManager;
import org.mindinformatics.gwt.framework.src.UUID;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * This class provides the serialization aspects peculiar to the Comment annotation.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonLinearCommentAnnotationSerializer extends JsonAnnotationSerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MLinearCommentAnnotation ann = (MLinearCommentAnnotation) obj;
		JSONObject annotation = initializeAnnotation(manager, ann);
		if(ann.getTitle()!=null && ann.getTitle().trim().length()>0) 
			annotation.put(IDublinCoreTerms.title, new JSONString(ann.getTitle()));
		JSONArray bodies = new JSONArray();
		JSONObject body = new JSONObject();
		body.put(IDomeoOntology.generalId,  new JSONString(UUID.uuid()));
		body.put(IDomeoOntology.generalType,  new JSONString("cnt:ContentAsText"));
		body.put("cnt:chars", new JSONString(ann.getText()));
		body.put("dc:format", new JSONString("text/plain"));
		bodies.set(0, body); 
		annotation.put(IDomeoOntology.content, bodies);
		return annotation;
	}
}
