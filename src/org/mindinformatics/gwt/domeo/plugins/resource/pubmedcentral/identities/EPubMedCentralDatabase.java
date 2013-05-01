package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.identities;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentDatabase;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class EPubMedCentralDatabase extends MAgentDatabase {

	private static EPubMedCentralDatabase _instance;
	
	public static EPubMedCentralDatabase getInstance() {
		if(_instance==null) _instance = new EPubMedCentralDatabase();
		return _instance;
	}
	
	private EPubMedCentralDatabase() {
		super("http://purl.org/domeo/entity/resource/PubMed_Central", "PMC NCBI - PubMed Central", "http://www.ncbi.nlm.nih.gov/pmc/", "<noversion>");
	}
}
