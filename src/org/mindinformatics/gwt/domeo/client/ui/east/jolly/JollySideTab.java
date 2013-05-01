package org.mindinformatics.gwt.domeo.client.ui.east.jolly;

import org.mindinformatics.gwt.framework.component.ui.east.ASideTab;
import org.mindinformatics.gwt.framework.component.ui.east.SidePanelsFacade;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JollySideTab extends ASideTab {
	
	public JollySideTab(IApplication application, SidePanelsFacade facade, String title, int size) {
		this(application, facade, title, size + "px");
	}
	
	public JollySideTab(IApplication application, SidePanelsFacade facade, 
			String label, String height) {
		super(application, facade, label, height, true);
		
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
