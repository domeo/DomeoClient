package org.mindinformatics.gwt.framework.component.ui.lenses.resources;

import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.ui.lenses.ILensComponent;
import org.mindinformatics.gwt.framework.widget.EditableLabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class LGenericResourceCardPanel extends Composite implements IRefreshableComponent, 
		IResourceLensComponent, ILensComponent {

	interface Binder extends UiBinder<Widget, LGenericResourceCardPanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField VerticalPanel body;
	@UiField EditableLabel uriField;
	@UiField EditableLabel labelField;
	
	// By contract 
	@SuppressWarnings("unused")
	private IDomeo _domeo;
	
	private MGenericResource _resource;
	
	public LGenericResourceCardPanel(IDomeo domeo) {
		_domeo = domeo;
		
		initWidget(binder.createAndBindUi(this));
		body.setHeight("100%");
	}
	
	public void initializeLens(MGenericResource resource, HashMap<String, String> parameters) {
		_resource = resource;
		if(parameters.containsKey(MGenericResource.PARAM_URI_READONLY)) {
			if(parameters.get(MGenericResource.PARAM_URI_READONLY).equals("true"))
				uriField.setReadOnly(true);
		}
		if(parameters.containsKey(MGenericResource.TITLE_URI_READONLY)) {
			if(parameters.get(MGenericResource.TITLE_URI_READONLY).equals("true"))
				labelField.setReadOnly(true);
		}
		refresh();
	}
	
	@Override
	public void refresh() {
		refreshLens();
	}

	@Override
	public void refreshLens() {
		uriField.setValue(_resource.getUrl());
		labelField.setValue(_resource.getLabel());
	}
}
