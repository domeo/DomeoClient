package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2;

import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors.APubMedBibliograhyExtractorCommand;
import org.mindinformatics.gwt.framework.component.pipelines.src.IParametersCache;
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
public class PubMedCentralExtractSubjectIdentityCommand extends APubMedBibliograhyExtractorCommand implements ICommand {

	public static final String FUNCTION = IBibliographicParameters.EXTRACT_SUBJECT;
	public static final String LABEL = "Subject extractor";
	
	IDomeo _domeo;
	IParametersCache _parametersCache;
	ICommandCompleted _completionCallback;
	
	public PubMedCentralExtractSubjectIdentityCommand(IDomeo domeo, IParametersCache parametersCache, ICommandCompleted completionCallback) {
		_domeo = domeo;
		_parametersCache = parametersCache;
		_completionCallback = completionCallback;
	}
	
	long start;
	
	@Override
	public void execute() {
		_domeo.getLogger().debug(this.getClass().getName()+":execute()", 
			"Starting extraction of PubMed Central document citation info...");
	
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
				_parametersCache.addParam(PUBMED_CENTRAL_ID, pmcId);
				_completionCallback.notifyStageCompletion();
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
