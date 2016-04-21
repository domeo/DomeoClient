package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.service;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublication;

/**
 * Defines behavior of the class that will handle the results
 * of micro-publications lookup and search.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IRetrieveMicropublicationsHandler {
	/**
	 * Called when the request did not return any result
	 */
	public void emptyResultSet();
	/**
	 * Called after search of one or more micro-publication
	 * @param microPublications
	 */
	public void returnResources(ArrayList<MMicroPublication> microPublications);
	/**
	 * Called to return one resource (normally result of direct access)
	 * @param microPublication The requested micro-publication
	 */
	public void returnResource(MMicroPublication microPublication);
	/**
	 * Called when direct access did not succeed.
	 * @param message	The explanatory message
	 */
	public void resourceNotFound(String message);		
	/**
	 * Called when exceptions have been handled
	 * @param message	The exception message
	 */
	public void exception(String message);
}
