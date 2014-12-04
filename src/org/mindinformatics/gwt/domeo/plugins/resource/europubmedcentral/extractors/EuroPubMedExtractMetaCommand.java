package org.mindinformatics.gwt.domeo.plugins.resource.europubmedcentral.extractors;

import java.util.Date;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.identities.EPubMedDatabase;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.identities.EPubMedExtractor;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model.MPubMedDocument;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

public class EuroPubMedExtractMetaCommand implements ICommand {

	private IDomeo _domeo;
	private ICommandCompleted _completionCallback;
	
	public EuroPubMedExtractMetaCommand(IDomeo domeo, ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_domeo = domeo;	
	}
	
	@Override
	public void execute() {
		_domeo.getLogger().debug(EuroPubMedDocumentPipeline.LOGGER, 
				this, "Extracting document basic info for Pubmed document");
	
		try {
			MPubMedDocument currentResource = new MPubMedDocument();
			currentResource.setUrl(_domeo.getContentPanel().getAnnotationFrameWrapper().getUrl());
			currentResource.setSource(EPubMedDatabase.getInstance());
			currentResource.setLabel(_domeo.getContentPanel().getAnnotationFrameWrapper().getDocumentTitle());
			currentResource.setDescription(_domeo.getContentPanel().getAnnotationFrameWrapper().getDocumentDescription());
			currentResource.setCreatedOn(new Date());
			currentResource.setCreator(EPubMedExtractor.getInstance());
			
			_domeo.getPersistenceManager().setCurrentResource(currentResource);
			_completionCallback.notifyStageCompletion(); // Necessary as synchronous
		} catch (Exception e) {
			_domeo.getLogger().exception(this, 
				"Exception while extracting PubMed document basic info " + e.getMessage());
		}
	}
}

