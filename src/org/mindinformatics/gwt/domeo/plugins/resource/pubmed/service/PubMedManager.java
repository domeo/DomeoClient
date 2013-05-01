package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service;

import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingBibliographySetHandler;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.test.GwtPubMedServiceConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.test.StandalonePubMedConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.src.JsonPubMedConnector;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PubMedManager {

	private IPubMedConnector _connector;
	
	private PubMedManager() {}
	private static PubMedManager _instance;
	public static PubMedManager getInstance() {
		if(_instance == null) _instance = new  PubMedManager(); 
		return _instance;
	}
	
	public IPubMedConnector selectPubMedConnector(IDomeo domeo, IPubMedItemsRequestCompleted callbackCompleted) {
		if(_connector!=null) return _connector;
		if(domeo.isStandaloneMode()) {
			_connector = new StandalonePubMedConnector();
		} else {
			if (domeo.isHostedMode()) {
				_connector = new GwtPubMedServiceConnector();
			} else {
				_connector = new JsonPubMedConnector(domeo, callbackCompleted);
			}
		}
		
		domeo.getLogger().debug(this, "PubMed Connector selected: " + _connector.getClass().getName());
		
		return _connector;
	} 
	
	public void retrieveExistingBibliographySetByUrl(IDomeo domeo, IRetrieveExistingBibliographySetHandler completionCallback, String url) 
			throws IllegalArgumentException {
		if (_connector!=null) {
			 _connector.retrieveExistingBibliographySetByUrl(completionCallback, url);
		} else throw new IllegalArgumentException("No PubMed Connector selected");
	}
	
	public void retrieveExistingBibliographySet(IDomeo domeo, IRetrieveExistingBibliographySetHandler completionCallback, int level) 
			throws IllegalArgumentException {
		if (_connector!=null) {
			 _connector.retrieveExistingBibliographySet(completionCallback, level);
		} else throw new IllegalArgumentException("No PubMed Connector selected");
	}
	
	public void getBibliographicObject(IPubMedItemsRequestCompleted completionCallback, 
			String typeQuery, String textQuery) throws IllegalArgumentException {
		if (_connector!=null) {
			 _connector.getBibliographicObject(completionCallback, typeQuery, textQuery);
		} else throw new IllegalArgumentException("No PubMed Connector selected");
	}
	
	public void getBibliographicObjectsByText(IPubMedItemsRequestCompleted completionCallback, 
			String typeQuery, List<String> textQuery, List<String> elements) throws IllegalArgumentException {
		if (_connector!=null) {
			 _connector.getBibliographicObjects(completionCallback, typeQuery, textQuery, elements);
		} else throw new IllegalArgumentException("No PubMed Connector selected");
	}
	
	public void searchBibliographicObjects(IPubMedPaginatedItemsRequestCompleted completionCallback, 
			String typeQuery, String textQuery, int maxResults, int offset) throws IllegalArgumentException {
		if (_connector!=null) {
			 _connector.searchBibliographicObjects(completionCallback, typeQuery, textQuery, maxResults, offset);
		} else throw new IllegalArgumentException("No PubMed Connector selected");
	}
}
