package org.mindinformatics.gwt.domeo.plugins.resource.document.lenses;

import java.util.Arrays;
import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.CachedImagesPanel;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.ICachedImages;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class LDocumentResourceCardPanel extends Composite implements IRefreshableComponent, 
	IResourceLensComponent, ICachedImages {
	
	public static final String KNOWN_RESOURCE = "KNOWN RESOURCE";
	
	interface Binder extends UiBinder<Widget, LDocumentResourceCardPanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField VerticalPanel content;
	@UiField Image urlImage;

	@UiField Label extractionDateDetails;
	@UiField Label extractorProvenanceDetails;
	@UiField Image extractionProvenanceImage;
	
	@UiField EditableLabel uriField;
	@UiField EditableLabel labelField;
	@UiField EditableLabel descriptionField;
	@UiField EditableLabel keywordsField;
	@UiField TabLayoutPanel tabToolsPanel;
	
	@UiField ScrollPanel imagesPanel;
	
	// By contract 
	private IDomeo _domeo;
	private MDocumentResource _resource;
	private HandlerRegistration _iconClickHandler;
	@SuppressWarnings("unused")
	private HashMap<String, String> _parameters;
	
	CachedImagesPanel p;
	
	public LDocumentResourceCardPanel(IDomeo domeo) {
		_domeo = domeo;
		
		initWidget(binder.createAndBindUi(this));
		content.setHeight("100%");
		urlImage.setVisible(false);
		
		tabToolsPanel.setHeight(Window.getClientHeight()-25 + "px");
	}
	
	public void initializeLens(MGenericResource resource, HashMap<String, String> parameters) {
		Resources _resources = Domeo.resources;
		try {
			_resource = ((MDocumentResource)resource);
			_parameters = parameters;
			
			if(parameters.containsKey(MDocumentResource.PARAM_URI_READONLY)) {
				if(parameters.get(MDocumentResource.PARAM_URI_READONLY).equals("true"))
					uriField.setReadOnly(true);
			} else {
				uriField.setReadOnly(true);
			}
			if(parameters.containsKey(MDocumentResource.TITLE_URI_READONLY)) {
				if(parameters.get(MDocumentResource.TITLE_URI_READONLY).equals("true"))
					labelField.setReadOnly(true);
			} else {
				labelField.setReadOnly(true);
			}
			if(parameters.containsKey(MDocumentResource.PARAM_DESCRIPTION_READONLY)) {
				if(parameters.get(MDocumentResource.PARAM_DESCRIPTION_READONLY).equals("true"))
					descriptionField.setReadOnly(true);
			} else {
				descriptionField.setReadOnly(true);
			}
			/*
			if(parameters.containsKey(MDocumentResource.PARAM_AUTHORS_READONLY)) {
				if(parameters.get(MDocumentResource.PARAM_AUTHORS_READONLY).equals("true"))
					authorsField.setReadOnly(true);
			} else {
				authorsField.setReadOnly(true);
			}
			*/
			if(parameters.containsKey(MDocumentResource.PARAM_KEYWORDS_READONLY)) {
				if(parameters.get(MDocumentResource.PARAM_KEYWORDS_READONLY).equals("true"))
					keywordsField.setReadOnly(true);
			} else {
				keywordsField.setReadOnly(true);
			}
			
			if (_iconClickHandler!=null) _iconClickHandler.removeHandler();
			_iconClickHandler = urlImage.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.alert("Show extractor details not implemented yet");
				}
			});
			
			if(_resource!=null) {
				extractionProvenanceImage.setUrl(Domeo.resources.pluginsGrayLittleIcon().getSafeUri().asString());
				extractionProvenanceImage.setTitle(_resource.getCreator().getName());
				extractionDateDetails.setText("On " + _resource.getFormattedCreationDate());
				extractorProvenanceDetails.setText("Extracted by " + _resource.getCreator().getName());
			}
			

			//createVisualization();
			
			refresh();
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while initializing generic resource info " + e.getMessage());
			content.clear();
			content.add(new HTML("<img src='" + _resources.crossLittleIcon().getSafeUri().asString() + "'/> Exception while initializing generic resource info " + e.getMessage()));
		}
	}
	
	public void refresh() {
		Resources _resources = Domeo.resources;
		try {
			uriField.setValue(_resource.getUrl());
			
			if(_resource.getLabel()!=null && _resource.getLabel().trim().length()>0) labelField.setValue(_resource.getLabel());
			else labelField.setValue("<none>");
			
			if(_resource.getDescription()!=null && _resource.getDescription().trim().length()>0) descriptionField.setValue(_resource.getDescription());
			else descriptionField.setValue("<none>");
			
			if(_resource.getKeywords()!=null) keywordsField.setValue(Arrays.toString(_resource.getKeywords()));
			else keywordsField.setValue("<none>");
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while rendering generic resource info " + e.getMessage());
			content.clear();
			content.add(new HTML("<img src='" + _resources.crossLittleIcon().getSafeUri().asString() + "'/> Exception while rendering generic resource info " + e.getMessage()));	
		}
		
		// Images
		try {
			p = new CachedImagesPanel(_domeo);
			imagesPanel.clear();
			imagesPanel.add(p);
			createVisualization();
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while rendering cached images " + e.getMessage());
			imagesPanel.clear();
			imagesPanel.add(new HTML("<img src='" + _resources.crossLittleIcon().getSafeUri().asString() + "'/> Exception while rendering cached images " + e.getMessage()));
		}
	}

	@Override
	public void createVisualization() {
		_domeo.getLogger().debug(this, "Creating Images Visualization");
		p.create();
		
		int scrollerHeight = Window.getClientHeight()-64;
		imagesPanel.setHeight("" + scrollerHeight + "px"); 
	}
}
