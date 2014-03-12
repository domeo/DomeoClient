package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.contentasrdf.model.MContentAsRdf;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.JsoAntibodyUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.JsoPharmgxUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSPL;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
import org.mindinformatics.gwt.framework.component.resources.serialization.JsonGenericResource;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class USPLJsonUnmarshaller extends AUnmarshaller implements
		IUnmarshaller {

	private IDomeo _domeo;

	public USPLJsonUnmarshaller(IDomeo domeo) {
		_domeo = domeo;
	}

	public Object unmarshall(JsonUnmarshallingManager manager,
			JavaScriptObject json, String validation, MAnnotationSet set,
			ArrayList<MSelector> selectors) {
		validation(json, validation, set, selectors);

		JsAnnotationSPL spl = (JsAnnotationSPL) json;

		// TODO: delete this from her and within the MSPLsAnnotation clas
		// because its not used as far as I know
		MContentAsRdf body = new MContentAsRdf();
		// body.setIndividualUri(spl.getBody().get(0).getId());
		// body.setFormat(spl.getBody().get(0).getFormat());
		// body.setChars(spl.getBody().get(0).getChars());

		_domeo.getLogger().debug(
				this,
				"Calling AnnotationFactory.cloneSPLs with body"
						+ body.toString());
		MSPLsAnnotation ann = AnnotationFactory.cloneSPLs(spl.getId(), spl
				.getLineageUri(), spl.getFormattedCreatedOn(), spl
				.getFormattedLastSaved(), set, _domeo.getAgentManager()
				.getAgentByUri(spl.getCreatedBy()), (ISoftware) _domeo
				.getAgentManager().getAgentByUri(spl.getCreatedWith()), spl
				.getVersionNumber(), spl.getPreviousVersion(), _domeo
				.getPersistenceManager().getCurrentResource(), selectors, spl
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
		JsArray<JsoPharmgxUsage> aus = spl.getBodies();

		Set<MLinkedResource> statements = new HashSet<MLinkedResource>();

		for (int j = 0; j < aus.length(); j++) {
			JsoPharmgxUsage au = aus.get(j);

			// PK Impact
			String jsPkImpact = au.getPKImpact();
			if (jsPkImpact != null) {
				_domeo.getLogger().debug(this,
						"jsPkImpact is not null: " + jsPkImpact.toString());

				// TODO: throw an exception if the label or description is null
				String jsLabel = au.getLabel();
				String jsDescript = au.getDescription();
				MLinkedResource pkImpact = ResourcesFactory
						.createLinkedResource(jsPkImpact, jsLabel, jsDescript);
				_domeo.getLogger().debug(
						this,
						"pkImpact linked resource created: "
								+ pkImpact.getLabel());

				ann.setPKImpact(pkImpact);
				_domeo.getLogger().debug(this,
						"pkImpact linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "jsPkImpact is null");
			}

			// PD Impact
			String jsPdImpact = au.getPDImpact();
			if (jsPdImpact != null) {
				_domeo.getLogger().debug(this,
						"jsPdImpact is not null: " + jsPdImpact.toString());

				// TODO: throw an exception if the label or description is null
				String jsLabel = au.getLabel();
				String jsDescript = au.getDescription();
				MLinkedResource pdImpact = ResourcesFactory
						.createLinkedResource(jsPdImpact, jsLabel, jsDescript);
				_domeo.getLogger().debug(
						this,
						"pdImpact linked resource created: "
								+ pdImpact.getLabel());

				ann.setPdImpact(pdImpact);
				_domeo.getLogger().debug(this,
						"pdImpact linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "jsPdImpact is null");
			}

			// drug selection recommendation
			String jsDrugRec = au.getDrugRec();
			if (jsDrugRec != null) {
				_domeo.getLogger().debug(this,
						"jsDrugRec is not null: " + jsDrugRec.toString());

				// TODO: throw an exception if the label or description is null
				String jsLabel = au.getLabel();
				String jsDescript = au.getDescription();
				MLinkedResource drugRec = ResourcesFactory
						.createLinkedResource(jsDrugRec, jsLabel, jsDescript);
				_domeo.getLogger().debug(
						this,
						"drugRec linked resource created: "
								+ drugRec.getLabel());

				ann.setDrugRec(drugRec);
				_domeo.getLogger().debug(this,
						"drugRec linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "jsDrugRec is null");
			}

			// dose selection recommendation
			String jsDoseRec = au.getDoseRec();
			if (jsDoseRec != null) {
				_domeo.getLogger().debug(this,
						"jsDoseRec is not null: " + jsDoseRec.toString());

				// TODO: throw an exception if the label or description is null
				String jsLabel = au.getLabel();
				String jsDescript = au.getDescription();
				MLinkedResource doseRec = ResourcesFactory
						.createLinkedResource(jsDoseRec, jsLabel, jsDescript);
				_domeo.getLogger().debug(
						this,
						"doseRec linked resource created: "
								+ doseRec.getLabel());

				ann.setDoseRec(doseRec);
				_domeo.getLogger().debug(this,
						"doseRec linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "jsDoseRec is null");
			}

			// monitoring recommendation
			String jsMonitRec = au.getMonitRec();
			if (jsMonitRec != null) {
				_domeo.getLogger().debug(this,
						"jsMonitRec is not null: " + jsMonitRec.toString());

				// TODO: throw an exception if the label or description is null
				String jsLabel = au.getLabel();
				String jsDescript = au.getDescription();
				MLinkedResource monitRec = ResourcesFactory
						.createLinkedResource(jsMonitRec, jsLabel, jsDescript);
				_domeo.getLogger().debug(
						this,
						"monitRec linked resource created: "
								+ monitRec.getLabel());

				ann.setMonitRec(monitRec);
				_domeo.getLogger().debug(this,
						"monitRec linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "jsMonitRec is null");
			}

			// test recommendation
			String jsTestRec = au.getTestRec();
			if (jsTestRec != null) {
				_domeo.getLogger().debug(this,
						"jsTestRec is not null: " + jsTestRec.toString());

				// TODO: throw an exception if the label or description is null
				String jsLabel = au.getLabel();
				String jsDescript = au.getDescription();
				MLinkedResource testRec = ResourcesFactory
						.createLinkedResource(jsTestRec, jsLabel, jsDescript);
				_domeo.getLogger().debug(
						this,
						"testRec linked resource created: "
								+ testRec.getLabel());

				ann.setTestRec(testRec);
				_domeo.getLogger().debug(this,
						"testRec linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "jsTestRec is null");
			}

			// common variant
			String jsVariant = au.getVariant();
			if (jsVariant != null) {
				_domeo.getLogger().debug(this,
						"jsVariant is not null: " + jsVariant.toString());

				// TODO: throw an exception if the label or description is null
				String jsLabel = au.getLabel();
				String jsDescript = au.getDescription();
				MLinkedResource variant = ResourcesFactory
						.createLinkedResource(jsVariant, jsLabel, jsDescript);
				_domeo.getLogger().debug(
						this,
						"variant linked resource created: "
								+ variant.getLabel());

				ann.setVariant(variant);
				_domeo.getLogger().debug(this,
						"variant linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "jsVariant is null");
			}

			// common test
			String jsTest = au.getTest();
			if (jsTest != null) {
				_domeo.getLogger().debug(this,
						"jsTest is not null: " + jsTest.toString());

				// TODO: throw an exception if the label or description is null
				String jsLabel = au.getLabel();
				String jsDescript = au.getDescription();
				MLinkedResource test = ResourcesFactory.createLinkedResource(
						jsTest, jsLabel, jsDescript);
				_domeo.getLogger().debug(this,
						"test linked resource created: " + test.getLabel());

				ann.setTest(test);
				_domeo.getLogger().debug(this,
						"test linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "jsTest is null");
			}

			// biomarker of interest
			String jsBiomarker = au.getBiomarkers();
			System.out.println("jsBiomarker:" + jsBiomarker);
			if (jsBiomarker != null) {
				_domeo.getLogger().debug(this,
						"jsBiomarker is not null: " + jsBiomarker.toString());

				// TODO: throw an exception if the label or description is null
				String jsLabel = au.getLabel();
				String jsDescript = au.getDescription();
				MLinkedResource biomarker = ResourcesFactory
						.createLinkedResource(jsBiomarker, jsLabel, jsDescript);
				_domeo.getLogger().debug(
						this,
						"biomarker linked resource created: "
								+ biomarker.getLabel());

				ann.setBiomarkers(biomarker);
				_domeo.getLogger().debug(this,
						"biomarker linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "jsBiomarker is null");
			}

			// drug of interest
			String jsDrug = au.getDrug();
			System.out.println("jsDrug:" + jsDrug);

			if (jsDrug != null) {
				_domeo.getLogger().debug(this,
						"jsDrug is not null: " + jsDrug.toString());

				// TODO: throw an exception if the label or description is null
				String jsLabel = au.getLabel();
				String jsDescript = au.getDescription();
				MLinkedResource drug = ResourcesFactory.createLinkedResource(
						jsDrug, jsLabel, jsDescript);
				_domeo.getLogger().debug(this,
						"drug linked resource created: " + drug.getLabel());

				ann.setDrugOfInterest(drug);
				_domeo.getLogger().debug(this,
						"drug linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "jsDrug is null");
			}

			// product label section
			String jsPLS = au.getProductLabelSection();
			if (jsPLS != null) {
				_domeo.getLogger().debug(this,
						"jsPLS is not null: " + jsPLS.toString());

				// TODO: throw an exception if the label or description is null
				int index_label = jsPLS.indexOf("#");

				String jsUrl = jsPLS;
				String jsLabel = au.getLabel();
				
				//jsPLS = prefix + label, split as url and label
				if (index_label > 0) {
					jsUrl = jsPLS.substring(0, index_label);
					jsLabel = jsPLS.substring(index_label + 1);
				} 

				String jsDescript = au.getDescription();
				MLinkedResource pls = ResourcesFactory.createLinkedResource(
						jsUrl, jsLabel, jsDescript);
				_domeo.getLogger().debug(this,
						"pls linked resource created: " + pls.getLabel());

				ann.setProductLabelSelection(pls);
				_domeo.getLogger().debug(this,
						"pls linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "jsPLS is null");
			}

			// alleles
			String alleles = au.getAlleles();
			if (alleles != null) {
				_domeo.getLogger().debug(this,
						"alleles is not null: " + alleles.toString());

				ann.setAllelesbody(alleles);
				_domeo.getLogger().debug(this,
						"alleles linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "alleles is null");
			}

			// medical condition
			String medicalCondtion = au.getMedicalCondition();
			if (medicalCondtion != null) {
				_domeo.getLogger().debug(
						this,
						"medicalCondtion is not null: "
								+ medicalCondtion.toString());

				ann.setMedconditbody(medicalCondtion);
				_domeo.getLogger().debug(this,
						"medicalCondtion linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "medicalCondtion is null");
			}

			// comment
			String comment = au.getComment();
			if (comment != null) {
				_domeo.getLogger().debug(this,
						"comment is not null: " + comment.toString());

				ann.setComment(comment);
				_domeo.getLogger().debug(this,
						"comment linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "comment is null");
			}

			// Concepts that the Statement Refers To: Active ingredient &
			// Concomitant medication concern & Variant Frequency

			// active ingredient in statements
			String jsActiveIngredient = au.getActiveIngredient();
			if (jsActiveIngredient != null) {

				_domeo.getLogger().debug(
						this,
						"jsActiveIngredient is not null: "
								+ jsActiveIngredient.toString());

				// TODO: throw an exception if the label or description is null
				String jsLabel = au.getLabel();
				String jsDescript = au.getDescription();
				MLinkedResource activeIngredient = ResourcesFactory
						.createLinkedResource(jsActiveIngredient, jsLabel,
								jsDescript);
				_domeo.getLogger().debug(
						this,
						"activeIngredient linked resource created: "
								+ activeIngredient.getLabel());

				statements.add(activeIngredient);
				_domeo.getLogger().debug(this,
						"activeIngredient linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "jsActiveIngredient is null");
			}

			// medication concern in statements
			String jsMediConcern = au.getMedicatConcern();
			if (jsMediConcern != null) {

				_domeo.getLogger().debug(
						this,
						"jsMediConcern is not null: "
								+ jsMediConcern.toString());

				// TODO: throw an exception if the label or description is null
				String jsLabel = au.getLabel();
				String jsDescript = au.getDescription();
				MLinkedResource mediConcern = ResourcesFactory
						.createLinkedResource(jsMediConcern, jsLabel,
								jsDescript);
				_domeo.getLogger().debug(
						this,
						"mediConcern linked resource created: "
								+ mediConcern.getLabel());

				statements.add(mediConcern);
				_domeo.getLogger().debug(this,
						"mediConcern linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "jsMediConcern is null");
			}

			// variant frequency in statements
			String jsVariFrequency = au.getVariantFrequency();
			if (jsVariFrequency != null) {
				_domeo.getLogger().debug(
						this,
						"jsVariFrequency is not null: "
								+ jsVariFrequency.toString());

				// TODO: throw an exception if the label or description is null
				String jsLabel = au.getLabel();
				String jsDescript = au.getDescription();
				MLinkedResource variFrequency = ResourcesFactory
						.createLinkedResource(jsVariFrequency, jsLabel,
								jsDescript);
				_domeo.getLogger().debug(
						this,
						"variFrequency linked resource created: "
								+ variFrequency.getLabel());

				statements.add(variFrequency);
				_domeo.getLogger().debug(this,
						"variFrequency linked resource added to annotation");
			} else {
				_domeo.getLogger().debug(this, "jsVariFrequency is null");
			}

		}

		if (statements != null && statements.size() > 0)
			ann.setStatements(statements);

		return ann;
	}

	protected void validation(JavaScriptObject json, String validation,
			MAnnotationSet set, ArrayList<MSelector> selectors) {
		// TODO Auto-generated method stub

	}
}
