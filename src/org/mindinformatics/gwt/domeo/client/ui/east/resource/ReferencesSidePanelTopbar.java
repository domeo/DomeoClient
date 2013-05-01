package org.mindinformatics.gwt.domeo.client.ui.east.resource;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.framework.component.preferences.src.BooleanPreference;
import org.mindinformatics.gwt.framework.component.preferences.src.TextPreference;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReferencesSidePanelTopbar  extends Composite {

	interface Binder extends UiBinder<VerticalPanel, ReferencesSidePanelTopbar> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private IDomeo _domeo;
	private boolean _isBibliographicSetVirtual;
	
	@UiField HorizontalPanel sidePanelProvenanceTopbar;
	@UiField HorizontalPanel sidePanelTopbar;
	@UiField CheckBox showCitations;
	@UiField CheckBox showReferences;
	@UiField Button retrieveReferences;
	@UiField Label savedOnField;
	@UiField Label versionField;
	@UiField Image rightSide;
	
	public ReferencesSidePanelTopbar(IDomeo domeo) {
		_domeo = domeo;
		
		initWidget(binder.createAndBindUi(this));
		
		retrieveReferences.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Retrieve references");
			}
		});

		if(!((TextPreference) _domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_REFERENCES)).getValue().equals(IBibliographicParameters.SKIP)) {
			if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_PERFORM_ANNOTATION_OF_REFERENCES)).getValue()) {
				showReferences.setValue(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_SHOW_ANNOTATION_OF_REFERENCES)).getValue());
				showReferences.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						_domeo.getContentPanel().getAnnotationFrameWrapper().manageSetHighlight(_domeo.getAnnotationPersistenceManager().getBibliographicSet(), showReferences.getValue());
					}
				});
			} else {
				showReferences.setValue(false);
				showReferences.setEnabled(false);
				showReferences.setTitle("Feature disabled as the preference is set on 'not performing annotation of references'");
			}
		} else {
			showReferences.setVisible(false);
		}
		if(!((TextPreference) _domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.EXTRACT_CITATIONS)).getValue().equals(IBibliographicParameters.SKIP)) {
			if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_PERFORM_ANNOTATION_OF_CITATIONS)).getValue()) {
				showCitations.setValue(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_SHOW_ANNOTATION_OF_CITATIONS)).getValue());
				showCitations.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						_domeo.getContentPanel().getAnnotationFrameWrapper().manageSetHighlightFragments(_domeo.getAnnotationPersistenceManager().getBibliographicSet(), showCitations.getValue());
					}
				});
			} else {
				showCitations.setValue(false);
				showCitations.setEnabled(false);
				showCitations.setTitle("Feature disabled as the preference is set on 'not performing annotation of citations'");
			}
		} else {
			showCitations.setVisible(false);
		}
		
		

		//retrieveReferences.setVisible(false);
	}
	
	public void refresh(boolean isBibliographicSetEmpty, boolean isBibliographicSetVirtual) {
		
		boolean retrieveReferencesFlag = 
			((TextPreference) _domeo.getPreferences().getPreferenceItem(IBibliographicParameters.COMPONENT, IBibliographicParameters.RETRIEVE_REFERENCES))
			.getValue().equals(IBibliographicParameters.ENABLED);
		
		if(!retrieveReferencesFlag || !isBibliographicSetEmpty) {
			if(!isBibliographicSetVirtual) {
				showReferences.setVisible(true);
				sidePanelTopbar.setVisible(true);
			} else {
				showReferences.setVisible(false);
				sidePanelTopbar.setVisible(false);
			}
			retrieveReferences.setVisible(false);
			sidePanelProvenanceTopbar.setVisible(true);
			rightSide.setVisible(true);
		} else {
			showReferences.setVisible(false);
			sidePanelProvenanceTopbar.setVisible(false);
			rightSide.setVisible(false);
		}
		
		MAnnotationSet _set = _domeo.getAnnotationPersistenceManager().getBibliographicSet();
		if(_set.getFormattedLastSavedOn()!=null)
			savedOnField.setText(_set.getFormattedLastSavedOn()+(_set.getHasChanged()?"*":""));
		else savedOnField.setText("<not saved>");
		if(_set.getVersionNumber()!=null) 
			versionField.setText(_set.getVersionNumber());
		else versionField.setText("<none>");
	}
}
