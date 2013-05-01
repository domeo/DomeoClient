package org.mindinformatics.gwt.domeo.client.ui.annotation.plugins;

import java.util.HashMap;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public abstract class APlugin {	

	/**
	 * This is the list of required plugins (names, version) for the present plugin
	 */
	public HashMap<String, String> requiredPlugins = new HashMap<String, String>();
	
	public abstract String getPluginName();
	public abstract String getSubType();
	public abstract String getType();
	public abstract String getVersion();
	public abstract Boolean getMandatory();
}
