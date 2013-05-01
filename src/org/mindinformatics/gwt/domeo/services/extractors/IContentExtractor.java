package org.mindinformatics.gwt.domeo.services.extractors;

import java.util.Map;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IContentExtractor {

	public String getShort();
	public String getLabel();
	public String getUrlPrefix();
	
	/**
	 * Parametrization of the extractor and start of the extraction
	 */
	public void parametrizeAndStart();
	
	/**
	 * Start the extractor with the desired parametrization
	 * @param params The parameters for tuning the pipeline
	 */
	public void start(Map<String, String> params);
}
