package org.mindinformatics.gwt.framework.component.qualifiers.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.list.ITermsSelectionConsumer;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.list.TermsSelectionList;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.IBioPortalItemsRequestCompleted;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public abstract class SearchTermsWidget extends Composite implements ISearchWidget, IBioPortalItemsRequestCompleted, ITermsSelectionConsumer {
	
	interface Binder extends UiBinder<VerticalPanel, SearchTermsWidget> {}
	private static final Binder binder = GWT.create(Binder.class);

	// By contract 
	protected IDomeo _domeo;
	protected Resources _resources;
	private ISearchTermsContainer _container;
	
	// Main panel: for this widget no other graphic element
	// has been defined in the xml
	@UiField VerticalPanel main;
	// Dynamically created ui elements
	protected Panel resultsContainerPanel;
	private TermsSelectionList termsResultsPanel;
	private ITermsSearch termsSearchPanel;
	protected TermsSelectionList termsSelectionList;
	
	private HashMap<String, MLinkedResource> associatedTerms = new HashMap<String, MLinkedResource>();
	protected ArrayList<MLinkedResource> searchTermsResult; 
	//private SelectedTerms selectedTerms;
	
	public SearchTermsWidget(IDomeo annotator, ISearchTermsContainer container, TermsSelectionList resultList,  boolean showTitle) {
		_domeo = annotator;
		_resources = Domeo.resources;
		_container = container;
		
		this.termsSelectionList = resultList;
		
		initWidget(binder.createAndBindUi(this)); // Necessary for initializing Composite 
		initMapOfAlreadyAssociatedTerms(container.getItems());
		
		if(showTitle) main.add(new Label(getWidgetTitle()));	
		
        termsSearchPanel = new TermsSearch(this, _container.getTextContent());
        main.add((Composite) termsSearchPanel);

        resultsContainerPanel = new FlowPanel();
        resultsContainerPanel.setHeight("347px");
        resultsContainerPanel.add(new HTML("No results to display"));
        main.add(resultsContainerPanel);
	}
	
	public SearchTermsWidget(IDomeo annotator, ISearchTermsContainer container, ITermsSearch termsSearch, TermsSelectionList resultList, boolean showTitle) {
        
	    try {
    	    _domeo = annotator;
            _resources = Domeo.resources;
            _container = container;
            
            this.termsSelectionList = resultList;
            
            initWidget(binder.createAndBindUi(this)); // Necessary for initializing Composite 
            initMapOfAlreadyAssociatedTerms(container.getItems());
            
            if(showTitle) main.add(new Label(getWidgetTitle()));
            
            termsSearchPanel = termsSearch;
            termsSearchPanel.initialize(this,  _container.getTextContent());
            
            main.add((Composite)termsSearchPanel);
            
            resultsContainerPanel = new FlowPanel();
            resultsContainerPanel.setHeight("347px");
            resultsContainerPanel.add(new HTML("No results to display"));
            main.add(resultsContainerPanel);   
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }
	
	public void setSearchBoxValue(String value) {
		termsSearchPanel.setSearchBoxValue(value);
	}
	
//	public void setSearchBoxValue(String value, boolean search) {
//		termsSearchPanel.setSearchBoxValue(value);
//		if(search)  performSearch(value);
//	}
	
	private void initMapOfAlreadyAssociatedTerms(ArrayList<MLinkedResource> terms) {
		for(MLinkedResource term: terms) {
			associatedTerms.put(term.getUrl(), term);
		}
	}
	
	public abstract void performSearch(String textQuery, String resource);
	/*
	{
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new Image(_resources.spinningIcon2()));
		
		_domeo.getLogger().command(this, "Searching for: " + textQuery);
		try {
			BioPortalManager bioPortalManager = BioPortalManager.getInstance();
			bioPortalManager.selectBioPortalConnector(_domeo);
			bioPortalManager.searchTerms(this, textQuery);
		} catch(Exception exc) {
			_domeo.getLogger().exception( 
				this, "Exception while searching BioPortal for " + textQuery + " - " + 
					exc.getMessage());
			return;
		}	
	}
	*/
	
	public void filterBySource(String sourceId) {
		termsResultsPanel.update(sourceId);
		//updateSelectedTerms(0);
	}
	
	/*
	public void updateSelectedTerms(int numberOfTerms) {
		selectedTerms.updateSelectedTerms(numberOfTerms);
	}
	*/
	
	public String getFilterValue() {
		return termsSearchPanel.getSourceFilterValue();
	}
	
	// ------------------------------------------------------------------------
	//  TERMS RESULTS MANAGEMENT
	// ------------------------------------------------------------------------
	public ArrayList<MLinkedResource> getTermsList() {
		return getSearchTermsResult();
	}
	
	public ArrayList<MLinkedResource> getSearchTermsResult() {
		return searchTermsResult;
	}
	
	public HashMap<String, String> getSearchTermsResultSources() {
		HashMap<String, String> map = new HashMap<String, String>();

		for(int i=0; i< searchTermsResult.size(); i++) {
			if(!map.containsKey(searchTermsResult.get(i).getSource().getUrl()))
				map.put(searchTermsResult.get(i).getSource().getUrl(), 
						searchTermsResult.get(i).getSource().getLabel());
		}

		return map;
	}
	
	protected void displayResults(ArrayList<MLinkedResource> list) {
		//termsResultsPanel = new TermsSelectionList(_domeo, this, list, associatedTerms);
		
	    _domeo.getLogger().info(this, "Found terms: " + list.size());
	    
	    termsSelectionList.initTermsList(list);
	    
		resultsContainerPanel.clear();
		resultsContainerPanel.add(termsSelectionList);
		
		termsSearchPanel.updateResultsStats();
	}
	
	public void updateResultsNumbers() {
		termsSearchPanel.updateResultsStats();
	}
	
	// ------------------------------------------------------------------------
	//  TERMS CHOICE MANAGEMENT
	// ------------------------------------------------------------------------
	public void addTerm(MLinkedResource term) {
		if(!associatedTerms.containsKey(term.getUrl())) {
			associatedTerms.put(term.getUrl(), term);
			_container.addAssociatedTerm(term);
		}
	}
	
	public HashMap<String, MLinkedResource> getSelectedTerms() {
		return associatedTerms;
	}
	
	public ArrayList<MLinkedResource> getTermsResults() {
		return searchTermsResult;
	}
	
	public void removeTerm(MLinkedResource term) {
		associatedTerms.remove(term.getUrl());
	}
	
	@Override
	public abstract String getWidgetTitle();

	@Override
	public void returnTerms(ArrayList<MLinkedResource> terms) {
		searchTermsResult = terms;
		displayResults(terms);
	}

	@Override
	public void returnTerms(int totalPages, int pageSize, int pageNumber,
			ArrayList<MLinkedResource> terms) {
		searchTermsResult = terms;
		displayResults(terms);
	}

	@Override
	public void reportException() {
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new HTML("Exception while performing search. See logs for details."));
	}
	
	@Override
	public void reportException(String message) {
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new HTML("Exception while performing search. " + message + " See logs for details."));
	}
}
