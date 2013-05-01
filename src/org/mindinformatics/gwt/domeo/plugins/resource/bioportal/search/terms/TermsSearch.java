package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.search.terms;

import java.util.HashMap;
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
public class TermsSearch extends Composite {

	 interface Binder extends UiBinder<HorizontalPanel, TermsSearch> { }
	 
	 private static final Binder binder = GWT.create(Binder.class);
	 
	 public static final String ALL_URI = "http://www.paolociccarese.info/ontology/misc/all";
	 public static final String ALL_LABEL = "All";
	 
	 private SearchTermsWidget _widget;
	 
	 @UiField TextBox searchBox;
	 @UiField Image rightSide;
	 @UiField Label numberResults;
	 @UiField Label filterLabel;
	 @UiField ListBox sourcesLabels;
	 
	 public TermsSearch(SearchTermsWidget widget, String exact) {
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
				    	_widget.performSearch(searchBox.getText());
				    }
				} else if (charCode == KeyCodes.KEY_ENTER) {
					_widget.performSearch(searchBox.getText());
				}
			}
		});
		 
		rightSide.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				_widget.performSearch(searchBox.getText());
			}
		});
		 
		// For filtering results according to the source
		sourcesLabels.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				_widget.filterBySource(sourcesLabels.getValue(sourcesLabels.getSelectedIndex()));
			}
		});
		 
		sourcesLabels.setEnabled(false);
	}
	 
	public void updateResultsStats() {

		sourcesLabels.setEnabled(true);

		String termLabel = _widget.getSearchTermsResult().size() > 1 ? " terms"
				: " term";
		String vocabularyLabel = _widget.getSearchTermsResultSources().size() > 1 ? " vocabularies"
				: " vocabulary";

		numberResults.setText("" + _widget.getSearchTermsResult().size()
				+ termLabel + " in "
				+ _widget.getSearchTermsResultSources().size()
				+ vocabularyLabel);

		sourcesLabels.clear();

		HashMap<String, String> map = _widget.getSearchTermsResultSources();
		sourcesLabels.addItem(ALL_LABEL, ALL_URI);
		Set<String> keys = map.keySet();
		for (String key : keys) {
			sourcesLabels.addItem(map.get(key), key);
		}
	}
	 
	public String getSourceFilterValue() {
		return sourcesLabels.getValue(sourcesLabels.getSelectedIndex());
	}
	
	public void setSearchBoxValue(String value) {
		searchBox.setValue(value);
	}
}
