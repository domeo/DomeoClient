package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.contentasrdf.model.MContentAsRdf;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSPL;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

import com.google.gwt.core.client.JavaScriptObject;

public class USPLJsonUnmarshaller extends AUnmarshaller implements IUnmarshaller {

	private IDomeo _domeo;
	
	public USPLJsonUnmarshaller(IDomeo domeo) { _domeo = domeo; }
	

	public Object unmarshall(
			JsonUnmarshallingManager manager, 
			JavaScriptObject json, 
			String validation,
			MAnnotationSet set, 
			ArrayList<MSelector> selectors
			) {
		validation(json, validation, set, selectors);
		
		JsAnnotationSPL spl = (JsAnnotationSPL) json;
		
		MContentAsRdf body = new MContentAsRdf();
		body.setIndividualUri(spl.getBody().get(0).getId());
		body.setFormat(spl.getBody().get(0).getFormat());
		body.setChars(spl.getBody().get(0).getChars());
		
		MSPLsAnnotation ann = AnnotationFactory.cloneSPLs(
			spl.getId(), 
			spl.getLineageUri(), 
			spl.getFormattedCreatedOn(),
			spl.getFormattedLastSaved(),
			set,
			_domeo.getAgentManager().getAgentByUri(spl.getCreatedBy()),
			(ISoftware)_domeo.getAgentManager().getAgentByUri(spl.getCreatedWith()),
			spl.getVersionNumber(),
			spl.getPreviousVersion(),
			_domeo.getPersistenceManager().getCurrentResource(),
			selectors,
			spl.getLabel(),
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
