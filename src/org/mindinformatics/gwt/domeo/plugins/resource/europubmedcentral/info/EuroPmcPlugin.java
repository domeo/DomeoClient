package org.mindinformatics.gwt.domeo.plugins.resource.europubmedcentral.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class EuroPmcPlugin extends APlugin {

	public static final String VERSION = "0.1";
	public static final String TYPE = "Resource";
	public static final String SUB_TYPE = "Extractor/Search";
	public static final String PLUGIN = EuroPmcPlugin.class.getName().substring(0, EuroPmcPlugin.class.getName().indexOf(".info"));

	private static EuroPmcPlugin instance;
	private EuroPmcPlugin() {}
	
	public static EuroPmcPlugin getInstance() {
		if(instance==null) instance = new EuroPmcPlugin();
		return instance;
	}
	
	@Override
	public String getPluginName() {
		return PLUGIN;
	}

	@Override
	public String getSubType() {
		return SUB_TYPE;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public Boolean getMandatory() {
		return false;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return VERSION;
	}

}
