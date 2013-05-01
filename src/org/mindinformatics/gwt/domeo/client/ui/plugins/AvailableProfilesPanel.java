package org.mindinformatics.gwt.domeo.client.ui.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.component.profiles.model.MProfile;
import org.mindinformatics.gwt.framework.component.profiles.src.IProfileManager;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IResizable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AvailableProfilesPanel extends Composite implements IContentPanel, IResizable {

	private static final String TITLE = "Available Profiles";
	
	interface Binder extends UiBinder<FlowPanel, AvailableProfilesPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	//private Resources _resources;
	private IDomeo _domeo;
	private IContainerPanel _containerPanel;
	private PluginsViewerPanel _parent;

	// Layout
	@UiField ScrollPanel pluginsPanel;
	@UiField Label footerMessage;
	@UiField Button loadButton;
	@UiField VerticalPanel pluginInfoPanel;
	
	MProfile _currentProfile;
	
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
	public AvailableProfilesPanel(IDomeo domeo, PluginsViewerPanel parent) {
		_domeo = domeo;
		_parent = parent;
		//_resources = resources;
		//_listPanel = new LogListPanel(_application);

		// Create layout
		initWidget(binder.createAndBindUi(this)); 
		this.setWidth((Window.getClientWidth() - 140) + "px");
		
		loadButton.setEnabled(false);
		loadButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_domeo.getProfileManager().setCurrentProfile(_currentProfile);
				_parent.refreshCurrentProfile(true);
				_parent.selectTab(0);
			}
		});
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new Image(Domeo.resources.littleProgressIcon()));
		hp.add(new Label("Loading available profiles..."));
		pluginsPanel.add(hp);
		
		IProfileManager profileManager = _domeo.getProfileManager();
		ArrayList<MProfile> profiles = profileManager.getUserProfiles();
		createList(profiles);
	}
	
	public void createList(ArrayList<MProfile> profiles) {
		pluginsPanel.clear();
		VerticalPanel vp = new VerticalPanel();
		if(profiles.size()>0) { 
			for(MProfile profile: profiles) {
				vp.add(new ProfileSummaryLens(_domeo, this, profile));
			}
		} else {
			vp.add(new HTML("No additional profiles available"));
		}
		pluginsPanel.add(vp);
	}
	

	static <T> DefaultSelectionEventManager<T> myMethod(int column) {
	   //call whatever functions you want
	   return DefaultSelectionEventManager.<T> createCheckboxManager(column);
	}

	public void setSelectedProfile(MProfile profile) {
		_currentProfile = profile;
		loadButton.setEnabled(true);
		
		pluginInfoPanel.clear();
		pluginInfoPanel.add(new ProfileLens(_domeo, this, profile));

		HashMap<String, String> pluginsMap = profile.getPlugins();
		Set<String> keys = pluginsMap.keySet();
		for(String key: keys) {
			if(pluginsMap.get(key).equals(MProfile.PLUGIN_ENABLED))
				pluginInfoPanel.add(new HTML("<b>"+key+"</b>"));
		}
	}

	@Override
	public void resized() {
		this.setWidth((Window.getClientWidth() - 140) + "px");
	}
	
	public void refreshMessagePanel() {
		if(_domeo.getProfileManager().getUserCurrentProfile().isChanged()) {
			footerMessage.setText("*Changes will be made available at the next Domeo reload.");
		}
	}
}

