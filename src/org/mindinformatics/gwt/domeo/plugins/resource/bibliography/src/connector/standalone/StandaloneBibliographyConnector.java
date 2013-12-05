package org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.standalone;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.IBibliographyConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.IStarringRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class StandaloneBibliographyConnector implements IBibliographyConnector {

	private IDomeo _domeo;
	public StandaloneBibliographyConnector(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public void starResource(MDocumentResource resource, IStarringRequestCompleted completionHandler) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void unstarResource(MDocumentResource resource, IStarringRequestCompleted completionHandler) {
		// TODO Auto-generated method stub
		
	}
}
