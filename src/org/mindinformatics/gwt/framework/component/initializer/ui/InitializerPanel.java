package org.mindinformatics.gwt.framework.component.initializer.ui;

import org.mindinformatics.gwt.framework.src.Application;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InitializerPanel extends Composite {
	
	interface Binder extends UiBinder<VerticalPanel, InitializerPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField HorizontalPanel initializerPanel;
	@UiField VerticalPanel messages;
	@UiField Image spinningIcon;
	@UiField Label initMessage;
	
	public InitializerPanel(IApplication application) {

		initWidget(binder.createAndBindUi(this)); 
		
		spinningIcon.setResource(
			Application.applicationResources.spinningIcon());
	}
	
	public void updateMessage(String message) {
		initMessage.setText(message);
	}
	
	public void addMessage(String message) {
		messages.add(new Label(message));
	}
	
	public void addCompletionMessage(String message) {
		messages.add(new HTML("<span style=\"font-weight: bold;\">"+message+"... DONE!</span>"));
	}
	
	public void addException(String message) {
		messages.add(new HTML("<span style=\"color: red;\">"+message+"</span>"));
	}
}