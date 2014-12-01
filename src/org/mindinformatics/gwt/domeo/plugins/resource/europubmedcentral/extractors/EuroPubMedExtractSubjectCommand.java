package org.mindinformatics.gwt.domeo.plugins.resource.europubmedcentral.extractors;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.identities.EPubMedExtractor;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model.MPubMedDocument;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.IPubMedItemsRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.PubMedManager;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;
import org.mindinformatics.gwt.utils.src.HtmlUtils;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.IFrameElement;

public class EuroPubMedExtractSubjectCommand extends AEuroPubMedBibliograhyExtractorCommand implements ICommand, 
		IPubMedItemsRequestCompleted {

	private IDomeo _domeo;
	private ICommandCompleted _completionCallback;
	
	public EuroPubMedExtractSubjectCommand(IDomeo domeo, ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_domeo = domeo;
	}
	
	@Override
	public void execute() {
		if(_domeo.getAnnotationPersistenceManager().getBibliographicSet().getLevel()>0) {
			_domeo.getLogger().debug(this.getClass().getName()+":execute()", 
					"Skipping extraction of self citation info...");
			_completionCallback.notifyStageCompletion();
		} else {
			_domeo.getLogger().debug(this.getClass().getName(), 
				"Starting extraction of PubMed document self citation info...");
		
			try {
				IFrameElement iframe = IFrameElement.as(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement());
				final Document frameDocument = iframe.getContentDocument();
				
				List<Element> elements = HtmlUtils.getElementsByClassName(frameDocument, "abs_nonlink_metadata", true);
				if(elements.size()==1) {
					Element e = elements.get(0);
					if(e.getChildCount()==1) {
						String pubmedId = e.getInnerText().substring(e.getInnerText().indexOf(":")+1, e.getInnerText().indexOf(")")).trim();
						if(pubmedId!=null && pubmedId.trim().length()>0) {
							_domeo.getLogger().debug(EuroPubMedDocumentPipeline.LOGGER, 
									this, "Extracting bibliographic object with Pubmed id: " + pubmedId);
							try {
								PubMedManager pubMedManager = PubMedManager.getInstance();
								pubMedManager.selectPubMedConnector(_domeo, this);
								pubMedManager.getBibliographicObject(this, PUBMED_CENTRAL_ID, pubmedId);
							} catch(Exception exc) {
								_domeo.getLogger().exception(EuroPubMedDocumentPipeline.LOGGER, 
									this.getClass().getName(), "Exception 1 while extracting bibliographic object from Euro PubMed " + 
										exc.getMessage());
								_completionCallback.notifyStageCompletion();
								return;
							}
						} else {
							_domeo.getLogger().exception(EuroPubMedDocumentPipeline.LOGGER, 
									this.getClass().getName(), "Exception 2 while extracting bibliographic object from Euro PubMed " + 
									_domeo.getContentPanel().getAnnotationFrameWrapper().getUrl());
							_completionCallback.notifyStageCompletion();
							return;
						}
					} else {
						_domeo.getLogger().exception(EuroPubMedDocumentPipeline.LOGGER, 
								this.getClass().getName(), "Exception 3 while extracting bibliographic object from Euro PubMed " + 
								_domeo.getContentPanel().getAnnotationFrameWrapper().getUrl());
						_completionCallback.notifyStageCompletion();
						return;
					}
				} else {
					_domeo.getLogger().exception(EuroPubMedDocumentPipeline.LOGGER, 
							this.getClass().getName(), "Exception 4 while extracting bibliographic object from Euro PubMed " + 
							_domeo.getContentPanel().getAnnotationFrameWrapper().getUrl());
					_completionCallback.notifyStageCompletion();
					return;
				}	
			} catch (Exception e) {
				_domeo.getLogger().exception(this, 
					"Exception while extracting self citation info " + e.getMessage());
				_completionCallback.notifyStageCompletion();
			}
		}
	}

	@Override
	public void returnBibliographicObject(ArrayList<MPublicationArticleReference> citations) {
		try {
			if(citations.size()>0) {
				MAnnotationReference c = AnnotationFactory.createCitation(
						EPubMedExtractor.getInstance(), (ISoftware) _domeo.getAgentManager().getSoftware(), citations.get(0),
						_domeo.getPersistenceManager().getCurrentResource());
				((MPubMedDocument)_domeo.getPersistenceManager().getCurrentResource()).setSelfReference(c);
				((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getBibliographicSet().addSelfReference(c);
			} 
		} catch (Exception e) {
			_domeo.getLogger().exception(this, "Exception while setting self citation info " + e.getMessage());
		} finally {
			_completionCallback.notifyStageCompletion();
		}
	}

	@Override
	public void bibliographyObjectNotFound() {
		// TODO Auto-generated method stub
		_completionCallback.notifyStageCompletion();
	}

	@Override
	public void bibliographyObjectNotFound(String message) {
		// TODO Auto-generated method stub
		_completionCallback.notifyStageCompletion();
	}
}
