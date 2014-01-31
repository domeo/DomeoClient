package org.mindinformatics.gwt.domeo.plugins.resource.document.lenses;

import java.util.Arrays;
import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.client.ui.east.resource.ReferencesSidePanelTopbar;
import org.mindinformatics.gwt.domeo.client.ui.east.resource.qualifiers.QualifiersSearchPanel;
import org.mindinformatics.gwt.domeo.client.ui.east.resource.references.CitationReferencesPanel;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.CachedImagesPanel;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.ICachedImages;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.BibliographyManager;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.IStarringRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.lenses.PubMedCitationPainter;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search.PubmedReferenceSearchPanel;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.profiles.model.IProfile;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.ui.glass.EnhancedGlassPanel;
import org.mindinformatics.gwt.framework.component.ui.lenses.resources.IResourceLensComponent;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
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
	IResourceLensComponent, ICachedImages, IStarringRequestCompleted {
	
	public static final String KNOWN_RESOURCE = "KNOWN RESOURCE";
	
	interface Binder extends UiBinder<Widget, LDocumentResourceCardPanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField VerticalPanel content;
	@UiField Image urlImage;

	
	
	@UiField HTML extractionSourceDetails;
	@UiField Label extractionDateDetails;
	@UiField Label extractorProvenanceDetails;
	@UiField Image extractionProvenanceImage;
	
	// @UiField EditableLabel uriField;
	@UiField HTML uriField;
	
	@UiField EditableLabel labelField;
	@UiField EditableLabel descriptionField;
	@UiField EditableLabel keywordsField;
	@UiField TabLayoutPanel tabToolsPanel;
	
	@UiField HorizontalPanel myBibilographyToolbarPanel;
	@UiField Image starImage;
	@UiField Label starLabel;
	@UiField HorizontalPanel myRecommendationsToolbarPanel;
	@UiField Image recommendImage;
	@UiField Label recommendLabel;
	@UiField HTML uriField2;
	@UiField Label titleField;
	@UiField FlowPanel self;
	@UiField FlowPanel identifiersField;
	@UiField FlowPanel citationField;
	
	@UiField VerticalPanel bibliographyToolbarPanel;
	@UiField ScrollPanel referencesPanel;
	
	@UiField VerticalPanel qualifiersToolbarPanel;
	@UiField ScrollPanel qualifiersPanel;
	
	@UiField ScrollPanel imagesPanel;
	
	// By contract 
	private IDomeo _domeo;
	private MDocumentResource _resource;
	private HandlerRegistration _iconClickHandler;
	@SuppressWarnings("unused")
	private HashMap<String, String> _parameters;
	
	HandlerRegistration _starImageHandler;
	HandlerRegistration _starLabelHandler;
	
	private ReferencesSidePanelTopbar referencesSidePanelTopbar;
	private ClickHandler starAction;
	private ClickHandler unstarAction;
	
	CachedImagesPanel p;
	
	public LDocumentResourceCardPanel(IDomeo domeo) {
		_domeo = domeo;
		
		initWidget(binder.createAndBindUi(this));
		content.setHeight("100%");
		urlImage.setVisible(false);
		
		if(_domeo.getProfileManager().getUserCurrentProfile().isFeatureEnabled(IProfile.FEATURE_REFERENCE_SELF)) {
			citationField.clear();
			if(!_domeo.getProfileManager().getUserCurrentProfile().isFeatureEnabled(IProfile.FEATURE_QUALIFIERS_SELF)) {
				tabToolsPanel.remove(2);
			}
		} else {
			tabToolsPanel.remove(0);
			tabToolsPanel.remove(0);
			if(!_domeo.getProfileManager().getUserCurrentProfile().isFeatureEnabled(IProfile.FEATURE_QUALIFIERS_SELF)) {
				tabToolsPanel.remove(0);
			}
		}
		
		
		
		tabToolsPanel.setHeight(Window.getClientHeight()-25 + "px");
	}
	
	public void initializeLens(MGenericResource resource, HashMap<String, String> parameters) {
		Resources _resources = Domeo.resources;
		try {
			_resource = ((MDocumentResource)resource);
			_parameters = parameters;
			
			/*
			if(parameters.containsKey(MDocumentResource.PARAM_URI_READONLY)) {
				if(parameters.get(MDocumentResource.PARAM_URI_READONLY).equals("true"))
					uriField.setReadOnly(true);
			} else {
				uriField.setReadOnly(true);
			}
			*/

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
			
			if(_domeo.getProfileManager().getUserCurrentProfile().isFeatureEnabled(IProfile.FEATURE_REFERENCE_SELF)) {
				bibliographyToolbarPanel.clear();
				referencesSidePanelTopbar = new ReferencesSidePanelTopbar(_domeo);
				bibliographyToolbarPanel.add(referencesSidePanelTopbar);
				bibliographyToolbarPanel.setCellWidth(referencesSidePanelTopbar, "100%");
				
				
				
	
				int scrollerHeight = Window.getClientHeight()-100;
				referencesPanel.setHeight("" + scrollerHeight + "px"); 
			}
			
			final IStarringRequestCompleted _this = this;
			starAction = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					BibliographyManager manager = BibliographyManager.getInstance();	
					manager.selectPubMedConnector(_domeo);
					if(_domeo.getAnnotationPersistenceManager().getCurrentResource() instanceof MDocumentResource) {
						_domeo.getLogger().debug(this, "starActionHandler");
						manager.starResource((MDocumentResource)_domeo.getAnnotationPersistenceManager().getCurrentResource(), _this);
					}
				}
			};
			unstarAction = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					BibliographyManager manager = BibliographyManager.getInstance();	
					manager.selectPubMedConnector(_domeo);
					if(_domeo.getAnnotationPersistenceManager().getCurrentResource() instanceof MDocumentResource) {
						_domeo.getLogger().debug(this, "unstarActionHandler");
						manager.unstarResource((MDocumentResource)_domeo.getAnnotationPersistenceManager().getCurrentResource(), _this);
					}			
				}
			};
			
			if(resource!=null && _domeo.getProfileManager().getUserCurrentProfile().isFeatureEnabled(IProfile.FEATURE_MY_BIBLIOGRAPHY)) {
				BibliographyManager bm = BibliographyManager.getInstance();
				bm.selectPubMedConnector(_domeo);
				bm.isResourceStarred((MDocumentResource)resource, this);
			} else {
				myBibilographyToolbarPanel.setVisible(false);
			}
			
			if(_domeo.getProfileManager().getUserCurrentProfile().isFeatureEnabled(IProfile.FEATURE_MY_RECOMMENDATIONS)) {
				recommendImage.setResource(Domeo.resources.shareDocumentIcon());
			} else {
				myRecommendationsToolbarPanel.setVisible(false);
			}
				
			refresh();
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while initializing generic resource info " + e.getMessage());
			content.clear();
			content.add(new HTML("<img src='" + _resources.crossLittleIcon().getSafeUri().asString() + "'/> Exception while initializing generic resource info " + e.getMessage()));
		}
	}
	
	private void init() {
		//sourceField.clear();
		if(_domeo.getProfileManager().getUserCurrentProfile().isFeatureEnabled(IProfile.FEATURE_REFERENCE_SELF)) {
			citationField.clear();
			identifiersField.clear();
		}
//		pubMedToolbarPanel.clear();
//		generalToolbarPanel.clear();
//		pubMedToolbarPanel.add(new ResourceSidePanelTopbar(_domeo, "PubMed Resource Information"));
//		generalToolbarPanel.add(new ResourceSidePanelTopbar(_domeo, "Generic Resource Information"));
		//bibliographyToolbarPanel.clear();
	}
	
	public void refresh() {
		Resources _resources = Domeo.resources;
		try {
			init();
			if(_resource.getSelfReference()!=null) {
				extractionProvenanceImage.setUrl(Domeo.resources.pluginsGrayLittleIcon().getSafeUri().asString());
				extractionProvenanceImage.setTitle(_resource.getCreator().getName());

				if(_resource.getSource()!=null)
					extractionSourceDetails.setHTML("Source <a href='" + _resource.getSource().getHomepage() + "' target='_blank'> <img src='" + Domeo.resources.externalLinkIcon().getSafeUri().asString() + "'/> "+_resource.getSource().getName()+ "</a>");

				if(_domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference()!=null) {
					extractorProvenanceDetails.setText("Contributed by " + _domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference().getCreator().getName());
				} else {
					extractorProvenanceDetails.setText("Extracted by " +  _resource.getCreator().getName());
				}
			
				//((MPublicationArticleReference)_resource.getSelfReference().getReference()).get);
				if(_domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference()!=null) {
					extractionDateDetails.setText("On " + _domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference().getFormattedCreationDate());
				} else {
					extractionDateDetails.setText("On " + _resource.getFormattedCreationDate());
				}
				
				if(_domeo.getProfileManager().getUserCurrentProfile().isFeatureEnabled(IProfile.FEATURE_REFERENCE_SELF)) {
					citationField.add(PubMedCitationPainter.getCitation((MPublicationArticleReference)_resource.getSelfReference().getReference()));
					identifiersField.add(PubMedCitationPainter.getIdentifiers((MPublicationArticleReference)_resource.getSelfReference().getReference(), _domeo));
				}
			} else {
				ClickHandler addCitationClickHandler = new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						_domeo.getLogger().command(this, "Open panel for adding a citation"); 
						PubmedReferenceSearchPanel afp = new PubmedReferenceSearchPanel(_domeo);
						new EnhancedGlassPanel(_domeo, afp, afp.getTitle(), false, false, false);
					}
				};
				Image addCitationIcon = new Image(Domeo.resources.addLittleIcon());
				addCitationIcon.addClickHandler(addCitationClickHandler);
				Label addCitationLabel = new Label("Add citation");
				addCitationLabel.addClickHandler(addCitationClickHandler);
				HorizontalPanel fp = new HorizontalPanel();
				fp.add(addCitationIcon);
				fp.add(addCitationLabel);
				if(_domeo.getProfileManager().getUserCurrentProfile().isFeatureEnabled(IProfile.FEATURE_REFERENCE_SELF)) {
					citationField.add(fp);
					identifiersField.add(new Label("<none>"));
				}
			}
			_domeo.getLogger().debug(this, "2: "+_resource.getUrl());
			uriField.setHTML("<a target='_blank' href='" + _resource.getUrl() + "'/>" + _resource.getUrl() + "</a>");
			
			if(_domeo.getProfileManager().getUserCurrentProfile().isFeatureEnabled(IProfile.FEATURE_REFERENCE_SELF)) {
				uriField2.setHTML("<a target='_blank' href='" + _resource.getUrl() + "'/>" + _resource.getUrl() + "</a>");
			}
			
			_domeo.getLogger().debug(this, "3: "+_resource.getLabel());
			if(_resource.getLabel()!=null && _resource.getLabel().trim().length()>0) labelField.setValue(_resource.getLabel());
			else labelField.setValue("<none>");
			
			_domeo.getLogger().debug(this, "4: "+_resource.getDescription());
			if(_resource.getDescription()!=null && _resource.getDescription().trim().length()>0) descriptionField.setValue(_resource.getDescription());
			else descriptionField.setValue("<none>");
			
			if(_resource.getKeywords()!=null) keywordsField.setValue(Arrays.toString(_resource.getKeywords()));
			else keywordsField.setValue("<none>");
			
			// PubMed bibliography panel
			// -------------------------
			//boolean isBibliographicSetEmpty = ((IReferences)_domeo.getPersistenceManager().getCurrentResource()).getReferences().size()==0;
			boolean isBibliographicSetEmpty = true;
			_domeo.getLogger().debug(this, "5");
			boolean isBibliographicSetVirtual = _domeo.getAnnotationPersistenceManager().getBibliographicSet().isVirtual();
			_domeo.getLogger().debug(this, "6");
			
			if(_domeo.getProfileManager().getUserCurrentProfile().isFeatureEnabled(IProfile.FEATURE_REFERENCE_SELF)) {
				referencesSidePanelTopbar.refresh(isBibliographicSetEmpty, isBibliographicSetVirtual);
				try {
					_domeo.getLogger().debug(this, "7");
					CitationReferencesPanel p = new CitationReferencesPanel(_domeo, isBibliographicSetVirtual);
					referencesPanel.clear();
					referencesPanel.add(p);
				} catch(Exception e) {
					_domeo.getLogger().exception(this, "Exception while rendering resource info " + e.getMessage());
					referencesPanel.clear();
					referencesPanel.add(new HTML("<img src='" + _resources.crossLittleIcon().getSafeUri().asString() + "'/> Exception while rendering PubMed resource info " + e.getMessage()));
				}
				if(_resource.getLabel()!=null && _resource.getLabel().trim().length()>0) titleField.setText(_resource.getLabel());
				else titleField.setText("<none>");
			}
			
			if(_domeo.getProfileManager().getUserCurrentProfile().isFeatureEnabled(IProfile.FEATURE_QUALIFIERS_SELF)) {
				ClickHandler addQualifierClickHandler = new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						_domeo.getLogger().command(this, "Open panel for adding qualifiers"); 
						QualifiersSearchPanel afp = new QualifiersSearchPanel(_domeo);
						new EnhancedGlassPanel(_domeo, afp, afp.getTitle(), false, false, false);
					}
				};
				Image addCitationIcon = new Image(Domeo.resources.addLittleIcon());
				addCitationIcon.addClickHandler(addQualifierClickHandler);
				Label addCitationLabel = new Label("Add qualifier");
				addCitationLabel.addClickHandler(addQualifierClickHandler);
				HorizontalPanel fp = new HorizontalPanel();
				fp.add(addCitationIcon);
				fp.add(addCitationLabel);
				qualifiersToolbarPanel.clear();
				qualifiersToolbarPanel.add(fp);
			}
			
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

	@Override
	public void documentResourceStarred() {
		starImage.setResource(Domeo.resources.starHotIcon());
		starImage.setTitle("Starring a document will list it in your bibliography");
		starLabel.setText("Unstar");
		if(_starImageHandler!=null)  _starImageHandler.removeHandler();
		if(_starLabelHandler!=null)  _starLabelHandler.removeHandler();
		_starImageHandler = starImage.addClickHandler(unstarAction);
		_starLabelHandler = starLabel.addClickHandler(unstarAction);
	}

	@Override
	public void documentResourceUnstarred() {
		starImage.setResource(Domeo.resources.starColdIcon());
		starImage.setTitle("Starring a document will list it in your bibliography");
		starLabel.setText("Star");
		if(_starImageHandler!=null) _starImageHandler.removeHandler();
		if(_starLabelHandler!=null) _starLabelHandler.removeHandler();
		_starImageHandler = starImage.addClickHandler(starAction);
		_starLabelHandler = starLabel.addClickHandler(starAction);
	}

	@Override
	public void documentResourceStarred(boolean starred) {
		_domeo.getLogger().info(this, "is starred " + starred);
		if(starred) {
			documentResourceStarred();
		} else {
			documentResourceUnstarred();
		}
	}
}
