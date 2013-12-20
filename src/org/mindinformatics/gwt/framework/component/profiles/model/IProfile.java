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
	public static final String FEATURE_TEXTMINING_SUMMARY = "org.mindinformatics.gwt.domeo.feature.textmining.summary";
	public static final String FEATURE_REFERENCE_SELF = "org.mindinformatics.gwt.domeo.feature.document.general.reference.self";
	public static final String FEATURE_QUALIFIERS_SELF = "org.mindinformatics.gwt.domeo.feature.document.general.qualifiers.self";
	public static final String FEATURE_MY_BIBLIOGRAPHY = "org.mindinformatics.gwt.domeo.feature.document.general.bibliography";
	public static final String FEATURE_MY_RECOMMENDATIONS = "org.mindinformatics.gwt.domeo.feature.document.general.recommendations";
	public static final String FEATURE_PRIVATE_ANNOTATION = "org.mindinformatics.gwt.domeo.feature.general.access.private";
	public static final String FEATURE_GROUP_ANNOTATION = "org.mindinformatics.gwt.domeo.feature.general.access.groups";
	public static final String FEATURE_PUBLIC_ANNOTATION = "org.mindinformatics.gwt.domeo.feature.general.access.public";
	public static final String FEATURE_EXTERNAL_ANNOTATION = "org.mindinformatics.gwt.domeo.feature.general.access.external";
	
	String getUuid();
	String getName();
	String getDescription();
	
	String getCreatedOnAsString();
	Date getCreatedOn();
}
