package org.mindinformatics.gwt.framework.component.preferences.ui;

import java.util.List;

import org.mindinformatics.gwt.framework.component.preferences.src.BooleanPreference;
import org.mindinformatics.gwt.framework.component.preferences.src.GenericPreference;
import org.mindinformatics.gwt.framework.component.preferences.src.TextPreference;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CompletePreferencesList extends Composite implements IContentPanel {

	interface Binder extends UiBinder<VerticalPanel, CompletePreferencesList> {}
	private static final Binder binder = GWT.create(Binder.class);

	// private Resources _resources;
	private IApplication _application;
	private IContainerPanel _containerPanel;
	
	private CellTable<GenericPreference> table;

	// Layout
	@UiField
	VerticalPanel main;

	public void setContainer(IContainerPanel containerPanel) {
		_containerPanel = containerPanel;
	}

	public IContainerPanel getContainer() {
		return _containerPanel;
	}

	public CompletePreferencesList(IApplication application) {
		_application = application;

		table = new CellTable<GenericPreference>();
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		
		TextColumn<GenericPreference> pluginColumn = new TextColumn<GenericPreference>() {
			@Override
			public String getValue(GenericPreference object) {
				return object.getPluginName();
			}
		};
		table.addColumn(pluginColumn, "Plugin");

		TextColumn<GenericPreference> nameColumn = new TextColumn<GenericPreference>() {
			@Override
			public String getValue(GenericPreference object) {
				return object.getName();
			}
		};
		table.addColumn(nameColumn, "Name");

		TextColumn<GenericPreference> valueColumn = new TextColumn<GenericPreference>() {
			@Override
			public String getValue(GenericPreference object) {
				if(object instanceof BooleanPreference) {
					return ((BooleanPreference)object).getValue().toString();
				} if(object instanceof TextPreference) {
					return ((TextPreference)object).getValue();
				}
				return "Preference type not supported " + object.getClass().getName();
			}
		};
		table.addColumn(valueColumn, "Value");
		
		initWidget(binder.createAndBindUi(this)); 
		refreshTable();
	}
	
	public void refresh() {
		refreshTable();
	}

	private void refreshTable() {
		List<GenericPreference> list = _application.getPreferences().getAllProperties();
		
		// Set the total row count. This isn't strictly necessary, but it affects
	    // paging calculations, so its good habit to keep the row count up to date.
	    table.setRowCount(list.size(), true);

	    // Push the data into the widget.
	    table.setRowData(0, list);
	    main.add(table);
	}
}
