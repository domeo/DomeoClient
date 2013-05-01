package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.textmine;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling.JsonUnmarshallingManager;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.IBioPortalTextminingRequestCompleted;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;

public class TextMiningManager implements IBioPortalTextminingRequestCompleted {

	IDomeo _domeo;
	
	public TextMiningManager(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public void returnTextminingResults(JsAnnotationSet set) {
		
		_domeo.getLogger().debug(this, "Textmining results received...");
		_domeo.getProgressPanelContainer().setProgressMessage("Textmining results received...");
		
		JsonUnmarshallingManager manager = _domeo.getUnmarshaller();
		
		try {
			manager.unmarshallTextmining(set);
			// Message of completion
			
			// TODO Hidious!!!!!
			IFrameElement iframe = IFrameElement.as(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement());
			final Document frameDocument = iframe.getContentDocument();
			_domeo.getContentPanel().getAnnotationFrameWrapper().clearSelection(frameDocument);
			
			_domeo.getToolbarPanel().deselectAnalyze();
			_domeo.getProgressPanelContainer().setCompletionMessage("Textmining completed!");
			_domeo.getLogger().info(this, "Textmining completed!");
			_domeo.refreshAllComponents();
		} catch(Exception e) {
			textMiningNotCompleted();
		} finally {
			// Put here common code
		}
	}
	
	@Override
	public void textMiningNotCompleted(String message) {
		IFrameElement iframe = IFrameElement.as(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement());
		final Document frameDocument = iframe.getContentDocument();
		_domeo.getContentPanel().getAnnotationFrameWrapper().clearSelection(frameDocument);
		
		_domeo.getToolbarPanel().deselectAnalyze();
		_domeo.getProgressPanelContainer().setErrorMessage("Textmining not completed! " + message);
		_domeo.getLogger().info(this, "Textmining not completed!");
	}
	
	@Override
	public void textMiningNotCompleted() {
		IFrameElement iframe = IFrameElement.as(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement());
		final Document frameDocument = iframe.getContentDocument();
		_domeo.getContentPanel().getAnnotationFrameWrapper().clearSelection(frameDocument);
		
		_domeo.getToolbarPanel().deselectAnalyze();
		_domeo.getProgressPanelContainer().setErrorMessage("Textmining not completed!");
		_domeo.getLogger().info(this, "Textmining not completed!");
	}
}
