package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class MicroPublicationsPlugin extends APlugin {
	
	public static final String VERSION = "0.1";
	public static final String TYPE = "Annotation";
	public static final String SUB_TYPE = "Micropublications";
	public static final String PLUGIN = MicroPublicationsPlugin.class.getName().substring(0, MicroPublicationsPlugin.class.getName().indexOf(".info"));

	private static MicroPublicationsPlugin instance;
	private MicroPublicationsPlugin() {}
	
	public static MicroPublicationsPlugin getInstance() {
		if(instance==null) instance = new MicroPublicationsPlugin();
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
		return false;
	}

	@Override
	public String getVersion() {
		return VERSION;
	}
}
