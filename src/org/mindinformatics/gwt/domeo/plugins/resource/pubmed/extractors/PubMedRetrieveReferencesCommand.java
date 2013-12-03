package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationCitationReference;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingBibliographySetHandler;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling.JsonUnmarshallingManager;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model.MPubMedDocument;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.IPubMedItemsRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.PubMedManager;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.identities.EPubMedCentralExtractor;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.core.client.JsArray;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PubMedRetrieveReferencesCommand implements ICommand, IPubMedItemsRequestCompleted, IRetrieveExistingBibliographySetHandler {

	public static final String LABEL = "References fetcher";
	public static final String UNRECOGNIZED = "UNRECOGNIZED";
	
	//private long start;
	private IDomeo _domeo;
	// List<Element> _elements;
	private List<String> referenceText;
	private ICommandCompleted _completionCallback;
	
	
	public PubMedRetrieveReferencesCommand(IDomeo domeo, ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_domeo = domeo;
	}
	
	@Override
	public void execute() {
		if(_domeo.getAnnotationPersistenceManager().getBibliographicSet().getLevel()>1) {
			_domeo.getLogger().debug(this.getClass().getName()+":execute()", 
					"Skipping extraction of PubMed Central document references info...");
			_completionCallback.notifyStageCompletion();
		} else {
			_domeo.getLogger().debug(PubMedDocumentPipeline.LOGGER, 
					this, "execute()", "Retrieving references for Pubmed document");
			
			try {
				PubMedManager pubMedManager = PubMedManager.getInstance();
				pubMedManager.selectPubMedConnector(_domeo, this);
				pubMedManager.retrieveExistingBibliographySet(_domeo, this, 2);
			} catch (Exception e) {
				_domeo.getLogger().exception(this,  "Exception while extracting PubMed Central references " + e.getMessage());
				_completionCallback.notifyStageCompletion();
			}
			//_completionCallback.notifyStageCompletion();
		}
	}
	
	@Override
	public void setExistingBibliographySetList(JsArray responseOnSets, boolean isVirtual) {
		// TODO Auto-generated method stub
		
		_domeo.getLogger().debug(this, "Is virtual: " + isVirtual);
		JsonUnmarshallingManager manager = _domeo.getUnmarshaller();
		if(isVirtual) 
			manager.unmarshallVirtualBibliography(responseOnSets, true, 0);
		else 
			manager.unmarshallBibliography(responseOnSets, true, 0);
		_completionCallback.notifyStageCompletion();
	}
	
	@Override
	public void bibliographySetListNotCreated() {
		_completionCallback.notifyStageCompletion();
	}
	
	@Override
	public void bibliographySetListNotCreated(String message) {
		_completionCallback.notifyStageCompletion();
	}
	
	@Override
	public void returnBibliographicObject(ArrayList<MPublicationArticleReference> citations) {
		if(citations.size()>0) {
			ArrayList<MAnnotationCitationReference> list = new ArrayList<MAnnotationCitationReference>();
			for(int i=0; i<citations.size(); i++) {
				if(citations.get(i)!=null) {
					if(citations.get(i).getUnrecognized()!=null && citations.get(i).getUnrecognized().equals("UNRECOGNIZED")) {
						_domeo.getLogger().debug(PubMedDocumentPipeline.LOGGER, this, i + "- UNRECOGNIZED -" + referenceText.get(i));
						citations.get(i).setUnrecognized(referenceText.get(i));
					} else {
						_domeo.getLogger().debug(PubMedDocumentPipeline.LOGGER, this, i + "-" + citations.get(i).getTitle() + "-" + referenceText.get(i));
					}
						
					MTextQuoteSelector selector = AnnotationFactory.createPrefixSuffixTextSelector(
							_domeo.getAgentManager().getUserPerson(), 
							_domeo.getPersistenceManager().getCurrentResource(), referenceText.get(i), "", "");
					
					MAnnotationCitationReference r = AnnotationFactory.createReference( 
							EPubMedCentralExtractor.getInstance(), _domeo.getAgentManager().getSoftware(), i, citations.get(i), 
							_domeo.getPersistenceManager().getCurrentResource(), selector);
					((MPubMedDocument)_domeo.getPersistenceManager().getCurrentResource()).addReference(r);
					
					// The virtual references are not added to the bibliographic set
					// as they don't have to get saved again.
					//((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getBibliographicSet().addReference(r);
					//((AnnotationPersistenceManager)_domeo.getPersistenceManager()).addAnnotation(r, ((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getBibliographicSet());
					
					list.add(r);
				}
			}
		} else {
			_domeo.getLogger().warn(this, "PubMed references info not found");
		}
		_completionCallback.notifyStageCompletion();
	}

	@Override
	public void bibliographyObjectNotFound() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bibliographyObjectNotFound(String message) {
		// TODO Auto-generated method stub
		
	}
}
