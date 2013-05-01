package org.mindinformatics.gwt.domeo.client.ui.preferences.styles;

import java.util.Comparator;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;
import org.mindinformatics.gwt.domeo.client.ui.plugins.PluginCard;
import org.mindinformatics.gwt.domeo.client.ui.preferences.EnhancedCheckboxCell;
import org.mindinformatics.gwt.domeo.client.ui.preferences.PreferencesViewerPanel;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IResizable;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class StylePreferencesPanel extends Composite implements IContentPanel, IResizable {

	private static final String TITLE = "Style";
	
	interface Binder extends UiBinder<FlowPanel, StylePreferencesPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	//private Resources _resources;
	private IDomeo _domeo;
	private IContainerPanel _containerPanel;
	@SuppressWarnings("unused")
	private PreferencesViewerPanel _parent;

	// Layout
	@UiField FlowPanel pluginsPanel;
	
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
	public StylePreferencesPanel(IDomeo domeo, PreferencesViewerPanel parent) {
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
		
		Column<PluginCard, PluginCard> selectColumn = new Column<PluginCard, PluginCard>(
				new EnhancedCheckboxCell()) { //CheckboxCell
			@Override
			public PluginCard getValue(PluginCard object) {
				return object;
			}
		};

		selectColumn.setFieldUpdater(new FieldUpdater<PluginCard, PluginCard>() {
			public void update(int index, PluginCard object, PluginCard value) {
				// Called when the user clicks on a checkbox.
				//selectionModel.setSelected(object, value);
				Window.alert(object.type);
			}
		});
		selectColumn.setSortable(true);
		table.addColumn(selectColumn, "Enabled");
		
		
	   

	    // Add the columns.
	    table.addColumn(typeColumn, "Type");
	    table.addColumn(subTypeColumn, "Sub Type");
	    table.addColumn(nameColumn, "Name");

	    // Create a data provider.
	    ListDataProvider<PluginCard> dataProvider = new ListDataProvider<PluginCard>();

	    // Connect the table to the data provider.
	    dataProvider.addDataDisplay(table);

	    // Add the data to the data provider, which automatically pushes it to the
	    // widget.
	    List<PluginCard> list = dataProvider.getList();
	    for (APlugin pluginName : _domeo.getPluginsManager().getPluginsList()) {
	    	PluginCard pluginCard = new PluginCard(pluginName.getPluginName(), pluginName.getType(), pluginName.getSubType(), pluginName.getVersion(), !pluginName.getMandatory(), true);
	    	list.add(pluginCard);
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
	    
	    ListHandler<PluginCard> selectColumnSortHandler = new ListHandler<PluginCard>(list);
		 selectColumnSortHandler.setComparator(selectColumn,
		        new Comparator<PluginCard>() {
		          public int compare(PluginCard o1, PluginCard o2) {
		            if (o1.selected == o2.selected) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		              return (o2 != null) ? (o1.selected && !(o2.selected))? -1:1 : 1;
		            }
		            return -1;
		          }
		        });
		    table.addColumnSortHandler(selectColumnSortHandler);

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
	

	/*
	public class DisableableCheckboxCell extends AbstractEditableCell<PluginCard, Boolean> {
	  private static final SafeHtml INPUT_CHECKED = SafeHtmlUtils.fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\" checked/>");
	  private static final SafeHtml INPUT_UNCHECKED = SafeHtmlUtils.fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\"/>");
	  private static final SafeHtml INPUT_CHECKED_DISABLED = SafeHtmlUtils.fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\" checked disabled=\"disabled\"/>");
	  private static final SafeHtml INPUT_UNCHECKED_DISABLED = SafeHtmlUtils.fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\" disabled=\"disabled\"/>");

	/** You'd copy the rest of the code from CheckboxCell, or implement the other required functions yourself **/

	/*
	  @Override
	  public void render(Context context, PluginCard value, SafeHtmlBuilder sb) {
	    // Get the view data.
	    Object key = context.getKey();
	    PluginCard viewData = getViewData(key);
	    if (viewData != null && viewData.equals(value)) {
	      clearViewData(key);
	      viewData = null;
	    }

	    PluginCard relevantValue = viewData != null ? viewData : value;
	    boolean checked = relevantValue.shouldBeChecked();
	    boolean enabled = relevantValue.shouldBeEnabled();

	    if (checked && !enabled)) {
	      sb.append(INPUT_CHECKED_DISABLED);
	    } else if (!checked && !enabled) {
	      sb.append(INPUT_UNCHECKED_DISABLED);
	    } else if (checked && enabled) {
	      sb.append(INPUT_CHECKED);
	    } else if (!checked && enabled) {
	      sb.append(INPUT_UNCHECKED);
	  }
	}
	}
	*/
}

