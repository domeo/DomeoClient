package org.mindinformatics.gwt.framework.component.ui.toolbar;

import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ToolbarPopup extends PopupPanel {

	IApplication _application;
	
	VerticalPanel vp;
	
	public static final ToolbarPopupResources localResources = 
		GWT.create(ToolbarPopupResources.class);
	
	public ToolbarPopup(IApplication application, String title, String iconUrl) {
		super(true, true);
		
		_application = application;
		
		localResources.popupCss().ensureInjected();
		
		vp = new VerticalPanel();
		vp.add(getHeaderPanel(title, iconUrl));
		
		addDomHandler(new MouseOutHandler() {
		    public void onMouseOut(MouseOutEvent event) {
		        hide();
		    }
		}, MouseOutEvent.getType());
		
		this.setWidget(vp);
	}
	
	private HorizontalPanel getHeaderPanel(String title, String iconUrl) {
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setStyleName(localResources.popupCss().toolbarPopupTitlePanel());
		
		Label label = new Label(title);
		label.setStyleName(localResources.popupCss().toolbarPopupTitleLabel());
		hp.add(label);
		
		if(iconUrl!=null && iconUrl.trim().length()>0) {
			Image icon = new Image(iconUrl);
			hp.add(icon);
			hp.setCellHorizontalAlignment(icon, HasHorizontalAlignment.ALIGN_RIGHT);
		}
		return hp;
	}
	
	public void addButtonPanel(String label, ClickHandler clickHandler) {
		addButtonPanel("", label, clickHandler);
	}
	
	public void addButtonPanel(String iconUrl, String label, ClickHandler clickHandler) {
		HorizontalPanel hp = new HorizontalPanel();
		hp.setStyleName(localResources.popupCss().toolbarPopupButtonPanel());
		Button html = new Button(label);
		if(clickHandler!=null) html.addClickHandler(clickHandler);
		html.setStyleName(localResources.popupCss().toolbarPopupButtonLabel());
		hp.add(html);
		
		if(iconUrl!=null && iconUrl.trim().length()>0) {
			Image icon = new Image(iconUrl);
			icon.setHeight("20px");
			hp.add(icon);
			hp.setCellHorizontalAlignment(icon, HasHorizontalAlignment.ALIGN_RIGHT);
		}
		vp.add(hp);
	}
}
