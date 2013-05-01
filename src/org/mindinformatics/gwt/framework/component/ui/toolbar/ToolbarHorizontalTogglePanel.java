package org.mindinformatics.gwt.framework.component.ui.toolbar;

import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.src.Application;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ToolbarHorizontalTogglePanel extends Composite  implements IToolbarItem, IInitializableComponent {
	
	@UiTemplate("ToolbarHorizontalPanel.ui.xml")
	interface Binder extends UiBinder<HorizontalPanel, ToolbarHorizontalTogglePanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField HorizontalPanel toolbarHorizontalPanel;
	
	private boolean status;
	
	private HTML labelPanel;
	private Image icon;
	private ImageResource iconWhenNotSelected;
	private ImageResource iconWhenSelected;
	
	private ClickHandler _clickHandler;
	private HandlerRegistration iconHandlerRegistration;
	private HandlerRegistration labelHandlerRegistration;
	
	public ToolbarHorizontalTogglePanel(IApplication application, ClickHandler clickHandler,
			ImageResource iconWhenNotSelected, ImageResource iconWhenSelected, String label, String tooltip) {

		_clickHandler = clickHandler;
		
		initWidget(binder.createAndBindUi(this)); 
		
		this.iconWhenSelected = iconWhenSelected;
		this.iconWhenNotSelected = iconWhenNotSelected;
		
		icon = new Image(iconWhenNotSelected);
		icon.setTitle(tooltip);
		icon.setHeight("24px");
		icon.setWidth("24px");
		setImageBackground("#EEEEEE");
		
		labelPanel = new HTML(label);
		labelPanel.setTitle(tooltip);
		
		toolbarHorizontalPanel.add(icon);
		toolbarHorizontalPanel.add(labelPanel);
		
		status = false;
		
		ClickHandler defClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (status) deselect(); else select();
			}
		};
		icon.addClickHandler(defClickHandler);
		labelPanel.addClickHandler(defClickHandler);
		
		enable();
	}
	
	public void setImageBackground(String color) {
		DOM.setStyleAttribute(icon.getElement(), "backgroundColor", color);
	}
	
	public boolean isSelected() {
		return status;
	}
	
	public void select() {
		status = true;
		labelPanel.setStyleName(Application.applicationResources.css().selectedLabel());
		icon.setResource(iconWhenSelected);
		if(icon.getHeight()<=16 && icon.getWidth()<=16)
			toolbarHorizontalPanel.setCellVerticalAlignment(icon, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	public void deselect() {
		status = false;
		labelPanel.setStyleName(Application.applicationResources.css().label());
		icon.setResource(iconWhenNotSelected);
		if(icon.getHeight()<=16 && icon.getWidth()<=16)
			toolbarHorizontalPanel.setCellVerticalAlignment(icon, HasVerticalAlignment.ALIGN_MIDDLE);
	}

	@Override
	public void enable() {
		iconHandlerRegistration  = icon.addClickHandler(_clickHandler);
		icon.setStyleName(Application.applicationResources.css().button());
		labelHandlerRegistration = labelPanel.addClickHandler(_clickHandler);
		labelPanel.setStyleName(Application.applicationResources.css().label());
		//if (status) deselect(); else select();
	}

	@Override
	public void disable() {
		iconHandlerRegistration.removeHandler();
		icon.setStyleName(Application.applicationResources.css().disabledButton());
		labelHandlerRegistration.removeHandler();
		if(status) labelPanel.setStyleName(Application.applicationResources.css().selectedLabel());
		else labelPanel.setStyleName(Application.applicationResources.css().disabledLabel());
	}

	@Override
	public void init() {
		deselect();
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
