package org.mindinformatics.gwt.framework.src.commands;

import org.mindinformatics.gwt.framework.component.users.src.IUserManager;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface InitUserManagerCommandCallback {

	public void setUserManager(IUserManager userManager);
	public IUserManager selectUserManager(ICommandCompleted completionCallback);
}
