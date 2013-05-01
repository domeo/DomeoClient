package org.mindinformatics.gwt.domeo.plugins.annotation.selection.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class SelectionPlugin extends APlugin {
	
	public static final String VERSION = "0.1";
	public static final String TYPE = "Annotation";
	public static final String SUB_TYPE = "Selection";
	public static final String PLUGIN = SelectionPlugin.class.getName().substring(0, SelectionPlugin.class.getName().indexOf(".info"));
	
	private static SelectionPlugin instance;
	private SelectionPlugin() {}
	
	public static SelectionPlugin getInstance() {
		if(instance==null) instance = new SelectionPlugin();
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
