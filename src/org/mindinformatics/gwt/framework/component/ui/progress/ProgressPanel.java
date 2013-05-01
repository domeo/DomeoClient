package org.mindinformatics.gwt.framework.component.ui.progress;

import org.mindinformatics.gwt.domeo.client.Domeo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class ProgressPanel extends Composite {

	interface Binder extends UiBinder<HorizontalPanel, ProgressPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField SimplePanel iconPanel;
	@UiField SimplePanel messagePanel;
	
	HorizontalPanel hp = new HorizontalPanel();
	public ProgressPanel() {
		initWidget(binder.createAndBindUi(this)); 
	}
	
	public void setProgressMessage(String message) {
		iconPanel.clear();
		iconPanel.add(new Image(Domeo.resources.littleProgressIcon()));
		messagePanel.clear();
		messagePanel.add(new Label(message));
	}
	
	public void setCompletionMessage(String message) {
		iconPanel.clear();
		iconPanel.add(new Image(Domeo.resources.checkIcon()));
		messagePanel.clear();
		messagePanel.add(new Label(message));
	}
	
	public void setWarningMessage(String message) {
		iconPanel.clear();
		iconPanel.add(new Image(Domeo.resources.warningIcon()));
		messagePanel.clear();
		messagePanel.add(new Label(message));
	}
	
	public void setErrorMessage(String message) {
		iconPanel.clear();
		iconPanel.add(new Image(Domeo.resources.crossLittleIcon()));
		messagePanel.clear();
		messagePanel.add(new Label(message + " - see logs"));
	}
	
	public void clear() {
		iconPanel.clear();
		messagePanel.clear();
	}
}
