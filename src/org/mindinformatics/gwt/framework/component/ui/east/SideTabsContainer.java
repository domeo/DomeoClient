package org.mindinformatics.gwt.framework.component.ui.east;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

/** 
 * Management of the side tabs.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class SideTabsContainer extends Composite {

	interface Binder extends UiBinder<VerticalPanel, SideTabsContainer> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField VerticalPanel sideBar;
	
	private IApplication _application;
	private SidePanelsFacade _sidePanelsFacade;
	
	private ArrayList<ASideTab> tabs;
	
	public SideTabsContainer(IApplication application, SidePanelsFacade sidePanelsFacade) {
		_application = application;
		_sidePanelsFacade = sidePanelsFacade;
		
		_application.getLogger().debug(this.getClass().getName(), 
			"Creating the sidebar");
		
		tabs = new ArrayList<ASideTab>();
		
		initWidget(binder.createAndBindUi(this)); 
	}
	
	/**
	 * Adds the new tab in the side bar.
	 * @param tab		New tab to add
	 * @param height	The height of the tab, it should be just bigger than the label
	 */
	public void addTab(ASideTab tab, String height) {
		tabs.add(tab);
		sideBar.add(tab);
		sideBar.setCellHeight(tab, height);
	}
	
	/**
	 * Removes the specified tab.
	 * @param tab	The tab to remove
	 */
	public void removeTab(ASideTab tab) {
		_sidePanelsFacade.toggleTab(tabs.get(0));
		sideBar.remove(tab);
		tabs.remove(tab);
	}
	
	/**
	 * Selects - if not already selected - or deselects - if already selected -
	 * the tab. 
	 * @param selectedTab	Tab to select/deselect
	 */
	public void selectTab(ASideTab selectedTab) {
		for(ASideTab tab: tabs) {
			if(tab==selectedTab) {
				if(!tab.isSelected()) 
					tab.selectTab();
			} else {
				tab.deselectTab();
			}
		}
	}
	
	/**
	 * Selects - if not already selected - or deselects - if already selected -
	 * the tab. 
	 * @param selectedTab	Tab to select/deselect
	 */
	public void toggleTab(ASideTab selectedTab) {
		for(ASideTab tab: tabs) {
			if(tab==selectedTab) {
				if(tab.isSelected()) {
					closeAllTabs();
					break;
				} else
					tab.selectTab();
			} else {
				tab.deselectTab();
			}
		}
	}
	
	private void closeAllTabs() {
		for(ASideTab tab: tabs) {
			tab.deselectTab();
		}
	}
}
