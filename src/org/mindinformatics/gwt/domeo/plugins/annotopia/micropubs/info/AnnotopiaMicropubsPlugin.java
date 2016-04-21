package org.mindinformatics.gwt.domeo.plugins.annotopia.micropubs.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotopiaMicropubsPlugin extends APlugin {

	public static final String VERSION = "0.1";
	public static final String TYPE = "Annotopia";
	public static final String SUB_TYPE = "Search";
	public static final String PLUGIN = AnnotopiaMicropubsPlugin.class.getName().substring(0, AnnotopiaMicropubsPlugin.class.getName().indexOf(".info"));

	private static AnnotopiaMicropubsPlugin instance;
	private AnnotopiaMicropubsPlugin() {}
	
	public static AnnotopiaMicropubsPlugin getInstance() {
		if(instance==null) instance = new AnnotopiaMicropubsPlugin();
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
