package org.mindinformatics.gwt.framework.component.statusbar.ui;

import org.mindinformatics.gwt.framework.src.Application;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class StatusbarPanel extends Composite {
	
	interface Binder extends UiBinder<HorizontalPanel, StatusbarPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField HorizontalPanel initializerPanel;
	@UiField Image spinningIcon;
	@UiField Label initMessage;
	
	public StatusbarPanel(IApplication application) {

		initWidget(binder.createAndBindUi(this)); 
		
		spinningIcon.setResource(
			Application.applicationResources.spinningIcon());
	}
	
	public void updateMessage(String message) {
		initMessage.setText(message);
	}
}