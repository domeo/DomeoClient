package org.mindinformatics.gwt.domeo.plugins.annotation.ddi.serialization;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.MddiAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonAnnotationSerializer;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonSerializerManager;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.src.UUID;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * This class provides the serialization aspects peculiar to the DDI annotation.
 * 
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class JsonddiAnnotationSerializer extends JsonAnnotationSerializer {

	public final String DIKBD2R_PREFIX = "dikbD2R:";
	public final String PDDI_URN_PREFIX = "urn:pddi:uuid:";
	public final String POC_PREFIX = "poc:";
	public final String SIO_PREFIX = "sio:";
	public final String NCIT_PREFIX = "ncit:";
	public final String DAILYMED_PREFIX = "dailymed:";
	public final String MP_PREFIX = "mp:";

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {

		MddiAnnotation ann = (MddiAnnotation) obj;
		JSONObject annotation = initializeAnnotation(manager, ann);
		manager._domeo.getLogger().debug(this, "Initialized JSON annotation object");
		// Body creation
		JSONArray bodies = new JSONArray();

		// add assertion type
		String assertTypeStr = "";
		MLinkedResource assertType = ann.getAssertType();
		if (assertType != null) {
			assertTypeStr = assertType.getLabel();

			annotation.put(DIKBD2R_PREFIX + "assertionType", new JSONString(assertTypeStr));
		} else {
			manager._domeo.getLogger().debug(this, "ddi annotation serilize error, no assertion type been found");
		}

		/*
		 * DDI statement urn
		 */
		JSONObject sets = new JSONObject();

		JSONObject statement = new JSONObject();
		String statementUUID = UUID.uuid();
		statement.put("@id", new JSONString(PDDI_URN_PREFIX + statementUUID));
		statement.put("@type", new JSONString(POC_PREFIX + "DrugDrugInteractionStatement"));

		/*
		 * PK DDI urn
		 */
		// add pkddi to array

		JSONArray pkddiArray = new JSONArray();

		JSONObject pkddi = new JSONObject();
		String pddiUUID = UUID.uuid();
		pkddi.put("@id", new JSONString(PDDI_URN_PREFIX + pddiUUID));
		pkddi.put("@type", new JSONString(DIKBD2R_PREFIX + "PK_DDI"));

		/*
		 * drug entity1
		 */

		JSONObject drug_entity1 = new JSONObject();

		/*
		 * drug id is PURL for an drug active ingredients (RXCUI), product
		 * (RXCUI), or Jochem
		 */

		MLinkedResource drug1 = ann.getDrug1();
		if (drug1 != null) {

			// get URI by term
			// String drugUri1 = findURIbyTerm(drug1.getLabel(),
			// type1.getLabel());
			String drugUri1 = drug1.getUrl();
			String drug_entity1_UUID = UUID.uuid();
			drug_entity1.put("@id", new JSONString(drug_entity1_UUID));
			drug_entity1.put(DAILYMED_PREFIX + "activeMoietyRxCUI", new JSONString(drugUri1));
			drug_entity1.put(IRdfsOntology.label, new JSONString(drug1.getLabel()));
			drug_entity1.put(IDublinCoreTerms.description, new JSONString(drug1.getDescription()));

		}

		// drug1 has type1
		MLinkedResource type1 = ann.getType1();
		if (type1 != null) {
			drug_entity1.put("@type", new JSONString(DIKBD2R_PREFIX + type1.getLabel()));
		}

		MLinkedResource role1 = ann.getRole1();
		if (role1 != null) {
			// drug1 has role1
			if (role1 != null) {
				drug_entity1.put(SIO_PREFIX + "SIO_000228", new JSONString(DIKBD2R_PREFIX + role1.getLabel()));

				// dose of drug 1, depends on drug role

				if (assertTypeStr.equals("DDI-clinical-trial")) {
					
					String doseLabel = "";

					if (role1.getLabel().toLowerCase().contains("object")) {

						MLinkedResource objectDose = ann.getObjectDose();
						if (objectDose != null){
							doseLabel = objectDose.getLabel();
						}
					}
					else if (role1.getLabel().toLowerCase().contains("precip")){
						MLinkedResource preciptDose = ann.getPreciptDose();
						if (preciptDose !=null){
							doseLabel = preciptDose.getLabel();
						}
						
					}
					
					drug_entity1.put(DIKBD2R_PREFIX + "dose", new JSONString(doseLabel));

				}

			}
		}

		/*
		 * create drug entity2
		 */

		JSONObject drug_entity2 = new JSONObject();

		/*
		 * ID is PURL for an drug active ingredients (RXCUI), product (RXCUI),
		 * or Jochem
		 */

		MLinkedResource drug2 = ann.getDrug2();
		if (drug2 != null) {
			// get URI by term

			// String drugUri2 = findURIbyTerm(drug2.getLabel(),
			// type2.getLabel());
			String drugUri2 = drug2.getUrl();
			String drug_entity2_UUID = UUID.uuid();
			drug_entity2.put("@id", new JSONString(drug_entity2_UUID));
			drug_entity2.put(DAILYMED_PREFIX + "activeMoietyRxCUI", new JSONString(drugUri2));
			drug_entity2.put(IRdfsOntology.label, new JSONString(drug2.getLabel()));
			drug_entity2.put(IDublinCoreTerms.description, new JSONString(drug2.getDescription()));

			// dose of drug 1
			if (assertTypeStr.equals("DDI-clinical-trial")) {
				MLinkedResource preciptDose = ann.getPreciptDose();
				if (preciptDose != null) {
				}
			}
		}

		// drug2 has type
		MLinkedResource type2 = ann.getType2();
		if (type2 != null) {
			drug_entity2.put("@type", new JSONString(DIKBD2R_PREFIX + type2.getLabel()));
		}

		// drug2 has role2
		MLinkedResource role2 = ann.getRole2();
		if (role2 != null) {
			drug_entity2.put(SIO_PREFIX + "SIO_000228", new JSONString(DIKBD2R_PREFIX + role2.getLabel()));
			
			String doseLabel = "";

			
			if (role2.getLabel().toLowerCase().contains("object")) {

				MLinkedResource objectDose = ann.getObjectDose();
				if (objectDose != null){
					doseLabel = objectDose.getLabel();
				}
			}
			else if (role2.getLabel().toLowerCase().contains("precip")){
				MLinkedResource preciptDose = ann.getPreciptDose();
				if (preciptDose !=null){
					doseLabel = preciptDose.getLabel();
				}
				
			}
			drug_entity2.put(DIKBD2R_PREFIX + "dose", new JSONString(doseLabel));

		}

		/*
		 * multiple drugs in pk_ddi
		 */
		JSONArray drugs = new JSONArray();
		int index_drugs = 0;
		// PDDI has entity1 participant in
		drugs.set(index_drugs++, drug_entity1);
		// PDDI has entity2 participant in
		drugs.set(index_drugs++, drug_entity2);

		/*
		 * DDI-clinical-trial fields
		 */

		if (assertTypeStr.equals("DDI-clinical-trial")) {

			MLinkedResource numOfParticipants = ann.getNumOfparcipitants();
			if (numOfParticipants != null) {
				pkddi.put(DIKBD2R_PREFIX + "numOfParticipants", new JSONString(numOfParticipants.getLabel().trim()));
			}

			MLinkedResource objectDuration = ann.getObjectDuration();
			if (objectDuration != null) {
				pkddi.put(DIKBD2R_PREFIX + "objectDuration", new JSONString(objectDuration.getLabel().trim()));
			}

			MLinkedResource objectFormu = ann.getObjectFormu();
			if (objectFormu != null) {
				pkddi.put(DIKBD2R_PREFIX + "objectFormulation", new JSONString(objectFormu.getLabel().trim()));
			}

			MLinkedResource objectRegimens = ann.getObjectRegimen();
			if (objectRegimens != null) {
				pkddi.put(DIKBD2R_PREFIX + "objectRegimens", new JSONString(objectRegimens.getLabel().trim()));
			}

			MLinkedResource preciptFormu = ann.getPreciptFormu();
			if (preciptFormu != null) {
				pkddi.put(DIKBD2R_PREFIX + "preciptFormulation", new JSONString(preciptFormu.getLabel().trim()));
			}

			MLinkedResource preciptDuration = ann.getPreciptDuration();
			if (preciptDuration != null) {
				pkddi.put(DIKBD2R_PREFIX + "preciptDuration", new JSONString(preciptDuration.getLabel().trim()));
			}

			MLinkedResource preciptRegimens = ann.getPreciptRegimen();
			if (preciptRegimens != null) {
				pkddi.put(DIKBD2R_PREFIX + "preciptRegimens", new JSONString(preciptRegimens.getLabel().trim()));
			}

			MLinkedResource auc = ann.getIncreaseAuc();
			if (auc != null) {
				pkddi.put(DIKBD2R_PREFIX + "auc", new JSONString(auc.getLabel().trim()));
			}

			MLinkedResource aucDirection = ann.getAucDirection();
			if (aucDirection != null) {
				pkddi.put(DIKBD2R_PREFIX + "aucDirection", new JSONString(aucDirection.getLabel().trim()));
			}

			MLinkedResource aucType = ann.getAucType();
			if (aucType != null) {
				pkddi.put(DIKBD2R_PREFIX + "aucType", new JSONString(aucType.getLabel().trim()));
			}

			MLinkedResource cl = ann.getCl();
			if (cl != null) {
				pkddi.put(DIKBD2R_PREFIX + "cl", new JSONString(cl.getLabel().trim()));
			}

			MLinkedResource clDirection = ann.getClDirection();
			if (clDirection != null) {
				pkddi.put(DIKBD2R_PREFIX + "clDirection", new JSONString(clDirection.getLabel().trim()));
			}

			MLinkedResource clType = ann.getClType();
			if (clType != null) {
				pkddi.put(DIKBD2R_PREFIX + "clType", new JSONString(clType.getLabel().trim()));
			}

			MLinkedResource cmax = ann.getCmax();
			if (cmax != null) {
				pkddi.put(DIKBD2R_PREFIX + "cmax", new JSONString(cmax.getLabel().trim()));
			}

			MLinkedResource cmaxDirection = ann.getCmaxDirection();
			if (cmaxDirection != null) {
				pkddi.put(DIKBD2R_PREFIX + "cmaxDirection", new JSONString(cmaxDirection.getLabel().trim()));
			}

			MLinkedResource cmaxType = ann.getCmaxType();
			if (cmaxType != null) {
				pkddi.put(DIKBD2R_PREFIX + "cmaxType", new JSONString(cmaxType.getLabel().trim()));
			}

			MLinkedResource t12 = ann.getT12();
			if (t12 != null) {
				pkddi.put(DIKBD2R_PREFIX + "t12", new JSONString(t12.getLabel().trim()));
			}

			MLinkedResource t12Direction = ann.getT12Direction();
			if (t12Direction != null) {
				pkddi.put(DIKBD2R_PREFIX + "t12Direction", new JSONString(t12Direction.getLabel().trim()));
			}

			MLinkedResource t12Type = ann.getT12Type();
			if (t12Type != null) {
				pkddi.put(DIKBD2R_PREFIX + "t12Type", new JSONString(t12Type.getLabel().trim()));
			}

		}

		// drug participant in pk_ddi
		if (drugs != null)
			pkddi.put(SIO_PREFIX + "SIO_000132", drugs);

		pkddiArray.set(0, pkddi);

		// DDI statement refers to PDDI
		if (pkddi != null)
			statement.put(SIO_PREFIX + "SIO_000628", pkddiArray);

		/*
		 * create statement
		 */

		MLinkedResource statemt = ann.getStatement();
		if (statemt != null) {
			// DDI statement is represented by statement
			statement.put(SIO_PREFIX + "SIO_000205", new JSONString(DIKBD2R_PREFIX + "statement"));
			statement.put(DIKBD2R_PREFIX + "statement", new JSONString(NCIT_PREFIX + statemt.getLabel()));
		}

		/*
		 * create modality
		 */
		MLinkedResource modality = ann.getModality();
		if (modality != null) {

			// modality describes DDI statement
			statement.put(SIO_PREFIX + "SIO_000563", new JSONString(DIKBD2R_PREFIX + "modality"));
			statement.put(DIKBD2R_PREFIX + "modality", new JSONString(NCIT_PREFIX + modality.getLabel()));
		}

		// comment
		String comment = ann.getComment();
		if (comment != null && !comment.trim().equals("")) {
			statement.put(DIKBD2R_PREFIX + "comment", new JSONString(comment));
		}

		// add statement to body

		sets.put("sets", statement);
		bodies.set(0, sets);
		annotation.put(IDomeoOntology.content, bodies);

		// add evidence type

		MLinkedResource evidenceType = ann.getEvidenceType();
		annotation.put(DIKBD2R_PREFIX + "evidenceType", new JSONString(evidenceType.getLabel()));

		return annotation;
	}

	/*
	 * public String findURIbyTerm(String term, String type) { String uri = "";
	 * if (type.equals("active-ingredient")) { uri =
	 * Mddi.getActiveIngredient_URI_map().get( term.toUpperCase()); } else if
	 * (type.equals("metabolite")) { uri = Mddi.getMetabolite_URI_map().get(
	 * term.toUpperCase()); } else if (type.equals("drug-product")) { uri =
	 * Mddi.getDrugProduct_URI_map().get( term.toUpperCase()); }
	 * 
	 * if (uri.length() > 4) return uri; else { return "fake_uri"; }
	 * 
	 * }
	 */

}
