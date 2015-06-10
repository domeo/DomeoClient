package org.mindinformatics.gwt.domeo.plugins.annotopia.base.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotopiaBasePlugin extends APlugin {

	public static final String VERSION = "0.1";
	public static final String TYPE = "Annotopia";
	public static final String SUB_TYPE = "Base";
	public static final String PLUGIN = AnnotopiaBasePlugin.class.getName().substring(0, AnnotopiaBasePlugin.class.getName().indexOf(".info"));

	private static AnnotopiaBasePlugin instance;
	private AnnotopiaBasePlugin() {}
	
	public static AnnotopiaBasePlugin getInstance() {
		if(instance==null) instance = new AnnotopiaBasePlugin();
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
