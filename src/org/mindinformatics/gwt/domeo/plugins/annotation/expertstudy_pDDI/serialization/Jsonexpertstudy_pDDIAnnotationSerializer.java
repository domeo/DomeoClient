package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.serialization;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDI;
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
		JsonAnnotationSerializer {

	public final String DIKBD2R_PREFIX = "dikbD2R:";
	public final String PDDI_URN_PREFIX = "urn:pddi:uuid:";
	public final String POC_PREFIX = "poc:";
	public final String SIO_PREFIX = "sio:";
	public final String NCIT_PREFIX = "ncit:";

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {

		Mexpertstudy_pDDIAnnotation ann = (Mexpertstudy_pDDIAnnotation) obj;
		JSONObject annotation = initializeAnnotation(manager, ann);
		manager._domeo.getLogger().debug(this,
				"Initialized JSON annotation object");
		// Body creation
		JSONArray bodies = new JSONArray();

		/*
		 * DDI statement urn
		 */
		JSONObject statement = new JSONObject();
		String statementUUID = UUID.uuid();
		statement.put("@id", new JSONString(PDDI_URN_PREFIX + statementUUID));
		statement.put("@type", new JSONString(POC_PREFIX
				+ "DrugDrugInteractionStatement"));
		
		/*
		 * PK DDI urn
		 */
		JSONObject pkddi = new JSONObject();
		String pddiUUID = UUID.uuid();
		pkddi.put("@id", new JSONString(PDDI_URN_PREFIX + pddiUUID));
		pkddi.put("@type", new JSONString(DIKBD2R_PREFIX + "PK_DDI"));
		
		/*
		 * drug entity1
		 */
		MLinkedResource drug1 = ann.getDrug1();

		JSONObject drug_entity1 = new JSONObject();

		/*
		 * drug id is PURL for an drug active ingredients (RXCUI), product
		 * (RXCUI), or Jochem
		 */
		
		MLinkedResource type1 = ann.getType1();
		
		
		if (type1 != null && drug1 != null) {
			
			// get URI by term
			String drugUri1 = findURIbyTerm(drug1.getLabel(), type1.getLabel());

			drug_entity1.put("@id", new JSONString(drugUri1));

			// drug1 has type1

			drug_entity1.put("@type",
					new JSONString(DIKBD2R_PREFIX + type1.getLabel()));

			drug_entity1.put(IRdfsOntology.label,
					new JSONString(drug1.getLabel()));
			drug_entity1.put(IDublinCoreTerms.description,
					new JSONString(drug1.getDescription()));
			
			
			// drug1 has role1
			MLinkedResource role1 = ann.getRole1();
			if (role1 != null) {
				drug_entity1.put(SIO_PREFIX + "SIO_000228", new JSONString(
						DIKBD2R_PREFIX + role1.getLabel()));
				// drug_entity1.put(DIKBD2R_PREFIX + role1.getLabel(), new
				// JSONString(role1.getUrl()));
			}
	
		} 
		
		/*
		 * create drug entity2
		 */

		MLinkedResource drug2 = ann.getDrug2();

		JSONObject drug_entity2 = new JSONObject();

		/*
		 * ID is PURL for an drug active ingredients (RXCUI), product (RXCUI),
		 * or Jochem
		 */
		MLinkedResource type2 = ann.getType2();
		if (type2 != null && drug2 != null) {
			// get URI by term
			
			String drugUri2 = "";
			drugUri2 = findURIbyTerm(drug2.getLabel(), type2.getLabel());
			
			drug_entity2.put("@id", new JSONString(drugUri2));

			// drug2 has type

			drug_entity2.put("@type",
					new JSONString(DIKBD2R_PREFIX + type2.getLabel()));
		
			drug_entity2.put(IRdfsOntology.label,
					new JSONString(drug2.getLabel()));
			drug_entity2.put(IDublinCoreTerms.description,
					new JSONString(drug2.getDescription()));
			
			// drug2 has role2
			MLinkedResource role2 = ann.getRole2();
			if (role2 != null) {
				drug_entity2.put(SIO_PREFIX + "SIO_000228", new JSONString(
						DIKBD2R_PREFIX + role2.getLabel()));
			}
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

		// drug participant in pk_ddi
		if(drugs!=null)
		pkddi.put(SIO_PREFIX + "SIO_000132", drugs);

		// DDI statement refers to PDDI
		if(pkddi!=null)
		statement.put(SIO_PREFIX + "SIO_000628", pkddi);

		/*
		 * create statement
		 */
		
		MLinkedResource statemt = ann.getStatement();
		if (statemt != null) {
			// DDI statement is represented by statement
			statement.put(SIO_PREFIX + "SIO_000205", new JSONString(
					DIKBD2R_PREFIX + "statement"));
			statement.put(DIKBD2R_PREFIX + "statement", new JSONString(
					NCIT_PREFIX + statemt.getLabel()));
		}

		/*
		 * create modality
		 */
		MLinkedResource modality = ann.getModality();
		if (modality != null) {

			// modality describes DDI statement
			statement.put(SIO_PREFIX + "SIO_000563", new JSONString(
					DIKBD2R_PREFIX + "modality"));
			statement.put(DIKBD2R_PREFIX + "modality", new JSONString(
					NCIT_PREFIX + modality.getLabel()));
		}

		// add statement to body
		bodies.set(0, statement);
		annotation.put(IDomeoOntology.content, bodies);
		
		return annotation;
	}

	public String findURIbyTerm(String term, String type) {
		String uri = "";
		if (type.equals("active-ingredient")) {
			uri = Mexpertstudy_pDDI.getActiveIngredient_URI_map().get(
					term.toUpperCase());
		} else if (type.equals("metabolite")) {
			uri = Mexpertstudy_pDDI.getMetabolite_URI_map().get(
					term.toUpperCase());
		} else if (type.equals("drug-product")) {
			uri = Mexpertstudy_pDDI.getDrugProduct_URI_map().get(
					term.toUpperCase());
		}

		if (uri.length() > 4)
			return uri;
		else {
			return "fake_uri";
		}
		
	}
	
}
