package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.serialization;

import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationTarget;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsoMpStatement extends JavaScriptObject {
	
	protected JsoMpStatement() {}
	
	// ------------------------------------------------------------------------
	//  Identity
	// ------------------------------------------------------------------------
	public final native String getId() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
		
	// ------------------------------------------------------------------------
	//  Subject
	// ------------------------------------------------------------------------
	public final native String getText() /*-{ 
		return this['mp:hasContent']; 
	}-*/;
	
	public final native JsAnnotationTarget getContext() /*-{ 
		return this[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::hasTarget]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  Antibody
	// ------------------------------------------------------------------------
	public final native boolean isSupportingEvidence() /*-{ 
		if(this['mp:supportedBy']) return true;
		return false; 
	}-*/;
	
	public final native JsArray<JsoMpRelationship> getSupportingEvidence() /*-{ 
		return this['mp:supportedBy']; 
	}-*/;
	
	public final native boolean isChallengingEvidence() /*-{ 
		if(this['mp:challengedBy']) return true;
		return false; 
	}-*/;
	
	public final native JsArray<JsoMpRelationship> getChallengingEvidence() /*-{ 
		return this['mp:challengedBy']; 
	}-*/;
	
	public final native boolean isQualifiers() /*-{ 
		if(this['mp:qualifiedBy']) return true;
		return false; 
	}-*/;
	
	public final native JsArray<JsoMpRelationship> getQualifier() /*-{ 
		return this['mp:qualifiedBy']; 
	}-*/;
}
