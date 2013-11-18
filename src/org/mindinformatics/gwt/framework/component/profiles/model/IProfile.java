package org.mindinformatics.gwt.framework.component.profiles.model;

import java.util.Date;

public interface IProfile {

	public static final String PLUGIN_DISABLED = "disabled";
	public static final String PLUGIN_ENABLED = "enabled";
	
	public static final String FEATURE_ADDRESSBAR = "org.mindinformatics.gwt.domeo.feature.addressbar";
	public static final String FEATURE_ANALYZE = "org.mindinformatics.gwt.domeo.feature.analyze";
	public static final String FEATURE_PREFERENCES = "org.mindinformatics.gwt.domeo.feature.preferences";
	public static final String FEATURE_SHARING = "org.mindinformatics.gwt.domeo.feature.sharing";
	public static final String FEATURE_HELP = "org.mindinformatics.gwt.domeo.feature.help";
	public static final String FEATURE_BRANDING = "org.mindinformatics.gwt.domeo.feature.branding";
	
	String getUuid();
	String getName();
	String getDescription();
	
	String getCreatedOnAsString();
	Date getCreatedOn();
}
