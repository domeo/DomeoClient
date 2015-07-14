package org.mindinformatics.gwt.domeo.plugins.resource.nif.service;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.textmining.src.ITextMiningConnector;
import org.mindinformatics.gwt.domeo.component.textmining.src.ITextminingRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.annotopia.nif.src.AnnotopiaNifConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.service.annotator.FNifAnnotatorParametrizationForm;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.service.annotator.PNifAnnotatorParameters;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.src.NifJsonConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.src.StandaloneNifConnector;
import org.mindinformatics.gwt.framework.src.Utils;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class NifManager implements ITextMiningConnector{

	private IDomeo _domeo;
	private INifConnector _connector;
	
	private NifManager() {}
	private static NifManager _instance;
	public static NifManager getInstance() {
		if(_instance == null) _instance = new NifManager(); 
		return _instance;
	}
	
	public boolean selectConnector(IDomeo domeo) {
		if(_connector!=null) return true;
		_domeo = domeo;
		if(domeo.isStandaloneMode()) {
			_connector = new StandaloneNifConnector();
		} else {
			if(domeo.isAnnotopiaEnabled()) {
				_connector = new AnnotopiaNifConnector(domeo, Utils.getAnnotopiaLocation());
			} else if (domeo.isHostedMode()) {
				_connector = new StandaloneNifConnector();
			} else {
				// Real service
				_connector = new NifJsonConnector(domeo);
			}
		}
		domeo.getLogger().debug(this, "Nif Connector selected: " + _connector.getClass().getName());	
		return false;
	} 
	
	public void searchData(final INifDataRequestCompleted completionCallback,
			String textQuery, String type, String vendor, String resource, int pageNumber, int pageSize)
			throws IllegalArgumentException {
		if (_connector!=null) {
			 _connector.searchData(completionCallback, textQuery, type, vendor, resource, 0, 0);
		} else throw new IllegalArgumentException("No Nif Connector selected");
	}

	@Override
	public void annotate(final ITextminingRequestCompleted completionCallback,
			String url, String textContent, String... params) throws IllegalArgumentException {
		if (_connector!=null) {
			 _connector.annotate(completionCallback, url, textContent, 
				 PNifAnnotatorParameters.getInstance().includeCat,
				 PNifAnnotatorParameters.getInstance().excludeCat,
				 PNifAnnotatorParameters.getInstance().minLength,
				 PNifAnnotatorParameters.getInstance().longestOnly,
				 PNifAnnotatorParameters.getInstance().includeAbbrev,
				 PNifAnnotatorParameters.getInstance().includeAcronym,
				 PNifAnnotatorParameters.getInstance().includeNumbers);
		} else throw new IllegalArgumentException("No Nif Connector selected");		
	}
	
	@Override
	public String getAnnotatorLabel() {
		return "NIF Annotator Web Service";
	}

	@Override
	public Widget getAnnotatorPanel() {
		return new FNifAnnotatorParametrizationForm(_domeo);
	}
}
