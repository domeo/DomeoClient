package org.mindinformatics.gwt.framework.src.commands;

import org.mindinformatics.gwt.framework.component.profiles.src.IProfileManager;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface InitProfileManagerCommandCallback {

	public IProfileManager getProfileManager();
	public void setProfileManager(IProfileManager profileManager);
	public IProfileManager selectProfileManager(ICommandCompleted completionCallback);
}
