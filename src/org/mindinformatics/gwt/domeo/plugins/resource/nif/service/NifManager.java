package org.mindinformatics.gwt.domeo.plugins.resource.nif.service;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.IBioPortalTextminingRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.src.NifJsonConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.src.StandaloneNifConnector;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class NifManager {

	private INifConnector _connector;
	
	private NifManager() {}
	private static NifManager _instance;
	public static NifManager getInstance() {
		if(_instance == null) _instance = new NifManager(); 
		return _instance;
	}
	
	public INifConnector selectConnector(IDomeo domeo) {
		if(_connector!=null) return _connector;
		if(domeo.isStandaloneMode()) {
			_connector = new StandaloneNifConnector();
		} else {
			if (domeo.isHostedMode()) {
				_connector = new StandaloneNifConnector();
			} else {
				// Real service
				_connector = new NifJsonConnector(domeo);
			}
		}
		
		domeo.getLogger().debug(this, "Nif Connector selected: " + _connector.getClass().getName());
		
		return _connector;
	} 
	
	public void searchData(final INifDataRequestCompleted completionCallback,
			String textQuery, String type, String vendor, String resource, int pageNumber, int pageSize)
			throws IllegalArgumentException {
		if (_connector!=null) {
			 _connector.searchData(completionCallback, textQuery, type, vendor, resource, 0, 0);
		} else throw new IllegalArgumentException("No Nif Connector selected");
	}

	public void annotate(final IBioPortalTextminingRequestCompleted completionCallback,
			String url, String textContent, String virtualIds) throws IllegalArgumentException {
		if (_connector!=null) {
			 _connector.annotate(completionCallback, url, textContent, "", "");
		} else throw new IllegalArgumentException("No Nif Connector selected");
		
	}
}
