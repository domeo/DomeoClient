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
import org.mindinformatics.gwt.domeo.component.bibliography.ui.listpicker.IReferencesListPickerContainer;
import org.mindinformatics.gwt.domeo.component.bibliography.ui.listpicker.ReferencesListPickerWidget;
import org.mindinformatics.gwt.domeo.component.cache.images.model.ImageProxy;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.listpicker.IImagesListPickerContainer;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.listpicker.ImagesListPickerWidget;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.ISelectionProvider;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.lenses.PubMedCitationPainter;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model.MPubMedDocument;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search.IPubmedSearchObjectContainer;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search.PubmedSearchWidget;
import org.mindinformatics.gwt.framework.model.references.IReferences;
import org.mindinformatics.gwt.framework.model.references.ISelfReference;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.src.IResizable;
import org.mindinformatics.gwt.framework.widget.WidgetUtilsResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
public class FMicroPublicationForm extends AFormComponent implements IResizable, ISelectionProvider, IPubmedSearchObjectContainer, IImagesListPickerContainer, IReferencesListPickerContainer {

	public static final String LABEL = "Micro Publication";
	public static final String LABEL_EDIT = "Edit Micro Publication";
	
	public static final String LOG_CATEGORY_QUALIFIER_CREATE = "CREATING MICROPUB";
	public static final String LOG_CATEGORY_QUALIFIER_EDIT = "EDITING MICROPUB";
	
	interface Binder extends UiBinder<VerticalPanel, FMicroPublicationForm> { }
	private static final Binder binder = GWT.create(Binder.class);

	public static final WidgetUtilsResources widgetUtilsResources = 
			GWT.create(WidgetUtilsResources.class);
	
	interface LocalCss extends CssResource {
		String indexOdd();
		String indexEven();
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
	
	private ImagesListPickerWidget imagesListPickerWidget;
	private ReferencesListPickerWidget referencesListPickerWidget;
	
	private ArrayList<ImageProxy> images = new ArrayList<ImageProxy>();
	private ArrayList<MPublicationArticleReference> references = new ArrayList<MPublicationArticleReference>();
	
	//private AntibodiesSearchWidget antibodiesSearchWidget;

	public FMicroPublicationForm(IDomeo domeo, final AFormsManager manager, boolean inFolders) {
		super(domeo);
		_manager = manager;
		
		initWidget(binder.createAndBindUi(this));
	
		refreshAnnotationSetFilter(annotationSet, null);
		statementBody.setText(getTextContent());
		
//		addDataImage.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
////				searchDataCitationWidget = new DataCitationSearchWidget(
////						_annotator, _this, _resources, true);
////				rightColumn.clear();
////				rightColumn.add(searchDataCitationWidget);
//			}
//		});

//		ButtonWithIcon yesButton = new ButtonWithIcon(Domeo.resources.generalCss().applyButton());
//		yesButton.setWidth("78px");
//		yesButton.setHeight("22px");
//		yesButton.setResource(Domeo.resources.acceptLittleIcon());
//		yesButton.setText("Apply");
//		yesButton.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				if(isContentInvalid()) return;
//			
////				try { 
////					if(_item == null) {
////						if(_manager instanceof TextAnnotationFormsPanel) {
////							MTextQuoteSelector selector = AnnotationFactory.createPrefixSuffixTextSelector(
////									_domeo.getAgentManager().getUserPerson(), 
////									_domeo.getPersistenceManager().getCurrentResource(), ((TextAnnotationFormsPanel)_manager).getHighlight().getExact(), 
////									((TextAnnotationFormsPanel)_manager).getHighlight().getPrefix(), ((TextAnnotationFormsPanel)_manager).getHighlight().getSuffix());
////							
////							
//////							// TODO Register coordinate of the selection.
//////							MAntibodyAnnotation annotation = NifAntibodyFactory.createAntibody(
//////									((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getCurrentSet(), 
//////									_domeo.getAgentManager().getUserPerson(), _domeo.getAgentManager().getSoftware(),
//////									_manager.getResource(), selector);
//////							// TODO Register coordinate of highlight.
//////							
//////							MAntibodyUsage antibodyUsage = NifAntibodyFactory.createAntibodyUsage();
//////							annotation.setAntibodyUsage(antibodyUsage);
//////								
//////							MAntibody normalizedAntibody = (MAntibody) _domeo.getResourcesManager().cacheResource(currentAntibody);
//////							annotation.getAntibodyUsage().setAntibody(normalizedAntibody);
//////							_domeo.getLogger().command(getLogCategoryCreate(), this, " with term " + currentAntibody.getLabel());
//////	
//////							annotation.setComment(statementBody.getText());
//////							annotation.getProtocols().clear();
//////								
//////							if(getSelectedSet(annotationSet)==null) {
//////								_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, true);
//////							} else {
//////								_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, getSelectedSet(annotationSet));
//////							}
//////							_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(annotation, ((TextAnnotationFormsPanel)_manager).getHighlight());
//////							_manager.hideContainer();
////							
////						} else if(_manager instanceof ImageAnnotationFormsPanel) {
////							MAntibodyAnnotation annotation = NifAntibodyFactory.createAntibody(_domeo,
////									((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getCurrentSet(), 
////									_domeo.getAgentManager().getUserPerson(), _domeo.getAgentManager().getSoftware(),
////									_manager.getResource());
////	//						annotation.setY((((ImageAnnotationFormsPanel)_manager).getImageY()));
////							
//////							MAntibodyUsage antibodyUsage = NifAntibodyFactory.createAntibodyUsage();
//////							annotation.setAntibodyUsage(antibodyUsage);
//////								
//////							MAntibody normalizedAntibody = (MAntibody) _domeo.getResourcesManager().cacheResource(currentAntibody);
//////							annotation.getAntibodyUsage().setAntibody(normalizedAntibody);
//////							_domeo.getLogger().command(getLogCategoryCreate(), this, " with term " + currentAntibody.getLabel());
//////	
//////							annotation.setComment(statementBody.getText());
//////							annotation.getProtocols().clear();
//////							
//////	//						Iterator<MLinkedDataResource> termsIterator = terms.iterator();
//////	//						while(termsIterator.hasNext()) {
//////	//							MLinkedDataResource term = termsIterator.next();
//////	//							MLinkedDataResource normalizedTerm = (MLinkedDataResource) _domeo.getResourcesManager().cacheResource(term);
//////	//							annotation.addTerm(normalizedTerm);
//////	//							_domeo.getLogger().command(getLogCategoryCreate(), this, " with term " + term.getLabel());
//////	//						}
//////							
//////							if(getSelectedSet(annotationSet)==null) {
//////								_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, true);
//////							} else {
//////								_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, getSelectedSet(annotationSet));
//////							}
//////							//TODO Display annotation for an image is present somehow
//////							_domeo.getAnnotationPersistenceManager().cacheAnnotationOfImage(_manager.getResource().getUrl(), annotation);
//////							_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(annotation, _manager.getResource(), ((ImageAnnotationFormsPanel) _manager).getSelectedElement());
//////							((ImageAnnotationFormsPanel) _manager).initializeForms();
//////							
//////							// Display cached images
//////							Object w = _domeo.getResourcePanelsManager().getResourcePanel(
//////									_domeo.getPersistenceManager().getCurrentResource().getClass().getName());
//////							if(w instanceof ICachedImages) {
//////								((ICachedImages)w).createVisualization();
//////							}
////						}
////					} else {
////						//_item.setType(PostitType.findByName(postitTypes.getItemText(postitTypes.getSelectedIndex())));
////						//_item.setText(getPostItBody());
////					}
////				} catch (Exception e) {
////					_domeo.getLogger().exception(this, e.getMessage());
////				}
//			}
//		});
//		buttonsPanel.add(yesButton);
//		
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
		
		/*
		antibodiesSearchWidget = new AntibodiesSearchWidget(_domeo, this, Domeo.resources, false);
		// TODO If border is needed, there is a need for a wrapper. SearchTermWidget is one
		//AntibodiesSelectionList termsList = new AntibodiesSelectionList(_domeo, this, getAntibodiesList(), new HashMap<String, MLinkedDataResource>(0));
		tabs.add(antibodiesSearchWidget);
		*/
		//tabs.add(termsList);		
		
//		tabBar.addTab("Search for Antibodies");
//		tabBar.addTab("Recently Used");

//		
//		rightColumn.add(tabs.get(0));
	
		//refreshAssociatedAntibodies();
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
		rightColumn.setHeight((Window.getClientHeight() - 340) + "px");
	}

//	@Override
//	public String getTextContent() {
//		// TODO Auto-generated method stub
//		if(_manager instanceof ATextFormsManager)
//			return ((ATextFormsManager)_manager).getHighlight().getExact();
//		return "";
//	}
	/*
	public void addAssociatedAntibody(MAntibody antibody) {
		currentAntibody = antibody;
		refreshAssociatedAntibodies();
	}
	
	
	public void refreshAssociatedAntibodies() {
		if(currentAntibody!=null) 
			displayAssociatedAntibodies(currentAntibody);
		else {
			newQualifiers.clear();
			HTML h = new HTML("<em>no antibody selected</em>.");
			newQualifiers.add(h);
		}
		//refreshTabTitle();
	}
	
	private void displayAssociatedAntibodies(MAntibody antibody) {
		newQualifiers.clear();
		//for(MAntibody antibody: antibodies) {
			final MAntibody _antibody = antibody;
			HorizontalPanel sp = new HorizontalPanel();
			sp.setStyleName("cs-acceptedQualifier");
			HTML a = new HTML("<a target='_blank' href='" + antibody.getUrl() + "'>" + antibody.getLabel() + "</a> (" + antibody.getVendor() + ")");
			a.setStyleName("cs-acceptedQualifierLabel");
			a.setTitle(antibody.getLabel() + ": " + antibody.getDescription()); // + " - SOURCE: " + term.getSource().getLabel());
			sp.add(a);
			Image i = new Image(Domeo.resources.deleteLittleIcon());
			i.setStyleName("cs-rejectQualifier");
			i.setTitle("Remove " + antibody.getLabel());
			i.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					currentAntibody = null;
					antibodiesSearchWidget.removeAntibody(_antibody);
					refreshAssociatedAntibodies();
				}
			});
			sp.add(i);
			newQualifiers.add(sp);
		//}
	}
	*/

	/*
	@Override
	public ArrayList<MAntibody> getAntibodies() {
		// TODO Auto-generated method stub
		return new ArrayList<MAntibody>();
	}
	*/

	/*
	@Override
	public String getFilterValue() {
		// No filtering for this
		return "";
	}
	*/

	/*
	@Override
	public void addAntibody(MAntibody antibody) {
		addAssociatedAntibody(antibody);
	}
	*/
/*
	@Override
	public ArrayList<MAntibody> getAntibodiesList() {
		//return _domeo.getAnnotationPersistenceManager().getAllTerms();
		return new ArrayList<MAntibody>();
	}
	*/
	
	@Override
	public String getTextContent() {
		// TODO Auto-generated method stub
		if(_manager instanceof ATextFormsManager)
			return ((ATextFormsManager)_manager).getHighlight().getExact();
		return "";
	}
	
	public void refreshSupport() {
		supportPanel.clear();
		VerticalPanel vp = new VerticalPanel();
		int counter = 0;
		for(ImageProxy image: images) {
			VerticalPanel hp1 = new VerticalPanel();
			hp1.setWidth("99%");
			
			boolean small = false;
			boolean reduced = false;
			Image img = new Image(image.getDisplayUrl());
			if(img.getWidth()>440) {
				img.setWidth("430px");  
				reduced = true;
			} else if(img.getWidth()<220) {
				small = true;
				//img.setWidth("200px"); 
			}
			if(image.getTitle()!=null && image.getTitle().trim().length()>0) {
				img.setTitle(image.getTitle());
			}
			
			if(!small) {
				
				HorizontalPanel main = new HorizontalPanel();
				VerticalPanel left = new VerticalPanel();
				
				SimplePanel imageWrap = new SimplePanel();
				imageWrap.setStyleName(style.imageWrap());
				imageWrap.add(img);
				imageWrap.setStyleName(style.centerText());
				left.add(imageWrap);
				left.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_LEFT);
				if(image.getTitle()!=null && image.getTitle().trim().length()>0) {
					HTML title = new HTML("<b>"+image.getTitle()+"</b>");
					left.add(title);
					left.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_LEFT);
				} else {
					HTML title = new HTML("<b>Title</b>");
					left.add(title);
					left.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_LEFT);
				}
				main.add(left);	
				
				final Image removeIcon = new Image(Domeo.resources.deleteLittleIcon());
				removeIcon.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						//box.setEnabled(false);
						//_container.addImageAsData(_image);
					}
				});					
				main.add(removeIcon);
				hp1.add(main);	
				
				if(counter%2 == 1) {
					hp1.addStyleName(style.indexOdd());
				} else {
					hp1.addStyleName(style.indexEven());
				}
				counter++;
			} else {
				HorizontalPanel main = new HorizontalPanel();
				
				SimplePanel imageWrap = new SimplePanel();
				imageWrap.setStyleName(style.imageWrap());
				imageWrap.add(img);
				main.add(imageWrap);
				hp1.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
				
				VerticalPanel right = new VerticalPanel();
				right.setWidth("100%");
				
				if(image.getTitle()!=null && image.getTitle().trim().length()>0) {
					HTML title = new HTML("title: <b>"+image.getTitle()+"</b>");
					right.add(title);
					right.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_LEFT);
				} else {					
					HTML title = new HTML("title: <b>Title</b>");
					right.add(title);
					right.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_LEFT);
				}
				
				main.add(right);
				main.setCellWidth(right, "100%");
				
				final Image removeIcon = new Image(Domeo.resources.deleteLittleIcon());
				removeIcon.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						//box.setEnabled(false);
						//_container.addImageAsData(_image);
					}
				});					
				main.add(removeIcon);
				
				
				hp1.add(main);	

				if(counter%2 == 1) {
					hp1.addStyleName(style.indexOdd());
				} else {
					hp1.addStyleName(style.indexEven());
				}
				counter++;
			}				
			vp.add(hp1);
		}
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
		
		
		supportPanel.add(vp);
	}

	@Override
	public void addImageAsData(ImageProxy image) {
		images.add(image);	
		refreshSupport();
	}

	@Override
	public void addBibliographicObject(MPublicationArticleReference reference) {
		references.add(reference);
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
}
