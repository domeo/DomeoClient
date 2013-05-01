package org.mindinformatics.gwt.domeo.plugins.resource.omim.extractors;

import java.util.Date;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.omim.identities.EOmimDatabase;
import org.mindinformatics.gwt.domeo.plugins.resource.omim.identities.EOmimExtractor;
import org.mindinformatics.gwt.domeo.plugins.resource.omim.model.MOmimDocument;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;
import org.mindinformatics.gwt.framework.src.commands.InitUserManagerCommandCallback;

public class OmimExtractMetaCommand implements ICommand {

	IDomeo _domeo;
	InitUserManagerCommandCallback _callback;
	ICommandCompleted _completionCallback;
	
	public OmimExtractMetaCommand(IDomeo domeo, InitUserManagerCommandCallback callback,  
			ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_domeo = domeo;
		_callback = callback;
	}
	
	@Override
	public void execute() {
		_domeo.getLogger().debug(this.getClass().getName(), 
			"Extracting meta tags content...");
		try {
			MOmimDocument currentResource = new MOmimDocument();
			currentResource.setUrl(_domeo.getContentPanel().getAnnotationFrameWrapper().getUrl());
			currentResource.setSource(EOmimDatabase.getInstance());
			currentResource.setLabel(_domeo.getContentPanel().getAnnotationFrameWrapper().getDocumentTitle());
			currentResource.setDescription(_domeo.getContentPanel().getAnnotationFrameWrapper().getDocumentDescription());
			currentResource.setCreatedOn(new Date());
			currentResource.setCreator(EOmimExtractor.getInstance());
			_domeo.getPersistenceManager().setCurrentResource(currentResource);
			_completionCallback.notifyStageCompletion(); // Necessary as synchronous
		} catch (Exception e) {
			_domeo.getLogger().exception(this, 
				"Exception while extracting meta tags content " + e.getMessage());
		}
	}
}
