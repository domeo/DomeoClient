package org.mindinformatics.gwt.domeo.plugins.annotation.relationship.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class RelationshipPlugin extends APlugin {
	
	public static final String VERSION = "0.1";
	public static final String TYPE = "Annotation";
	public static final String SUB_TYPE = "Relationship";
	public static final String PLUGIN = RelationshipPlugin.class.getName().substring(0, RelationshipPlugin.class.getName().indexOf(".info"));
	
	private static RelationshipPlugin instance;
	private RelationshipPlugin() {}
	
	public static RelationshipPlugin getInstance() {
		if(instance==null) instance = new RelationshipPlugin();
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
