package org.mindinformatics.gwt.domeo.plugins.persistence.json.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsAgentGroup extends JavaScriptObject {

	protected JsAgentGroup() {}
	
	public final native String getUuid() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::uuid]; 
	}-*/;
}
