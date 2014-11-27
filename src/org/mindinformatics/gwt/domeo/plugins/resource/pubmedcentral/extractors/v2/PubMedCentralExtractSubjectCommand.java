package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors.APubMedBibliograhyExtractorCommand;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model.MPubMedDocument;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.IPubMedItemsRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.PubMedManager;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.identities.EPubMedCentralExtractor;
import org.mindinformatics.gwt.framework.component.ui.dialog.ProgressMessagePanel;
import org.mindinformatics.gwt.framework.component.ui.glass.DialogGlassPanel;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;
import org.mindinformatics.gwt.utils.src.HtmlUtils;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.ui.Label;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PubMedCentralExtractSubjectCommand extends APubMedBibliograhyExtractorCommand implements ICommand, 
	IPubMedItemsRequestCompleted {

	public static final String FUNCTION = IBibliographicParameters.EXTRACT_SUBJECT;
	public static final String LABEL = "Subject extractor";
	
	IDomeo _domeo;
	ICommandCompleted _completionCallback;
	
	public PubMedCentralExtractSubjectCommand(IDomeo domeo, ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_domeo = domeo;
	}
	
	long start;
	
	@Override
	public void execute() {
		if(_domeo.getAnnotationPersistenceManager().getBibliographicSet().getLevel()>0) {
			_domeo.getLogger().debug(this.getClass().getName()+":execute()", 
					"Skipping extraction of self citation info... " + _domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference());
			((ProgressMessagePanel)((DialogGlassPanel)_domeo.getDialogPanel()).getPanel()).setMessage("Skipping extraction of self citation info...");
			_completionCallback.notifyStageCompletion();
		} else {
			_domeo.getLogger().debug(this.getClass().getName()+":execute()", 
				"Starting extraction of self citation info...");
			((ProgressMessagePanel)((DialogGlassPanel)_domeo.getDialogPanel()).getPanel()).setMessage("Extracting self citation info...");
		
			try {
				start = System.currentTimeMillis();
				
				IFrameElement iframe = IFrameElement.as(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement());
				final Document frameDocument = iframe.getContentDocument();
				
				String pmcId = null;
				boolean hit = false;
				
				List<Element> elementsCase1 = HtmlUtils.getElementsByClassName(frameDocument, "accid", true);
				if(elementsCase1.size()>0) {
					pmcId = elementsCase1.get(0).getInnerText();
					if(pmcId!=null && pmcId.trim().length()>0 && pmcId.trim().startsWith("PMC")) {
						hit = true;
						_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, 
								this, "execute(1)", "Extracting bibliographic object with Pubmed Central id: " + pmcId);
					}
				} else {
					List<Element> elementsCase2 = HtmlUtils.getElementsByClassName(frameDocument, "fm-citation-pmcid", true);
					if(elementsCase2.size()>0) {
						Element e = elementsCase2.get(0);
						NodeList<Node> nl = e.getChildNodes();
						if(nl.getLength()==2) {
							pmcId =  ((Element) nl.getItem(1)).getInnerText();
							if(pmcId!=null && pmcId.trim().length()>0 && pmcId.trim().startsWith("PMC")) {
								hit = true;
								_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, 
										this, "execute(2)", "Extracting bibliographic object with Pubmed Central id: " + 
												((Element) nl.getItem(1)).getInnerText());
							}
						}
					}
				}
				
				if(hit) {
					try {
						PubMedManager pubMedManager = PubMedManager.getInstance();
						pubMedManager.selectPubMedConnector(_domeo, this);
						pubMedManager.getBibliographicObject(this, PUBMED_CENTRAL_ID, pmcId);
					} catch(Exception exc) {
						_domeo.getLogger().exception(PubMedCentralDocumentPipeline.LOGGER, 
							this, "Exception while retrieving bibliographic object from PubMed Central " + 
								exc.getMessage());
						_completionCallback.notifyStageCompletion();
						return;
					}
				} else {
					_domeo.getLogger().exception(PubMedCentralDocumentPipeline.LOGGER, 
							this, "Exception while extracting bibliographic object from PubMed Central " + 
							_domeo.getContentPanel().getAnnotationFrameWrapper().getUrl());
					_domeo.getReportManager().sendWidgetAsEmail(this.getClass().getName(), 
							"PubMed Central Extractor Problem", new Label("PubMed Central id info not found for document " + 
							_domeo.getPersistenceManager().getCurrentResourceUrl()), _domeo.getPersistenceManager().getCurrentResourceUrl());
	
					_completionCallback.notifyStageCompletion();
					return;
				}
			} catch (Exception e) {
				_domeo.getLogger().exception(this, 
						"Exception while extracting PubMed Central document citation info " + e.getMessage());
				_domeo.getReportManager().sendWidgetAsEmail(this.getClass().getName(), 
						"PubMed Central Extractor Problem", new Label("Exception while extracting citation info for document " + 
						_domeo.getPersistenceManager().getCurrentResourceUrl()), _domeo.getPersistenceManager().getCurrentResourceUrl());	
				_completionCallback.notifyStageCompletion();
			}
		}
	}

	@Override
	public void returnBibliographicObject(ArrayList<MPublicationArticleReference> citations) {
		_domeo.getLogger().debug(this, "# Returned Bibliographic Objects: " + citations.size());
		if(citations.size()>0) {
			try {
				MAnnotationReference annotation = AnnotationFactory.createCitation(
						EPubMedCentralExtractor.getInstance(), _domeo.getAgentManager().getSoftware(), citations.get(0), _domeo.getPersistenceManager().getCurrentResource());

				((MPubMedDocument)_domeo.getPersistenceManager().getCurrentResource()).setSelfReference(annotation);
				((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getBibliographicSet().addAnnotation(annotation);
				
				//((AnnotationPersistenceManager)_domeo.getPersistenceManager()).addAnnotation(annotation, ((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getBibliographicSet());
				_domeo.getLogger().debug(this, "returnBibliographicObject()", 
						"Extraction of PubMed Central citation info completed in " + (System.currentTimeMillis()-start) + "ms");
				_completionCallback.notifyStageCompletion();
			} catch (Exception e) {
				_domeo.getLogger().exception(this, 
						"Exception while setting PubMed Central document citation info " + e.getMessage());
				_completionCallback.notifyStageCompletion();
			}
		} else {
			// TODO Communicate the anomaly			
			_domeo.getLogger().warn(this, "returnBibliographicObject()", 
					"PubMed Central document citation info not found - completed in " + (System.currentTimeMillis()-start) + "ms");
			
			_domeo.getReportManager().sendWidgetAsEmail(this.getClass().getName()+":returnBibliographicObject()", 
					"PubMed Central Extractor Problem", new Label("PubMed Central citation info not found for document " + 
					_domeo.getPersistenceManager().getCurrentResourceUrl()), _domeo.getPersistenceManager().getCurrentResourceUrl());
			
			// This composes a citation when it is not possible to find the entry in PubMed
			try {
				start = System.currentTimeMillis();
				
				IFrameElement iframe = IFrameElement.as(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement());
				final Document frameDocument = iframe.getContentDocument();
				
				MPublicationArticleReference reference = new MPublicationArticleReference();
				
				List<Element> elements = HtmlUtils.getElementsByClassName(frameDocument, "fm-citation-pmcid", true);
				if(elements.size()>0) {
					Element e = elements.get(0);
					NodeList<Node> nl = e.getChildNodes();
					if(nl.getLength()==2) {
						String pubMedCentralId =  ((Element) nl.getItem(1)).getInnerText();
						if(pubMedCentralId!=null && pubMedCentralId.trim().length()>0) {
							reference.setPubMedCentralId(pubMedCentralId);
						}
					}
				}
				
				List<Element> elementsTitle = HtmlUtils.getElementsByClassName(frameDocument, "fm-title", true);
				if(elementsTitle.size()>0) {
					Element e = elementsTitle.get(0);
					reference.setTitle(e.getInnerText());
				} else {
					List<Element> contentTitle = HtmlUtils.getElementsByClassName(frameDocument, "content-title", true);
					if(contentTitle.size()>0) {
						Element e = contentTitle.get(0);
						reference.setTitle(e.getInnerText());
					}
				}
				
				List<Element> elementsAuthors = HtmlUtils.getElementsByClassName(frameDocument, "fm-author", true);
				if(elementsAuthors.size()>0) {
					Element e = elementsAuthors.get(0);
					reference.setAuthorNames(e.getInnerText());
				}
				
				List<Element> elementsCitation = HtmlUtils.getElementsByClassName(frameDocument, "fm-citation", true);
				if(elementsCitation.size()>0) {
					Element e = elementsCitation.get(0);
					if(e.getInnerText().contains("doi:")) 
						reference.setJournalPublicationInfo(e.getInnerText().substring(0, e.getInnerText().indexOf("doi")));
					else
						reference.setJournalPublicationInfo(e.getInnerText());
				}
				
				List<Element> elementsDoi = HtmlUtils.getElementsByClassName(frameDocument, "fm-vol-iss-date", false);
				if(elementsDoi.size()>0) {
					for(Element e: elementsDoi) {
						if(e.getInnerText().trim().startsWith("doi:")) {
							reference.setDoi(e.getInnerText().trim().substring(e.getInnerText().trim().indexOf(' ')));
							break;
						}
					}
				}
				
				MAnnotationReference a = AnnotationFactory.createCitation(
						EPubMedCentralExtractor.getInstance(), _domeo.getAgentManager().getSoftware(), reference, _domeo.getPersistenceManager().getCurrentResource());

				((MPubMedDocument)_domeo.getPersistenceManager().getCurrentResource()).setSelfReference(a);
				_domeo.getLogger().debug(this.getClass().getName(), 
						"Creation of PubMed Central document citation info completed in " + (System.currentTimeMillis()-start) + "ms");
			} catch(Exception e) {
				_domeo.getLogger().exception(this, 
						"Exception while creating PubMed Central document citation info " + e.getMessage());
			}
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
