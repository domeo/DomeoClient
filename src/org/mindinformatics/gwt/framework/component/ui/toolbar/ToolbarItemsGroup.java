package org.mindinformatics.gwt.framework.component.ui.toolbar;

import java.util.ArrayList;
import java.util.List;

public class ToolbarItemsGroup {

	private String _groupName;
	private List<IToolbarItem> _items = new ArrayList<IToolbarItem>();
	
	public ToolbarItemsGroup(String groupName) {
		_groupName = groupName;
	}
	
	public String getGroupName() {
		return _groupName;
	}

	public boolean addItem(IToolbarItem item) {
		if(_items.contains(item)) return false;
		else {
			_items.add(item);
			return true;
		}
	}
	
	public List<IToolbarItem> getItems() {
		return _items;
	}

	public void hide() {
		for(IToolbarItem item: _items) {
			item.hide();
		}
	}
	
	public void show() {
		for(IToolbarItem item: _items) {
			item.show();
		}
	}
}
