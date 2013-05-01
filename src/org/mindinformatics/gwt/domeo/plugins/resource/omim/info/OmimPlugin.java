package org.mindinformatics.gwt.domeo.plugins.resource.omim.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class OmimPlugin extends APlugin {

	public static final String VERSION = "0.1";
	public static final String TYPE = "Resource";
	public static final String SUB_TYPE = "Extractor/Search";
	public static final String PLUGIN = OmimPlugin.class.getName().substring(0, OmimPlugin.class.getName().indexOf(".info"));

	private static OmimPlugin instance;
	private OmimPlugin() {}
	
	public static OmimPlugin getInstance() {
		if(instance==null) instance = new OmimPlugin();
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
