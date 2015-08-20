package org.mindinformatics.gwt.framework.component.profiles.src.testing;

import java.util.HashMap;

public interface IUpdateProfileCallback {

	public void updateCurrentProfile();
	public void failedSavingCurrentProfile();
	public HashMap<String, String> getPluginsStatus();
	public HashMap<String, String> getFeaturesStatus();
}
