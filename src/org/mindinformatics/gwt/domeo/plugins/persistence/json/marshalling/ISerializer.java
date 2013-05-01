package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;

/**
 *
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ISerializer {
	public JSONObject serialize(JsonSerializerManager manager, Object obj);
	public JSONObject serializeWhatChanged(JsonSerializerManager manager, Object obj);
}
