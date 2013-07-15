package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model;

import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

public class MMpReference extends MMpElement implements IMpSupportingElement {

	private MPublicationArticleReference reference;

	public MPublicationArticleReference getReference() {
		return reference;
	}

	public void setReference(MPublicationArticleReference reference) {
		this.reference = reference;
	}
}
