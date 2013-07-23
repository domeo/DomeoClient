package org.mindinformatics.gwt.domeo.plugins.resource.omim.extractors;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.services.extractors.IContentExtractor;
import org.mindinformatics.gwt.framework.component.pipelines.src.APipeline;
import org.mindinformatics.gwt.framework.component.pipelines.src.IStage;
import org.mindinformatics.gwt.framework.component.pipelines.src.Stage;
import org.mindinformatics.gwt.framework.component.preferences.src.BooleanPreference;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class OmimDocumentPipeline extends APipeline implements IContentExtractor {

	public static final String PREFIX = "http://omim.org/entry/";
	public static final String LABEL = "OMIM Extractor";
	public static final String SHORT = "OMIM";
	public static final String LOGGER = "PLUGIN::ANALYZER::OMIM";
	
	@Override
	public String getUrlPrefix() { return PREFIX; }
	
	@Override
	public String getLabel() { return LABEL; }
	
	@Override
	public String getPipelineName() { return LABEL; }
	
	@Override
	public String getShort() { return SHORT; }
	
	public OmimDocumentPipeline(IDomeo application) {
		super(application);
		
		IStage metaStage = new Stage(new OmimExtractMetaCommand(application, application, this));
		_stages.add(metaStage);
		
		if(!application.isHostedMode()) {
			IStage retrieveBibliographyByUrlStage = new Stage(new OmimRetrieveBibliographyByUrlCommand(application, this));
			_stages.add(retrieveBibliographyByUrlStage);
		}
		
		IStage subjectStage = new Stage(new OmimExtractSubjectCommand(application, application, this));
		_stages.add(subjectStage);
		
		IStage referencesStage = new Stage(new OmimExtractBibliographicReferencesCommand(application, application, this));
		_stages.add(referencesStage);
	}

	@Override
	public void notifyPipelineTermination(long startTime) {
		_application.getLogger().debug(this, "OMIM pipeline completed in " + (System.currentTimeMillis()-startTime) + "ms");
		_application.notifyEndExtraction();
		
		// Saving of the bibliography in parallel with the refreshing process
		if(((BooleanPreference) _application.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.PERSIST_AUTOMATICALLY)).getValue()) {
			if(((IDomeo)_application).getAnnotationPersistenceManager().getBibliographicSet().getHasChanged()) ((IDomeo)_application).getAnnotationPersistenceManager().saveBibliography();
		}	
	}
	
	@Override
	public void parametrizeAndStart() {
		
		_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.PERSIST_AUTOMATICALLY, true);
		_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.RETRIEVE_REFERENCES, IBibliographicParameters.DISABLED);
		// Registering Pipeline Stages Settings
		_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_META, IBibliographicParameters.EXECUTE);
		_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_SUBJECT, IBibliographicParameters.EXECUTE);
		_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_REFERENCES, IBibliographicParameters.EXECUTE);
		_application.getPreferences().registerPreferenceItemValue(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_CITATIONS, IBibliographicParameters.SKIP);

		start(_params);
	}
}
