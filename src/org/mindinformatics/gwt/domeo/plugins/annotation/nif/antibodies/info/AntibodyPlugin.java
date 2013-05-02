package org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AntibodyPlugin extends APlugin {
	
	public static final String VERSION = "0.1";
	public static final String TYPE = "Annotation";
	public static final String SUB_TYPE = "Antibodies";
	public static final String PLUGIN = AntibodyPlugin.class.getName().substring(0, AntibodyPlugin.class.getName().indexOf(".info"));

	private static AntibodyPlugin instance;
	private AntibodyPlugin() {}
	
	public static AntibodyPlugin getInstance() {
		if(instance==null) instance = new AntibodyPlugin();
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
