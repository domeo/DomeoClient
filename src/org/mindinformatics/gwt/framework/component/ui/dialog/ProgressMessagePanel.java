package org.mindinformatics.gwt.framework.component.ui.dialog;

import org.mindinformatics.gwt.framework.src.Application;
import org.mindinformatics.gwt.framework.src.ApplicationResources;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProgressMessagePanel extends Composite implements IContentPanel {

	interface Binder extends UiBinder<VerticalPanel, ProgressMessagePanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField Image icon;
	@UiField HTML firstLineMessagePanel;
	@UiField HTML secondLineMessagePanel;
	@UiField FlowPanel buttonsPanel;
	
	protected IApplication _application;
	protected IContainerPanel _containerPanel;
	protected ApplicationResources _resources;
	
	protected String _subMessage;
	
	public void setContainer(IContainerPanel containerPanel) {
		_containerPanel = containerPanel;
	}
	
	public IContainerPanel getContainer() {
		return _containerPanel;
	}
	
	public ProgressMessagePanel(IApplication application, String message, String subMessage) {
		_application = application;
		_subMessage = subMessage;
		_resources = Application.applicationResources;
		
		// Create layout
		initWidget(binder.createAndBindUi(this)); 
		
		icon.setResource(_resources.progressIcon());
		firstLineMessagePanel.setText(message);
		secondLineMessagePanel.setText(subMessage);
		
		this.setWidth((Window.getClientWidth() - 140) + "px");
	}
	
	public void setMessage(String message) {
		String before = firstLineMessagePanel.getHTML();
		firstLineMessagePanel.setHTML(before + "<br/>" + message);
	}
	
	public void addErrorMessage(String message) {
		String before = firstLineMessagePanel.getHTML();
		firstLineMessagePanel.setHTML(before + "<br/><font style=\"color: red;\">" + message + "</font>");
	}
	
	public void setSuccessIcon() {
		icon.setResource(_resources.questionLittleIcon());
	}
	
	public void setIcon(ImageResource resource) {
		icon.setResource(resource);
	}
	
	protected void addButton(String label, ClickHandler clickHandler) {
		Button newButton = new Button(label);
		if(clickHandler!=null) newButton.addClickHandler(clickHandler);
		buttonsPanel.add(newButton);
	}
	
	protected void addCancelButton() {
		Button newButton = new Button("Cancel");
		newButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_containerPanel.hide();
			}
		});
		buttonsPanel.add(newButton);
	}
}
