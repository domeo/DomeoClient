package org.mindinformatics.gwt.domeo.plugins.resource.document.identities;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class EGenericDocumentExtractor extends MAgentSoftware {

private static EGenericDocumentExtractor _instance;
	
	public static EGenericDocumentExtractor getInstance() {
		if(_instance==null) _instance = new EGenericDocumentExtractor();
		return _instance;
	}
	
	private EGenericDocumentExtractor() {
		super("http://www.commonsemantics.com/software/Domeo/plugin/document/1", "Generic Document Extractor Plugin", "1", "001");
	}
}
