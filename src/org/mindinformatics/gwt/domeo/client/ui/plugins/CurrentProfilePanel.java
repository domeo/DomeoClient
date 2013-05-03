package org.mindinformatics.gwt.domeo.client.ui.plugins;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;
import org.mindinformatics.gwt.framework.component.profiles.model.IProfile;
import org.mindinformatics.gwt.framework.component.profiles.model.MProfile;
import org.mindinformatics.gwt.framework.component.profiles.src.testing.IUpdateProfileCallback;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IResizable;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CurrentProfilePanel extends Composite implements IContentPanel, IResizable, IUpdateProfileCallback {

	private static final String TITLE = "Current Profile";
	
	interface Binder extends UiBinder<FlowPanel, CurrentProfilePanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	//private Resources _resources;
	private IDomeo _domeo;
	private IContainerPanel _containerPanel;
	private PluginsViewerPanel _parent;

	// Layout
	@UiField ScrollPanel pluginsPanel;
	@UiField HorizontalPanel titlePanel;
	@UiField Label profileDescription;
	@UiField Label profileName;
	@UiField Label profileDate;
	@UiField Label profileCreator;
	@UiField Label footerMessage;
	@UiField Button saveButton;
	@UiField Button saveAsButton;
	@UiField Button saveAsCurrentButton;
	@UiField SimplePanel progressIconPanel;
	
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
	public CurrentProfilePanel(IDomeo domeo, PluginsViewerPanel parent) {
		_domeo = domeo;
		_parent = parent;
		//_resources = resources;
		//_listPanel = new LogListPanel(_application);

		// Create layout
		initWidget(binder.createAndBindUi(this)); 
		this.setWidth((Window.getClientWidth() - 140) + "px");
		final IUpdateProfileCallback _this = this;
		saveButton.setEnabled(false);
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				progressIconPanel.add(new Image(Domeo.resources.littleProgressIcon()));
				MProfile newProfile = new MProfile();
				newProfile.setName(_domeo.getProfileManager().getUserCurrentProfile().getName());
				newProfile.setUuid(_domeo.getProfileManager().getUserCurrentProfile().getUuid());
				_domeo.getProfileManager().saveUserProfile(newProfile, _this);
			}
		});

		saveAsButton.setEnabled(false);
		saveAsButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String newName = Window.prompt("New profile name", "");
				if(newName!=null) {
					progressIconPanel.add(new Image(Domeo.resources.littleProgressIcon()));
					MProfile newProfile = new MProfile();
					newProfile.setName(newName);
					newProfile.setUuid(_domeo.getProfileManager().getUserCurrentProfile().getUuid());
					_domeo.getProfileManager().saveUserProfile(newProfile, _this);
				}
			}
		});
		saveAsCurrentButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_parent.selectTab(1);
			}
		});
		refresh(false);
	}
	
	public void refresh(boolean change) {
		
		if(change) {
			refreshMessagePanel(true);
			saveAsCurrentButton.setEnabled(true);
		}

		 // Create a CellTable.
	    final CellTable<PluginCard> table = new CellTable<PluginCard>();
	    
		// Create name column.
		TextColumn<PluginCard> nameColumn = new TextColumn<PluginCard>() {
			@Override
			public String getValue(PluginCard pluginCard) {
				return pluginCard.name;
			}
		};

		// Make the name column sortable.
		nameColumn.setSortable(true);

		// Create type column.
		TextColumn<PluginCard> typeColumn = new TextColumn<PluginCard>() {
			@Override
			public String getValue(PluginCard pluginCard) {
				return pluginCard.type;
			}
		};
		typeColumn.setSortable(true);
		
		// Create sub type column.
		TextColumn<PluginCard> subTypeColumn = new TextColumn<PluginCard>() {
			@Override
			public String getValue(PluginCard pluginCard) {
				return pluginCard.subType;
			}
		};
		Column<PluginCard, Boolean> selectColumn = new Column<PluginCard, Boolean>(new CheckboxCell()) {
	        @Override
	        public Boolean getValue(PluginCard object) {
	        	//if(object.mandatory) return true; 
	            return object.selected;
	        }
	    };
		selectColumn.setFieldUpdater(new FieldUpdater<PluginCard, Boolean>() {
			public void update(int index, PluginCard object, Boolean value) {
				// Called when the user clicks on a checkbox.
				//selectionModel.setSelected(object, value);
				object.selected = value;
				if(!value) _domeo.getProfileManager().getUserCurrentProfile().addPluginPreference(object.name, MProfile.PLUGIN_DISABLED);
				else _domeo.getProfileManager().getUserCurrentProfile().addPluginPreference(object.name, MProfile.PLUGIN_ENABLED);
				refreshMessagePanel(true);
			}
		});
		selectColumn.setSortable(true);
		table.addColumn(selectColumn, "Enabled");
		
		// Create sub type column.
		TextColumn<PluginCard> versionColumn = new TextColumn<PluginCard>() {
			@Override
			public String getValue(PluginCard pluginCard) {
				return pluginCard.version;
			}
		};

	    // Add the columns.
	    table.addColumn(typeColumn, "Type");
	    table.addColumn(subTypeColumn, "Sub Type");
	    table.addColumn(nameColumn, "Name");
	    table.addColumn(versionColumn, "Version");

	    // Create a data provider.
	    ListDataProvider<PluginCard> dataProvider = new ListDataProvider<PluginCard>();

	    // Connect the table to the data provider.
	    dataProvider.addDataDisplay(table);

	    // Add the data to the data provider, which automatically pushes it to the
	    // widget.
	    List<PluginCard> list = dataProvider.getList();
	    for (APlugin plugin : _domeo.getPluginsManager().getPluginsList()) {
	    	if(plugin.getMandatory()){
	    		//PluginCard pluginCard = new PluginCard(plugin.getPluginName(), plugin.getType(), plugin.getSubType(), true, true);
		    	//list.add(pluginCard);
	    	} else if(_domeo.getProfileManager().getUserCurrentProfile().isPluginEnabled(plugin.getPluginName())) {
		    	PluginCard pluginCard = new PluginCard(plugin.getPluginName(), plugin.getType(), plugin.getSubType(), plugin.getVersion(), false, true);
		    	list.add(pluginCard);
	    	} else {
	    		PluginCard pluginCard = new PluginCard(plugin.getPluginName(), plugin.getType(), plugin.getSubType(), plugin.getVersion(), !plugin.getMandatory(), false);
		    	list.add(pluginCard);
	    	}
	    }

	    profileName.setText(_domeo.getProfileManager().getUserCurrentProfile().getName());
	    profileDescription.setText(_domeo.getProfileManager().getUserCurrentProfile().getDescription());
	    DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yy h:mma");
	    profileDate.setText(fmt.format(_domeo.getProfileManager().getUserCurrentProfile().getLastSavedOn()));

	    if(_domeo.getProfileManager().getUserCurrentProfile().getLastSavedBy()!=null) 
	    	profileCreator.setText(_domeo.getProfileManager().getUserCurrentProfile().getLastSavedBy().getName());
	    	
	    
	    ListHandler<PluginCard> nameColumnSortHandler = new ListHandler<PluginCard>(list);
	    nameColumnSortHandler.setComparator(nameColumn,
	        new Comparator<PluginCard>() {
	          public int compare(PluginCard o1, PluginCard o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the name columns.
	            if (o1 != null) {
	              return (o2 != null) ? o1.name.compareTo(o2.name) : 1;
	            }
	            return -1;
	          }
	        });
	    table.addColumnSortHandler(nameColumnSortHandler);
	    
	    ListHandler<PluginCard> typeColumnSortHandler = new ListHandler<PluginCard>(list);
	    typeColumnSortHandler.setComparator(typeColumn,
	        new Comparator<PluginCard>() {
	          public int compare(PluginCard o1, PluginCard o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the name columns.
	            if (o1 != null) {
	              return (o2 != null) ? o1.type.compareTo(o2.type) : 1;
	            }
	            return -1;
	          }
	        });
	    table.addColumnSortHandler(typeColumnSortHandler);
	    _domeo.getLogger().debug(this, "1p");
	    ListHandler<PluginCard> selectColumnSortHandler = new ListHandler<PluginCard>(list);
		selectColumnSortHandler.setComparator(selectColumn,
	        new Comparator<PluginCard>() {
	          public int compare(PluginCard o1, PluginCard o2) {
	            if (o1.mandatory == o2.mandatory) {
	              return 0;
	            }

	            // Compare the name columns.
	            if (o1 != null) {
	              return (o2 != null) ? ((o1.mandatory && !(o2.mandatory))? -1:1) : 1;
	            } 
	            return -1;
	          }
	        });
		table.addColumnSortHandler(selectColumnSortHandler);
		
		pluginsPanel.clear();
		pluginsPanel.add(table);
	}
	

	static <T> DefaultSelectionEventManager<T> myMethod(int column) {
	   //call whatever functions you want
	   return DefaultSelectionEventManager.<T> createCheckboxManager(column);
	}



	@Override
	public void resized() {
		this.setWidth((Window.getClientWidth() - 140) + "px");
	}
	
	public void refreshMessagePanel(boolean fromLoading) {
		if(fromLoading) {
			if(_domeo.getProfileManager().getUserCurrentProfile().isChanged()) {
				footerMessage.setText("*The profile has to be saved to be effective");
	
				saveAsCurrentButton.setEnabled(false);
				saveButton.setEnabled(true);
				saveAsButton.setEnabled(true);
			}
		} else {
			if(_domeo.getProfileManager().getUserCurrentProfile().isChanged()) {
				footerMessage.setText("*Changes will be made available at the next Domeo reload.");
	
				// TODO Check if same user of original profile. If not just SaveAs
				Window.alert(_domeo.getProfileManager().getUserCurrentProfile().getLastSavedBy().getUri());
				Window.alert(_domeo.getAgentManager().getUserPerson().getUri());
				
				saveAsCurrentButton.setEnabled(false);
				saveButton.setEnabled(true);
				saveAsButton.setEnabled(true);
			} else {
				footerMessage.setText("*Changes will be made available at the next Domeo reload.");
				
			}
		}
	}

	@Override
	public void updateCurrentProfile() {
		profileName.setText(_domeo.getProfileManager().getUserCurrentProfile().getName());
		DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yy h:mma");
		profileDate.setText(fmt.format(_domeo.getProfileManager().getUserCurrentProfile().getLastSavedOn()));
		profileCreator.setText(_domeo.getProfileManager().getUserCurrentProfile().getLastSavedBy().getName());
		
		refresh(false);
		progressIconPanel.clear();
  	  	saveButton.setEnabled(false);
  	  	saveAsButton.setEnabled(false);
		footerMessage.setText("Profile saved!");
		Timer t = new Timer() {
			public void run() {
				refreshMessagePanel(false);
			}
		};
		t.schedule(1500);
		
		
	}

	@Override
	public HashMap<String, String> getPluginsStatus() {
		HashMap<String, String> pluginsStatus = new HashMap<String, String>();
		for (APlugin plugin : _domeo.getPluginsManager().getPluginsList()) {
			if(plugin.getMandatory()){
	    	} else if(_domeo.getProfileManager().getUserCurrentProfile().isPluginEnabled(plugin.getPluginName())) {
				pluginsStatus.put(plugin.getPluginName(), IProfile.PLUGIN_ENABLED);
	    	} else {
	    		pluginsStatus.put(plugin.getPluginName(), IProfile.PLUGIN_DISABLED);
	    	}	 
		}
		return pluginsStatus;
	}
}

