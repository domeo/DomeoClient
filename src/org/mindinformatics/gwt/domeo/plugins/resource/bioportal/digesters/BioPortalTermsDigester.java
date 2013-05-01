package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.digesters;

import org.mindinformatics.gwt.domeo.component.linkeddata.digesters.ITrustedResourceDigester;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class BioPortalTermsDigester implements ITrustedResourceDigester {
	
	private String PREFIX1 = "http://bioportal.bioontology.org/ontologies/";
	private String PREFIX2 = "http://rest.bioontology.org/obs/ontologies#";
	
	public boolean doesDigesterApply(MLinkedResource resource) {
		if(resource.getSource().getUrl().startsWith(PREFIX1) || resource.getSource().getUrl().startsWith(PREFIX2)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String getLinkUrl(MLinkedResource resource) {
		if(resource.getSource().getUrl().startsWith(PREFIX2)) 
			return resource.getSource().getUrl().replaceAll(PREFIX2, PREFIX1) + "?p=terms&conceptid=" + resource.getUrl().replace("#", "%23");
		return resource.getSource().getUrl() + "?p=terms&conceptid=" + resource.getUrl().replace("#", "%23");
	}

	@Override
	public String getLinkLabel(MLinkedResource resource) {
		return "BioPortal";
	}

	@Override
	public String getSourceUrl(MLinkedResource resource) {
		if(resource.getSource().getUrl().startsWith(PREFIX2)) 
			return resource.getSource().getUrl().replaceAll(PREFIX2, PREFIX1);
		else return resource.getSource().getUrl();
	}
}
