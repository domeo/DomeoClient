package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.highlight.model.MHighlightAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationHighlight;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

import com.google.gwt.core.client.JavaScriptObject;

public class HighlightJsonUnmarshaller extends AUnmarshaller implements IUnmarshaller {

	private IDomeo _domeo;
	
	public HighlightJsonUnmarshaller(IDomeo domeo) { _domeo = domeo; }
	

	public Object unmarshall(
			JsonUnmarshallingManager manager, 
			JavaScriptObject json, 
			String validation,
			MAnnotationSet set, 
			ArrayList<MSelector> selectors
			) {
		validation(json, validation, set, selectors);
		
		JsAnnotationHighlight highlight = (JsAnnotationHighlight) json;
		MHighlightAnnotation ann = AnnotationFactory.cloneHighlight(
				highlight.getId(), 
				highlight.getLineageUri(), 
				highlight.getFormattedCreatedOn(),
				highlight.getFormattedLastSaved(),
				set,
				_domeo.getAgentManager().getAgentByUri(highlight.getCreatedBy()),
				(ISoftware)_domeo.getAgentManager().getAgentByUri(highlight.getCreatedWith()),
				// _domeo.getAgentManager().getUserPerson(), // TODO fix with the right user 
				highlight.getVersionNumber(),
				highlight.getPreviousVersion(),
				_domeo.getPersistenceManager().getCurrentResource(),
				selectors,
				highlight.getLabel());
		
		return ann;
	}

	protected void validation(JavaScriptObject json, String validation, MAnnotationSet set, 
			ArrayList<MSelector> selectors) {
		// TODO Auto-generated method stub
		
	}
}
