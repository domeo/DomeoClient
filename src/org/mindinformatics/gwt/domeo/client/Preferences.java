package org.mindinformatics.gwt.domeo.client;

import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.logging.src.LogLevel;
import org.mindinformatics.gwt.framework.component.logging.src.LogsManager;
import org.mindinformatics.gwt.framework.component.preferences.src.PreferencesFactory;
import org.mindinformatics.gwt.framework.component.preferences.src.PreferencesManager;
import org.mindinformatics.gwt.framework.src.Application;
import org.mindinformatics.gwt.framework.src.IApplication;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class Preferences implements IInitializableComponent {

	private IApplication _application;
	
	public Preferences(IApplication application) {
		_application = application;
	}
	
	public void init() {
		PreferencesManager prefsManager = _application.getPreferences();
		PreferencesFactory prefsFactory = new PreferencesFactory();
		
		// Cliboard
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Domeo.PREF_ANN_MULTIPLE_TARGETS, Application.class.getName(), true));
		
		// Logging level
		prefsManager.putPreferenceItem(prefsFactory.createTextPreference(LogsManager.LEVEL, Application.class.getName(), LogLevel.DEBUG.toString()));
		// Automatically reporting issues
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Application.PREF_AUTOMATICALLY_REPORT_ISSUES, Application.class.getName(), true));
		
		// Wrapping of tables
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Domeo.PREF_WRAP_TABLES, Domeo.class.getName(), false));
		// Wrapping of images
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Domeo.PREF_WRAP_IMAGES, Domeo.class.getName(), true));
		// Wrapping of links
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Domeo.PREF_WRAP_LINKS, Domeo.class.getName(), true));
		
		// Display of provenance
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Domeo.PREF_DISPLAY_PROVENANCE, Domeo.class.getName(), true));
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Domeo.PREF_DISPLAY_USER_PROVENANCE, Domeo.class.getName(), true));
		
		// Display of annotations for debug
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Domeo.PREF_DISPLAY_ANNOTATION_FOR_DEBUG, Domeo.class.getName(), true));
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Domeo.PREF_ALLOW_COMMENTING, Domeo.class.getName(), true));
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Domeo.PREF_ALLOW_CURATION, Domeo.class.getName(), true));
		
		// Perform annotation of references
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Domeo.PREF_PERFORM_ANNOTATION_OF_REFERENCES, Domeo.class.getName(), true));
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Domeo.PREF_PERFORM_ANNOTATION_OF_CITATIONS, Domeo.class.getName(), true));
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Domeo.PREF_SHOW_ANNOTATION_OF_REFERENCES, Domeo.class.getName(), true));
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Domeo.PREF_SHOW_ANNOTATION_OF_CITATIONS, Domeo.class.getName(), true));
		
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Domeo.PREF_ASK_BEFORE_DELETION, Domeo.class.getName(), true));
		prefsManager.putPreferenceItem(prefsFactory.createBooleanPreference(Domeo.PREF_ALLOW_CURATION_MOTIVATION, Domeo.class.getName(), true));

	}
}
