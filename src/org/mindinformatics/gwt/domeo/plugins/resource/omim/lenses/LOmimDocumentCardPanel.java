package org.mindinformatics.gwt.domeo.plugins.resource.omim.lenses;

import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.client.ui.east.resource.CitationReferencesPanel;
import org.mindinformatics.gwt.domeo.plugins.resource.document.lenses.LSlimDocumentResourceCardPanel;
import org.mindinformatics.gwt.domeo.plugins.resource.omim.model.MOmimDocument;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class LOmimDocumentCardPanel extends Composite implements IRefreshableComponent, IResourceLensComponent {
	
	public static final String KNOWN_RESOURCE = "KNOWN RESOURCE";
	
	interface Binder extends UiBinder<Widget, LOmimDocumentCardPanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField FlowPanel body;
	@UiField Image urlImage;
	@UiField EditableLabel uriField;
	//@UiField FlowPanel sourceField;
	@UiField FlowPanel aboutField;

	@UiField HTML extractionSourceDetails;
	@UiField Label extractionDateDetails;
	@UiField Label extractorProvenanceDetails;
	@UiField Image extractionProvenanceImage;
	
	@UiField SimplePanel contentPanel;
	@UiField TabLayoutPanel tabToolsPanel;
	
	@UiField ScrollPanel referencesPanel;
	
	// By contract 
	private IDomeo _domeo;
	private MOmimDocument _resource;
	private HandlerRegistration _iconClickHandler;
	private HashMap<String, String> _parameters;
	
	public LOmimDocumentCardPanel(IDomeo domeo) {
		_domeo = domeo;
		
		initWidget(binder.createAndBindUi(this));
		body.setHeight("100%");
		tabToolsPanel.setHeight(Window.getClientHeight()-45 + "px");
		//tabToolsPanel.addStyleName(style.tabLayoutPanel());
		urlImage.setVisible(false);
	}
	
	public void initializeLens(MGenericResource resource, HashMap<String, String> parameters) {
		Resources _resources = Domeo.resources;
		try {
			_resource = (MOmimDocument) resource;
			_parameters = parameters;
			
			if(parameters.containsKey(MOmimDocument.PARAM_URI_READONLY)) {
				if(parameters.get(MOmimDocument.PARAM_URI_READONLY).equals("true")) {
					uriField.setReadOnly(true);
				}
			}
			
			if (_iconClickHandler!=null) _iconClickHandler.removeHandler();
			_iconClickHandler = urlImage.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.alert("Show extractor details not implemented yet");
				}
			});
			
			refresh();
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while initialing resource info " + e.getMessage());
			body.clear();
			body.add(new HTML("<img src='" + _resources.crossLittleIcon().getSafeUri().asString() + "'/> Exception while initialing OMIM resource info " + e.getMessage()));
		}
	}
	
	private void init() {
		//sourceField.clear();
		aboutField.clear();
	}
	
	public void refresh() {
		Resources _resources = Domeo.resources;
		
		try {
			init();
			uriField.setValue(_resource.getUrl());
			//sourceField.add(new HTML("<img src='" + Domeo.resources.externalLinkIcon().getURL() + "'/> <a href='" + 
			//	_resource.getSource().getHomepage() + "' target='_blank'>"+_resource.getSource().getName() + "</a> "));
			
			String synonyms = _resource.getIsAbout().getSynonyms();			
			aboutField.add(new HTML(_resource.getIsAbout().getLabel() + " " + (_resource.getIsAbout().getSynonyms()!=null ? synonyms: "")));

			if(_domeo.getExtractorsManager().isExtractorDefined()) {
				urlImage.setResource(_resources.checkIcon());
				urlImage.setTitle("Known resource, executed extractor '" + _domeo.getExtractorsManager().getExtractorLabel() +"'");
				urlImage.setVisible(true);
			} else {
				urlImage.setVisible(false);
			}
			
			if(_resource!=null) {
				extractionProvenanceImage.setUrl(Domeo.resources.pluginsGrayLittleIcon().getSafeUri().asString());
				extractionProvenanceImage.setTitle(_resource.getCreator().getName());
				extractorProvenanceDetails.setText("Extracted by " + _resource.getCreator().getName());
				extractionSourceDetails.setHTML("Source <a href='" + 
						_resource.getSource().getHomepage() + "' target='_blank'> <img src='" + Domeo.resources.externalLinkIcon().getSafeUri().asString() + "'/> "+_resource.getSource().getName()+ "</a>");
				extractionDateDetails.setText("On " + _resource.getFormattedCreationDate());
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while rendering OMIM resource info " + e.getMessage());
			body.clear();
			body.add(new HTML("<img src='" + _resources.crossLittleIcon().getSafeUri().asString() + "'/> Exception while rendering OMIM resource info " + e.getMessage()));
		}
		
		// References
		try {
			CitationReferencesPanel p = new CitationReferencesPanel(_domeo, false);
			referencesPanel.clear();
			referencesPanel.add(p);
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while rendering resource info " + e.getMessage());
			referencesPanel.clear();
			referencesPanel.add(new HTML("<img src='" + _resources.crossLittleIcon().getSafeUri().asString() + "'/> Exception while rendering OMIM resource info " + e.getMessage()));
		}
		
		// Generic Document Resource 
		try {
			LSlimDocumentResourceCardPanel d = new LSlimDocumentResourceCardPanel(_domeo);
			d.initializeLens(_resource, _parameters);
			contentPanel.clear();
			contentPanel.add(d);
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while rendering resource info " + e.getMessage());
			body.clear();
			body.add(new HTML("<img src='" + _resources.crossLittleIcon().getSafeUri().asString() + "'/> Exception while rendering OMIM resource info " + e.getMessage()));
		}
	}
}
