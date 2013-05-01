package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;
import org.mindinformatics.gwt.framework.component.agents.model.MAgent;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.model.references.IReferences;
import org.mindinformatics.gwt.framework.model.references.ISelfReference;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MPubMedDocument extends MDocumentResource implements ISelfReference, IReferences {
	
	private MAgent source;
	private MLinkedResource isAbout; // To change into annotation
	
	private MAnnotationReference reference;
	private List<MAnnotationReference> references = new ArrayList<MAnnotationReference>();

	public MAgent getSource() {
		return source;
	}
	public void setSource(MAgent source) {
		this.source = source;
	}
	public MLinkedResource getIsAbout() {
		return isAbout;
	}
	public void setIsAbout(MLinkedResource isAbout) {
		this.isAbout = isAbout;
	}

	@Override
	public void setSelfReference(MAnnotationReference citation) {
		this.reference = (MAnnotationReference) citation;
	}
	@Override
	public MAnnotationReference getSelfReference() {
		return reference;
	}
	@Override
	public void setReferences(List<MAnnotationReference> references) {
		this.references = references;
	}
	@Override
	public List<MAnnotationReference> getReferences() {
		return references;
	}
	// Maybe making it more robust cheking if the index is present already
	@Override
	public void addReference(MAnnotationReference reference) {
		references.add(reference);
	}
	@Override
	public MAnnotationReference getReferenceByIndex(Integer index) {
		if((index-1)>=0 && (index-1)<references.size())  
			return references.get(index-1);
		else return null;
	}
	@Override
	public MAnnotationReference getReferenceByUrl(String url) {
		for(MAnnotationReference reference: references) {
			if(((MPublicationArticleReference)reference.getReference()).getUrl()!=null && 
					((MPublicationArticleReference)reference.getReference()).getUrl().equals(url)) {
				return reference;
			}
		}
		return null;
	}
}
