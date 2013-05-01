package org.mindinformatics.gwt.domeo.client.ui.east.debug;

import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class AnnotationDebugSidePanelTopbar  extends Composite {

	interface Binder extends UiBinder<HorizontalPanel, AnnotationDebugSidePanelTopbar> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	@SuppressWarnings("unused")
	private IApplication _application;
	
	public AnnotationDebugSidePanelTopbar(IApplication application) {
		_application = application;
		
		initWidget(binder.createAndBindUi(this));
	}
	
}
