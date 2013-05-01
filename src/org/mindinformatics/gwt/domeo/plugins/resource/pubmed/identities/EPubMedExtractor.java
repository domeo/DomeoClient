package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.identities;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class EPubMedExtractor extends MAgentSoftware {

private static EPubMedExtractor _instance;
	
	public static EPubMedExtractor getInstance() {
		if(_instance==null) _instance = new EPubMedExtractor();
		return _instance;
	}
	
	private EPubMedExtractor() {
		super("http://purl.org/domeo/entity/plugin/pubmed/v2", "PubMed Extractor", "2", "001");
	}
}
