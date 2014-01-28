package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.contentasrdf.model.MContentAsRdf;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationPostIt;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

import com.google.gwt.core.client.JavaScriptObject;

public class UPostitJsonUnmarshaller extends AUnmarshaller implements IUnmarshaller {

	private IDomeo _domeo;
	
	public UPostitJsonUnmarshaller(IDomeo domeo) { _domeo = domeo; }
	

	public Object unmarshall(
			JsonUnmarshallingManager manager, 
			JavaScriptObject json, 
			String validation,
			MAnnotationSet set, 
			ArrayList<MSelector> selectors
			) {
		validation(json, validation, set, selectors);
		
		JsAnnotationPostIt postIt = (JsAnnotationPostIt) json;
		
		MContentAsRdf body = new MContentAsRdf();
		body.setIndividualUri(postIt.getBody().get(0).getId());
		body.setFormat(postIt.getBody().get(0).getFormat());
		body.setChars(postIt.getBody().get(0).getChars());
		
		MPostItAnnotation ann = AnnotationFactory.clonePostIt(
			postIt.getId(), 
			postIt.getLineageUri(), 
			postIt.getFormattedCreatedOn(),
			postIt.getFormattedLastSaved(),
			set,
			_domeo.getAgentManager().getAgentByUri(postIt.getCreatedBy()),
			(ISoftware)_domeo.getAgentManager().getAgentByUri(postIt.getCreatedWith()),
			postIt.getVersionNumber(),
			postIt.getPreviousVersion(),
			_domeo.getPersistenceManager().getCurrentResource(),
			selectors,
			postIt.getLabel(),
			body,
			null
			);
		
		return ann;
	}

	protected void validation(JavaScriptObject json, String validation, MAnnotationSet set, 
			ArrayList<MSelector> selectors) {
		// TODO Auto-generated method stub
		
	}
}
