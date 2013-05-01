package org.mindinformatics.gwt.framework.component;

import java.util.HashSet;

public class RefreshableComponentsManager {

	private HashSet<IRefreshableComponent> refreshableComponents = 
		new HashSet<IRefreshableComponent>();
	
	public boolean register(IRefreshableComponent component) {
		if(!refreshableComponents.contains(component)) {
			refreshableComponents.add(component);
			return true;
		} else return false;
	}
	
	public void refreshAllComponents() {
		for(IRefreshableComponent component: refreshableComponents) {
			component.refresh();
		}
	}
}
