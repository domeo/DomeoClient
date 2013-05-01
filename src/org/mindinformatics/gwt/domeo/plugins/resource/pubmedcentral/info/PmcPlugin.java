package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PmcPlugin extends APlugin {

	public static final String VERSION = "0.1";
	public static final String TYPE = "Resource";
	public static final String SUB_TYPE = "Extractor/Search";
	public static final String PLUGIN = PmcPlugin.class.getName().substring(0, PmcPlugin.class.getName().indexOf(".info"));

	private static PmcPlugin instance;
	private PmcPlugin() {}
	
	public static PmcPlugin getInstance() {
		if(instance==null) instance = new PmcPlugin();
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
