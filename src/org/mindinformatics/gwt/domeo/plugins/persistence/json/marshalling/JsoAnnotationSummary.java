package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import java.util.Date;

import org.mindinformatics.gwt.framework.component.users.model.JsoUser;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsoAnnotationSummary extends JavaScriptObject {

	protected JsoAnnotationSummary() {}
	
	public final native String getId() /*-{ return this['@id']; }-*/;
	public final native String getNewId() /*-{ return this.domeo_new_id; }-*/;
	public final native String getLocalId() /*-{ return this.domeo_temp_localId; }-*/;
	public final native String getMongoUuid() /*-{ return this['domeo:mongoUuid']; }-*/;
	
	// ------------------------------------------------------------------------
	//  General (RDFS and Dublin Core Terms
	// ------------------------------------------------------------------------
	public final native String getLabel() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology::rdfLabel]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  PAV (Provenance, Authoring and Versioning) Ontology
	// ------------------------------------------------------------------------
	public final native String getCreatedOn() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology::createdOn]; 
	}-*/;
	public final native JsoUser getCreatedBy() /*-{ 
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
}
