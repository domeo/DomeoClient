package org.mindinformatics.gwt.domeo.client.ui.east.debug;

import org.mindinformatics.gwt.framework.component.ui.east.ASideTab;
import org.mindinformatics.gwt.framework.component.ui.east.SidePanelsFacade;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationDebugSideTab extends ASideTab {

	private static final String TITLE = "Debug";
	public static final Integer HEIGHT = 50;
	private static final String HEIGHT_PX = HEIGHT + "px";
	
	public AnnotationDebugSideTab(IApplication application, SidePanelsFacade facade) {
		this(application, facade, TITLE, HEIGHT_PX);
	}
	
	public AnnotationDebugSideTab(IApplication application, SidePanelsFacade facade, 
			String label, String height) {
		super(application, facade, label, height, false);
		
		final ASideTab _this = this;
		
		this.addClickHandlers(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_facade.toggleTab(_this);
			}
		}, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_facade.closeSidePanel(_this);
			}
		});
	}
}
