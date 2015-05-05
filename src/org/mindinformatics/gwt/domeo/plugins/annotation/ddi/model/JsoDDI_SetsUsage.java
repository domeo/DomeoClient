package org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoDDI_SetsUsage extends JavaScriptObject {
	protected JsoDDI_SetsUsage() {
	}

	// get set
	public final native JsoddiUsage getSets() /*-{
		return this['sets'];
	}-*/;
}
