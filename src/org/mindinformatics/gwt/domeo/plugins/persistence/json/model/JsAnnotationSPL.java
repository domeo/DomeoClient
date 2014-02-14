package org.mindinformatics.gwt.domeo.plugins.persistence.json.model;

import java.util.Date;

import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.JsoPharmgxUsage;
import org.mindinformatics.gwt.framework.component.resources.serialization.JsonGenericResource;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * JavaScriptObject for accessing the representation of the annotation
 * of type SPL (Pharmacogenomics) in JSON format.
 * 
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class JsAnnotationSPL extends JavaScriptObject {

	protected JsAnnotationSPL() {}

	// ------------------------------------------------------------------------
	//  Identity
	// ------------------------------------------------------------------------
	public final native String getId() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  General (RDFS and Dublin Core Terms)
	// ------------------------------------------------------------------------
	public final native String getLabel() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology::label]; 
	}-*/;
	public final native String getTitle() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms::title]; 
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
	public final native String getCreatedWith() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology::createdWith]; 
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
	//  Annotation Ontology
	// ------------------------------------------------------------------------
	public final native JsArray<JsAnnotationTarget> getTargets() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::hasTarget]; 
	}-*/;
	public final native JsArray<JsoPharmgxUsage> getBody() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::content]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  Permissions Ontology
	// ------------------------------------------------------------------------
	public final native boolean isLocked() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPermissionsOntology::isLocked];
	}-*/;
	
	//public final native String getTitle() /*-{ return this.domeo_title; }-*/;
	public final native String getType() /*-{ return this.domeo_SPLType; }-*/;
	
}


