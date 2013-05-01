package org.mindinformatics.gwt.framework.component.ui.buttons;

import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class SimpleIconButtonPanel extends Composite  {
	
	@UiTemplate("SimpleIconButtonPanel.ui.xml")
	interface Binder extends UiBinder<SimplePanel, SimpleIconButtonPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField SimplePanel simpleButtonPanel;
	
	private Image img;
	
	public SimpleIconButtonPanel(IApplication application, ClickHandler clickHandler,
			String icon, String tooltip) {

		initWidget(binder.createAndBindUi(this)); 
		
		img = new Image(icon);
		img.setTitle(tooltip);
		img.addClickHandler(clickHandler);

		simpleButtonPanel.add(img);
	}
}
