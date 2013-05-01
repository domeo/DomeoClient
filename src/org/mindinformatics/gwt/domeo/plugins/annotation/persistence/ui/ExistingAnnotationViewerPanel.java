package org.mindinformatics.gwt.domeo.plugins.annotation.persistence.ui;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.service.AnnotationPersistenceServiceFacade;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsoAnnotationSetSummary;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IResizable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ExistingAnnotationViewerPanel extends Composite implements IContainerPanel, IContentPanel, IResizable {

	private static final String TITLE = "Import of Existing Annotation";
	
	interface Binder extends UiBinder<VerticalPanel, ExistingAnnotationViewerPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	//private Resources _resources;
	private IDomeo _domeo;
	private IContainerPanel _containerPanel;
	private ExistingAnnotationSetsListPanel privateAnnotationSetsPanel;
	private ExistingAnnotationSetsListPanel groupsAnnotationSetsPanel;
	private ExistingAnnotationSetsListPanel publicAnnotationSetsPanel;

	// Layout
	@UiField VerticalPanel main;
	@UiField TabLayoutPanel tabToolsPanel;
	
	public boolean tabToolsPanelDisabled;
	
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
	public ExistingAnnotationViewerPanel(IDomeo domeo, JsArray responseOnSets) {
		_domeo = domeo;
		//_resources = resources;
		//_listPanel = new LogListPanel(_application);

		// Create layout
		initWidget(binder.createAndBindUi(this)); 
		this.setWidth((Window.getClientWidth() - 140) + "px");
		
		try {
			List<JsoAnnotationSetSummary> privateList = new ArrayList<JsoAnnotationSetSummary> ();
			List<JsoAnnotationSetSummary> groupsList = new ArrayList<JsoAnnotationSetSummary> ();
			List<JsoAnnotationSetSummary> publicList = new ArrayList<JsoAnnotationSetSummary> ();
			for(int i=0; i< responseOnSets.length(); i++) {
				//Window.alert(((JsoAnnotationSetSummary)responseOnSets.get(i)).getPermissionsAccessType() + " ---- " + _domeo.getAgentManager().getUserPerson().getUri());
				if(((JsoAnnotationSetSummary)responseOnSets.get(i)).isPublic()) {
					publicList.add((JsoAnnotationSetSummary)responseOnSets.get(i));
				} else if(((JsoAnnotationSetSummary)responseOnSets.get(i)).isGroups()) {
					groupsList.add((JsoAnnotationSetSummary)responseOnSets.get(i));
				} else if(((JsoAnnotationSetSummary)responseOnSets.get(i)).getPermissionsAccessType().equals(_domeo.getAgentManager().getUserPerson().getUri())) {
					privateList.add((JsoAnnotationSetSummary)responseOnSets.get(i));
				}
			}
			
			if(privateList.size()>0) {
				privateAnnotationSetsPanel = new ExistingAnnotationSetsListPanel(_domeo, this, AnnotationPersistenceServiceFacade.ALLOW_MINE, privateList);
				tabToolsPanel.add(privateAnnotationSetsPanel, "Private");
			}
			
			if(groupsList.size()>0) {
				groupsAnnotationSetsPanel = new ExistingAnnotationSetsListPanel(_domeo, this, AnnotationPersistenceServiceFacade.ALLOW_GROUPS, groupsList);
				tabToolsPanel.add(groupsAnnotationSetsPanel, "Groups");
			}
			
			if(publicList.size()>0) {
				publicAnnotationSetsPanel = new ExistingAnnotationSetsListPanel(_domeo, this, AnnotationPersistenceServiceFacade.ALLOW_PUBLIC, publicList);
				tabToolsPanel.add(publicAnnotationSetsPanel, "Public");
			}
			
			tabToolsPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
				@Override
				public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
				    // Simple if statement - your test for whether the tab should be disabled
				    // will probably be more complicated
				    if (tabToolsPanelDisabled) {
				      // Canceling the event prevents the tab from being selected.
				      event.cancel();
				    }
				}
			});
		} catch (Exception e) {
			_domeo.getLogger().debug(this, "ExistingAnnotationViewerPanel " + e.getMessage());
		}

	}

	public void refreshPreferencesList() {
		//completePreferencesList.refresh();
	}
	
	@Override
	public void resized() {
		this.setWidth((Window.getClientWidth() - 140) + "px");
		tabToolsPanel.setWidth((Window.getClientWidth() - 130) + "px");
	}

	@Override
	public void hide() {
		_containerPanel.hide();
	}
}

