package org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.gwt;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.IBibliographyConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.IStarringRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class GwtBibliographyConnector implements IBibliographyConnector {

	private IDomeo _domeo;
	public GwtBibliographyConnector(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public void starResource(MDocumentResource resource, IStarringRequestCompleted completionHandler) {
		completionHandler.documentResourceStarred();
	}
	
	@Override
	public void unstarResource(MDocumentResource resource, IStarringRequestCompleted completionHandler) {
		completionHandler.documentResourceUnstarred();
	}
}
