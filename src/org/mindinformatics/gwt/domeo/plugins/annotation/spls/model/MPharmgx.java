package org.mindinformatics.gwt.domeo.plugins.annotation.spls.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTrustedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
public class MPharmgx extends MTrustedResource {

	// String pkImpact, pdImpact, doseRec, drugRec, monitRec, testRec;

	MLinkedResource pkImpactResource, pdImpactResource, doseRecResource,
			drugRecResource, monitRecResource, testRecResource, comment,
			Alleles, biomarkers, variant, test, medicalCondition,
			drugOfInterest, productLabelSelection, HGNCGeneSymbol;

	public MLinkedResource getHGNCGeneSymbol() {
		return HGNCGeneSymbol;
	}

	public void setHGNCGeneSymbol(MLinkedResource hGNCGeneSymbol) {
		HGNCGeneSymbol = hGNCGeneSymbol;
	}

	public List<Biomarker> biomarkerUris = new ArrayList<Biomarker>() {
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

	public static HashMap<String, String> HGNCGeneSymbolUris = new HashMap<String, String>() {
		{
			put("ALK", "http://bio2rdf.org/hgnc:427");
			put("BAFF/TNFSF13B", "http://bio2rdf.org/hgnc:11929");
			put("BCR/ABL1", "http://bio2rdf.org/hgnc:76");
			put("BRAF", "http://bio2rdf.org/hgnc:1097");
			put("CCR5", "http://bio2rdf.org/hgnc:1606");
			put("CFTR", "http://bio2rdf.org/hgnc:1884");
			put("CYB5R1-4", "http://bio2rdf.org/hgnc:13397");
			put("CYP1A2", "http://bio2rdf.org/hgnc:2596");
			put("CYP2C19", "http://bio2rdf.org/hgnc:2621");
			put("CYP2C9", "http://bio2rdf.org/hgnc:2623");
			put("CYP2D6", "http://bio2rdf.org/hgnc:2625");
			put("del (5q)", "http://www.fakeuri.com");
			put("DPYD", "http://bio2rdf.org/hgnc:3012");
			put("EGFR", "http://bio2rdf.org/hgnc:3236");
			put("ERBB2", "http://bio2rdf.org/hgnc:3430");
			put("ESR1", "http://bio2rdf.org/hgnc:3467");
			put("ESR1, PGR", "http://bio2rdf.org/hgnc:8910");
			put("F2", "http://bio2rdf.org/hgnc:3535");
			put("F5", "http:// bio2rdf.org/hgnc:3542");
			put("FIP1L1/PDGFRA", "http://bio2rdf.org/hgnc:8803");
			put("G6PD", "http://bio2rdf.org/hgnc:4057");
			put("GBA", "http://bio2rdf.org/hgnc:4177");
			put("HLA-A", "http://bio2rdf.org/hgnc:4931  ");
			put("HLA-B", "http://bio2rdf.org/hgnc:4932");
			put("HPRT1", "http://bio2rdf.org/hgnc:5157");
			put("IFNL3", "http://www.fakeuri.com");
			put("IL2RA", "http://bio2rdf.org/hgnc:6008");
			put("KIT", "http://bio2rdf.org/hgnc:6342");
			put("KRAS", "http://bio2rdf.org/hgnc:6407");
			put("LDLR", "http://bio2rdf.org/hgnc:6547");
			put("MS4A1", "http://bio2rdf.org/hgnc:7315");
			put("NAGS", "http://bio2rdf.org/hgnc:17996");
			put("NAGS, CPS1, ASS1, OTC, ASL, ABL2", "http://www.fakeuri.com");
			put("NAT1-2", "http://www.fakeuri.com");
			put("PDGFRB", "http://bio2rdf.org/hgnc:8804");
			put("Ph Chromosome", "http://www.fakeuri.com");
			put("PML/RARA", "http://bio2rdf.org/hgnc:9864");
			put("POLG", "http://bio2rdf.org/hgnc:9179");
			put("SERPINC1", "http://bio2rdf.org/hgnc:775");
			put("TNFRSF8", "http://bio2rdf.org/hgnc:11923");
			put("TPMT", "http://bio2rdf.org/hgnc:12014");
			put("UGT1A1", "http://bio2rdf.org/hgnc:12530");
			put("VKORC1", "http://bio2rdf.org/hgnc:23663");
		}
	};

	public static HashMap<String, String> drugUris_FDA = new HashMap<String, String>() {
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
	
	public String getHGNCGeneSymbolUri(String name){
		return HGNCGeneSymbolUris.get(name);
	}

	public String getDrugUri(String drugname) {
		return drugUris_FDA.get(drugname);
	}

	public String getBioUri(int index) {
		return biomarkerUris.get(index).getUri();
	}

	public String getBioName(int index) {
		return biomarkerUris.get(index).getName();
	}

	public int getLengthOfBioList() {
		return biomarkerUris.size();
	}

	public MLinkedResource getVariant() {
		return variant;
	}

	public MLinkedResource getDrugOfInterest() {
		return drugOfInterest;
	}

	public void setDrugOfInterest(MLinkedResource drugOfInterest) {
		this.drugOfInterest = drugOfInterest;
	}

	public MLinkedResource getProductLabelSelection() {
		return productLabelSelection;
	}

	public void setProductLabelSelection(MLinkedResource productLabelSelection) {
		this.productLabelSelection = productLabelSelection;
	}

	public void setPkImpactResource(MLinkedResource pkImpactResource) {
		this.pkImpactResource = pkImpactResource;
	}

	public void setPdImpactResource(MLinkedResource pdImpactResource) {
		this.pdImpactResource = pdImpactResource;
	}

	public void setDoseRecResource(MLinkedResource doseRecResource) {
		this.doseRecResource = doseRecResource;
	}

	public void setDrugRecResource(MLinkedResource drugRecResource) {
		this.drugRecResource = drugRecResource;
	}

	public void setMonitRecResource(MLinkedResource monitRecResource) {
		this.monitRecResource = monitRecResource;
	}

	public void setTestRecResource(MLinkedResource testRecResource) {
		this.testRecResource = testRecResource;
	}

	public void setVariant(MLinkedResource variant) {
		this.variant = variant;
	}

	public MLinkedResource getMedicalCondition() {
		return medicalCondition;
	}

	public void setMedicalCondition(MLinkedResource medicalCondition) {
		this.medicalCondition = medicalCondition;
	}

	public MLinkedResource getTest() {
		return test;
	}

	public void setTest(MLinkedResource test) {
		this.test = test;
	}

	Set<MLinkedResource> statementsResource;

	public void setAlleles(MLinkedResource alleles) {
		Alleles = alleles;
	}

	public void setBiomarkers(MLinkedResource biomarkers) {
		this.biomarkers = biomarkers;
	}

	public MLinkedResource getComment() {
		return comment;
	}

	public MLinkedResource getAlleles() {
		return Alleles;
	}

	public MLinkedResource getBiomarkers() {
		return biomarkers;
	}

	public void setComment(MLinkedResource comment) {
		this.comment = comment;
	}

	public MPharmgx(String url, String label, MGenericResource source) {
		super(url, label, source);
	}

	public Set<MLinkedResource> getStatementsResource() {
		return statementsResource;
	}

	public MLinkedResource getPkImpactResource() {
		return pkImpactResource;
	}

	// public String getPkImpactLabel() {
	// return (pkImpactResource!=null) ? pkImpactResource.getLabel():"";
	// }
	public void setPkImpact(MLinkedResource pkImpact) {
		this.pkImpactResource = pkImpact;
	}

	public MLinkedResource getPdImpactResource() {
		return pdImpactResource;
	}

	// public String getPdImpactLabel() {
	// return (pdImpactResource!=null) ? pdImpactResource.getLabel():"";
	// }
	public void setPdImpact(MLinkedResource pdImpact) {
		this.pdImpactResource = pdImpact;
	}

	public void setStatementsResource(Set<MLinkedResource> statementsResource) {
		this.statementsResource = statementsResource;
	}

	public MLinkedResource getDoseRecResource() {
		return doseRecResource;
	}

	// public String getDoseRecLabel() {
	// return (doseRecResource!=null) ? doseRecResource.getLabel():"";
	// }
	public void setDoseRec(MLinkedResource doseRec) {
		this.doseRecResource = doseRec;
	}

	public MLinkedResource getDrugRecResource() {
		return drugRecResource;
	}

	// public String getDrugRecLabel() {
	// return (drugRecResource!=null) ? drugRecResource.getLabel():"";
	// }
	public void setDrugRec(MLinkedResource drugRec) {
		this.drugRecResource = drugRec;
	}

	public MLinkedResource getMonitRecResource() {
		return monitRecResource;
	}

	// public String getMonitRecLabel() {
	// return (monitRecResource!=null) ? monitRecResource.getLabel():"";
	// }
	public void setMonitRec(MLinkedResource monitRec) {
		this.monitRecResource = monitRec;
	}

	public MLinkedResource getTestRecResource() {
		return testRecResource;
	}

	// public String getTestRecLabel() {
	// return (testRecResource!=null) ? testRecResource.getLabel():"";
	// }
	public void setTestRec(MLinkedResource testRec) {
		this.testRecResource = testRec;
	}
}
