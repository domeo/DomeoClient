package org.mindinformatics.gwt.domeo.plugins.resource.nif.service.annotator;

import java.util.HashSet;

import org.mindinformatics.gwt.domeo.client.IDomeo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
public class FNifAnnotatorParametrizationForm extends Composite {

	interface Binder extends UiBinder<Widget, FNifAnnotatorParametrizationForm> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField CheckBox longestOnly;
	@UiField CheckBox includeAbbrev;
	@UiField CheckBox includeAcronym;
	@UiField CheckBox includeNumbers;
	@UiField Image nifLogo;
	@UiField Label addCategory;
	@UiField Label clearCategory;
	@UiField ListBox categoriesList;
	@UiField FlowPanel categories;
	@UiField ListBox criteria;
		
	// By contract 
	private IDomeo _domeo;
	
	private HashSet<String> cats = new  HashSet<String>();
	
	public FNifAnnotatorParametrizationForm(IDomeo domeo) {
		_domeo = domeo;
		
		//Resources _resources = Domeo.resources;
		
		initWidget(binder.createAndBindUi(this));
		
		longestOnly.setValue(PNifAnnotatorParameters.getInstance().longestOnly);
		longestOnly.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PNifAnnotatorParameters.getInstance().longestOnly=longestOnly.getValue();
			}
		});
		
		includeAbbrev.setValue(PNifAnnotatorParameters.getInstance().includeAbbrev);
		includeAbbrev.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PNifAnnotatorParameters.getInstance().includeAbbrev=includeAbbrev.getValue();
			}
		});
		
		includeAcronym.setValue(PNifAnnotatorParameters.getInstance().includeAcronym);
		includeAcronym.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PNifAnnotatorParameters.getInstance().includeAcronym=includeAcronym.getValue();
			}
		});
		
		includeNumbers.setValue(PNifAnnotatorParameters.getInstance().includeNumbers);
		includeNumbers.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PNifAnnotatorParameters.getInstance().includeNumbers=includeNumbers.getValue();
			}
		});
		
		criteria.setEnabled(false);
		criteria.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if(criteria.getSelectedIndex()==0) {
					PNifAnnotatorParameters.getInstance().includeCat = PNifAnnotatorParameters.getInstance().excludeCat;
					PNifAnnotatorParameters.getInstance().excludeCat = "";
				} else {
					PNifAnnotatorParameters.getInstance().excludeCat = PNifAnnotatorParameters.getInstance().includeCat;
					PNifAnnotatorParameters.getInstance().includeCat = "";
				}
			}
		});
		
		for(String category: PNifAnnotatorParameters.getInstance().categories) {
			categoriesList.addItem(category);
		}
		
		clearCategory.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cats.clear();
				criteria.setEnabled(false);
				refreshCategories();
			}
		});
		
		addCategory.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(cats.size()==0) {
					PNifAnnotatorParameters.getInstance().includeCat = "";
					PNifAnnotatorParameters.getInstance().excludeCat = "";
				}
				if(categoriesList.getItemText(categoriesList.getSelectedIndex()).equals("All")) {
					cats.clear();
					cats.add("All");
					criteria.setSelectedIndex(0);
					criteria.setEnabled(false);
					PNifAnnotatorParameters.getInstance().includeCat = "";
					PNifAnnotatorParameters.getInstance().excludeCat = "";
				} else {
					if(cats.size()==1 && cats.contains("All")) {
						cats.clear();
						PNifAnnotatorParameters.getInstance().includeCat = "";
						PNifAnnotatorParameters.getInstance().excludeCat = "";
					}
					cats.add(categoriesList.getItemText(categoriesList.getSelectedIndex()));
					if(criteria.getSelectedIndex()==0) {
						if(PNifAnnotatorParameters.getInstance().includeCat.trim().length()==0) 
							PNifAnnotatorParameters.getInstance().includeCat = categoriesList.getItemText(categoriesList.getSelectedIndex());
						else 
							PNifAnnotatorParameters.getInstance().includeCat = PNifAnnotatorParameters.getInstance().includeCat + "," + categoriesList.getItemText(categoriesList.getSelectedIndex());
					} else {
						if(PNifAnnotatorParameters.getInstance().excludeCat.trim().length()==0) 
							PNifAnnotatorParameters.getInstance().excludeCat = categoriesList.getItemText(categoriesList.getSelectedIndex());
						else
							PNifAnnotatorParameters.getInstance().excludeCat = PNifAnnotatorParameters.getInstance().excludeCat + "," + categoriesList.getItemText(categoriesList.getSelectedIndex());
					}
					criteria.setEnabled(true);
				}
				refreshCategories();
			}		
		});
	}
	
	public void refreshCategories() {
		categories.clear();
		Object[] cs = cats.toArray();
		for(int i=0; i<cs.length; i++) {
			categories.add(new HTML((String)cs[i]));
		}
	}
}
