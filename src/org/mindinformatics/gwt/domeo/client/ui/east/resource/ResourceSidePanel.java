package org.mindinformatics.gwt.domeo.client.ui.east.resource;

import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.lenses.LOnlineResourceCardPanel;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.IResourceRefreshableComponent;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.ui.east.ASidePanel;
import org.mindinformatics.gwt.framework.component.ui.east.ASideTab;
import org.mindinformatics.gwt.framework.component.ui.east.SidePanelsFacade;
import org.mindinformatics.gwt.framework.component.ui.lenses.resources.IResourceLensComponent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The side panel for describing the resource. It collects a general
 * resource panel or a specific one if the resource is recognized.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ResourceSidePanel extends ASidePanel implements IRefreshableComponent, 
	IResourceRefreshableComponent, IInitializableComponent {

	interface Binder extends UiBinder<Widget, ResourceSidePanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField VerticalPanel body;

	public ResourceSidePanel(IDomeo domeo, SidePanelsFacade facade, ASideTab tab) {
		super(domeo, facade, tab);
		
		initWidget(binder.createAndBindUi(this));

		body.setHeight("100%");	
	}
	
	@Override
	public void init() {
		body.clear();
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(MGenericResource.PARAM_URI_READONLY, "true");
		params.put(MGenericResource.TITLE_URI_READONLY, "false");
		params.put(LOnlineResourceCardPanel.KNOWN_RESOURCE, "false");
		
//		Widget w = _application.getResourcePanelsManager().getResourcePanel(
//				((IDomeo)_application).getPersistenceManager().getCurrentResource().getClass().getName());
//		
//		((IResourceLensComponent)w).initializeLens(((IDomeo)_application)
//				.getPersistenceManager().getCurrentResource(), params);
	}
	
	@Override
	public void refresh() {
		body.clear();
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(MGenericResource.PARAM_URI_READONLY, "true");
		params.put(MGenericResource.TITLE_URI_READONLY, "false");
		params.put(LOnlineResourceCardPanel.KNOWN_RESOURCE, "false");
		
		Widget w = _application.getResourcePanelsManager().getResourcePanel(
				((IDomeo)_application).getPersistenceManager().getCurrentResource().getClass().getName());
		
		((IResourceLensComponent)w).initializeLens(((IDomeo)_application)
				.getPersistenceManager().getCurrentResource(), params);
		body.add(w);
	}
}
