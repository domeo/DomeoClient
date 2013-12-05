package org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.IBibliographyConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.IStarringRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.gwt.GwtBibliographyConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.json.JsonBibliographyConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.standalone.StandaloneBibliographyConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class BibliographyManager implements IBibliographyConnector {

	private IBibliographyConnector _connector;
	
	/* Singleton */
	private BibliographyManager() {}
	private static BibliographyManager _instance;
	public static BibliographyManager getInstance() {
		if(_instance == null) _instance = new  BibliographyManager(); 
		return _instance;
	}
	
	public IBibliographyConnector selectPubMedConnector(IDomeo domeo) {
		if(_connector!=null) return _connector;
		if(domeo.isStandaloneMode()) {
			_connector = new StandaloneBibliographyConnector(domeo);
		} else {
			if (domeo.isHostedMode()) {
				_connector = new GwtBibliographyConnector(domeo);
			} else {
				_connector = new JsonBibliographyConnector(domeo);
			}
		}
		
		domeo.getLogger().debug(this, "Bibliography Connector selected: " + _connector.getClass().getName());
		
		return _connector;
	}

	@Override
	public void starResource(MDocumentResource resource, IStarringRequestCompleted completionHandler) 
		throws IllegalArgumentException {
			if (_connector!=null) {
				_connector.starResource(resource, completionHandler);
			} else throw new IllegalArgumentException("No resource to star");
	}

	@Override
	public void unstarResource(MDocumentResource resource, IStarringRequestCompleted completionHandler) 
		throws IllegalArgumentException {
			if (_connector!=null) {
				_connector.unstarResource(resource, completionHandler);
			} else throw new IllegalArgumentException("No resource to unstar");
	} 
}
