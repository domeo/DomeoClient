package org.mindinformatics.gwt.domeo.plugins.resource.nif.search.antibodies;

import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AntibodiesSearch extends Composite {

	 interface Binder extends UiBinder<HorizontalPanel, AntibodiesSearch> { }
	 
	 private static final Binder binder = GWT.create(Binder.class);
	 
	 public static final String ALL_URI = "http://www.paolociccarese.info/ontology/misc/all";
	 public static final String ALL_LABEL = "All";
	 
	 public static final String CATALOG = "catalog";
	 public static final String NAME = "name";
	 public static final String CLONE = "clone";
	 
	 private AntibodiesSearchWidget _widget;
	 
	 @UiField ListBox searchType;
	 @UiField TextBox searchBox;
	 @UiField TextBox vendorBox;
	 @UiField Image rightSide;
	 @UiField Label numberResults;
	 @UiField Label filterLabel;
	 @UiField ListBox sourcesLabels;
	 
	 public AntibodiesSearch(AntibodiesSearchWidget widget, String exact) {
		 _widget = widget;
		 initWidget(binder.createAndBindUi(this));
		 
		if (exact.length() < 40) searchBox.setText(exact);
		searchBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				int charCode = event.getUnicodeCharCode();
				if (charCode == 0) {
					// it's probably Firefox
					int keyCode = event.getNativeEvent().getKeyCode();
				    if (keyCode == KeyCodes.KEY_ENTER) {
				    	_widget.performSearch(searchType.getValue(searchType.getSelectedIndex()), searchBox.getText().trim(), vendorBox.getText().trim());
				    }
				} else if (charCode == KeyCodes.KEY_ENTER) {
					_widget.performSearch(searchType.getValue(searchType.getSelectedIndex()), searchBox.getText().trim(), vendorBox.getText().trim());
				}
			}
		});
		
		/*
		vendorBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					_widget.performSearch(searchType.getValue(searchType.getSelectedIndex()), searchBox.getText().trim(), vendorBox.getText().trim());
				}
			}
		});
		*/
		
		//vendorBox.setEnabled(false);
		 
		rightSide.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				_widget.performSearch(searchType.getValue(searchType.getSelectedIndex()), searchBox.getText().trim(), vendorBox.getText().trim());
			}
		});
		 
		// For filtering results according to the source
		sourcesLabels.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				_widget.filterBySource(sourcesLabels.getValue(sourcesLabels.getSelectedIndex()));
			}
		});
		 
		sourcesLabels.setEnabled(false);
		
		searchType.addItem("Name", NAME);
		searchType.addItem("Catalog Number", CATALOG);
		searchType.addItem("Clone Number", CLONE);
	}
	 
	public void updateResultsStats() {

		sourcesLabels.setEnabled(true);

		String termLabel = _widget.getSearchTermsResult().size() > 1 ? " results"
				: " result";
		String vocabularyLabel = _widget.getSearchTermsResultSources().size() > 1 ? " vendors"
				: " vendor";

		numberResults.setText("" + _widget.getSearchTermsResult().size()
				+ termLabel + " in "
				+ _widget.getSearchTermsResultSources().size()
				+ vocabularyLabel);

		sourcesLabels.clear();

		Set<String> keys = _widget.getSearchTermsResultSources();
		sourcesLabels.addItem(ALL_LABEL, ALL_URI);
		for (String key : keys) {
			sourcesLabels.addItem(((key.length()>30)?(key.substring(0, 30)+"..."):key), key);
		}
	}
	 
	public String getSourceFilterValue() {
		return sourcesLabels.getValue(sourcesLabels.getSelectedIndex());
	}
}
