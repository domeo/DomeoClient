package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.identities;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentDatabase;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class EPubMedDatabase extends MAgentDatabase {

	private static EPubMedDatabase _instance;
	
	public static EPubMedDatabase getInstance() {
		if(_instance==null) _instance = new EPubMedDatabase();
		return _instance;
	}
	
	private EPubMedDatabase() {
		super("http://purl.org/domeo/entity/resource/PubMed", "PubMed NCBI - National Center for Biotechnology Information", "http://www.ncbi.nlm.nih.gov/pubmed/", "<noversion>");
	}
}
