package org.mindinformatics.gwt.domeo.plugins.resource.nif.digesters;

import org.mindinformatics.gwt.domeo.component.linkeddata.digesters.ITrustedResourceDigester;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class NifStandardDigester implements ITrustedResourceDigester {
	
	private String PREFIX1 = "http://bioportal.bioontology.org/ontologies/1084";
	private String PREFIX2 = "http://uri.neuinfo.org/nif/nifstd/";
	
	public boolean doesDigesterApply(MLinkedResource resource) {
		if(resource.getSource().getUrl().equals(PREFIX1) || resource.getSource().getUrl().equals(PREFIX2)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String getLinkUrl(MLinkedResource resource) {
		String id = null;
		if(resource.getUrl().lastIndexOf("#")>0) {
			id = resource.getUrl().substring(resource.getUrl().lastIndexOf("#")+1);
		} else {
			id = resource.getUrl().substring(resource.getUrl().lastIndexOf("/")+1);
		}
		return "http://neurolex.org/wiki/"+id;
	}

	@Override
	public String getLinkLabel(MLinkedResource resource) {
		return "NIF";
	}

	@Override
	public String getSourceUrl(MLinkedResource resource) {
		if(resource.getUrl().startsWith(PREFIX1)) return PREFIX1;
		return PREFIX2;
	}
}
