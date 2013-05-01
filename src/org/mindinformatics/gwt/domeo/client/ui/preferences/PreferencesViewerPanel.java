package org.mindinformatics.gwt.domeo.client.ui.preferences;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.preferences.styles.StylePreferencesPanel;
import org.mindinformatics.gwt.framework.component.preferences.ui.CompletePreferencesList;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IResizable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PreferencesViewerPanel extends Composite implements IContentPanel, IResizable {

	private static final String TITLE = "Preferences";
	
	interface Binder extends UiBinder<VerticalPanel, PreferencesViewerPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	//private Resources _resources;
	private IDomeo _domeo;
	private IContainerPanel _containerPanel;

	// Layout
	@UiField VerticalPanel main;
	@UiField TabLayoutPanel tabToolsPanel;
	
	private StylePreferencesPanel stylePreferencesPanel;
	private AdvancedPreferencesPanel advancedPreferencesPanel;
	private CompletePreferencesList completePreferencesList;
	
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
	public PreferencesViewerPanel(IDomeo domeo) {
		_domeo = domeo;
		//_resources = resources;
		//_listPanel = new LogListPanel(_application);

		// Create layout
		initWidget(binder.createAndBindUi(this)); 
		this.setWidth((Window.getClientWidth() - 140) + "px");
		
		stylePreferencesPanel = new StylePreferencesPanel(_domeo, this);
		tabToolsPanel.add(stylePreferencesPanel, stylePreferencesPanel.getTitle());
		
		advancedPreferencesPanel = new AdvancedPreferencesPanel(_domeo, this);
		tabToolsPanel.add(advancedPreferencesPanel, advancedPreferencesPanel.getTitle());
		
		completePreferencesList = new CompletePreferencesList(_domeo);
		tabToolsPanel.add(completePreferencesList, "All properties");
	}

	public void refreshPreferencesList() {
		completePreferencesList.refresh();
	}
	
	@Override
	public void resized() {
		this.setWidth((Window.getClientWidth() - 140) + "px");
		tabToolsPanel.setWidth((Window.getClientWidth() - 130) + "px");
	}
}

