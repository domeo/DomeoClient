package org.mindinformatics.gwt.domeo.plugins.resource.opentrials.extractor;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;
import org.mindinformatics.gwt.framework.src.commands.InitUserManagerCommandCallback;

public class OpenTrialsExtractSubjectCommand implements ICommand {

	IDomeo _domeo;
	InitUserManagerCommandCallback _callback;
	ICommandCompleted _completionCallback;
	
	public OpenTrialsExtractSubjectCommand(IDomeo domeo, InitUserManagerCommandCallback callback,  
			ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_domeo = domeo;
		_callback = callback;
		
	}
	
	@Override
	public void execute() {
		_domeo.getLogger().debug(this.getClass().getName(), 
			"Extracting document basic info...");
		
		try {
			long start = System.currentTimeMillis();
//			MTermIdentifiedByUri term = new MTermIdentifiedByUri();
//			term.setUrl(_domeo.getContentPanel().getAnnotationFrameWrapper().getUrl()+"#self");
//			term.setSource(EOpenTrialsDatabase.getInstance());
//			
//			IFrameElement iframe = IFrameElement.as(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement());
//			final Document frameDocument = iframe.getContentDocument();
//			com.google.gwt.dom.client.Element base = frameDocument.getElementById("results");
//			
//			NodeList<com.google.gwt.dom.client.Element> tables = base.getElementsByTagName("table");
//			NodeList<com.google.gwt.dom.client.Element> rows = tables.getItem(0).getElementsByTagName("tr");
//			term.setLabel(rows.getItem(1).getInnerText());
//			term.setSynonyms(rows.getItem(4).getElementsByTagName("td").getItem(0).getInnerHTML().split("<br>"));
//			
//			((MOpenTrialsDocument)_domeo.getPersistenceManager().getCurrentResource()).setIsAbout(term);

			_domeo.getLogger().debug(this.getClass().getName(), 
				"Extraction of document basic info completed in " + (System.currentTimeMillis()-start) + "ms");
			
			_completionCallback.notifyStageCompletion(); // Necessary as synchronous
		} catch (Exception e) {
			_domeo.getLogger().exception(this, 
				"Exception while extracting document basic info " + e.getMessage());
		}
	}
}
