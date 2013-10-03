package org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.info;
import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

// This plugin info file was created by the plugin-builder
public class SPL_DDIPlugin extends APlugin {

    public static final String VERSION = "1.0";
    public static final String TYPE = "Annotation";
    public static final String SUB_TYPE = "annotation";
    public static final String PLUGIN = SPL_DDIPlugin.class.getName().substring(0, SPL_DDIPlugin.class.getName().indexOf(".info"));

    private static SPL_DDIPlugin instance;
    private SPL_DDIPlugin() {}

    public static SPL_DDIPlugin getInstance() {
         if(instance==null) instance = new SPL_DDIPlugin();
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