package org.mindinformatics.gwt.domeo.component.textmining.ui;

import java.util.ArrayList;
import java.util.Collection;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.textmining.src.ITextMiningConnector;
import org.mindinformatics.gwt.domeo.component.textmining.src.TextMiningManager;
import org.mindinformatics.gwt.domeo.component.textmining.src.TextMiningRegistry;
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
	public String getTitle() { return TITLE; }
	
	// UI BInder
	interface Binder extends UiBinder<VerticalPanel, TextMiningServicePicker> {}
	private static final Binder binder = GWT.create(Binder.class);	
	
	// By contract 
	private IDomeo _domeo;
	private IContainerPanel _container;
	
	@UiField Button annotateButton;
	@UiField VerticalPanel availableServices;
	@UiField VerticalPanel annotatorDetails;

	private ArrayList<RadioButton> buttons = new ArrayList<RadioButton>();
	
	public TextMiningServicePicker(IDomeo domeo) {
		_domeo = domeo;
	
		initWidget(binder.createAndBindUi(this)); // Necessary for initializing Composite
		
		Collection<ITextMiningConnector> connectors = TextMiningRegistry.getInstance(_domeo).getTextMiningServices();
		boolean first = true;
		for(ITextMiningConnector connector: connectors) {
			RadioButton button = new RadioButton("textmining", connector.getAnnotatorLabel());
			final ITextMiningConnector _connector = connector;
			button.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					annotatorDetails.clear();
					annotatorDetails.add(_connector.getAnnotatorPanel());
				}
			});
			if(first) {
				button.setValue(true);
				annotatorDetails.add(_connector.getAnnotatorPanel());
			}
			first = false;
			availableServices.add(button);
			buttons.add(button);
		}
		
		annotateButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {				
				for(RadioButton button: buttons) {
					if(button.getValue()) {
						ITextMiningConnector connector = TextMiningRegistry.getInstance(_domeo).getTextMiningServiceByName(button.getText());
						if(connector!=null) {
							connector.selectConnector(_domeo);
							connector.annotate(new TextMiningManager(_domeo), 
								_domeo.getPersistenceManager().getCurrentResourceUrl(), 
								_domeo.getContentPanel().getAnnotationFrameWrapper().matchText);
							break;
						}
					}
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
	

}
