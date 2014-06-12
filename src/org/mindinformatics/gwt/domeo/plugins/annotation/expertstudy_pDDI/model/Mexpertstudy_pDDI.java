package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model;

import java.util.HashMap;
import java.util.HashSet;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTrustedResource;

/**
 * @author Andres Hernandez <amh211@pitt.edu>
 */
@SuppressWarnings("serial")
public class Mexpertstudy_pDDI extends MTrustedResource {

	MLinkedResource drug1, drug2, type1, role1, type2, role2, statement,
			modality;
	String comment;

	/*
	 * inner class for drug entity
	 *
	 */
	public static class drugEntity {

		public drugEntity(String name, String URI, String type) {
			this.name = name;
			
			this.type = type;

		}

		private String name;
		private String rxcui;
		public String getRxcui() {
			return rxcui;
		}

		public void setRxcui(String rxcui) {
			this.rxcui = rxcui;
		}

		private String type;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}

	/*
	 * define drug entities
	 * 
	 * mappings of exact drug name in selector to preferred name, URI and type
	 */

	public static HashMap<String, drugEntity> getDrugEntities() {
		return drugEntitiesMap;
	}

	/*
	 * the mappings of exact string matching and drug attributes (preferred term, type and rxcui)
	 */
	
	public static HashMap<String, drugEntity> drugEntitiesMap = new HashMap<String, drugEntity>() {
		{
			put("adriamycin", new drugEntity("ADRIAMYCIN",
					"1191140",
					"drug product"));
			put("warfarin", new drugEntity("warfarin",
					"11289",
					"Active Ingredient"));
		

			// TODO: add mappings of drug entities into model
		}

	};

	/*
	 * the mappings of drug product to purl URI (rxcui)
	 */
	public static HashMap<String, String> drugProduct_URI_map = new HashMap<String, String>() {
		{
			put("ADRIAMYCIN",
					"http://purl.bioontology.org/ontology/RXNORM/1191140");
			put("AVALIDE", "http://purl.bioontology.org/ontology/RXNORM/823938");
			put("AVANDIA", "http://purl.bioontology.org/ontology/RXNORM/261242");
			put("FELBATOL",
					"http://purl.bioontology.org/ontology/RXNORM/209028");
			put("LIPITOR", "http://purl.bioontology.org/ontology/RXNORM/262095");
			put("ONCOVIN", "http://purl.bioontology.org/ontology/RXNORM/152151");
			put("PLAVIX", "http://purl.bioontology.org/ontology/RXNORM/749196");
			put("REYATAZ", "http://purl.bioontology.org/ontology/RXNORM/402095");
			put("SEPTRA", "http://purl.bioontology.org/ontology/RXNORM/876429");
			put("SYNERCID",
					"http://purl.bioontology.org/ontology/RXNORM/261306");
			put("VAPRISOL",
					"http://purl.bioontology.org/ontology/RXNORM/1294552");
		}

	};

	public static HashMap<String, String> getDrugProduct_URI_map() {
		return drugProduct_URI_map;
	}

	public static HashMap<String, String> getMetabolite_URI_map() {
		return metabolite_URI_map;
	}

	public static HashMap<String, String> getActiveIngredient_URI_map() {
		return activeIngredient_URI_map;
	}

	/*
	 * the mappings of metabolite to purl URI (rxcui)
	 */
	public static HashMap<String, String> metabolite_URI_map = new HashMap<String, String>() {
		{
			put("14-HYDROXY-clarithromycin",
					"http://www.biosemantics.org/chemlist#concept:14_hydroxyclarithromycin:4260775");
			put("2-OH-DESIPRAMINE",
					"http://www.biosemantics.org/chemlist#concept:2_hydroxydesipramine:4256097");
			put("2-OH-IMIPRAMINE",
					"http://www.biosemantics.org/chemlist#concept:2_Hydroxyimipramine:4010185");
			put("7-HYDROXYMETHOTREXATE",
					"http://www.biosemantics.org/chemlist#concept:7_Hydroxymethotrexate:4045813");
			put("9-HYDROXYRISPERIDONE",
					"http://www.biosemantics.org/chemlist#concept:9_hydroxy_risperidone:4265124");
			put("CARBAMAZEPINE EXPOXIDE",
					"http://www.biosemantics.org/chemlist#concept:carbamazepine_epoxide:4253040");
			put("DESIPRAMINE",
					"http://www.biosemantics.org/chemlist#concept:desipramine:4274764");
			put("DESMETHYLDIAZEPAM",
					" http://www.biosemantics.org/chemlist#concept:Nordazepam:4249145");
			put("?-HYDROXYMETOPROLOL",
					"http://www.biosemantics.org/chemlist#concept:alpha_Hydroxymetoprolol:4125156");
			put("M8",
					"http://www.biosemantics.org/chemlist#concept:2_Amino_2_methyl_1_propanol:4008675");
			put("MCPP",
					"http://www.biosemantics.org/chemlist#concept:mecoprop:4252108");
			put("MESORIDAZINE",
					"http://www.biosemantics.org/chemlist#concept:Mesoridazine:4249568");
			put("NORMEPERIDINE",
					"http://www.biosemantics.org/chemlist#concept:normeperidine:4251009");
			put("NORVERAPAMIL",
					"http://www.biosemantics.org/chemlist#concept:norverapamil:4254029");
			put("O-DESMETHYLVENLAFAXINE",
					"http://www.biosemantics.org/chemlist#concept:O_desmethylvenlafaxine:4266016");
			put("SULFORIADAZINE",
					"http://www.biosemantics.org/chemlist#concept:sulforidazine:4258300");

		}

	};

	/*
	 * the mappings of active ingredient to purl URI (rxcui)
	 */
	public static HashMap<String, String> activeIngredient_URI_map = new HashMap<String, String>() {
		{
			put("ACARBOSE", "http://purl.bioontology.org/ontology/RXNORM/16681");
			put("ACETAMINOPHEN",
					"http://purl.bioontology.org/ontology/RXNORM/161");
			put("ACYCLOVIR", "http://purl.bioontology.org/ontology/RXNORM/281");
			put("ADRIAMYCIN",
					"http://purl.bioontology.org/ontology/RXNORM/42512");
			put("ALISKIREN",
					"http://purl.bioontology.org/ontology/RXNORM/325646");
			put("ALLOPURINOL",
					"http://purl.bioontology.org/ontology/RXNORM/519");
			put("ALPRAZOLAM", "http://purl.bioontology.org/ontology/RXNORM/596");
			put("ALUMINUM",
					"http://purl.bioontology.org/ontology/RXNORM/1311504");
			put("ALUMINUM HYDROXIDE",
					"http://purl.bioontology.org/ontology/RXNORM/612");
			put("AMITRIPTYLINE",
					"http://purl.bioontology.org/ontology/RXNORM/704");
			put("AMLODIPINE",
					"http://purl.bioontology.org/ontology/RXNORM/17767");
			put("AMOXICILLIN",
					"http://purl.bioontology.org/ontology/RXNORM/723");
			put("AMPICILLIN", "http://purl.bioontology.org/ontology/RXNORM/733");
			put("ANTIPYRINE",
					"http://purl.bioontology.org/ontology/RXNORM/1001");
			put("ATAZANAVIR",
					"http://purl.bioontology.org/ontology/RXNORM/343047");
			put("ATENOLOL", "http://purl.bioontology.org/ontology/RXNORM/1202");
			put("ATOMOXETINE",
					"http://purl.bioontology.org/ontology/RXNORM/38400");
			put("ATORVASTATIN",
					"http://purl.bioontology.org/ontology/RXNORM/83367");
			put("AZATHIOPRINE",
					"http://purl.bioontology.org/ontology/RXNORM/1256");
			put("AZITHROMYCIN",
					"http://purl.bioontology.org/ontology/RXNORM/18631");
			put("BISOPROLOL",
					"http://purl.bioontology.org/ontology/RXNORM/19484");
			put("BOSENTAN", "http://purl.bioontology.org/ontology/RXNORM/75207");
			put("CAFFEINE", "http://purl.bioontology.org/ontology/RXNORM/1886");
			put("CAPTOPRIL", "http://purl.bioontology.org/ontology/RXNORM/1998");
			put("CARBAMAZEPINE",
					"http://purl.bioontology.org/ontology/RXNORM/2002");
			put("CELECOXIB",
					"http://purl.bioontology.org/ontology/RXNORM/140587");
			put("CHLORPROMAZINE",
					"http://purl.bioontology.org/ontology/RXNORM/2403");
			put("CHOLESTYRAMINE",
					"http://purl.bioontology.org/ontology/RXNORM/2447");
			put("CIMETIDINE",
					"http://purl.bioontology.org/ontology/RXNORM/2541");
			put("CISAPRIDE",
					"http://purl.bioontology.org/ontology/RXNORM/35255");
			put("CISPLATIN", "http://purl.bioontology.org/ontology/RXNORM/2555");
			put("CITALOPRAM",
					"http://purl.bioontology.org/ontology/RXNORM/2556");
			put("CLARITHROMYCIN",
					"http://purl.bioontology.org/ontology/RXNORM/21212");
			put("CLOMIPRAMINE",
					"http://purl.bioontology.org/ontology/RXNORM/2597");
			put("CLONAZEPAM",
					"http://purl.bioontology.org/ontology/RXNORM/2598");
			put("CLOPIDOGREL",
					"http://purl.bioontology.org/ontology/RXNORM/32968");
			put("CLOZAPINE", "http://purl.bioontology.org/ontology/RXNORM/2626");
			put("COLESTIPOL",
					"http://purl.bioontology.org/ontology/RXNORM/2685");
			put("CONIVAPTAN",
					"http://purl.bioontology.org/ontology/RXNORM/302285");
			put("CYCLOPHOSPHAMIDE",
					"http://purl.bioontology.org/ontology/RXNORM/3002");
			put("CYCLOSPORINE",
					"http://purl.bioontology.org/ontology/RXNORM/3008");
			put("DALFOPRISTIN",
					"http://purl.bioontology.org/ontology/RXNORM/229369");
			put("DAPSONE", "http://purl.bioontology.org/ontology/RXNORM/3108");
			put("DESIPRAMINE",
					"http://purl.bioontology.org/ontology/RXNORM/3247");
			put("DIAZEPAM", "http://purl.bioontology.org/ontology/RXNORM/3322");
			put("DIDANOSINE",
					"http://purl.bioontology.org/ontology/RXNORM/3364");
			put("DIGITOXIN", "http://purl.bioontology.org/ontology/RXNORM/3403");
			put("DIGOXIN", "http://purl.bioontology.org/ontology/RXNORM/3407");
			put("DILTIAZEM", "http://purl.bioontology.org/ontology/RXNORM/3443");
			put("DISULFIRAM",
					"http://purl.bioontology.org/ontology/RXNORM/3554");
			put("DONEPEZIL",
					"http://purl.bioontology.org/ontology/RXNORM/135447");
			put("DOXAZOSIN",
					"http://purl.bioontology.org/ontology/RXNORM/49276");
			put("DOXORUBICIN",
					"http://purl.bioontology.org/ontology/RXNORM/3639");
			put("EFAVIRENZ",
					"http://purl.bioontology.org/ontology/RXNORM/195085");
			put("EPOPROSTENOL",
					"http://purl.bioontology.org/ontology/RXNORM/8814");
			put("ERYTHROMYCIN",
					"http://purl.bioontology.org/ontology/RXNORM/4053");
			put("ESTRADIOL", "http://purl.bioontology.org/ontology/RXNORM/4083");
			put("ETHANOL", "http://purl.bioontology.org/ontology/RXNORM/448");
			put("ETHINYL ESTRADIOL",
					"http://purl.bioontology.org/ontology/RXNORM/4124");
			put("FELBAMATE",
					"http://purl.bioontology.org/ontology/RXNORM/24812");
			put("FEXOFENADINE",
					"http://purl.bioontology.org/ontology/RXNORM/87636");
			put("FLUCONAZOLE",
					"http://purl.bioontology.org/ontology/RXNORM/4450");
			put("FLUOXETINE",
					"http://purl.bioontology.org/ontology/RXNORM/4493");
			put("FLUVASTATIN",
					"http://purl.bioontology.org/ontology/RXNORM/41127");
			put("FLUVOXAMINE",
					"http://purl.bioontology.org/ontology/RXNORM/42355");
			put("FUROSEMIDE",
					"http://purl.bioontology.org/ontology/RXNORM/4603");
			put("GALANTAMINE",
					"http://purl.bioontology.org/ontology/RXNORM/4637");
			put("GEMFIBROZIL",
					"http://purl.bioontology.org/ontology/RXNORM/4719");
			put("GESTODENE",
					"http://purl.bioontology.org/ontology/RXNORM/25734");
			put("GLIMEPIRIDE",
					"http://purl.bioontology.org/ontology/RXNORM/25789");
			put("GLIPIZIDE", "http://purl.bioontology.org/ontology/RXNORM/4821");
			put("GLYBURIDE", "http://purl.bioontology.org/ontology/RXNORM/4815");
			put("HALOPERIDOL",
					"http://purl.bioontology.org/ontology/RXNORM/5093");
			put("HYDROCHLOROTHIAZIDE",
					"http://purl.bioontology.org/ontology/RXNORM/5487");
			put("IBUPROFEN", "http://purl.bioontology.org/ontology/RXNORM/5640");
			put("IMIPRAMINE",
					"http://purl.bioontology.org/ontology/RXNORM/5691");
			put("INDINAVIR",
					"http://purl.bioontology.org/ontology/RXNORM/114289");
			put("INDOMETHACIN",
					"http://purl.bioontology.org/ontology/RXNORM/5781");
			put("IRBESARTAN",
					"http://purl.bioontology.org/ontology/RXNORM/83818");
			put("IRON", "http://purl.bioontology.org/ontology/RXNORM/90176");
			put("ISONIAZID", "http://purl.bioontology.org/ontology/RXNORM/6038");
			put("ITRACONAZOLE",
					"http://purl.bioontology.org/ontology/RXNORM/28031");
			put("KETOCONAZOLE",
					"http://purl.bioontology.org/ontology/RXNORM/6135");
			put("LAMIVUDINE",
					"http://purl.bioontology.org/ontology/RXNORM/68244");
			put("LANSOPRAZOLE",
					"http://purl.bioontology.org/ontology/RXNORM/17128");
			put("LEVODOPA", "http://purl.bioontology.org/ontology/RXNORM/6375");
			put("LEVONORGESTREL",
					"http://purl.bioontology.org/ontology/RXNORM/6373");
			put("LIDOCAINE", "http://purl.bioontology.org/ontology/RXNORM/6387");
			put("LITHIUM", "http://purl.bioontology.org/ontology/RXNORM/6448");
			put("LOPINAVIR",
					"http://purl.bioontology.org/ontology/RXNORM/195088");
			put("LORAZEPAM", "http://purl.bioontology.org/ontology/RXNORM/6470");
			put("LOVASTATIN",
					"http://purl.bioontology.org/ontology/RXNORM/6472");
			put("MAGNESIUM", "http://purl.bioontology.org/ontology/RXNORM/6574");
			put("MAGNESIUM HYDROXIDE",
					"http://purl.bioontology.org/ontology/RXNORM/6581");
			put("MEPERIDINE",
					"http://purl.bioontology.org/ontology/RXNORM/6754");
			put("MERCAPTOPURINE",
					"http://purl.bioontology.org/ontology/RXNORM/103");
			put("METFORMIN", "http://purl.bioontology.org/ontology/RXNORM/6809");
			put("METHADONE", "http://purl.bioontology.org/ontology/RXNORM/6813");
			put("METHOTREXATE",
					"http://purl.bioontology.org/ontology/RXNORM/6851");
			put("METOCLOPRAMIDE",
					"http://purl.bioontology.org/ontology/RXNORM/6915");
			put("METOPROLOL",
					"http://purl.bioontology.org/ontology/RXNORM/6918");
			put("MEXILETINE",
					"http://purl.bioontology.org/ontology/RXNORM/6926");
			put("MIDAZOLAM", "http://purl.bioontology.org/ontology/RXNORM/6960");
			put("NAPROXEN", "http://purl.bioontology.org/ontology/RXNORM/7258");
			put("NEFAZODONE",
					"http://purl.bioontology.org/ontology/RXNORM/31565");
			put("NELFINAVIR",
					"http://purl.bioontology.org/ontology/RXNORM/134527");
			put("NIFEDIPINE",
					"http://purl.bioontology.org/ontology/RXNORM/7417");
			put("NISOLDIPINE",
					"http://purl.bioontology.org/ontology/RXNORM/7435");
			put("NITROFURANTOIN",
					"http://purl.bioontology.org/ontology/RXNORM/7454");
			put("NORETHINDRONE",
					"http://purl.bioontology.org/ontology/RXNORM/7514");
			put("NORTRIPTYLINE",
					"http://purl.bioontology.org/ontology/RXNORM/7531");
			put("OMEPRAZOLE",
					"http://purl.bioontology.org/ontology/RXNORM/7646");
			put("OXYCODONE", "http://purl.bioontology.org/ontology/RXNORM/7804");
			put("PACLITAXEL",
					"http://purl.bioontology.org/ontology/RXNORM/56946");
			put("PAROXETINE",
					"http://purl.bioontology.org/ontology/RXNORM/32937");
			put("PHENOBARBITAL",
					"http://purl.bioontology.org/ontology/RXNORM/8134");
			put("PHENYTOIN", "http://purl.bioontology.org/ontology/RXNORM/8183");
			put("PIMOZIDE", "http://purl.bioontology.org/ontology/RXNORM/8331");
			put("PRAVASTATIN",
					"http://purl.bioontology.org/ontology/RXNORM/42463");
			put("PREDNISONE",
					"http://purl.bioontology.org/ontology/RXNORM/8640");
			put("PROBENECID",
					"http://purl.bioontology.org/ontology/RXNORM/8698");
			put("PROCARBAZINE",
					"http://purl.bioontology.org/ontology/RXNORM/8702");
			put("PROCYCLIDINE",
					"http://purl.bioontology.org/ontology/RXNORM/8718");
			put("PROPAFENONE",
					"http://purl.bioontology.org/ontology/RXNORM/8754");
			put("PROPOXYPHENE",
					"http://purl.bioontology.org/ontology/RXNORM/8785");
			put("PROPRANOLOL",
					"http://purl.bioontology.org/ontology/RXNORM/8787");
			put("QUINIDINE", "http://purl.bioontology.org/ontology/RXNORM/9068");
			put("QUINUPRISTIN",
					"http://purl.bioontology.org/ontology/RXNORM/229367");
			put("RAMIPRIL", "http://purl.bioontology.org/ontology/RXNORM/35296");
			put("RANITIDINE",
					"http://purl.bioontology.org/ontology/RXNORM/9143");
			put("REPAGLINIDE",
					"http://purl.bioontology.org/ontology/RXNORM/73044");
			put("RIBAVIRIN", "http://purl.bioontology.org/ontology/RXNORM/9344");
			put("RIFAMPIN", "http://purl.bioontology.org/ontology/RXNORM/9384");
			put("RISPERIDONE",
					"http://purl.bioontology.org/ontology/RXNORM/35636");
			put("RITONAVIR",
					"http://purl.bioontology.org/ontology/RXNORM/85762");
			put("ROSIGLITAZONE",
					"http://purl.bioontology.org/ontology/RXNORM/84108");
			put("SAQUINAVIR",
					"http://purl.bioontology.org/ontology/RXNORM/83395");
			put("SERTRALINE",
					"http://purl.bioontology.org/ontology/RXNORM/36437");
			put("SILDENAFIL",
					"http://purl.bioontology.org/ontology/RXNORM/136411");
			put("SIMVASTATIN",
					"http://purl.bioontology.org/ontology/RXNORM/36567");
			put("SIROLIMUS",
					"http://purl.bioontology.org/ontology/RXNORM/35302");
			put("SOTALOL", "http://purl.bioontology.org/ontology/RXNORM/9947");
			put("STAVUDINE",
					"http://purl.bioontology.org/ontology/RXNORM/59763");
			put("SUCRALFATE",
					"http://purl.bioontology.org/ontology/RXNORM/10156");
			put("SULFAMETHOXAZOLE",
					"http://purl.bioontology.org/ontology/RXNORM/10180");
			put("SULFINPYRAZONE",
					"http://purl.bioontology.org/ontology/RXNORM/10205");
			put("TACRINE", "http://purl.bioontology.org/ontology/RXNORM/10318");
			put("TACROLIMUS",
					"http://purl.bioontology.org/ontology/RXNORM/42316");
			put("TENOFOVIR",
					"http://purl.bioontology.org/ontology/RXNORM/117466");
			put("TERFENADINE",
					"http://purl.bioontology.org/ontology/RXNORM/42330");
			put("TETRACYCLINE",
					"http://purl.bioontology.org/ontology/RXNORM/10395");
			put("THEOPHYLLINE",
					"http://purl.bioontology.org/ontology/RXNORM/10438");
			put("THIORIDAZINE",
					"http://purl.bioontology.org/ontology/RXNORM/10502");
			put("TOLBUTAMIDE",
					"http://purl.bioontology.org/ontology/RXNORM/10635");
			put("TRAZODONE",
					"http://purl.bioontology.org/ontology/RXNORM/10737");
			put("TRIAZOLAM",
					"http://purl.bioontology.org/ontology/RXNORM/10767");
			put("TRIMETHOPRIM",
					"http://purl.bioontology.org/ontology/RXNORM/10829");
			put("VALPROATE",
					"http://purl.bioontology.org/ontology/RXNORM/40254");
			put("VALSARTAN",
					"http://purl.bioontology.org/ontology/RXNORM/69749");
			put("VENLAFAXINE",
					"http://purl.bioontology.org/ontology/RXNORM/39786");
			put("VERAPAMIL",
					"http://purl.bioontology.org/ontology/RXNORM/11170");
			put("VINDESINE",
					"http://purl.bioontology.org/ontology/RXNORM/11204");
			put("VORICONAZOLE",
					"http://purl.bioontology.org/ontology/RXNORM/121243");
			put("WARFARIN", "http://purl.bioontology.org/ontology/RXNORM/11289");
			put("ZIDOVUDINE",
					"http://purl.bioontology.org/ontology/RXNORM/11413");
			put("ZONISAMIDE",
					"http://purl.bioontology.org/ontology/RXNORM/39998");

		}

	};

	public MLinkedResource getStatement() {
		return statement;
	}

	public void setStatement(MLinkedResource statement) {
		this.statement = statement;
	}

	public MLinkedResource getDrug1() {
		return drug1;
	}

	public void setDrug1(MLinkedResource drug1) {
		this.drug1 = drug1;
	}

	public MLinkedResource getDrug2() {
		return drug2;
	}

	public void setDrug2(MLinkedResource drug2) {
		this.drug2 = drug2;
	}

	public MLinkedResource getType1() {
		return type1;
	}

	public void setType1(MLinkedResource type1) {
		this.type1 = type1;
	}

	public MLinkedResource getRole1() {
		return role1;
	}

	public void setRole1(MLinkedResource role1) {
		this.role1 = role1;
	}

	public MLinkedResource getType2() {
		return type2;
	}

	public void setType2(MLinkedResource type2) {
		this.type2 = type2;
	}

	public MLinkedResource getRole2() {
		return role2;
	}

	public void setRole2(MLinkedResource role2) {
		this.role2 = role2;
	}

	public MLinkedResource getModality() {
		return modality;
	}

	public void setModality(MLinkedResource modality) {
		this.modality = modality;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Mexpertstudy_pDDI(String url, String label, MGenericResource source) {
		super(url, label, source);
	}

}
