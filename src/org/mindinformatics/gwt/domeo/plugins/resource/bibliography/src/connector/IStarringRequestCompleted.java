package org.mindinformatics.gwt.domeo.plugins.resource.bibliography.src.connector;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IStarringRequestCompleted {

	public void documentResourceStarred();
	public void documentResourceStarred(boolean starred);
	public void documentResourceUnstarred();
}
