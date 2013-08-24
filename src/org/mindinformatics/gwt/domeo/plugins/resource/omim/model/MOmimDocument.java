package org.mindinformatics.gwt.domeo.plugins.resource.omim.model;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;
import org.mindinformatics.gwt.framework.component.agents.model.MAgent;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.model.references.IReferences;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

@SuppressWarnings("serial")
public class MOmimDocument extends MDocumentResource implements IReferences {

	public static final String PARAM_DESCRIPTION_READONLY = "Description Read Only";
	
	private MAgent source;
	private MLinkedResource isAbout;
	
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
