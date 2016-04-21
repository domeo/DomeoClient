/*
 * Copyright 2013 Massachusetts General Hospital
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.picker;

import java.util.ArrayList;
import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.ICache;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublication;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.service.IMicroPublicationsConnector;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.service.IRetrieveMicropublicationsHandler;
import org.mindinformatics.gwt.domeo.plugins.annotopia.micropubs.src.AnnotopiaMicropubsConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors.PubMedDocumentPipeline;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class StatementsSearchWidget extends Composite implements IStatementPaginatedItemsRequestCompleted, IStatementItemsRequestCompleted, IRetrieveMicropublicationsHandler {

	public static final String WIDGET_TITLE = "PubMed Search";
	
	// UI BInder
	interface Binder extends UiBinder<VerticalPanel, StatementsSearchWidget> {}
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private IDomeo _domeo;
	private IStatementsSearchObjectContainer _container;
		
	@UiField VerticalPanel main;
	private Panel resultsContainerPanel;
	private StatementsSearchBar searchBarPanel;
	private StatementsResultsPagination pagingPanel;
	private StatementsResultsListPanel publicationsListPanel;
	
	private HashMap<String, MMicroPublicationAnnotation> associatedPublications = 
		new HashMap<String, MMicroPublicationAnnotation>();
	private ArrayList<MMicroPublicationAnnotation> searchPublicationsResult; 
	private int total;
	private int offset;
	private int range;
	
	public StatementsSearchWidget(IDomeo domeo, IStatementsSearchObjectContainer container, boolean showTitle) {
		_domeo = domeo;
		_container = container;
		
		initWidget(binder.createAndBindUi(this)); // Necessary for initializing Composite
		
		if(showTitle) main.add(new Label(WIDGET_TITLE));
		
		// Search panel
		searchBarPanel = new StatementsSearchBar(_domeo, this);
		main.add(searchBarPanel);
		
		// Result panel
		resultsContainerPanel = new FlowPanel();
		resultsContainerPanel.setHeight("440px");
		resultsContainerPanel.add(new HTML("No results to display"));
		main.add(resultsContainerPanel);	
		
		pagingPanel = new StatementsResultsPagination(_domeo);
		main.add(pagingPanel);
	}
	
	public ArrayList<MMicroPublicationAnnotation> getMicroPublicationObjects() {
		return _container.getMicroPublicationObjects();
	}
	
	public void performSearch(String typeQuery, String textQuery, int maxResults, int offset) {
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new Image(Domeo.resources.progressIcon()));
		
		try {
			if(typeQuery.equals("Local")) {
				ICache c = _domeo.getAnnotationPersistenceManager().getCache(MMicroPublicationAnnotation.class.getName());
				ArrayList<MAnnotation> anns = c.getCachedAnnotations();
				ArrayList<MMicroPublicationAnnotation> a = new ArrayList<MMicroPublicationAnnotation>();
				for(MAnnotation ann: anns) {
					a.add((MMicroPublicationAnnotation)ann);
				}
				returnMicroPublicationObject(a);
			} else {
				IMicroPublicationsConnector micropubsConnector = new AnnotopiaMicropubsConnector(_domeo);
				micropubsConnector.searchMicropublications(this, typeQuery, textQuery, maxResults, offset);
				//PubMedManager pubMedManager = PubMedManager.getInstance();
				//pubMedManager.selectPubMedConnector(_domeo, this);
				//pubMedManager.searchBibliographicObjects(this, typeQuery, textQuery, maxResults, offset);
			}	
		} catch(Exception exc) {
			_domeo.getLogger().exception(PubMedDocumentPipeline.LOGGER, 
				this.getClass().getName(), "Exception while searching for Micro Publications " + 
					exc.getMessage());
			return;
		}	
	}
	
	public void filterBySource(String sourceId) {
		//publicationsResultsPanel.update(sourceId);
	}
	
	public String getFilterValue() {
		return searchBarPanel.getSourceFilterValue();
	}
	
	public ArrayList<MMicroPublicationAnnotation> getSearchTermsResult() {
		return searchPublicationsResult;
	}
	
	public HashMap<String, String> getSearchTermsResultSources() {
		HashMap<String, String> map = new HashMap<String, String>();
		/*
		for(int i=0; i< searchPublicationsResult.size(); i++) {
			if(!map.containsKey(searchPublicationsResult.get(i).getProvidedBy().getUrl()))
				map.put(searchPublicationsResult.get(i).getProvidedBy().getUrl(), 
						searchPublicationsResult.get(i).getProvidedBy().getName());
		}
		*/
		return map;
	}
	
	private void displayResults(ArrayList<MMicroPublicationAnnotation> list) {
		resultsContainerPanel.clear();
		
		/*
		FlowPanel fp = new FlowPanel();
		for(int i=0; i<list.size(); i++) {
			HorizontalPanel hp = new HorizontalPanel();
			//if(i%2 == 1) hp.setStyleName(style.oddRow());
			hp.add(PubMedCitationPainter.getCitation(list.get(i)));
			fp.add(hp);
		}
		resultsContainerPanel.add(fp);
		*/
		
		try {
			publicationsListPanel = new StatementsResultsListPanel(_domeo, this, list, associatedPublications, 
					StatementsResultsListPanel.SINGLE_SELECTION);
			resultsContainerPanel.add(publicationsListPanel);
		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
		
		updateResultsNumbers();
		
	}
	
	private void displayResults(int total, int offset, int range, ArrayList<MMicroPublicationAnnotation> list) {
		this.total = total;
		this.offset = offset;
		this.range = range;
		
		resultsContainerPanel.clear();
		try {
			publicationsListPanel = new StatementsResultsListPanel(_domeo, this, list, associatedPublications, 
					StatementsResultsListPanel.SINGLE_SELECTION);
			resultsContainerPanel.add(publicationsListPanel);
		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
		updateResultsNumbers();	
	}
	
	public void updateResultsNumbers() {
		searchBarPanel.updateResultsStats();
	}

	public HashMap<String, MMicroPublicationAnnotation> getPublications() {
		return associatedPublications;
	}
	
	public void addStatement(MMicroPublicationAnnotation publication) {
		//_domeo.getLogger().command(this, "Selected publication: " + publication.getAuthorNames() + " " + publication.getTitle() + " " + publication.getJournalPublicationInfo());
		_container.addMicroPublicationObject(publication);
	}
	
	public ArrayList<MMicroPublicationAnnotation> getTermsResults() {
		return searchPublicationsResult;
	}

	@Override
	public void returnMicroPublicationObject(ArrayList<MMicroPublicationAnnotation> reference) {
		searchPublicationsResult = reference;
		displayResults(reference);
	}
	
	@Override
	public void returnMicroPublicationObject(int total, int offset, int range, ArrayList<MMicroPublicationAnnotation> reference) {
		searchPublicationsResult = reference;
		displayResults(total, offset, range, reference);
	}

	public StatementsResultsPagination getPagingPanel() {
		return pagingPanel;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	@Override
	public void microPublicationObjectNotFound() {
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new HTML("Exception while searching PubMed"));
	}

	@Override
	public void microPublicationObjectNotFound(String message) {
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new HTML(message));
	}

	@Override
	public void microPublicationSearchNotCompleted() {
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new HTML("Exception while searching PubMed"));
	}

	@Override
	public void microPublicationSearchNotCompleted(String message) {
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new HTML(message));
	}

	@Override
	public void emptyResultSet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void returnResources(ArrayList<MMicroPublication> microPublications) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void returnResource(MMicroPublication microPublication) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resourceNotFound(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exception(String message) {
		// TODO Auto-generated method stub
		
	}
}
