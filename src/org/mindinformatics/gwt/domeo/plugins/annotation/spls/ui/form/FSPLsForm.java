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
public class FSPLsForm extends AFormComponent implements IResizable {

	private static Logger logger = Logger.getLogger("");

	public static final String LABEL = "pharmacogenomics";
	public static final String LABEL_EDIT = "EDIT SPL ANNOTATION";

	public static final String LOG_CATEGORY_QUALIFIER_CREATE = "CREATING SPL ANNOTATION";
	public static final String LOG_CATEGORY_QUALIFIER_EDIT = "EDITING SPL ANNOTATION";

	public static final String SPL_POC_PREFIX = "http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#";
	public static final String DAILYMED_PREFIX = "http://dbmi-icode-01.dbmi.pitt.edu/linkedSPLs/vocab/resource/";

	interface Binder extends UiBinder<VerticalPanel, FSPLsForm> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private MSPLsAnnotation _item;
	private MPharmgx currentPharmgx;

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
	ListBox descriptbm, descriptsvtlb, descriptstslb, descriptdoi, descriptpls;

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
			descriptdsnc, descriptdsnone;

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
	CheckBox descriptsai, descriptsmcc;

	@UiField
	CheckBox descriptppm;

	// alleles
	@UiField
	TextArea commentBody, allelesbody, medconditbody, descriptsothervt,
			descriptsotherts;

	// String[] biomarker = { "ApoE2", "BRAF", "C-Kit", "CCR5", "CD20_antigen",
	// "CD25", "CD30", "CYP1A2", "CYP2C19", "CYP2C9", "CYP2D6", "DPD",
	// "EGFR", "ER and PgR_receptor", "ER_receptor", "FIP1L1-PDGFRα",
	// "G6PD", "HLA-B*1502", "HLA-B*5701", "Her2/neu", "IL28B", "KRAS",
	// "LDL_Receptor", "NAT1;_NAT2", "PDGFR", "PML/RARα", "Rh_genotype",
	// "TPMT", "UGT1A1", "VKORC1" };

	List<Biomarker> biomarkerUris = new ArrayList<Biomarker>() {
		{
			add(new Biomarker("ApoE2",
					"http://purl.obolibrary.org/obo/PR_000004155"));
			add(new Biomarker("BRAF",
					"http://purl.obolibrary.org/obo/PR_000004801"));
			add(new Biomarker("C-Kit",
					"http://purl.obolibrary.org/obo/PR_000009345"));
			add(new Biomarker("CCR5",
					"http://purl.obolibrary.org/obo/PR_000001201"));
			add(new Biomarker("CD20_antigen",
					"http://purl.obolibrary.org/obo/PR_000001289"));
			add(new Biomarker("CD25",
					"http://purl.obolibrary.org/obo/PR_000001380"));
			add(new Biomarker("CD30",
					"http://purl.obolibrary.org/obo/PR_000001380"));
			add(new Biomarker("CYP1A2",
					"http://purl.obolibrary.org/obo/PR_000006102"));
			add(new Biomarker("CYP2C19",
					"http://purl.obolibrary.org/obo/PR_000006102"));
			add(new Biomarker("CYP2C9",
					"http://purl.obolibrary.org/obo/PR_000006120"));
			add(new Biomarker("CYP2D6",
					"http://purl.obolibrary.org/obo/PR_000006121"));
			add(new Biomarker("DPD",
					"http://purl.obolibrary.org/obo/PR_000006678"));
			add(new Biomarker("EGFR",
					"http://purl.obolibrary.org/obo/PR_000006933"));
			add(new Biomarker(
					"ER and PgR_receptor",
					"http://purl.obolibrary.org/obo/PR_000012621http://purl.obolibrary.org/obo/PR_000007204http://purl.obolibrary.org/obo/PR_000007205"));
			add(new Biomarker(
					"ER_receptor",
					"http://purl.obolibrary.org/obo/PR_000007204http://purl.obolibrary.org/obo/PR_000007205"));
			// add(new Biomarker("FIP1L1-PDGFRα", ""));
			add(new Biomarker("G6PD",
					"http://purl.obolibrary.org/obo/PR_000007749"));
			add(new Biomarker("HLA-B*1502",
					"http://purl.obolibrary.org/obo/PR_000002010"));
			add(new Biomarker("HLA-B*5701",
					"http://purl.obolibrary.org/obo/PR_000002010"));
			add(new Biomarker("Her2/neu",
					"http://purl.obolibrary.org/obo/PR_000002082"));
			add(new Biomarker("IL28B",
					"http://purl.obolibrary.org/obo/PR_000001470"));
			add(new Biomarker("KRAS",
					"http://purl.obolibrary.org/obo/PR_000009442"));
			add(new Biomarker("LDL_Receptor",
					"http://purl.obolibrary.org/obo/PR_000009744"));
			add(new Biomarker(
					"NAT1;_NAT2",
					"http://purl.obolibrary.org/obo/PR_000010994http://purl.obolibrary.org/obo/PR_000011001"));
			add(new Biomarker("PDGFR",
					"http://purl.obolibrary.org/obo/PR_000012492"));
			// add(new Biomarker("PML/RARα", ""));
			add(new Biomarker("Rh_genotype",
					"http://purl.obolibrary.org/obo/PR_000001442"));
			add(new Biomarker("TPMT",
					"http://purl.obolibrary.org/obo/PR_000016583"));
			add(new Biomarker("UGT1A1",
					"http://purl.obolibrary.org/obo/PR_000017048"));
			add(new Biomarker("VKORC1",
					"http://purl.obolibrary.org/obo/PR_000017303"));
		}
	};

	class Biomarker {

		Biomarker() {

		}

		Biomarker(String name, String uri) {
			this.name = name;
			this.uri = uri;
		}

		private String name;
		private String uri;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

	}

	private static HashMap<String, String> drugUris_FDA = new HashMap<String, String>() {
		{
			put("PROPAFENONE", "http://www.drugbank.ca/drugs/DB01182");
			put("TRETINOIN", "http://www.drugbank.ca/drugs/DB00755");
			put("EXEMESTANE", "http://www.drugbank.ca/drugs/DB00990");
			put("FLUVOXAMINE", "http://www.drugbank.ca/drugs/DB00176");
			put("IVACAFTOR", "http://www.drugbank.ca/drugs/DB08820");
			put("TETRABENAZINE", "http://www.drugbank.ca/drugs/DB04844");
			put("NORTRIPTYLINE", "http://www.drugbank.ca/drugs/DB00540");
			put("NEFAZODONE", "http://www.drugbank.ca/drugs/DB01149");
			put("TERBINAFINE", "http://www.drugbank.ca/drugs/DB00857");

			put("ARIPIPRAZOLE", "http://www.drugbank.ca/drugs/DB01238");
			put("TELAPREVIR", "http://www.drugbank.ca/drugs/DB05521");

			put("AZATHIOPRINE", "http://www.drugbank.ca/drugs/DB00993");

			put("ATORVASTATIN", "http://www.drugbank.ca/drugs/DB01076");
			put("DAPSONE", "http://www.drugbank.ca/drugs/DB00250");
			put("PHENYTOIN", "http://www.drugbank.ca/drugs/DB00252");
			put("CISPLATIN", "http://www.drugbank.ca/drugs/DB00515");
			put("GALANTAMINE", "http://www.drugbank.ca/drugs/DB00674");

			put("LENALIDOMIDE", "http://www.drugbank.ca/drugs/DB00480");
			put("RIFAMPIN", "http://www.drugbank.ca/drugs/DB01045");

			put("CLOMIPRAMINE", "http://www.drugbank.ca/drugs/DB01242");
			put("DESIPRAMINE", "http://www.drugbank.ca/drugs/DB01151");
			put("CRIZOTINIB", "http://www.drugbank.ca/drugs/DB08700");
			put("METOPROLOL", "http://www.drugbank.ca/drugs/DB00264");
			put("PROPRANOLOL", "http://www.drugbank.ca/drugs/DB00571");
			put("PIMOZIDE", "http://www.drugbank.ca/drugs/DB01100");
			put("CODEINE", "http://www.drugbank.ca/drugs/DB00318");

			put("FLURBIPROFEN", "http://www.drugbank.ca/drugs/DB00712");

			put("THIORIDAZINE", "http://www.drugbank.ca/drugs/DB00679");

			put("PERPHENAZINE", "http://www.drugbank.ca/drugs/DB00850");
			put("TAMOXIFEN", "http://www.drugbank.ca/drugs/DB00675");

			put("VORICONAZOLE", "http://www.drugbank.ca/drugs/DB00582");

			put("PANTOPRAZOLE", "http://www.drugbank.ca/drugs/DB00213");
			put("VENLAFAXINE", "http://www.drugbank.ca/drugs/DB00285");
			put("RABEPRAZOLE", "http://www.drugbank.ca/drugs/DB01129");
			put("NILOTINIB", "http://www.drugbank.ca/drugs/DB04868");
			put("FLUOXETINE", "http://www.drugbank.ca/drugs/DB00472");
			put("DASATINIB", "http://www.drugbank.ca/drugs/DB01254");
			put("CETUXIMAB", "http://www.drugbank.ca/drugs/DB00002");
			put("RISPERIDONE", "http://www.drugbank.ca/drugs/DB00734");
			put("CLOMIPHENE", "http://www.drugbank.ca/drugs/DB00882");
			put("TOSITUMOMAB", "http://www.drugbank.ca/drugs/DB00081");
			put("MERCAPTOPURINE", "http://www.drugbank.ca/drugs/DB01033");
			put("CLOBAZAM", "http://www.drugbank.ca/drugs/DB00349");
			put("CLOZAPINE", "http://www.drugbank.ca/drugs/DB00363");
			put("CARBAMAZEPINE", "http://www.drugbank.ca/drugs/DB00564");
			put("PANITUMUMAB", "http://www.drugbank.ca/drugs/DB01269");
			put("TICAGRELOR", "http://www.drugbank.ca/drugs/DB08816");
			put("QUINIDINE", "http://www.drugbank.ca/drugs/DB00908");
			put("WARFARIN", "http://www.drugbank.ca/drugs/DB00682");
			put("IRINOTECAN", "http://www.drugbank.ca/drugs/DB00762");
			put("FULVESTRANT", "http://www.drugbank.ca/drugs/DB00947");
			put("ISONIAZID", "http://www.drugbank.ca/drugs/DB00951");
			put("RASBURICASE", "http://www.drugbank.ca/drugs/DB00049");
			put("BUSULFAN", "http://www.drugbank.ca/drugs/DB01008");
			put("DIAZEPAM", "http://www.drugbank.ca/drugs/DB00829");
			put("INDACATEROL", "http://www.drugbank.ca/drugs/DB05039");
			put("CLOPIDOGREL", "http://www.drugbank.ca/drugs/DB00758");
			put("MARAVIROC", "http://www.drugbank.ca/drugs/DB04835");
			put("ERLOTINIB", "http://www.drugbank.ca/drugs/DB00530");
			put("PAROXETINE", "http://www.drugbank.ca/drugs/DB00715");

			put("FLUOROURACIL", "http://www.drugbank.ca/drugs/DB00544");
			put("PRAVASTATIN", "http://www.drugbank.ca/drugs/DB00175");
			put("CEVIMELINE", "http://www.drugbank.ca/drugs/DB00185");
			put("PROTRIPTYLINE", "http://www.drugbank.ca/drugs/DB00344");
			put("PRASUGREL", "http://www.drugbank.ca/drugs/DB06209");

			put("TRIMIPRAMINE", "http://www.drugbank.ca/drugs/DB00726");

			put("CARISOPRODOL", "http://www.drugbank.ca/drugs/DB00395");
			put("EVEROLIMUS", "http://www.drugbank.ca/drugs/DB01590");
			put("ABACAVIR", "http://www.drugbank.ca/drugs/DB01048");
			put("THIOGUANINE", "http://www.drugbank.ca/drugs/DB00352");
			put("CELECOXIB", "http://www.drugbank.ca/drugs/DB00482");
			put("OMEPRAZOLE", "http://www.drugbank.ca/drugs/DB00338");
			put("TOLTERODINE", "http://www.drugbank.ca/drugs/DB01036");

			put("CAPECITABINE", "http://www.drugbank.ca/drugs/DB01101");
			put("CITALOPRAM", "http://www.drugbank.ca/drugs/DB00215");
			put("LAPATINIB", "http://www.drugbank.ca/drugs/DB01259");
			put("ATOMOXETINE", "http://www.drugbank.ca/drugs/DB00289");

			put("PYRAZINAMIDE", "http://www.drugbank.ca/drugs/DB00339");
			put("DOXEPIN", "http://www.drugbank.ca/drugs/DB01142");
			put("IMATINIB", "http://www.drugbank.ca/drugs/DB00619");
			put("CARVEDILOL", "http://www.drugbank.ca/drugs/DB01136");
			put("CHLOROQUINE", "http://www.drugbank.ca/drugs/DB00608");
			put("LETROZOLE", "http://www.drugbank.ca/drugs/DB01006");
			put("IMIPRAMINE", "http://www.drugbank.ca/drugs/DB00458");

			put("ESOMEPRAZOLE", "http://www.drugbank.ca/drugs/DB00736");
			put("MODAFINIL", "http://www.drugbank.ca/drugs/DB00745");
			put("TRASTUZUMAB", "http://www.drugbank.ca/drugs/DB00072");
			put("ARSENIC_TRIOXIDE", "http://www.drugbank.ca/drugs/DB01169");
			put("BRENTUXIMAB_VEDOTIN", "http://www.drugbank.ca/drugs/DB08870");

			put("CARISOPRODOL", "http://www.drugbank.ca/drugs/DB00395");
			put("DENILEUKIN_DIFTITOX", "http://www.drugbank.ca/drugs/DB00004");
			put("ILOPERIDONE", "http://www.drugbank.ca/drugs/DB04946");
			put("PEGINTERFERON_ALFA-2B", "http://www.drugbank.ca/drugs/DB00022");
			put("PERTUZUMAB", "http://www.drugbank.ca/drugs/DB06366");
			put("VALPROIC_ACID", "http://www.drugbank.ca/drugs/DB00313");
			put("VEMURAFENIB", "http://www.drugbank.ca/drugs/DB08881");
			put("CHLORDIAZEPOXIDE_AND_AMITRIPTYLINE",
					"http://www.drugbank.ca/drugs/DB00475http://www.drugbank.ca/drugs/DB00321");

			put("DEXTROMETHORPHAN_AND_QUINIDINE",
					"http://www.drugbank.ca/drugs/DB00514http://www.drugbank.ca/drugs/DB00908");

			put("DROSPIRENONE_AND_ESTRADIOL",
					"http://www.drugbank.ca/drugs/DB01395http://www.drugbank.ca/drugs/DB00783");

			put("FLUOXETINE_AND_OLANZAPINE",
					"http://www.drugbank.ca/drugs/DB00472http://www.drugbank.ca/drugs/DB00334");

			put("TRAMADOL_AND_ACETAMINOPHEN",
					"http://www.drugbank.ca/drugs/DB00193http://www.drugbank.ca/drugs/DB00316");
		}
	};

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
	String[] drugs = { "Abacavir", "Aripiprazole", "Arsenic_Trioxide",
			"Atomoxetine", "Atorvastatin", "Azathioprine", "Boceprevir",
			"Brentuximab_Vedotin", "Busulfan", "Capecitabine", "Carbamazepine",
			"Carisoprodol", "Carvedilol", "Celecoxib", "Cetuximab",
			"Cevimeline", "Chlordiazepoxide_and_Amitriptyline", "Chloroquine",
			"Cisplatin", "Citalopram", "Clobazam", "Clomiphene",
			"Clomipramine", "Clopidogrel", "Clozapine", "Codeine",
			"Crizotinib", "Dapsone", "Dasatinib", "Denileukin_Diftitox",
			"Desipramine", "Dexlansoprazole", "Dextromethorphan_and_Quinidine",
			"Diazepam", "Doxepin", "Drospirenone_and_Estradiol", "Erlotinib",
			"Esomeprazole", "Exemestane", "Everolimus", "Fluorouracil",
			"Fluoxetine", "Fluoxetine_and_Olanzapine", "Flurbiprofen",
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
			"Tolterodine", "Tositumomab", "Tramadol_and_Acetaminophen",
			"Trastuzumab", "Tretinoin", "Trimipramine", "Valproic_Acid",
			"Vemurafenib", "Venlafaxine", "Voriconazole", "Warfarin" };

	String[] productLabelSections = {
			"ABUSE (34086-9)",
			"ADVERSE REACTIONS (34084-4)",
			"BOXED WARNING (34066-1)",
			"CARCINOGENESIS AND MUTAGENESIS AND IMPAIRMENT OF FERTILITY (34083-6)",
			"CLINICAL PHARMACOLOGY (34090-1)", "CLINICAL STUDIES (34092-7)",
			"CONTRAINDICATIONS (34070-3)", "DEPENDENCE (34087-7)",
			"DESCRIPTION (34089-3)", "DOSAGE AND ADMINISTRATION (34068-7)",
			"DOSAGE FORMS AND STRENGTHS (43678-2)",
			"DRUG AND OR LABORATORY TEST INTERACTIONS (34074-5)",
			"DRUG ABUSE AND DEPENDENCE (42227-9)",
			"DRUG INTERACTIONS (34073-7)", "GENERAL PRECAUTIONS (34072-9)",
			"GERIATRIC USE (34082-8)", "HOW SUPPLIED (34069-5)",
			"INDICATIONS AND USAGE (34067-9)",
			"INFORMATION FOR PATIENTS (34076-0)", "LABORATORY TESTS (34075-2)",
			"MECHANISM OF ACTION (43679-0)", "MICROBIOLOGY (49489-8)",
			"NONCLINICAL TOXICOLOGY (43680-8)",
			"NONTERATOGENIC EFFECTS (34078-6)", "NURSING MOTHERS (34080-2)",
			"OTHER SAFETY INFORMATION (60561-8)", "OVERDOSAGE (34088-5)",
			"PATIENT MEDICATION INFORMATION (68498-5)",
			"PEDIATRIC USE (34081-0)", "PHARMACODYNAMICS (43681-6)",
			"PHARMACOGENOMICS (66106-6)", "PHARMACOKINETICS (43682-4)",
			"PRECAUTIONS (42232-9)", "PREGNANCY (42228-7)",
			"ROUTE,METHOD AND FREQUENCY OF ADMINISTRATION (60562-6)",
			"SUMMARY OF SAFETY AND EFFECTIVENESS (60563-4)",
			"TERATOGENIC EFFECTS (34077-8)",
			"USE IN SPECIFIC POPULATIONS (43684-0)",
			"USER SAFETY WARNINGS (54433-8)",
			"WARNINGS AND PRECAUTIONS (43685-7)", "WARNINGS (34071-1)",
			"SUPPLEMENTAL PATIENT MATERIAL (38056-8)" };

	// drug of interest
	public MLinkedResource getDrugOfInterest() {

		int indexdoi = descriptdoi.getSelectedIndex();

		if (indexdoi != 0) {
			String drugname = descriptdoi.getItemText(indexdoi).toUpperCase();

			String druguri = drugUris_FDA.get(drugname);

			if (druguri == null || druguri.trim().equals("")) {
				System.out.println("WARNING: DRUG URI IS NOT FOUND IN MAP");
				druguri = "";
			}

			return ResourcesFactory.createLinkedResource(druguri,
					descriptdoi.getItemText(indexdoi), "The drug of interest.");
		} else
			return null;
	}

	// biomarkers
	public MLinkedResource getBioMarkers() {

		int indexbm = descriptbm.getSelectedIndex();

		if (indexbm != 0) {
			String biomarkerName = descriptbm.getItemText(indexbm);
			String biomarkerURL = biomarkerUris.get(indexbm - 1).getUri();

			return ResourcesFactory.createLinkedResource(biomarkerURL,
					biomarkerName, "The selected biomarker.");
		} else
			return null;
	}

	// Product label sections
	public MLinkedResource getProductLabelSection() {

		int indexbm = descriptpls.getSelectedIndex();

		// TODO: fix the biomarker URI listing to be accurate
		return ResourcesFactory
				.createLinkedResource(
						SPL_POC_PREFIX,
						descriptpls.getItemText(indexbm),
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
		// else if (descriptpknone.getValue()) {
		// return ResourcesFactory.createLinkedResource(SPL_POC_PREFIX
		// + "none", "None", "none");
		// }

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

		// else if (descriptpdnone.getValue()) {
		// return ResourcesFactory.createLinkedResource(SPL_POC_PREFIX
		// + "none", "None", "none.");
		// }
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
							SPL_POC_PREFIX + "do-not-restart",
							"Do not restart",
							"The pharmacogenomic biomarker is related to a recommendation to not restart the drug");
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

		// TODO: this is not in the current pharmgx annotation model, add it
		if (descriptsai.getValue()) {
			statements.add(ResourcesFactory.createLinkedResource(
					DAILYMED_PREFIX + "ingredient-active", "Active ingredient",
					"the ingredient is active"));
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

	// Variant
	public MLinkedResource getVariant() {

		int indexbm = descriptsvtlb.getSelectedIndex();

		// TODO: test for "none" to skip when encountered
		// TODO: get specific descriptions
		return ResourcesFactory
				.createLinkedResource(
						// SPL_POC_PREFIX + descriptsvtlb.getItemText(indexbm),
						// url triggers url validates error
						SPL_POC_PREFIX,
						descriptsvtlb.getItemText(indexbm),
						"A specific variant of a gene, including the wild-type allele, or a patient phenotype");
	}

	// Test
	public MLinkedResource getTest() {

		int indexbm = descriptstslb.getSelectedIndex();

		// TODO: test for "none" to skip when encountered
		// TODO: add the descriptions for the individual test types
		return ResourcesFactory.createLinkedResource(SPL_POC_PREFIX
				+ descriptstslb.getItemText(indexbm),
				descriptstslb.getItemText(indexbm),
				"A test result that is somehow related to the biomarker.");
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
							pharmgxUsage.setVariant(getVariant());
							pharmgxUsage.setTest(getTest());
							pharmgxUsage.setDrugOfInterest(getDrugOfInterest());

							// other variant and other test just storing in
							// persistence manager but won't displaying in card
							// and tile
							pharmgxUsage.setOtherVariant(descriptsothervt
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
				if (_item.getDrugOfInterest().getLabel().equals("unselected")) {
					descriptdoi.setSelectedIndex(0);
				}

				String drugOfInterest = _item.getDrugOfInterest().getLabel();
				for (int i = 0; i < drugs.length; i++) {
					if (drugs[i].equals(drugOfInterest)) {
						descriptdoi.setSelectedIndex(i + 1);
					}
				}
			}

			// set biomarkers
			if (_item.getBiomarkers() != null) {

				if (_item.getBiomarkers().getLabel().equals("unselected")) {
					descriptbm.setSelectedIndex(0);
				}

				String bioLabel = _item.getBiomarkers().getLabel();
				for (int i = 0; i < biomarkerUris.size(); i++) {
					if (biomarkerUris.get(i).getName().equals(bioLabel)) {
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
				else if (_item.getDrugRec().getLabel().equals("Do not restart"))
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

					if (statemt.getLabel().equals("Active ingredient")) {
						descriptsai.setValue(true);
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

			// Variant

			if (_item.getVariant() != null) {

				if (_item.getVariant().getLabel().equals("unselected")) {
					descriptsvtlb.setSelectedIndex(0);
				}

				String variantLabel = _item.getVariant().getLabel();
				for (int i = 0; i < variant.length; i++) {
					if (variant[i].equals(variantLabel)) {
						descriptsvtlb.setSelectedIndex(i + 1);
					}
				}
			}

			// Test

			if (_item.getTest() != null) {

				if (_item.getTest().getLabel().equals("unselected")) {
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
			if (_item.getOtherVariant() != null) {
				descriptsothervt.setText(_item.getOtherVariant());
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
				System.out.println("onClick function triggered");
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
					_item.setBiomarkers(getBioMarkers());
					_item.setProductLabelSelection(getProductLabelSection());
					_item.setPKImpact(getPkImpact());
					_item.setPdImpact(getPdImpact());
					_item.setDrugRec(getDrugRec());
					_item.setDoseRec(getDoseRec());
					_item.setMonitRec(getMonitRec());
					_item.setStatements(getStatements());
					_item.setTestRec(getTest_Re());
					_item.setVariant(getVariant());
					_item.setTest(getTest());
					_item.setOtherVariant(descriptsothervt.getText());
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
