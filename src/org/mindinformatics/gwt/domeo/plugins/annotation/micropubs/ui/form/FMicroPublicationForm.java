/*
 * Copyright 2013 Massachusetts General Hospital
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.form;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.text.ATextFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.text.TextAnnotationFormsPanel;
import org.mindinformatics.gwt.domeo.component.bibliography.ui.listpicker.IReferencesListPickerContainer;
import org.mindinformatics.gwt.domeo.component.bibliography.ui.listpicker.ReferencesListPickerWidget;
import org.mindinformatics.gwt.domeo.component.cache.images.model.ImageProxy;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.listpicker.IImagesListPickerContainer;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.listpicker.ImagesListPickerWidget;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.model.MOnlineImage;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.model.selectors.MImageInDocumentSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublication;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpData;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpReference;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpRelationship;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MicroPublicationFactory;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MicroPublicationsResources;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.ISelectionProvider;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.lenses.PubMedCitationPainter;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model.MPubMedDocument;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search.IPubmedSearchObjectContainer;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search.PubmedSearchWidget;
import org.mindinformatics.gwt.framework.model.references.ISelfReference;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.src.IResizable;
import org.mindinformatics.gwt.framework.widget.ButtonWithIcon;
import org.mindinformatics.gwt.framework.widget.WidgetUtilsResources;
import org.mindinformatics.gwt.utils.src.HtmlUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class FMicroPublicationForm extends AFormComponent implements IResizable, ISelectionProvider, 
	IPubmedSearchObjectContainer, IImagesListPickerContainer, IReferencesListPickerContainer, IEvidenceRelationshipChangeListener {

	public static final String LABEL = "Micro Publication";
	public static final String LABEL_EDIT = "Edit Micro Publication";
	
	public static final String LOG_CATEGORY_QUALIFIER_CREATE = "CREATING MICROPUB";
	public static final String LOG_CATEGORY_QUALIFIER_EDIT = "EDITING MICROPUB";
	
	interface Binder extends UiBinder<VerticalPanel, FMicroPublicationForm> { }
	private static final Binder binder = GWT.create(Binder.class);

	public static final WidgetUtilsResources widgetUtilsResources = 
			GWT.create(WidgetUtilsResources.class);
	
	public static final MicroPublicationsResources localResources = GWT.create(MicroPublicationsResources.class);
	
	interface LocalCss extends CssResource {
		String link();
		String indexOdd();
		String indexEven();
		String indexWrapper();
		String imageWrap();
		String centerText();
	}
	
	@UiField LocalCss style;
	
	//private MAntibodyAnnotation _item;
	//private MAntibody currentAntibody;
	
	private ArrayList<Widget> tabs = new ArrayList<Widget>();
	
	@UiField VerticalPanel container;
	//@UiField FlowPanel newQualifiers;
	@UiField HorizontalPanel buttonsPanel;
	@UiField ListBox annotationSet;
	@UiField VerticalPanel rightColumn;
	@UiField TabBar tabBar;
	@UiField ScrollPanel supportPanel;
	
	//@UiField Image addDataImage;
	
	@UiField TextArea statementBody;
	
	@UiField TabLayoutPanel tabPanel;
	@UiField TabLayoutPanel evidenceTabs;
	
	@UiField VerticalPanel referencePanel;
	
	private MMicroPublication _item;
	private FMicroPublicationForm _this;
	private ImagesListPickerWidget imagesListPickerWidget;
	private ReferencesListPickerWidget referencesListPickerWidget;
	
	//private ArrayList<ImageProxy> images = new ArrayList<ImageProxy>();
	//private ArrayList<MPublicationArticleReference> references = new ArrayList<MPublicationArticleReference>();
	
	private ArrayList<MMpRelationship> evidence = new ArrayList<MMpRelationship>();
	
	//private AntibodiesSearchWidget antibodiesSearchWidget;

	public FMicroPublicationForm(IDomeo domeo, final AFormsManager manager, boolean inFolders) {
		super(domeo);
		_manager = manager;
		_this = this;
		
		initWidget(binder.createAndBindUi(this));
	
		refreshAnnotationSetFilter(annotationSet, null);
		statementBody.setText(getTextContent());

		ButtonWithIcon yesButton = new ButtonWithIcon(Domeo.resources.generalCss().applyButton());
		yesButton.setWidth("78px");
		yesButton.setHeight("26px");
		yesButton.setResource(Domeo.resources.acceptLittleIcon());
		yesButton.setText("Apply");
		yesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(_item == null) {
					if(_manager instanceof TextAnnotationFormsPanel) {
						MTextQuoteSelector selector = AnnotationFactory.createPrefixSuffixTextSelector(
							_domeo.getAgentManager().getUserPerson(), 
							_domeo.getPersistenceManager().getCurrentResource(), ((TextAnnotationFormsPanel)_manager).getHighlight().getExact(), 
							((TextAnnotationFormsPanel)_manager).getHighlight().getPrefix(), ((TextAnnotationFormsPanel)_manager).getHighlight().getSuffix());
						
						MMicroPublication micropublication = MicroPublicationFactory.createMicroPublication((MTextQuoteSelector) selector);
						
						MMicroPublicationAnnotation annotation = MicroPublicationFactory.createMicroPublicationAnnotation(
							((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getCurrentSet(), 
							_domeo.getAgentManager().getUserPerson(), 
							_domeo.getAgentManager().getSoftware(),
							_manager.getResource(), selector, 
							micropublication);
						
						if(getSelectedSet(annotationSet)==null) {
							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, true);
						} else {
							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, getSelectedSet(annotationSet));
						}
						_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(annotation, ((TextAnnotationFormsPanel)_manager).getHighlight());
						_manager.hideContainer();
					}
				}
			}
		});
		buttonsPanel.add(yesButton);
		
		this.setHeight("100px");
		
		imagesListPickerWidget = new ImagesListPickerWidget(_domeo, this, false);
		tabs.add(imagesListPickerWidget);
		tabBar.addTab("Images");
		
		if(_domeo.getPersistenceManager().getCurrentResource() instanceof MPubMedDocument) {
			referencesListPickerWidget = new ReferencesListPickerWidget(_domeo, this, false);
			tabs.add(referencesListPickerWidget);
			tabBar.addTab("References");
		}
		
		PubmedSearchWidget pubmedSearchWidget = new PubmedSearchWidget(_domeo, this, false);
		tabs.add(pubmedSearchWidget);
		tabBar.addTab("PubMed Search");
		
		tabBar.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				rightColumn.clear();
				rightColumn.add(tabs.get(event.getSelectedItem()));
			}
	    });
		
		rightColumn.add(tabs.get(0));
		
		if(_domeo.getPersistenceManager().getCurrentResource() instanceof ISelfReference && ((ISelfReference)_domeo.getPersistenceManager().getCurrentResource()).getSelfReference()!=null) {
			referencePanel.add(PubMedCitationPainter.getCitation((MPublicationArticleReference)(((ISelfReference)_domeo.getPersistenceManager().getCurrentResource()).getSelfReference()).getReference()));
		}
		resized();
	}
	
//	// ------------------------
//	//  EDITING OF ANNOTATION
//	// ------------------------
	public FMicroPublicationForm(IDomeo domeo, final AFormsManager manager, final MMicroPublicationAnnotation annotation) {
		super(domeo);
		_manager = manager;
//		_item = annotation;
//		
//		initWidget(binder.createAndBindUi(this));
//		
//		try {
//			if(_item.getComment()!=null) statementBody.setText(_item.getComment());
//			refreshAnnotationSetFilter(annotationSet, annotation);
//			
//			
//		} catch(Exception e) {
//			_domeo.getLogger().exception(this, "Failed to display current annotation " + annotation.getLocalId());
//			displayDialog("Failed to properly display existing annotation " + e.getMessage(), true);
//		}
//		
////		try {
////			refreshAnnotationSetFilter(annotationSet, annotation);
////			currentAntibody = annotation.getAntibodyUsage().getAntibody();
////		} catch(Exception e) {
////			_domeo.getLogger().exception(AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION, this, "Failed to display current annotation " + annotation.getLocalId());
////			displayDialog("Failed to properly display existing annotation " + e.getMessage(), true);
////		}
//		
//		ButtonWithIcon sameVersionButton = new ButtonWithIcon();
//		sameVersionButton.setStyleName(Domeo.resources.generalCss().applyButton());
//		sameVersionButton.setWidth("78px");
//		sameVersionButton.setHeight("22px");
//		sameVersionButton.setResource(Domeo.resources.acceptLittleIcon());
//		sameVersionButton.setText("Apply");
//		sameVersionButton.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				try {
//					if(isContentInvalid()) return;
//					if(!isContentChanged(_item)) {
//						_domeo.getLogger().debug(this, "No changes to save for annotation " + _item.getLocalId());
//						_manager.getContainer().hide();
//						return;
//					}
//					_item.setComment(statementBody.getText());
////					_item.getProtocols().clear();
////					_item.getAntibodyUsage().setAntibody(currentAntibody);
//					
//					_domeo.getContentPanel().getAnnotationFrameWrapper().updateAnnotation(_item, getSelectedSet(annotationSet));
//					_manager.hideContainer();
//				} catch(Exception e) {
//					_domeo.getLogger().exception(AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION, this, "Failed to apply modified anntoation " + annotation.getLocalId());
//					displayDialog("Failed to apply modified annotation " + e.getMessage(), true);
//				}
//			}
//		});
//		buttonsPanel.add(sameVersionButton);
//
//		this.setHeight("100px");
//		/*
//		antibodiesSearchWidget = new AntibodiesSearchWidget(_domeo, this, Domeo.resources, false);
//		// TODO If border is needed, there is a need for a wrapper. SearchTermWidget is one
//		//AntibodiesSelectionList termsList = new AntibodiesSelectionList(_domeo, this, getAntibodiesList(), new HashMap<String, MLinkedDataResource>(0));
//		tabs.add(antibodiesSearchWidget);
//		//tabs.add(termsList);		
//		 * 
//		 */
//		
//		tabBar.addTab("Search for Antibodies");
//		tabBar.addTab("Recently Used");
//		tabBar.addSelectionHandler(new SelectionHandler<Integer>() {
//			public void onSelection(SelectionEvent<Integer> event) {
//				rightColumn.clear();
//				rightColumn.add(tabs.get(event.getSelectedItem()));
//			}
//	    });
//		
//		rightColumn.add(tabs.get(0));
//		//refreshAssociatedAntibodies();
		resized();
	}
	
	public String getTitle() {
		return LABEL;
	}

	@Override
	public String getLogCategoryCreate() {
		return LOG_CATEGORY_QUALIFIER_CREATE;
	}

	@Override
	public String getLogCategoryEdit() {
		return LOG_CATEGORY_QUALIFIER_EDIT;
	}

	@Override
	public boolean isContentInvalid() {
//		if(currentAntibody==null) {
//			_manager.displayMessage("The body of the annotation cannot be empty!");
//			Timer timer = new Timer() {
//				public void run() {
//					_manager.clearMessage();
//				}
//			};
//			timer.schedule(2000);
//			return true;
//		}
		return false;
	}

	@Override
	public boolean isContentChanged(MAnnotation annotation) {
		// TODO just checking the size is not right.
//		if(_item!=null) {
//			if(!_item.getAntibodyUsage().getAntibody().getUrl().equals(currentAntibody.getUrl())
//					|| !statementBody.getText().equals(_item.getAntibodyUsage().getComment())) {
//				return true;
//			}
//		}	
		return false;
	}

//	@Override
//	public void resized() {
//		container.setWidth("100%");
//	}
	
	@Override
	public void resized() {
		this.setWidth((Window.getClientWidth() - 340) + "px");
		tabBar.setWidth((Window.getClientWidth() - 615) + "px");
		for(Widget tab:tabs) {
			//if(tab instanceof IResizable) ((IResizable)tab).resized();
			tab.setWidth((Window.getClientWidth() - 615) + "px");
		}
		
		evidenceTabs.setHeight((Window.getClientHeight() - 490) + "px");
		supportPanel.setHeight((Window.getClientHeight() - 520) + "px");
		rightColumn.setHeight((Window.getClientHeight() - 340) + "px");
	}
	
	@Override
	public String getTextContent() {
		// TODO Auto-generated method stub
		if(_manager instanceof ATextFormsManager)
			return ((ATextFormsManager)_manager).getHighlight().getExact();
		return "";
	}
	
	public void refreshSupport() {
		supportPanel.clear();
		
		Integer counter = 0;
		VerticalPanel vp = new VerticalPanel();
		
		for(MMpRelationship ev: evidence) {
			if(ev.getObjectElement() instanceof MMpData) {
				if(ev.getObjectElement().getSelector() instanceof MImageInDocumentSelector) {
					//String imgUrl = ((MImageInDocumentSelector)ev.getObjectElement().getSelector()).getTarget().getUrl();
					//Window.alert(imgUrl);
					displayImageInEvidence(vp, counter++, ev);
				}
			} else if(ev.getObjectElement() instanceof MMpReference) {
				displayReferenceInEvidence(vp, counter++, ev);
			}
		}
		
		/*
		for(MPublicationArticleReference reference: references) {
			HorizontalPanel hp1 = new HorizontalPanel();
			hp1.add(PubMedCitationPainter.getFullCitation(reference, _domeo));
			
			//final Button box = new Button("Remove");
			final Image removeIcon = new Image(Domeo.resources.deleteLittleIcon());
			removeIcon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					//box.setEnabled(false);
					//_container.addImageAsData(_image);
				}
			});					
			hp1.add(removeIcon);
			
			if(counter%2 == 1) {
				hp1.addStyleName(style.indexOdd());
			} else {
				hp1.addStyleName(style.indexEven());
			}
			counter++;
			vp.add(hp1);
		}
		*/
		
		
		supportPanel.add(vp);
	}

	@Override
	public void addImageAsData(ImageProxy image) {		
		MOnlineImage _imageResource = new MOnlineImage();
		_imageResource.setUrl(image.getOriginalUrl());
		_imageResource.setDisplayUrl(image.getDisplayUrl());
		_imageResource.setLocalId(((Element) image.getImage()).getAttribute("imageid"));
		_imageResource.setLabel(image.getTitle());
		_imageResource.setImage((Element) image.getImage());
		_imageResource.setXPath(HtmlUtils.getElementXPath((Element) image.getImage()));
		
		MImageInDocumentSelector imgSelector = AnnotationFactory.createImageSelector(_domeo, _domeo.getAgentManager().getUserPerson(), _imageResource);
		MMpData imageData = new MMpData(imgSelector);
		
		MMpRelationship suportedBy = MicroPublicationFactory.createMicroPublicationRelationship(_domeo.getAgentManager().getUserPerson(), imageData, IMicroPublicationsOntology.supportedBy);
		evidence.add(suportedBy);	
		refreshSupport();
	}

	@Override
	public void addBibliographicObject(MPublicationArticleReference reference) {
		//references.add(reference);
		MMpReference referenceData = new MMpReference();
		referenceData.setReference(reference);
		
		MMpRelationship suportedBy = MicroPublicationFactory.createMicroPublicationRelationship(_domeo.getAgentManager().getUserPerson(), referenceData, IMicroPublicationsOntology.supportedBy);
		
		evidence.add(suportedBy);
		refreshSupport();
	}

	@Override
	public ArrayList<MPublicationArticleReference> getBibliographicObjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MAnnotationReference> getBibliographicObjectAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void displayReferenceInEvidence(VerticalPanel vp, Integer counter, final MMpRelationship relationship) {
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.setWidth("442px");
		hp1.setStyleName(style.indexWrapper());
	
		if(relationship.getName().equals(IMicroPublicationsOntology.supportedBy)) {
			final Image supportedByIcon = new Image(localResources.supportedBy());
			supportedByIcon.setTitle("Supported By");
			supportedByIcon.setStyleName(style.link());
			supportedByIcon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					EvidenceRelationshipBubble bubble = new EvidenceRelationshipBubble(
							_this, _domeo, _item, relationship, "");
					int x = supportedByIcon.getAbsoluteLeft();
					int y = supportedByIcon.getAbsoluteTop();
					bubble.show(x, y);
				}
			});					
			hp1.add(supportedByIcon);
			hp1.setCellWidth(supportedByIcon, "22px");
		} else if(relationship.getName().equals(IMicroPublicationsOntology.challengedBy)) {
			final Image supportedByIcon = new Image(localResources.challengedBy());
			supportedByIcon.setTitle("Supported By");
			supportedByIcon.setStyleName(style.link());
			supportedByIcon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					EvidenceRelationshipBubble bubble = new EvidenceRelationshipBubble(
							_this, _domeo, _item, relationship, "");
					int x = supportedByIcon.getAbsoluteLeft();
					int y = supportedByIcon.getAbsoluteTop();
					bubble.show(x, y);
				}
			});					
			hp1.add(supportedByIcon);
			hp1.setCellWidth(supportedByIcon, "22px");
		}
		
		hp1.add(PubMedCitationPainter.getFullCitation(((MMpReference)relationship.getObjectElement()).getReference(), _domeo));
		
		final Image removeIcon = new Image(Domeo.resources.deleteLittleIcon());
		removeIcon.setStyleName(style.link());
		removeIcon.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//box.setEnabled(false);
				//_container.addImageAsData(_image);
			}
		});					
		hp1.add(removeIcon);
		hp1.setCellWidth(removeIcon, "16px");
		hp1.setCellHorizontalAlignment(removeIcon, HasHorizontalAlignment.ALIGN_RIGHT);
		
		vp.add(hp1);
	}
	
	private void displayImageInEvidence(VerticalPanel vp, Integer counter, final MMpRelationship relationship) {

		MOnlineImage image = (MOnlineImage)((MImageInDocumentSelector)relationship.getObjectElement().getSelector()).getTarget();
		VerticalPanel hp1 = new VerticalPanel();
		hp1.setWidth("442px");
		
		boolean small = false;
		boolean reduced = false;
		Image img = new Image(image.getDisplayUrl());
		if(img.getWidth()>380) {
			img.setWidth("380px");  
			reduced = true;
		} else if(img.getWidth()<220) {
			small = true;
			//img.setWidth("200px"); 
		}
		if(image.getLabel()!=null && image.getLabel().trim().length()>0) {
			img.setTitle(image.getLabel());
		}
		
		if(!small) {
			
			HorizontalPanel main = new HorizontalPanel();
			main.setWidth("442px");
			
			//VerticalPanel actionsPanel = new VerticalPanel();
			//actionsPanel.setHeight("100%");
			
			if(relationship.getName().equals(IMicroPublicationsOntology.supportedBy)) {
				final Image supportedByIcon = new Image(localResources.supportedBy());
				supportedByIcon.setTitle("Supported By");
				supportedByIcon.setStyleName(style.link());
				supportedByIcon.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						EvidenceRelationshipBubble bubble = new EvidenceRelationshipBubble(
								_this, _domeo, _item, relationship, "");
						int x = supportedByIcon.getAbsoluteLeft();
						int y = supportedByIcon.getAbsoluteTop();
						bubble.show(x, y);
					}
				});					
				main.add(supportedByIcon);
				main.setCellWidth(supportedByIcon, "22px");
			} else if(relationship.getName().equals(IMicroPublicationsOntology.challengedBy)) {
				final Image supportedByIcon = new Image(localResources.challengedBy());
				supportedByIcon.setTitle("Supported By");
				supportedByIcon.setStyleName(style.link());
				supportedByIcon.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						EvidenceRelationshipBubble bubble = new EvidenceRelationshipBubble(
								_this, _domeo, _item, relationship, "");
						int x = supportedByIcon.getAbsoluteLeft();
						int y = supportedByIcon.getAbsoluteTop();
						bubble.show(x, y);
					}
				});					
				main.add(supportedByIcon);
				main.setCellWidth(supportedByIcon, "22px");
			}
			
			
			VerticalPanel left = new VerticalPanel();
			
			SimplePanel imageWrap = new SimplePanel();
			imageWrap.setStyleName(style.imageWrap());
			imageWrap.add(img);
			imageWrap.setStyleName(style.centerText());
			left.add(imageWrap);
			left.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_LEFT);
			if(image.getLabel()!=null && image.getLabel().trim().length()>0) {
				HTML title = new HTML("<b>"+image.getLabel()+"</b>");
				left.add(title);
				left.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_LEFT);
			} /* else {
				HTML title = new HTML("<b>Title</b>");
				left.add(title);
				left.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_LEFT);
			} */
			main.add(left);	
			main.setStyleName(style.indexWrapper());
			
			if(counter%2 == 1) {
				left.addStyleName(style.indexOdd());
			} else {
				left.addStyleName(style.indexEven());
			}
			
			final Image removeIcon = new Image(Domeo.resources.deleteLittleIcon());
			removeIcon.setStyleName(style.link());
			removeIcon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					//box.setEnabled(false);
					//_container.addImageAsData(_image);
				}
			});					
			main.add(removeIcon);
			main.setCellWidth(removeIcon, "16px");
			main.setCellHorizontalAlignment(removeIcon, HasHorizontalAlignment.ALIGN_RIGHT);
			
			
			hp1.add(main);	
		} else {
			HorizontalPanel main = new HorizontalPanel();
			main.setStyleName(style.indexWrapper());
			
			final Image supportedByIcon = new Image(localResources.supportedBy());
			supportedByIcon.setTitle("Supported By");
			supportedByIcon.setStyleName(style.link());
			supportedByIcon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					EvidenceRelationshipBubble bubble = new EvidenceRelationshipBubble(
							_this, _domeo, _item, relationship, "");
					int x = supportedByIcon.getAbsoluteLeft();
					int y = supportedByIcon.getAbsoluteTop();
					bubble.show(x, y);
				}
			});					
			main.add(supportedByIcon);
			main.setCellWidth(supportedByIcon, "22px");
			
			SimplePanel imageWrap = new SimplePanel();
			imageWrap.setStyleName(style.imageWrap());
			imageWrap.add(img);
			main.add(imageWrap);
			
			//hp1.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
			
			VerticalPanel right = new VerticalPanel();
			right.setWidth("100%");
			
			if(image.getLabel()!=null && image.getLabel().trim().length()>0) {
				HTML title = new HTML("<b>"+image.getLabel()+"</b>");
				right.add(title);
				right.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_LEFT);
			} /*else {					
				HTML title = new HTML("title: <b>Title</b>");
				right.add(title);
				right.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_LEFT);
			}*/
			
			main.add(right);
			main.setCellWidth(right, "100%");
			
			final Image removeIcon = new Image(Domeo.resources.deleteLittleIcon());
			removeIcon.setStyleName(style.link());
			removeIcon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					//box.setEnabled(false);
					//_container.addImageAsData(_image);
				}
			});					
			main.add(removeIcon);
			main.setCellWidth(removeIcon, "16px");
			main.setCellHorizontalAlignment(removeIcon, HasHorizontalAlignment.ALIGN_RIGHT);
			
			hp1.add(main);	

			if(counter%2 == 1) {
				right.addStyleName(style.indexOdd());
			} else {
				right.addStyleName(style.indexEven());
			}
		}				
		vp.add(hp1);
	
	}

	@Override
	public void updateEvidence(MMicroPublication element, MMpRelationship evidence, String originalType, String newType) {
		
		//ArrayList<MMpRelationship> evidences = element.getEvidence();
		//for(MMpRelationship ev: evidences) {
		//	if()
		//}
		evidence.setName(newType);
		
		
		// TODO Auto-generated method stub
		//MImageInDocumentSelector imgSelector = AnnotationFactory.createImageSelector(_domeo, _domeo.getAgentManager().getUserPerson(), evidence.getObjectElement());
		//MMpData imageData = new MMpData(imgSelector);
		//MMpRelationship suportedBy = new MMpRelationship(imageData, IMicroPublicationsOntology.supportedBy);
		//evidence.add(suportedBy);
		//images.add(image);	
		refreshSupport();
	}
}
