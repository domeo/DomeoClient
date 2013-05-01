package org.mindinformatics.gwt.domeo.plugins.resource.document.extractor;

import java.util.Date;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.document.identities.EGenericDocumentExtractor;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;
import org.mindinformatics.gwt.domeo.services.extractors.IContentExtractorCallback;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

/**
*
* @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
*/
public class GenericDocumentExtractMetadataCommand implements ICommand {

	IDomeo _domeo;
	IContentExtractorCallback _callback;
	ICommandCompleted _completionCallback;
	
	public GenericDocumentExtractMetadataCommand(IDomeo domeo, IContentExtractorCallback callback,  
			ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_domeo = domeo;
		_callback = callback;
		
	}
	
	@Override
	public void execute() {
		MDocumentResource currentResource = new MDocumentResource();
		currentResource.setUrl(_domeo.getContentPanel().getAnnotationFrameWrapper().getUrl());
		currentResource.setLabel(_domeo.getContentPanel().getAnnotationFrameWrapper().getDocumentTitle());
		currentResource.setDescription(_domeo.getContentPanel().getAnnotationFrameWrapper().getDocumentDescription());
		currentResource.setKeywords(_domeo.getContentPanel().getAnnotationFrameWrapper().getDocumentKeywords());
		currentResource.setCreatedOn(new Date());
		currentResource.setCreator(EGenericDocumentExtractor.getInstance());
		_domeo.getPersistenceManager().setCurrentResource(currentResource);
		
		_completionCallback.notifyStageCompletion(); // Necessary as synchronous
	}
}
