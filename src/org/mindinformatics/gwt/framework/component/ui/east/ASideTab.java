package org.mindinformatics.gwt.framework.component.ui.east;

import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public abstract class ASideTab extends Composite {

	interface Binder extends UiBinder<SimplePanel, ASideTab> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	interface SideTabStyle extends CssResource {
	    String outerSelected();
	    String outerNotSelected();
	  }

	@UiField SideTabStyle style;

	@UiField SimplePanel sideTab;
	@UiField Label sideBarLabel;
	
	protected boolean _closeable;
	protected String _title;
	protected IApplication _application;
	protected SidePanelsFacade _facade;
	private ClickHandler _clickSelectionHandler;
	private ClickHandler _clickDeselectionHandler;
	private HandlerRegistration _handlerRegistrationn;
	
	private boolean isSelected = false;
	
	public ASideTab(IApplication application, SidePanelsFacade facade, String label, String height, boolean closeable) {
		_closeable = closeable;
		_application = application;
		_facade = facade;
		_title = label;
		
		_application.getLogger().debug(this.getClass().getName(), 
			"Initializing the Application Sidebar");
		
		initWidget(binder.createAndBindUi(this)); 
		
		sideTab.setHeight(height);
		sideBarLabel.setText(label);
	}
	
	public String getTitle() {
		return _title;
	}
	
	public void addClickHandlers(ClickHandler clickSelectionHandler, ClickHandler clickDeselectionHandler) {
		_clickSelectionHandler = clickSelectionHandler;
		_clickDeselectionHandler = clickDeselectionHandler;
		_handlerRegistrationn = sideBarLabel.addClickHandler(clickSelectionHandler);
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	/**
	 * Returns if the tab is closaable. This is important for the 
	 * initialization of the component.
	 * @return	true if the tab can be removed.
	 */
	public boolean isCloseable() {
		return _closeable;
	}
	
	/**
	 * Marks this tab as deselected or closed
	 */
	public void deselectTab() {
		isSelected = false;
		_handlerRegistrationn.removeHandler();
		_handlerRegistrationn = sideBarLabel.addClickHandler(_clickSelectionHandler);
		sideTab.removeStyleName(style.outerSelected());
		sideTab.addStyleName(style.outerNotSelected());
	}
	
	/**
	 * Marks this tab as selected
	 */
	public void selectTab() {
		isSelected = true;
		_handlerRegistrationn.removeHandler();
		_handlerRegistrationn = sideBarLabel.addClickHandler(_clickDeselectionHandler);
		sideTab.removeStyleName(style.outerNotSelected());
		sideTab.addStyleName(style.outerSelected());
	}
}
