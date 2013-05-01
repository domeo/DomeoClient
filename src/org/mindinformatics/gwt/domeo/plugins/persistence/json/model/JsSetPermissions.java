package org.mindinformatics.gwt.domeo.plugins.persistence.json.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsSetPermissions extends JavaScriptObject {

	protected JsSetPermissions() {}
	
	public final native String isLocked() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPermissionsOntology::isLocked];
	}-*/;
	public final native String getPermissionType() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPermissionsOntology::accessType];
	}-*/;
	public final native JsAccessDetails getPermissionDetails() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPermissionsOntology::accessDetails];
	}-*/;
}
