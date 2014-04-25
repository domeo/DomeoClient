package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.contentasrdf.model.MContentAsRdf;
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

public class Uexpertstudy_pDDIJsonUnmarshaller extends AUnmarshaller implements
IUnmarshaller{
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
		JsArray<Jsoexpertstudy_pDDIUsage> aus = pddi.getBodies();

		//Set<MLinkedResource> statements = new HashSet<MLinkedResource>();

		for (int j = 0; j < aus.length(); j++) {
			Jsoexpertstudy_pDDIUsage au = aus.get(j);

			// drug 1
			String jsdrug1 = au.getDrug1();
			if (jsdrug1 != null) {
				_domeo.getLogger().debug(this,
						"jsdrug1 is not null: " + jsdrug1.toString());

				// TODO: throw an exception if the label or description is null
				String jsLabel = au.getLabel();
				String jsDescript = au.getDescription();
				MLinkedResource drug1 = ResourcesFactory
						.createLinkedResource(jsdrug1, jsLabel, jsDescript);
				_domeo.getLogger().debug(
						this,
						"drug1 linked resource created: "
								+ drug1.getLabel());

				ann.setDrug1(drug1);
				_domeo.getLogger().debug(this,
						"drug1 linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "jsdrug1 is null");
			}

		}
		return ann;
	}

	protected void validation(JavaScriptObject json, String validation,
			MAnnotationSet set, ArrayList<MSelector> selectors) {
		// TODO Auto-generated method stub

	}
}


