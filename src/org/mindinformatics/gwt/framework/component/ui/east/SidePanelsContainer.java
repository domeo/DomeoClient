package org.mindinformatics.gwt.framework.component.ui.east;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class SidePanelsContainer extends Composite {

	interface Binder extends UiBinder<HorizontalPanel, SidePanelsContainer> { }	
	private static final Binder binder = GWT.create(Binder.class);

	@UiField HorizontalPanel sidePanel;
	@UiField VerticalPanel contentPanel;
	
	@UiField SimplePanel heatMap;
	
	Canvas canvas;
	
	private IApplication _application;
	
	public SidePanelsContainer(IApplication application) {
		_application = application;
		
		
		_application.getLogger().debug(this.getClass().getName(), 
			"Creating the sidepanel");
		
		initWidget(binder.createAndBindUi(this)); 
		
		CanvasWrapper canvasWrapper = new CanvasWrapper((IDomeo)application);
		canvas = canvasWrapper.getCanvas();   
		heatMap.add(canvas);
	}
	
	/**
	 * Visualizes a side content panel.
	 * @param widget	The content panel
	 * @param width		The width of the content panel
	 */
	public void open(Widget widget, int width) {
		contentPanel.clear();
		contentPanel.add(widget);
		contentPanel.setWidth((width-35) + "px");
		sidePanel.setCellWidth(contentPanel, width + "px");
		
	}
	
	/**
	 * Closes whatever panel is open
	 */
	public void close() {
		contentPanel.clear();
		contentPanel.setWidth("0px");
		sidePanel.setCellWidth(contentPanel, "0px");
	}
}
