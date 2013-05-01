package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.plugins.resource.document.extractor.GenericDocumentExtractImagesCommand;
import org.mindinformatics.gwt.domeo.services.extractors.IContentExtractor;
import org.mindinformatics.gwt.domeo.services.extractors.IContentExtractorCallback;
import org.mindinformatics.gwt.framework.component.pipelines.src.APipeline;
import org.mindinformatics.gwt.framework.component.pipelines.src.IStage;
import org.mindinformatics.gwt.framework.component.pipelines.src.Stage;
import org.mindinformatics.gwt.framework.component.preferences.src.BooleanPreference;
import org.mindinformatics.gwt.framework.component.preferences.src.TextPreference;

/**
 * PubMed extraction pipeline
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PubMedDocumentPipeline extends APipeline implements IContentExtractor {

	public static final String PREFIX = "http://www.ncbi.nlm.nih.gov/pubmed/";
	public static final String LABEL = "PubMed Analyzer";
	public static final String SHORT = "PubMed";
	public static final String LOGGER = "PLUGIN::ANALYZER::PUBMED";
	
	@Override
	public String getUrlPrefix() { return PREFIX; }
	
	@Override
	public String getLabel() { return LABEL; }
	
	@Override
	public String getPipelineName() { return LABEL; }
	
	@Override
	public String getShort() { return SHORT; }
	
	public PubMedDocumentPipeline(IDomeo application) {
		super(application);
		
		IStage metaStage = new Stage(new PubMedExtractMetaCommand(application, this));
		_stages.add(metaStage);
		
		IStage imagesStage = new Stage(new GenericDocumentExtractImagesCommand(application, 
				(IContentExtractorCallback) application.getPersistenceManager(), this));
		_stages.add(imagesStage);
		
		if(!application.isHostedMode()) {
			IStage retrieveBibliographyByUrlStage = new Stage(new PubMedRetrieveBibliographyByUrlCommand(application, this));
			_stages.add(retrieveBibliographyByUrlStage);
		}
		
		IStage subjectStage = new Stage(new PubMedExtractSubjectCommand(application, this));
		_stages.add(subjectStage);
		
		if(!application.isHostedMode()) {
			IStage referencesStage = new Stage(new PubMedRetrieveReferencesCommand(application, this));
			_stages.add(referencesStage);
		}
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
			_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.PERSIST_AUTOMATICALLY, true);
			
			// Duplicated so that the profile loading can be anabled later on
			// Defaults, the changes through the UI are not saved yet
			_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_META, IBibliographicParameters.EXECUTE);
			_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_SUBJECT, IBibliographicParameters.EXECUTE);
			_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_REFERENCES, IBibliographicParameters.EXECUTE);
			_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_CITATIONS, IBibliographicParameters.SKIP);
			_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.RETRIEVE_REFERENCES, IBibliographicParameters.ENABLED);
	
			_params.put(PubMedExtractMetaCommand.class.getName(), 
					Boolean.toString(((TextPreference) _application.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_META)).getValue().equals(IBibliographicParameters.EXECUTE)));
			_params.put(PubMedExtractSubjectCommand.class.getName(),
					Boolean.toString(((TextPreference) _application.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_SUBJECT)).getValue().equals(IBibliographicParameters.EXECUTE)));
			_params.put(PubMedRetrieveReferencesCommand.class.getName(),
					Boolean.toString(((TextPreference) _application.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_REFERENCES)).getValue().equals(IBibliographicParameters.EXECUTE)));
			
			start(_params);
			//CPubMedCentralParams lwp = new CPubMedCentralParams((IDomeo)_application, manager, _stages, _params);
			//new EnhancedGlassPanel(_application, lwp, lwp.getTitle(), 300, false, false, false);
		} catch (Exception e) {
			_application.getLogger().exception(this, e.getMessage());
		}
	}
}
