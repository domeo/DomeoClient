package org.mindinformatics.gwt.domeo.plugins.annotation.spls.serialization;

import java.util.Set;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibodyAnnotation;
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
public class JsonSPLsAnnotationSerializer extends JsonAnnotationSerializer
		implements IPharmgxOntology {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		// TODO: use the IPharmgxOntology/SPL ontoogy for these

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
			body.put("@type", new JSONString("poc:"
					+ "PharmacogenomicsStatement")); // common
			body.put("sio:" + "SIO_000563", new JSONString("poc:"
					+ "PharmacokineticImpact"));
			
			body.put("poc:" + "PharmacokineticImpact",
					new JSONString(pkImpact.getUrl()));
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
			body.put("@type", new JSONString("poc:"
					+ "PharmacogenomicsStatement"));
			body.put("sio:" + "SIO_000563", new JSONString("poc:"
					+ "PharmacodynamicImpact"));
			body.put("poc:" + "PharmacodynamicImpact",
					new JSONString(pdImpact.getUrl()));
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
			body.put("@type", new JSONString("poc:"
					+ "PharmacogenomicsStatement"));
			body.put("sio:" + "SIO_000563", new JSONString("poc:"
					+ "DrugSelectionRecommendation"));
			body.put("poc:" + "DrugSelectionRecommendation", new JSONString(
					drugRec.getUrl()));
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
			body.put("@type", new JSONString("poc:"
					+ "PharmacogenomicsStatement"));
			body.put("sio:" + "SIO_000563", new JSONString("poc:"
					+ "DoseSelectionRecommendation"));
			body.put("poc:" + "DoseSelectionRecommendation", new JSONString(
					doseRec.getUrl()));
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
			body.put("@type", new JSONString("poc:"
					+ "PharmacogenomicsStatement"));
			body.put("sio:" + "SIO_000563", new JSONString("poc:"
					+ "MonitoringRecommendation"));
			body.put("poc:" + "MonitoringRecommendation", new JSONString(
					monitRec.getUrl()));
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
			body.put("@type", new JSONString("poc:"
					+ "PharmacogenomicsStatement"));
			body.put("sio:" + "SIO_000563", new JSONString("poc:"
					+ "TestRecommendation"));
			body.put("poc:" + "TestRecommendation",
					new JSONString(testRec.getUrl()));
			body.put(IRdfsOntology.label, new JSONString(testRec.getLabel()));
			body.put(IDublinCoreTerms.description,
					new JSONString(testRec.getDescription()));
			bodies.set(idx, body);
			idx += 1;
		}

		// Common Variant
		MLinkedResource variant = ann.getPhenotype();
		if (variant != null && !variant.getLabel().equals("unselected")) {
			JSONObject body = new JSONObject();
			String statementUUID = UUID.uuid();
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
			body.put("@type", new JSONString("poc:"
					+ "PharmacogenomicsStatement"));
			body.put("sio:" + "SIO_000628", new JSONString("poc:" + "Variant"));
			body.put("poc:" + "Variant", new JSONString(variant.getUrl()));
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
			body.put("@type", new JSONString("poc:"
					+ "PharmacogenomicsStatement"));
			body.put("sio:" + "SIO_000338", new JSONString("poc:"
					+ "TestResult"));
			body.put("poc:" + "TestResult", new JSONString(test.getUrl()));
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
			body.put("@type", new JSONString("poc:"
					+ "PharmacogenomicsStatement"));
			body.put("sio:" + "SIO_000628", new JSONString("dailymed:"
					+ "pharmgxDrug"));

			String drugUri = drug.getUrl();

			//System.out.println("uri:" + drugUri);

			// two uri for single drug
		/*	if (drugUri.substring(4).contains("http://")) {

				// separate uri
				int split = drugUri.substring(4).indexOf("http://") + 4;
				String uri1 = drugUri.substring(0, split);
				String uri2 = drugUri.substring(split);

				// use the drug name as key to put jsonObject
				// drugname1_and_drugname2
				String drugname1 = drug.getLabel().substring(0,
						drug.getLabel().indexOf("_"));

				String drugname2 = drug.getLabel();
				
				System.out.println("drugname1:"+drugname1+"|drugname2"+drugname2);
				
				while (drugname2.contains("_")) {
					drugname2 = drugname2.substring(drugname2.indexOf("_") + 1);
				}

				// put name-uri mapping to jsonObject
				// put two uri for combo make PLS can't brings back
				//body.put("dailymed:" + drugname1, new JSONString(uri1));
				//body.put("dailymed:" + drugname2, new JSONString(uri2));
				
				body.put("dailymed:" + "pharmgxDrug", new JSONString(uri1));
			} else {*/
				body.put("dailymed:" + "pharmgxDrug", new JSONString(drugUri));
			//}

			body.put(IRdfsOntology.label, new JSONString(drug.getLabel()));
			body.put(IDublinCoreTerms.description,
					new JSONString(drug.getDescription()));
			bodies.set(idx, body);
			idx += 1;
		}
		
		// HGNCGeneSymbol
				MLinkedResource HGNCGeneSymbol = ann.getHGNCGeneSymbol();
				if (HGNCGeneSymbol != null && !HGNCGeneSymbol.getLabel().equals("unselected")) {
					JSONObject body = new JSONObject();
					String statementUUID = UUID.uuid();
					body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
					body.put("@type", new JSONString("poc:"
							+ "PharmacogenomicsStatement"));
					body.put("sio:" + "SIO_000628", new JSONString("dailymed:"
							+ "HGNCGeneSymbol"));

					// process multiple uris for a single biomarker
					String Uri = HGNCGeneSymbol.getUrl();
					int count = 1;

					System.out.println("Uri" + Uri);
					while (Uri.substring(4).contains("http://")) {
						int end = Uri.substring(4).indexOf("http://") + 4;
						String subUri = Uri.substring(0, end);

						// System.out.println("subUri:"+subUri);

						body.put("dailymed:" + "HGNCGeneSymbol" + count++,
								new JSONString(subUri));
						Uri = Uri.substring(end);
					}
					// System.out.println("last Uri"+Uri);
					body.put("dailymed:" + "HGNCGeneSymbol", new JSONString(Uri));

					body.put(IRdfsOntology.label, new JSONString(HGNCGeneSymbol.getLabel()));
					body.put(IDublinCoreTerms.description,
							new JSONString(HGNCGeneSymbol.getDescription()));
					bodies.set(idx, body);
					idx += 1;
				}
		

		// Biomarkers of interest
		MLinkedResource biomarkers = ann.getBiomarkers();
		if (biomarkers != null && !biomarkers.getLabel().equals("unselected")) {
			JSONObject body = new JSONObject();
			String statementUUID = UUID.uuid();
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
			body.put("@type", new JSONString("poc:"
					+ "PharmacogenomicsStatement"));
			body.put("sio:" + "SIO_000628", new JSONString("dailymed:"
					+ "pharmgxBiomarker"));

			// process multiple uris for a single biomarker
			String Uri = biomarkers.getUrl();
			int count = 1;

			System.out.println("Uri" + Uri);
			while (Uri.substring(4).contains("http://")) {
				int end = Uri.substring(4).indexOf("http://") + 4;
				String subUri = Uri.substring(0, end);

				// System.out.println("subUri:"+subUri);

				body.put("dailymed:" + "pharmgxBiomarker" + count++,
						new JSONString(subUri));
				Uri = Uri.substring(end);
			}
			// System.out.println("last Uri"+Uri);
			body.put("dailymed:" + "pharmgxBiomarker", new JSONString(Uri));

			body.put(IRdfsOntology.label, new JSONString(biomarkers.getLabel()));
			body.put(IDublinCoreTerms.description,
					new JSONString(biomarkers.getDescription()));
			bodies.set(idx, body);
			idx += 1;
		}

		// product label section
		MLinkedResource productls = ann.getProductLabelSelection();
		if (productls != null && !productls.getLabel().equals("unselected")) {
			JSONObject body = new JSONObject();
			String statementUUID = UUID.uuid();
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
			body.put("@type", new JSONString("poc:"
					+ "PharmacogenomicsStatement"));

			body.put("sio:" + "SIO_000111",
					new JSONString(productls.getLabel()));

			// made up a url = poc prefix + label
			body.put("poc:" + "productLabelSection", new JSONString(
					SPL_POC_PREFIX + productls.getLabel()));

			body.put(IRdfsOntology.label, new JSONString("SPL Section"));
			body.put(IDublinCoreTerms.description,
					new JSONString(productls.getDescription()));
			bodies.set(idx, body);
			idx += 1;
		}

		// alleles
		String alleles = ann.getAllelesbody();

		if (alleles != null && !alleles.trim().equals("")) {

			JSONObject body = new JSONObject();
			String statementUUID = UUID.uuid();
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
			body.put("@type", new JSONString("poc:"
					+ "PharmacogenomicsStatement"));
			body.put(NCIT_PREFIX + "Alleles", new JSONString(alleles));
			body.put(IRdfsOntology.label, new JSONString("Alleles"));
			bodies.set(idx, body);
			idx += 1;
		}

		// medical condition
		String medicalCondition = ann.getMedconditbody();

		if (medicalCondition != null && !medicalCondition.trim().equals("")) {

			JSONObject body = new JSONObject();
			String statementUUID = UUID.uuid();
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
			body.put("@type", new JSONString("poc:"
					+ "PharmacogenomicsStatement"));
			body.put("poc:" + "MedicalCondition", new JSONString(
					medicalCondition));
			body.put(IRdfsOntology.label, new JSONString("MedicalCondition"));
			bodies.set(idx, body);
			idx += 1;
		}

		// comment
		String comment = ann.getComment();
		System.out.println("comment in seri:" + comment);

		if (comment != null && !comment.trim().equals("")) {

			JSONObject body = new JSONObject();
			String statementUUID = UUID.uuid();
			body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
			body.put("@type", new JSONString("poc:"
					+ "PharmacogenomicsStatement"));
			body.put("poc:" + "Comment", new JSONString(comment));
			body.put(IRdfsOntology.label, new JSONString("Comment"));
			bodies.set(idx, body);
			idx += 1;
		}

		// Concepts that the Statement Refers To: Active ingredient &
		// Concomitant medication concern & Variant Frequency
		Set<MLinkedResource> statements = ann.getStatements();

		if (statements != null) {

			for (MLinkedResource statement : statements) {
				
				
				// Clinical Trial
				if (statement.getLabel().equals("Clinical Trial")) {
					JSONObject body = new JSONObject();
					String statementUUID = UUID.uuid();
					body.put("@id", new JSONString(SPL_URN_PREFIX
							+ statementUUID));
					body.put("@type", new JSONString("poc:"
							+ "PharmacogenomicsStatement"));
					body.put("sio:" + "SIO_000628", new JSONString("dailymed:"
							+ "ClinicalTrial"));

					body.put("dailymed:" + "ClinicalTrial", new JSONString(
							statement.getUrl()));

					body.put(IRdfsOntology.label,
							new JSONString(statement.getLabel()));
					body.put(IDublinCoreTerms.description, new JSONString(
							statement.getDescription()));
					bodies.set(idx, body);
					idx += 1;
				}
				

				// Prodrug
				if (statement.getLabel().equals("Prodrug")) {
					JSONObject body = new JSONObject();
					String statementUUID = UUID.uuid();
					body.put("@id", new JSONString(SPL_URN_PREFIX
							+ statementUUID));
					body.put("@type", new JSONString("poc:"
							+ "PharmacogenomicsStatement"));
					body.put("sio:" + "SIO_000628", new JSONString("dailymed:"
							+ "Prodrug"));

					body.put("dailymed:" + "Prodrug", new JSONString(
							statement.getUrl()));

					body.put(IRdfsOntology.label,
							new JSONString(statement.getLabel()));
					body.put(IDublinCoreTerms.description, new JSONString(
							statement.getDescription()));
					bodies.set(idx, body);
					idx += 1;
				}

				// Concomitant medication concern
				if (statement.getLabel().equals(
						"Concomitant medication concern")) {
					JSONObject body = new JSONObject();
					String statementUUID = UUID.uuid();
					body.put("@id", new JSONString(SPL_URN_PREFIX
							+ statementUUID));
					body.put("@type", new JSONString("poc:"
							+ "PharmacogenomicsStatement"));
					body.put("sio:" + "SIO_000628", new JSONString("dailymed:"
							+ "ConcomitantMedicationOfConcern"));
					body.put("dailymed:" + "ConcomitantMedicationOfConcern",
							new JSONString(SPL_POC_PREFIX
									+ "concomitant-medication-concern"));
					body.put(IRdfsOntology.label,
							new JSONString(statement.getLabel()));
					body.put(IDublinCoreTerms.description, new JSONString(
							statement.getDescription()));
					bodies.set(idx, body);
					idx += 1;
				}

				// Variant Frequency
				if (statement.getLabel().equals("Variant Frequency")) {
					JSONObject body = new JSONObject();
					String statementUUID = UUID.uuid();
					body.put("@id", new JSONString(SPL_URN_PREFIX
							+ statementUUID));
					body.put("@type", new JSONString("poc:"
							+ "PharmacogenomicsStatement"));
					body.put("sio:" + "SIO_000563", new JSONString("poc:"
							+ "VariantFrequency"));
					body.put("poc:" + "VariantFrequency", new JSONString(
							SPL_POC_PREFIX + "population-frequency-mentioned"));
					body.put(IRdfsOntology.label,
							new JSONString(statement.getLabel()));
					body.put(IDublinCoreTerms.description, new JSONString(
							statement.getDescription()));
					bodies.set(idx, body);
					idx += 1;
				}

			}

		}

		annotation.put(IDomeoOntology.content, bodies);

		return annotation;
	}

}
