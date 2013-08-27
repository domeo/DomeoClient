package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.HashMap;
import java.util.Map;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology;
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
	public MTextQuoteSelector unmarshallWithoutValidating(JsonUnmarshallingManager manager, JavaScriptObject json) {
		return (MTextQuoteSelector) super.unmarshallWithoutValidating(manager, json);
	}
	
	@Override
	public MTextQuoteSelector unmarshall(JsonUnmarshallingManager manager, JavaScriptObject json, String validation) {
		// Triggers input validation (skip when validation is OFF: IUnmarshaller.OFF_VALIDATION)
		validation(json, validation);

		JsTextQuoteSelector jsonSelector = (JsTextQuoteSelector) json;
		MTextQuoteSelector selector = AnnotationFactory.clonePrefixSuffixTextSelector(
			jsonSelector.getId(), 
			jsonSelector.getFormattedCreatedOn(), 
			_domeo.getPersistenceManager().getCurrentResource(), 
			jsonSelector.getMatch().replaceAll("\\\\n", "n"), 
			jsonSelector.getPrefix().replaceAll("\\\\n", "n"), 
			jsonSelector.getSuffix().replaceAll("\\\\n", "n"));
		return selector;
	}

	@Override
	public void validation(JavaScriptObject json, String validation) {
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
