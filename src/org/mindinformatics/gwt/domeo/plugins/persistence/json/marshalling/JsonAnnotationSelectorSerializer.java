package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MAnnotationSelector;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Annotation Selector serializer.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */ 
public class JsonAnnotationSelectorSerializer extends JsonSelectorSerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MAnnotationSelector annotationSelector = (MAnnotationSelector) obj;
		JSONObject jsonSelector = (JSONObject) initializeSelector(annotationSelector);
		jsonSelector.put(MAnnotationSelector.TARGET, new JSONString(annotationSelector.getAnnotation().getIndividualUri()));
		JSONObject jsonSpecificTarget = (JSONObject) initializeSpecificTarget(annotationSelector);
		jsonSpecificTarget.put(IDomeoOntology.selector, jsonSelector);
		return jsonSpecificTarget;
	}
}
