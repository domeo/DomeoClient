package org.mindinformatics.gwt.domeo.plugins.resource.omim.identities;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentDatabase;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class EOmimDatabase extends MAgentDatabase {

	private static EOmimDatabase _instance;
	
	public static EOmimDatabase getInstance() {
		if(_instance==null) _instance = new EOmimDatabase();
		return _instance;
	}
	
	private EOmimDatabase() {
		super("http://purl.org/domeo/entity/resource/OMIM", "OMIM - Online Mendelian Inheritance in Man", "http://omim.org/", "<noversion>");
	}
}
