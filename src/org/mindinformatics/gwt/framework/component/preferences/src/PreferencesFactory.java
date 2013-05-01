package org.mindinformatics.gwt.framework.component.preferences.src;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 *
 * This is the factory for all preference items. 
 */
public class PreferencesFactory {

	public BooleanPreference createBooleanPreference
		(String name, String pluginName, boolean value) 
	{
		BooleanPreference preferenceItem = new  BooleanPreference();
		preferenceItem.setPluginName(pluginName);
		preferenceItem.setValue(value);
		preferenceItem.setName(name);
		return preferenceItem;
	}
	
	public TextPreference createTextPreference
		(String name, String pluginName, String value) 
	{
		TextPreference preferenceItem = new TextPreference();
		preferenceItem.setPluginName(pluginName);
		preferenceItem.setValue(value);
		preferenceItem.setName(name);
		return preferenceItem;
	}
}
