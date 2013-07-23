package org.mindinformatics.gwt.domeo.plugins.resource.omim.extractors;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationCitationReference;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.resource.omim.model.MOmimDocument;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors.PubMedExtractSubjectCommand;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.IPubMedItemsRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.PubMedManager;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2.PubMedCentralDocumentPipeline;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2.PubMedCentralExtractReferencesCommand;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.identities.EPubMedCentralExtractor;
import org.mindinformatics.gwt.framework.component.ui.dialog.ProgressMessagePanel;
import org.mindinformatics.gwt.framework.component.ui.glass.DialogGlassPanel;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;
import org.mindinformatics.gwt.framework.src.commands.InitUserManagerCommandCallback;
import org.mindinformatics.gwt.utils.src.HtmlUtils;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

public class OmimExtractBibliographicReferencesCommand implements ICommand, IPubMedItemsRequestCompleted {

	long start;
	IDomeo _domeo;
	List<Element> _elements;
	List<String> referenceText;
	
	InitUserManagerCommandCallback _callback;
	ICommandCompleted _completionCallback;
	
	public OmimExtractBibliographicReferencesCommand(IDomeo domeo, InitUserManagerCommandCallback callback,  
			ICommandCompleted completionCallback) {
		_domeo = domeo;
		_callback = callback;
		_completionCallback = completionCallback;
	}
	
	@Override
	public void execute() {
		if(_domeo.getAnnotationPersistenceManager().getBibliographicSet().getLevel()>1) {
			_domeo.getLogger().debug(this.getClass().getName()+":execute()", 
					"Skipping extraction of OMIM document references info...");
			((ProgressMessagePanel)((DialogGlassPanel)_domeo.getDialogPanel()).getPanel()).setMessage("Skipping extraction of document references...");
			_completionCallback.notifyStageCompletion();
		} else {
			_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, 
					this, "execute()", "Extracting document bibliography for OMIM document");
			((ProgressMessagePanel)((DialogGlassPanel)_domeo.getDialogPanel()).getPanel()).setMessage("Extracting document references...");
			
			try {
				start = System.currentTimeMillis();
				
				IFrameElement iframe = IFrameElement.as(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement());
				final Document frameDocument = iframe.getContentDocument();
				
				_elements = new ArrayList<Element>();
				referenceText = new ArrayList<String>();
				
				List<String> pmids = new ArrayList<String>();
				//List<String> pmcids = new ArrayList<String>();
				//List<String> dois = new ArrayList<String>();
				
				// beef
				List<Element> tableElements = HtmlUtils.getElementsByClassName(frameDocument, "reference-text", true);
				if(tableElements.size()>0) {
					for(int i = 0; i< tableElements.size(); i++) {
						_elements.add(((Element)(tableElements.get(i))));
						
						boolean pmidflag = false;
						StringBuffer sb = new StringBuffer();
						NodeList<Node> nodes = tableElements.get(i).getChildNodes();
						for(int j = 0; j<nodes.getLength(); j++) {
							if(j<6 && ((Element)nodes.getItem(j)).getInnerText().trim().length()>0) {
								sb.append(((Element)nodes.getItem(j)).getInnerText() + " ");
							}
							if(j==7) {
								pmidflag = true;
								String refs = ((Element)nodes.getItem(j)).getInnerText();
								int pubmedIdIndexStart = refs.indexOf("[PubMed:") + 8;
								int pubmedIdIndexStop = refs.indexOf(',');
								if(pubmedIdIndexStart>8 && pubmedIdIndexStop>10) {
									pmids.add(refs.substring(pubmedIdIndexStart, pubmedIdIndexStop).trim());
								} else {
									// Manage absence of Pubmed
									pmids.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
								}
							}
						}
						if(!pmidflag) pmids.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
						referenceText.add(sb.toString().trim().replaceAll("\\<.*?>",""));
					}
				}			
				
				_domeo.getLogger().debug(this, "Duration: " + (System.currentTimeMillis()-start) + "ms");
				_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, "Exit sequence");
				
				StringBuffer sb = new StringBuffer();
				for(int i=0; i<pmids.size(); i++) {
					sb.append("#"+i);
					sb.append(". ");
					sb.append(pmids.get(i));
					sb.append(" - ");
				}
				_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, 
						this, "PubMed Ids: " +sb.toString());
				
				PubMedManager pubMedManager = PubMedManager.getInstance();
				pubMedManager.selectPubMedConnector(_domeo, this);
				pubMedManager.getBibliographicObjectsByText(this, PubMedExtractSubjectCommand.PUBMED_IDS, pmids, referenceText);	
				
			} catch (Exception e) {
				_domeo.getLogger().exception(this,  "Exception while extracting OMIM references " + e.getMessage());
				_completionCallback.notifyStageCompletion();
			}
		}
		// TODO Auto-generated method stub	
		//_completionCallback.notifyStageCompletion(); 
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

	@Override
	public void returnBibliographicObject(ArrayList<MPublicationArticleReference> citations) {
		
		if(citations.size()>0) {
			ArrayList<MAnnotationCitationReference> list = new ArrayList<MAnnotationCitationReference>();
			for(int i=0; i<citations.size(); i++) {
				if(citations.get(i)!=null) {
					if(citations.get(i).getUnrecognized()!=null && citations.get(i).getUnrecognized().equals("UNRECOGNIZED")) {
						_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, i + "- UNRECOGNIZED -" + referenceText.get(i));
						citations.get(i).setUnrecognized(referenceText.get(i));
					} else {
						_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, i + "-" + citations.get(i).getTitle() + "-" + referenceText.get(i));
					}
						
					MTextQuoteSelector selector = AnnotationFactory.createPrefixSuffixTextSelector(
							_domeo.getAgentManager().getUserPerson(), 
							_domeo.getPersistenceManager().getCurrentResource(), referenceText.get(i), "", "");
					
					MAnnotationCitationReference r = AnnotationFactory.createReference( 
							EPubMedCentralExtractor.getInstance(), _domeo.getAgentManager().getSoftware(), i, citations.get(i), 
							_domeo.getPersistenceManager().getCurrentResource(), selector);
					((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getBibliographicSet().addReference(r);
					//((AnnotationPersistenceManager)_domeo.getPersistenceManager()).addAnnotation(r, ((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getBibliographicSet());
					((MOmimDocument)_domeo.getPersistenceManager().getCurrentResource()).addReference(r);
					
					list.add(r);
				}
			}
			_domeo.getLogger().debug(this.getClass().getName(), 
					"Extraction of OMIM document references info completed in " + (System.currentTimeMillis()-start) + "ms");
			
			_domeo.getContentPanel().getAnnotationFrameWrapper().performReferencesAnnotation(list, _elements);
			
			_completionCallback.notifyStageCompletion();
		} else {
			_domeo.getLogger().warn(this, 
					"OMIM document references info not found - completed in " + (System.currentTimeMillis()-start) + "ms");
			_completionCallback.notifyStageCompletion();
		}
	}
}
