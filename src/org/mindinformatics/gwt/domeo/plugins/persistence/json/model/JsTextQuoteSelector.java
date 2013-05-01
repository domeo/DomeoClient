package org.mindinformatics.gwt.domeo.plugins.persistence.json.model;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * JavaScriptObject for accessing the JSON representation of the
 * annotation of type TextQuoteSelector
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsTextQuoteSelector extends JavaScriptObject {

	protected JsTextQuoteSelector() {}
	
	public final native String getId() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId];
	}-*/;
	
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
	
	public final native String getMatch() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.selectors.MTextSelector::EXACT]; 
	}-*/;
	public final native String getPrefix() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector::PREFIX]; 
	}-*/;
	public final native String getSuffix() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector::SUFFIX]; 
	}-*/;
}

