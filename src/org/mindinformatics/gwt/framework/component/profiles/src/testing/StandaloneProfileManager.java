package org.mindinformatics.gwt.framework.component.profiles.src.testing;

import java.util.ArrayList;
import java.util.Date;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.profiles.model.MProfile;
import org.mindinformatics.gwt.framework.component.profiles.src.AProfileManager;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class StandaloneProfileManager extends AProfileManager {

	public StandaloneProfileManager(IApplication application, ICommandCompleted callbackCompleted) {
		super(application, callbackCompleted);
	}

	@Override
	public void retrieveAndCacheUserAvailableProfiles() {
		ArrayList<MProfile> profiles = new ArrayList<MProfile>();
		profiles.add(getProfileOne());
		profiles.add(getProfileTwo());
		setProfiles(profiles);
		notifyActionCompletion();
	}

	@Override
	public void retrieveAndCacheUserCurrentProfile() {
		setCurrentProfile(getProfileOne());
		notifyActionCompletion();
	}
	
	private MProfile getProfileOne() {
		MProfile profile = new MProfile();
		profile.setName("Complete Bio Profile");
		profile.setDescription("All the tools you need for biocuration");
		profile.setUuid("4fa09e38adb4d0.96200877");
		profile.setLastSavedOn(new Date());
		MAgentPerson person = new MAgentPerson();
		person.setName("Dr. Maurizio Mosca");
		person.setUuid("maurizio.mosca");
		profile.setLastSavedBy(person);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.opentrials", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.omim", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.pubmed", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.europubmedcentral", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.bioportal", MProfile.PLUGIN_ENABLED);
		return profile;
	}
	
	private MProfile getProfileTwo() {
		MProfile profile = new MProfile();
		profile.setName("Simple Bio Profile");
		profile.setDescription("A few tools to start");
		profile.setUuid("4fa09e38adb4d0.96200878");
		profile.setLastSavedOn(new Date());
		MAgentPerson person = new MAgentPerson();
		person.setName("Dr. Paolo Ciccarese");
		person.setUuid("paolo.ciccarese");
		profile.setLastSavedBy(person);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.opentrials", MProfile.PLUGIN_DISABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.omim", MProfile.PLUGIN_DISABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.pubmed", MProfile.PLUGIN_ENABLED);
		profile.addPluginPreference("org.mindinformatics.gwt.domeo.plugins.resource.bioportal", MProfile.PLUGIN_ENABLED);
		return profile;
	}

	@Override
	public MProfile saveUserProfile(MProfile newProfile,
			IUpdateProfileCallback callback) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveCurrentProfile(MProfile currentProfile,
			IUpdateProfileCallback callback) {
		// TODO Auto-generated method stub
	}

	@Override
	public void retrieveAndCacheAllProfiles() {
		// TODO Auto-generated method stub		
	}
}
