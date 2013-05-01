package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2;

import java.util.Date;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model.MPubMedDocument;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.identities.EPubMedCentralDatabase;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.identities.EPubMedCentralExtractor;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * Stage for extraction of basic metadata from the HTML page.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PubMedCentralExtractMetaCommand implements ICommand {

	public static final String FUNCTION = IBibliographicParameters.EXTRACT_META;
	
	IDomeo _domeo;
	ICommandCompleted _completionCallback;
	
	public PubMedCentralExtractMetaCommand(IDomeo domeo, ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_domeo = domeo;	
	}

	@Override
	public void execute() {
		_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, 
				this, "Extracting document basic metadata");
	
		try {
			MPubMedDocument currentResource = new MPubMedDocument();
			currentResource.setUrl(_domeo.getContentPanel().getAnnotationFrameWrapper().getUrl());
			currentResource.setSource(EPubMedCentralDatabase.getInstance());
			currentResource.setLabel(_domeo.getContentPanel().getAnnotationFrameWrapper().getDocumentTitle());
			currentResource.setDescription(_domeo.getContentPanel().getAnnotationFrameWrapper().getDocumentDescription());
			currentResource.setCreator(EPubMedCentralExtractor.getInstance());
			currentResource.setCreatedOn(new Date());
			
			_domeo.getPersistenceManager().setCurrentResource(currentResource);
			_completionCallback.notifyStageCompletion(); // Necessary as synchronous
		} catch (Exception e) {
			_domeo.getLogger().exception(this, 
				"Exception while extracting document basic metadata " + e.getMessage());
		}
	}
}
