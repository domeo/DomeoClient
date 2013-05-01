package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationCitationReference;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors.PubMedExtractSubjectCommand;
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

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PubMedCentralExtractReferencesCommand implements ICommand, IPubMedItemsRequestCompleted {

	public static final String FUNCTION = IBibliographicParameters.EXTRACT_REFERENCES;
	public static final String LABEL = "References extractor";
	public static final String UNRECOGNIZED = "UNRECOGNIZED";
	
	long start;
	IDomeo _domeo;
	List<Element> _elements;
	List<String> referenceText;
	ICommandCompleted _completionCallback;
	
	
	public PubMedCentralExtractReferencesCommand(IDomeo domeo, ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_domeo = domeo;		
	}
	
	@Override
	public void execute() {
		if(_domeo.getAnnotationPersistenceManager().getBibliographicSet().getLevel()>1) {
			_domeo.getLogger().debug(this.getClass().getName()+":execute()", 
					"Skipping extraction of PubMed Central document references info...");
			((ProgressMessagePanel)((DialogGlassPanel)_domeo.getDialogPanel()).getPanel()).setMessage("Skipping extraction of document references...");
			_completionCallback.notifyStageCompletion();
		} else {
			_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, 
					this, "execute()", "Extracting document bibliography for Pubmed Central document");
			((ProgressMessagePanel)((DialogGlassPanel)_domeo.getDialogPanel()).getPanel()).setMessage("Extracting document references...");
			
			try {
				start = System.currentTimeMillis();
				
				IFrameElement iframe = IFrameElement.as(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement());
				final Document frameDocument = iframe.getContentDocument();
				
				_elements = new ArrayList<Element>();
				List<String> pmids = new ArrayList<String>();
				List<String> pmcids = new ArrayList<String>();
				List<String> dois = new ArrayList<String>();
				/*List<String>*/ referenceText = new ArrayList<String>();
				
				//_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, "Start");
				
				List<Element> ulElement = HtmlUtils.getElementsByClassName(frameDocument, "back-ref-list", true);
				if(ulElement.size()>0) {
					_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, "execute(1)", "Case 1");
					long start = System.currentTimeMillis();
					NodeList<Node> liElements = ulElement.get(0).getChildNodes();
					for(int i = 0; i< liElements.getLength(); i++) {
						_elements.add(((Element)liElements.getItem(i)).getFirstChildElement());
	
						referenceText.add(((Element)liElements.getItem(i)).getInnerHTML().replaceAll("\\<.*?>",""));
						int firstIndex = ((Element)liElements.getItem(i)).getInnerHTML().indexOf('[');
						int lastIndex = ((Element)liElements.getItem(i)).getInnerHTML().lastIndexOf(']');
						if(firstIndex!=-1 && lastIndex!=-1) {
							String substring = ((Element)liElements.getItem(i)).getInnerHTML().substring(firstIndex, lastIndex);
							_domeo.getLogger().debug(this, substring);
							
							boolean pmFlag = false;
							boolean pmcFlag = false;
							boolean doiFlag = false;
							
							String[] splits = substring.split("\\[");
							for(String sub: splits) {
								if(sub.contains("PubMed")) {
									String subsub = sub.substring(sub.indexOf("href=\"")+6);
									String url = subsub.substring(0, subsub.indexOf("\""));
									_domeo.getLogger().debug(this, "PubMed:: " + url.substring(subsub.indexOf("pubmed/")+7));
									pmids.add(url.substring(subsub.indexOf("pubmed/")+7));
									pmFlag = true;
								} else if(sub.contains("PMC free article")) {
									String subsub = sub.substring(sub.indexOf("href=\"")+6);
									String url = subsub.substring(0, subsub.indexOf("\""));
									_domeo.getLogger().debug(this, "PMC free article:: " + url.substring(subsub.indexOf("articles/")+9, url.length()-2));
									pmcFlag = true;
								} else if(sub.contains("Cross Ref")) {
									String subsub = sub.substring(sub.indexOf("href=\"")+6);
									String url = subsub.substring(0, subsub.indexOf("\""));
									_domeo.getLogger().debug(this, "Cross Ref:: " +  url.substring(subsub.indexOf("crossref.org/")+13));
									doiFlag = true;
								}
							}
							
							if(!pmFlag) pmids.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
							if(!pmcFlag) pmcids.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
							if(!doiFlag) dois.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
						} else {
							pmids.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
							pmcids.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
							dois.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);	
						}
					}
					_domeo.getLogger().debug(this, "Duration: " + (System.currentTimeMillis()-start) + "ms");
				} else {
					_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, "execute(2)", "Case 2");
					long start = System.currentTimeMillis();
					
					// Example http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3059018/
					Element container = frameDocument.getElementById("reference-list");
					if(container!=null) {
						NodeList<Node> liElements = container.getChildNodes();
						for(int i = 0; i< liElements.getLength(); i++) {
							_elements.add(((Element)liElements.getItem(i)).getFirstChildElement());
	
							referenceText.add(((Element)liElements.getItem(i)).getInnerHTML().replaceAll("\\<.*?>",""));
							int firstIndex = ((Element)liElements.getItem(i)).getInnerHTML().indexOf('[');
							int lastIndex = ((Element)liElements.getItem(i)).getInnerHTML().lastIndexOf(']');
							if(firstIndex!=-1 && lastIndex!=-1) {
								String substring = ((Element)liElements.getItem(i)).getInnerHTML().substring(firstIndex, lastIndex);
								_domeo.getLogger().debug(this, substring);
								
								boolean pmFlag = false;
								boolean pmcFlag = false;
								boolean doiFlag = false;
								
								String[] splits = substring.split("\\[");
								for(String sub: splits) {
									if(sub.contains("PubMed")) {
										String subsub = sub.substring(sub.indexOf("href=\"")+6);
										String url = subsub.substring(0, subsub.indexOf("\""));
										_domeo.getLogger().debug(this, "PubMed:: " + url.substring(subsub.indexOf("pubmed/")+7));
										pmids.add(url.substring(subsub.indexOf("pubmed/")+7));
										pmFlag = true;
									} else if(sub.contains("PMC free article")) {
										String subsub = sub.substring(sub.indexOf("href=\"")+6);
										String url = subsub.substring(0, subsub.indexOf("\""));
										_domeo.getLogger().debug(this, "PMC free article:: " + url.substring(subsub.indexOf("articles/")+9, url.length()-2));
										pmcFlag = true;
									} else if(sub.contains("Cross Ref")) {
										String subsub = sub.substring(sub.indexOf("href=\"")+6);
										String url = subsub.substring(0, subsub.indexOf("\""));
										_domeo.getLogger().debug(this, "Cross Ref:: " +  url.substring(subsub.indexOf("crossref.org/")+13));
										doiFlag = true;
									}
								}
								
								if(!pmFlag) pmids.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
								if(!pmcFlag) pmcids.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
								if(!doiFlag) dois.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
							} else {
								pmids.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
								pmcids.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
								dois.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);	
							}
						}
					}
					_domeo.getLogger().debug(this, "Duration: " + (System.currentTimeMillis()-start) + "ms");
				}
				
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
				
				/*
				List<Element> ulElement = HtmlUtils.getElementsByClassName(frameDocument, "back-ref-list", true);
				if(ulElement.size()>0) {
					_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, "execute(1)", "Case 1");
					NodeList<Node> liElements = ulElement.get(0).getChildNodes();
					for(int i = 0; i< liElements.getLength(); i++) {
						List<Element> es = HtmlUtils.getElementsByClassNameFrom((Element)liElements.getItem(i), "ref-extlink", true);
						if(es.size()>0) {
							String href = es.get(0).getAttribute("href");
							int pubmedIndex = href.indexOf("pubmed");
							if(pubmedIndex>0) {
								String pubmedId = href.substring(pubmedIndex).replaceAll("pubmed/", "");
								pubmedIds.add(pubmedId);
								_elements.add((Element)liElements.getItem(i));
							} else {
								// If crossref it goes here - Can be improved
								pubmedIds.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
								_elements.add((Element)liElements.getItem(i));
								unrecognized.add(((Element)liElements.getItem(i)).getInnerText());
							}
						} else {
							pubmedIds.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
							_elements.add((Element)liElements.getItem(i));
							unrecognized.add(((Element)liElements.getItem(i)).getInnerText());
						}
					}
				} else {
					_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, "execute(2)", "Case 2");
					ulElement = HtmlUtils.getElementsByClassName(frameDocument, "back-matter-section", true);
					if(ulElement.size()>0 && ulElement.get(0).getTagName().equalsIgnoreCase("ul")) {
						if(ulElement.size() >0) {
							NodeList<Node> liElements = ulElement.get(0).getChildNodes();
							for(int i = 0; i< liElements.getLength(); i++) {
								List<Element> citation = HtmlUtils.getElementsByClassNameFrom((Element)liElements.getItem(i), "ref-extlink", true);
								if(citation.size()>0) {
									String href = citation.get(0).getAttribute("href");
									if(href!=null && !href.isEmpty()) {
										int pubmedIndex = href.indexOf("pubmed");
										if(pubmedIndex>0) {
											String pubmedId = href.substring(pubmedIndex).replaceAll("pubmed/", "");
											pubmedIds.add(pubmedId);
											_elements.add((Element)liElements.getItem(i));
										}
									}
								} else {
									pubmedIds.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
									_elements.add(((Element)liElements.getItem(i)));
									unrecognized.add(((Element)liElements.getItem(i)).getInnerText());
								}					
							}
						} else {
							_domeo.getLogger().warn(PubMedCentralDocumentPipeline.EXCEPTION, 
								this.getClass().getName(), "Pubmed Central model seems changed");
							_completionCallback.notifyStageCompletion();
							return;
						} 
						
					} else if(ulElement.size()>0 && ulElement.get(0).getTagName().equalsIgnoreCase("div")) {
						if(ulElement.get(0).getChildNodes().getLength() >0) {
							NodeList<Element> ulList = ulElement.get(0).getElementsByTagName("ul");
							if(ulList.getLength()>0) {
								_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, "Variant 1");
								NodeList<Node> liElements = ulList.getItem(0).getChildNodes();
								for(int i = 0; i< liElements.getLength(); i++) {
									List<Element> citation = HtmlUtils.getElementsByClassNameFrom((Element)liElements.getItem(i), "ref-extlink", true);
									if(citation.size()>0) {
										String href = citation.get(0).getAttribute("href");
										if(href!=null && !href.isEmpty()) {
											int pubmedIndex = href.indexOf("pubmed");
											if(pubmedIndex>0) {
												String pubmedId = href.substring(pubmedIndex).replaceAll("pubmed/", "");
												_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, 
														this.getClass().getName(), "pubmedId " + pubmedId);
												pubmedIds.add(pubmedId);
												_elements.add((Element)liElements.getItem(i));
											}
										}
									} else {
										_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, 
												this.getClass().getName(), "UNRECOGNIZED " + ((Element)liElements.getItem(i)).getInnerText());
										pubmedIds.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
										_elements.add(((Element)liElements.getItem(i)));
										unrecognized.add(((Element)liElements.getItem(i)).getInnerText());
									}					
								}
							} else {
								_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, "Variant 2");
								NodeList<Element> divElements = ulElement.get(0).getElementsByTagName("div");
								if(divElements.getLength()>0) {
									for(int i = 0; i< divElements.getLength(); i++) {
										List<Element> citation = HtmlUtils.getElementsByClassNameFrom((Element)divElements.getItem(i), "ref-extlink", true);
										if(citation.size()>0) {
											String href = citation.get(0).getAttribute("href");
											if(href!=null && !href.isEmpty()) {
												int pubmedIndex = href.indexOf("pubmed");
												if(pubmedIndex>0) {
													String pubmedId = href.substring(pubmedIndex).replaceAll("pubmed/", "");
													_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, 
															this.getClass().getName(), "pubmedId " + pubmedId);
													pubmedIds.add(pubmedId);
													_elements.add((Element)divElements.getItem(i));
												}
											}
										} else {
											_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, 
													this.getClass().getName(), "UNRECOGNIZED " + ((Element)divElements.getItem(i)).getInnerText());
											pubmedIds.add(PubMedCentralExtractReferencesCommand.UNRECOGNIZED);
											_elements.add(((Element)divElements.getItem(i)));
											unrecognized.add(((Element)divElements.getItem(i)).getInnerText());
										}					
									}
								} else {
									_domeo.getLogger().warn(PubMedCentralDocumentPipeline.EXCEPTION, 
											this.getClass().getName(), "An unknown Pubmed Central template has been detected.");
								}
							}
						} else {
							_domeo.getLogger().warn(PubMedCentralDocumentPipeline.EXCEPTION, 
									this.getClass().getName(), "An unknown Pubmed Central template has been detected.");
							_completionCallback.notifyStageCompletion();
							return;
						} 
					} else {
						_domeo.getLogger().warn(PubMedCentralDocumentPipeline.EXCEPTION, 
								this.getClass().getName(), "PubMed Central format not recognized");
						_domeo.getReportManager().sendWidgetAsEmail(this.getClass().getName(), 
								"PubMed Central Extractor Problem", new Label("PubMed Central document references not found for document " + 
								_domeo.getPersistenceManager().getCurrentResourceUrl()), _domeo.getPersistenceManager().getCurrentResourceUrl());
						_completionCallback.notifyStageCompletion();
						return;
					}
				}
				
				
				_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, "Exit sequence");
				
				StringBuffer sb = new StringBuffer();
				for(int i=0; i<pubmedIds.size(); i++) {
					sb.append("#"+i);
					sb.append(". ");
					sb.append(pubmedIds.get(i));
					sb.append(" - ");
				}
				_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, 
						this, "PubMed Ids: " +sb.toString());
				
				PubMedManager pubMedManager = PubMedManager.getInstance();
				pubMedManager.selectPubMedConnector(_domeo, this);
				pubMedManager.getBibliographicObjects(this, PubMedExtractSubjectCommand.PUBMED_IDS, pubmedIds, _elements);	
				*/		
			} catch (Exception e) {
				_domeo.getLogger().exception(this,  "Exception while extracting PubMed Central references " + e.getMessage());
				_completionCallback.notifyStageCompletion();
			}
		}
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
					((MPubMedDocument)_domeo.getPersistenceManager().getCurrentResource()).addReference(r);
					
					list.add(r);
				}
			}
			_domeo.getLogger().debug(this.getClass().getName(), 
					"Extraction of PubMed Central document references info completed in " + (System.currentTimeMillis()-start) + "ms");
			
			_domeo.getContentPanel().getAnnotationFrameWrapper().performReferencesAnnotation(list, _elements);
			
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
		_completionCallback.notifyStageCompletion();
	}

	@Override
	public void bibliographyObjectNotFound(String message) {
		// TODO Auto-generated method stub
		_completionCallback.notifyStageCompletion();
	}
}
