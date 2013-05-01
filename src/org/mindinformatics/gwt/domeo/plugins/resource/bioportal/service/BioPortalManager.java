package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.test.GwtBioPortalServiceConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.test.StandaloneBioPortalConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.src.JsonBioPortalConnector;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class BioPortalManager {

	private IBioPortalConnector _connector;
	
	private BioPortalManager() {}
	private static BioPortalManager _instance;
	public static BioPortalManager getInstance() {
		if(_instance == null) _instance = new  BioPortalManager(); 
		return _instance;
	}
	
	public IBioPortalConnector selectBioPortalConnector(IDomeo domeo) {
		if(_connector!=null) return _connector;
		if(domeo.isStandaloneMode()) {
			_connector = new StandaloneBioPortalConnector();
		} else {
			if (domeo.isHostedMode()) {
				_connector = new GwtBioPortalServiceConnector();
			} else {
				// Real service
				_connector = new JsonBioPortalConnector(domeo);
			}
		}
		
		domeo.getLogger().debug(this, "BioPortal Connector selected: " + _connector.getClass().getName());
		
		return _connector;
	} 
	
	public void searchTerms(IBioPortalItemsRequestCompleted completionCallback, String textQuery) throws IllegalArgumentException {
		if (_connector!=null) {
			 _connector.searchTerm(completionCallback, textQuery, "");
		} else throw new IllegalArgumentException("No BioPortal Connector selected");
	}
	
	public void textmine(final IBioPortalTextminingRequestCompleted completionCallback,
			String url, String textContent, String virtualIds) throws IllegalArgumentException {
		if (_connector!=null) {
			 _connector.textmine(completionCallback, url, textContent, "");
		} else throw new IllegalArgumentException("No BioPortal Connector selected");
		
	}
}
