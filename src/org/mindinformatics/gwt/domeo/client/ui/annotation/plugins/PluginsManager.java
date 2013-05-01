package org.mindinformatics.gwt.domeo.client.ui.annotation.plugins;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PluginsManager {

	private HashMap<String, APlugin> pluginsList = new HashMap<String, APlugin>();
	private HashMap<APlugin, Boolean> pluginsActivation = new HashMap<APlugin, Boolean>();
	
	public void registerPlugin(APlugin plugin) {
		registerPlugin(plugin, false);
	}
	
	public void registerPlugin(APlugin plugin, boolean enable) {
		pluginsList.put(plugin.getPluginName(), plugin);
		pluginsActivation.put(plugin, enable);
	}
	
	public boolean enablePlugin(APlugin plugin, boolean enable) {
		return pluginsActivation.put(plugin, enable);
	}
	
	public Collection<APlugin> getPluginsList() {
		return pluginsList.values();
	}
}
