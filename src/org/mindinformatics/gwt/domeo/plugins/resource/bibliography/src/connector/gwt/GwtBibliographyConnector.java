package org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.gwt;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.IBibliographyConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.IStarringRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class GwtBibliographyConnector implements IBibliographyConnector {

	private IDomeo _domeo;
	public GwtBibliographyConnector(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public void starResource(MDocumentResource documentResource, IStarringRequestCompleted completionHandler) {
		JSONObject r = new JSONObject();
		r.put("url", new JSONString(documentResource.getUrl()));
		r.put("label", new JSONString(documentResource.getLabel()));
		
		
		if(_domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference()!=null && 
				_domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference()!=null &&
				((MAnnotationReference)_domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference()).getReference()!=null &&
				((MAnnotationReference)_domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference()).getReference() instanceof MPublicationArticleReference) {
			MPublicationArticleReference ref = (MPublicationArticleReference) ((MAnnotationReference)_domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference()).getReference();
			
			Window.alert("Self reference");
			
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
			
			r.put("reference", reference);
		} else {
			Window.alert("No self reference");
		}
		Window.alert(r.toString());
		completionHandler.documentResourceStarred();
	}
	
	@Override
	public void unstarResource(MDocumentResource resource, IStarringRequestCompleted completionHandler) {
		completionHandler.documentResourceUnstarred();
	}
}
