package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.identities;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class EPubMedCentralExtractor extends MAgentSoftware {

private static EPubMedCentralExtractor _instance;
	
	public static EPubMedCentralExtractor getInstance() {
		if(_instance==null) _instance = new EPubMedCentralExtractor();
		return _instance;
	}
	
	private EPubMedCentralExtractor() {
		super("http://purl.org/domeo/entity/plugin/pubmedcentral/v2", "PubMed Central Extractor", "2" , "001");
	}
}
