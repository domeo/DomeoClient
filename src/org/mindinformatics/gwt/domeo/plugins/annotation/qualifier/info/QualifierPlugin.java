package org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class QualifierPlugin extends APlugin {
	
	public static final String VERSION = "0.1";
	public static final String TYPE = "Annotation";
	public static final String SUB_TYPE = "Qualifier";
	public static final String PLUGIN = QualifierPlugin.class.getName().substring(0, QualifierPlugin.class.getName().indexOf(".info"));
		
	private static QualifierPlugin instance;
	private QualifierPlugin() {}
	
	public static QualifierPlugin getInstance() {
		if(instance==null) instance = new QualifierPlugin();
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
