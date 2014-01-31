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
package org.mindinformatics.gwt.domeo.component.cache.images.ui;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.component.cache.images.model.ImageProxy;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationCitationReference;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;
import org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.model.JsPmcImage;
import org.mindinformatics.gwt.framework.widget.WidgetUtilsResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CachedImagesPanel extends Composite {

	// UI Binder
	interface Binder extends UiBinder<Widget, CachedImagesPanel> { }
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
	
	// By contract 
	private IDomeo _domeo;
	
	@UiField VerticalPanel container;
	
	public CachedImagesPanel(IDomeo domeo) {
		_domeo = domeo;
		
		initWidget(binder.createAndBindUi(this));

		container.add(new HTML("No images detected"));
	}
	
	public void create() {
		container.clear();
		container.add(new HTML("Number of images detected: "+_domeo.getImagesCache().getSize()));
		
		widgetUtilsResources.widgetCss().ensureInjected();
		
		int counter = 0;
		for(String key:_domeo.getImagesCache().getKeys()) {
			ArrayList<ImageProxy> list = _domeo.getImagesCache().getValue(key);
			for(ImageProxy image: list) {
				VerticalPanel hp1 = new VerticalPanel();
				hp1.setWidth("100%");
				
				boolean small = false;
				boolean reduced = false;
				Image img = new Image(image.getDisplayUrl());
				if(img.getWidth()>440) {
					img.setWidth("430px");  
					reduced = true;
				} else if(img.getWidth()<220) {
					small = true;
					img.setWidth("200px"); 
				}
				if(image.getTitle()!=null && image.getTitle().trim().length()>0) {
					img.setTitle(image.getTitle());
				}
				
				if(!small) {
					SimplePanel imageWrap = new SimplePanel();
					imageWrap.setStyleName(style.imageWrap());
					imageWrap.add(img);
					imageWrap.setStyleName(style.centerText());
					hp1.add(imageWrap);
					hp1.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
					if(image.getTitle()!=null && image.getTitle().trim().length()>0) {
						HTML title = new HTML("<b>"+image.getTitle()+"</b>");
						hp1.add(title);
						hp1.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_CENTER);
					}
					
					_domeo.getLogger().debug(this, "0 " + image);
					_domeo.getLogger().debug(this, "1 " + image.getDisplayUrl());
					JsPmcImage im = _domeo.getPmcImagesCache().getImageMetadataFromUrl(image.getDisplayUrl());
					
					if(im!=null) {
						_domeo.getLogger().debug(this, "2 " + im.getCaption());
						hp1.add(new HTML("<span style='font-weight: bold;'>" + im.getTitle() + "</span> - " + im.getCaption()  + 
							" - <a target='_blank' href='http://krauthammerlab.med.yale.edu/imagefinder/Figure.external?sp=S" + 
								im.getImageId() + "'>Yale Image Finder</a>" + 
							" - <a target='_blank' href='http://cbakerlab.unbsj.ca:8080/iCyrus/page/" + 
								im.getImageId() + "'>iCyrus</a>"));
					}
				
					HorizontalPanel hp = new HorizontalPanel();
					final ImageProxy _image = image;
					ClickHandler exploreHandler = new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							Window.alert("Exploration of image not yet implemented (wip) " + _image.getOriginalUrl());
						}
					};
					
//					Image annotateIcon = new Image(Domeo.resources.editLittleIcon());
//					annotateIcon.addClickHandler(exploreHandler);
//					HTML annotateLabel = new HTML("Annotate");
//					hp.add(annotateIcon);
//					annotateLabel.addClickHandler(exploreHandler);
//					hp.add(annotateLabel);
//					if(!reduced) {
						Image exploreIcon = new Image(Domeo.resources.browseLittleIcon());
						exploreIcon.addClickHandler(exploreHandler);
						HTML exploreLabel = new HTML("Explore");
						hp.add(exploreIcon);
						exploreLabel.addClickHandler(exploreHandler);
						hp.add(exploreLabel);
//					}
					
					_domeo.getLogger().info(this, "cache lookup image;;;" + _image.getOriginalUrl());
					
					// Make the lin absolute
					ArrayList<MAnnotation> annotations = _domeo.getAnnotationPersistenceManager().annotationsForImage(_image.getOriginalUrl());
//					if(annotations!=null && annotations.size()>0) {
//						ClickHandler annotationsHandler = new ClickHandler() {
//							@Override
//							public void onClick(ClickEvent event) {
//								Window.alert("Exploration of annotations not yet implemented (wip) " + _image.getSrc());
//							}
//						};
//						Image exploreIcon = new Image(Domeo.resources.browseLittleIcon());
//						exploreIcon.addClickHandler(annotationsHandler);
//						HTML exploreLabel = new HTML(annotations.size() + " Annotations");
//						hp.add(exploreIcon);
//						exploreLabel.addClickHandler(annotationsHandler);
//						hp.add(exploreLabel);
//					}
					
					if(counter%2 == 1) {
						hp1.addStyleName(style.indexOdd());
					} else {
						hp1.addStyleName(style.indexEven());
					}
					counter++;
					
					hp1.add(hp);
					
					// List of annotations
					try {
						if(annotations!=null && annotations.size()>0) {
							//Collections.sort(annotations, new SortByVerticalPostion());
							for(MAnnotation ann: annotations) {
								if(ann instanceof MCommentAnnotation || ann instanceof MAnnotationCitationReference) continue; 
								ITileComponent c = _domeo.getAnnotationTailsManager().getAnnotationTile(ann.getClass().getName(), null);
								if(c==null) {
									VerticalPanel vp = new VerticalPanel();
									vp.add(new Label(ann.getLocalId() + " - " + ann.getClass().getName() + " - " + ann.getY()));
									hp1.add(vp);
								} else {
									try {
										c.initializeLens(ann);
										hp1.add(c.getTile());
									} catch(Exception e) {
										// If something goes wrong just display the default tile
										_domeo.getLogger().exception(this, "Refreshing tile: " + e.getMessage());
										VerticalPanel vp = new VerticalPanel();
										vp.add(new Label(ann.getLocalId() + " - " + ann.getClass().getName() + " - " + ann.getY()));
										hp1.add(vp);
									}
								}
							}
						}
						if(counter%2 == 1) {
							//hp1.setStyleName(widgetUtilsResources.widgetCss().tableOddRow());
						}
					} catch(Exception e) {
						_domeo.getLogger().exception(this, e.getMessage());
					}
				} else {
					HorizontalPanel main = new HorizontalPanel();
					
					SimplePanel imageWrap = new SimplePanel();
					imageWrap.setStyleName(style.imageWrap());
					imageWrap.add(img);
					main.add(imageWrap);
					
					
					//_domeo.getImagesCache().getValue(key);

					
					
					//Window.alert("" + image.getDisplayUrl() + "" + _domeo.getPmcImagesCache().getImageMetadata(""));
					
					//hp1.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
					
					VerticalPanel right = new VerticalPanel();
					right.setWidth("100%");
					
					if(image.getTitle()!=null && image.getTitle().trim().length()>0) {
						HTML title = new HTML("title: <b>"+image.getTitle()+"</b>");
						right.add(title);
						right.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_LEFT);
					}
					
					HorizontalPanel hp = new HorizontalPanel();
					final ImageProxy _image = image;
					ClickHandler exploreHandler = new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							Window.alert("Exploration of image not yet implemented (wip) " + _image.getOriginalUrl());
						}
					};
					
//					Image annotateIcon = new Image(Domeo.resources.editLittleIcon());
//					annotateIcon.addClickHandler(exploreHandler);
//					HTML annotateLabel = new HTML("Annotate");
//					hp.add(annotateIcon);
//					annotateLabel.addClickHandler(exploreHandler);
//					hp.add(annotateLabel);
//					if(!reduced) {
						Image exploreIcon = new Image(Domeo.resources.browseLittleIcon());
						exploreIcon.addClickHandler(exploreHandler);
						HTML exploreLabel = new HTML("Explore");
						hp.add(exploreIcon);
						exploreLabel.addClickHandler(exploreHandler);
						hp.add(exploreLabel);
//					}
					ArrayList<MAnnotation> annotations = _domeo.getAnnotationPersistenceManager().annotationsForImage(_image.getOriginalUrl());
//					if(annotations!=null && annotations.size()>0) {
//						ClickHandler annotationsHandler = new ClickHandler() {
//							@Override
//							public void onClick(ClickEvent event) {
//								Window.alert("Exploration of annotations not yet implemented (wip) " + _image.getSrc());
//							}
//						};
//						Image exploreIcon = new Image(Domeo.resources.browseLittleIcon());
//						exploreIcon.addClickHandler(annotationsHandler);
//						HTML exploreLabel = new HTML(annotations.size() + " Annotations");
//						hp.add(exploreIcon);
//						exploreLabel.addClickHandler(annotationsHandler);
//						hp.add(exploreLabel);
//					}
					right.add(hp);
					main.add(right);
					
					JsPmcImage im = _domeo.getPmcImagesCache().getImageMetadataFromUrl(image.getDisplayUrl());
					if(im!=null) {
						_domeo.getLogger().debug(this, "2 " + im.getCaption());
						main.add(new HTML("<span style='font-weight: bold;'>" + im.getTitle() + "</span> - " + im.getCaption()  + 
							" - <a target='_blank' href='http://krauthammerlab.med.yale.edu/imagefinder/Figure.external?sp=S" + 
								im.getImageId() + "'>Yale Image Finder</a>" + 
							" - <a target='_blank' href='http://cbakerlab.unbsj.ca:8080/iCyrus/page/" + 
								im.getImageId() + "'>iCyrus</a>"));
					}
										
					if(counter%2 == 1) {
						//right.setStyleName(widgetUtilsResources.widgetCss().tableOddRow());
					}
					
					if(counter%2 == 1) {
						hp1.addStyleName(style.indexOdd());
					} else {
						hp1.addStyleName(style.indexEven());
					}
					counter++;
					
					main.setCellWidth(right, "100%");
					hp1.add(main);	
					
					// List of annotations
					try {
						if(annotations!=null && annotations.size()>0) {
							//Collections.sort(annotations, new SortByVerticalPostion());
							for(MAnnotation ann: annotations) {
								if(ann instanceof MCommentAnnotation || ann instanceof MAnnotationCitationReference) continue; 
								ITileComponent c = _domeo.getAnnotationTailsManager().getAnnotationTile(ann.getClass().getName(), null);
								if(c==null) {
									VerticalPanel vp = new VerticalPanel();
									vp.add(new Label(ann.getLocalId() + " - " + ann.getClass().getName() + " - " + ann.getY()));
									hp1.add(vp);
								} else {
									try {
										c.initializeLens(ann);
										hp1.add(c.getTile());
									} catch(Exception e) {
										// If something goes wrong just display the default tile
										_domeo.getLogger().exception(this, "Refreshing tile: " + e.getMessage());
										VerticalPanel vp = new VerticalPanel();
										vp.add(new Label(ann.getLocalId() + " - " + ann.getClass().getName() + " - " + ann.getY()));
										hp1.add(vp);
									}
								}
							}
						}
					} catch(Exception e) {
						_domeo.getLogger().exception(this, e.getMessage());
					}
					
				}
				container.add(hp1);
			}
		}
	}
}
