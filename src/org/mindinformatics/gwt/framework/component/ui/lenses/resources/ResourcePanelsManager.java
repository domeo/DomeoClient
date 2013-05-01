package org.mindinformatics.gwt.framework.component.ui.lenses.resources;

import java.util.HashMap;

import org.mindinformatics.gwt.domeo.model.MOnlineResource;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ResourcePanelsManager {

	private HashMap<String, Widget> 
		resourcePanels = new HashMap<String, Widget>();
	
	public boolean registerResourcePanel(String resourceType, Widget panel) {
		if(!resourcePanels.containsKey(resourceType)) {
			resourcePanels.put(resourceType, panel);
			return true;
		}
		return false;
	}
	
	public Widget getResourcePanel(String resourceType) {
		Widget w = resourcePanels.get(resourceType);
		if(w == null) w =  resourcePanels.get(MOnlineResource.class.getName());
		return w;
	}
}
