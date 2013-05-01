package org.mindinformatics.gwt.domeo.client.ui.content;

import org.mindinformatics.gwt.domeo.client.IDomeo;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ContentPanel extends Composite {
	
	public static final String FRAME_ID = "domeoframe";
	
	// By contract 
	private IDomeo _application;
	
	// Elements
	private AnnotationFrameWrapper frameWrapper;
	//private Frame frame;
	private SimplePanel main;
	//private AnnotationDetailsPanel annotationDetailsPanel;
	
	public ContentPanel(IDomeo application) {
		_application = application;
		
		Frame frame = new Frame();
		frame.setStyleName("cs-Frame");
		DOM.setElementAttribute(frame.getElement(), "id", FRAME_ID);
		frameWrapper = new AnnotationFrameWrapper(_application, frame);
		frameWrapper.getFrame().setWidth("100%");
		frameWrapper.getFrame().setHeight("100%");
		
		main = new SimplePanel();
		main.setWidth("100%");
		main.setHeight("100%");
		main.setStyleName("cs-frameContainer");
		main.add(frameWrapper.getFrame());
		
		initWidget(main); // Necessary for initializing Composite 
	}
	
	public AnnotationFrameWrapper getAnnotationFrameWrapper() {
		return frameWrapper;
	}
	
	/*
	public void showDetailsPanel() {
		main.setWidget(0, 1, annotationDetailsPanel);
		main.getCellFormatter().setStyleName(0, 1, "cs-AnnotationDetailsColumn");
	}
	
	public void hideDetailsPanel() {
		main.removeCell(0, 1);
	}

	public AnnotationDetailsPanel getAnnotationDetailsPanel() {
		return annotationDetailsPanel;
	}

	public void setAnnotationDetailsPanel(
			AnnotationDetailsPanel annotationDetailsPanel) {
		this.annotationDetailsPanel = annotationDetailsPanel;
	}
	
	public void refresh() {
		annotationDetailsPanel.notifyDocumentAnnotationSummaryChange();
	}
	*/
}
