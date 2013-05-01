package org.mindinformatics.gwt.domeo.client.commands;

import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IPersistenceManager;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface InitPersistenceManagerCommandCallback {

	public void setPersistenceManager(IPersistenceManager profileManager);
	public IPersistenceManager selectPersistenceManager(ICommandCompleted completionCallback);
}
