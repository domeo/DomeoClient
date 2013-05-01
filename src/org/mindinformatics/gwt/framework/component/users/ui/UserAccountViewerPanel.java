package org.mindinformatics.gwt.framework.component.users.ui;

import java.util.ArrayList;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.framework.component.users.model.MUserGroup;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;
import org.mindinformatics.gwt.framework.model.users.IUserRole;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IResizable;
import org.mindinformatics.gwt.utils.src.ResourcesUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class UserAccountViewerPanel extends Composite implements IContentPanel, IResizable {

	private static final String TITLE = "Account";
	
	interface Binder extends UiBinder<VerticalPanel, UserAccountViewerPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	//private Resources _resources;
	private IApplication _application;
	private IContainerPanel _containerPanel;
	private ArrayList<IUserGroup> _groups;
	
	// Layout
	@UiField VerticalPanel main;
	@UiField TabLayoutPanel tabToolsPanel;
	@UiField SimplePanel picturePanel;
	@UiField Label titlePanel;
	@UiField Label firstnamePanel;
	@UiField Label middlenamePanel;
	@UiField Label lastnamePanel;
	@UiField Label emailPanel;
	@UiField Anchor userLink;
	
	@UiField FlowPanel groupsPanel;
	@UiField FlowPanel groupDetails;
	@UiField Label namePanel;
	@UiField Label descriptionPanel;
	@UiField Label permissionPanel;
	@UiField Anchor groupLink;
	@UiField Label rolePanel;
	@UiField FlowPanel infoPanel;
	
	@UiField HorizontalPanel footerPanel;
	
	public void setContainer(IContainerPanel containerPanel) {
		_containerPanel = containerPanel;
	}
	
	public IContainerPanel getContainer() {
		return _containerPanel;
	}
	
	public String getTitle() {
		return TITLE;
	}	
	
	// ------------------------------------------------------------------------
	//  CREATION OF ANNOTATIONS OF VARIOUS KIND
	// ------------------------------------------------------------------------
	public UserAccountViewerPanel(IApplication application) {
		_application = application;
		//_resources = resources;
		//_listPanel = new LogListPanel(_application);

		// Create layout
		initWidget(binder.createAndBindUi(this)); 
		this.setWidth((Window.getClientWidth() - 140) + "px");

		if(_application.getAgentManager().getUserPerson().getPicture()!=null) {
			picturePanel.add(ResourcesUtils.getImage(_application.getLogger(), _application.getAgentManager().getUserPerson().getPicture(), 
					Domeo.resources.unknownPersonIcon()));
		} else {
			picturePanel.add(new Image(Domeo.resources.unknownPersonIcon()));
		}
		
		titlePanel.setText(_application.getAgentManager().getUserPerson().getTitle());
		firstnamePanel.setText(_application.getAgentManager().getUserPerson().getFirstName());
		middlenamePanel.setText(_application.getAgentManager().getUserPerson().getMiddleName());
		lastnamePanel.setText(_application.getAgentManager().getUserPerson().getLastName());
		emailPanel.setText(_application.getAgentManager().getUserPerson().getEmail());
		
		userLink.setText("More info");
		userLink.setTarget("_blank");
		userLink.setHref(_application.getAgentManager().getUserPerson().getUri());
		
		final ListBox groupsList = new ListBox();
		groupsList.setWidth("260px");
		groupsList.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int selectedIndex = groupsList.getSelectedIndex();
				displayGroupInfo(selectedIndex);
			}
		});
		
		retrieveUsersGroups();
		groupsList.setVisibleItemCount(10);
		int counter = 0;
		for(IUserGroup group: _groups) {
			if(counter++ == 0) {
				groupsList.setSelectedIndex(0);
				displayGroupInfo(0);
			}
			groupsList.addItem(group.getDescription(), group.getUri());
		}
		groupsList.setSelectedIndex(0);
		groupsPanel.add(groupsList);
		
		// https://developers.google.com/chart/interactive/docs/gallery
		// https://google-developers.appspot.com/chart/interactive/docs/gallery/linechart
		//LineChart pie = new LineChart(createTable(), createOptions());

	        //pie.addSelectHandler(createSelectHandler(pie));
		//infoPanel.add(pie);

	}
	
	/*
	 private Options createOptions() {
		    Options options = Options.create();
		    options.setWidth(400);
		    options.setHeight(240);
		    //options.set3D(true);
		    options.setTitle("My Activity");
		    return options;
		  }

	 private AbstractDataTable createTable() {
		    DataTable data = DataTable.create();
		    data.addColumn(ColumnType.STRING, "Task");
		    data.addColumn(ColumnType.NUMBER, "Hours per Day");
		    data.addRows(2);
		    data.setValue(0, 0, "Work");
		    data.setValue(0, 1, 14);
		    data.setValue(1, 0, "Sleep");
		    data.setValue(1, 1, 10);
		    return data;
		  }
*/
	
	public void retrieveUsersGroups() {
		Set<IUserGroup> groups = _application.getUserManager().getUsersGroups();
		_groups = new ArrayList<IUserGroup>();
		if(groups!=null) _groups.addAll(groups);
	}
	
	public void displayGroupInfo(Integer groupIndex) {
		showGroupDetails(_groups.get(groupIndex));
	}
		
	public void showGroupDetails(IUserGroup group) {
		namePanel.setText(group.getName());
		descriptionPanel.setText(group.getDescription());

		if(group.isReadPermission() && group.isWritePermission()) 
			permissionPanel.setText("Permissions: Read and write");
		else if(group.isReadPermission() && !group.isWritePermission()) 
			permissionPanel.setText("Permissions: Read only");
		else
			permissionPanel.setText("Permissions unknown (!!!)");
		
		groupLink.setText("More info");
		groupLink.setTarget("_blank");
		groupLink.setHref(group.getGroupLink());
		boolean firstFlag = false;
		StringBuffer sb = new StringBuffer();
		if(group instanceof MUserGroup) {
			for(IUserRole role: ((MUserGroup)group).getRoles()) {
				if(firstFlag) sb.append(", ");
				sb.append(role.getName());
				firstFlag = true;
			}
		}
		rolePanel.setText("Roles: " + sb.toString());
	}
	
	public void hideFooter() {
		footerPanel.setVisible(false);
	}
	
	public void showProgressMessage() {
		
	}
	
	public void showCompletionMessage() {
		
	}

	@Override
	public void resized() {
		this.setWidth((Window.getClientWidth() - 140) + "px");
		tabToolsPanel.setWidth((Window.getClientWidth() - 130) + "px");
	}
}

