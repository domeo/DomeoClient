package org.mindinformatics.gwt.framework.component.ui.lenses.agents;

import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentDatabase;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.ui.lenses.resources.IResourceLensComponent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class LAgentDatabasePanel extends Composite implements IRefreshableComponent, IResourceLensComponent {

	interface Binder extends UiBinder<Widget, LAgentDatabasePanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField VerticalPanel body;
	@UiField Label uriField;
	@UiField Label labelField;
	@UiField Label homepageField;
	
	// By contract 
	@SuppressWarnings("unused")
	private IDomeo _domeo;
	
	private MAgentDatabase _resource;
	
	public LAgentDatabasePanel(IDomeo domeo) {
		_domeo = domeo;
		
		initWidget(binder.createAndBindUi(this));
		body.setHeight("100%");
	}
	
	public void initializeLens(MGenericResource resource, HashMap<String, String> parameters) {
		_resource = (MAgentDatabase) resource;
		refresh();
	}
	
	public void refresh() {
		uriField.setText(_resource.getUrl());
		labelField.setText(_resource.getName());
		homepageField.setText(_resource.getHomepage());
	}
}
