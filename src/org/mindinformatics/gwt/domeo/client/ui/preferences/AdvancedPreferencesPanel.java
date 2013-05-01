package org.mindinformatics.gwt.domeo.client.ui.preferences;

import java.util.Arrays;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.component.logging.src.LogLevel;
import org.mindinformatics.gwt.framework.component.logging.src.LogsManager;
import org.mindinformatics.gwt.framework.component.preferences.src.BooleanPreference;
import org.mindinformatics.gwt.framework.component.preferences.src.TextPreference;
import org.mindinformatics.gwt.framework.src.Application;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IResizable;
import org.mindinformatics.gwt.utils.src.HtmlUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ValueListBox;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AdvancedPreferencesPanel extends Composite implements IContentPanel, IResizable {

	private static final String TITLE = "Advanced";
	
	interface Binder extends UiBinder<FlowPanel, AdvancedPreferencesPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	//private Resources _resources;
	private IDomeo _domeo;
	private IContainerPanel _containerPanel;
	private PreferencesViewerPanel _parent;

	// Layout
	@UiField SimplePanel loggingLevelPanel;
	@UiField Label localStorageDetectionPanel;
	@UiField CheckBox provenanceDisplayCheckBox;
	@UiField CheckBox provenanceHideUsersProvenanceCheckBox;
	@UiField CheckBox automaticReportCheckBox;
	@UiField CheckBox multipleTargetsCheckBox;
	
	public void setContainer(IContainerPanel containerPanel) {
		_containerPanel = containerPanel;
	}
	
	public IContainerPanel getContainer() {
		return _containerPanel;
	}
	
	public String getTitle() {
		return TITLE;
	}
	
	
	// ------------------------------------------------------------------------
	//  CREATION OF ANNOTATIONS OF VARIOUS KIND
	// ------------------------------------------------------------------------
	public AdvancedPreferencesPanel(IDomeo domeo, PreferencesViewerPanel parent) {
		_domeo = domeo;
		_parent = parent;
		//_resources = resources;
		//_listPanel = new LogListPanel(_application);

		// Create layout
		initWidget(binder.createAndBindUi(this)); 
		this.setWidth((Window.getClientWidth() - 140) + "px");
		
		//tabToolsPanel.setWidth((Window.getClientWidth() - 145) + "px");
		
		ValueListBox<LogLevel> levelPicker = new ValueListBox<LogLevel>(new AbstractRenderer<LogLevel>() {
		    @Override
		    public String render(LogLevel object) {
		        return object == null ? null : object.toString();
		    }
		});
		levelPicker.addValueChangeHandler(new ValueChangeHandler<LogLevel>() {
			@Override
			public void onValueChange(ValueChangeEvent<LogLevel> event) {
				((TextPreference)_domeo.getPreferences().
						getPreferenceItem(Application.class.getName(), LogsManager.LEVEL)).setValue(event.getValue().toString());
				_parent.refreshPreferencesList();
			}
		});
		
		HtmlUtils htmlUtils = new HtmlUtils();
		if(htmlUtils.isStorageEnabled()) localStorageDetectionPanel.setText("Enabled");
		else localStorageDetectionPanel.setText("Disabled");

		if(((BooleanPreference)_domeo.getPreferences().
				getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_PROVENANCE))!=null) {
			provenanceDisplayCheckBox.setValue(((BooleanPreference)_domeo.getPreferences().
				getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_PROVENANCE)).getValue());
		}
		provenanceDisplayCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				_domeo.getPreferences().changePreferenceItemValue(
						Domeo.class.getName(), Domeo.PREF_DISPLAY_PROVENANCE, provenanceDisplayCheckBox.getValue().toString());
				_parent.refreshPreferencesList();
				_domeo.refreshAnnotationComponents();
			}
		});
		
		if(((BooleanPreference)_domeo.getPreferences().
				getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_USER_PROVENANCE))!=null) {
			provenanceHideUsersProvenanceCheckBox.setValue(((BooleanPreference)_domeo.getPreferences().
				getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_USER_PROVENANCE)).getValue());
		}
		provenanceHideUsersProvenanceCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				_domeo.getPreferences().changePreferenceItemValue(
						Domeo.class.getName(), Domeo.PREF_DISPLAY_USER_PROVENANCE, provenanceHideUsersProvenanceCheckBox.getValue().toString());
				_parent.refreshPreferencesList();
				_domeo.refreshAnnotationComponents();
			}
		});
		
		if(((BooleanPreference)_domeo.getPreferences().
				getPreferenceItem(Application.class.getName(), Application.PREF_AUTOMATICALLY_REPORT_ISSUES))!=null) {
			automaticReportCheckBox.setValue(((BooleanPreference)_domeo.getPreferences().
				getPreferenceItem(Application.class.getName(), Application.PREF_AUTOMATICALLY_REPORT_ISSUES)).getValue());
		}
		automaticReportCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				_domeo.getPreferences().changePreferenceItemValue(
						Application.class.getName(), Application.PREF_AUTOMATICALLY_REPORT_ISSUES, automaticReportCheckBox.getValue().toString());
				_parent.refreshPreferencesList();
				_domeo.refreshAnnotationComponents();
			}
		});
		
		if(((BooleanPreference)_domeo.getPreferences().
				getPreferenceItem(Application.class.getName(), Domeo.PREF_ANN_MULTIPLE_TARGETS))!=null) {
			multipleTargetsCheckBox.setValue(((BooleanPreference)_domeo.getPreferences().
				getPreferenceItem(Application.class.getName(), Domeo.PREF_ANN_MULTIPLE_TARGETS)).getValue());
		}
		multipleTargetsCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				_domeo.getPreferences().changePreferenceItemValue(
						Application.class.getName(), Domeo.PREF_ANN_MULTIPLE_TARGETS, multipleTargetsCheckBox.getValue().toString());
				_parent.refreshPreferencesList();
				_domeo.refreshAnnotationComponents();
			}
		});
		
		levelPicker.setAcceptableValues(Arrays.asList(LogLevel.values()));
		levelPicker.setValue(LogLevel.getLevelByValue(((TextPreference)_domeo.getPreferences().
			getPreferenceItem(Application.class.getName(), LogsManager.LEVEL)).getValue()));
		loggingLevelPanel.add(levelPicker);
	}

	@Override
	public void resized() {
		this.setWidth((Window.getClientWidth() - 140) + "px");
	}
}

