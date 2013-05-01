package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.selectors.MTargetSelector;

import com.google.gwt.json.client.JSONObject;

public class JsonTargetSelectorSerializer extends JsonSelectorSerializer implements ISerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MTargetSelector sel = (MTargetSelector) obj;
		//JSONObject specificTarget = (JSONObject) initializeSpecificTarget(sel);
		
		//JSONObject selector = (JSONObject) initializeSelector(sel);
		JSONObject specificTarget = (JSONObject) initializeTarget(sel);
		//specificTarget.put("ao:hasSelector", selector);
		return specificTarget;
		
		//return new JSONString(sel.getTarget().getUrl());
	}
}
