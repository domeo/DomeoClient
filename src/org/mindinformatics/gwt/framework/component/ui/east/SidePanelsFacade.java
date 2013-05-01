package org.mindinformatics.gwt.framework.component.ui.east;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.user.client.ui.DockLayoutPanel;

/**
 * This facade is grouping all the features necessary for managing the
 * side panels of the application. The tabs and the content panels are 
 * managed by two separate UI components. 
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class SidePanelsFacade implements IInitializableComponent {

	/**
	 * Defines the width of the side panels when open
	 */
	private static final int SIDE_PANEL_WIDTH = 500;
	/**
	 * Defines the width of the side panels when closed
	 */
	private static final int SIDE_PANEL_CLOSED_WIDTH = 35;
	
	private IApplication _application;
	private DockLayoutPanel _layout;
	
	private SideTabsContainer sideTabsContainer; 
	private SidePanelsContainer sidePanelsContainer;
	private HashMap<ASideTab, ASidePanel> sideTabsPanelsMap;
	
	private boolean isActive = false;
	
	/**
	 * The facade manages most of the aspects related to the side panels.
	 * @param application	The originating application
	 * @param layout		The dock layout of the application
	 */
	public SidePanelsFacade(IApplication application, DockLayoutPanel layout) {
		_application = application;
		_layout = layout;
		
		sideTabsPanelsMap = new HashMap<ASideTab, ASidePanel>();
		sideTabsContainer = new SideTabsContainer(_application, this);
		sidePanelsContainer = new SidePanelsContainer(_application);
	}
	
	@Override
	public void init() {
		_application.getLogger().debug(this, "Initializing...");
		// All the tabs that can be closed are destroyed
		// They are usually viewers or context dependent
		destroyCloseableTabs();
	}
		
	/**
	 * Removes all the closeable tabs (initialization)
	 */
	private void destroyCloseableTabs() {
		try {
			Set<ASideTab> tabsToRemove = new HashSet<ASideTab>();
			for(ASideTab tab: sideTabsPanelsMap.keySet()) {
				if(sideTabsPanelsMap.get(tab) instanceof ASidePanel && tab.isCloseable()) 
					tabsToRemove.add(tab);
			}
			for(ASideTab tabToRemove: tabsToRemove) {
				((ASidePanel)sideTabsPanelsMap.get(tabToRemove)).destroy();
			}
		} catch (Exception e) {
			_application.getLogger().exception(this, "Initalization cleanup failed " + e.getMessage());
		}
	}
	
	/**
	 * Registers a tab and its panel in the container
	 * displaying on the right side of the UI. The content panel is
	 * also registered within the application component manager.
	 * @param tab		The tab component of the panel
	 * @param sidePanel	The panel content
	 * @return	Returns true if the tab already exists.
	 */
	public ASideTab registerSideComponent(ASideTab tab, ASidePanel sidePanel, String height) {
		try {
			_application.getLogger().info(this, "Registration of side component " + tab.getTitle());
			
			// If the side panel is already present it will be returned
			Object existingTab = isPanelAlreadyOpen(sidePanel);
			if(existingTab!=null) return (ASideTab) existingTab; 
			
			// Registration of the content component
			_application.getComponentsManager().addComponent(sidePanel);
			
			registerSideTab(tab, sidePanel);
			sideTabsContainer.addTab(tab, height);
			return tab;
		} catch(Exception e) {
			_application.getLogger().exception(this, "Registration of side component " + tab.getTitle() + 
				" failed " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Unregisters a tab and its panel from the right panels container.
	 * @param tab	The tab to be removed. The correspondent panel is kept track of.        
	 */
	public void unregisterSideComponent(ASideTab tab) {
		try {
			_application.getLogger().command(this, "Unregistration of side component " + tab.getTitle());
			
			// Removal of the content component
			_application.getComponentsManager().removeComponent(getPanelForTab(tab));
			
			unregisterSideTab(tab);
		} catch(Exception e) {
			_application.getLogger().exception(this, "Unregistration of side component " + tab.getTitle() + 
				" failed " + e.getMessage());
		}
	}
	
	/**
	 * Removes a tab from the list of visible tabs.
	 * @param tab	The tab to remove.
	 */
	public void removeSideTab(ASideTab tab) {
		try {
			_application.getLogger().debug(this, "Removing side component " + tab.getTitle());
	
			sideTabsContainer.removeTab(tab);
		} catch(Exception e) {
			_application.getLogger().exception(this, "Removal of side component " + tab.getTitle() + 
				" failed " + e.getMessage());
		}
	}
	
	/**
	 * Perform the registration of a tab and the related content panel.
	 * Registration is not equivalent to displaying the component.
	 * @param tab		The tab
	 * @param sidePanel	The content panel
	 * @return Returns true if the panel is not present and therefore inserted.
	 */
	private boolean registerSideTab(ASideTab tab, ASidePanel sidePanel) {
		if(sideTabsPanelsMap.containsKey(tab)) return false;
		sideTabsPanelsMap.put(tab, sidePanel);
		return true;
	}
	
	/**
	 * Removes the tab - and the related content panel - from the map of
	 * all those that are available.
	 * @param tab	The tab to be removed
	 */
	private void unregisterSideTab(ASideTab tab) {
		sideTabsPanelsMap.remove(tab);
	}
	
	/**
	 * Returns the content panel for a specific tab. Or an HTML message
	 * if no panel has been found.
	 * @param tab	The tab of interest
	 * @return	The content panel associated to the specified tab.
	 */
	public ASidePanel getPanelForTab(ASideTab tab) {
		if(sideTabsPanelsMap.containsKey(tab)) return sideTabsPanelsMap.get(tab);
		return null;
	}
	
	/**
	 * Select the tab and opens the content panel associated with the given tab.
	 * @param originatingTab	The tab to open
	 */
	public void selectTab(ASideTab tab) {
		if(!isActive) return; 
		try {
			_application.getLogger().command(this, "Selection of side component " + tab.getTitle());
			sideTabsContainer.selectTab(tab);
			sidePanelsContainer.open(getPanelForTab(tab), SIDE_PANEL_WIDTH);
			_layout.setWidgetSize(sidePanelsContainer, SIDE_PANEL_WIDTH);
		} catch(Exception e) {
			_application.getLogger().exception(this, "Selection of side component " + tab.getTitle() + 
				" failed " + e.getMessage());
		}
	}
	
	/**
	 * Select the tab and opens the content panel associated with the given tab.
	 * @param originatingTab	The tab to open
	 */
	public void toggleTab(ASideTab tab) {
		if(!isActive) return; 
		try {
			_application.getLogger().command(this, "Selection of side component " + tab.getTitle());
			sideTabsContainer.toggleTab(tab);
			sidePanelsContainer.open(getPanelForTab(tab), SIDE_PANEL_WIDTH);
			_layout.setWidgetSize(sidePanelsContainer, SIDE_PANEL_WIDTH);
		} catch(Exception e) {
			_application.getLogger().exception(this, "Selection of side component " + tab.getTitle() + 
				" failed " + e.getMessage());
		}
	}
	
	/**
	 * Closes the side panel. The status is recorded by the currentSideTab 
	 * variable.
	 */
	public void closeSidePanel(ASideTab tab) {
		if(!isActive) return; 
		try {
			_application.getLogger().command(this, "Closing side component " + tab.getTitle());
			sideTabsContainer.toggleTab(tab);
			_layout.setWidgetSize(sidePanelsContainer, SIDE_PANEL_CLOSED_WIDTH);
			sidePanelsContainer.close();
		} catch(Exception e) {
			_application.getLogger().exception(this, "Closing of side component " + tab.getTitle() + 
				" failed " + e.getMessage());
		}
	}
		
	/**
	 * Checks if the same side panel type - class name -, with the same content 
	 * - through the method getComparisonContentObject - is already open. If it 
	 * is, it returns the correspondent tab. 
	 * @param sidePanel 	The side panel to be checked
	 * @return The existing tab or null
	 */
	public ASideTab isPanelAlreadyOpen(ASidePanel sidePanel) {
		for(ASideTab tab: sideTabsPanelsMap.keySet()) {
			if(sideTabsPanelsMap.get(tab).getClass().getName().equals(sidePanel.getClass().getName()) && 
					sideTabsPanelsMap.get(tab) instanceof ASidePanel)
				if(((ASidePanel)sideTabsPanelsMap.get(tab)).isSidePanelAlreadyOpen(sidePanel.getComparisonContentObject())) return tab;
		}
		return null;
	}
	
	// Getters 
	public SideTabsContainer getSideTabsContainer() {
		return sideTabsContainer;
	}

	public SidePanelsContainer getSidePanelsContainer() {
		return sidePanelsContainer;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void setActive(boolean flag) {
		isActive = flag;
	}
}
