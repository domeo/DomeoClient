package org.mindinformatics.gwt.domeo.model;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.i18n.client.DateTimeFormat;

public class JsoAnnotationSet extends JavaScriptObject {

	protected JsoAnnotationSet() {}

	public final native String getUuid() /*-{ return this.uuid; }-*/;
	public final native String getLocalId() /*-{ return this.localId; }-*/;
	
	public final native String getLabel() /*-{ return this.label; }-*/;
	public final native String getDescription() /*-{ return this.description; }-*/;
	
	public final native String getVersionNumber() /*-{ return this.versionNumber; }-*/;
	public final native String getPreviousVersion() /*-{ return this.previousVersion; }-*/;
	
	public final native String getCreatedOnAsString() /*-{ return this.createdon; }-*/;
	public final Date getCreatedOn() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy HH:mm:ss Z");
		return fmt.parse(getCreatedOnAsString());
	}
	public final native String getLastSavedOnAsString() /*-{ return this.lastSavedOn; }-*/;
	public final Date getLastSavedOn() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy HH:mm:ss Z");
		return fmt.parse(getLastSavedOnAsString());
	}
	
	// Creator
	public final native JsArray<JavaScriptObject> getCreatedBy() /*-{
	    return this.createdby || [];
	}-*/;
	
	public final native JsArray<JsoAnnotation> getAnnotation() /*-{
	   return this.annotation || [];
	}-*/;
}
