package org.mindinformatics.gwt.domeo.plugins.annotation.persistence.model;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsAnnotationSetSummary extends JavaScriptObject {

	protected JsAnnotationSetSummary() {}

	public final native String getUuid() /*-{ return this.uuid; }-*/;
	public final native boolean isLocked() /*-{ return this.isLocked; }-*/;
	public final native String getVersionNumber() /*-{ return this.versionNumber; }-*/;
	public final native String getPreviousVersion() /*-{ return this.previousVersion; }-*/;

	public final native String getLabel() /*-{ return this.label; }-*/;
	public final native String getDescription() /*-{ return this.description; }-*/;
	
	public final native String getCreatedBy() /*-{ return this.createdBy; }-*/;
	public final native String getCreatedById() /*-{ return this.createdById; }-*/;
	public final native String getCreatedByUrl() /*-{ return this.createdByUrl; }-*/;
	public final native String getjsCreatedOn() /*-{ return this.createdOn; }-*/;
	public final native String getjsLastSavedOn() /*-{ return this.lastSavedOn; }-*/;
	
	public final native int getNumberOfAnnotationItems() /*-{ return this.numberOfAnnotationItems; }-*/;
	
	public final Date getFormattedCreatedOn() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy.MM.dd HH:mm:ss Z");
		return fmt.parse(getjsCreatedOn());
	}
	public final Date getCreatedOn() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy.MM.dd HH:mm:ss Z");
		return fmt.parse(getjsCreatedOn());
	}
	public final Date getLastSavedOn() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy.MM.dd HH:mm:ss Z");
		return fmt.parse(getjsLastSavedOn());
	}  
}
