package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2;

import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.bibliography.src.IBibliographicParameters;
import org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.model.JsPmcImage;
import org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.service.IPmcImagesRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.src.PmcImagesMetadataCache;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model.MPubMedDocument;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
 * Stage for extraction of basic metadata from the HTML page.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PubMedCentralExtractImageInfoCommand implements ICommand, IPmcImagesRequestCompleted {

	public static final String FUNCTION = IBibliographicParameters.EXTRACT_META;
	
	IDomeo _domeo;
	ICommandCompleted _completionCallback;
	
	public PubMedCentralExtractImageInfoCommand(IDomeo domeo, ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_domeo = domeo;	
	}

	@Override
	public void execute() {
		_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, 
				this, "Retrieving images metadata");
	
		try {
			String pmid = ((MPublicationArticleReference)((MPubMedDocument)_domeo.getPersistenceManager().getCurrentResource()).getSelfReference().getReference()).getPubMedId();
			String pmcid = ((MPublicationArticleReference)((MPubMedDocument)_domeo.getPersistenceManager().getCurrentResource()).getSelfReference().getReference()).getPubMedCentralId();
			String doi = ((MPublicationArticleReference)((MPubMedDocument)_domeo.getPersistenceManager().getCurrentResource()).getSelfReference().getReference()).getDoi();

			if(pmid!=null || pmcid!=null || doi!=null) {
				PmcImagesMetadataCache imageMetadataCache = _domeo.getPmcImagesCache();
				imageMetadataCache.init();
				imageMetadataCache.retrievePmcImagesData(this, pmid, pmcid, doi);			
			} else {
				_completionCallback.notifyStageCompletion(); // Necessary as synchronous
			}
		} catch (Exception e) {
			_domeo.getLogger().exception(this, 
				"Exception while retrieving images metadata " + e.getMessage());
			_completionCallback.notifyStageCompletion();
		}
	}

	@Override
	public void returnPmcImagesData(HashMap<String, JsPmcImage> images) {
		 _domeo.getPmcImagesCache().setCache(images);
		 _completionCallback.notifyStageCompletion(); // Necessary as synchronous
	}

	@Override
	public void pmcImagesDataNotFound() {
		// TODO Auto-generated method stub
		_completionCallback.notifyStageCompletion(); // Necessary as synchronous
	}

	@Override
	public void pmcImagesDataNotFound(String message) {
		// TODO Auto-generated method stub
		_completionCallback.notifyStageCompletion(); // Necessary as synchronous
	}
}
