package org.mindinformatics.gwt.domeo.plugins.annotation.ddi.info;
import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

// This plugin info file was created by the plugin-builder
public class ddiPlugin extends APlugin {

    public static final String VERSION = "1.0";
    public static final String TYPE = "Annotation";
    public static final String SUB_TYPE = "Expert Vs. Non-Expert Study annotation";
    public static final String PLUGIN = ddiPlugin.class.getName().substring(0, ddiPlugin.class.getName().indexOf(".info"));

    private static ddiPlugin instance;
    private ddiPlugin() {}

    public static ddiPlugin getInstance() {
         if(instance==null) instance = new ddiPlugin();
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