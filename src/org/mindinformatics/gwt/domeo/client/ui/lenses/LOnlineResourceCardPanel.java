package org.mindinformatics.gwt.domeo.client.ui.lenses;

import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.ui.lenses.resources.IResourceLensComponent;
import org.mindinformatics.gwt.framework.widget.EditableLabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class LOnlineResourceCardPanel extends Composite implements IRefreshableComponent, IResourceLensComponent {
	
	public static final String KNOWN_RESOURCE = "KNOWN RESOURCE";
	
	interface Binder extends UiBinder<Widget, LOnlineResourceCardPanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField VerticalPanel body;
	@UiField Image urlImage;
	@UiField EditableLabel uriField;
	@UiField EditableLabel labelField;
	
	// By contract 
	private IDomeo _domeo;
	private MGenericResource _resource;
	private HandlerRegistration _iconClickHandler;
	
	public LOnlineResourceCardPanel(IDomeo domeo) {
		_domeo = domeo;
		
		initWidget(binder.createAndBindUi(this));
		body.setHeight("100%");
		urlImage.setVisible(false);
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
		
		if (_iconClickHandler!=null) _iconClickHandler.removeHandler();
		_iconClickHandler = urlImage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Show extractor details not implemented yet");
			}
		});
		
		refresh();
	}
	
	@SuppressWarnings("unused")
	private void init() {}
	
	public void refresh() {
		uriField.setValue(_resource.getUrl());
		labelField.setValue(_resource.getLabel());
		
		Resources _resources = Domeo.resources;
		if(_domeo.getExtractorsManager().isExtractorDefined()) {
			urlImage.setResource(_resources.checkIcon());
			urlImage.setTitle("Known resource, loaded extractor '" + _domeo.getExtractorsManager().getExtractorLabel() +"'");
			urlImage.setVisible(true);
		} else {
			urlImage.setVisible(false);
		}
	}
}
