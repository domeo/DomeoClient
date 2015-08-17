package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsTextQuoteSelector;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */ 
public class TextQuoteSelectorJsonUnmarshaller extends AUnmarshaller implements IUnmarshaller {

	private IDomeo _domeo;
	
	public TextQuoteSelectorJsonUnmarshaller(IDomeo domeo) { _domeo = domeo;}
	
	@Override
	public MTextQuoteSelector unmarshallWithoutValidating(JsonUnmarshallingManager manager, JavaScriptObject json, MAnnotationSet set, 
			ArrayList<MSelector> selectors) {
		return (MTextQuoteSelector) super.unmarshallWithoutValidating(manager, json, set, selectors);
	}
	
	@Override
	public MTextQuoteSelector unmarshall(JsonUnmarshallingManager manager, JavaScriptObject json, String validation, MAnnotationSet set, 
			ArrayList<MSelector> selectors) {
		// Triggers input validation (skip when validation is OFF: IUnmarshaller.OFF_VALIDATION)
		validation(json, validation, set, selectors);

		JsTextQuoteSelector jsonSelector = (JsTextQuoteSelector) json;
		MTextQuoteSelector selector = AnnotationFactory.clonePrefixSuffixTextSelector(
			jsonSelector.getId(), 
			jsonSelector.getFormattedCreatedOn(), 
			_domeo.getPersistenceManager().getCurrentResource(), 
			
			
			/*
			 * Original new line character escape
			 */
			
			jsonSelector.getMatch().replaceAll("\\\\n", "n").replaceAll("\\\\r", "r").replaceAll("\\\\t", "t").replaceAll("\\\\\"", "\""), 
			jsonSelector.getPrefix().replaceAll("\\\\n", "n").replaceAll("\\\\r", "r").replaceAll("\\\\t", "t").replaceAll("\\\\\"", "\""), 
			jsonSelector.getSuffix().replaceAll("\\\\n", "n").replaceAll("\\\\r", "r").replaceAll("\\\\t", "t").replaceAll("\\\\\"", "\""));
			
			//jsonSelector.getMatch().replaceAll("\\\\n", "n").replaceAll("\\\\\"", "\""), 
			//jsonSelector.getPrefix().replaceAll("\\\\n", "n").replaceAll("\\\\\"", "\""), 
			//jsonSelector.getSuffix().replaceAll("\\\\n", "n").replaceAll("\\\\\"", "\""));
		return selector;
	}

	@Override
	public void validation(JavaScriptObject json, String validation, MAnnotationSet set, 
			ArrayList<MSelector> selectors) {
		verifyType(json, MTextQuoteSelector.TYPE);
		
		JsTextQuoteSelector jsonSelector = (JsTextQuoteSelector) json;
		Map<String, String> notNullableFieldsMap = new HashMap<String, String>();
		notNullableFieldsMap.put(IDomeoOntology.generalId, jsonSelector.getId());
		notNullableFieldsMap.put(IPavOntology.createdOn, jsonSelector.getCreatedOn());
		notNullableFieldsMap.put(MTextQuoteSelector.EXACT, jsonSelector.getMatch());
		notNullableFieldsMap.put(MTextQuoteSelector.PREFIX, jsonSelector.getPrefix());
		notNullableFieldsMap.put(MTextQuoteSelector.SUFFIX, jsonSelector.getSuffix());
		completeValidation(notNullableFieldsMap);
	}
}
