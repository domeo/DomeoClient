package org.mindinformatics.gwt.framework.component.ui.toolbar;

import org.mindinformatics.gwt.framework.src.Application;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ToolbarHorizontalPanel extends Composite implements IToolbarItem {
	
	@UiTemplate("ToolbarHorizontalPanel.ui.xml")
	interface Binder extends UiBinder<HorizontalPanel, ToolbarHorizontalPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField HorizontalPanel toolbarHorizontalPanel;
	
	private Image img;
	private HTML labelPanel;
	private ClickHandler _clickHandler;
	private HandlerRegistration iconHandlerRegistration;
	private HandlerRegistration labelHandlerRegistration;
	
	public ToolbarHorizontalPanel(IApplication application, ClickHandler clickHandler,
			String icon, String label, String tooltip) {

		_clickHandler = clickHandler;
		
		initWidget(binder.createAndBindUi(this)); 
		
		img = new Image(icon);
		img.setTitle(tooltip);
		//img.setHeight("24px");
		//img.setWidth("24px");
		
		labelPanel = new HTML(label);
		labelPanel.setTitle(tooltip);
		
		toolbarHorizontalPanel.add(img);
		toolbarHorizontalPanel.add(labelPanel);
		
		enable();
	}

	@Override
	public void enable() {
		iconHandlerRegistration  = img.addClickHandler(_clickHandler);
		img.setStyleName(Application.applicationResources.css().button());
		labelHandlerRegistration = labelPanel.addClickHandler(_clickHandler);
		labelPanel.setStyleName(Application.applicationResources.css().label());
	}

	@Override
	public void disable() {
		iconHandlerRegistration.removeHandler();
		img.setStyleName(Application.applicationResources.css().disabledButton());
		labelHandlerRegistration.removeHandler();
		labelPanel.setStyleName(Application.applicationResources.css().disabledLabel());
	}
	
	@Override
	public void hide() {
		toolbarHorizontalPanel.setVisible(false);
	}

	@Override
	public void show() {
		toolbarHorizontalPanel.setVisible(true);
	}
}
