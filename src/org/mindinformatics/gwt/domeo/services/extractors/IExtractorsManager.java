package org.mindinformatics.gwt.domeo.services.extractors;

import java.util.Map;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IExtractorsManager {
	/**
	 * Call for parametrization and start of the extraction pipeline.
	 */
	public void parametrizeExtractorAndProcess();
	
	/**
	 * Call for the execution pipeline start with a specific set
	 * of paramenters
	 * @param params	The list of parameters for pipeline parametrization
	 */
	public void processDocumentExtraction(Map<String, String> params);
}
