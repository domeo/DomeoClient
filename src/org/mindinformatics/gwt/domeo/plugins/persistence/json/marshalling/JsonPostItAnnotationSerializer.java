package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.ICntOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.PostitType;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * This class provides the serialization aspects peculiar to the PostIt annotation.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonPostItAnnotationSerializer extends JsonAnnotationSerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MPostItAnnotation ann = (MPostItAnnotation) obj;
		JSONObject annotation = initializeAnnotation(manager, ann);
		//annotation.put("domeo_postItType", new JSONString(ann.getType().getName()));
		
		// Types management
		JSONArray types = new JSONArray();
		types.set(0, new JSONString(ann.getAnnotationType()));
		types.set(1, new JSONString(PostitType.typeByName(ann.getType().getName())));
		annotation.put(IRdfsOntology.type, types);
		annotation.put(IRdfsOntology.label, new JSONString(PostitType.findByName(ann.getType().getName()).getName()));
		
		// Body creation
		JSONArray bodies = new JSONArray();
		JSONObject body = new JSONObject();
		body.put(IDomeoOntology.generalId,  new JSONString(ann.getBody().getIndividualUri()));
		body.put(IDomeoOntology.generalType,  new JSONString(ann.getBody().getAnnotationType()));
		body.put(ICntOntology.chars, new JSONString(ann.getBody().getChars()));
		body.put(IDublinCoreTerms.format, new JSONString(ann.getBody().getFormat()));
		bodies.set(0, body); 
		annotation.put(IDomeoOntology.content, bodies);
		//annotation.put(IDomeoOntology.content, new JSONString(ann.getText()));
		return annotation;
	}
}
