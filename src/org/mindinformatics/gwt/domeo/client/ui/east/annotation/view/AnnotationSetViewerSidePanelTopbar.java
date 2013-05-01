package org.mindinformatics.gwt.domeo.client.ui.east.annotation.view;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class AnnotationSetViewerSidePanelTopbar  extends Composite {

	interface Binder extends UiBinder<HorizontalPanel, AnnotationSetViewerSidePanelTopbar> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	@SuppressWarnings("unused")
	private IDomeo _domeo;
	private AnnotationForSetSidePanel _annotationSidePanel;
	
	@UiField Label titleLabel;
	@UiField SimplePanel explorePanel;
	@UiField Label exploreLabel;
	@UiField SimplePanel closePanel;

	public AnnotationSetViewerSidePanelTopbar(IDomeo domeo, final AnnotationForSetSidePanel annotationSidePanel,
			String title) {
		_domeo = domeo;
		_annotationSidePanel = annotationSidePanel;
		
		initWidget(binder.createAndBindUi(this));

		titleLabel.setText(title);
		
		Image exploreIcon =new Image(Domeo.resources.showLittleIcon());
		exploreIcon.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Explore to be implemented");	
				
			}
		});
		exploreLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Explore to be implemented");	
			}
		});
		explorePanel.add(exploreIcon);
		//printPanel.add(new Image(Domeo.resources.printLittleIcon()));
		Image closeIcon = new Image(Domeo.resources.closeLittleIcon());
		closeIcon.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_annotationSidePanel.destroy();
			}
		});
		closePanel.add(closeIcon);
	}
	
	public void init() {

	}
	
	public void refresh() {

	}
	
}
