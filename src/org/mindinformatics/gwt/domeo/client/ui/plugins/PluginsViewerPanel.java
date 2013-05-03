/*
 * Copyright 2013 Massachusetts General Hospital
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.mindinformatics.gwt.domeo.client.ui.plugins;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IResizable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PluginsViewerPanel extends Composite implements IContentPanel, IResizable {

	private static final String TITLE = "Profiles and Add-ons";
	
	interface Binder extends UiBinder<VerticalPanel, PluginsViewerPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	//private Resources _resources;
	private IDomeo _domeo;
	private IContainerPanel _containerPanel;

	// Layout
	@UiField VerticalPanel main;
	@UiField TabLayoutPanel tabToolsPanel;
	
	private ComponentsPanel componentsPanel;
	private CurrentProfilePanel annotationPreferencesPanel;
	private AvailableProfilesPanel availableProfilesPanel;
	
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
	public PluginsViewerPanel(IDomeo domeo) {
		_domeo = domeo;
		//_resources = resources;
		//_listPanel = new LogListPanel(_application);

		// Create layout
		initWidget(binder.createAndBindUi(this)); 
		this.setWidth((Window.getClientWidth() - 140) + "px");
		
		tabToolsPanel.setWidth("840px");
		
		annotationPreferencesPanel = new CurrentProfilePanel(_domeo, this);
		tabToolsPanel.add(annotationPreferencesPanel, annotationPreferencesPanel.getTitle());
		
		availableProfilesPanel = new AvailableProfilesPanel(_domeo, this);
		tabToolsPanel.add(availableProfilesPanel, availableProfilesPanel.getTitle());
		
		componentsPanel = new ComponentsPanel(_domeo, this);
		tabToolsPanel.add(componentsPanel, componentsPanel.getTitle());
		
		tabToolsPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				// Ajax call for retrieving the available profiles
			}
		});
	}

	public void refreshCurrentProfile(boolean change) {
		annotationPreferencesPanel.refresh(change);
	}
	
	public void selectTab(int index) {
		tabToolsPanel.selectTab(index);
	}
	
	@Override
	public void resized() {
		this.setWidth((Window.getClientWidth() - 140) + "px");
	}
}

