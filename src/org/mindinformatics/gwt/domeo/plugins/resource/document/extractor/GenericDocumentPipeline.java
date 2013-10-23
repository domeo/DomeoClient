package org.mindinformatics.gwt.domeo.plugins.resource.document.extractor;

import java.util.HashMap;
import java.util.Map;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.services.extractors.IContentExtractor;
import org.mindinformatics.gwt.domeo.services.extractors.IContentExtractorCallback;
import org.mindinformatics.gwt.framework.component.pipelines.src.APipeline;
import org.mindinformatics.gwt.framework.component.pipelines.src.IStage;
import org.mindinformatics.gwt.framework.component.pipelines.src.Stage;

/**
 *
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class GenericDocumentPipeline extends APipeline implements IContentExtractor {

	public static final String PREFIX = "http";
	public static final String LABEL = "Generic Document Extractor";
	public static final String SHORT = "Generic";
	
	@Override
	public String getUrlPrefix() { return PREFIX; }
	
	@Override
	public String getLabel() { return LABEL; }
	
	@Override
	public String getPipelineName() { return LABEL; }
	
	@Override
	public String getShort() { return SHORT; }
	
	public GenericDocumentPipeline(IDomeo application) {
		super(application);
		
		IStage metadataStage = new Stage(new GenericDocumentExtractMetadataCommand(application, 
				(IContentExtractorCallback) application.getPersistenceManager(), this));
		_stages.add(metadataStage);
		
		IStage imagesStage = new Stage(new GenericDocumentExtractImagesCommand(application, 
				(IContentExtractorCallback) application.getPersistenceManager(), this));
		_stages.add(imagesStage);
	}

	@Override
	public void notifyPipelineTermination(long startTime) {
		_application.getLogger().debug(this, "Extraction pipeline completed in " + (System.currentTimeMillis()-startTime) + "ms");
		((IDomeo)_application).notifyEndExtraction();
	}
	
	@Override
	public void parametrizeAndStart() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(IBibliographicParameters.EXTRACT_META, IBibliographicParameters.EXECUTE);
		start(_params);
	}
}
