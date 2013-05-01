package org.mindinformatics.gwt.framework.component.ui.toolbar;

import java.util.HashSet;

import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ToolbarPanel extends Composite implements IInitializableComponent {

	private static final String RIGHT_CELL_WIDTH = "140";
	
	@UiTemplate("ToolbarPanel.ui.xml")
	interface Binder extends UiBinder<HorizontalPanel, ToolbarPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField HorizontalPanel toolbarPanel;
	@UiField HorizontalPanel leftPanel;
	@UiField HorizontalPanel rightPanel;
	
	private IApplication _application;
	
	private HashSet<ToolbarItemsGroup> groups = new HashSet<ToolbarItemsGroup>();
	private HashSet<IToolbarItem> items = new HashSet<IToolbarItem>();
	
	public ToolbarPanel(IApplication application) {
		_application = application;
		
		_application.getLogger().debug(this.getClass().getName(), 
			"Creating the Application Toolbar");
		
		initWidget(binder.createAndBindUi(this)); 
		
		toolbarPanel.setCellWidth(rightPanel,RIGHT_CELL_WIDTH);
		rightPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		
		
		_application.getLogger().debug(this.getClass().getName(), 
			"Creation of the Application Toolbar completed");
	}
	
	public void enableToolbarItems() {
		for(IToolbarItem item: items) {
			item.enable();
		}
	}
	
	public void disableToolbarItems() {
		for(IToolbarItem item: items) {
			item.disable();
		}
	}
	
	public void addToLeftPanel(Widget widget) {
		if(widget instanceof IToolbarItem) items.add((IToolbarItem)widget);
		leftPanel.add(widget);
	}
	
	public void addToLeftPanel(Widget widget, String width) {
		addToLeftPanel(widget);
		leftPanel.setCellWidth(widget,width);
	}
	
	public void addToRightPanel(Widget widget) {
		if(widget instanceof IToolbarItem) items.add((IToolbarItem)widget);
		rightPanel.add(widget);
	}
	
	public void addToRightPanel(Widget widget, String width) {
		addToRightPanel(widget);
		rightPanel.setCellWidth(widget,width);
	}
	
	public void registerGroup(ToolbarItemsGroup group) {
		groups.add(group);
	}
	
	public void attachGroup(String groupName) {
		for(ToolbarItemsGroup group:groups) {
			if(group.getGroupName().equals(groupName)) attachGroup(group);
		}
	}
	
	public void attachGroup(ToolbarItemsGroup group) {
		for(IToolbarItem item: group.getItems()) {
			addToLeftPanel((Widget) item);
		}
	}
	
	public void detachGroup(String groupName) {
		for(ToolbarItemsGroup group:groups) {
			if(group.getGroupName().equals(groupName)) {
				_application.getLogger().debug(this.getClass().getName(), 
						"Activating group: " + groupName);
				detachGroup(group);
			}
		}
	}
	
	public void detachGroup(ToolbarItemsGroup group) {
		for(IToolbarItem item: group.getItems()) {
			leftPanel.remove((Widget) item);
		}
	}
	
	public void hideGroup(String groupName) {
		for(ToolbarItemsGroup group: groups) {
			if(group.getGroupName().equals(groupName))  group.hide();
		}
	}

	@Override
	public void init() {
		for(IToolbarItem item: items) {
			if(item instanceof IInitializableComponent) ((IInitializableComponent)item).init();
		}
	}
}
