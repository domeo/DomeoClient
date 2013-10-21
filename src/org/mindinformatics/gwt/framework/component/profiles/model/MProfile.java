package org.mindinformatics.gwt.framework.component.profiles.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import org.mindinformatics.gwt.framework.model.agents.IPerson;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */ 
@SuppressWarnings("serial")
public class MProfile implements IProfile, Serializable, IsSerializable {
	
	private String uuid;
	private String name;
	private String description;
	private Date lastSavedOn;
	private IPerson lastSavedBy;
	private boolean changed;
	

	/*
	 * This stores the list of the activated plugins   
	 */
	private HashMap<String, String> pluginsMap = new HashMap<String, String>();
	
	/**
	 * @param plugin		The full name of the plugin
	 * @param preference	The preference value (enabled/disabled)
	 * @return True if the value has been modified
	 */
	public boolean addPluginPreference(String pluginName, String preference) {
		changed = true;
		return (pluginsMap.put(pluginName, preference)!=null);
	}
	
	/**
	 * @param pluginName	The full name of the plugin
	 * @return True if the plugin is enabled
	 */
	public boolean isPluginEnabled(String pluginName) {
		if(pluginName.equals("")) return true;
		return (pluginsMap.get(pluginName)!=null && pluginsMap.get(pluginName).equals(PLUGIN_ENABLED));
	}
	
	public boolean isPluginDisabled(String pluginName) {
		return (pluginsMap.get(pluginName)!=null && pluginsMap.get(pluginName).equals(PLUGIN_DISABLED));
	}
	
	public HashMap<String, String> getPlugins() {
		return pluginsMap;
	}
	
	private HashMap<String, String> featuresMap = new HashMap<String, String>();
	
	/**
	 * @param plugin		The full name of the feature
	 * @param preference	The preference value (enabled/disabled)
	 * @return True if the value has been modified
	 */
	public boolean addFeaturePreference(String featureName, String preference) {
		changed = true;
		return (featuresMap.put(featureName, preference)!=null);
	}
	
	/**
	 * @param pluginName	The full name of the plugin
	 * @return True if the plugin is enabled
	 */
	public boolean isFeatureEnabled(String featureName) {
		if(featureName.equals("")) return true;
		return (featuresMap.get(featureName)!=null && featuresMap.get(featureName).equals(PLUGIN_ENABLED));
	}
	
	public boolean isFeatureDisabled(String pluginName) {
		return (featuresMap.get(pluginName)!=null && featuresMap.get(pluginName).equals(PLUGIN_DISABLED));
	}
	
	public HashMap<String, String> getFeatures() {
		return featuresMap;
	}
	
	public void setAsChanged() {
		changed = true;
	}
	
	public void setAsUnchanged() {
		changed = false;
	}
	
	public boolean isChanged() {
		return changed;
	}

	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getLastSavedOn() {
		return lastSavedOn;
	}
	
	public String getFormattedLastSavedOn() {
		//DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yy h:mma");
		//return fmt.format(lastSavedOn);
		return "";
	}

	public void setLastSavedOn(Date lastSavedOn) {
		this.lastSavedOn = lastSavedOn;
	}

	public IPerson getLastSavedBy() {
		return lastSavedBy;
	}

	public void setLastSavedBy(IPerson lastSavedBy) {
		this.lastSavedBy = lastSavedBy;
	}

	@Override
	public String getCreatedOnAsString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getCreatedOn() {
		// TODO Auto-generated method stub
		return null;
	}
}
