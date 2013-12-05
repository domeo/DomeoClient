package org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector;

import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IBibliographyConnector {

	public void starResource(MDocumentResource resource, IStarringRequestCompleted completionHandler);
	public void unstarResource(MDocumentResource resource, IStarringRequestCompleted completionHandler);
}
