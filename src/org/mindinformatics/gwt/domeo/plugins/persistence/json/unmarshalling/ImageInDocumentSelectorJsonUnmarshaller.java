package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.MOnlineImage;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MImageInDocumentSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsImageInDocumentSelector;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsTextQuoteSelector;

import com.google.gwt.core.client.JavaScriptObject;

public class ImageInDocumentSelectorJsonUnmarshaller extends AUnmarshaller implements IUnmarshaller {

	private IDomeo _domeo;
	
	public ImageInDocumentSelectorJsonUnmarshaller(IDomeo domeo) { _domeo = domeo; }
	
	@Override
	public MImageInDocumentSelector unmarshallWithoutValidating(JsonUnmarshallingManager manager, JavaScriptObject json, MAnnotationSet set, 
			ArrayList<MSelector> selectors) {
		return (MImageInDocumentSelector) super.unmarshallWithoutValidating(manager, json, set, selectors);
	}
	
	@Override
	public MImageInDocumentSelector unmarshall(JsonUnmarshallingManager manager, JavaScriptObject json, String validation, MAnnotationSet set, 
			ArrayList<MSelector> selectors) {
		// Triggers input validation (skip when validation is OFF: IUnmarshaller.OFF_VALIDATION)
		validation(json, validation, set, selectors);
		
		JsImageInDocumentSelector jsonSelector = (JsImageInDocumentSelector) json;
		MOnlineImage image = AnnotationFactory.cloneOnlineImage("", jsonSelector.getXPath(), "");
		//image.setUrl(jsonSelector.g);
		
		MImageInDocumentSelector selector = AnnotationFactory.cloneImageInDocumentSelector(
				jsonSelector.getId(), 
				jsonSelector.getFormattedCreatedOn(), 
				null, // TODO fix 
				image, 
				_domeo.getPersistenceManager().getCurrentResource());
		return selector;
	}

	public void validation(JavaScriptObject json, String validation, MAnnotationSet set, 
			ArrayList<MSelector> selectors) {
		verifyType(json, MImageInDocumentSelector.TYPE);
		
		JsTextQuoteSelector jsonSelector = (JsTextQuoteSelector) json;
		Map<String, String> notNullableFieldsMap = new HashMap<String, String>();
		notNullableFieldsMap.put(IDomeoOntology.generalId, jsonSelector.getId());
		notNullableFieldsMap.put(IPavOntology.createdOn, jsonSelector.getCreatedOn());
		//notNullableFieldsMap.put(MTextQuoteSelector.EXACT, jsonSelector.getMatch());
		//notNullableFieldsMap.put(MTextQuoteSelector.PREFIX, jsonSelector.getPrefix());
		//notNullableFieldsMap.put(MTextQuoteSelector.SUFFIX, jsonSelector.getSuffix());
		completeValidation(notNullableFieldsMap);
	}
}
