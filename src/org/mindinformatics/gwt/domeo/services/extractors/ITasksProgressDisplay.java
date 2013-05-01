package org.mindinformatics.gwt.domeo.services.extractors;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ITasksProgressDisplay {
	public void updateProgressMessage(String message);
	public void displayProgressItems();
	public void removeProgressItems();
	public void showThisAnnotationItems();
}
