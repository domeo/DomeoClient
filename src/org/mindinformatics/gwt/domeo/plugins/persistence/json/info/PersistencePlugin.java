package org.mindinformatics.gwt.domeo.plugins.persistence.json.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PersistencePlugin extends APlugin {
	
	public static final String VERSION = "0.1";
	public static final String TYPE = "Annotation";
	public static final String SUB_TYPE = "Persistence";
	public static final String PLUGIN = PersistencePlugin.class.getName().substring(0, PersistencePlugin.class.getName().indexOf(".info"));
	
	private static PersistencePlugin instance;
	private PersistencePlugin() {}
	
	public static PersistencePlugin getInstance() {
		if(instance==null) instance = new PersistencePlugin();
		return instance;
	}
	
	@Override
	public String getPluginName() {
		return PLUGIN;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public String getSubType() {
		return SUB_TYPE;
	}

	@Override
	public Boolean getMandatory() {
		return true;
	}

	@Override
	public String getVersion() {
		return VERSION;
	}
}
