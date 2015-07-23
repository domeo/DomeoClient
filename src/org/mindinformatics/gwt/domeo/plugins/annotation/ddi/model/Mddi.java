package org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model;

import java.util.HashMap;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTrustedResource;

/**
 * @author Yifan Ning
 */
@SuppressWarnings("serial")
public class Mddi extends MTrustedResource {

	// basic DDI elements
	MLinkedResource drug1, drug2, type1, role1, type2, role2, statement, modality;
	String comment;

	// increase AUC
	MLinkedResource numOfparcipitants, objectDose, preciptDose, increaseAuc, evidenceType, assertType, t12, cmax,
			objectRegimen, preciptRegimen;

	public MLinkedResource getT12() {
		return t12;
	}

	public void setT12(MLinkedResource t12) {
		this.t12 = t12;
	}

	public MLinkedResource getCmax() {
		return cmax;
	}

	public void setCmax(MLinkedResource cmax) {
		this.cmax = cmax;
	}

	public MLinkedResource getObjectRegimen() {
		return objectRegimen;
	}

	public void setObjectRegimen(MLinkedResource objectRegimen) {
		this.objectRegimen = objectRegimen;
	}

	public MLinkedResource getPreciptRegimen() {
		return preciptRegimen;
	}

	public void setPreciptRegimen(MLinkedResource preciptRegimen) {
		this.preciptRegimen = preciptRegimen;
	}

	public MLinkedResource getAssertType() {
		return assertType;
	}

	public void setAssertType(MLinkedResource assertType) {
		this.assertType = assertType;
	}

	public MLinkedResource getNumOfparcipitants() {
		return numOfparcipitants;
	}

	public void setNumOfparcipitants(MLinkedResource numOfparcipitants) {
		this.numOfparcipitants = numOfparcipitants;
	}

	public MLinkedResource getObjectDose() {
		return objectDose;
	}

	public void setObjectDose(MLinkedResource objectDose) {
		this.objectDose = objectDose;
	}

	public MLinkedResource getPreciptDose() {
		return preciptDose;
	}

	public void setPreciptDose(MLinkedResource preciptDose) {
		this.preciptDose = preciptDose;
	}

	public MLinkedResource getIncreaseAuc() {
		return increaseAuc;
	}

	public void setIncreaseAuc(MLinkedResource increaseAuc) {
		this.increaseAuc = increaseAuc;
	}

	public MLinkedResource getEvidenceType() {
		return evidenceType;
	}

	public void setEvidenceType(MLinkedResource evidenceType) {
		this.evidenceType = evidenceType;
	}

	public static HashMap<String, drugEntity> getDrugEntitiesMap() {
		return drugEntitiesMap;
	}

	public static void setDrugEntitiesMap(HashMap<String, drugEntity> drugEntitiesMap) {
		Mddi.drugEntitiesMap = drugEntitiesMap;
	}

	/*
	 * inner class for drug entity
	 */
	public static class drugEntity {

		public drugEntity(String name, String rxcui, String type) {
			this.name = name;
			this.rxcui = rxcui;
			this.type = type;

		}

		private String name;
		private String rxcui;
		private String type;

		public String getRxcui() {
			return rxcui;
		}

		public void setRxcui(String rxcui) {
			this.rxcui = rxcui;
		}

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
	 * the mappings of exact string matching and drug attributes (preferred
	 * term, type and rxcui)
	 */

	public static HashMap<String, drugEntity> drugEntitiesMap = new HashMap<String, drugEntity>() {
		{
			put("4-hydroxyatomoxetine", new drugEntity("4-hydroxyatomoxetine", "0", "Metabolite"));

			put("7-hydroxymethotrexate", new drugEntity("7-hydroxymethotrexate", "0", "Metabolite"));

			put("9-hydroxyrisperidone", new drugEntity("9-hydroxyrisperidone", "679314", "Active Ingredient"));

			put("a 10", new drugEntity("a 10", "0", "NAN"));

			put("a 300", new drugEntity("a 300", "0", "NAN"));

			put("a 7", new drugEntity("a 7", "0", "NAN"));

			put("acarbose", new drugEntity("acarbose", "16681", "Active Ingredient"));

			put("acyclovir", new drugEntity("acyclovir", "281", "Active Ingredient"));

			put("adriamycin", new drugEntity("adriamycin", "42512", "Drug Product"));

			put("aliskiren", new drugEntity("aliskiren", "325646", "Active Ingredient"));

			put("alosetron", new drugEntity("alosetron", "85248", "Active Ingredient"));

			put("alprazolam", new drugEntity("alprazolam", "596", "Active Ingredient"));

			put("aminophylline", new drugEntity("aminophylline", "689", "Active Ingredient"));

			put("amiodarone", new drugEntity("amiodarone", "703", "Active Ingredient"));

			put("amitriptyline", new drugEntity("amitriptyline", "704", "Active Ingredient"));

			put("amlodipine", new drugEntity("amlodipine", "17767", "Active Ingredient"));

			put("amoxicillin", new drugEntity("amoxicillin", "723", "Active Ingredient"));

			put("ampicillin", new drugEntity("ampicillin", "733", "Active Ingredient"));

			put("angiotensinogen", new drugEntity("angiotensinogen", "0", "NAN"));

			put("antineoplastic agents", new drugEntity("antineoplastic agents", "0", "NAN"));

			put("aspirin", new drugEntity("aspirin", "1191", "Active Ingredient"));

			put("atazanavir", new drugEntity("atazanavir", "343047", "Active Ingredient"));

			put("atazanavir sulfate", new drugEntity("atazanavir sulfate", "358299", "Active Ingredient"));

			put("atenolol 100 mg", new drugEntity("atenolol 100 mg", "315436", "Active Ingredient"));

			put("atenolol", new drugEntity("atenolol", "1202", "Active Ingredient"));

			put("atomoxetine", new drugEntity("atomoxetine", "38400", "Active Ingredient"));

			put("atomoxetine hydrochloride",
					new drugEntity("atomoxetine hydrochloride", "353103", "Active Ingredient"));

			put("atorvastatin", new drugEntity("atorvastatin", "83367", "Active Ingredient"));

			put("avandia", new drugEntity("avandia", "261455", "Drug Product"));

			put("azathioprine", new drugEntity("azathioprine", "1256", "Active Ingredient"));

			put("azithromycin", new drugEntity("azithromycin", "18631", "Active Ingredient"));

			put("basis", new drugEntity("basis", "1046801", "Drug Product"));

			put("benzodiazepines", new drugEntity("benzodiazepines", "0", "NAN"));

			put("bisoprolol fumarate", new drugEntity("bisoprolol fumarate", "142146", "Active Ingredient"));

			put("bosentan", new drugEntity("bosentan", "75207", "Active Ingredient"));

			put("caffeine", new drugEntity("caffeine", "1886", "Active Ingredient"));

			put("carbamazepine", new drugEntity("carbamazepine", "2002", "Active Ingredient"));

			put("carbamazepine 400 mg", new drugEntity("carbamazepine 400 mg", "315567", "Active Ingredient"));

			put("carbamazepine epoxide", new drugEntity("carbamazepine epoxide", "2002", "Active Ingredient"));

			put("celecoxib", new drugEntity("celecoxib", "140587", "Active Ingredient"));

			put("chlorpromazine", new drugEntity("chlorpromazine", "2403", "Active Ingredient"));

			put("cholestyramine", new drugEntity("cholestyramine", "2447", "Active Ingredient"));

			put("cimetidine", new drugEntity("cimetidine", "2541", "Active Ingredient"));

			put("cimetidine 400 mg", new drugEntity("cimetidine 400 mg", "315680", "Active Ingredient"));

			put("cimetidine 600 mg", new drugEntity("cimetidine 600 mg", "332843", "Active Ingredient"));

			put("cisapride", new drugEntity("cisapride", "35255", "Active Ingredient"));

			put("cisplatin", new drugEntity("cisplatin", "2555", "Active Ingredient"));

			put("citalopram", new drugEntity("citalopram", "2556", "Active Ingredient"));

			put("citalopram 40 mg", new drugEntity("citalopram 40 mg", "329445", "Active Ingredient"));

			put("clarithromycin", new drugEntity("clarithromycin", "21212", "Active Ingredient"));

			put("clomipramine", new drugEntity("clomipramine", "2597", "Active Ingredient"));

			put("clonazepam", new drugEntity("clonazepam", "2598", "Active Ingredient"));

			put("clopidogrel", new drugEntity("clopidogrel", "32968", "Active Ingredient"));

			put("clozapine", new drugEntity("clozapine", "2626", "Active Ingredient"));

			put("colestipol", new drugEntity("colestipol", "2685", "Active Ingredient"));

			put("conivaptan", new drugEntity("conivaptan", "302285", "Active Ingredient"));

			put("conivaptan hydrochloride", new drugEntity("conivaptan hydrochloride", "1294548", "Active Ingredient"));

			put("cortisol", new drugEntity("cortisol", "5492", "Active Ingredient"));

			put("coumarin", new drugEntity("coumarin", "2898", "Active Ingredient"));

			put("cyclophosphamide", new drugEntity("cyclophosphamide", "3002", "Active Ingredient"));

			put("cyclosporine", new drugEntity("cyclosporine", "3008", "Active Ingredient"));

			put("dalfopristin", new drugEntity("dalfopristin", "229369", "Active Ingredient"));

			put("dapsone", new drugEntity("dapsone", "3108", "Active Ingredient"));

			put("desipramine", new drugEntity("desipramine", "3247", "Active Ingredient"));

			put("desmethyldiazepam", new drugEntity("desmethyldiazepam", "3155", "Active Ingredient"));

			put("diazepam", new drugEntity("diazepam", "3322", "Active Ingredient"));

			put("didanosine", new drugEntity("didanosine", "3364", "Active Ingredient"));

			put("digitalis", new drugEntity("digitalis", "91235", "Active Ingredient"));

			put("digitoxin", new drugEntity("digitoxin", "3403", "Active Ingredient"));

			put("digoxin", new drugEntity("digoxin", "3407", "Active Ingredient"));

			put("diltiazem", new drugEntity("diltiazem", "3443", "Active Ingredient"));

			put("disulfiram", new drugEntity("disulfiram", "3554", "Active Ingredient"));

			put("doxazosin", new drugEntity("doxazosin", "49276", "Active Ingredient"));

			put("doxorubicin", new drugEntity("doxorubicin", "3639", "Active Ingredient"));

			put("efavirenz", new drugEntity("efavirenz", "195085", "Active Ingredient"));

			put("epoprostenol", new drugEntity("epoprostenol", "8814", "Active Ingredient"));

			put("erythromycin", new drugEntity("erythromycin", "4053", "Active Ingredient"));

			put("esters", new drugEntity("esters", "0", "NAN"));

			put("ethanol", new drugEntity("ethanol", "448", "Active Ingredient"));

			put("ethinyl estradiol", new drugEntity("ethinyl estradiol", "4124", "Active Ingredient"));

			put("felbamate", new drugEntity("felbamate", "24812", "Active Ingredient"));

			put("felbatol", new drugEntity("felbatol", "224900", "Drug Product"));

			put("fexofenadine", new drugEntity("fexofenadine", "87636", "Active Ingredient"));

			put("fexofenadine hydrochloride 120 mg",
					new drugEntity("fexofenadine hydrochloride 120 mg", "997549", "Active Ingredient"));

			put("fexofenadine hydrochloride",
					new drugEntity("fexofenadine hydrochloride", "236474", "Active Ingredient"));

			put("flecainide", new drugEntity("flecainide", "4441", "Active Ingredient"));

			put("fluconazole 100 mg", new drugEntity("fluconazole 100 mg", "315935", "Active Ingredient"));

			put("fluconazole 200 mg", new drugEntity("fluconazole 200 mg", "315938", "Active Ingredient"));

			put("fluconazole", new drugEntity("fluconazole", "4450", "Active Ingredient"));

			put("fluconazole 50 mg", new drugEntity("fluconazole 50 mg", "315939", "Active Ingredient"));

			put("fluoxetine", new drugEntity("fluoxetine", "4493", "Active Ingredient"));

			put("fluvastatin", new drugEntity("fluvastatin", "41127", "Active Ingredient"));

			put("fluvoxamine", new drugEntity("fluvoxamine", "42355", "Active Ingredient"));

			put("fluvoxamine maleate 100 mg",
					new drugEntity("fluvoxamine maleate 100 mg", "903872", "Active Ingredient"));

			put("fluvoxamine maleate", new drugEntity("fluvoxamine maleate", "203143", "Active Ingredient"));

			put("fosamprenavir", new drugEntity("fosamprenavir", "358262", "Active Ingredient"));

			put("furosemide", new drugEntity("furosemide", "4603", "Active Ingredient"));

			put("ganciclovir", new drugEntity("ganciclovir", "4678", "Active Ingredient"));

			put("gemfibrozil", new drugEntity("gemfibrozil", "4719", "Active Ingredient"));

			put("glimepiride", new drugEntity("glimepiride", "25789", "Active Ingredient"));

			put("glipizide", new drugEntity("glipizide", "4821", "Active Ingredient"));

			put("glyburide", new drugEntity("glyburide", "4815", "Active Ingredient"));

			put("glycoprotein", new drugEntity("glycoprotein", "0", "NAN"));

			put("hmg-coa", new drugEntity("hmg-coa", "0", "NAN"));

			put("hydrochlorothiazide", new drugEntity("hydrochlorothiazide", "5487", "Active Ingredient"));

			put("hypromellose", new drugEntity("hypromellose", "27334", "Active Ingredient"));

			put("imipramine", new drugEntity("imipramine", "5691", "Active Ingredient"));

			put("indinavir", new drugEntity("indinavir", "114289", "Active Ingredient"));

			put("insulin", new drugEntity("insulin", "5856", "Active Ingredient"));

			put("isoproterenol", new drugEntity("isoproterenol", "6054", "Active Ingredient"));

			put("itraconazole 200 mg", new drugEntity("itraconazole 200 mg", "992412", "Active Ingredient"));

			put("itraconazole", new drugEntity("itraconazole", "28031", "Active Ingredient"));

			put("ketoconazole", new drugEntity("ketoconazole", "6135", "Active Ingredient"));

			put("lamivudine", new drugEntity("lamivudine", "68244", "Active Ingredient"));

			put("levodopa", new drugEntity("levodopa", "6375", "Active Ingredient"));

			put("levonorgestrel", new drugEntity("levonorgestrel", "6373", "Active Ingredient"));

			put("lipitor", new drugEntity("lipitor", "153165", "Drug Product"));

			put("lithium carbonate", new drugEntity("lithium carbonate", "42351", "Active Ingredient"));

			put("lopinavir", new drugEntity("lopinavir", "195088", "Active Ingredient"));

			put("lorazepam", new drugEntity("lorazepam", "6470", "Active Ingredient"));

			put("lotronex", new drugEntity("lotronex", "261563", "Drug Product"));

			put("lovastatin", new drugEntity("lovastatin", "6472", "Active Ingredient"));

			put("luteinizing hormone", new drugEntity("luteinizing hormone", "6383", "Active Ingredient"));

			put("maalox", new drugEntity("maalox", "29115", "Drug Product"));

			put("magnesium stearate", new drugEntity("magnesium stearate", "1310567", "Active Ingredient"));

			put("magnesium trisilicate", new drugEntity("magnesium trisilicate", "29170", "Active Ingredient"));

			put("meperidine", new drugEntity("meperidine", "6754", "Active Ingredient"));

			put("mercaptopurine", new drugEntity("mercaptopurine", "103", "Active Ingredient"));

			put("mesoridazine", new drugEntity("mesoridazine", "6779", "Active Ingredient"));

			put("metformin", new drugEntity("metformin", "6809", "Active Ingredient"));

			put("methadone", new drugEntity("methadone", "6813", "Active Ingredient"));

			put("methotrexate", new drugEntity("methotrexate", "6851", "Active Ingredient"));

			put("metoclopramide", new drugEntity("metoclopramide", "6915", "Active Ingredient"));

			put("metoprolol", new drugEntity("metoprolol", "6918", "Active Ingredient"));

			put("metoprolol succinate", new drugEntity("metoprolol succinate", "221124", "Active Ingredient"));

			put("metronidazole", new drugEntity("metronidazole", "6922", "Active Ingredient"));

			put("mexiletine", new drugEntity("mexiletine", "6926", "Active Ingredient"));

			put("mexiletine hydrochloride", new drugEntity("mexiletine hydrochloride", "142138", "Active Ingredient"));

			put("midazolam", new drugEntity("midazolam", "6960", "Active Ingredient"));

			put("monoamine oxidase inhibitors", new drugEntity("monoamine oxidase inhibitors", "0", "NAN"));

			put("naproxen 500 mg", new drugEntity("naproxen 500 mg", "316328", "Active Ingredient"));

			put("n-desmethylatomoxetine", new drugEntity("n-desmethylatomoxetine", "0", "Metabolite"));

			put("nefazodone", new drugEntity("nefazodone", "31565", "Active Ingredient"));

			put("nelfinavir", new drugEntity("nelfinavir", "134527", "Active Ingredient"));

			put("nifedipine 10 mg", new drugEntity("nifedipine 10 mg", "316352", "Active Ingredient"));

			put("nifedipine", new drugEntity("nifedipine", "7417", "Active Ingredient"));

			put("nisoldipine", new drugEntity("nisoldipine", "7435", "Active Ingredient"));

			put("nitrofurantoin", new drugEntity("nitrofurantoin", "7454", "Active Ingredient"));

			put("norethindrone", new drugEntity("norethindrone", "7514", "Active Ingredient"));

			put("normeperidine", new drugEntity("normeperidine", "0", "Metabolite"));

			put("noroxycodone", new drugEntity("noroxycodone", "0", "Metabolite"));

			put("nortriptyline", new drugEntity("nortriptyline", "7531", "Active Ingredient"));

			put("norverapamil", new drugEntity("norverapamil", "0", "Metabolite"));

			put("norverapamil", new drugEntity("norverapamil", "0", "NAN"));

			put("o-desmethylvenlafaxine", new drugEntity("o-desmethylvenlafaxine", "0", "Metabolite"));

			put("omeprazole 20 mg", new drugEntity("omeprazole 20 mg", "316408", "Active Ingredient"));

			put("omeprazole 40 mg", new drugEntity("omeprazole 40 mg", "317451", "Active Ingredient"));

			put("omeprazole", new drugEntity("omeprazole", "7646", "Active Ingredient"));

			put("oncovin", new drugEntity("oncovin", "0", "NAN"));

			put("orlistat", new drugEntity("orlistat", "37925", "Active Ingredient"));

			put("oxycodone", new drugEntity("oxycodone", "7804", "Active Ingredient"));

			put("oxymorphone", new drugEntity("oxymorphone", "7814", "Active Ingredient"));

			put("paclitaxel", new drugEntity("paclitaxel", "56946", "Active Ingredient"));

			put("paroxetine 20 mg", new drugEntity("paroxetine 20 mg", "317659", "Active Ingredient"));

			put("paroxetine", new drugEntity("paroxetine", "32937", "Active Ingredient"));

			put("paroxetine hydrochloride", new drugEntity("paroxetine hydrochloride", "235830", "Active Ingredient"));

			put("penicillins", new drugEntity("penicillins", "0", "NAN"));

			put("phenobarbital", new drugEntity("phenobarbital", "8134", "Active Ingredient"));

			put("phenothiazines", new drugEntity("phenothiazines", "0", "NAN"));

			put("phenytoin", new drugEntity("phenytoin", "8183", "Active Ingredient"));

			put("pimozide 2 mg", new drugEntity("pimozide 2 mg", "316520", "Active Ingredient"));

			put("pimozide", new drugEntity("pimozide", "8331", "Active Ingredient"));

			put("polydextrose", new drugEntity("polydextrose", "1363053", "Active Ingredient"));

			put("polyethylene glycol", new drugEntity("polyethylene glycol", "8516", "Active Ingredient"));

			put("povidone", new drugEntity("povidone", "8610", "Active Ingredient"));

			put("pravastatin", new drugEntity("pravastatin", "42463", "Active Ingredient"));

			put("prazosin", new drugEntity("prazosin", "8629", "Active Ingredient"));

			put("prednisone", new drugEntity("prednisone", "8640", "Active Ingredient"));

			put("probenecid", new drugEntity("probenecid", "8698", "Active Ingredient"));

			put("procaine", new drugEntity("procaine", "8701", "Active Ingredient"));

			put("procarbazine", new drugEntity("procarbazine", "8702", "Active Ingredient"));

			put("propafenone", new drugEntity("propafenone", "8754", "Active Ingredient"));

			put("propoxyphene", new drugEntity("propoxyphene", "8785", "Active Ingredient"));

			put("propranolol", new drugEntity("propranolol", "8787", "Active Ingredient"));

			put("quinidine", new drugEntity("quinidine", "9068", "Active Ingredient"));

			put("quinidine sulfate", new drugEntity("quinidine sulfate", "9069", "Active Ingredient"));

			put("quinupristin", new drugEntity("quinupristin", "229367", "Active Ingredient"));

			put("ramipril", new drugEntity("ramipril", "35296", "Active Ingredient"));

			put("ranitidine 150 mg", new drugEntity("ranitidine 150 mg", "328494", "Active Ingredient"));

			put("ranitidine", new drugEntity("ranitidine", "9143", "Active Ingredient"));

			put("repaglinide", new drugEntity("repaglinide", "73044", "Active Ingredient"));

			put("revatio", new drugEntity("revatio", "581642", "Drug Product"));

			put("reyataz", new drugEntity("reyataz", "382467", "Drug Product"));

			put("ribavirin", new drugEntity("ribavirin", "9344", "Active Ingredient"));

			put("rifampin", new drugEntity("rifampin", "9384", "Active Ingredient"));

			put("risperidone", new drugEntity("risperidone", "35636", "Active Ingredient"));

			put("ritonavir", new drugEntity("ritonavir", "85762", "Active Ingredient"));

			put("rosiglitazone", new drugEntity("rosiglitazone", "84108", "Active Ingredient"));

			put("rythmol", new drugEntity("rythmol", "82079", "Drug Product"));

			put("saquinavir", new drugEntity("saquinavir", "83395", "Active Ingredient"));

			put("septra", new drugEntity("septra", "202807", "Drug Product"));

			put("serotonin", new drugEntity("serotonin", "1311214", "Active Ingredient"));

			put("sertraline 200 mg", new drugEntity("sertraline 200 mg", "334494", "Active Ingredient"));

			put("sertraline", new drugEntity("sertraline", "36437", "Active Ingredient"));

			put("sildenafil", new drugEntity("sildenafil", "136411", "Active Ingredient"));

			put("simvastatin", new drugEntity("simvastatin", "36567", "Active Ingredient"));

			put("simvastatin 40 mg", new drugEntity("simvastatin 40 mg", "316674", "Active Ingredient"));

			put("simvastatin acid", new drugEntity("simvastatin acid", "36567", "Active Ingredient"));

			put("sirolimus", new drugEntity("sirolimus", "35302", "Active Ingredient"));

			put("stavudine", new drugEntity("stavudine", "59763", "Active Ingredient"));

			put("sulfinpyrazone", new drugEntity("sulfinpyrazone", "10205", "Active Ingredient"));

			put("synercid", new drugEntity("synercid", "135097", "Drug Product"));

			put("tacrine", new drugEntity("tacrine", "10318", "Active Ingredient"));

			put("tacrolimus", new drugEntity("tacrolimus", "42316", "Active Ingredient"));

			put("tenofovir disoproxil fumarate",
					new drugEntity("tenofovir disoproxil fumarate", "322248", "Active Ingredient"));

			put("terfenadine", new drugEntity("terfenadine", "42330", "Active Ingredient"));

			put("theophylline", new drugEntity("theophylline", "10438", "Active Ingredient"));

			put("thiazide diuretics", new drugEntity("thiazide diuretics", "0", "NAN"));

			put("thioridazine", new drugEntity("thioridazine", "10502", "Active Ingredient"));

			put("thymidine", new drugEntity("thymidine", "1372538", "Active Ingredient"));

			put("tizanidine", new drugEntity("tizanidine", "57258", "Active Ingredient"));

			put("tolbutamide", new drugEntity("tolbutamide", "10635", "Active Ingredient"));

			put("trazodone", new drugEntity("trazodone", "10737", "Active Ingredient"));

			put("triacetin", new drugEntity("triacetin", "10756", "Active Ingredient"));

			put("triazolam", new drugEntity("triazolam", "10767", "Active Ingredient"));

			put("triptans", new drugEntity("triptans", "0", "NAN"));

			put("tryptophan", new drugEntity("tryptophan", "10898", "Active Ingredient"));

			put("valproate", new drugEntity("valproate", "40254", "Active Ingredient"));

			put("venlafaxine", new drugEntity("venlafaxine", "39786", "Active Ingredient"));

			put("venlafaxine 50 mg", new drugEntity("venlafaxine 50 mg", "328800", "Active Ingredient"));

			put("venlafaxine hydrochloride",
					new drugEntity("venlafaxine hydrochloride", "235988", "Active Ingredient"));

			put("verapamil", new drugEntity("verapamil", "11170", "Active Ingredient"));

			put("verapamil hydrochloride", new drugEntity("verapamil hydrochloride", "203138", "Active Ingredient"));

			put("vindesine", new drugEntity("vindesine", "11204", "Active Ingredient"));

			put("voriconazole", new drugEntity("voriconazole", "121243", "Active Ingredient"));

			put("warfarin", new drugEntity("warfarin", "11289", "Active Ingredient"));

			put("zidovudine", new drugEntity("zidovudine", "11413", "Active Ingredient"));

			put("zonisamide", new drugEntity("zonisamide", "39998", "Active Ingredient"));

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

	public Mddi(String url, String label, MGenericResource source) {
		super(url, label, source);
	}

}
