package org.mindinformatics.gwt.domeo.client.ui.plugins;

import java.util.Comparator;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IResizable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.view.client.ListDataProvider;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ComponentsPanel extends Composite implements IContentPanel, IResizable {

	private static final String TITLE = "Basic Components";
	
	interface Binder extends UiBinder<FlowPanel, ComponentsPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	//private Resources _resources;
	private IDomeo _domeo;
	private IContainerPanel _containerPanel;
	@SuppressWarnings("unused")
	private PluginsViewerPanel _parent;

	// Layout
	@UiField FlowPanel componentsPanel;
	
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
	public ComponentsPanel(IDomeo domeo, PluginsViewerPanel parent) {
		_domeo = domeo;
		_parent = parent;
		//_resources = resources;
		//_listPanel = new LogListPanel(_application);

		// Create layout
		initWidget(binder.createAndBindUi(this)); 
		this.setWidth((Window.getClientWidth() - 140) + "px");
		
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
	    		PluginCard pluginCard = new PluginCard(plugin.getPluginName(), plugin.getType(), plugin.getSubType(), plugin.getVersion(), true, true);
		    	list.add(pluginCard);
	    	} 
	    }
	    
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
	    
	    componentsPanel.add(table);
	}
	
	@Override
	public void resized() {
		this.setWidth((Window.getClientWidth() - 140) + "px");
	}
}

