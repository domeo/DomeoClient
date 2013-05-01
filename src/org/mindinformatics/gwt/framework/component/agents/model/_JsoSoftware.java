package org.mindinformatics.gwt.framework.component.agents.model;

import org.mindinformatics.gwt.framework.model.agents.ISoftware;

import com.google.gwt.core.client.JavaScriptObject;

public class _JsoSoftware extends JavaScriptObject implements ISoftware {

	public static final String FULL_NAME = "org.mindinformatics.gwt.framework.component.agents.model.JsoSoftware";
	
	protected _JsoSoftware() {}
	
	public final native String getUri() /*-{ 
		alert("software uri: " + this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]);
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
	public final native String getType() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;
	public final native String getUuid() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::uuid];
	}-*/;
	public final native String getName() /*-{ 
		alert("software name: " + this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology::name]);
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology::name]; 
	}-*/;
	public final native String getHomepage() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology::homepage]; 
	}-*/;
	
	public final native String getVersion() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology::version]; 
	}-*/;
	public final native String getBuild() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology::build]; 
	}-*/;
	
	@Override
	public String getAgentType() {
		return TYPE;
	}
}
