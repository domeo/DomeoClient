package org.mindinformatics.gwt.domeo.plugins.resource.europubmedcentral.identities;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class EEuroPubMedCentralExtractor extends MAgentSoftware {

private static EEuroPubMedCentralExtractor _instance;
	
	public static EEuroPubMedCentralExtractor getInstance() {
		if(_instance==null) _instance = new EEuroPubMedCentralExtractor();
		return _instance;
	}
	
	private EEuroPubMedCentralExtractor() {
		super("http://purl.org/domeo/entity/plugin/europubmedcentral/v2", "Euro PubMed Central Extractor", "2" , "001");
	}
}
