package org.mindinformatics.gwt.domeo.plugins.resource.omim.identities;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class EOmimExtractor extends MAgentSoftware {

private static EOmimExtractor _instance;
	
	public static EOmimExtractor getInstance() {
		if(_instance==null) _instance = new EOmimExtractor();
		return _instance;
	}
	
	private EOmimExtractor() {
		super("http://www.commonsemantics.com/software/Domeo/plugin/omim/1", "OMIM Extractor", "1", "001");
	}
}
