package org.mindinformatics.gwt.framework.component.preferences.src;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 *
 * This is a class for modeling an abstract generic preference item.
 * The different kind of preference items will extend this class.
 */
public class GenericPreference {

	String name;
	String pluginName;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPluginName() {
		return pluginName;
	}
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
}
