package org.mindinformatics.gwt.domeo.plugins.resource.opentrials.model;

import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;
import org.mindinformatics.gwt.framework.component.agents.model.MAgent;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

@SuppressWarnings("serial")
public class MOpenTrialsDocument extends MDocumentResource {

	public static final String PARAM_DESCRIPTION_READONLY = "Description Read Only";
	
	private MAgent source;
	private MLinkedResource isAbout;

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
}
