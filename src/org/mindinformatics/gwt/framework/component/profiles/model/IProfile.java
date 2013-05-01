package org.mindinformatics.gwt.framework.component.profiles.model;

import java.util.Date;

public interface IProfile {

	public static final String PLUGIN_DISABLED = "disabled";
	public static final String PLUGIN_ENABLED = "enabled";
	
	String getUuid();
	String getName();
	String getDescription();
	
	String getCreatedOnAsString();
	Date getCreatedOn();
}
