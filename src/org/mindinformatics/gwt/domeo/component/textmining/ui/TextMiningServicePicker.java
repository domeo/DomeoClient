package org.mindinformatics.gwt.domeo.component.textmining.ui;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.BioPortalManager;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.textmine.TextMiningManager;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.service.NifManager;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TextMiningServicePicker  extends Composite implements IContentPanel {

	private static final String TITLE = "Text mining picker";
	
	// UI BInder
	interface Binder extends UiBinder<VerticalPanel, TextMiningServicePicker> {}
	private static final Binder binder = GWT.create(Binder.class);	
	
	// By contract 
	private IDomeo _domeo;
	private IContainerPanel _container;
	
	@UiField Button annotateButton;
	@UiField RadioButton nifAnnotator;
	@UiField RadioButton ncboAnnotator;

	public TextMiningServicePicker(IDomeo domeo) {
		_domeo = domeo;
	
		initWidget(binder.createAndBindUi(this)); // Necessary for initializing Composite
		
		annotateButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(ncboAnnotator.getValue()) {
					BioPortalManager bioPortalManager = BioPortalManager.getInstance();
					bioPortalManager.selectBioPortalConnector(_domeo);
					bioPortalManager.textmine(new TextMiningManager(_domeo), _domeo.getPersistenceManager().getCurrentResourceUrl(), _domeo.getContentPanel().getAnnotationFrameWrapper().matchText , "");
				} else {
					NifManager nifManager = NifManager.getInstance();
					nifManager.selectConnector(_domeo);
					nifManager.annotate(new TextMiningManager(_domeo), _domeo.getPersistenceManager().getCurrentResourceUrl(), _domeo.getContentPanel().getAnnotationFrameWrapper().matchText , "");
				}
				_container.hide();
			}
		});
	}

	@Override
	public void setContainer(IContainerPanel glassPanel) {
		_container = glassPanel;
	}

	@Override
	public IContainerPanel getContainer() {
		return _container;
	}
	
	public String getTitle() {
		return TITLE;
	}
}
