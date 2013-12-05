package org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.json;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.IBibliographyConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.IStarringRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.src.ApplicationUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonBibliographyConnector implements IBibliographyConnector {

	private IDomeo _domeo;
	public JsonBibliographyConnector(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public void starResource(MDocumentResource documentResource, final IStarringRequestCompleted completionHandler) {
		String url = GWT.getModuleBaseURL() + "bibliography/star";
		if(!_domeo.isHostedMode())
			url = ApplicationUtils.getUrlBase(GWT.getModuleBaseURL()) + "bibliography/star";
	    RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
	    builder.setHeader("Content-Type", "application/json");
	    builder.setTimeoutMillis(10000);
	    
	    try {
	    	JSONObject r = new JSONObject();
			r.put("url", new JSONString(documentResource.getUrl()));
			r.put("label", new JSONString(documentResource.getLabel()));
			
			
			if(_domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference()!=null && 
					_domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference()!=null &&
					((MAnnotationReference)_domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference()).getReference()!=null &&
					((MAnnotationReference)_domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference()).getReference() instanceof MPublicationArticleReference) {
				MPublicationArticleReference ref = (MPublicationArticleReference) ((MAnnotationReference)_domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference()).getReference();
				
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
			
			Request request = builder.sendRequest(r.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					Window.alert("error");
				}
				
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						completionHandler.documentResourceStarred();
					} else if (503 == response.getStatusCode()) {
						_domeo.getLogger().exception(this, "503: " + response.getText());
						Window.alert("503");
						//completionCallback.reportException("Couldn't perform BioPortal term search (503)");
					} else {
						_domeo.getLogger().exception(this,  response.getStatusCode() + "");
						Window.alert("code");
						//completionCallback.reportException("Couldn't perform BioPortal term search " + response.getStatusCode());
					}
				}
			});
		} catch (RequestException e) {
			_domeo.getLogger().exception(this, "Couldn't complete the starring of bibliography request");
			//completionCallback.reportException();
		}	
	}
	
	@Override
	public void unstarResource(MDocumentResource resource, final IStarringRequestCompleted completionHandler) {
		// TODO Auto-generated method stub
		
	}	
}
