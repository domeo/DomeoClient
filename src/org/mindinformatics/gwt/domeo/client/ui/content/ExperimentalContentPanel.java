package org.mindinformatics.gwt.domeo.client.ui.content;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ExperimentalContentPanel extends Composite {
	
	interface Binder extends UiBinder<HTMLPanel, ExperimentalContentPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField HTMLPanel main;
	
	@SuppressWarnings("unused")
	private IApplication _application;
	
	public ExperimentalContentPanel(IApplication application) {
		_application = application;
		
		@SuppressWarnings("unused")
		Resources resources = Domeo.resources;
		
		initWidget(binder.createAndBindUi(this)); 
	}
}
