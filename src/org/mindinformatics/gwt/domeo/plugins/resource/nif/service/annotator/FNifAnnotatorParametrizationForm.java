package org.mindinformatics.gwt.domeo.plugins.resource.nif.service.annotator;

import org.mindinformatics.gwt.domeo.client.IDomeo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
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
		
	// By contract 
	private IDomeo _domeo;
	
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
	}
}
