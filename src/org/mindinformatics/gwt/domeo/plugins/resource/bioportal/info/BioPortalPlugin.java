package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class BioPortalPlugin extends APlugin {

	public static final String VERSION = "0.1";
	public static final String TYPE = "Resource";
	public static final String SUB_TYPE = "Search";
	public static final String PLUGIN = BioPortalPlugin.class.getName().substring(0, BioPortalPlugin.class.getName().indexOf(".info"));

	private static BioPortalPlugin instance;
	private BioPortalPlugin() {}
	
	public static BioPortalPlugin getInstance() {
		if(instance==null) instance = new BioPortalPlugin();
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
		return VERSION;
	}

}
