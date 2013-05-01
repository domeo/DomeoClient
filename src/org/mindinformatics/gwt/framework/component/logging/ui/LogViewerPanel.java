package org.mindinformatics.gwt.framework.component.logging.ui;

import org.mindinformatics.gwt.framework.component.ui.glass.IExportable;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IResizable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class LogViewerPanel extends Composite implements IContentPanel, IResizable, IExportable {

	private static final String TITLE = "Log Stream";
	
	interface Binder extends UiBinder<FlowPanel, LogViewerPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	//private Resources _resources;
	private IApplication _application;
	private IContainerPanel _containerPanel;
	
	// Layout
	@UiField FlowPanel main;
	@UiField ScrollPanel scrollPanel;
	
	private LogListPanel _listPanel;
	
	
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
	public LogViewerPanel(IApplication application) {
		_application = application;
		//_resources = resources;
		_listPanel = new LogListPanel(_application, false);

		// Create layout
		initWidget(binder.createAndBindUi(this)); 
		this.setWidth((Window.getClientWidth() - 140) + "px");
		
		scrollPanel.add(_listPanel);
		scrollPanel.setWidth((Window.getClientWidth() - 134) + "px");
	}

	@Override
	public void resized() {
		scrollPanel.setWidth((Window.getClientWidth() - 134) + "px");
	}

	@Override
	public void export() {
		LogListPanel logListPanel = new LogListPanel(_application, true);
		_application.getReportManager().displayWidget(logListPanel);
	}
}

