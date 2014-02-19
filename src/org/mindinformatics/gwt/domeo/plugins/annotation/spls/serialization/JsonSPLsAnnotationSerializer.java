package org.mindinformatics.gwt.domeo.plugins.annotation.spls.serialization;

import java.util.Set;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.IPharmgxOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonAnnotationSerializer;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonSerializerManager;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.src.UUID;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * This class provides the serialization aspects peculiar to the SPLs
 * pharmacogenomics annotation.
 * 
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class JsonSPLsAnnotationSerializer extends JsonAnnotationSerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		// TODO: use the IPharmgxOntology/SPL ontoogy for these
		String SPL_URN_PREFIX = "urn:linkedspls:uuid:";
		String SPL_POC_PREFIX = "poc:";
		String SIO_PREFIX = "sio:";
		String DAILYMED_PREFIX = "dailymed:";

		MSPLsAnnotation ann = (MSPLsAnnotation) obj;
		JSONObject annotation = initializeAnnotation(manager, ann);
		manager._domeo.getLogger().debug(this,
				"Initialized JSON annotation object");

		// Body creation
		JSONArray bodies = new JSONArray();
		Integer idx = 0;

		// TODO; create resources that include all of the resources in the
		// model. Make concordant with the USPLJsonUnmarshaller class where the
		// data gets unmarshalled

		// PK Impact
		MLinkedResource pkImpact = ann.getPKImpact();
		if (pkImpact != null) {
			JSONObject body = new JSONObject(); // common
			String statementUUID = UUID.uuid(); // common
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID)); // common
			body.put("@type", new JSONString(SPL_POC_PREFIX
					+ "PharmacogenomicsStatement")); // common
			body.put(SIO_PREFIX + "SIO_000563", new JSONString(SPL_POC_PREFIX
					+ "PharmacokineticImpact"));
			body.put(SPL_POC_PREFIX + "PharmacokineticImpact", new JSONString(
					pkImpact.getUrl()));
			body.put(IRdfsOntology.label, new JSONString(pkImpact.getLabel()));
			body.put(IDublinCoreTerms.description,
					new JSONString(pkImpact.getDescription()));
			bodies.set(idx, body);
			idx += 1;
		}

		// PD Impact
		MLinkedResource pdImpact = ann.getPdImpact();
		if (pdImpact != null) {
			JSONObject body = new JSONObject();
			String statementUUID = UUID.uuid();
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
			body.put("@type", new JSONString(SPL_POC_PREFIX
					+ "PharmacogenomicsStatement"));
			body.put(SIO_PREFIX + "SIO_000563", new JSONString(SPL_POC_PREFIX
					+ "PharmacodynamicImpact"));
			body.put(SPL_POC_PREFIX + "PharmacodynamicImpact", new JSONString(
					pdImpact.getUrl()));
			body.put(IRdfsOntology.label, new JSONString(pdImpact.getLabel()));
			body.put(IDublinCoreTerms.description,
					new JSONString(pdImpact.getDescription()));
			bodies.set(idx, body);
			idx += 1;
		}

		// Drug Selection Recommendation
		MLinkedResource drugRec = ann.getDrugRec();
		if (drugRec != null) {
			JSONObject body = new JSONObject();
			String statementUUID = UUID.uuid();
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
			body.put("@type", new JSONString(SPL_POC_PREFIX
					+ "PharmacogenomicsStatement"));
			body.put(SIO_PREFIX + "SIO_000563", new JSONString(SPL_POC_PREFIX
					+ "DrugSelectionRecommendation"));
			body.put(SPL_POC_PREFIX + "DrugSelectionRecommendation",
					new JSONString(drugRec.getUrl()));
			body.put(IRdfsOntology.label, new JSONString(drugRec.getLabel()));
			body.put(IDublinCoreTerms.description,
					new JSONString(drugRec.getDescription()));
			bodies.set(idx, body);
			idx += 1;
		}

		// Dose Selection Recommendation
		MLinkedResource doseRec = ann.getDoseRec();
		if (doseRec != null) {
			JSONObject body = new JSONObject();
			String statementUUID = UUID.uuid();
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
			body.put("@type", new JSONString(SPL_POC_PREFIX
					+ "PharmacogenomicsStatement"));
			body.put(SIO_PREFIX + "SIO_000563", new JSONString(SPL_POC_PREFIX
					+ "DoseSelectionRecommendation"));
			body.put(SPL_POC_PREFIX + "DoseSelectionRecommendation",
					new JSONString(doseRec.getUrl()));
			body.put(IRdfsOntology.label, new JSONString(doseRec.getLabel()));
			body.put(IDublinCoreTerms.description,
					new JSONString(doseRec.getDescription()));
			bodies.set(idx, body);
			idx += 1;
		}

		// Monitoring Recommendation
		MLinkedResource monitRec = ann.getMonitRec();
		if (monitRec != null) {
			JSONObject body = new JSONObject();
			String statementUUID = UUID.uuid();
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
			body.put("@type", new JSONString(SPL_POC_PREFIX
					+ "PharmacogenomicsStatement"));
			body.put(SIO_PREFIX + "SIO_000563", new JSONString(SPL_POC_PREFIX
					+ "MonitoringRecommendation"));
			body.put(SPL_POC_PREFIX + "MonitoringRecommendation",
					new JSONString(monitRec.getUrl()));
			body.put(IRdfsOntology.label, new JSONString(monitRec.getLabel()));
			body.put(IDublinCoreTerms.description,
					new JSONString(monitRec.getDescription()));
			bodies.set(idx, body);
			idx += 1;
		}

		// Test Recommendation
		MLinkedResource testRec = ann.getTestRec();
		if (testRec != null) {
			JSONObject body = new JSONObject();
			String statementUUID = UUID.uuid();
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
			body.put("@type", new JSONString(SPL_POC_PREFIX
					+ "PharmacogenomicsStatement"));
			body.put(SIO_PREFIX + "SIO_000563", new JSONString(SPL_POC_PREFIX
					+ "TestRecommendation"));
			body.put(SPL_POC_PREFIX + "TestRecommendation", new JSONString(
					testRec.getUrl()));
			body.put(IRdfsOntology.label, new JSONString(testRec.getLabel()));
			body.put(IDublinCoreTerms.description,
					new JSONString(testRec.getDescription()));
			bodies.set(idx, body);
			idx += 1;
		}

		// Common Variant
		MLinkedResource variant = ann.getVariant();
		if (variant != null && !variant.getLabel().equals("unselected")) {
			JSONObject body = new JSONObject();
			String statementUUID = UUID.uuid();
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
			body.put("@type", new JSONString(SPL_POC_PREFIX
					+ "PharmacogenomicsStatement"));
			body.put(SIO_PREFIX + "SIO_000628", new JSONString(SPL_POC_PREFIX
					+ "Variant"));
			body.put(SPL_POC_PREFIX + "Variant",
					new JSONString(variant.getUrl()));
			body.put(IRdfsOntology.label, new JSONString(variant.getLabel()));
			body.put(IDublinCoreTerms.description,
					new JSONString(variant.getDescription()));
			bodies.set(idx, body);
			idx += 1;
		}

		// Common Test
		MLinkedResource test = ann.getTest();
		if (test != null && !test.getLabel().equals("unselected")) {
			JSONObject body = new JSONObject();
			String statementUUID = UUID.uuid();
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
			body.put("@type", new JSONString(SPL_POC_PREFIX
					+ "PharmacogenomicsStatement"));
			body.put(SIO_PREFIX + "SIO_000338", new JSONString(SPL_POC_PREFIX
					+ "TestResult"));
			body.put(SPL_POC_PREFIX + "TestResult",
					new JSONString(test.getUrl()));
			body.put(IRdfsOntology.label, new JSONString(test.getLabel()));
			body.put(IDublinCoreTerms.description,
					new JSONString(test.getDescription()));
			bodies.set(idx, body);
			idx += 1;
		}

		// Drug of Interest
		MLinkedResource drug = ann.getDrugOfInterest();
		if (drug != null && !drug.getLabel().equals("unselected")) {
			JSONObject body = new JSONObject();
			String statementUUID = UUID.uuid();
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
			body.put("@type", new JSONString(SPL_POC_PREFIX
					+ "PharmacogenomicsStatement"));
			body.put(SIO_PREFIX + "SIO_000628", new JSONString(DAILYMED_PREFIX
					+ "pharmgxDrug"));
			body.put(DAILYMED_PREFIX + "pharmgxDrug",
					new JSONString(drug.getUrl()));
			body.put(IRdfsOntology.label, new JSONString(drug.getLabel()));
			body.put(IDublinCoreTerms.description,
					new JSONString(drug.getDescription()));
			bodies.set(idx, body);
			idx += 1;
		}

		// Biomarkers of interest
		MLinkedResource biomarkers = ann.getBiomarkers();
		if (biomarkers != null && !biomarkers.getLabel().equals("unselected")) {
			JSONObject body = new JSONObject();
			String statementUUID = UUID.uuid();
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
			body.put("@type", new JSONString(SPL_POC_PREFIX
					+ "PharmacogenomicsStatement"));
			body.put(SIO_PREFIX + "SIO_000628", new JSONString(DAILYMED_PREFIX
					+ "pharmgxBiomarker"));
			body.put(DAILYMED_PREFIX + "pharmgxBiomarker", new JSONString(
					biomarkers.getUrl()));
			body.put(IRdfsOntology.label, new JSONString(biomarkers.getLabel()));
			body.put(IDublinCoreTerms.description,
					new JSONString(biomarkers.getDescription()));
			bodies.set(idx, body);
			idx += 1;
		}

		// statements: variant frequency 
		Set<MLinkedResource> statements = ann.getStatements();

		if (statements != null) {

			for (MLinkedResource statement : statements) {

				/*if (statement.getLabel().equals(
						"Concomitant medication concern")) {
					JSONObject body = new JSONObject();
					String statementUUID = UUID.uuid();
					body.put("@id", new JSONString(SPL_URN_PREFIX
							+ statementUUID));
					body.put("@type", new JSONString(SPL_POC_PREFIX
							+ "PharmacogenomicsStatement"));
					body.put(SIO_PREFIX + "SIO_000563", new JSONString(
							SPL_POC_PREFIX + "MedicalCondition"));
					body.put(SPL_POC_PREFIX + "MedicalCondition",
							new JSONString(statement.getUrl()));
					body.put(IRdfsOntology.label,
							new JSONString(statement.getLabel()));
					body.put(IDublinCoreTerms.description, new JSONString(
							statement.getDescription()));
					bodies.set(idx, body);
					idx += 1;
				}*/

				if (statement.getLabel().equals("Variant Frequency")) {
					JSONObject body = new JSONObject();
					String statementUUID = UUID.uuid();
					body.put("@id", new JSONString(SPL_URN_PREFIX
							+ statementUUID));
					body.put("@type", new JSONString(SPL_POC_PREFIX
							+ "PharmacogenomicsStatement"));
					body.put(SIO_PREFIX + "SIO_000563", new JSONString(
							SPL_POC_PREFIX + "VariantFrequency"));
					body.put(SPL_POC_PREFIX + "VariantFrequency",
							new JSONString(statement.getUrl()));
					body.put(IRdfsOntology.label,
							new JSONString(statement.getLabel()));
					body.put(IDublinCoreTerms.description, new JSONString(
							statement.getDescription()));
					bodies.set(idx, body);
					idx += 1;
				}

			}
			
			// medication condition
/*			MLinkedResource medicalCondition = ann.getMedconditbody();
			if (medicalCondition != null && !medicalCondition.getLabel().trim().equals("")) {
				JSONObject body = new JSONObject();
				String statementUUID = UUID.uuid();
				body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
				body.put("@type", new JSONString(SPL_POC_PREFIX
						+ "PharmacogenomicsStatement"));
				body.put(SIO_PREFIX + "SIO_000563", new JSONString(SPL_POC_PREFIX
						+ "MedicalCondition"));
				body.put(SPL_POC_PREFIX + "MedicalCondition",
						new JSONString(medicalCondition.getUrl()));
				body.put(IRdfsOntology.label, new JSONString(medicalCondition.getLabel()));
				body.put(IDublinCoreTerms.description,
						new JSONString(medicalCondition.getDescription()));
				bodies.set(idx, body);
				idx += 1;
			}
			*/
		}

		annotation.put(IDomeoOntology.content, bodies);

		return annotation;
	}
}
