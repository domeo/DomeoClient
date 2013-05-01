package org.mindinformatics.gwt.domeo.plugins.resource.opentrials.identities;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class EOpenTrialsExtractorPlugin extends MAgentSoftware {

private static EOpenTrialsExtractorPlugin _instance;
	
	public static EOpenTrialsExtractorPlugin getInstance() {
		if(_instance==null) _instance = new EOpenTrialsExtractorPlugin();
		return _instance;
	}
	
	private EOpenTrialsExtractorPlugin() {
		super("http://www.commonsemantics.com/software/Domeo/plugin/opentrials/1", "OpenTrials Extractor Plugin", "1", "001");
	}
}
