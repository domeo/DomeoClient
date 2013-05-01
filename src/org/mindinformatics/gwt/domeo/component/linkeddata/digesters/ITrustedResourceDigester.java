package org.mindinformatics.gwt.domeo.component.linkeddata.digesters;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ITrustedResourceDigester {

	public boolean doesDigesterApply(MLinkedResource resource);
	public String getLinkUrl(MLinkedResource resource);
	public String getLinkLabel(MLinkedResource resource);
	public String getSourceUrl(MLinkedResource resource);
}
