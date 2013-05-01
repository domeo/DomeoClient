package org.mindinformatics.gwt.framework.component.agents.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsoAgent extends JavaScriptObject {

	protected JsoAgent() {}

	public final native String getUri() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
	public final native String getType() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;
	public final native String getUuid() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::uuid];
	}-*/;
	public final native String getName() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology::name]; 
	}-*/;
	public final native String getHomepage() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology::homepage]; 
	}-*/;
	
	// Person	
	public final native String getEmail() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology::email]; 
	}-*/;
	public final native String getTitle() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology::title]; 
	}-*/;
	public final native String getFirstName() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology::firstname]; 
	}-*/;
	public final native String getMiddleName() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology::middlename]; 
	}-*/;
	public final native String getLastName() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology::lastname]; 
	}-*/;
	public final native String getPicture() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology::picture]; 
	}-*/;
	
	// Software
	public final native String getVersion() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology::version]; 
	}-*/;
	public final native String getBuild() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IFoafxOntology::build]; 
	}-*/;
}
