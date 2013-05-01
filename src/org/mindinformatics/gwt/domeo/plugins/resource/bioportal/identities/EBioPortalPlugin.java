package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.identities;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class EBioPortalPlugin extends MAgentSoftware {

private static EBioPortalPlugin _instance;
	
	public static EBioPortalPlugin getInstance() {
		if(_instance==null) _instance = new EBioPortalPlugin();
		return _instance;
	}
	
	private EBioPortalPlugin() {
		super("http://www.commonsemantics.com/software/Domeo/plugin/bioportal/1", "NCBO BioPortal Plugin", "1", "001");
	}
}
