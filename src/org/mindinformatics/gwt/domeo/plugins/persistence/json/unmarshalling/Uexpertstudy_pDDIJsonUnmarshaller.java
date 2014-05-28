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
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.JsoDDI_DrugEntityUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.JsoDDI_PKDDIUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Jsoexpertstudy_pDDIUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationexpertstudy_pDDI;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class Uexpertstudy_pDDIJsonUnmarshaller extends AUnmarshaller implements
		IUnmarshaller, Iexpertstudy_pDDI {
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
		Mexpertstudy_pDDIAnnotation ann = AnnotationFactory
				.cloneexpertstudy_pDDI(
						pddi.getId(),
						pddi.getLineageUri(),
						pddi.getFormattedCreatedOn(),
						pddi.getFormattedLastSaved(),
						set,
						_domeo.getAgentManager().getAgentByUri(
								pddi.getCreatedBy()),
						(ISoftware) _domeo.getAgentManager().getAgentByUri(
								pddi.getCreatedWith()),
						pddi.getVersionNumber(), pddi.getPreviousVersion(),
						_domeo.getPersistenceManager().getCurrentResource(),
						selectors, pddi.getLabel(), body, // TODO: this doesn't
															// seem to do
															// anything and
															// can likely be
															// removed from the
															// class
						null);
		_domeo.getLogger().debug(this,
				"AnnotationFactory.cloneSPLs done. ann:" + ann.toString());

		// unmarshall the SPL specific entities. There should be on body for
		// each semantic tag
		_domeo.getLogger()
				.debug(this,
						"Parsing body resources and adding them to the annotation instance");

		// get body of annotation
		Jsoexpertstudy_pDDIUsage ddibody = pddi.getBodies().get(0).getSets();

		// get DDI
		JsoDDI_PKDDIUsage pkddi = ddibody.getPKDDI().get(0);

		// there are two drug participant in DDI
		JsArray<JsoDDI_DrugEntityUsage> drugs = pkddi.getDrugs();

		/*
		 * drug entities
		 */
		for (int index = 0; index < drugs.length(); index++) {

			JsoDDI_DrugEntityUsage drug = drugs.get(index);

			if (drug != null) {
				String jsLabel = drug.getLabel();
				String jsDescript = drug.getDescription();
				String jsURI = drug.getId();

				MLinkedResource drugEntity = ResourcesFactory
						.createLinkedResource(jsURI, jsLabel, jsDescript);

				_domeo.getLogger().debug(
						this,
						"drug entity " + (index + 1)
								+ " linked resource created: " + jsLabel);

				if (index == 0) {
					ann.setDrug1(drugEntity);
				} else {
					ann.setDrug2(drugEntity);
				}
			}

			/*
			 * drug entity has type
			 */

			String typeScript = drug.getType();
			if (typeScript != null && !typeScript.trim().equals("")) {
				String typeURI = typeScript.replace("dikbD2R:", DIKBD2R_PREFIX);
				String typeLabel = typeScript.substring(8);
				String typeDescript = "Referred to the type of the mention within the sentence for drug.";

				MLinkedResource type = ResourcesFactory.createLinkedResource(
						typeURI, typeLabel, typeDescript);

				if (index == 0) {
					ann.setType1(type);
				} else {
					ann.setType2(type);
				}
			}

			/*
			 * drug entity has role
			 */

			String roleScript = drug.getRole();
			if (roleScript != null && !roleScript.trim().equals("")) {
				String roleURI = roleScript.replace("dikbD2R:", DIKBD2R_PREFIX);
				String roleLabel = roleScript.substring(8);
				String roleDescript = "Referred to the role that each drug one plays within the interaction.";

				MLinkedResource role = ResourcesFactory.createLinkedResource(
						roleURI, roleLabel, roleDescript);
				if (index == 0) {
					ann.setRole1(role);
				} else {
					ann.setRole2(role);
				}
			}
		}

		/*
		 * statement
		 */

		String stateScript = ddibody.getStatement();
		if (stateScript != null && !stateScript.trim().equals("")) {
			String jsLabel = stateScript.substring(5);
			String jsDescript = "Referred to data type that is described in the sentence.";
			String jsURI = stateScript.replace("ncit:", NCIT_PREFIX);

			MLinkedResource statement = ResourcesFactory.createLinkedResource(
					jsURI, jsLabel, jsDescript);

			_domeo.getLogger().debug(this,
					"statement linked resource created: " + jsLabel);

			ann.setStatement(statement);

		}

		/*
		 * modality
		 */
		String modalityScript = ddibody.getModality();

		if (modalityScript != null&& !modalityScript.trim().equals("")) {
			String jsLabel = modalityScript.substring(5);
			String jsDescript = "Referred to extra sources of information that the annotator considers "
					+ "are helpful to provide evidence for or against the existence of the pDDI.";
			String jsURI = modalityScript.replace("ncit:", NCIT_PREFIX);

			MLinkedResource modality = ResourcesFactory.createLinkedResource(
					jsURI, jsLabel, jsDescript);

			_domeo.getLogger().debug(this,
					"modality linked resource created: " + jsLabel);

			ann.setModality(modality);
		}
		return ann;
	}

	protected void validation(JavaScriptObject json, String validation,
			MAnnotationSet set, ArrayList<MSelector> selectors) {
		// TODO Auto-generated method stub

	}
}
