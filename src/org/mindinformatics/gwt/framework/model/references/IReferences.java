package org.mindinformatics.gwt.framework.model.references;

import java.util.List;

import org.mindinformatics.gwt.domeo.model.MAnnotationReference;

public interface IReferences {

	public void addReference(MAnnotationReference reference);
	public void setReferences(List<MAnnotationReference> references);
	public List<MAnnotationReference> getReferences();
	public MAnnotationReference getReferenceByIndex(Integer index);
	public MAnnotationReference getReferenceByUrl(String url);
}
