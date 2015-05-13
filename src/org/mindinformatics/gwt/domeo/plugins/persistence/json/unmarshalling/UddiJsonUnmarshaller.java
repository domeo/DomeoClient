package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.contentasrdf.model.MContentAsRdf;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.Iddi;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.JsoDDI_DrugEntityUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.JsoDDI_PKDDIUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.JsoddiUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.MddiAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationddi;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class UddiJsonUnmarshaller extends AUnmarshaller implements
		IUnmarshaller, Iddi {
	private IDomeo _domeo;

	public UddiJsonUnmarshaller(IDomeo domeo) {
		_domeo = domeo;
	}

	public Object unmarshall(JsonUnmarshallingManager manager,
			JavaScriptObject json, String validation, MAnnotationSet set,
			ArrayList<MSelector> selectors) {

		System.out.println("[INFO] unmarshaller for ddi annotation ...");

		validation(json, validation, set, selectors);

		JsAnnotationddi pddi = (JsAnnotationddi) json;

		MContentAsRdf body = new MContentAsRdf();

		_domeo.getLogger().debug(
				this,
				"Calling AnnotationFactory.cloneSPLs with body"
						+ body.toString());
		MddiAnnotation ann = AnnotationFactory.cloneddi(pddi.getId(), pddi
				.getLineageUri(), pddi.getFormattedCreatedOn(), pddi
				.getFormattedLastSaved(), set, _domeo.getAgentManager()
				.getAgentByUri(pddi.getCreatedBy()), (ISoftware) _domeo
				.getAgentManager().getAgentByUri(pddi.getCreatedWith()), pddi
				.getVersionNumber(), pddi.getPreviousVersion(), _domeo
				.getPersistenceManager().getCurrentResource(), selectors, pddi
				.getLabel(), body, // TODO: this doesn't
									// seem to do
									// anything and
									// can likely be
									// removed from the
									// class
				null);
		System.out.println("AnnotationFactory.cloneSPLs done. ann:"
				+ ann.toString());

		// unmarshall the SPL specific entities. There should be on body for
		// each semantic tag
		System.out
				.println("Parsing body resources and adding them to the annotation instance");

		// get body of annotation
		JsoddiUsage ddibody = pddi.getBodies().get(0).getSets();

		/*
		 * assertion type
		 */
		String assertionTypeScript = pddi.getAssertionType();
		// System.out.println("assertionTypeScript:" + assertionTypeScript);

		if (assertionTypeScript != null
				&& !assertionTypeScript.trim().equals("")) {
			String jsLabel = assertionTypeScript;
			String jsDescript = "assertion type (DDI or increase AUC).";
			String jsURI = DIKBD2R_PREFIX + jsLabel;

			MLinkedResource assertionType = ResourcesFactory
					.createLinkedResource(jsURI, jsLabel, jsDescript);

			// System.out.println("assertionType linked resource created: " +
			// jsLabel);

			ann.setAssertType(assertionType);
		}

		// get DDI
		JsoDDI_PKDDIUsage pkddi = ddibody.getPKDDI().get(0);

		// there are two drug participant in DDI
		JsArray<JsoDDI_DrugEntityUsage> drugs = pkddi.getDrugs();

		/*
		 * drug entities
		 */

		if (drugs != null) {

			// System.out.println("num of drugs: " + drugs.length());

			for (int index = 0; index < drugs.length(); index++) {

				JsoDDI_DrugEntityUsage drug = drugs.get(index);

				// String jsURI = drug.getId(); // NOTE: technically, there is
				// UUID for the drug entity but we use the RxCUI for
				// unmarshalling
				String jsURI = drug.getRxcui();

				if (drug != null) {
					System.out.println("drug rxcui: " + jsURI);

					String jsLabel = drug.getLabel();
					String jsDescript = drug.getDescription();

					MLinkedResource drugEntity = ResourcesFactory
							.createLinkedResource(jsURI, jsLabel, jsDescript);

					System.out.println("drug entity " + (index + 1)
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

				if (drug != null && !drug.getLabel().trim().equals("")) {
					String typeScript = drug.getType();
					if (typeScript != null && !typeScript.trim().equals("")) {
						String typeURI = typeScript.replace("dikbD2R:",
								DIKBD2R_PREFIX);
						String typeLabel = typeScript.substring(8);
						String typeDescript = "Referred to the type of the mention within the sentence for drug.";

						System.out.println("type: " + typeLabel + "|" + typeURI
								+ "|");

						MLinkedResource type = ResourcesFactory
								.createLinkedResource(typeURI, typeLabel,
										typeDescript);

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
						String roleURI = roleScript.replace("dikbD2R:",
								DIKBD2R_PREFIX);
						String roleLabel = roleScript.substring(8);
						String roleDescript = "Referred to the role that each drug one plays within the interaction.";

						System.out.println("role: " + roleLabel + "|" + roleURI
								+ "|");

						MLinkedResource role = ResourcesFactory
								.createLinkedResource(roleURI, roleLabel,
										roleDescript);
						if (index == 0) {
							ann.setRole1(role);
						} else {
							ann.setRole2(role);
						}

						/*
						 * drug entity has dose
						 */

						if (assertionTypeScript.equals("increase-auc")) {
							String doseScript = drug.getDose();

							// System.out.println("dose: " + doseScript);

							if (doseScript != null
									&& !doseScript.trim().equals("")) {
								// String doseURI =
								// doseScript.replace("dikbD2R:",DIKBD2R_PREFIX);
								String doseURI = DIKBD2R_PREFIX + doseScript;
								String doseLabel = doseScript;

								String doseDescript = "Referred to the dose that each drug within the interaction.";

								// System.out.println("dose: " + doseLabel + "|"
								// + doseURI + "|");

								MLinkedResource dose = ResourcesFactory
										.createLinkedResource(doseURI,
												doseLabel, doseDescript);
								if (roleLabel.toLowerCase().contains("object")) {
									ann.setObjectDose(dose);
								} else {
									ann.setPreciptDose(dose);
								}
							}

						}
					}

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

		if (modalityScript != null && !modalityScript.trim().equals("")) {
			String jsLabel = modalityScript.substring(5);
			String jsDescript = "Referred to extra sources of information that the annotator considers "
					+ "are helpful to provide evidence for or against the existence of the pDDI.";
			String jsURI = modalityScript.replace("ncit:", NCIT_PREFIX);

			// System.out.println("modality: " + jsLabel + "|" + jsURI + "|");

			MLinkedResource modality = ResourcesFactory.createLinkedResource(
					jsURI, jsLabel, jsDescript);

			// System.out.println("modality linked resource created: " +
			// jsLabel);

			ann.setModality(modality);
		}

		/*
		 * increase Auc fields
		 */

		if (assertionTypeScript.equals("increase-auc")) {

			/*
			 * number of participants
			 */
			String numPartScript = pkddi.getNumOfParticipants();

			// System.out.println("numPartScript:" + numPartScript);

			if (numPartScript != null && !numPartScript.trim().equals("")) {
				String jsLabel = numPartScript;
				String jsDescript = "The number of participants involved in interaction.";
				String jsURI = DIKBD2R_PREFIX + numPartScript;

				System.out.println("num participants: " + jsLabel + "|" + jsURI
						+ "|");

				MLinkedResource numParticipt = ResourcesFactory
						.createLinkedResource(jsURI, jsLabel, jsDescript);

				// System.out.println("numParticipt linked resource created: " +
				// jsLabel);

				ann.setNumOfparcipitants(numParticipt);
			}

			/*
			 * increase AUC
			 */
			String aucScript = pkddi.getIncreaseAUC();
			// System.out.println("aucScript:" + aucScript);

			if (aucScript != null && !aucScript.trim().equals("")) {
				String jsLabel = aucScript;
				String jsDescript = "The maximum increasing Auc in interation.";
				String jsURI = DIKBD2R_PREFIX + aucScript;

				System.out.println("auc: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource auc = ResourcesFactory.createLinkedResource(
						jsURI, jsLabel, jsDescript);

				// System.out.println("AUC linked resource created: " +
				// jsLabel);

				ann.setIncreaseAuc(auc);
			}
		}

		/*
		 * evidence type
		 */

		String evidenceTypeScript = pddi.getEvidenceType();
		// System.out.println("evidenceTypeScript:" + evidenceTypeScript);

		if (evidenceTypeScript != null && !evidenceTypeScript.trim().equals("")) {
			String jsLabel = evidenceTypeScript;
			String jsDescript = "evidence that supports or challenges drug-drug interaction assertion.";
			String jsURI = DIKBD2R_PREFIX + jsLabel;

			MLinkedResource evidenceType = ResourcesFactory
					.createLinkedResource(jsURI, jsLabel, jsDescript);

			// System.out.println("evidenceType linked resource created: " +
			// jsLabel);

			ann.setEvidenceType(evidenceType);
		}

		return ann;
	}

	protected void validation(JavaScriptObject json, String validation,
			MAnnotationSet set, ArrayList<MSelector> selectors) {
		// TODO Auto-generated method stub

	}
}
