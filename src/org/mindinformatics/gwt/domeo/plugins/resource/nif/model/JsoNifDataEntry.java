package org.mindinformatics.gwt.domeo.plugins.resource.nif.model;

import com.google.gwt.core.client.JavaScriptObject;

public class JsoNifDataEntry extends JavaScriptObject {

	protected JsoNifDataEntry() {}
	
	public final native String getUri() /*-{ return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; }-*/;
	public final native String getType() /*-{ return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; }-*/;
	public final native String getLabel() /*-{ return this.termLabel; }-*/;
	public final native String getDescription() /*-{ return this.description; }-*/;
	public final native String getSourceUri() /*-{ return this.sourceUri; }-*/;
	public final native String getSourceLabel() /*-{ return this.sourceLabel; }-*/;
	
	public final native String getCloneId() /*-{ return this.cloneId; }-*/;
	public final native String getVendor() /*-{ return this.vendor; }-*/;
	public final native String getTarget() /*-{ return this.target; }-*/;
	public final native String getOrganism() /*-{ return this.sourceOrganism; }-*/;
	public final native String getCatalog() /*-{ return this.catalog; }-*/;
	//public final native JsArray<JsoPerson> getAuthorNames() /*-{ return this.authorNames; }-*/;
}
