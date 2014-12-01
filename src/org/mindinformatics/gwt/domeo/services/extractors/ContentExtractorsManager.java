package org.mindinformatics.gwt.domeo.services.extractors;

import java.util.LinkedHashMap;
import java.util.Map;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.document.extractor.GenericDocumentPipeline;
import org.mindinformatics.gwt.domeo.plugins.resource.europubmedcentral.extractors.EuroPubMedDocumentPipeline;
import org.mindinformatics.gwt.domeo.plugins.resource.europubmedcentral.info.EuroPmcPlugin;
import org.mindinformatics.gwt.domeo.plugins.resource.omim.extractors.OmimDocumentPipeline;
import org.mindinformatics.gwt.domeo.plugins.resource.omim.info.OmimPlugin;
import org.mindinformatics.gwt.domeo.plugins.resource.opentrials.extractor.OpenTrialsDocumentPipeline;
import org.mindinformatics.gwt.domeo.plugins.resource.opentrials.info.OpenTrialsPlugin;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors.PubMedDocumentPipeline;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.info.PubMedPlugin;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2.PubMedCentralDocumentPipeline;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.info.PmcPlugin;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;

/**
 * This class manages all the extractors or extraction pipelines.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ContentExtractorsManager implements IExtractorsManager, IInitializableComponent {

	// Prefixes for logging
	public static final String ANALYZER_PLUGIN = "PLUGIN::EXTRACTOR::";
	public static final String EXTRACTION_START = "EXTRACTION START";
	public static final String EXTRACTION_PROGRESS = "EXTRACTION PROGRESS";
	public static final String EXTRACTION_PROBLEM = "EXTRACTION PROBLEM";
	
	private IDomeo _domeo;
	private IContentExtractor _currentExtractor;
	
	private LinkedHashMap<String, IContentExtractor> extractors = new LinkedHashMap<String, IContentExtractor>();
	
	public ContentExtractorsManager(IDomeo domeo) {
		_domeo = domeo;
		
		domeo.getLogger().debug(this, "Initializing the content extractor manager");
		
		// EXTRACTION PIPELINES REGISTRATION
		// ---------------------------------
		
		// OMIM PIPELINE
		if(_domeo.getProfileManager().getUserCurrentProfile().isPluginEnabled(OmimPlugin.getInstance().getPluginName())) {
			registerExtractor(new OmimDocumentPipeline(_domeo));
		}
		// PUBMED & PUBMED CENTRAL PIPELINE
		if(_domeo.getProfileManager().getUserCurrentProfile().isPluginEnabled(PubMedPlugin.getInstance().getPluginName())) {
			registerExtractor(new PubMedDocumentPipeline(_domeo));
			if(_domeo.getProfileManager().getUserCurrentProfile().isPluginEnabled(PmcPlugin.getInstance().getPluginName())) {
				registerExtractor(new PubMedCentralDocumentPipeline(_domeo));
			}
		}
		// EURO PUBMED CENTRAL PIPELINE
		if(_domeo.getProfileManager().getUserCurrentProfile().isPluginEnabled(EuroPmcPlugin.getInstance().getPluginName())) {
			registerExtractor(new EuroPubMedDocumentPipeline(_domeo));			
		}
		// OPENTRIALS PIPELINE
		if(_domeo.getProfileManager().getUserCurrentProfile().isPluginEnabled(OpenTrialsPlugin.getInstance().getPluginName())) {
			registerExtractor(new OpenTrialsDocumentPipeline(_domeo));
		}
		// GENERIC PIPELINE
		// This is used when no suitable dedicated pipeline is found
		IContentExtractor document = new GenericDocumentPipeline(_domeo);
		registerExtractor(document);
	}
	
	@Override
	public void init() {
		_currentExtractor = null;
	}
	
	private boolean registerExtractor(IContentExtractor extractor) {
		if(extractors.containsKey(extractor.getUrlPrefix())) {
			_domeo.getLogger().warn(this, "Extractor already registered for prefix " + extractor.getUrlPrefix());
			return false;
		} else {
			_domeo.getLogger().info(ANALYZER_PLUGIN + extractor.getShort(), this, "Extractor registered for prefix " + extractor.getUrlPrefix());
			extractors.put(extractor.getUrlPrefix(), extractor);
			return true;
		}
	}
	
	/**
	 * Selects a suitable extractor to process the given URL.
	 * @param url URL of the page to extract from
	 */
	public void setUpExtractor(String url) {
		for (String prefix: extractors.keySet()) {
			if(url.contains(prefix)) {
				_currentExtractor = extractors.get(prefix);
				_domeo.getLogger().info(this, "Extractor '" + _currentExtractor.getLabel() + "' selected for prefix " +  url);
				return;
			}
		}
		_domeo.getLogger().exception(this, "No extractor found for prefix " +  url);
	}

	/**
	 * @return True if an extractor has been selected
	 */
	public boolean isExtractorDefined() {
		return _currentExtractor!=null;
	}
	
	/**
	 * Returns the label of the current extractor
	 * @return Extractor label
	 */
	public String getExtractorLabel() {
		if(isExtractorDefined()) return _currentExtractor.getLabel();
		else {
			 _domeo.getLogger().exception(this, "No extractor currently defined, can't execute getExtractorLabel()");
			return "No extractor found";
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mindinformatics.gwt.domeo.services.extractors.IExtractorsManager#parametrizeExtractorAndProcess()
	 */
	public void parametrizeExtractorAndProcess() {
		if(isExtractorDefined()) _currentExtractor.parametrizeAndStart();
		else _domeo.getLogger().exception(this, "No extractor currently defined, can't execute parametrizeExtractorAndProcess()");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mindinformatics.gwt.domeo.services.extractors.IExtractorsManager#processDocument(java.util.Map)
	 */
	public void processDocumentExtraction(Map<String, String> params) {
		if(isExtractorDefined()) _currentExtractor.start(params);
		else _domeo.getLogger().exception(this, "No extractor currently defined, can't execute processDocument(Map<String, String> params)");
	}
}
