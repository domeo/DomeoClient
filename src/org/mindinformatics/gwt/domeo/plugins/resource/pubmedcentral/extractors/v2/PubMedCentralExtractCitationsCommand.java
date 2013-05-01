package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationCitationReference;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model.MPubMedDocument;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.IPubMedItemsRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.identities.EPubMedCentralExtractor;
import org.mindinformatics.gwt.framework.component.ui.dialog.ProgressMessagePanel;
import org.mindinformatics.gwt.framework.component.ui.glass.DialogGlassPanel;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;
import org.mindinformatics.gwt.utils.src.HtmlTraversalUtils;
import org.mindinformatics.gwt.utils.src.HtmlUtils;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PubMedCentralExtractCitationsCommand implements ICommand, IPubMedItemsRequestCompleted {

	public static final String FUNCTION = IBibliographicParameters.EXTRACT_CITATIONS;
	public static final String LABEL = "Citations extractor";
	public static final String UNRECOGNIZED = "UNRECOGNIZED";
	
	long start;
	IDomeo _domeo;
	List<Element> _elements;
	ICommandCompleted _completionCallback;
	
	
	public PubMedCentralExtractCitationsCommand(IDomeo domeo, ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_domeo = domeo;
	}
	
	@Override
	public void execute() {
		boolean citationsFlag = _domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_CITATIONS).toString().equals(IBibliographicParameters.EXECUTE);
		if(_domeo.getAnnotationPersistenceManager().getBibliographicSet().getLevel()>2 || !citationsFlag) {
			_domeo.getLogger().debug(this.getClass().getName()+":execute()", 
					"Skipping extraction of PubMed Central document citations info...");
			((ProgressMessagePanel)((DialogGlassPanel)_domeo.getDialogPanel()).getPanel()).setMessage("Skipping extraction of document citations...");
			_completionCallback.notifyStageCompletion();
		} else {
			_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, 
					this, "Extracting citations in Pubmed Central document");
			((ProgressMessagePanel)((DialogGlassPanel)_domeo.getDialogPanel()).getPanel()).setMessage("Extraction of document citations...");
			
			try {
				start = System.currentTimeMillis();
				
				IFrameElement iframe = IFrameElement.as(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement());
				final Document frameDocument = iframe.getContentDocument();
	
				_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, "Start");
				
				int _lastIndex=0; // Used to improve the algorithm performance by starting the next step where the previous one ended
				List<Node> textNodes = HtmlTraversalUtils.getTextNodes(HtmlUtils.getBodyNode());
				
				NodeList<Element> ulElement = frameDocument.getElementsByTagName("a");
				for(int i = 0; i< ulElement.getLength(); i++) {
					if(ulElement.getItem(i).getAttribute("rid").startsWith("R")) { // Case 1
	
						Object ref =  ((MPubMedDocument)_domeo.getPersistenceManager().getCurrentResource())
							.getReferenceByIndex(Integer.valueOf(ulElement.getItem(i).getAttribute("rid").substring(ulElement.getItem(i).getAttribute("rid").indexOf("R")+1)));
						
						if(ref!=null) {
							MAnnotationCitationReference ref2 = (MAnnotationCitationReference) ref;
							_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, ulElement.getItem(i).getAttribute("rid")+ "-"+ref2.getLocalId());
							
							String[] prefixExactPostfix = new String[3];
							_lastIndex = HtmlUtils.getPrefixExactPostfixForNode(_lastIndex, prefixExactPostfix, textNodes, ulElement.getItem(i), true);
	
							// TODO find prefix and postfix.
							MTextQuoteSelector selector = AnnotationFactory.createPrefixSuffixTextSelector(
									_domeo.getAgentManager().getUserPerson(), 
									_domeo.getPersistenceManager().getCurrentResource(), prefixExactPostfix[1], prefixExactPostfix[0], prefixExactPostfix[2]);
							ref2.addCitation(selector);
							
							_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, ref2.getReferenceIndex() + "-" +ulElement.getItem(i).getInnerText() + "-" + selector.getExact());
							
							_domeo.getContentPanel().getAnnotationFrameWrapper().performReferencesAnnotation(ref2, selector, ulElement.getItem(i));
						} else {
							_domeo.getLogger().warn(PubMedCentralDocumentPipeline.LOGGER, this, "Not found " + ulElement.getItem(i).getAttribute("rid"));	
						}
					} else if(ulElement.getItem(i).getAttribute("rid").startsWith("b")) { // Case 2 Small numbers case 
						Object ref =  ((MPubMedDocument)_domeo.getPersistenceManager().getCurrentResource())
						.getReferenceByIndex(Integer.valueOf(ulElement.getItem(i).getAttribute("rid").substring(ulElement.getItem(i).getAttribute("rid").indexOf("b")+1, ulElement.getItem(i).getAttribute("rid").indexOf("-"))));
					
						if(ref!=null) {
							MAnnotationCitationReference ref2 = (MAnnotationCitationReference) ref;
							_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, ulElement.getItem(i).getAttribute("rid")+ "-"+ref2.getLocalId());
							
							String[] prefixExactPostfix = new String[3];
							_lastIndex = HtmlUtils.getPrefixExactPostfixForNode(_lastIndex, prefixExactPostfix, textNodes, ulElement.getItem(i), true);
		
							// TODO find prefix and postfix.
							MTextQuoteSelector selector = AnnotationFactory.createPrefixSuffixTextSelector(
									_domeo.getAgentManager().getUserPerson(), 
									_domeo.getPersistenceManager().getCurrentResource(), prefixExactPostfix[1], prefixExactPostfix[0], prefixExactPostfix[2]);
							ref2.addCitation(selector);
							
							_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, ref2.getReferenceIndex() + "-" +ulElement.getItem(i).getInnerText() + "-" + selector.getExact());
							
							_domeo.getContentPanel().getAnnotationFrameWrapper().performReferencesAnnotation(ref2, selector, ulElement.getItem(i));
						} else {
							_domeo.getLogger().warn(PubMedCentralDocumentPipeline.LOGGER, this, "Not found " + ulElement.getItem(i).getAttribute("rid"));	
						}
					} else if(ulElement.getItem(i).getAttribute("rid").startsWith("ref")) { // Case 3 Greenberg paper
						Object ref =  ((MPubMedDocument)_domeo.getPersistenceManager().getCurrentResource())
						.getReferenceByIndex(Integer.valueOf(ulElement.getItem(i).getAttribute("rid").substring(ulElement.getItem(i).getAttribute("rid").indexOf("ref")+3)));
					
						if(ref!=null) {
							MAnnotationCitationReference ref2 = (MAnnotationCitationReference) ref;
							_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, ulElement.getItem(i).getAttribute("rid")+ "-"+ref2.getLocalId());
							
							String[] prefixExactPostfix = new String[3];
							_lastIndex = HtmlUtils.getPrefixExactPostfixForNode(_lastIndex, prefixExactPostfix, textNodes, ulElement.getItem(i), true);
		
							// TODO find prefix and postfix.
							MTextQuoteSelector selector = AnnotationFactory.createPrefixSuffixTextSelector(
									_domeo.getAgentManager().getUserPerson(), 
									_domeo.getPersistenceManager().getCurrentResource(), prefixExactPostfix[1], prefixExactPostfix[0], prefixExactPostfix[2]);
							ref2.addCitation(selector);
							
							_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, ref2.getReferenceIndex() + "-" +ulElement.getItem(i).getInnerText() + "-" + selector.getExact());
							
							_domeo.getContentPanel().getAnnotationFrameWrapper().performReferencesAnnotation(ref2, selector, ulElement.getItem(i));
						} else {
							_domeo.getLogger().warn(PubMedCentralDocumentPipeline.LOGGER, this, "Not found " + ulElement.getItem(i).getAttribute("rid"));	
						}
					}
				}
	
				_domeo.getLogger().debug(this.getClass().getName(), 
						"Extraction of PubMed citations completed in " + (System.currentTimeMillis()-start) + "ms");
				
				
				
				_completionCallback.notifyStageCompletion(); // Necessary as synchronous
			} catch (Exception e) {
				_domeo.getLogger().exception(this,  "Exception while extracting PubMed Central citations " + e.getMessage());
				_completionCallback.notifyStageCompletion();
			}
		}
	}
	
	@Override
	public void returnBibliographicObject(ArrayList<MPublicationArticleReference> citations) {
		if(citations.size()>0) {
			for(int i=0; i<citations.size(); i++) {
				if(citations.get(i)!=null) {
					_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, citations.get(i).getTitle() + "-" +
							_elements.get(i).getInnerText());
					((MPubMedDocument)_domeo.getPersistenceManager().getCurrentResource()).addReference(
							AnnotationFactory.createCitationReference(
								EPubMedCentralExtractor.getInstance(), _domeo.getAgentManager().getSoftware(), i, citations.get(i), 
								_domeo.getPersistenceManager().getCurrentResource(), null)
						);
				}
			}

			_domeo.getLogger().debug(this.getClass().getName(), 
					"Extraction of PubMed Central document references info completed in " + (System.currentTimeMillis()-start) + "ms");
			
			//_domeo.getContentPanel().getAnnotationFrameWrapper().performCitationsAnnotation(list, _elements);
			
			_completionCallback.notifyStageCompletion();
		} else {
			_domeo.getLogger().warn(this, 
					"PubMed Central document references info not found - completed in " + (System.currentTimeMillis()-start) + "ms");
			_completionCallback.notifyStageCompletion();
		}
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
