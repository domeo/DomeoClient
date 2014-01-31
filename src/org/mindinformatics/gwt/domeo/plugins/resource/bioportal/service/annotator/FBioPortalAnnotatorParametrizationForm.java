package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.annotator;

import java.util.HashSet;

import org.mindinformatics.gwt.domeo.client.IDomeo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class FBioPortalAnnotatorParametrizationForm extends Composite {

	interface Binder extends UiBinder<Widget, FBioPortalAnnotatorParametrizationForm> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	//@UiField CheckBox longestOnly; //Invalid Parameter.The longestOnly flag may not be set to "true" when either the ontologiesToExpand or ontologiesToKeepInResult field is populated
	@UiField CheckBox wholeWordOnly;
	@UiField CheckBox filterNumbers;
	@UiField CheckBox withDefaultStopWords;
	@UiField CheckBox isStopWordsCaseSenstive;
	@UiField CheckBox scored;
	@UiField CheckBox withSynonyms;
	@UiField Image ncboLogo;
	@UiField Label addCategory;
	@UiField Label clearCategory;
	@UiField ListBox categoriesList;
	@UiField FlowPanel categories;
	//@UiField ListBox criteria;
		
	// By contract 
	private IDomeo _domeo;
	
	private HashSet<String> cats = new  HashSet<String>();
	
	public FBioPortalAnnotatorParametrizationForm(IDomeo domeo) {
		_domeo = domeo;
		
		//Resources _resources = Domeo.resources;
		
		initWidget(binder.createAndBindUi(this));
		
		/*
		longestOnly.setValue(PBioPortalAnnotatorParameters.getInstance().longestOnly);
		longestOnly.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PBioPortalAnnotatorParameters.getInstance().longestOnly=longestOnly.getValue();
			}
		});
		*/
		
		wholeWordOnly.setValue(PBioPortalAnnotatorParameters.getInstance().wholeWordOnly);
		wholeWordOnly.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PBioPortalAnnotatorParameters.getInstance().wholeWordOnly=wholeWordOnly.getValue();
			}
		});
		
		filterNumbers.setValue(PBioPortalAnnotatorParameters.getInstance().filterNumbers);
		filterNumbers.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PBioPortalAnnotatorParameters.getInstance().filterNumbers=filterNumbers.getValue();
			}
		});
		
		withDefaultStopWords.setValue(PBioPortalAnnotatorParameters.getInstance().withDefaultStopWords);
		withDefaultStopWords.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PBioPortalAnnotatorParameters.getInstance().withDefaultStopWords=withDefaultStopWords.getValue();
			}
		});
		
		isStopWordsCaseSenstive.setValue(PBioPortalAnnotatorParameters.getInstance().isStopWordsCaseSenstive);
		isStopWordsCaseSenstive.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PBioPortalAnnotatorParameters.getInstance().isStopWordsCaseSenstive=isStopWordsCaseSenstive.getValue();
			}
		});
		
		scored.setValue(PBioPortalAnnotatorParameters.getInstance().scored);
		scored.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PBioPortalAnnotatorParameters.getInstance().scored=scored.getValue();
			}
		});
		
		withSynonyms.setValue(PBioPortalAnnotatorParameters.getInstance().withSynonyms);
		withSynonyms.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PBioPortalAnnotatorParameters.getInstance().withSynonyms=withSynonyms.getValue();
			}
		});
		
//		criteria.setEnabled(false);
//		criteria.addChangeHandler(new ChangeHandler() {
//			@Override
//			public void onChange(ChangeEvent event) {
//				if(criteria.getSelectedIndex()==0) {
//					PBioPortalAnnotatorParameters.getInstance().includeCat = PBioPortalAnnotatorParameters.getInstance().excludeCat;
//					PBioPortalAnnotatorParameters.getInstance().excludeCat = "";
//				} else {
//					PBioPortalAnnotatorParameters.getInstance().excludeCat = PBioPortalAnnotatorParameters.getInstance().includeCat;
//					PBioPortalAnnotatorParameters.getInstance().includeCat = "";
//				}
//			}
//		});
//		
//		for(String category: PBioPortalAnnotatorParameters.getInstance().categories) {
//			categoriesList.addItem(category);
//		}
//		
//		clearCategory.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				cats.clear();
//				criteria.setEnabled(false);
//				refreshCategories();
//			}
//		});
		
//		addCategory.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				if(cats.size()==0) {
//					PBioPortalAnnotatorParameters.getInstance().includeCat = "";
//					PBioPortalAnnotatorParameters.getInstance().excludeCat = "";
//				}
//				if(categoriesList.getItemText(categoriesList.getSelectedIndex()).equals("All")) {
//					cats.clear();
//					cats.add("All");
//					criteria.setSelectedIndex(0);
//					criteria.setEnabled(false);
//					PBioPortalAnnotatorParameters.getInstance().includeCat = "";
//					PBioPortalAnnotatorParameters.getInstance().excludeCat = "";
//				} else {
//					if(cats.size()==1 && cats.contains("All")) {
//						cats.clear();
//						PBioPortalAnnotatorParameters.getInstance().includeCat = "";
//						PBioPortalAnnotatorParameters.getInstance().excludeCat = "";
//					}
//					cats.add(categoriesList.getItemText(categoriesList.getSelectedIndex()));
//					if(criteria.getSelectedIndex()==0) {
//						if(PBioPortalAnnotatorParameters.getInstance().includeCat.trim().length()==0) 
//							PBioPortalAnnotatorParameters.getInstance().includeCat = categoriesList.getItemText(categoriesList.getSelectedIndex());
//						else 
//							PBioPortalAnnotatorParameters.getInstance().includeCat = PBioPortalAnnotatorParameters.getInstance().includeCat + "," + categoriesList.getItemText(categoriesList.getSelectedIndex());
//					} else {
//						if(PBioPortalAnnotatorParameters.getInstance().excludeCat.trim().length()==0) 
//							PBioPortalAnnotatorParameters.getInstance().excludeCat = categoriesList.getItemText(categoriesList.getSelectedIndex());
//						else
//							PBioPortalAnnotatorParameters.getInstance().excludeCat = PBioPortalAnnotatorParameters.getInstance().excludeCat + "," + categoriesList.getItemText(categoriesList.getSelectedIndex());
//					}
//					criteria.setEnabled(true);
//				}
//				refreshCategories();
//			}		
//		});
	}
	
	public void refreshCategories() {
		categories.clear();
		Object[] cs = cats.toArray();
		for(int i=0; i<cs.length; i++) {
			categories.add(new HTML((String)cs[i]));
		}
	}
}
