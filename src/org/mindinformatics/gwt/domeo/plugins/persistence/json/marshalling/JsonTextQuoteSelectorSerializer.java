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
		
		System.out.println("JsonTextQuoteSelectorSerializer - serialize");
		System.out.println("exact:" + sel.getExact() + "|");
		
		System.out.println("JsonTextQuoteSelectorSerializer - after replace ....");

		System.out.println("exact:" + sel.getExact().replaceAll("\n", "\\\\\\n"));
		
		System.out.println("exact:" + new JSONString(sel.getExact().replaceAll("\n", "\\\\\\n")));

		
		selector.put(MTextQuoteSelector.PREFIX, new JSONString(sel.getPrefix().replaceAll("\n", "\\\\\\n").replaceAll("\r", "\\\\\\r").replace("\t", "\\\\\\t")));
		selector.put(MTextQuoteSelector.EXACT, new JSONString(sel.getExact().replaceAll("\n", "\\\\\\n").replaceAll("\r", "\\\\\\r").replace("\t", "\\\\\\t")));
		selector.put(MTextQuoteSelector.SUFFIX, new JSONString(sel.getSuffix().replaceAll("\n", "\\\\\\n").replaceAll("\r", "\\\\\\r").replace("\t", "\\\\\\t")));
		
		//str.replace(/\n/g, "\\\\n").replace(/\r/g, "\\\\r").replace(/\t/g, "\\\\t");
		/*
		 * original JSON String escape
		 */
		//selector.put(MTextQuoteSelector.PREFIX, new JSONString(sel.getPrefix().replaceAll("\n", "\\\\\\n")));
		//selector.put(MTextQuoteSelector.EXACT, new JSONString(sel.getExact().replaceAll("\n", "\\\\\\n")));
		//selector.put(MTextQuoteSelector.SUFFIX, new JSONString(sel.getSuffix().replaceAll("\n", "\\\\\\n")));
		JSONObject specificTarget = (JSONObject) initializeSpecificTarget(sel);
		specificTarget.put(IDomeoOntology.selector, selector);
		return specificTarget;
	}
}
