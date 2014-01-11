package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.info;
import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

// This plugin info file was created by the plugin-builder
public class expertstudy_pDDIPlugin extends APlugin {

    public static final String VERSION = "1.0";
    public static final String TYPE = "Annotation";
    public static final String SUB_TYPE = "annotation";
    public static final String PLUGIN = expertstudy_pDDIPlugin.class.getName().substring(0, expertstudy_pDDIPlugin.class.getName().indexOf(".info"));

    private static expertstudy_pDDIPlugin instance;
    private expertstudy_pDDIPlugin() {}

    public static expertstudy_pDDIPlugin getInstance() {
         if(instance==null) instance = new expertstudy_pDDIPlugin();
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