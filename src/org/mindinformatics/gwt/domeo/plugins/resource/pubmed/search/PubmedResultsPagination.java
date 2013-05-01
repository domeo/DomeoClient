package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search;

import org.mindinformatics.gwt.domeo.client.IDomeo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PubmedResultsPagination extends Composite {

	// UI binder
	interface Binder extends UiBinder<HorizontalPanel, PubmedResultsPagination> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField HorizontalPanel main;
	@UiField SimplePanel paginationInfoPanel;
	@UiField Button previousPage;
	@UiField TextBox goToPage;
	@UiField Button nextPage;
	
	public PubmedResultsPagination(IDomeo domeo) {
		initWidget(binder.createAndBindUi(this));
	}
	
	public void setInfoPanel(Widget infoPanel) {
		paginationInfoPanel.setWidget(infoPanel);
	}
	
	public void setButtonsPanel(int pageNumber, int totalPages) {
		if(pageNumber>1) previousPage.setVisible(true);
		else previousPage.setVisible(false);
		goToPage.setVisible(true);
		if(pageNumber<totalPages) nextPage.setVisible(true);
		else nextPage.setVisible(false);
	}
	
	public Button getPreviousPageButton() {
		return previousPage;
	}
	
	public TextBox getTextBox() {
		return goToPage;
	}
	
	public Button getNextPageButton() {
		return nextPage;
	}
}
