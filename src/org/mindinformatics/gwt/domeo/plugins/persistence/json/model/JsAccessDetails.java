package org.mindinformatics.gwt.domeo.plugins.persistence.json.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsAccessDetails extends JavaScriptObject {

	protected JsAccessDetails() {}
	
	public final native JsArray<JsAgentGroup> getAllowedGroups() /*-{ return this['permissions:allowedGroups']; }-*/;
}
