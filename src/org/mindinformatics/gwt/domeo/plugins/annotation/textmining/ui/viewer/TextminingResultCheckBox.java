package org.mindinformatics.gwt.domeo.plugins.annotation.textmining.ui.viewer;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class TextminingResultCheckBox extends Composite {
	
	interface Binder extends UiBinder<HorizontalPanel, TextminingResultCheckBox> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private IDomeo _domeo;
	private MLinkedResource _resource;
	
	@UiField HorizontalPanel main;
	@UiField CheckBox checkbox;
	@UiField HTML label;
	
	public TextminingResultCheckBox(IDomeo domeo, MLinkedResource resource, Integer counter) {
		_domeo = domeo;
		_resource = resource;
		
		initWidget(binder.createAndBindUi(this));
		
		label.setHTML("<a href='" + resource.getUrl() + "' target='_blank'>" + resource.getLabel() +  " - "+ counter + "</a>");
	}
	
	public String getSelection() {
		if(checkbox.getValue()) return _resource.getUrl();
		else return null;
	}
}
