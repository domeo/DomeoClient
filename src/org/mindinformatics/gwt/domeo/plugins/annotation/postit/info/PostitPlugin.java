package org.mindinformatics.gwt.domeo.plugins.annotation.postit.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PostitPlugin extends APlugin {
	
	public static final String VERSION = "0.1";
	public static final String TYPE = "Annotation";
	public static final String SUB_TYPE = "Post it";
	public static final String PLUGIN = PostitPlugin.class.getName().substring(0, PostitPlugin.class.getName().indexOf(".info"));

	private static PostitPlugin instance;
	private PostitPlugin() {}
	
	public static PostitPlugin getInstance() {
		if(instance==null) instance = new PostitPlugin();
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
