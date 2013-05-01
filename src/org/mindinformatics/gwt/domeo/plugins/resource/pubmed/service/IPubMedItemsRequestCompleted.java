package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IPubMedItemsRequestCompleted {
	public void returnBibliographicObject(ArrayList<MPublicationArticleReference> reference);
	public void bibliographyObjectNotFound();
	public void bibliographyObjectNotFound(String message);	
}
