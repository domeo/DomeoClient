package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.identities;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentDatabase;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class EBioPortalDatabase extends MAgentDatabase {

	private static EBioPortalDatabase _instance;
	
	public static EBioPortalDatabase getInstance() {
		if(_instance==null) _instance = new EBioPortalDatabase();
		return _instance;
	}
	
	private EBioPortalDatabase() {
		super("http://purl.org/domeo/entity/resource/BioPortal", "BioPortal - NCBO National Center for Biomedical Ontologies", "http://bioportal.bioontology.org/", "<noversion>");
	}
}
