package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.serialization;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;

public class JsoMpRelationship extends JavaScriptObject {
	
	protected JsoMpRelationship() {}
	
	// ------------------------------------------------------------------------
	//  Identity
	// ------------------------------------------------------------------------
	public final native String getId() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
	
	public final native String getCreatedOn() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology::createdOn]; 
	}-*/;
	public final native String getCreatedBy() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology::createdBy]; 
	}-*/;
	
	public final native JavaScriptObject getResource() /*-{ 
		return this['reif:resource']; 
	}-*/;
	
	public final Date getFormattedCreatedOn() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss Z");
		return fmt.parse(getCreatedOn());
	} 
}
