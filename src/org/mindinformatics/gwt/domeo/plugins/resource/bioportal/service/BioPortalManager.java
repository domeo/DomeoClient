package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.textmining.src.ITextMiningConnector;
import org.mindinformatics.gwt.domeo.component.textmining.src.ITextminingRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.test.GwtBioPortalServiceConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.test.StandaloneBioPortalConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.src.JsonBioPortalConnector;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class BioPortalManager implements ITextMiningConnector {

	private IBioPortalConnector _connector;
	
	private BioPortalManager() {}
	private static BioPortalManager _instance;
	public static BioPortalManager getInstance() {
		if(_instance == null) _instance = new  BioPortalManager(); 
		return _instance;
	}
	
	public boolean selectConnector(IDomeo domeo) {
		if(_connector!=null) return true;
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
		return false;
	} 
	
	public void searchTerms(IBioPortalItemsRequestCompleted completionCallback, String textQuery) throws IllegalArgumentException {
		if (_connector!=null) {
			 _connector.searchTerm(completionCallback, textQuery, "");
		} else throw new IllegalArgumentException("No BioPortal Connector selected");
	}
	
	@Override
	public void annotate(ITextminingRequestCompleted completionCallback,
			String url, String textContent, String... params) throws IllegalArgumentException {
		if (_connector!=null) {
			 _connector.textmine(completionCallback, url, textContent, "");
		} else throw new IllegalArgumentException("No BioPortal Connector selected");
		
	}

	@Override
	public String getAnnotatorLabel() {
		return "NCBO Annotator Web Service";
	}

	@Override
	public Widget getAnnotatorPanel() {
		// TODO Auto-generated method stub
		return null;
	}
}
