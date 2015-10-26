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

public class UddiJsonUnmarshaller extends AUnmarshaller implements IUnmarshaller, Iddi {
	private IDomeo _domeo;

	public UddiJsonUnmarshaller(IDomeo domeo) {
		_domeo = domeo;
	}

	public Object unmarshall(JsonUnmarshallingManager manager, JavaScriptObject json, String validation,
			MAnnotationSet set, ArrayList<MSelector> selectors) {

		System.out.println("[INFO] unmarshaller for ddi annotation ...");

		validation(json, validation, set, selectors);

		JsAnnotationddi pddi = (JsAnnotationddi) json;

		MContentAsRdf body = new MContentAsRdf();

		_domeo.getLogger().debug(this, "Calling AnnotationFactory.cloneSPLs with body" + body.toString());
		MddiAnnotation ann = AnnotationFactory.cloneddi(pddi.getId(), pddi.getLineageUri(),
				pddi.getFormattedCreatedOn(), pddi.getFormattedLastSaved(), set,
				_domeo.getAgentManager().getAgentByUri(pddi.getCreatedBy()),
				(ISoftware) _domeo.getAgentManager().getAgentByUri(pddi.getCreatedWith()), pddi.getVersionNumber(),
				pddi.getPreviousVersion(), _domeo.getPersistenceManager().getCurrentResource(), selectors,
				pddi.getLabel(), body, // TODO: this doesn't
										// seem to do
										// anything and
										// can likely be
										// removed from the
										// class
				null);
		System.out.println("AnnotationFactory.cloneSPLs done. ann:" + ann.toString());

		// unmarshall the SPL specific entities. There should be on body for
		// each semantic tag
		System.out.println("Parsing body resources and adding them to the annotation instance");

		// get body of annotation
		JsoddiUsage ddibody = pddi.getBodies().get(0).getSets();

		/*
		 * assertion type
		 */
		String assertionTypeScript = pddi.getAssertionType();
		// System.out.println("assertionTypeScript:" + assertionTypeScript);

		if (assertionTypeScript != null && !assertionTypeScript.trim().equals("")) {
			String jsLabel = assertionTypeScript;
			String jsDescript = "assertion type (DDI or clinical trail).";
			String jsURI = DIKBD2R_PREFIX + jsLabel;


			MLinkedResource assertionType = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

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

			for (int index = 0; index < drugs.length(); index++) {

				JsoDDI_DrugEntityUsage drug = drugs.get(index);

				// String jsURI = drug.getId(); // NOTE: technically, there is
				// UUID for the drug entity but we use the RxCUI for
				// unmarshalling
				String jsURI = drug.getRxcui();

				if (drug != null) {

					String jsLabel = drug.getLabel();
					String jsDescript = drug.getDescription();

					MLinkedResource drugEntity = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

					// System.out.println("drug entity " + (index + 1) + " linked resource created: " + jsLabel);

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
						String typeURI = typeScript.replace("dikbD2R:", DIKBD2R_PREFIX);
						String typeLabel = typeScript.substring(8);
						String typeDescript = "Referred to the type of the mention within the sentence for drug.";

						// System.out.println("type: " + typeLabel + "|" + typeURI + "|");

						MLinkedResource type = ResourcesFactory.createLinkedResource(typeURI, typeLabel, typeDescript);

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


						MLinkedResource role = ResourcesFactory.createLinkedResource(roleURI, roleLabel, roleDescript);
						if (index == 0) {
							ann.setRole1(role);
						} else {
							ann.setRole2(role);
						}

						/*
						 * drug entity has dose
						 */

						if (assertionTypeScript.equals("DDI-clinical-trial")) {
							String doseScript = drug.getDose();

							// System.out.println("dose: " + doseScript);

							if (doseScript != null && !doseScript.trim().equals("")) {

								String doseURI = DIKBD2R_PREFIX + "dose";
								String doseLabel = doseScript;

								String doseDescript = "Referred to the dose that each drug within the interaction.";

								MLinkedResource dose = ResourcesFactory.createLinkedResource(doseURI, doseLabel,
										doseDescript);
								if (roleLabel.toLowerCase().contains("object")) {
									//System.out.println("drug entity " + (index + 1) + " | drug label: " + drug.getLabel() + " | role: " + roleLabel + " | doseage: objectDose " + dose.getLabel());
									ann.setObjectDose(dose);
								} else {
									//System.out.println("drug entity " + (index + 1) + " | drug label: " + drug.getLabel() + " | role: " + roleLabel + " | doseage: preciptDose " + dose.getLabel());
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

			MLinkedResource statement = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

			_domeo.getLogger().debug(this, "statement linked resource created: " + jsLabel);

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

			MLinkedResource modality = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

			// System.out.println("modality linked resource created: " +
			// jsLabel);

			ann.setModality(modality);
		}

		/*
		 * comment
		 */
		String commentScript = ddibody.getComment();

		if (commentScript != null && !commentScript.trim().equals("")) {
			ann.setComment(commentScript);
		}

		/*
		 * DDI-clinical-trial fields
		 */

		if (assertionTypeScript.equals("DDI-clinical-trial")) {

			/*
			 * number of participants
			 */
			String numPartScript = pkddi.getNumOfParticipants();

			// System.out.println("numPartScript:" + numPartScript);

			if (numPartScript != null && !numPartScript.trim().equals("")) {
				String jsLabel = numPartScript;
				String jsDescript = "The number of participants involved in interaction.";
				//String jsURI = DIKBD2R_PREFIX + numPartScript;
				String jsURI = DIKBD2R_PREFIX + "numOfParticipants";
				
				System.out.println("num participants: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource numParticipt = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				// System.out.println("numParticipt linked resource created: " +
				// jsLabel);

				ann.setNumOfparcipitants(numParticipt);
			}

			/*
			 * object Formu
			 */
			String objectFormuScript = pkddi.getObjectFormulation();

			if (objectFormuScript != null && !objectFormuScript.trim().equals("")) {
				String jsLabel = objectFormuScript;
				String jsDescript = "object drug Formulation in DDI clinical trail.";
				String jsURI = DIKBD2R_PREFIX + objectFormuScript;

				System.out.println("objectFormulation: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource objectFormu = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				ann.setObjectFormu(objectFormu);
			}

			/*
			 * object duration
			 */
			String objectDurationScript = pkddi.getObjectDuration();

			if (objectDurationScript != null && !objectDurationScript.trim().equals("")) {
				String jsLabel = objectDurationScript;
				String jsDescript = "objectDuration in DDI clinical trail.";
				//String jsURI = DIKBD2R_PREFIX + objectDurationScript;
				String jsURI = DIKBD2R_PREFIX + "objectDuration";
				
				//System.out.println("objectDuration: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource objectDuration = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				ann.setObjectDuration(objectDuration);
			}

			/*
			 * object regimens
			 */
			String objectRegimensScript = pkddi.getObjectRegimen();
			// System.out.println("objectRegimensScript:" +
			// objectRegimensScript);

			if (objectRegimensScript != null && !objectRegimensScript.trim().equals("")) {
				String jsLabel = objectRegimensScript;
				String jsDescript = "objectRegimens in DDI clinical trail.";
				String jsURI = DIKBD2R_PREFIX + objectRegimensScript;

				System.out.println("objectRegimens: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource objectRegimens = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				// System.out.println("objectRegimens linked resource created: "
				// +
				// jsLabel);

				ann.setObjectRegimen(objectRegimens);
			}

			/*
			 * precipt Formu
			 */
			String preciptFormuScript = pkddi.getPreciptFormulation();

			if (preciptFormuScript != null && !preciptFormuScript.trim().equals("")) {
				String jsLabel = preciptFormuScript;
				String jsDescript = "preciptitant drug Formulation in DDI clinical trail.";
				String jsURI = DIKBD2R_PREFIX + preciptFormuScript;

				System.out.println("preciptFormulation: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource preciptFormu = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				ann.setPreciptFormu(preciptFormu);
			}

			/*
			 * precipt duration
			 */
			String preciptDurationScript = pkddi.getPreciptDuration();

			if (preciptDurationScript != null && !preciptDurationScript.trim().equals("")) {
				String jsLabel = preciptDurationScript;
				String jsDescript = "preciptDuration in DDI clinical trail.";
				String jsURI = DIKBD2R_PREFIX + "preciptDuration";

				System.out.println("preciptDuration: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource preciptDuration = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				ann.setPreciptDuration(preciptDuration);
			}

			/*
			 * precipt regimens
			 */
			String preciptRegimensScript = pkddi.getPreciptRegimen();
			// System.out.println("preciptRegimensScript:" +
			// preciptRegimensScript);

			if (preciptRegimensScript != null && !preciptRegimensScript.trim().equals("")) {
				String jsLabel = preciptRegimensScript;
				String jsDescript = "preciptRegimens in DDI clinical trail.";
				String jsURI = DIKBD2R_PREFIX + preciptRegimensScript;

				System.out.println("preciptRegimens: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource preciptRegimens = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				// System.out.println("preciptRegimens linked resource created:
				// " +
				// jsLabel);

				ann.setPreciptRegimen(preciptRegimens);
			}

			/*
			 * AUC
			 */
			String aucScript = pkddi.getIncreaseAUC();
			// System.out.println("aucScript:" + aucScript);

			if (aucScript != null && !aucScript.trim().equals("")) {
				String jsLabel = aucScript;
				String jsDescript = "The maximum increasing Auc in interation.";
				//String jsURI = DIKBD2R_PREFIX + aucScript;
				String jsURI = DIKBD2R_PREFIX + "auc";

				//System.out.println("auc: " + jsLabel + "|" + jsURI + "|");
				MLinkedResource auc = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				ann.setIncreaseAuc(auc);
			}

			/*
			 * increase aucDirection
			 */
			String aucDirectionScript = pkddi.getAucDirection();
			// System.out.println("aucDirectionScript:" + aucDirectionScript);

			if (aucDirectionScript != null && !aucDirectionScript.trim().equals("")) {
				String jsLabel = aucDirectionScript;
				String jsDescript = "The auc Direction in interation.";
				String jsURI = DIKBD2R_PREFIX + aucDirectionScript;

				System.out.println("aucDirection: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource aucDirection = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				ann.setAucDirection(aucDirection);
			}

			/*
			 * increase aucType
			 */
			String aucTypeScript = pkddi.getAucType();
			// System.out.println("aucTypeScript:" + aucTypeScript);

			if (aucTypeScript != null && !aucTypeScript.trim().equals("")) {
				String jsLabel = aucTypeScript;
				String jsDescript = "The auc Type in interation.";
				String jsURI = DIKBD2R_PREFIX + aucTypeScript;

				System.out.println("aucType: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource aucType = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				ann.setAucType(aucType);
			}

			/*
			 * CL
			 */
			String clScript = pkddi.getCl();
			// System.out.println("clScript:" + clScript);

			if (clScript != null && !clScript.trim().equals("")) {
				String jsLabel = clScript;
				String jsDescript = "The maximum increasing cl in interation.";
				String jsURI = DIKBD2R_PREFIX + "cl";

				System.out.println("cl: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource cl = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				ann.setCl(cl);
			}

			/*
			 * clDirection
			 */
			String clDirectionScript = pkddi.getClDirection();
			// System.out.println("clDirectionScript:" + clDirectionScript);

			if (clDirectionScript != null && !clDirectionScript.trim().equals("")) {
				String jsLabel = clDirectionScript;
				String jsDescript = "The cl Direction in interation.";
				String jsURI = DIKBD2R_PREFIX + clDirectionScript;

				System.out.println("clDirection: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource clDirection = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				ann.setClDirection(clDirection);
			}

			/*
			 * clType
			 */
			String clTypeScript = pkddi.getClType();
			// System.out.println("clTypeScript:" + clTypeScript);

			if (clTypeScript != null && !clTypeScript.trim().equals("")) {
				String jsLabel = clTypeScript;
				String jsDescript = "The cl Type in interation.";
				String jsURI = DIKBD2R_PREFIX + clTypeScript;

				System.out.println("clType: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource clType = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				ann.setClType(clType);
			}

			/*
			 * Cmax
			 */
			String cmaxScript = pkddi.getCmax();
			// System.out.println("cmaxScript:" + cmaxScript);

			if (cmaxScript != null && !cmaxScript.trim().equals("")) {
				String jsLabel = cmaxScript;
				String jsDescript = "cmax in DDI clinical trail.";
				String jsURI = DIKBD2R_PREFIX + "cmax";

				System.out.println("cmax: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource cmax = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				// System.out.println("cmax linked resource created: " +
				// jsLabel);

				ann.setCmax(cmax);
			}

			/*
			 * CmaxDirection
			 */
			String CmaxDirectionScript = pkddi.getCmaxDirection();

			if (CmaxDirectionScript != null && !CmaxDirectionScript.trim().equals("")) {
				String jsLabel = CmaxDirectionScript;
				String jsDescript = "The Cmax Direction in interation.";
				String jsURI = DIKBD2R_PREFIX + CmaxDirectionScript;

				System.out.println("CmaxDirection: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource CmaxDirection = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				ann.setCmaxDirection(CmaxDirection);
			}

			/*
			 * CmaxType
			 */
			String CmaxTypeScript = pkddi.getCmaxType();
			// System.out.println("CmaxTypeScript:" + CmaxTypeScript);

			if (CmaxTypeScript != null && !CmaxTypeScript.trim().equals("")) {
				String jsLabel = CmaxTypeScript;
				String jsDescript = "The Cmax Type in interation.";
				String jsURI = DIKBD2R_PREFIX + CmaxTypeScript;

				System.out.println("CmaxType: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource CmaxType = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				ann.setCmaxType(CmaxType);
			}

			/*
			 * T1/2
			 */
			String t12Script = pkddi.getT12();
			// System.out.println("t12Script:" + t12Script);

			if (t12Script != null && !t12Script.trim().equals("")) {
				String jsLabel = t12Script;
				String jsDescript = "t12 in DDI clinical trail.";
				String jsURI = DIKBD2R_PREFIX + "t12";

				System.out.println("t12: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource t12 = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				// System.out.println("t12 linked resource created: " +
				// jsLabel);

				ann.setT12(t12);
			}

			/*
			 * T12Direction
			 */
			String T12DirectionScript = pkddi.getT12Direction();
			// System.out.println("T12DirectionScript:" + T12DirectionScript);

			if (T12DirectionScript != null && !T12DirectionScript.trim().equals("")) {
				String jsLabel = T12DirectionScript;
				String jsDescript = "The T12 Direction in interation.";
				String jsURI = DIKBD2R_PREFIX + T12DirectionScript;

				//System.out.println("T12Direction: " + jsLabel + "|" + jsURI + "|");

				MLinkedResource T12Direction = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				ann.setT12Direction(T12Direction);
			}

			/*
			 * T12Type
			 */
			String T12TypeScript = pkddi.getT12Type();
			// System.out.println("T12TypeScript:" + T12TypeScript);

			if (T12TypeScript != null && !T12TypeScript.trim().equals("")) {
				String jsLabel = T12TypeScript;
				String jsDescript = "The T12 Type in interation.";
				String jsURI = DIKBD2R_PREFIX + T12TypeScript;

				MLinkedResource T12Type = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

				ann.setT12Type(T12Type);
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

			MLinkedResource evidenceType = ResourcesFactory.createLinkedResource(jsURI, jsLabel, jsDescript);

			// System.out.println("evidenceType linked resource created: " +
			// jsLabel);

			ann.setEvidenceType(evidenceType);
		}

		return ann;
	}

	protected void validation(JavaScriptObject json, String validation, MAnnotationSet set,
			ArrayList<MSelector> selectors) {
		// TODO Auto-generated method stub

	}
}
