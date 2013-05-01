package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service;

import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface PubMedServiceAsync {
	void getBibliographicObject(String typeQuery, String textQuery, AsyncCallback<MPublicationArticleReference> callback) throws IllegalArgumentException;
}
