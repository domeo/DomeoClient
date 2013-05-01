package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.MOnlineImage;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MImageInDocumentSelector;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * This class provides the serialization aspects peculiar to selector that
 * identifies images in a document.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonImageInDocumentSelectorSerializer extends JsonSelectorSerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MImageInDocumentSelector sel = (MImageInDocumentSelector) obj;
		JSONValue selector = initializeSelector(sel);
		((JSONObject)selector).put(IDomeoOntology.xpath,  new JSONString(((MOnlineImage)sel.getTarget()).getXPath()));
		JSONObject specificTarget = (JSONObject) initializeSpecificTarget(sel);
		specificTarget.put(IDomeoOntology.context, new JSONString(sel.getContext().getUrl()));
		specificTarget.put(IDomeoOntology.selector, selector);
		return specificTarget;
	}
}
