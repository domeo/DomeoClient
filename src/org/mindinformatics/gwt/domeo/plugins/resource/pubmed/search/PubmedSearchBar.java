package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search;

import java.util.HashMap;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors.APubMedBibliograhyExtractorCommand;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PubmedSearchBar extends Composite {

	 public static final String ALL_URI = "http://www.paolociccarese.info/ontology/misc/all";
	 public static final String ALL_LABEL = "All";
	
	// UI binder
	interface Binder extends UiBinder<HorizontalPanel, PubmedSearchBar> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private IDomeo _domeo;
	private PubmedSearchWidget _searchWidget;
	private HandlerRegistration previousHandlerRegistration;
	private HandlerRegistration nextHandlerRegistration;
	
	
	@UiField ListBox searchType;
	@UiField TextBox searchBox;
	@UiField Image rightSide;
	@UiField Label numberResults;
	@UiField Label filterLabel;
	@UiField ListBox sourcesLabels;
	@UiField HorizontalPanel filterPanel;
	
	public PubmedSearchBar(IDomeo domeo, PubmedSearchWidget searchWidget) {
		_domeo = domeo;
		_searchWidget = searchWidget;
		
		initWidget(binder.createAndBindUi(this));
		filterPanel.setVisible(false);
		 
		searchBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getUnicodeCharCode() == 0 || event.getCharCode() == KeyCodes.KEY_ENTER) {
					_domeo.getLogger().command(this, "Searching PubMed (textbox)  by *" + 
							searchType.getValue(searchType.getSelectedIndex()) + "* for " +
							searchBox.getText());
					_searchWidget.performSearch(searchType.getValue(searchType.getSelectedIndex()), searchBox.getText(), 10, 0);
				}
			}
		});
		 
		rightSide.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				_domeo.getLogger().command(this, "Searching PubMed (button) by *" + 
						searchType.getValue(searchType.getSelectedIndex()) + "* for " +
						searchBox.getText());
				_searchWidget.performSearch(searchType.getValue(searchType.getSelectedIndex()), searchBox.getText(), 10, 0);
			}
		});
		 
		// For filtering results according to the source
		sourcesLabels.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				_searchWidget.filterBySource(sourcesLabels.getValue(sourcesLabels.getSelectedIndex()));
			}
		});
		
		/*
		_searchWidget.getPagingPanel().getTextBox().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
			}
		});
		 */
		
		sourcesLabels.setEnabled(false);
		
		searchType.addItem("Title", APubMedBibliograhyExtractorCommand.TITLE);
		searchType.addItem("PubMed Ids", APubMedBibliograhyExtractorCommand.PUBMED_IDS);
		searchType.addItem("PMC Ids", APubMedBibliograhyExtractorCommand.PUBMED_CENTRAL_IDS);
	}
	
	public void updateResultsStats() {
		sourcesLabels.setEnabled(true);
		
		String totalLabel = _searchWidget.getTotal() > 1 ? " results" : " result";
		String termLabel = _searchWidget.getOffset() + "-" + (_searchWidget.getOffset()+_searchWidget.getRange()) + " of " + _searchWidget.getTotal() + totalLabel;
		
		numberResults.setText(termLabel);
		numberResults.setVisible(true);

		sourcesLabels.clear();

		HashMap<String, String> map = _searchWidget.getSearchTermsResultSources();
		sourcesLabels.addItem(ALL_LABEL, ALL_URI);
		Set<String> keys = map.keySet();
		for (String key : keys) {
			sourcesLabels.addItem(map.get(key), key);
		}
		
		// Calculating pages
		int pageNumber = (_searchWidget.getOffset()/_searchWidget.getRange()) + 1;
		int totalPages = (_searchWidget.getTotal()/_searchWidget.getRange());
		
		Label infoPanelLabel = new Label("Page " + pageNumber + " of " + totalPages);
		_searchWidget.getPagingPanel().setInfoPanel(infoPanelLabel);
		_searchWidget.getPagingPanel().setButtonsPanel(pageNumber, totalPages);
		
		if(previousHandlerRegistration!=null) previousHandlerRegistration.removeHandler(); 
		previousHandlerRegistration = _searchWidget.getPagingPanel().getPreviousPageButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_searchWidget.performSearch(searchType.getValue(searchType.getSelectedIndex()), 
					searchBox.getText(), _searchWidget.getRange(), _searchWidget.getOffset()-_searchWidget.getRange());	
			}
		});
		
		_searchWidget.getPagingPanel().getTextBox().setValue(""+pageNumber);
		
		if(nextHandlerRegistration!=null) nextHandlerRegistration.removeHandler(); 
		nextHandlerRegistration = _searchWidget.getPagingPanel().getNextPageButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_searchWidget.performSearch(searchType.getValue(searchType.getSelectedIndex()), 
					searchBox.getText(), _searchWidget.getRange(), _searchWidget.getOffset()+_searchWidget.getRange());	
			}
		});
	}
	 
	public String getSourceFilterValue() {
		return sourcesLabels.getValue(sourcesLabels.getSelectedIndex());
	}
}
