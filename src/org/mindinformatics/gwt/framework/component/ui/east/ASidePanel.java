package org.mindinformatics.gwt.framework.component.ui.east;

import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.user.client.ui.Composite;

/**
 * Abstract class that should be used as basis for every side content panel.
 *
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public abstract class ASidePanel extends Composite {

	/**
	 * Tab associated to this side content panel
	 */
	protected IApplication _application;
	protected ASideTab _tab;
	protected SidePanelsFacade _facade;
	
	public ASidePanel(IApplication application, SidePanelsFacade facade, ASideTab tab) {
		_application = application;
		_facade = facade;
		_tab = tab;
	}
	
	public Object getComparisonContentObject() {
		return null;
	}
	
	public boolean isSidePanelAlreadyOpen(Object obj) {
		return false;
	}
	
	/**
	 * Remove completely the tab.
	 */
	public void destroy() {
		_facade.unregisterSideComponent(_tab);
		_facade.removeSideTab(_tab);
	}
}
