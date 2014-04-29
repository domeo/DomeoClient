package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.serialization;

import java.util.Set;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Iexpertstudy_pDDI;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIAnnotation;
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
public class Jsonexpertstudy_pDDIAnnotationSerializer extends
		JsonAnnotationSerializer implements Iexpertstudy_pDDI {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {

		Mexpertstudy_pDDIAnnotation ann = (Mexpertstudy_pDDIAnnotation) obj;
		JSONObject annotation = initializeAnnotation(manager, ann);
		manager._domeo.getLogger().debug(this,
				"Initialized JSON annotation object");

		// Body creation
		JSONArray bodies = new JSONArray();
		Integer idx = 0;

		// TODO; create resources that include all of the resources in the
		// model. Make concordant with the JsonUnmarshaller class where the
		// data gets unmarshalled

		/*
		 * create drug entity1
		 */
		MLinkedResource drug1 = ann.getDrug1();

		if (drug1 != null) {

			// create DDI statement
			JSONObject statement = new JSONObject();
			String statementUUID = UUID.uuid();
			statement.put("@id",
					new JSONString(PDDI_URN_PREFIX + statementUUID));
			statement.put("@type", new JSONString("poc:"
					+ "DrugDrugInteractionStatement"));

			// create pk_ddi
			JSONObject pkddi = new JSONObject();
			String pddiUUID = UUID.uuid();
			pkddi.put("@id", new JSONString(PDDI_URN_PREFIX + pddiUUID));
			pkddi.put("@type", new JSONString("dikbD2R:PK_DDI"));

			// create drug entity 1
			JSONObject drug_entity1 = new JSONObject();
			String entity1UUID = UUID.uuid();

			drug_entity1.put("@id", new JSONString(PDDI_URN_PREFIX
					+ entity1UUID));

			// drug1 has type1
			MLinkedResource type1 = ann.getType1();
			if (type1 != null)
				drug_entity1.put("@type",
						new JSONString("dikbD2R:" + type1.getLabel()));

			/*
			 * TODO: add drug uri for each drug entities
			 */

			drug_entity1.put(IRdfsOntology.label,
					new JSONString(drug1.getLabel()));
			drug_entity1.put(IDublinCoreTerms.description, new JSONString(
					drug1.getDescription()));

			// drug1 has role1
			MLinkedResource role1 = ann.getRole1();
			if (role1 != null) {
				drug_entity1.put("sio:" + "SIO_000228", new JSONString(
						"dikbD2R:" + role1.getLabel()));
				drug_entity1.put("dikbD2R:" + role1.getLabel(), new JSONString(
						role1.getUrl()));
			}

			// PDDI has entity1 participant in
			pkddi.put("sio:" + "SIO_000132", drug_entity1);

			// DDI statement refers to PDDI
			statement.put("sio:" + "SIO_000628", pkddi);

			bodies.set(idx, statement);
			idx += 1;

		}

		/*
		 * create drug entity 2
		 */
		MLinkedResource drug2 = ann.getDrug2();
		if (drug2 != null) {

			// create DDI statement
			JSONObject statement = new JSONObject();
			String statementUUID = UUID.uuid();
			statement.put("@id",
					new JSONString(PDDI_URN_PREFIX + statementUUID));
			statement.put("@type", new JSONString("poc:"
					+ "DrugDrugInteractionStatement"));

			// create pk_ddi
			JSONObject pkddi = new JSONObject();
			String pddiUUID = UUID.uuid();
			pkddi.put("@id", new JSONString(PDDI_URN_PREFIX + pddiUUID));
			pkddi.put("@type", new JSONString("dikbD2R:PK_DDI"));
			// create drug entity 2
			JSONObject drug_entity2 = new JSONObject();
			String entity2UUID = UUID.uuid();

			drug_entity2.put("@id", new JSONString(PDDI_URN_PREFIX
					+ entity2UUID));

			// drug2 has type
			MLinkedResource type2 = ann.getType2();
			if (type2 != null)
				drug_entity2.put("@type",
						new JSONString("dikbD2R:" + type2.getLabel()));

			drug_entity2.put(IRdfsOntology.label,
					new JSONString(drug2.getLabel()));
			drug_entity2.put(IDublinCoreTerms.description,
					new JSONString(drug2.getDescription()));

			// drug2 has role2
			MLinkedResource role2 = ann.getRole2();
			if (role2 != null) {
				drug_entity2.put("sio:" + "SIO_000228", new JSONString(
						"dikbD2R:" + role2.getLabel()));
				drug_entity2.put("dikbD2R:" + role2.getLabel(), new JSONString(
						role2.getUrl()));	
			}

			// PDDI has entity2 participant in
			pkddi.put("sio:" + "SIO_000132", drug_entity2);

			// DDI statement refers to PDDI
			statement.put("sio:" + "SIO_000628", pkddi);

			bodies.set(idx, statement);
			idx += 1;

		}

		/*
		 * create statement
		 */

		MLinkedResource statemt = ann.getStatement();
		if (statemt != null) {

			// create DDI statement
			JSONObject statement = new JSONObject();
			String statementUUID = UUID.uuid();
			statement.put("@id",
					new JSONString(PDDI_URN_PREFIX + statementUUID));
			statement.put("@type", new JSONString("poc:"
					+ "DrugDrugInteractionStatement"));

			// DDI statement is represented by statement
			statement.put("sio:" + "SIO_000205", new JSONString("dikbD2R:"
					+ "statement"));
			statement.put("dikbD2R:" + "statement",
					new JSONString(statemt.getUrl()));
			statement.put(IDublinCoreTerms.description,
					new JSONString(statemt.getDescription()));

			bodies.set(idx, statement);
			idx += 1;
		}

		/*
		 * create modality
		 */

		MLinkedResource modality = ann.getModality();
		if (modality != null) {

			// create DDI statement
			JSONObject statement = new JSONObject();
			String statementUUID = UUID.uuid();
			statement.put("@id",
					new JSONString(PDDI_URN_PREFIX + statementUUID));
			statement.put("@type", new JSONString("poc:"
					+ "DrugDrugInteractionStatement"));

			// modality describes DDI statement
			statement.put("sio:" + "SIO_000228", new JSONString("dikbD2R:"
					+ "modality"));
			statement.put("dikbD2R:" + "modality",
					new JSONString(modality.getUrl()));
			statement.put(IDublinCoreTerms.description,
					new JSONString(modality.getDescription()));

			bodies.set(idx, statement);
			idx += 1;
		}

		annotation.put(IDomeoOntology.content, bodies);

		return annotation;
	}
}
