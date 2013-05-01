package org.mindinformatics.gwt.domeo.plugins.annotation.comment.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CommentariesPlugin extends APlugin {
	
	public static final String VERSION = "0.1";
	public static final String TYPE = "General";
	public static final String SUB_TYPE = "Commentaries";
	public static final String PLUGIN = CommentariesPlugin.class.getName().substring(0, CommentariesPlugin.class.getName().indexOf(".info"));
	
	private static CommentariesPlugin instance;
	private CommentariesPlugin() {}
	
	public static CommentariesPlugin getInstance() {
		if(instance==null) instance = new CommentariesPlugin();
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
