package org.mindinformatics.gwt.domeo.plugins.resource.document.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class DocumentPlugin extends APlugin {

	public static final String VERSION = "0.1";
	public static final String TYPE = "Resource";
	public static final String SUB_TYPE = "Extractor/Search";
	public static final String PLUGIN = DocumentPlugin.class.getName().substring(0, DocumentPlugin.class.getName().indexOf(".info"));

	private static DocumentPlugin instance;
	private DocumentPlugin() {}
	
	public static DocumentPlugin getInstance() {
		if(instance==null) instance = new DocumentPlugin();
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
		return true;
	}

	@Override
	public String getVersion() {
		return VERSION;
	}

}
