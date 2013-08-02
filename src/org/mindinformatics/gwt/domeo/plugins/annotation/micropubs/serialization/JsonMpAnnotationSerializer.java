package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.serialization;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpData;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpDataImage;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpQualifier;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpReference;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpRelationship;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpStatement;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonAnnotationSerializer;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonSerializerManager;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;



/**
 * This class provides the serialization aspects peculiar to the PostIt annotation.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonMpAnnotationSerializer extends JsonAnnotationSerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MMicroPublicationAnnotation ann = (MMicroPublicationAnnotation) obj;
		JSONObject annotation = initializeAnnotation(manager, ann);
		
		JSONArray types = new JSONArray();
		types.set(0, new JSONString("mp:MicroPublication"));
		types.set(1, new JSONString("swande:"+ann.getMicroPublication().getType()));
				
		// Body creation
		JSONArray bodies = new JSONArray();
		JSONObject body = new JSONObject();
		body.put(IDomeoOntology.generalId,  new JSONString(ann.getMicroPublication().getId()));
		body.put(IDomeoOntology.generalType, new JSONString("mp:MicroPublication"));
		
		JSONObject argues = new JSONObject();
		argues.put(IDomeoOntology.generalType, new JSONString("mp:Statement"));
		//argues.put(IDomeoOntology.generalId, new JSONString(ann.getMicroPublication().getArgues().getId()));
		argues.put("mp:hasContent", new JSONString(ann.getMicroPublication().getArgues().getText()));
		
		JSONValue val = encodeSelector(manager, ann.getMicroPublication().getArgues().getSelector());
		argues.put("hasContext", val);
		
		body.put("mp:argues", argues);
		
		int i = 0;
		JSONArray support = new JSONArray();
		for(MMpRelationship rel: ann.getMicroPublication().getEvidence()) {
			JSONObject supportedBy = new JSONObject();
			supportedBy.put(IDomeoOntology.generalId,  new JSONString(rel.getId()));
			supportedBy.put(IPavOntology.createdBy, new JSONString(rel.getCreator()!=null?ann.getCreator().getUri():""));
			if(ann.getCreator()!=null) manager.addAgentToSerialize(ann.getCreator());
			supportedBy.put(IPavOntology.createdOn, nonNullable(rel.getCreationDate()));
			
			if(rel.getObjectElement() instanceof MMpDataImage) {
				MMpData mMpData = (MMpData) rel.getObjectElement();
				JSONObject data = new JSONObject();
				data.put(IDomeoOntology.generalId,  new JSONString(mMpData.getSelector().getUri()));
				data.put(IDomeoOntology.generalType, new JSONString("mp:DataImage"));
				//data.put(IPavOntology.createdBy, new JSONString(mMpData.getSelector().getCreator()!=null?mMpData.getSelector().getCreator().getUri():""));
				//if(mMpData.getSelector().getCreator()!=null) manager.addAgentToSerialize(mMpData.getSelector().getCreator());
				//data.put(IPavOntology.createdOn, nonNullable(mMpData.getSelector().getCreatedOn()));
				//data.put("resource", nonNullable(mMpData.getSelector().getTarget().getUrl()));
				
				JSONValue v = encodeSelector(manager, mMpData.getSelector());
				data.put("hasContext", v);
				
				supportedBy.put("reif:resource", data);
			} else if(rel.getObjectElement() instanceof MMpReference) {
				MMpReference mMpReference = (MMpReference) rel.getObjectElement();
				//JSONObject ref = new JSONObject();
				
				//ref.put(IDomeoOntology.generalId,  new JSONString(mMpReference.getSelector().getUri()));
				//ref.put(IDomeoOntology.generalType, new JSONString("mp:Reference"));
				//ref.put(IPavOntology.createdBy, new JSONString(mMpReference.getSelector().getCreator()!=null?mMpReference.getSelector().getCreator().getUri():""));
				//if(mMpReference.getSelector().getCreator()!=null) manager.addAgentToSerialize(mMpReference.getSelector().getCreator());
				//ref.put(IPavOntology.createdOn, nonNullable(mMpReference.getSelector().getCreatedOn()));
				
				JSONObject r =  (JSONObject)manager.serialize(mMpReference.getReference());
				
				MPublicationArticleReference aRef = mMpReference.getReference();

				
				
				if(mMpReference.getSelector()!=null) {
					JSONValue v = encodeSelector(manager, mMpReference.getSelector());
					r.put("hasContext", v);
				}
				
				supportedBy.put("reif:resource", r);
			} else if(rel.getObjectElement() instanceof MMpStatement) {
				MMpStatement mMpStatement = (MMpStatement) rel.getObjectElement();
				
				JSONObject statement = new JSONObject();
				statement.put(IDomeoOntology.generalType, new JSONString("mp:Statement"));
				//argues.put(IDomeoOntology.generalId, new JSONString(ann.getMicroPublication().getArgues().getId()));
				statement.put("mp:hasContent", new JSONString(mMpStatement.getText()));
				
				JSONValue va = encodeSelector(manager, mMpStatement.getSelector());
				statement.put("hasContext", va);
				
				supportedBy.put("reif:resource", statement);
			}
			
			support.set(i++, supportedBy);
		}
		argues.put("mp:supportedBy", support);
		
		int j = 0;
		JSONArray qualification = new JSONArray();
		for(MMpRelationship rel: ann.getMicroPublication().getQualifiers()) {
			JSONObject qualifiedBy = new JSONObject();
			qualifiedBy.put(IDomeoOntology.generalId,  new JSONString(rel.getId()));
			qualifiedBy.put(IPavOntology.createdBy, new JSONString(rel.getCreator()!=null?ann.getCreator().getUri():""));
			if(ann.getCreator()!=null) manager.addAgentToSerialize(ann.getCreator());
			qualifiedBy.put(IPavOntology.createdOn, nonNullable(rel.getCreationDate()));
			
			MLinkedResource r = ((MMpQualifier)rel.getObjectElement()).getQualifier();
			
			JSONObject term = new JSONObject();
			term.put(IDomeoOntology.generalId, new JSONString(r.getUrl()));
			term.put(IRdfsOntology.label, new JSONString(r.getLabel()));
			if(r.getDescription()!=null) 
				term.put(IDublinCoreTerms.description, new JSONString(r.getDescription()));
			if(r.getSynonyms()!=null) 
				term.put(IDomeoOntology.synonyms, new JSONString(r.getSynonyms()));
			
			// This should be lazy linked and not redaundant
			JSONObject resource = new JSONObject();
			MGenericResource res = r.getSource();
			resource.put(IDomeoOntology.generalId, new JSONString(res.getUrl()));
			resource.put(IRdfsOntology.label, new JSONString(res.getLabel()));
			term.put(IDublinCoreTerms.source, resource);
			
			qualifiedBy.put("reif:resource", term);
			
			qualification.set(j++, qualifiedBy);
		}
		argues.put("mp:qualifiedBy", qualification);
		
		//body.put(ICntOntology.chars, new JSONString(ann.getBody().getChars()));
		//body.put(IDublinCoreTerms.format, new JSONString(ann.getBody().getFormat()));
		bodies.set(0, body); 
		annotation.put(IDomeoOntology.content, bodies);
		//annotation.put(IDomeoOntology.content, new JSONString(ann.getText()));
		return annotation;
	}
}