package org.mindinformatics.gwt.domeo.plugins.annotation.spls.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Richard Boyce  <rdb20@pitt.edu>
 */
public class SPLsPlugin extends APlugin {

    public static final String VERSION = "0.1";
    public static final String TYPE = "Annotation";
    public static final String SUB_TYPE = "Structured Product Label Annotation";
    public static final String PLUGIN = SPLsPlugin.class.getName().substring(0, SPLsPlugin.class.getName().indexOf(".info"));

    private static SPLsPlugin instance;
    private SPLsPlugin() {}

    public static SPLsPlugin getInstance() {
         if(instance==null) instance = new SPLsPlugin();
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