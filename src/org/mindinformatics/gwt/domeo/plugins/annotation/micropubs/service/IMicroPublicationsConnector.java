package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.service;


/**
 * Defines the behavior of the micro-publications connector for retrieval
 * and search of micro-publications.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IMicroPublicationsConnector {

	/**
	 * Retrieve a specific micro-publication
	 * @param handler	The responsible for handling the response	
	 * @param urn		The urn of the requested micro-publication
	 * @throws IllegalArgumentException
	 */
	public void retrieveMicropublicationByUrn(final IRetrieveMicropublicationsHandler handler, String urn)
			throws IllegalArgumentException;
	
	/**
	 * Retrieves all the available micro-publications for a specific document
	 * @param handler		The responsible for handling the response	
	 * @param documentUrl	The document we are interested in retrieving micro-publications for
	 * @throws IllegalArgumentException
	 */
	public void retrieveMicropublicationsByDocumentUrl(final IRetrieveMicropublicationsHandler handler, String documentUrl)
			throws IllegalArgumentException;
	
	/**
	 * Searches for micro-publications by text
	 * @param handler		The responsible for handling the response	
	 * @param typeQuery 	The type of query
	 * @param textQuery		The text to search for
	 * @param maxResults	Pagination, page size
	 * @param offset		Pagination, page start
	 * @throws IllegalArgumentException
	 */
	public void searchMicropublications(IRetrieveMicropublicationsHandler handler,
			String typeQuery, final String textQuery, int maxResults, int offset) throws IllegalArgumentException;
}
