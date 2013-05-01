package org.mindinformatics.gwt.domeo.services.extractors;

import org.mindinformatics.gwt.domeo.client.IDomeo;

public abstract class AContentExtractor {

	protected IDomeo _application;
	
	/**
	 * Initialization
	 * @param application	The application main class
	 */
	public void init(IDomeo application) {
		_application = application;
	}
	
	public void start() {
		
	}
	
	/**
	 * Returns the label for the analyzer.
	 * @return	The name/label of the analyzer.
	 */
	public abstract String getLabel();
}
