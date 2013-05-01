package org.mindinformatics.gwt.domeo.plugins.annotation.persistence.ui;

import org.mindinformatics.gwt.domeo.client.IDomeo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class PrivateAnnotationTopbar  extends Composite {

	interface Binder extends UiBinder<HorizontalPanel, PrivateAnnotationTopbar> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField CheckBox selectAll;
	
	@SuppressWarnings("unused")
	private IDomeo _domeo;
	private ExistingAnnotationSetsListPanel _containerPanel;
	
	public PrivateAnnotationTopbar(IDomeo domeo, ExistingAnnotationSetsListPanel containerPanel) {
		_domeo = domeo;
		_containerPanel = containerPanel;
		
		initWidget(binder.createAndBindUi(this)); 
		
		selectAll.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_containerPanel.manageSelection(selectAll.getValue());
			}
		});
	}
	
}
