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
package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors.PubMedDocumentPipeline;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.IPubMedItemsRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.IPubMedPaginatedItemsRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.PubMedManager;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

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
public class PubmedSearchWidget extends Composite implements IPubMedPaginatedItemsRequestCompleted, IPubMedItemsRequestCompleted {

	public static final String WIDGET_TITLE = "PubMed Search";
	
	// UI BInder
	interface Binder extends UiBinder<VerticalPanel, PubmedSearchWidget> {}
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private IDomeo _domeo;
	private IPubmedSearchObjectContainer _container;
		
	@UiField VerticalPanel main;
	private Panel resultsContainerPanel;
	private PubmedSearchBar searchBarPanel;
	private PubmedResultsPagination pagingPanel;
	private PublicationResultsListPanel publicationsListPanel;
	
	private HashMap<String, MPublicationArticleReference> associatedPublications = 
		new HashMap<String, MPublicationArticleReference>();
	private ArrayList<MPublicationArticleReference> searchPublicationsResult; 
	private int total;
	private int offset;
	private int range;
	
	public PubmedSearchWidget(IDomeo domeo, IPubmedSearchObjectContainer container, boolean showTitle) {
		_domeo = domeo;
		_container = container;
		
		initWidget(binder.createAndBindUi(this)); // Necessary for initializing Composite
		
		if(showTitle) main.add(new Label(WIDGET_TITLE));
		
		// Search panel
		searchBarPanel = new PubmedSearchBar(_domeo, this);
		main.add(searchBarPanel);
		
		// Result panel
		resultsContainerPanel = new FlowPanel();
		resultsContainerPanel.setHeight("440px");
		resultsContainerPanel.add(new HTML("No results to display"));
		main.add(resultsContainerPanel);	
		
		pagingPanel = new PubmedResultsPagination(_domeo);
		main.add(pagingPanel);
	}
	
	public ArrayList<MPublicationArticleReference> getBibliographicObjects() {
		return _container.getBibliographicObjects();
	}
	
	public void performSearch(String typeQuery, String textQuery, int maxResults, int offset) {
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new Image(Domeo.resources.progressIcon()));
		
		try {
			PubMedManager pubMedManager = PubMedManager.getInstance();
			pubMedManager.selectPubMedConnector(_domeo, this);
			pubMedManager.searchBibliographicObjects(this, typeQuery, textQuery, maxResults, offset);
		} catch(Exception exc) {
			_domeo.getLogger().exception(PubMedDocumentPipeline.LOGGER, 
				this.getClass().getName(), "Exception while extracting bibliographic object from PubMed " + 
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
	
	public ArrayList<MPublicationArticleReference> getSearchTermsResult() {
		return searchPublicationsResult;
	}
	
	public HashMap<String, String> getSearchTermsResultSources() {
		HashMap<String, String> map = new HashMap<String, String>();
		for(int i=0; i< searchPublicationsResult.size(); i++) {
			if(!map.containsKey(searchPublicationsResult.get(i).getProvidedBy().getUrl()))
				map.put(searchPublicationsResult.get(i).getProvidedBy().getUrl(), 
						searchPublicationsResult.get(i).getProvidedBy().getName());
		}
		return map;
	}
	
	private void displayResults(ArrayList<MPublicationArticleReference> list) {
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
			publicationsListPanel = new PublicationResultsListPanel(_domeo, this, list, associatedPublications, 
					PublicationResultsListPanel.SINGLE_SELECTION);
			resultsContainerPanel.add(publicationsListPanel);
		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
		
		updateResultsNumbers();
		
	}
	
	private void displayResults(int total, int offset, int range, ArrayList<MPublicationArticleReference> list) {
		this.total = total;
		this.offset = offset;
		this.range = range;
		
		resultsContainerPanel.clear();
		try {
			publicationsListPanel = new PublicationResultsListPanel(_domeo, this, list, associatedPublications, 
					PublicationResultsListPanel.SINGLE_SELECTION);
			resultsContainerPanel.add(publicationsListPanel);
		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
		updateResultsNumbers();	
	}
	
	public void updateResultsNumbers() {
		searchBarPanel.updateResultsStats();
	}

	public HashMap<String, MPublicationArticleReference> getPublications() {
		return associatedPublications;
	}
	
	public void addPublication(MPublicationArticleReference publication) {
		_domeo.getLogger().command(this, "Selected publication: " + publication.getAuthorNames() + " " + publication.getTitle() + " " + publication.getJournalPublicationInfo());
		_container.addBibliographicObject(publication);
	}
	
	public ArrayList<MPublicationArticleReference> getTermsResults() {
		return searchPublicationsResult;
	}

	@Override
	public void returnBibliographicObject(ArrayList<MPublicationArticleReference> reference) {
		searchPublicationsResult = reference;
		displayResults(reference);
	}
	
	@Override
	public void returnBibliographicObject(int total, int offset, int range, ArrayList<MPublicationArticleReference> reference) {
		searchPublicationsResult = reference;
		displayResults(total, offset, range, reference);
	}

	public PubmedResultsPagination getPagingPanel() {
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
	public void bibliographyObjectNotFound() {
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new HTML("Exception while searching PubMed"));
	}

	@Override
	public void bibliographyObjectNotFound(String message) {
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new HTML(message));
	}

	@Override
	public void bibliographicSearchNotCompleted() {
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new HTML("Exception while searching PubMed"));
	}

	@Override
	public void bibliographicSearchNotCompleted(String message) {
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new HTML(message));
	}
}
