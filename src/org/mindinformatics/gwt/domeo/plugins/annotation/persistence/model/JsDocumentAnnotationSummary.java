package org.mindinformatics.gwt.domeo.plugins.annotation.persistence.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsDocumentAnnotationSummary extends JavaScriptObject{
	
	protected JsDocumentAnnotationSummary() {}

	public final native String getUuid() /*-{ return this.uuid; }-*/;

	public final native String getVersionNumber() /*-{ return this.versionNumber; }-*/;
	public final native String getPreviousVersion() /*-{ return this.previousVersion; }-*/;
}
