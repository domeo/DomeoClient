package org.mindinformatics.gwt.domeo.plugins.resource.opentrials.identities;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentDatabase;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class EOpenTrialsDatabase extends MAgentDatabase {

	private static EOpenTrialsDatabase _instance;
	
	public static EOpenTrialsDatabase getInstance() {
		if(_instance==null) _instance = new EOpenTrialsDatabase();
		return _instance;
	}
	
	private EOpenTrialsDatabase() {
		super("http://purl.org/domeo/entity/resource/OpenTrials", "OpenTrials - Lilly Open Clinical Trials", "http://opentrials.org/", "<noversion>");
	}
}
