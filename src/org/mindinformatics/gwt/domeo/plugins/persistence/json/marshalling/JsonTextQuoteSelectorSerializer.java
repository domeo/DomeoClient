package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * This class provides the serialization aspects peculiar to the Text Quote selector.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonTextQuoteSelectorSerializer extends JsonSelectorSerializer {
	
	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MTextQuoteSelector sel = (MTextQuoteSelector) obj;
		JSONObject selector = (JSONObject) initializeSelector(sel);
		selector.put(MTextQuoteSelector.PREFIX, new JSONString(sel.getPrefix()));
		selector.put(MTextQuoteSelector.EXACT, new JSONString(sel.getExact()));
		selector.put(MTextQuoteSelector.SUFFIX, new JSONString(sel.getSuffix()));
		JSONObject specificTarget = (JSONObject) initializeSpecificTarget(sel);
		specificTarget.put(IDomeoOntology.selector, selector);
		return specificTarget;
	}
}
