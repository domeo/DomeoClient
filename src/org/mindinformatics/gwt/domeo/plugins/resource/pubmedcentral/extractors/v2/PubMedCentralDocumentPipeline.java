package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.plugins.resource.document.extractor.GenericDocumentExtractImagesCommand;
import org.mindinformatics.gwt.domeo.services.extractors.IContentExtractor;
import org.mindinformatics.gwt.domeo.services.extractors.IContentExtractorCallback;
import org.mindinformatics.gwt.framework.component.pipelines.src.APipeline;
import org.mindinformatics.gwt.framework.component.pipelines.src.IStage;
import org.mindinformatics.gwt.framework.component.pipelines.src.Stage;
import org.mindinformatics.gwt.framework.component.preferences.src.BooleanPreference;

/**
 * The PubMed Central extraction pipeline takes care of the scraping the content
 * for triggering content elements extraction and metadata retrieval.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PubMedCentralDocumentPipeline extends APipeline implements IContentExtractor {

	public static final String PREFIX = "http://www.ncbi.nlm.nih.gov/pmc/articles/";
	public static final String LABEL = "PubMedCentral Analyzer";
	public static final String SHORT = "PubMedCentral";
	public static final String LOGGER = "PLUGIN::ANALYZER::PUBMEDCENTRAL";
	public static final String EXCEPTION = "PLUGIN::ANALYZER::PUBMEDCENTRAL::Exception";
	
	public static final String PARAM_PMCID = "pmcid";
	
	@Override
	public String getUrlPrefix() { return PREFIX; }
	
	@Override
	public String getLabel() { return LABEL; }
	
	@Override
	public String getPipelineName() { return LABEL; }
	
	@Override
	public String getShort() { return SHORT; }
	
	public PubMedCentralDocumentPipeline(IDomeo application) {
		super(application);
		
		IStage metaStage = new Stage(new PubMedCentralExtractMetaCommand(application, this));
		_stages.add(metaStage);
		
		IStage imagesStage = new Stage(new GenericDocumentExtractImagesCommand(application, 
				(IContentExtractorCallback) application.getPersistenceManager(), this));
		_stages.add(imagesStage);
		
		if(!application.isHostedMode()) {
			IStage retrieveBibliographyByUrlStage = new Stage(new PubMedCentralRetrieveBibliographyByUrlCommand(application, this));
			_stages.add(retrieveBibliographyByUrlStage);
		}
		
		IStage dialogStage = new Stage(new PubMedCentralExtractionDialogCommand(application, this, this));
		_stages.add(dialogStage);
		
		IStage subjectStage = new Stage(new PubMedCentralExtractSubjectCommand(application, this));
		_stages.add(subjectStage);
		
		IStage referencesStage = new Stage(new PubMedCentralExtractReferencesCommand(application, this));
		_stages.add(referencesStage);
		
		IStage citationsStage = new Stage(new PubMedCentralExtractCitationsCommand(application, this));
		_stages.add(citationsStage);
	}

	@Override
	public void notifyPipelineTermination(long startTime) {
		_application.getLogger().debug(this, "PubMed pipeline completed in " + (System.currentTimeMillis()-startTime) + "ms");
		_application.notifyEndExtraction();
		
		// Saving of the bibliography in parallel with the refreshing process
		if(((BooleanPreference) _application.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.PERSIST_AUTOMATICALLY)).getValue()) {
			if(((IDomeo)_application).getAnnotationPersistenceManager().getBibliographicSet().getHasChanged()) ((IDomeo)_application).getAnnotationPersistenceManager().saveBibliography();
		}	
	}

	@Override
	public void parametrizeAndStart() {		
		try {
			// TODO Revise the preferences management. Maybe they should be defined elsewhere and duplicated here
			// to allow local customization
			
			// Registering Default Settings
			_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.PERSIST_AUTOMATICALLY, true);
			_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.RETRIEVE_REFERENCES, IBibliographicParameters.DISABLED);
			// Registering Pipeline Stages Settings
			_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_META, IBibliographicParameters.EXECUTE);
			_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_SUBJECT, IBibliographicParameters.EXECUTE);
			_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_REFERENCES, IBibliographicParameters.EXECUTE);
			_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_CITATIONS, IBibliographicParameters.SKIP);
			
			/*
			_params.put(IBibliographicParameters.EXTRACT_META, 
					((TextPreference) _application.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_META)).getValue());
			_params.put(IBibliographicParameters.EXTRACT_SUBJECT, 
					((TextPreference) _application.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_SUBJECT)).getValue());
			_params.put(IBibliographicParameters.EXTRACT_REFERENCES, 
					((TextPreference) _application.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_REFERENCES)).getValue());
			_params.put(IBibliographicParameters.EXTRACT_CITATIONS, 
					((TextPreference) _application.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_CITATIONS)).getValue());
					*/
			
			start(_params);
		} catch (Exception e) {
			_application.getLogger().exception(this, e.getMessage());
		}
	}
}
