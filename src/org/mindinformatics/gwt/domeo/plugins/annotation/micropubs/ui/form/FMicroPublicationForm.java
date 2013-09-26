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
import java.util.List;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.text.ATextFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.text.TextAnnotationFormsPanel;
import org.mindinformatics.gwt.domeo.client.ui.content.AnnotationFrameWrapper;
import org.mindinformatics.gwt.domeo.component.bibliography.ui.listpicker.IReferencesListPickerContainer;
import org.mindinformatics.gwt.domeo.component.bibliography.ui.listpicker.ReferencesListPickerWidget;
import org.mindinformatics.gwt.domeo.component.cache.images.model.ImageProxy;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.listpicker.IImagesListPickerContainer;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.listpicker.ImagesListPickerWidget;
import org.mindinformatics.gwt.domeo.component.linkeddata.digesters.ITrustedResourceDigester;
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
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpDataImage;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpQualifier;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpReference;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpRelationship;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpStatement;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MicroPublicationFactory;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MicroPublicationsResources;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.ISelectionProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.picker.IStatementsSearchObjectContainer;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.picker.StatementsSearchWidget;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.search.terms.SearchTermsWidget;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.lenses.PubMedCitationPainter;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model.MPubMedDocument;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search.IPubmedSearchObjectContainer;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search.PubmedSearchWidget;
import org.mindinformatics.gwt.framework.component.qualifiers.ui.ISearchTermsContainer;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
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
import com.google.gwt.user.client.ui.RadioButton;
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
	IPubmedSearchObjectContainer, IImagesListPickerContainer, IReferencesListPickerContainer, IEvidenceRelationshipChangeListener, ISearchTermsContainer, IStatementsSearchObjectContainer {

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
	
	@UiField HorizontalPanel headerPanel;
	@UiField HorizontalPanel buttonsPanel;
	@UiField HorizontalPanel buttonsPanelSpacer;
	
	@UiField VerticalPanel container;
	//@UiField FlowPanel newQualifiers;
	@UiField ListBox annotationSet;
	@UiField VerticalPanel rightColumn;
	@UiField TabBar tabBar;
	@UiField ScrollPanel supportPanel;
	@UiField ScrollPanel qualifiersPanel;
	
	@UiField RadioButton radioClaim;
	@UiField RadioButton radioHypothesis;
	//@UiField Image addDataImage;
	
	@UiField TextArea statementBody;
	
	@UiField TabLayoutPanel tabPanel;
	@UiField TabLayoutPanel evidenceTabs;
	
	@UiField VerticalPanel referencePanel;
	
	private boolean hasChanged = false;
	private MMicroPublicationAnnotation _ann;
	private MMicroPublication _item;
	private FMicroPublicationForm _this;
	private ImagesListPickerWidget imagesListPickerWidget;
	private ReferencesListPickerWidget referencesListPickerWidget;
	
	//private ArrayList<ImageProxy> images = new ArrayList<ImageProxy>();
	//private ArrayList<MPublicationArticleReference> references = new ArrayList<MPublicationArticleReference>();
	
	private ArrayList<MMpRelationship> evidence = new ArrayList<MMpRelationship>();
	private ArrayList<MMpRelationship> qualifiers = new ArrayList<MMpRelationship>();
	
	//private AntibodiesSearchWidget antibodiesSearchWidget;

	public FMicroPublicationForm(IDomeo domeo, final AFormsManager manager, boolean inFolders) {
		super(domeo);
		_manager = manager;
		_this = this;
		
		initWidget(binder.createAndBindUi(this));
	
		refreshAnnotationSetFilter(annotationSet, null);
		radioClaim.setValue(true);
		statementBody.setText(getTextContent());

		ButtonWithIcon yesButton = new ButtonWithIcon(Domeo.resources.generalCss().applyButton());
		yesButton.setWidth("78px");
		yesButton.setHeight("22px");
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
						micropublication.setQualifiers(qualifiers);
						micropublication.setEvidence(evidence);				

						if(radioClaim.getValue()) {
							micropublication.setType(MMicroPublication.CLAIM);
						} else if(radioHypothesis.getValue()) {
							micropublication.setType(MMicroPublication.HYPOTHESIS);
						}
						
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
		
		SearchTermsWidget searchTermsWidget = new SearchTermsWidget(_domeo, this, false);
		tabs.add(searchTermsWidget);
		tabBar.addTab("Terms Search");
		
		StatementsSearchWidget statementsListPickerWidget = new StatementsSearchWidget(_domeo, this, false);
		tabs.add(statementsListPickerWidget);
		tabBar.addTab("Statements Search");
		
		
		tabBar.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				rightColumn.clear();
				rightColumn.add(tabs.get(event.getSelectedItem()));
			}
	    });
		
		rightColumn.add(tabs.get(0));
		
		if(_domeo.getPersistenceManager().getCurrentResource() instanceof ISelfReference && 
				((ISelfReference)_domeo.getPersistenceManager().getCurrentResource()).getSelfReference()!=null) {
			referencePanel.add(PubMedCitationPainter.getCitation((MPublicationArticleReference)(((ISelfReference)_domeo.getPersistenceManager().getCurrentResource()).getSelfReference()).getReference()));
		} else {
			referencePanel.add(new HTML("<a target='_blank' href='" + _domeo.getPersistenceManager().getCurrentResourceUrl() + "'>" + _domeo.getPersistenceManager().getCurrentResourceUrl() + "</a>"));
		}
		resized();
	}
	
//	// ------------------------
//	//  EDITING OF ANNOTATION
//	// ------------------------
	public FMicroPublicationForm(IDomeo domeo, final AFormsManager manager, final MMicroPublicationAnnotation annotation) {
		super(domeo);
		_manager = manager;
		_ann = annotation;
		_item = annotation.getMicroPublication();
		_this = this;
		
		initWidget(binder.createAndBindUi(this));
		
//		try {
//			if(_item.getComment()!=null) statementBody.setText(_item.getComment());
//			refreshAnnotationSetFilter(annotationSet, annotation);
//			
//			
//		} catch(Exception e) {
//			_domeo.getLogger().exception(this, "Failed to display current annotation " + annotation.getLocalId());
//			displayDialog("Failed to properly display existing annotation " + e.getMessage(), true);
//		}
		
		try {
			refreshAnnotationSetFilter(annotationSet, annotation);
		} catch(Exception e) {
			_domeo.getLogger().exception(AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION, this, "Failed to display current annotation " + annotation.getLocalId());
			displayDialog("Failed to properly display existing annotation " + e.getMessage(), true);
		}
		
		if(_item.getType().equals("Claim")) {
			radioClaim.setValue(true);
		} else if(_item.getType().equals("Hypothesis")) {
			radioHypothesis.setValue(true);
		}
		_domeo.getLogger().debug(this, "0");
		statementBody.setText(_item.getArgues().getText());
		_domeo.getLogger().debug(this, "1");
		evidence.addAll(_item.getEvidence());
		_domeo.getLogger().debug(this, "2");
		qualifiers.addAll(_item.getQualifiers());
		_domeo.getLogger().debug(this, "3");
		refreshSupport();
		_domeo.getLogger().debug(this, "4");
		refreshQualifiers();
		
		ButtonWithIcon sameVersionButton = new ButtonWithIcon();
		sameVersionButton.setStyleName(Domeo.resources.generalCss().applyButton());
		sameVersionButton.setWidth("78px");
		sameVersionButton.setHeight("22px");
		sameVersionButton.setResource(Domeo.resources.acceptLittleIcon());
		sameVersionButton.setText("Apply");
		sameVersionButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				try {
					if(isContentInvalid()) return;
					
					if(radioClaim.getValue() && _item.getType().equals(MMicroPublication.HYPOTHESIS)) hasChanged = true;
					if(radioHypothesis.getValue() && _item.getType().equals(MMicroPublication.CLAIM)) hasChanged = true;
					
					if(!isContentChanged(_ann)) {
						_domeo.getLogger().debug(this, "No changes to save for annotation " + _ann.getLocalId());
						_manager.getContainer().hide();
						return;
					}
					
					_item.getArgues().setText(statementBody.getText());
					
					_item.setQualifiers(qualifiers);
					_item.setEvidence(evidence);
					
					if(radioClaim.getValue()) {
						_item.setType(MMicroPublication.CLAIM);
					} else if(radioHypothesis.getValue()) {
						_item.setType(MMicroPublication.HYPOTHESIS);
					}
					
//					if(_manager instanceof TextAnnotationFormsPanel) {
//						MTextQuoteSelector selector = AnnotationFactory.createPrefixSuffixTextSelector(
//							_domeo.getAgentManager().getUserPerson(), 
//							_domeo.getPersistenceManager().getCurrentResource(), ((TextAnnotationFormsPanel)_manager).getHighlight().getExact(), 
//							((TextAnnotationFormsPanel)_manager).getHighlight().getPrefix(), ((TextAnnotationFormsPanel)_manager).getHighlight().getSuffix());
//						
//						MMicroPublication micropublication = MicroPublicationFactory.createMicroPublication((MTextQuoteSelector) selector);
//						micropublication.setQualifiers(qualifiers);
//						micropublication.setEvidence(evidence);
//						
//						MMicroPublicationAnnotation annotation = MicroPublicationFactory.createMicroPublicationAnnotation(
//							((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getCurrentSet(), 
//							_domeo.getAgentManager().getUserPerson(), 
//							_domeo.getAgentManager().getSoftware(),
//							_manager.getResource(), selector, 
//							micropublication);
//						
//						if(getSelectedSet(annotationSet)==null) {
//							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, true);
//						} else {
//							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, getSelectedSet(annotationSet));
//						}
//						_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(annotation, ((TextAnnotationFormsPanel)_manager).getHighlight());
//						_manager.hideContainer();
//					}
					
					_domeo.getContentPanel().getAnnotationFrameWrapper().updateAnnotation(_ann, getSelectedSet(annotationSet));
					_manager.hideContainer();
				} catch(Exception e) {
					_domeo.getLogger().exception(AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION, this, "Failed to apply modified anntoation " + annotation.getLocalId());
					displayDialog("Failed to apply modified annotation " + e.getMessage(), true);
				}
			}
		});
		buttonsPanel.add(sameVersionButton);

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
		
		SearchTermsWidget searchTermsWidget = new SearchTermsWidget(_domeo, this, false);
		tabs.add(searchTermsWidget);
		tabBar.addTab("Terms Search");
		
		StatementsSearchWidget statementsListPickerWidget = new StatementsSearchWidget(_domeo, this, false);
		tabs.add(statementsListPickerWidget);
		tabBar.addTab("Statements Search");
		
		tabBar.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				rightColumn.clear();
				rightColumn.add(tabs.get(event.getSelectedItem()));
			}
	    });
		
		rightColumn.add(tabs.get(0));
		
		if(_domeo.getPersistenceManager().getCurrentResource() instanceof ISelfReference && 
				((ISelfReference)_domeo.getPersistenceManager().getCurrentResource()).getSelfReference()!=null) {
			referencePanel.add(PubMedCitationPainter.getCitation((MPublicationArticleReference)(((ISelfReference)_domeo.getPersistenceManager().getCurrentResource()).getSelfReference()).getReference()));
		} else {
			referencePanel.add(new HTML("<a target='_blank' href='" + _domeo.getPersistenceManager().getCurrentResourceUrl() + "'>" + _domeo.getPersistenceManager().getCurrentResourceUrl() + "</a>"));
		}
		
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
		if(_item!=null) {
			if(!_item.getArgues().getText().equals(statementBody.getText()) || hasChanged) {
				return true;
			}
		}	
		return false;
	}

//	@Override
//	public void resized() {
//		container.setWidth("100%");
//	}
	
	@Override
	public void resized() {
//		this.setWidth((Window.getClientWidth() - 340) + "px");
//		tabBar.setWidth((Window.getClientWidth() - 615) + "px");
//		for(Widget tab:tabs) {
//			//if(tab instanceof IResizable) ((IResizable)tab).resized();
//			tab.setWidth((Window.getClientWidth() - 615) + "px");
//		}
//		
//		evidenceTabs.setHeight((Window.getClientHeight() - 490) + "px");
//		supportPanel.setHeight((Window.getClientHeight() - 520) + "px");
//		rightColumn.setHeight((Window.getClientHeight() - 340) + "px");
		
		
		buttonsPanelSpacer.setWidth(Math.max(0, (_manager.getContainerWidth()-300)) + "px");
		buttonsPanel.setWidth(Math.max(0, (_manager.getContainerWidth()-320)) + "px");
		headerPanel.setCellWidth(buttonsPanelSpacer, Math.max(0, (_manager.getContainerWidth()-300)) + "px");
	}
	
	@Override
	public String getTextContent() {
		// TODO Auto-generated method stub
		if(_manager instanceof ATextFormsManager)
			return ((ATextFormsManager)_manager).getHighlight().getExact();
		return "";
	}
	
	public void refreshQualifiers() {
		qualifiersPanel.clear();
		
		Integer counter = 0;
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("444px");
		
		for(MMpRelationship q: qualifiers) {
			final MMpRelationship _q = q;
			if(q.getObjectElement() instanceof MMpQualifier) {
				MLinkedResource res = ((MMpQualifier)q.getObjectElement()).getQualifier();

				StringBuffer sb = new StringBuffer();				
				sb.append("<img src='" + Domeo.resources.tagIcon().getSafeUri().asString() + "'/> " +  "<b>" + res.getLabel()+"</b> from <a target=\"_blank\"href=\""+res.getSource().getUrl() +"\">"+
						res.getSource().getLabel()+"</a>");
				if(res.getDescription()!=null && res.getDescription().length()>0)
					sb.append(", "+ res.getDescription());
				boolean nodigester = true;
				List<ITrustedResourceDigester> digesters = _domeo.getLinkedDataDigestersManager().getLnkedDataDigesters(res);
				for(ITrustedResourceDigester digester: digesters) {
					if(digester.getLinkLabel(res).trim().length()>0) {
						sb.append(", <a target=\"_blank\"href=\""+digester.getLinkUrl(res)+"\">@"+digester.getLinkLabel(res)+"</a>&nbsp;");
						nodigester = false;
					}
				}		
				HorizontalPanel hp = new HorizontalPanel();
				hp.setStyleName(style.indexWrapper());
				hp.setWidth("444px");
				hp.add(new HTML(sb.toString()));
				vp.add(hp);
				
				final Image removeIcon = new Image(Domeo.resources.deleteLittleIcon());
				removeIcon.setStyleName(style.link());
				removeIcon.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						hasChanged = true;
						qualifiers.remove(_q);
						if(_item!=null) {
							_item.getQualifiers().remove(_q);
							_ann.setHasChanged(true);
						}
						refreshQualifiers();
					}
				});					
				hp.add(removeIcon);
				hp.setCellWidth(removeIcon, "16px");
				hp.setCellHorizontalAlignment(removeIcon, HasHorizontalAlignment.ALIGN_RIGHT);
			}
		}
		
		qualifiersPanel.add(vp);
	}
	
	public void refreshSupport() {
		supportPanel.clear();
		
		Integer counter = 0;
		VerticalPanel vp = new VerticalPanel();
		_domeo.getLogger().debug(this, "3a");
		for(MMpRelationship ev: evidence) {
			_domeo.getLogger().debug(this, "3a" + ev.getObjectElement().getClass().getName());
			if(ev.getObjectElement() instanceof MMpData) {
				_domeo.getLogger().debug(this, "3b");
				if(ev.getObjectElement().getSelector() instanceof MImageInDocumentSelector) {
					_domeo.getLogger().debug(this, "3c");
					//String imgUrl = ((MImageInDocumentSelector)ev.getObjectElement().getSelector()).getTarget().getUrl();
					//Window.alert(imgUrl);
					displayImageInEvidence(vp, counter++, ev);
				}
			} else if(ev.getObjectElement() instanceof MMpReference) {
				displayReferenceInEvidence(vp, counter++, ev);
			} else if(ev.getObjectElement() instanceof MMpStatement) {
				displayStatementInEvidence(vp, counter++, ev);
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
		MMpDataImage imageData = new MMpDataImage(imgSelector);
		
		MMpRelationship suportedBy = MicroPublicationFactory.createMicroPublicationRelationship(_domeo.getAgentManager().getUserPerson(), imageData, IMicroPublicationsOntology.supportedBy);
		evidence.add(suportedBy);	
		hasChanged = true;
		refreshSupport();		
	}

	@Override
	public void addBibliographicObject(MPublicationArticleReference reference) {
		//references.add(reference);
		MMpReference referenceData = new MMpReference();
		referenceData.setReference(reference);
		
		MMpRelationship suportedBy = MicroPublicationFactory.createMicroPublicationRelationship(_domeo.getAgentManager().getUserPerson(), referenceData, IMicroPublicationsOntology.supportedBy);
		
		evidence.add(suportedBy);
		hasChanged = true;
		refreshSupport();
	}
	
	@Override
	public void addBibliographicObject(MAnnotationReference referenceAnnotation) {
		//references.add(reference);
		MMpReference referenceData = new MMpReference();
		referenceData.setReference((MPublicationArticleReference)referenceAnnotation.getReference());
		
		MMpRelationship suportedBy = MicroPublicationFactory.createMicroPublicationRelationship(_domeo.getAgentManager().getUserPerson(), referenceData, IMicroPublicationsOntology.supportedBy);
		
		evidence.add(suportedBy);
		hasChanged = true;
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
		
		final MMpRelationship _q = relationship;
		final Image removeIcon = new Image(Domeo.resources.deleteLittleIcon());
		removeIcon.setStyleName(style.link());
		removeIcon.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hasChanged = true;
				evidence.remove(_q);
				if(_item!=null) {
					_item.getEvidence().remove(_q);
					_ann.setHasChanged(true);
				}
				refreshSupport();
				//box.setEnabled(false);
				//_container.addImageAsData(_image);
			}
		});					
		hp1.add(removeIcon);
		hp1.setCellWidth(removeIcon, "16px");
		hp1.setCellHorizontalAlignment(removeIcon, HasHorizontalAlignment.ALIGN_RIGHT);
		
		vp.add(hp1);
	}
	
	private void displayStatementInEvidence(VerticalPanel vp, Integer counter, final MMpRelationship relationship) {
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
		
		hp1.add(new HTML(((MMpStatement)relationship.getObjectElement()).getText()));
		
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

		_domeo.getLogger().debug(this, "3c1");
		MOnlineImage image = (MOnlineImage)((MImageInDocumentSelector)relationship.getObjectElement().getSelector()).getTarget();
		_domeo.getLogger().debug(this, "3c2");
		VerticalPanel hp1 = new VerticalPanel();
		hp1.setWidth("442px");
		
		boolean small = false;
		boolean reduced = false;
		//Window.alert(image.getUrl() + " - " + image.getDisplayUrl());
		Image img = new Image(image.getDisplayUrl());
		_domeo.getLogger().debug(this, "3c3");
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
		
		_domeo.getLogger().debug(this, "3c4");
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
		hasChanged = true;
		refreshSupport();
	}

	@Override
	public void addAssociatedTerm(MLinkedResource term) {
		//MMpReference referenceData = new MMpReference();
		//referenceData.setReference((MPublicationArticleReference)referenceAnnotation.getReference());
		MMpQualifier qualifier = new MMpQualifier();
		qualifier.setQualifier(term);
		MMpRelationship suportedBy = MicroPublicationFactory.createMicroPublicationRelationship(_domeo.getAgentManager().getUserPerson(), qualifier, IMicroPublicationsOntology.supportedBy);
		
		qualifiers.add(suportedBy);
		hasChanged = true;
		refreshQualifiers();
	}

	@Override
	public ArrayList<MLinkedResource> getItems() {
		// TODO Auto-generated method stub
		return new  ArrayList<MLinkedResource>();
	}

	@Override
	public void addMicroPublicationObject(MMicroPublicationAnnotation reference) {
		MMpRelationship suportedBy = MicroPublicationFactory.createMicroPublicationRelationship(_domeo.getAgentManager().getUserPerson(), reference.getMicroPublication().getArgues(), IMicroPublicationsOntology.supportedBy);
		
		evidence.add(suportedBy);
		hasChanged = true;
		refreshSupport();
	}

	@Override
	public ArrayList<MMicroPublicationAnnotation> getMicroPublicationObjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MMicroPublicationAnnotation> getMicroPublicationAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}
}
