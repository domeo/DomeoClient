package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.lenses;

import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.client.ui.east.resource.ReferencesSidePanelTopbar;
import org.mindinformatics.gwt.domeo.client.ui.east.resource.ResourceSidePanelTopbar;
import org.mindinformatics.gwt.domeo.client.ui.east.resource.references.CitationReferencesPanel;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.CachedImagesPanel;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.ICachedImages;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.BibliographyManager;
import org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector.IStarringRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.document.lenses.LSlimDocumentResourceCardPanel;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;
import org.mindinformatics.gwt.domeo.plugins.resource.omim.model.MOmimDocument;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model.MPubMedDocument;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search.PubmedReferencesSearchPanel;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.profiles.model.IProfile;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.ui.glass.EnhancedGlassPanel;
import org.mindinformatics.gwt.framework.component.ui.lenses.resources.IResourceLensComponent;
import org.mindinformatics.gwt.framework.model.references.IReferences;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.widget.EditableLabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class LPubMedDocumentCardPanel extends Composite implements IRefreshableComponent, 
	IResourceLensComponent, ICachedImages, IStarringRequestCompleted {
	
	public static final String KNOWN_RESOURCE = "KNOWN RESOURCE";
	
	interface Binder extends UiBinder<Widget, LPubMedDocumentCardPanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	interface LocalCss extends CssResource {
		String tabLayoutPanel();
	}
	
	@UiField LocalCss style;
	
	@UiField VerticalPanel myBibilographyToolbarPanel;
	@UiField Image starImage;
	@UiField Label starLabel;
	@UiField Image recommendImage;
	@UiField Label recommendLabel;
	
	@UiField FlowPanel body;
	@UiField Image urlImage;
	@UiField EditableLabel uriField;
	//@UiField FlowPanel sourceField;
	//@UiField FlowPanel aboutField;
	@UiField FlowPanel citationField;

	@UiField HTML extractionSourceDetails;
	@UiField Label extractionDateDetails;
	@UiField Label extractorProvenanceDetails;
	@UiField Image extractionProvenanceImage;
	
	@UiField FlowPanel identifiersField;
	@UiField ScrollPanel referencesPanel;
	@UiField TabLayoutPanel tabToolsPanel;
	
	@UiField SimplePanel contentPanel;
	@UiField VerticalPanel bibliographyToolbarPanel;
	@UiField VerticalPanel generalToolbarPanel;
	
	@UiField ScrollPanel imagesPanel;
	CachedImagesPanel p;
	
	HandlerRegistration _starImageHandler;
	HandlerRegistration _starLabelHandler;
	
	private ReferencesSidePanelTopbar referencesSidePanelTopbar;
	private ClickHandler starAction;
	private ClickHandler unstarAction;
	
	// By contract 
	private IDomeo _domeo;
	private MPubMedDocument _resource;
	private HandlerRegistration _iconClickHandler;
	private HashMap<String, String> _parameters;
	
	public LPubMedDocumentCardPanel(IDomeo domeo) {
		_domeo = domeo;
		
		initWidget(binder.createAndBindUi(this));
		body.setHeight("100%");
		tabToolsPanel.setHeight(Window.getClientHeight()-45 + "px");
		tabToolsPanel.addStyleName(style.tabLayoutPanel());
		urlImage.setVisible(false);
	}
	
	public void initializeLens(MGenericResource resource, HashMap<String, String> parameters) {
		Resources _resources = Domeo.resources;
		try {
			_resource = (MPubMedDocument) resource;
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
			
			generalToolbarPanel.add(new ResourceSidePanelTopbar(_domeo, "Generic Resource Information"));
			bibliographyToolbarPanel.clear();
			referencesSidePanelTopbar = new ReferencesSidePanelTopbar(_domeo);
			bibliographyToolbarPanel.add(referencesSidePanelTopbar);
			bibliographyToolbarPanel.setCellWidth(referencesSidePanelTopbar, "100%");
			
			int scrollerHeight = Window.getClientHeight()-100;
			referencesPanel.setHeight("" + scrollerHeight + "px"); 
		
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
			
			if(_domeo.getProfileManager().getUserCurrentProfile().isFeatureEnabled(IProfile.FEATURE_MY_BIBLIOGRAPHY)) {
				starImage.setResource(Domeo.resources.starColdIcon());
				starImage.setTitle("Starring a document will list it in your bibliography");
				_starImageHandler = starImage.addClickHandler(starAction);
				_starLabelHandler = starLabel.addClickHandler(starAction);
			} else {
				myBibilographyToolbarPanel.setVisible(false);
			}
			
			if(_domeo.getProfileManager().getUserCurrentProfile().isFeatureEnabled(IProfile.FEATURE_MY_RECOMMENDATIONS)) {
				recommendImage.setResource(Domeo.resources.shareDocumentIcon());
			} else {
				
			}
			
			refresh();
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while initializing resource info " + e.getMessage());
			body.clear();
			body.add(new HTML("<img src='" + _resources.crossLittleIcon().getSafeUri().asString() + "'/> Exception while initializing PubMed resource info " + e.getMessage()));
		}
	}
	
	private void init() {
		//sourceField.clear();
		citationField.clear();
		identifiersField.clear();
		generalToolbarPanel.clear();
		generalToolbarPanel.add(new ResourceSidePanelTopbar(_domeo, "Generic Resource Information"));
		//bibliographyToolbarPanel.clear();
	}
	
	public void refresh() {
		Resources _resources = Domeo.resources;
		
		boolean isBibliographicSetEmpty = ((IReferences)_domeo.getPersistenceManager().getCurrentResource()).getReferences().size()==0;
		boolean isBibliographicSetVirtual = _domeo.getAnnotationPersistenceManager().getBibliographicSet().isVirtual();
		
		//try {
			init();
			uriField.setValue(_resource.getUrl());
		
			// PubMed reference panel
			// ----------------------
			if(_resource.getSelfReference()!=null) {
				extractionProvenanceImage.setUrl(Domeo.resources.pluginsGrayLittleIcon().getSafeUri().asString());
				extractionProvenanceImage.setTitle(_resource.getCreator().getName());
				extractorProvenanceDetails.setText("Extracted by " +  _resource.getCreator().getName());
				extractionSourceDetails.setHTML("Source <a href='" + 
						_resource.getSource().getHomepage() + "' target='_blank'> <img src='" + Domeo.resources.externalLinkIcon().getSafeUri().asString() + "'/> "+_resource.getSource().getName()+ "</a>");
				extractionDateDetails.setText("On " + _resource.getFormattedCreationDate());
				citationField.add(PubMedCitationPainter.getCitation((MPublicationArticleReference)_resource.getSelfReference().getReference()));
				identifiersField.add(PubMedCitationPainter.getIdentifiers((MPublicationArticleReference)_resource.getSelfReference().getReference(), _domeo));
			} else {
				ClickHandler addCitationClickHandler = new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						_domeo.getLogger().command(this, "Open panel for adding a citation"); 
						PubmedReferencesSearchPanel afp = new PubmedReferencesSearchPanel(_domeo, -1);
						new EnhancedGlassPanel(_domeo, afp, afp.getTitle(), false, false, false);
						Window.alert("addCitation not implemented yet");
					}
				};
				Image addCitationIcon = new Image(Domeo.resources.addLittleIcon());
				addCitationIcon.addClickHandler(addCitationClickHandler);
				Label addCitationLabel = new Label("Add citation");
				addCitationLabel.addClickHandler(addCitationClickHandler);
				HorizontalPanel fp = new HorizontalPanel();
				fp.add(addCitationIcon);
				fp.add(addCitationLabel);
				citationField.add(fp);
				identifiersField.add(new Label("<none>"));
			}
			if(_domeo.getExtractorsManager().isExtractorDefined()) {
				urlImage.setUrl(_resources.checkIcon().getSafeUri().asString());
				urlImage.setTitle("Known resource, loaded extractor '" + _domeo.getExtractorsManager().getExtractorLabel() +"'");
				urlImage.setVisible(true);
			} else {
				urlImage.setVisible(false);
			}
//		} catch(Exception e) {
//			_domeo.getLogger().exception(this, "Exception while rendering PubMed resource info " + e.getMessage());
//			body.clear();
//			body.add(new HTML("<img src='" + _resources.crossLittleIcon().getSafeUri().asString() + "'/> Exception while rendering PubMed resource info " + e.getMessage()));
//		}
		
		// PubMed bibliography panel
		// -------------------------
		referencesSidePanelTopbar.refresh(isBibliographicSetEmpty, isBibliographicSetVirtual);
		try {
			CitationReferencesPanel p = new CitationReferencesPanel(_domeo, isBibliographicSetVirtual);
			referencesPanel.clear();
			referencesPanel.add(p);
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while rendering resource info " + e.getMessage());
			referencesPanel.clear();
			referencesPanel.add(new HTML("<img src='" + _resources.crossLittleIcon().getSafeUri().asString() + "'/> Exception while rendering PubMed resource info " + e.getMessage()));
		}
		
		// Images
		// ------
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

		// Metadata panel
		// --------------
		try {
			LSlimDocumentResourceCardPanel d = new LSlimDocumentResourceCardPanel(_domeo);
			d.initializeLens(_resource, _parameters);
			contentPanel.clear();
			contentPanel.add(d);
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while rendering resource info " + e.getMessage());
			contentPanel.clear();
			contentPanel.add(new HTML("<img src='" + _resources.crossLittleIcon().getSafeUri().asString() + "'/> Exception while rendering PubMed resource info " + e.getMessage()));
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
		_starImageHandler.removeHandler();
		_starLabelHandler.removeHandler();
		_starImageHandler = starImage.addClickHandler(unstarAction);
		_starLabelHandler = starLabel.addClickHandler(unstarAction);
	}

	@Override
	public void documentResourceUnstarred() {
		starImage.setResource(Domeo.resources.starColdIcon());
		starImage.setTitle("Starring a document will list it in your bibliography");
		starLabel.setText("Star");
		_starImageHandler.removeHandler();
		_starLabelHandler.removeHandler();
		_starImageHandler = starImage.addClickHandler(starAction);
		_starLabelHandler = starLabel.addClickHandler(starAction);
	}
}
