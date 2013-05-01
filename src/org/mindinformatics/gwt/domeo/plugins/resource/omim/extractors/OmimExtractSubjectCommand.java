package org.mindinformatics.gwt.domeo.plugins.resource.omim.extractors;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.omim.identities.EOmimDatabase;
import org.mindinformatics.gwt.domeo.plugins.resource.omim.model.MOmimDocument;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;
import org.mindinformatics.gwt.framework.src.commands.InitUserManagerCommandCallback;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.NodeList;

public class OmimExtractSubjectCommand implements ICommand {

	IDomeo _domeo;
	InitUserManagerCommandCallback _callback;
	ICommandCompleted _completionCallback;
	
	public OmimExtractSubjectCommand(IDomeo domeo, InitUserManagerCommandCallback callback,  
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
			MLinkedResource term = ResourcesFactory.createLinkedResource(_domeo.getContentPanel().getAnnotationFrameWrapper().getUrl()+"#self", "");
			term.setSource(EOmimDatabase.getInstance());
			
			IFrameElement iframe = IFrameElement.as(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement());
			final Document frameDocument = iframe.getContentDocument();
			com.google.gwt.dom.client.Element base = frameDocument.getElementById("results");
			
			NodeList<com.google.gwt.dom.client.Element> tables = base.getElementsByTagName("table");
			NodeList<com.google.gwt.dom.client.Element> rows = tables.getItem(0).getElementsByTagName("tr");
			term.setLabel(rows.getItem(1).getInnerText());
			//term.setSynonyms(rows.getItem(4).getElementsByTagName("td").getItem(0).getInnerHTML().split("<br>"));
			String[] syn = rows.getItem(4).getElementsByTagName("td").getItem(0).getInnerHTML().split("<br>");
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<syn.length; i++) {
				sb.append(syn[i]);
				if(i<syn.length-1) sb.append(",");
			}
			
			((MOmimDocument)_domeo.getPersistenceManager().getCurrentResource()).setIsAbout(term);

			_domeo.getLogger().debug(this.getClass().getName(), 
				"Extraction of document basic info completed in " + (System.currentTimeMillis()-start) + "ms");
			
			_completionCallback.notifyStageCompletion(); // Necessary as synchronous
		} catch (Exception e) {
			_domeo.getLogger().exception(this, 
				"Exception while extracting document basic info " + e.getMessage());
		}
	}
}
