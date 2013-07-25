package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.MDocumentAnnotation;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class JsonDocumentAnnotationSerializer extends ASerializer implements ISerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MDocumentAnnotation o = (MDocumentAnnotation) obj;
		
		JSONObject jo = new JSONObject();
		jo.put("@type", new JSONString("ao:DocumentAnnotation"));
		jo.put("uuid", new JSONString(o.getUuid()));
		jo.put("localId", new JSONString(Long.toString(o.getLocalId())));
		jo.put("versionNumber", new JSONString(o.getVersionNumber()));
		jo.put("previousVersion", new JSONString(o.getPreviousVersion()));

		return jo;
	}
	
	public JSONObject serializeWhatChanged(JsonSerializerManager manager, Object obj) {
		return serialize(manager, obj);
	}
}
