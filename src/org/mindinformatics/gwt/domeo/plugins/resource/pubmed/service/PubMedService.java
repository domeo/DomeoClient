package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service;

import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface PubMedService extends RemoteService {
	public MPublicationArticleReference getBibliographicObject(String typeQuery, String textQuery) throws IllegalArgumentException;
}
