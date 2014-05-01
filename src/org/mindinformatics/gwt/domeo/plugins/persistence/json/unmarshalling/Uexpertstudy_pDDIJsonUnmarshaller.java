package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.contentasrdf.model.MContentAsRdf;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Iexpertstudy_pDDI;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Jsoexpertstudy_pDDIUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.JsoPharmgxUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSPL;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationexpertstudy_pDDI;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONObject;

public class Uexpertstudy_pDDIJsonUnmarshaller extends AUnmarshaller implements
IUnmarshaller, Iexpertstudy_pDDI{
	private IDomeo _domeo;

	public Uexpertstudy_pDDIJsonUnmarshaller(IDomeo domeo) {
		_domeo = domeo;
	}

	public Object unmarshall(JsonUnmarshallingManager manager,
			JavaScriptObject json, String validation, MAnnotationSet set,
			ArrayList<MSelector> selectors) {
		validation(json, validation, set, selectors);

		JsAnnotationexpertstudy_pDDI pddi = (JsAnnotationexpertstudy_pDDI) json;


		MContentAsRdf body = new MContentAsRdf();

		_domeo.getLogger().debug(
				this,
				"Calling AnnotationFactory.cloneSPLs with body"
						+ body.toString());
		Mexpertstudy_pDDIAnnotation ann = AnnotationFactory.cloneexpertstudy_pDDI(pddi.getId(), pddi
				.getLineageUri(), pddi.getFormattedCreatedOn(), pddi
				.getFormattedLastSaved(), set, _domeo.getAgentManager()
				.getAgentByUri(pddi.getCreatedBy()), (ISoftware) _domeo
				.getAgentManager().getAgentByUri(pddi.getCreatedWith()), pddi
				.getVersionNumber(), pddi.getPreviousVersion(), _domeo
				.getPersistenceManager().getCurrentResource(), selectors, pddi
				.getLabel(), body, // TODO: this doesn't seem to do anything and
									// can likely be removed from the class
				null);
		_domeo.getLogger().debug(this,
				"AnnotationFactory.cloneSPLs done. ann:" + ann.toString());

		// unmarshall the SPL specific entities. There should be on body for
		// each semantic tag
		_domeo.getLogger()
				.debug(this,
						"Parsing body resources and adding them to the annotation instance");
		JsArray<Jsoexpertstudy_pDDIUsage> bodies = pddi.getBodies();

		//Set<MLinkedResource> statements = new HashSet<MLinkedResource>();

	  
	    System.out.println("ddi bodies0:"+bodies.length());
		System.out.println("ddi bodies1:"+bodies.get(0).getDrug1());
		System.out.println("ddi bodies2:"+bodies.get(0).getDrug2());
		System.out.println("ddi bodies3:"+bodies.get(0).getStatement());
		System.out.println("ddi bodies4:"+bodies.get(0).getRole1());
		System.out.println("ddi bodies5:"+bodies.get(0).getPKDDI().toString());
		
		
		
		return ann;
	}

	protected void validation(JavaScriptObject json, String validation,
			MAnnotationSet set, ArrayList<MSelector> selectors) {
		// TODO Auto-generated method stub

	}
}


