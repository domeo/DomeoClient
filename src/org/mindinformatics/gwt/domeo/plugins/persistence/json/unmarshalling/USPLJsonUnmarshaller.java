package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.contentasrdf.model.MContentAsRdf;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.JsoAntibodyUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.JsoPharmgxUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSPL;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
import org.mindinformatics.gwt.framework.component.resources.serialization.JsonGenericResource;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

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
		//body.setFormat(spl.getBody().get(0).getFormat());
		//body.setChars(spl.getBody().get(0).getChars());
		
		_domeo.getLogger().debug(this, "Calling AnnotationFactory.cloneSPLs with body" + body.toString());
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
		_domeo.getLogger().debug(this, "AnnotationFactory.cloneSPLs done. ann:" + ann.toString());
		JsArray<JsoPharmgxUsage> aus = spl.getBody();
		JsoPharmgxUsage au = aus.get(0);
		//JsonGenericResource jsPkImpact = au.getPKImpact();
		String jsPkImpact = au.getPKImpact();
		if(jsPkImpact != null) {
			_domeo.getLogger().debug(this, "jsPkImpact is not null: " + jsPkImpact.toString());
			
			//TODO: fix the serializer class (JSONSPLsAnnotationSerializer) to place the correct resources in the JSON-LD stored on the server. All data in this resource should be from the JSON returned from the server
			MLinkedResource pkImpact = ResourcesFactory.createTrustedTypedResource(
					jsPkImpact, 
					"Distribution Increase", 
					"The pharmacogenomic biomarker is associated with a increase in distribution of the drug.", 
					"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#PharmacokineticImpact", 
					"http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#", 
					"DUMMY TEST");
			// TODO: the data should have the URL, label, description, type, and source so we can use MTrustedTypedResource directly with (jsPkImpact.getUrl(), jsPkImpact.getLabel());
			_domeo.getLogger().debug(this, "pkImpact linked resource created: " + pkImpact.getLabel());
			
			//TODO: currently, we expect that the pharmgxusage class in MSPLsAnnotation is initialized, this is done in the clone methods above but its odd...find out if there is a simple way to do this
			ann.setPKImpact(pkImpact);
			_domeo.getLogger().debug(this, "pkImpact linked resource added to annotation");									
		} else {
			_domeo.getLogger().debug(this, "jsPkImpact IS null");
		}
		
		return ann;
	}

	protected void validation(JavaScriptObject json, String validation, MAnnotationSet set, 
			ArrayList<MSelector> selectors) {
		// TODO Auto-generated method stub
		
	}
}
