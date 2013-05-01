package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;
import org.mindinformatics.gwt.framework.src.commands.InitUserManagerCommandCallback;

public class PubMedExtractBibliographicCitationsCommand implements ICommand {

	IDomeo _domeo;
	InitUserManagerCommandCallback _callback;
	ICommandCompleted _completionCallback;
	
	public PubMedExtractBibliographicCitationsCommand(IDomeo domeo, InitUserManagerCommandCallback callback,  
			ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_domeo = domeo;
		_callback = callback;
		
	}
	
	@Override
	public void execute() {
		_domeo.getLogger().debug(PubMedDocumentPipeline.LOGGER, 
				this, "Extracting document bibliography for Pubmed document");
		_completionCallback.notifyStageCompletion(); 
	}
}
