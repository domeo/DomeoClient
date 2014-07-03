package org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.text.TextAnnotationFormsPanel;
import org.mindinformatics.gwt.domeo.client.ui.content.AnnotationFrameWrapper;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.IPharmgxOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MPharmgx;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLPharmgxUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.SPLsFactory;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
import org.mindinformatics.gwt.framework.src.IResizable;
import org.mindinformatics.gwt.framework.widget.ButtonWithIcon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class FSPLsForm extends AFormComponent implements IResizable,
		IPharmgxOntology {

	private static Logger logger = Logger.getLogger("");

	public static final String LABEL = "pharmacogenomics";
	public static final String LABEL_EDIT = "EDIT SPL ANNOTATION";

	public static final String LOG_CATEGORY_QUALIFIER_CREATE = "CREATING SPL ANNOTATION";
	public static final String LOG_CATEGORY_QUALIFIER_EDIT = "EDITING SPL ANNOTATION";

	interface Binder extends UiBinder<VerticalPanel, FSPLsForm> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private MSPLsAnnotation _item;
	private MPharmgx currentPharmgx;

	// for uri mapping: providing a instance of MPharmgx
	private MPharmgx pharmgxmodel = new MPharmgx("", "", null);

	@UiField
	VerticalPanel container;
	@UiField
	FlowPanel newQualifiers;
	@UiField
	HorizontalPanel buttonsPanel;
	@UiField
	ListBox annotationSet;
	@UiField
	VerticalPanel rightColumn;

	// Biomarkers
	@UiField
	ListBox descriptbm, descriptsvtlb, descriptstslb, descriptdoi, descriptpls,
			descripthgs;

	// PK Impact
	@UiField
	RadioButton descriptpkdm, descriptpkim, descriptpkia, descriptpkda,
			descriptpkid, descriptpkdd, descriptpkie, descriptpkde,
			descriptpkni, descriptpknone;

	// PD Impact
	@UiField
	RadioButton descriptpddt, descriptpdit, descriptpdir, descriptpdni,
			descriptpdie, descriptpdde, descriptpdnone;

	// recommendation drug
	@UiField
	CheckBox descriptdsca, descriptdsal, descriptdsam, descriptdsnr,
			descriptdsns, descriptdsnc, descriptdsnone;

	// recommendation dose
	@UiField
	CheckBox descriptdrdfb, descriptdrifb, descriptdrnc, descriptdrus,
			descriptdrcs, descriptdrnone;

	// recommendation monitoring
	@UiField
	CheckBox descriptmreq, descriptmrec, descriptmnc, descriptmcms,
			descriptmcnone;

	// test section
	@UiField
	CheckBox descripttreq, descripttrec, descriptttna, descripttnn,
			descripttnone;

	// statements
	@UiField
	CheckBox descriptspd, descriptsmcc, descriptsct;

	@UiField
	CheckBox descriptppm;

	// alleles
	@UiField
	TextArea commentBody, allelesbody, medconditbody, descriptsothervt,
			descriptsotherts;

	String[] HGNCGeneSymbols = { "ALK", "BAFF/TNFSF13B", "BCR/ABL1", "BRAF",
			"CCR5", "CFTR", "CYB5R1-4", "CYP1A2", "CYP2C19", "CYP2C9",
			"CYP2D6", "del (5q)", "DPYD", "EGFR", "ERBB2", "ESR1", "ESR1, PGR",
			"F2", "F5", "FIP1L1/PDGFRA", "G6PD", "GBA", "HLA-A", "HLA-B",
			"HPRT1", "IFNL3", "IL2RA", "KIT", "KRAS", "LDLR", "MS4A1", "NAGS",
			"NAGS, CPS1, ASS1, OTC, ASL, ABL2", "NAT1-2", "PDGFRB",
			"Ph Chromosome", "PML/RARA", "POLG", "SERPINC1", "TNFRSF8", "TPMT",
			"UGT1A1", "VKORC1" };

	String[] variant = { "poor-metabolizer", "intermediate-metabolizer",
			"extensive-metabolizer", "ultra-metabolizer",
			"intermediate-activity", "low-or-absent-activity", "HLA-B*1502",
			"HLA-B*5701" };

	String[] test = { "gene-haplotype-positive", "gene-haplotype-negative",
			"gene-SNP-positive", "gene-SNP-negative", "fused-gene-positive",
			"fused-gene-negative", "gene-expression-level-high",
			"gene-expression-level-low", "biomarker-positive",
			"biomarker-negative", "biomarker-level-high",
			"biomarker-level-low", "poor-metabolizer-positive",
			"poor-metabolizer-negative", "ultra-metabolizer-positive",
			"ultra-metabolizer-negative", "chromosomal-aberration-positive",
			"chromosomal-aberration-negative" };

	// TODO: get real URIs from the swat-4-med-safety project
	// "Sodium_Phenylacetate", "Sodium_Benzoate","Sodium_Phenylbutyrate",
	
	// TODO: should change model to process product that have two URIs
	// "Chlordiazepoxide_and_Amitriptyline","Dextromethorphan_and_Quinidine",
	// "Drospirenone_and_Estradiol",
	// "Fluoxetine_and_Olanzapine", "Tramadol_and_Acetaminophen"

	String[] drugs = { "Abacavir", "Aripiprazole", "Arsenic_Trioxide",
			"Atomoxetine", "Atorvastatin", "Azathioprine", "Boceprevir",
			"Brentuximab_Vedotin", "Busulfan", "Capecitabine", "Carbamazepine",
			"Carisoprodol", "Carvedilol", "Celecoxib", "Cetuximab",
			"Cevimeline", "Chloroquine", "Cisplatin", "Citalopram", "Clobazam",
			"Clomiphene", "Clomipramine", "Clopidogrel", "Clozapine",
			"Codeine", "Crizotinib", "Dapsone", "Dasatinib",
			"Denileukin_Diftitox", "Desipramine", "Dexlansoprazole",
			"Diazepam", "Doxepin", "Erlotinib", "Esomeprazole", "Exemestane",
			"Everolimus", "Fluorouracil", "Fluoxetine", "Flurbiprofen",
			"Fluvoxamine", "Fulvestrant", "Galantamine", "Iloperidone",
			"Imatinib", "Imipramine", "Indacaterol", "Irinotecan",
			"Isosorbide", "Ivacaftor", "Lapatinib", "Lenalidomide",
			"Letrozole", "Maraviroc", "Mercaptopurine", "Metoprolol",
			"Modafinil", "Nefazodone", "Nilotinib", "Nortriptyline",
			"Omeprazole", "Panitumumab", "Pantoprazole", "Paroxetine",
			"Peginterferon_alfa-2b", "Perphenazine", "Pertuzumab", "Phenytoin",
			"Pimozide", "Prasugrel", "Pravastatin", "Propafenone",
			"Propranolol", "Protriptyline", "Quinidine", "Rabeprazole",
			"Rasburicase", "Rifampin", "Isoniazid", "Pyrazinamide",
			"Risperidone", "Tamoxifen", "Telaprevir", "Terbinafine",
			"Tetrabenazine", "Thioguanine", "Thioridazine", "Ticagrelor",
			"Tolterodine", "Tositumomab", "Trastuzumab", "Tretinoin",
			"Trimipramine", "Valproic_Acid", "Vemurafenib", "Venlafaxine",
			"Voriconazole", "Warfarin" };

	String[] productLabelSections = {
			"34086-9 ABUSE SECTION",
			"34084-4 ADVERSE REACTIONS SECTION",
			"69761-5 ALARMS",
			"34066-1 BOXED WARNING SECTION",
			"34083-6 CARCINOGENESIS AND MUTAGENESIS AND IMPAIRMENT OF FERTILITY SECTION",
			"34090-1 CLINICAL PHARMACOLOGY SECTION",
			"34092-7 CLINICAL STUDIES SECTION",
			"34070-3 CONTRAINDICATIONS SECTION",
			"34085-1 CONTROLLED SUBSTANCE SECTION",
			"34087-7 DEPENDENCE SECTION", "34089-3 DESCRIPTION SECTION",
			"69763-1 DISPOSAL AND WASTE HANDLING",
			"34068-7 DOSAGE AND ADMINISTRATION SECTION",
			"43678-2 DOSAGE FORMS AND STRENGTHS SECTION",
			"34074-5 DRUG AND OR LABORATORY TEST INTERACTIONS SECTION",
			"42227-9 DRUG ABUSE AND DEPENDENCE SECTION",
			"34073-7 DRUG INTERACTIONS SECTION",
			"50742-6 ENVIRONMENTAL WARNING SECTION",
			"50743-4 FOOD SAFETY WARNING SECTION",
			"34072-9 GENERAL PRECAUTIONS SECTION",
			"34082-8 GERIATRIC USE SECTION",
			"71744-7 HEALTH CARE PROVIDER LETTER SECTION",
			"69719-3 HEALTH CLAIM SECTION", "34069-5 HOW SUPPLIED SECTION",
			"51727-6 INACTIVE INGREDIENT SECTION",
			"34067-9 INDICATIONS AND USAGE SECTION",
			"50744-2 INFORMATION FOR OWNERS/CAREGIVERS SECTION",
			"34076-0 INFORMATION FOR PATIENTS SECTION",
			"59845-8 INSTRUCTIONS FOR USE SECTION",
			"34079-4 LABOR AND DELIVERY SECTION",
			"34075-2 LABORATORY TESTS SECTION",
			"43679-0 MECHANISM OF ACTION SECTION",
			"49489-8 MICROBIOLOGY SECTION",
			"43680-8 NONCLINICAL TOXICOLOGY SECTION",
			"34078-6 NONTERATOGENIC EFFECTS SECTION",
			"34080-2 NURSING MOTHERS SECTION",
			"60561-8 OTHER SAFETY INFORMATION", "34088-5 OVERDOSAGE SECTION",
			"51945-4 PACKAGE LABEL.PRINCIPAL DISPLAY PANEL",
			"68498-5 PATIENT MEDICATION INFORMATION SECTION",
			"34081-0 PEDIATRIC USE SECTION",
			"43681-6 PHARMACODYNAMICS SECTION",
			"66106-6 PHARMACOGENOMICS SECTION",
			"43682-4 PHARMACOKINETICS SECTION", "42232-9 PRECAUTIONS SECTION",
			"42228-7 PREGNANCY SECTION",
			"43683-2 RECENT MAJOR CHANGES SECTION",
			"34093-5 REFERENCES SECTION", "53412-3 RESIDUE WARNING SECTION",
			"69759-9 RISKS",
			"60562-6 ROUTE, METHOD AND FREQUENCY OF ADMINISTRATION",
			"50741-8 SAFE HANDLING WARNING SECTION",
			"48779-3 SPL INDEXING DATA ELEMENTS SECTION",
			"48780-1 SPL PRODUCT DATA ELEMENTS SECTION",
			"42231-1 SPL MEDGUIDE SECTION",
			"42230-3 SPL PATIENT PACKAGE INSERT SECTION",
			"42229-5 SPL UNCLASSIFIED SECTION",
			"69718-5 STATEMENT OF IDENTITY SECTION",
			"44425-7 STORAGE AND HANDLING SECTION",
			"60563-4 SUMMARY OF SAFETY AND EFFECTIVENESS",
			"34077-8 TERATOGENIC EFFECTS SECTION", "69762-3 TROUBLESHOOTING",
			"43684-0 USE IN SPECIFIC POPULATIONS SECTION",
			"54433-8 USER SAFETY WARNINGS SECTION",
			"43685-7 WARNINGS AND PRECAUTIONS SECTION",
			"34071-1 WARNINGS SECTION" };

	// drug of interest
	public MLinkedResource getDrugOfInterest() {

		int indexdoi = descriptdoi.getSelectedIndex();

		if (indexdoi != 0) {
			String drugname = descriptdoi.getItemText(indexdoi).toUpperCase();

			String druguri = pharmgxmodel.getDrugUri(drugname);

			if (druguri == null || druguri.trim().equals("")) {
				System.out.println("WARNING: DRUG URI IS NOT FOUND IN MAP");
				druguri = "";
			}

			return ResourcesFactory.createLinkedResource(druguri,
					descriptdoi.getItemText(indexdoi), "The drug of interest.");
		} else
			return null;
	}

	// HGNCGeneSymbol
	public MLinkedResource getHGNCGeneSymbol() {

		int indexhgs = descripthgs.getSelectedIndex();
		if (indexhgs != 0) {

			String hgsName = descripthgs.getItemText(indexhgs);

			String hgsuri = pharmgxmodel.getHGNCGeneSymbolUri(hgsName);

			if (hgsuri == null || hgsuri.trim().equals("")) {
				System.out
						.println("WARNING: HGNCGeneSymbol URI IS NOT FOUND IN MAP");
				hgsuri = "";
			}

			return ResourcesFactory.createLinkedResource(hgsuri, hgsName,
					"The selected HGNCGeneSymbol.");
		} else
			return null;
	}

	// biomarkers
	public MLinkedResource getBioMarkers() {

		int indexbm = descriptbm.getSelectedIndex();
		if (indexbm != 0) {
			String biomarkerName = descriptbm.getItemText(indexbm);

			String biomarkerURI = pharmgxmodel.getBioUri(indexbm - 1);

			return ResourcesFactory.createLinkedResource(biomarkerURI,
					biomarkerName, "The selected biomarker.");
		} else
			return null;
	}

	// Product label sections
	public MLinkedResource getProductLabelSection() {

		int indexpls = descriptpls.getSelectedIndex();

		return ResourcesFactory
				.createLinkedResource(
						SPL_POC_PREFIX,
						descriptpls.getItemText(indexpls),
						"The section of the label where pharmacists identify clinical pharmgx statements");
	}

	// pk impact
	public MLinkedResource getPkImpact() {

		if (descriptpkia.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "absorption-increase",
							"Absorption Increase",
							"The pharmacogenomic biomarker is associated with a increase in absorption of the drug.");
		} else if (descriptpkda.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "absorption-decrease",
							"Absorption Decrease",
							"The pharmacogenomic biomarker is associated with an decrease in absorption of the drug.");
		} else if (descriptpkid.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "distribution-increase",
							"Distribution Increase",
							"The pharmacogenomic biomarker is associated with a increase in distribution of the drug.");
		} else if (descriptpkdd.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "distribution-decrease",
							"Distribution Decrease",
							"The pharmacogenomic biomarker is associated with an decrease in distribution of the drug.");
		} else if (descriptpkim.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "metabolism-increase",
							"Metabolism Increase",
							"The pharmacogenomic biomarker is associated with an increase in metabolism of the drug.");
		} else if (descriptpkdm.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "metabolism-decrease",
							"Metabolism Decrease",
							"The pharmacogenomic biomarker is associated with a decrease in metabolism of the drug.");
		} else if (descriptpkie.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "excretion-increase",
							"Excretion Increase",
							"The pharmacogenomic biomarker is associated with a increase in excretion of the drug");
		} else if (descriptpkde.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "excretion-decrease",
							"Excretion Decrease",
							"The pharmacogenomic biomarker is associated with a decrease in excretion of the drug");
		} else if (descriptpkni.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "not-important",
							"Not Important",
							"The pharmacogenomic biomarker is not associated any clinically relevant pharmacokinetic with respect to the drug.");
		}

		return null;
	}

	// Pharmacodynamic impact PD
	public MLinkedResource getPdImpact() {

		if (descriptpddt.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "drug-toxicity-risk-decreased",
							"Decreased Toxicity Risk",
							"The pharmacogenomic biomarker is associated with an decreased risk of toxicity.");
		} else if (descriptpdit.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "drug-toxicity-risk-increased",
							"Increased Toxicity Risk",
							"The pharmacogenomic biomarker is associated with an increased risk of toxicity.");
		} else if (descriptpdir.getValue()) {
			return ResourcesFactory.createLinkedResource(SPL_POC_PREFIX
					+ "influences-drug-response", "Influences Drug Response",
					"The pharmacogenomic biomarker influences drug response");
		} else if (descriptpdni.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "not-important",
							"Not Important",
							"The pharmacogenomic biomarker is not associated with clinically relevant pharmacodynamic effect");
		} else if (descriptpdie.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX
									+ "drug-efficacy-increased-from-baseline",
							"Increased Efficacy",
							"The pharmacogenomic biomarker is associated with an increase in the efficacy of the drug. ");
		} else if (descriptpdde.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX
									+ "drug-efficacy-decreased-from-baseline",
							"Decreased Efficacy",
							"The pharmacogenomic biomarker is associated with an decrease in the efficacy of the drug.");
		}

		return null;
	}

	// Recommendation drug
	public MLinkedResource getDrugRec() {

		if (descriptdsal.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "alternative-recommended",
							"Alternative Recommended",
							"The pharmacogenomic biomarker is related to a recommendation to use an alternative drug.");
		} else if (descriptdsnr.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "discontinue-do-not-restart",
							"Discontinue/Do not restart",
							"The pharmacogenomic biomarker is related to a recommendation to not restart the drug");
		} else if (descriptdsns.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "do-not-start",
							"Do not start",
							"The pharmacogenomic biomarker is not associated with any drug selection recommendation.");
		} else if (descriptdsnc.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "no-change-necessary",
							"Not change necessary",
							"The pharmacogenomic biomarker is not associated with any drug selection recommendation.");
		} else if (descriptdsam.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "addition-of-medication",
							"Addition of medication",
							"The pharmacogenomic biomarker is related to a recommendation to add a concomitant medication.");
		} else if (descriptdsca.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "change-in-route-of-admin",
							"Change in route of administration",
							"The pharmacogenomic biomarker is related to a recommendation to add change the route of administration for the drug.");
		}

		// else if (descriptdsnone.getValue()) {
		// return ResourcesFactory.createLinkedResource(SPL_POC_PREFIX
		// + "none", "None", "none.");
		// }
		return null;
	}

	// Recommendation Dose
	public MLinkedResource getDoseRec() {

		if (descriptdrdfb.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX
									+ "decrease-from-recommended-baseline",
							"Decrease from baseline",
							"The pharmacogenomic biomarker is related to a recommendation to decrease the dose of the drug from the recommended baseline.");
		} else if (descriptdrifb.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX
									+ "increase-from-recommended-baseline",
							"Increase from baseline",
							"The pharmacogenomic biomarker is related to a recommendation to increase the dose of the drug from the recommended baseline.");
		} else if (descriptdrnc.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX
									+ "not-change-from-recommended-baseline",
							"Not change from baseline",
							"The pharmacogenomic biomarker is related to a recommendation to not change the dose of the drug from the recommended baseline.");
		} else if (descriptdrus.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "use-specific",
							"Use specific",
							"The pharmacogenomic biomarker is related to a recommendation to use specific dose of the drug from the recommended baseline.");
		} else if (descriptdrcs.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "change-schedule",
							"Change schedule",
							"The pharmacogenomic biomarker is related to a recommendation to change schedule of the dose of the drug from the recommended baseline.");
		}
		// else if (descriptdrnone.getValue()) {
		// return ResourcesFactory.createLinkedResource(SPL_POC_PREFIX
		// + "none", "None", "none.");
		// }
		return null;
	}

	// Recommendation Monitoring
	public MLinkedResource getMonitRec() {

		if (descriptmreq.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "required",
							"Required",
							"A required monitoring recommendation is related to the pharmacogenomic biomarker.");
		} else if (descriptmrec.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "recommended",
							"Recommended",
							"A recommended monitoring recommendation is related to the pharmacogenomic biomarker.");
		} else if (descriptmnc.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "not-necessary",
							"Not necessary",
							"A not necessary monitoring recommendation is related to the pharmacogenomic biomarker.");
		} else if (descriptmcms.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + "change-monitoring-strategy",
							"Change monitoring strategy",
							"A strategy changed monitoring recommendation is related to the pharmacogenomic biomarker.");
		}

		// else if (descriptmcnone.getValue()) {
		// return ResourcesFactory.createLinkedResource(SPL_POC_PREFIX
		// + "none", "None", "none.");
		// }
		return null;
	}

	// test section

	public MLinkedResource getTest_Re() {
		if (descripttreq.getValue()) {
			return ResourcesFactory.createLinkedResource(SPL_POC_PREFIX
					+ "required", "Required",
					"A required test is related to the biomarker.");
			// TODO: the label should be "Recomended", fix
		} else if (descripttrec.getValue()) {
			return ResourcesFactory.createLinkedResource(SPL_POC_PREFIX
					+ "recommended", "Recommend",
					"A recommended test is related to the biomarker.");
		}

		else if (descriptttna.getValue()) {
			return ResourcesFactory
					.createLinkedResource(SPL_POC_PREFIX
							+ "take-note-that-tests-are-avaliable",
							"Take note that tests are avaliable",
							"Testing related to the pharmacogenomic biomarker is avaliable.");
		}

		else if (descripttnn.getValue()) {
			return ResourcesFactory
					.createLinkedResource(SPL_POC_PREFIX + "not-necessary",
							"Not necessary",
							"Testing related to the pharmacogenomic biomarker is not necessary.");
		}

		// else if (descripttnone.getValue()) {
		// return ResourcesFactory.createLinkedResource(SPL_POC_PREFIX
		// + "none", "None", "none.");
		// }
		return null;
	}

	// statements
	// the value of each variables is made up
	public Set<MLinkedResource> getStatements() {

		Set<MLinkedResource> statements = new HashSet<MLinkedResource>();

		if (descriptsct.getValue()) {
			statements.add(ResourcesFactory.createLinkedResource(
					DAILYMED_PREFIX + "clinical-trial", "Clinical Trial",
					"Referred to clinical trial"));
		}

		// TODO: this is not in the current pharmgx annotation model, add it
		if (descriptspd.getValue()) {
			statements.add(ResourcesFactory.createLinkedResource(
					DAILYMED_PREFIX + "pro-drug", "Prodrug",
					"Referred to Prodrug"));
		}

		// TODO: this is not in the current pharmgx annotation model, add it
		if (descriptsmcc.getValue()) {
			statements.add(ResourcesFactory.createLinkedResource(
					DAILYMED_PREFIX + "concomitant-medication-concern",
					"Concomitant medication concern",
					"A concomitant medication of concern is mentioned."));
		}

		if (descriptppm.getValue()) {

			statements
					.add(ResourcesFactory
							.createLinkedResource(
									SPL_POC_PREFIX
											+ "population-frequency-mentioned",
									"Variant Frequency",
									"The frequency or proportion at which a variant occurs in a specific population is mentioned."));
		}
		return statements;
	}

	// phenotype
	public MLinkedResource getPhenotype() {

		int indexvt = descriptsvtlb.getSelectedIndex();

		// TODO: test for "none" to skip when encountered
		// TODO: get specific descriptions

		if (indexvt != 0)
			return ResourcesFactory
					.createLinkedResource(
							SPL_POC_PREFIX + descriptsvtlb.getItemText(indexvt),
							descriptsvtlb.getItemText(indexvt),
							"A specific variant of a gene, including the wild-type allele, or a patient phenotype");
		return null;
	}

	// Test results
	public MLinkedResource getTest() {

		int indexts = descriptstslb.getSelectedIndex();

		// TODO: test for "none" to skip when encountered
		// TODO: add the descriptions for the individual test types
		if (indexts != 0)
			return ResourcesFactory.createLinkedResource(SPL_POC_PREFIX
					+ descriptstslb.getItemText(indexts),
					descriptstslb.getItemText(indexts),
					"A test result that is somehow related to the biomarker.");

		return null;
	}

	// NEW annotation
	@SuppressWarnings("deprecation")
	public FSPLsForm(IDomeo domeo, final AFormsManager manager) {
		super(domeo);
		_manager = manager;

		initWidget(binder.createAndBindUi(this));

		_domeo.getLogger().debug(this, "SPL annotation widget bound to UI");

		refreshAnnotationSetFilter(annotationSet, null);

		_domeo.getLogger().debug(this, "SPL annotation filter set refreshed");

		ButtonWithIcon yesButton = new ButtonWithIcon(Domeo.resources
				.generalCss().applyButton());
		yesButton.setWidth("78px");
		yesButton.setHeight("22px");
		yesButton.setResource(Domeo.resources.acceptLittleIcon());
		yesButton.setText("Apply");
		yesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				// validates required fields
				if (!validatesRequiredFields())
					return;

				if (isContentInvalid())
					return; // TODO: use this function to validate form elements
							// in the UI

				_domeo.getLogger().debug(this,
						"SPL annotation content validated");

				try {
					if (_item == null) {
						_domeo.getLogger().debug(this, "_item is null...");
						if (_manager instanceof TextAnnotationFormsPanel) {
							MTextQuoteSelector selector = AnnotationFactory
									.createPrefixSuffixTextSelector(
											_domeo.getAgentManager()
													.getUserPerson(),
											_domeo.getPersistenceManager()
													.getCurrentResource(),
											((TextAnnotationFormsPanel) _manager)
													.getHighlight().getExact(),
											((TextAnnotationFormsPanel) _manager)
													.getHighlight().getPrefix(),
											((TextAnnotationFormsPanel) _manager)
													.getHighlight().getSuffix());

							_domeo.getLogger().debug(this,
									"quote selector initialized");

							// TODO Register coordinate of the selection.
							MSPLsAnnotation annotation = SPLsFactory
									.createSPLsAnnotation(
											((AnnotationPersistenceManager) _domeo
													.getPersistenceManager())
													.getCurrentSet(), _domeo
													.getAgentManager()
													.getUserPerson(), _domeo
													.getAgentManager()
													.getSoftware(), _manager
													.getResource(), selector);

							_domeo.getLogger().debug(this,
									"SPL annotation factory initialized");

							MSPLPharmgxUsage pharmgxUsage = SPLsFactory
									.createSPLPharmgxUsage();

							// take the form values and assign
							_domeo.getLogger().debug(this, "SPL annotation 1");
							pharmgxUsage.setBiomarkers(getBioMarkers());
							_domeo.getLogger().debug(this, "SPL annotation 2");
							pharmgxUsage.setPkImpact(getPkImpact());
							_domeo.getLogger().debug(this, "SPL annotation 3");
							pharmgxUsage.setPdImpact(getPdImpact());
							_domeo.getLogger().debug(this, "SPL annotation 4");
							pharmgxUsage.setDrugRec(getDrugRec());
							_domeo.getLogger().debug(this, "SPL annotation 5");
							pharmgxUsage.setDoseRec(getDoseRec());
							_domeo.getLogger().debug(this, "SPL annotation 6");
							pharmgxUsage.setMonitRec(getMonitRec());
							_domeo.getLogger().debug(this, "SPL annotation 7");
							pharmgxUsage.setStatements(getStatements());
							_domeo.getLogger().debug(this, "SPL annotation 8");
							pharmgxUsage.setTestRec(getTest_Re());
							_domeo.getLogger().debug(this, "SPL annotation 9");
							pharmgxUsage.setAllelesbody(allelesbody.getText());
							_domeo.getLogger().debug(this, "SPL annotation 10");
							pharmgxUsage
									.setProductLabelSelection(getProductLabelSection());
							pharmgxUsage.setMedconditbody(medconditbody
									.getText());
							pharmgxUsage.setPhenotype(getPhenotype());
							pharmgxUsage.setTest(getTest());
							pharmgxUsage.setDrugOfInterest(getDrugOfInterest());
							pharmgxUsage.setHGNCGeneSymbol(getHGNCGeneSymbol());

							// other variant and other test just storing in
							// persistence manager but won't displaying in card
							// and tile
							pharmgxUsage.setOtherPhenotype(descriptsothervt
									.getText());
							pharmgxUsage.setOtherTest(descriptsotherts
									.getText());

							annotation.setPharmgxUsage(pharmgxUsage);

							_domeo.getLogger()
									.debug(this,
											"SPL pharmgxUsage initialized and attached to the SPL annotation instance");

							// _domeo.getLogger().command(getLogCategoryCreate(),
							// this, " with term " + currentPharmgx.getLabel());

							annotation.setComment(commentBody.getText());
							_domeo.getLogger().debug(this, "SPL comment set");

							if (getSelectedSet(annotationSet) == null) {
								_domeo.getLogger()
										.debug(this,
												"empty annotation set, passing first SPL annotation to persistance manager");
								_domeo.getAnnotationPersistenceManager()
										.addAnnotation(annotation, true);
							} else {
								_domeo.getLogger()
										.debug(this,
												"Annotation set is not empty, passing new SPL annotation to persistance manager");
								_domeo.getAnnotationPersistenceManager()
										.addAnnotation(annotation,
												getSelectedSet(annotationSet));
							}

							_domeo.getLogger()
									.debug(this,
											"Making the new SPL annotation visible in the content panel");
							_domeo.getContentPanel()
									.getAnnotationFrameWrapper()
									.performAnnotation(
											annotation,
											((TextAnnotationFormsPanel) _manager)
													.getHighlight());

							_domeo.getLogger()
									.debug(this,
											"SPL annotation data collected. widget should close now.");

							_manager.hideContainer();

						}
					} else {
						// _item.setType(PostitType.findByName(postitTypes.getItemText(postitTypes.getSelectedIndex())));
						// _item.setText(getPostItBody());
						_domeo.getLogger().debug(this, "_item is NOT null...");

						_manager.hideContainer();
					}
				} catch (Exception e) {
					logger.log(Level.WARNING,
							"throwing exception " + e.getMessage());
					_domeo.getLogger().exception(this, e.getMessage());
				}
			}
		});
		buttonsPanel.add(yesButton);

		_domeo.getLogger().debug(this, "'Apply' button added");

		this.setHeight("100px");

		// rightColumn.add(tabs.get(0));
		// resized();
	}

	// ------------------------
	// EDITING OF ANNOTATION
	// ------------------------
	public FSPLsForm(IDomeo domeo, final AFormsManager manager,
			final MSPLsAnnotation annotation) {
		super(domeo);
		_manager = manager;
		_item = annotation;

		initWidget(binder.createAndBindUi(this));

		_domeo.getLogger().debug(this,
				"SPL annotation widget bound to UI (edit)");

		try {
			_domeo.getLogger()
					.debug(this,
							"Attempting to initialize UI with loaded annotation values");

			if (_item.getComment() != null && !_item.getComment().equals(""))
				commentBody.setText(_item.getComment());

			// drug of interest

			if (_item.getDrugOfInterest() != null) {
				if (_item.getDrugOfInterest().getLabel().equals("")) {
					descriptdoi.setSelectedIndex(0);
				}

				String drugOfInterest = _item.getDrugOfInterest().getLabel();
				for (int i = 0; i < drugs.length; i++) {
					if (drugs[i].equals(drugOfInterest)) {
						descriptdoi.setSelectedIndex(i + 1);
					}
				}
			}

			// HGNCGeneSymbol
			if (_item.getHGNCGeneSymbol() != null) {
				if (_item.getHGNCGeneSymbol().getLabel().equals("")) {
					descripthgs.setSelectedIndex(0);
				}

				String HGNCGeneSymbol = _item.getHGNCGeneSymbol().getLabel();
				for (int i = 0; i < HGNCGeneSymbols.length; i++) {
					if (HGNCGeneSymbols[i].equals(HGNCGeneSymbol)) {
						descripthgs.setSelectedIndex(i + 1);
					}
				}
			}

			// set biomarkers
			if (_item.getBiomarkers() != null) {

				if (_item.getBiomarkers().getLabel().equals("")) {
					descriptbm.setSelectedIndex(0);
				}

				String bioLabel = _item.getBiomarkers().getLabel();
				for (int i = 0; i < pharmgxmodel.getLengthOfBioList(); i++) {
					if (pharmgxmodel.getBioName(i).equals(bioLabel)) {
						descriptbm.setSelectedIndex(i + 1);
					}
				}
			}

			// set product label selection
			if (_item.getProductLabelSelection() != null) {

				if (_item.getProductLabelSelection().getLabel()
						.equals("unselected")) {
					descriptpls.setSelectedIndex(0);
				}

				String plsLabel = _item.getProductLabelSelection().getLabel();
				for (int i = 0; i < productLabelSections.length; i++) {
					if (productLabelSections[i].equals(plsLabel)) {
						descriptpls.setSelectedIndex(i + 1);
					}
				}
			}

			if (_item.getPKImpact() != null) {

				descriptpknone.setValue(false);
				if (_item.getPKImpact().getLabel()
						.equals("Metabolism Decrease"))
					descriptpkdm.setValue(true);
				else if (_item.getPKImpact().getLabel()
						.equals("Metabolism Increase"))
					descriptpkim.setValue(true);
				else if (_item.getPKImpact().getLabel()
						.equals("Absorption Increase"))
					descriptpkia.setValue(true);
				else if (_item.getPKImpact().getLabel()
						.equals("Absorption Decrease"))
					descriptpkda.setValue(true);
				else if (_item.getPKImpact().getLabel()
						.equals("Distribution Increase"))
					descriptpkid.setValue(true);
				else if (_item.getPKImpact().getLabel()
						.equals("Distribution Decrease"))
					descriptpkdd.setValue(true);
				else if (_item.getPKImpact().getLabel()
						.equals("Excretion Increase"))
					descriptpkie.setValue(true);
				else if (_item.getPKImpact().getLabel()
						.equals("Excretion Decrease"))
					descriptpkde.setValue(true);
				else if (_item.getPKImpact().getLabel().equals("Not Important"))
					descriptpkni.setValue(true);
				else if (_item.getPKImpact().getLabel().equals("None"))
					descriptpknone.setValue(true);
			}

			if (_item.getPdImpact() != null) {

				descriptpdnone.setValue(false);
				if (_item.getPdImpact().getLabel()
						.equals("Decreased Toxicity Risk"))
					descriptpddt.setValue(true);
				else if (_item.getPdImpact().getLabel()
						.equals("Increased Toxicity Risk"))
					descriptpdit.setValue(true);
				else if (_item.getPdImpact().getLabel()
						.equals("Influences Drug Response"))
					descriptpdir.setValue(true);
				else if (_item.getPdImpact().getLabel().equals("Not Important"))
					descriptpdni.setValue(true);
				else if (_item.getPdImpact().getLabel()
						.equals("Increased Efficacy"))
					descriptpdie.setValue(true);
				else if (_item.getPdImpact().getLabel()
						.equals("Decreased Efficacy"))
					descriptpdde.setValue(true);
				else if (_item.getPdImpact().getLabel().equals("None"))
					descriptpdnone.setValue(true);
			}

			if (_item.getDoseRec() != null) {

				descriptdrnone.setValue(false);
				if (_item.getDoseRec().getLabel()
						.equals("Decrease from baseline"))
					descriptdrdfb.setValue(true);
				else if (_item.getDoseRec().getLabel()
						.equals("Increase from baseline"))
					descriptdrifb.setValue(true);
				else if (_item.getDoseRec().getLabel()
						.equals("Not change from baseline"))
					descriptdrnc.setValue(true);
				else if (_item.getDoseRec().getLabel().equals("Use specific"))
					descriptdrus.setValue(true);
				else if (_item.getDoseRec().getLabel()
						.equals("Change schedule"))
					descriptdrcs.setValue(true);
				else if (_item.getDoseRec().getLabel().equals("None"))
					descriptdrnone.setValue(true);
			}

			if (_item.getMonitRec() != null) {

				descriptmcnone.setValue(false);
				if (_item.getMonitRec().getLabel().equals("Required"))
					descriptmreq.setValue(true);
				else if (_item.getMonitRec().getLabel().equals("Recommended"))
					descriptmrec.setValue(true);
				else if (_item.getMonitRec().getLabel().equals("Not necessary"))
					descriptmnc.setValue(true);
				else if (_item.getMonitRec().getLabel()
						.equals("Change monitoring strategy"))
					descriptmcms.setValue(true);
				else if (_item.getMonitRec().getLabel().equals("None"))
					descriptmcnone.setValue(true);
			}

			if (_item.getDrugRec() != null) {

				descriptdsnone.setValue(false);
				if (_item.getDrugRec().getLabel()
						.equals("Alternative Recommended"))
					descriptdsal.setValue(true);
				else if (_item.getDrugRec().getLabel()
						.equals("Change in route of administration"))
					descriptdsca.setValue(true);
				else if (_item.getDrugRec().getLabel().equals("Add medication"))
					descriptdsam.setValue(true);
				else if (_item.getDrugRec().getLabel().equals("Do not start"))
					descriptdsns.setValue(true);
				else if (_item.getDrugRec().getLabel()
						.equals("Discontinue/Do not restart"))
					descriptdsnr.setValue(true);
				else if (_item.getDrugRec().getLabel()
						.equals("Not change necessary"))
					descriptdsnc.setValue(true);
				else if (_item.getDrugRec().getLabel().equals("None"))
					descriptdsnone.setValue(true);
			}

			if (_item.getTestRec() != null) {

				descripttnone.setValue(false);
				if (_item.getTestRec().getLabel().equals("Required"))
					descripttreq.setValue(true);
				else if (_item.getTestRec().getLabel().equals("Recommend"))
					descripttrec.setValue(true);
				else if (_item.getTestRec().getLabel()
						.equals("Take note that tests are avaliable"))
					descriptttna.setValue(true);
				else if (_item.getTestRec().getLabel().equals("Not necessary"))
					descripttnn.setValue(true);
				else if (_item.getTestRec().getLabel().equals("None"))
					descripttnone.setValue(true);
			}

			// statements, values are made up
			// checkbox

			if (_item.getStatements() != null) {
				for (MLinkedResource statemt : _item.getStatements()) {
					if (statemt.getLabel().equals("Clinical Trial")) {
						descriptsct.setValue(true);
					}

					if (statemt.getLabel().equals("Prodrug")) {
						descriptspd.setValue(true);
					}
					if (statemt.getLabel().equals(
							"Concomitant medication concern")) {
						descriptsmcc.setValue(true);
					}

					if (statemt.getLabel().equals("Variant Frequency")) {
						descriptppm.setValue(true);
					}

				}
			}

			// Phenotype

			if (_item.getPhenotype() != null) {

				if (_item.getPhenotype().getLabel().equals("not mentioned")) {
					descriptsvtlb.setSelectedIndex(0);
				}

				String variantLabel = _item.getPhenotype().getLabel();
				for (int i = 0; i < variant.length; i++) {
					if (variant[i].equals(variantLabel)) {
						descriptsvtlb.setSelectedIndex(i + 1);
					}
				}
			}

			// Test results

			if (_item.getTest() != null) {

				if (_item.getTest().getLabel().equals("not mentioned")) {
					descriptstslb.setSelectedIndex(0);
				}

				String testLabel = _item.getTest().getLabel();
				for (int i = 0; i < test.length; i++) {
					if (test[i].equals(testLabel)) {
						descriptstslb.setSelectedIndex(i + 1);
					}
				}
			}

			// alleles body

			if (_item.getAllelesbody() != null) {
				allelesbody.setText(_item.getAllelesbody());
			}

			// medical condition
			if (_item.getMedconditbody() != null) {
				medconditbody.setText(_item.getMedconditbody());
			}

			// variant textarea
			if (_item.getOtherPhenotype() != null) {
				descriptsothervt.setText(_item.getOtherPhenotype());
			}

			// test textarea
			if (_item.getOtherTest() != null) {
				descriptsotherts.setText(_item.getOtherTest());
			}

		} catch (Exception e) {
			_domeo.getLogger().exception(
					this,
					"Failed to display current annotation "
							+ annotation.getLocalId());
			displayDialog(
					"Failed to properly display existing annotation "
							+ e.getMessage(), true);
		}

		try {
			refreshAnnotationSetFilter(annotationSet, annotation);

			_domeo.getLogger().debug(this,
					"SPL annotation filter set refreshed");

			currentPharmgx = annotation.getPharmgxUsage().getPharmgx();

			_domeo.getLogger()
					.debug(this,
							"acquired the pharmgx usage from the annotation instance passed to this method");
		} catch (Exception e) {
			_domeo.getLogger().exception(
					AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION,
					this,
					"Failed to display current annotation "
							+ annotation.getLocalId());
			displayDialog(
					"Failed to properly display existing annotation "
							+ e.getMessage(), true);
		}

		ButtonWithIcon sameVersionButton = new ButtonWithIcon();
		sameVersionButton.setStyleName(Domeo.resources.generalCss()
				.applyButton());
		sameVersionButton.setWidth("78px");
		sameVersionButton.setHeight("22px");
		sameVersionButton.setResource(Domeo.resources.acceptLittleIcon());
		sameVersionButton.setText("Apply");
		sameVersionButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// validates required fields
				if (!validatesRequiredFields())
					return;

				try {
					if (isContentInvalid()) {
						return; // TODO: modify this function to validate our UI

					}

					MTextQuoteSelector selector = AnnotationFactory
							.createPrefixSuffixTextSelector(_domeo
									.getAgentManager().getUserPerson(), _domeo
									.getPersistenceManager()
									.getCurrentResource(),
									((TextAnnotationFormsPanel) _manager)
											.getHighlight().getExact(),
									((TextAnnotationFormsPanel) _manager)
											.getHighlight().getPrefix(),
									((TextAnnotationFormsPanel) _manager)
											.getHighlight().getSuffix());

					_item.setSelector(selector);

					_domeo.getLogger().debug(this,
							"SPL annotation content validated (edit)");

					_item.setComment(commentBody.getText());

					_item.setDrugOfInterest(getDrugOfInterest());
					_item.setHGNCGeneSymbol(getHGNCGeneSymbol());
					_item.setBiomarkers(getBioMarkers());
					_item.setProductLabelSelection(getProductLabelSection());
					_item.setPKImpact(getPkImpact());
					_item.setPdImpact(getPdImpact());
					_item.setDrugRec(getDrugRec());
					_item.setDoseRec(getDoseRec());
					_item.setMonitRec(getMonitRec());
					_item.setStatements(getStatements());
					_item.setTestRec(getTest_Re());
					_item.setPhenotype(getPhenotype());
					_item.setTest(getTest());
					_item.setOtherPhenotype(descriptsothervt.getText());
					_item.setOtherTest(descriptsotherts.getText());
					_item.setAllelesbody(allelesbody.getText());
					_item.setMedconditbody(medconditbody.getText());

					_domeo.getLogger().debug(this,
							"SPL descriptions cleared and re-loaded (edit)");

					_item.getPharmgxUsage().setPharmgx(currentPharmgx);

					_domeo.getContentPanel()
							.getAnnotationFrameWrapper()
							.updateAnnotation(_item,
									getSelectedSet(annotationSet));

					_domeo.getLogger()
							.debug(this,
									"SPL annotation data collected. widget should close now.");
					_manager.hideContainer();
				} catch (Exception e) {
					_domeo.getLogger()
							.exception(
									AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION,
									this,
									"Failed to apply modified anntoation "
											+ annotation.getLocalId());
					displayDialog(
							"Failed to apply modified annotation "
									+ e.getMessage(), true);
				}
			}
		});
		buttonsPanel.add(sameVersionButton);

		this.setHeight("100px");

		// rightColumn.add(tabs.get(0));
		// resized();
	}

	public boolean validatesRequiredFields() {
		ArrayList<String> requireds = new ArrayList<String>();

		if (descriptdoi.getSelectedIndex() == 0)
			requireds.add("drug of interest");
		if (descripthgs.getSelectedIndex() == 0)
			requireds.add("HGNCGeneSymbol");
		if (descriptpls.getSelectedIndex() == 0)
			requireds.add("Product label sections");

		if (requireds.size() > 0) {

			String message = "";
			for (String str : requireds) {
				message += str + " ,";
			}
			message = message.substring(0, message.length() - 1);

			Window.alert(message + "are required");
			return false;
		}
		return true;
	}

	public String getTitle() {
		return LABEL;
	}

	@Override
	public String getLogCategoryCreate() {
		return LOG_CATEGORY_QUALIFIER_CREATE;
	}

	@Override
	public String getLogCategoryEdit() {
		return LOG_CATEGORY_QUALIFIER_EDIT;
	}

	// TODO: edit this method to validate the relevant form elements
	@Override
	public boolean isContentInvalid() {
		// _manager.displayMessage("Click received!");
		// if(currentPharmgx==null) {
		// _manager.displayMessage("The body of the annotation cannot be empty!");
		// Timer timer = new Timer() {
		// public void run() {
		// _manager.clearMessage();
		// }
		// };
		// timer.schedule(2000);
		// return true;
		// }
		return false;
	}

	public boolean areMethodsChanged() {
		// if(_item.getSioDescriptions().size()!=getMethods().size()) return
		// true;
		// TODO check items
		return false;
	}

	@Override
	public void resized() {
		/*
		 * this.setWidth((Window.getClientWidth() - 340) + "px");
		 * tabBar.setWidth((Window.getClientWidth() - 615) + "px"); for (Widget
		 * tab : tabs) { // if(tab instanceof IResizable)
		 * ((IResizable)tab).resized(); tab.setWidth((Window.getClientWidth() -
		 * 615) + "px"); }
		 */
	}

	@Override
	public boolean isContentChanged(MAnnotation annotation) {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	// public void addPharmgx(MPharmgx pharmgx) {
	// addAssociatedAntibody(pharmgx);
	// }
}
