package org.mindinformatics.gwt.domeo.plugins.persistence.json.model;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsImageInDocumentSelector extends JavaScriptObject {

	protected JsImageInDocumentSelector() {}
	
	public final native String getId() /*-{ return this['@id']; }-*/;
	
	// ------------------------------------------------------------------------
	//  PAV (Provenance, Authoring and Versioning) Ontology
	// ------------------------------------------------------------------------
	public final native String getCreatedOn() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology::createdOn]; 
	}-*/;
	
	public final Date getFormattedCreatedOn() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss Z");
		return fmt.parse(getCreatedOn());
	}
	
	public final native String getDomeoUuid() /*-{ return this.domeo_mongoUuid; }-*/;
	public final native String getXPath() /*-{ return this['domeo:xPath']; }-*/;
}
