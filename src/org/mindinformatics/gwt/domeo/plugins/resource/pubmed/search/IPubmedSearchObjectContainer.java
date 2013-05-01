package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.search;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IPubmedSearchObjectContainer {
	public void addBibliographicObject(MPublicationArticleReference reference);
	public ArrayList<MPublicationArticleReference> getBibliographicObjects();
	public ArrayList<MAnnotationReference> getBibliographicObjectAnnotations();
}
