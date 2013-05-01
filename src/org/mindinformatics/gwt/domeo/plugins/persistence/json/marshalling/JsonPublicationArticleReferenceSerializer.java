package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class JsonPublicationArticleReferenceSerializer implements ISerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MPublicationArticleReference ref = (MPublicationArticleReference) obj;
		JSONObject reference = new JSONObject();
		//jo.put("class", new JSONString(MAnnotation.class.getName()));
		reference.put("@type", new JSONString(ref.getClass().getName()));
		reference.put("@id", new JSONString(ref.getDoi()!=null?ref.getDoi():"<unknown-fix>"));
		reference.put("title", new JSONString(ref.getTitle()!=null?ref.getTitle():"<unknown>"));
		reference.put("authorNames", new JSONString(ref.getAuthorNames()!=null?ref.getAuthorNames():"<unknown>"));
		if(ref.getDoi()!=null) reference.put("doi", new JSONString(ref.getDoi()));
		if(ref.getPubMedId()!=null) reference.put("pmid", new JSONString(ref.getPubMedId()));
		if(ref.getPubMedCentralId()!=null) reference.put("pmcid", new JSONString(ref.getPubMedCentralId()));
		reference.put("publicationInfo", new JSONString(ref.getJournalPublicationInfo()!=null?ref.getJournalPublicationInfo():"<unknown>"));
		reference.put("journalName", new JSONString(ref.getJournalName()!=null?ref.getJournalName():"<unknown>"));
		reference.put("journalIssn", new JSONString(ref.getJournalIssn()!=null?ref.getJournalIssn():"<unknown>"));
		if(ref.getUnrecognized()!=null) reference.put("unrecognized", new JSONString(ref.getUnrecognized()));
		
		if(ref.getProvidedBy()!=null) {
			reference.put(IPavOntology.providedBy, manager.serialize(ref.getProvidedBy()));
			manager.addAgentToSerialize(ref.getProvidedBy());
		}
		if(ref.getImportedOn()!=null) reference.put(IPavOntology.importedOn, new JSONString(ref.getImportedOn()));
	
		return reference;
	}
	
	public JSONObject serializeWhatChanged(JsonSerializerManager manager, Object obj) {
		return serialize(manager, obj);
	}
}
