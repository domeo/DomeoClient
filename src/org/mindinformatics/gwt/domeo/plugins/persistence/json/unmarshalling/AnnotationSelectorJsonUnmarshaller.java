package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.HashMap;
import java.util.Map;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MAnnotationSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSelector;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */ 
public class AnnotationSelectorJsonUnmarshaller extends AUnmarshaller implements IUnmarshaller {

	private IDomeo _domeo;
	
	public AnnotationSelectorJsonUnmarshaller(IDomeo domeo) { _domeo = domeo; }
	
	@Override
	public MAnnotationSelector unmarshallWithoutValidating(JsonUnmarshallingManager manager, JavaScriptObject json) {
		return (MAnnotationSelector) super.unmarshallWithoutValidating(manager, json);
	}
	
	@Override
	public MAnnotationSelector unmarshall(JsonUnmarshallingManager manager, JavaScriptObject json, String validation) {
		// Triggers input validation (skip when validation is OFF: IUnmarshaller.OFF_VALIDATION)
		validation(json, validation);
		
		JsAnnotationSelector jsonSelector = (JsAnnotationSelector) json;
		MAnnotationSelector selector = AnnotationFactory.cloneAnnotationSelector(
			jsonSelector.getId(), 
			jsonSelector.getFormattedCreatedOn(), 
			_domeo.getPersistenceManager().getCurrentResource());
		
		MAnnotation annotation = new MPostItAnnotation();
		annotation.setIndividualUri(jsonSelector.getAnnotation());
		selector.setAnnotation(annotation);
		
		return selector;
	}

	@Override
	protected void validation(JavaScriptObject json, String validation) {
		verifyType(json, MAnnotationSelector.TYPE);
		
		JsAnnotationSelector jsonSelector = (JsAnnotationSelector) json;
		Map<String, String> notNullableFieldsMap = new HashMap<String, String>();
		notNullableFieldsMap.put(IDomeoOntology.generalId, jsonSelector.getId());
		notNullableFieldsMap.put(IPavOntology.createdOn, jsonSelector.getCreatedOn());
		notNullableFieldsMap.put(MAnnotationSelector.TARGET, jsonSelector.getAnnotation());
		
		completeValidation(notNullableFieldsMap);
	}
}
