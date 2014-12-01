package org.mindinformatics.gwt.domeo.plugins.resource.europubmedcentral.identities;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentDatabase;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class EEuroPubMedCentralDatabase extends MAgentDatabase {

	private static EEuroPubMedCentralDatabase _instance;
	
	public static EEuroPubMedCentralDatabase getInstance() {
		if(_instance==null) _instance = new EEuroPubMedCentralDatabase();
		return _instance;
	}
	
	private EEuroPubMedCentralDatabase() {
		super("http://purl.org/domeo/entity/resource/Euro_PubMed_Central", "EBI - Euro PubMed Central", "http://europepmc.org/", "<noversion>");
	}
}
