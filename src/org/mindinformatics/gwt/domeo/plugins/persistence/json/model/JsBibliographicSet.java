package org.mindinformatics.gwt.domeo.plugins.persistence.json.model;

import java.util.Date;

import org.mindinformatics.gwt.framework.component.agents.model.JsoAgent;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * JavaScriptObject that allows access to the JSON of the Bibliographic Set.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsBibliographicSet extends JavaScriptObject {

	protected JsBibliographicSet() {}
	
	// ------------------------------------------------------------------------
	//  Identity
	// ------------------------------------------------------------------------
	public final native String getId() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
	public final native String getType() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;
	public final native String getMongoUuid() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::mongoUuid]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  General (RDFS and Dublin Core Terms
	// ------------------------------------------------------------------------
	public final native String getLabel() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology::rdfLabel]; 
	}-*/;
	public final native String getDescription() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms::description]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  PAV (Provenance, Authoring and Versioning) Ontology
	// ------------------------------------------------------------------------
	public final native String getCreatedOn() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology::createdOn]; 
	}-*/;
	public final native String getCreatedBy() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology::createdBy]; 
	}-*/;
	public final native String getLastSaved() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology::lastSavedOn]; 
	}-*/;
	public final native String getVersionNumber() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology::versionNumber]; 
	}-*/;
	public final native String getPreviousVersion() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology::previousVersion]; 
	}-*/;
	public final native String getLineageUri() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology::lineageUri]; 
	}-*/;
	
	public final Date getFormattedCreatedOn() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss Z");
		return fmt.parse(getCreatedOn());
	} 
	
	public final Date getFormattedLastSaved() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss Z");
		return fmt.parse(getLastSaved());
	} 
	
	// ------------------------------------------------------------------------
	//  Permissions Ontology
	// ------------------------------------------------------------------------
	public final native boolean isLocked() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPermissionsOntology::isLocked]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  Annotation Ontology
	// ------------------------------------------------------------------------
	public final native JsArray<JavaScriptObject> getAnnotation() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::annotations]; 
	}-*/;
	public final native int getNumberOfAnnotationItems() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::annotationsCount];
	}-*/;
	
	
	public final native JsArray<JsoAgent> getAgents() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::agents];
	}-*/;

	public final native String getLevel() /*-{ return this.extraction_level; }-*/;
	public final native String getLocalId() /*-{ return this.domeo_localId; }-*/;
	

	
	
	
}
