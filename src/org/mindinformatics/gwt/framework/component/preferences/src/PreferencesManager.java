package org.mindinformatics.gwt.framework.component.preferences.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 *
 * This class manages the preferences of the main applications and all the 
 * plugins. The application and all the plugins are supposed to register
 * their preferences items in this cache.
 */
public class PreferencesManager {

	/**
	 * Preferences cache. This is organizing the preferences by
	 * PLUGIN NAME -> PREFERENCE NAME -> PREFERENCE ITEM
	 */
	public HashMap<String, HashMap<String, GenericPreference>> preferencesCache = 
		new HashMap<String, HashMap<String, GenericPreference>>();
	
	/**
	 * Adds preference items to the cache.
	 * @param preferenceItem	The generic preference item.
	 * @return	True if the preference value changed.
	 */
	public boolean putPreferenceItem(GenericPreference preferenceItem) {
		boolean isPreferenceChanged = false;
		if(preferencesCache.containsKey(preferenceItem.getPluginName())) {
			HashMap<String, GenericPreference> map = 
				preferencesCache.get(preferenceItem.getPluginName());
			if(map.containsKey(preferenceItem.getName())) {
				map.remove(preferenceItem.getName());
				isPreferenceChanged = true;
			}
			map.put(preferenceItem.getName(), preferenceItem);
		} else {
			HashMap<String, GenericPreference> map =
				new HashMap<String, GenericPreference>();
			map.put(preferenceItem.getName(), preferenceItem);
			preferencesCache.put(preferenceItem.getPluginName(), map); 
		}
		return isPreferenceChanged;
	}
	
	public boolean registerPreferenceItemValue(String pluginName, String name, String value) {
		TextPreference preferenceItem = new TextPreference();
		preferenceItem.setName(name);
		preferenceItem.setValue(value);
		preferenceItem.setPluginName(pluginName);
		return putPreferenceItem(preferenceItem);
	}
	
	public boolean registerPreferenceItemValue(String pluginName, String name, boolean value) {
		BooleanPreference preferenceItem = new BooleanPreference();
		preferenceItem.setName(name);
		preferenceItem.setValue(value);
		preferenceItem.setPluginName(pluginName);
		return putPreferenceItem(preferenceItem);
	}
	
	/**
	 * Changes the value of the value of a given property.
	 * @param pluginName	The name of the plugin the property belongs to
	 * @param name			The name of the property
	 * @param value			The value of the property
	 */
	public boolean changePreferenceItemValue(String pluginName, String name, String value) {
		if(preferencesCache.containsKey(pluginName)) {
			HashMap<String, GenericPreference> map = 
				preferencesCache.get(pluginName);
			if(map.containsKey(name)) {
				GenericPreference pref = map.get(name);
				if(pref instanceof TextPreference) {
					((TextPreference) pref).setValue(value);
					return true;
				} else if(pref instanceof BooleanPreference) {
					((BooleanPreference) pref).setValue(new Boolean(value));
					return true;
				}
			}
		}
		return false;
	}	
	
	/**
	 * Retrieves a preference item by plugin name and preference name.
	 * @param pluginName	The plugin name.
	 * @param name			The preference name.
	 * @return	The requested preference.
	 */
	public GenericPreference getPreferenceItem(String pluginName, String name) {
		HashMap<String, GenericPreference> map = preferencesCache.get(pluginName);
		return map.get(name);
	}
	
	//TODO add getItemAsBoolean
	
	/**
	 * Returns all properties as a list.
	 * @return	The list of available properties.
	 */
	public List<GenericPreference> getAllProperties() {
		ArrayList<GenericPreference> list = new ArrayList<GenericPreference>();
		Set<String> sets = preferencesCache.keySet();
		for(String set: sets) {
			list.addAll(preferencesCache.get(set).values());
		}
		return list;
	}
}
