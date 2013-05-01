package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import java.util.List;

import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MBibliographicSet;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * This class serializes the Bibliographic Set in JSON format.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonBibliographicSetSerializer extends JsonAnnotationSetSerializer implements ISerializer {
	
	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MBibliographicSet bibliographicSet = (MBibliographicSet) obj;
		JSONObject annotationSetJson = initAnnotationSetSerialization(manager, bibliographicSet);
		annotationSetJson.put(IDomeoOntology.extractionLevel, new JSONString(Integer.toString(bibliographicSet.getLevel())));

		// Serialization of the annotation items
		JSONArray annotations = new JSONArray();
		List<MAnnotation> annotationsList = bibliographicSet.getAnnotations();
		for(int i=0; i<annotationsList.size(); i++) {
			JSONValue ann = manager.serialize(annotationsList.get(i));
			if(ann!=null) annotations.set(i, ann);
		}
		
		annotationSetJson.put(IDomeoOntology.annotations, annotations);
		return annotationSetJson;
	}
}
