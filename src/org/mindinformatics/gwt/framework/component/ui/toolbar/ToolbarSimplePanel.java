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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ToolbarSimplePanel extends Composite implements IToolbarItem  {
	
	@UiTemplate("ToolbarSimplePanel.ui.xml")
	interface Binder extends UiBinder<SimplePanel, ToolbarSimplePanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField SimplePanel toolbarSimpleButtonPanel;
	
	private Image img;
	private ClickHandler _clickHandler;
	private HandlerRegistration handlerRegistration;
	
	public ToolbarSimplePanel(IApplication application, ClickHandler clickHandler,
			String icon, String tooltip) {

		_clickHandler = clickHandler;
		
		initWidget(binder.createAndBindUi(this)); 
		
		img = new Image(icon);
		img.setTitle(tooltip);

		toolbarSimpleButtonPanel.add(img);
		
		enable();
	}

	@Override
	public void enable() {
		handlerRegistration  = img.addClickHandler(_clickHandler);
		img.setStyleName(Application.applicationResources.css().button());
	}

	@Override
	public void disable() {
		handlerRegistration.removeHandler();
		img.setStyleName(Application.applicationResources.css().disabledButton());
	}

	@Override
	public void hide() {
		toolbarSimpleButtonPanel.setVisible(false);
	}

	@Override
	public void show() {
		toolbarSimpleButtonPanel.setVisible(true);
	}
}
