package org.mindinformatics.gwt.domeo.plugins.resource.opentrials.extractor;

import java.util.HashMap;
import java.util.Map;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.services.extractors.IContentExtractor;
import org.mindinformatics.gwt.domeo.services.extractors.IExtractorsManager;
import org.mindinformatics.gwt.framework.component.pipelines.src.APipeline;
import org.mindinformatics.gwt.framework.component.pipelines.src.IStage;
import org.mindinformatics.gwt.framework.component.pipelines.src.Stage;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class OpenTrialsDocumentPipeline extends APipeline implements IContentExtractor {

	public static final String PREFIX = "http://clinicaltrials.gov/ct2/show/";
	public static final String LABEL = "OpenTrials Extractor";
	public static final String SHORT = "OpenTrials";
	public static final String LOGGER = "PLUGIN::ANALYZER::OpenTrials";
	
	@Override
	public String getUrlPrefix() { return PREFIX; }
	
	@Override
	public String getLabel() { return LABEL; }
	
	@Override
	public String getPipelineName() { return LABEL; }
	
	@Override
	public String getShort() { return SHORT; }
	
	public OpenTrialsDocumentPipeline(IDomeo application) {
		super(application);
		
		IStage metaStage = new Stage(new OpenTrialsExtractMetaCommand(application, application, this));
		_stages.add(metaStage);
		
		IStage subjectStage = new Stage(new OpenTrialsExtractSubjectCommand(application, application, this));
		_stages.add(subjectStage);
		
		IStage referencesStage = new Stage(new OpenTrialsExtractBibliographicCitationsCommand(application, application, this));
		_stages.add(referencesStage);
	}

	@Override
	public void notifyPipelineTermination(long startTime) {
		_application.getLogger().debug(this, "OpenTrials pipeline completed in " + (System.currentTimeMillis()-startTime) + "ms");
		_application.notifyEndExtraction();
	}
	
	@Override
	public void parametrizeAndStart() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(IBibliographicParameters.EXTRACT_META, IBibliographicParameters.EXECUTE);
		params.put(IBibliographicParameters.EXTRACT_SUBJECT, IBibliographicParameters.EXECUTE);
		params.put(IBibliographicParameters.EXTRACT_REFERENCES, IBibliographicParameters.EXECUTE);
		start(_params);
	}
}
