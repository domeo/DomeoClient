package org.mindinformatics.gwt.domeo.client.ui.east.resource;

import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class ResourceSidePanelTopbar  extends Composite {

	interface Binder extends UiBinder<HorizontalPanel, ResourceSidePanelTopbar> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	@SuppressWarnings("unused")
	private IApplication _application;
	
	@UiField HorizontalPanel sidePanelTopbar;
	@UiField Label titlePanel;
	
	public ResourceSidePanelTopbar(IApplication application, String title) {
		_application = application;
		
		initWidget(binder.createAndBindUi(this));
		
		titlePanel.setText(title);
	}
}
