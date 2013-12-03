package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingBibliographySetHandler;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors.PubMedExtractSubjectCommand;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.identities.EPubMedDatabase;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.IPubMedConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.IPubMedItemsRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.IPubMedPaginatedItemsRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2.PubMedCentralExtractReferencesCommand;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class StandalonePubMedConnector implements IPubMedConnector {
	
	@Override
	public void retrieveExistingBibliographySet(IRetrieveExistingBibliographySetHandler handler, int level) {
		
	}
	
	@Override
	public void getBibliographicObject(IPubMedItemsRequestCompleted completionCallback, String typeQuery, String textQuery) throws IllegalArgumentException {
	
		ArrayList<MPublicationArticleReference> items = new ArrayList<MPublicationArticleReference>();
		MPublicationArticleReference item = getPublicationArticleReference(typeQuery, textQuery);
		if(item!=null) {
			items.add(item);
			completionCallback.returnBibliographicObject(items);
			return;
		}
		completionCallback.returnBibliographicObject(items);
	}

	@Override
	public void getBibliographicObjects(
			IPubMedItemsRequestCompleted completionCallback, String typeQuery,
			List<String> textQuery, List<String> elements)
			throws IllegalArgumentException {
		ArrayList<MPublicationArticleReference> references = new ArrayList<MPublicationArticleReference>();
		for(int i=0; i<textQuery.size(); i++) {
			MPublicationArticleReference  item = getPublicationArticleReference(typeQuery, textQuery.get(i));
			if(item!=null) references.add(item);
			else {
				MPublicationArticleReference reference = new MPublicationArticleReference();
				reference.setUnrecognized(elements.get(i));
				references.add(reference);
			}
		}		
		completionCallback.returnBibliographicObject(references);
	}
	
	@Override
	public void searchBibliographicObjects(IPubMedPaginatedItemsRequestCompleted completionCallback, String typeQuery, final String textQuery, int maxResults, int offset) throws IllegalArgumentException {

		ArrayList<MPublicationArticleReference> references = new ArrayList<MPublicationArticleReference>();
		
		if(typeQuery.equals(PubMedExtractSubjectCommand.TITLE)) {
			if(textQuery.equals("test")) {
				MPublicationArticleReference reference2 = new MPublicationArticleReference();
				//reference2.setId(id);
				reference2.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/10679938");
				reference2.setDoi("10.1002/(SICI)1098-1004(200003)15:3<228::AID-HUMU3>3.0.CO;2-9");
				reference2.setPublisherItemId("10.1002/(SICI)1098-1004(200003)15:3<228::AID-HUMU3>3.0.CO;2-9");
				reference2.setTitle("An update of the mutation spectrum of the survival motor neuron gene " +
						"(SMN1) in autosomal recessive spinal muscular atrophy (SMA).");
				reference2.setAuthorNames("Wirth B");
				reference2.setCreationDate(new Date());
				reference2.setJournalPublicationInfo("Hum Mutat. 2000;15(3):228-37.");
				reference2.setPubMedCentralId("");
				reference2.setPubMedId("10679938");
				reference2.setPublicationType("Journal Article");
				reference2.setJournalName("Human mutation");
				reference2.setPublicationDate("2000");
				reference2.setSource(EPubMedDatabase.getInstance());
				references.add(reference2);
				
				MPublicationArticleReference reference3 = new MPublicationArticleReference();
				//reference3.setId(id);
				reference3.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/17561409");
				reference3.setDoi("10.1016/j.nbd.2007.04.009");
				reference3.setPublisherItemId("S0969-9961(07)00090-3");
				reference3.setTitle("Abnormal motor phenotype in the SMNDelta7 mouse model of spinal muscular atrophy.");
				reference3.setAuthorNames("Matthew ER Butchbach, Jonathan D Edwards and Arthur HM Burghes");
				reference3.setCreationDate(new Date());
				reference3.setJournalPublicationInfo("Neurobiol Dis. 2007 Aug;27(2):207-19. Epub 2007 May 5.");
				reference3.setPubMedCentralId("PMC2700002");
				reference3.setPubMedId("17561409");
				reference3.setPublicationType("Journal Article");
				reference3.setJournalName("Neurobiology of disease");
				reference3.setSource(EPubMedDatabase.getInstance());
				references.add(reference3);
			}
		}

		completionCallback.returnBibliographicObject(2, 0, 2, references);
	}
	
//	@Override
//	public void getBibliographicObjects(IPubMedItemsRequestCompleted completionCallback, String typeQuery, final List<String> textQuery,
//			List<Element> elements) throws IllegalArgumentException {
//
//		ArrayList<MPublicationArticleReference> references = new ArrayList<MPublicationArticleReference>();
//		
//		if(typeQuery.equals(PubMedExtractSubjectCommand.TITLE_AND_ABSTRACT)) {
//			if(textQuery.equals("test")) {
//				MPublicationArticleReference reference2 = new MPublicationArticleReference();
//				//reference2.setId(id);
//				reference2.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/10679938");
//				reference2.setDoi("10.1002/(SICI)1098-1004(200003)15:3<228::AID-HUMU3>3.0.CO;2-9");
//				reference2.setPublisherItemId("10.1002/(SICI)1098-1004(200003)15:3<228::AID-HUMU3>3.0.CO;2-9");
//				reference2.setTitle("An update of the mutation spectrum of the survival motor neuron gene " +
//						"(SMN1) in autosomal recessive spinal muscular atrophy (SMA).");
//				reference2.setAuthorNames("Wirth B");
//				reference2.setCreationDate(new Date());
//				reference2.setJournalPublicationInfo("Hum Mutat. 2000;15(3):228-37.");
//				reference2.setPubMedCentralId("");
//				reference2.setPubMedId("10679938");
//				reference2.setPublicationType("Journal Article");
//				reference2.setPublisher("Human mutation");
//				reference2.setPublicationDate("2000");
//				reference2.setSource(EPubMedDatabase.getInstance());
//				references.add(reference2);
//				
//				MPublicationArticleReference reference3 = new MPublicationArticleReference();
//				//reference3.setId(id);
//				reference3.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/17561409");
//				reference3.setDoi("10.1016/j.nbd.2007.04.009");
//				reference3.setPublisherItemId("S0969-9961(07)00090-3");
//				reference3.setTitle("Abnormal motor phenotype in the SMNDelta7 mouse model of spinal muscular atrophy.");
//				reference3.setAuthorNames("Matthew ER Butchbach, Jonathan D Edwards and Arthur HM Burghes");
//				reference3.setCreationDate(new Date());
//				reference3.setJournalPublicationInfo("Neurobiol Dis. 2007 Aug;27(2):207-19. Epub 2007 May 5.");
//				reference3.setPubMedCentralId("PMC2700002");
//				reference3.setPubMedId("17561409");
//				reference3.setPublicationType("Journal Article");
//				reference3.setPublisher("Neurobiology of disease");
//				reference3.setSource(EPubMedDatabase.getInstance());
//				references.add(reference3);
//			}
//			return;
//		}
//		
//		for(int i=0; i<textQuery.size(); i++) {
//			MPublicationArticleReference reference = new MPublicationArticleReference();
//			reference.setUnrecognized(elements.get(i).getInnerText());
//			references.add(reference);
//		}
//		
//		completionCallback.returnBibliographicObject(references);
//	}
	
//	@Override
//	public void searchBibliographicObject(IPubMedItemsRequestCompleted completionCallback,
//			String typeQuery, String textQuery) throws IllegalArgumentException {
//		ArrayList<MPublicationArticleReference> results = new ArrayList<MPublicationArticleReference>();
//		MPublicationArticleReference result = getPublicationArticleReference(typeQuery, textQuery);
//		if(result!=null) results.add(result);
//		completionCallback.returnBibliographicObject(results);
//	}
	
	private MPublicationArticleReference getPublicationArticleReference(String typeQuery, String textQuery) {
		if(typeQuery.equals(PubMedExtractSubjectCommand.PUBMED_IDS)) {
			if(textQuery.equals(PubMedCentralExtractReferencesCommand.UNRECOGNIZED)) {
				return null;
			} else if(textQuery.equals("10679938")) {
				MPublicationArticleReference reference2 = new MPublicationArticleReference();
				//reference2.setId(id);
				reference2.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/10679938");
				reference2.setDoi("10.1002/(SICI)1098-1004(200003)15:3<228::AID-HUMU3>3.0.CO;2-9");
				reference2.setPublisherItemId("10.1002/(SICI)1098-1004(200003)15:3<228::AID-HUMU3>3.0.CO;2-9");
				reference2.setTitle("An update of the mutation spectrum of the survival motor neuron gene " +
						"(SMN1) in autosomal recessive spinal muscular atrophy (SMA).");
				reference2.setAuthorNames("Wirth B");
				reference2.setCreationDate(new Date());
				reference2.setJournalPublicationInfo("Hum Mutat. 2000;15(3):228-37.");
				reference2.setPubMedCentralId("");
				reference2.setPubMedId("10679938");
				reference2.setPublicationType("Journal Article");
				reference2.setJournalName("Human mutation");
				reference2.setSource(EPubMedDatabase.getInstance());
				return reference2;
			} else if(textQuery.equals("17561409")) {
				MPublicationArticleReference reference3 = new MPublicationArticleReference();
				//reference3.setId(id);
				reference3.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/17561409");
				reference3.setDoi("10.1016/j.nbd.2007.04.009");
				reference3.setPublisherItemId("S0969-9961(07)00090-3");
				reference3.setTitle("Abnormal motor phenotype in the SMNDelta7 mouse model of spinal muscular atrophy.");
				reference3.setAuthorNames("Matthew ER Butchbach, Jonathan D Edwards and Arthur HM Burghes");
				reference3.setCreationDate(new Date());
				reference3.setJournalPublicationInfo("Neurobiol Dis. 2007 Aug;27(2):207-19. Epub 2007 May 5.");
				reference3.setPubMedCentralId("PMC2700002");
				reference3.setPubMedId("17561409");
				reference3.setPublicationType("Journal Article");
				reference3.setJournalName("Neurobiology of disease");
				reference3.setSource(EPubMedDatabase.getInstance());
				return reference3;
			} else if(textQuery.equals("11555628")) {
				MPublicationArticleReference reference3 = new MPublicationArticleReference();
				//reference3.setId(id);
				reference3.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/11555628");
				reference3.setDoi("");
				reference3.setPublisherItemId("");
				reference3.setTitle("Neurodevelopmental delay, motor abnormalities and cognitive deficits in transgenic mice overexpressing Dyrk1A (minibrain), a murine model of Down's syndrome.");
				reference3.setAuthorNames("Altafaj X, Dierssen M, Baamonde C, Mart’ E, Visa J, Guimerˆ J, Oset M, Gonz‡lez JR, Fl—rez J, Fillat C, Estivill X");
				reference3.setCreationDate(new Date());
				reference3.setJournalPublicationInfo("Hum Mol Genet. 2001 Sep 1;10(18):1915-23.");
				reference3.setPubMedCentralId("");
				reference3.setPubMedId("11555628");
				reference3.setPublicationType("Journal Article");
				reference3.setJournalName("Human molecular genetics");
				reference3.setSource(EPubMedDatabase.getInstance());
				return reference3;
			} else if(textQuery.equals("15548226")) {
				MPublicationArticleReference reference3 = new MPublicationArticleReference();
				//reference3.setId(id);
				reference3.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/11555628");
				reference3.setDoi("10.1111/j.1460-9568.2004.03745.x");
				reference3.setPublisherItemId("EJN3745");
				reference3.setTitle("Altered sensorimotor development in a transgenic mouse model of amyotrophic lateral sclerosis.");
				reference3.setAuthorNames("Amendola J, Verrier B, Roubertoux P, Durand J.");
				reference3.setCreationDate(new Date());
				reference3.setJournalPublicationInfo("Eur J Neurosci. 2004 Nov;20(10):2822-6.");
				reference3.setPubMedCentralId("");
				reference3.setPubMedId("15548226");
				reference3.setPublicationType("Journal Article");
				reference3.setJournalName("The European journal of neuroscience");
				reference3.setSource(EPubMedDatabase.getInstance());
				return reference3;
			} else if(textQuery.equals("8995582")) {
				MPublicationArticleReference reference3 = new MPublicationArticleReference();
				//reference3.setId(id);
				reference3.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/8995582");
				reference3.setDoi("");
				reference3.setPublisherItemId("10.1002/(SICI)1097-4598(199701)20:1<45::AID-MUS6>3.0.CO;2-H");
				reference3.setTitle("Progressive motor neuron impairment in an animal model of familial amyotrophic lateral sclerosis.");
				reference3.setAuthorNames("Azzouz M, Leclerc N, Gurney M, Warter JM, Poindron P, Borg J.");
				reference3.setCreationDate(new Date());
				reference3.setJournalPublicationInfo("Muscle Nerve. 1997 Jan;20(1):45-51.");
				reference3.setPubMedCentralId("");
				reference3.setPubMedId("8995582");
				reference3.setPublicationType("Journal Article");
				reference3.setJournalName("Muscle & nerve");
				reference3.setSource(EPubMedDatabase.getInstance());
				return reference3;
			} else if(textQuery.equals("9052802")) {
				MPublicationArticleReference reference3 = new MPublicationArticleReference();
				//reference3.setId(id);
				reference3.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/9052802");
				reference3.setDoi("");
				reference3.setPublisherItemId("S0896-6273(00)80272-X");
				reference3.setTitle("ALS-linked SOD1 mutant G85R mediates damage to astrocytes and promotes rapidly progressive disease with SOD1-containing inclusions.");
				reference3.setAuthorNames("Bruijn LI, Becher MW, Lee MK, Anderson KL, Jenkins NA, Copeland NG, Sisodia SS, Rothstein JD, Borchelt DR, Price DL, Cleveland DW.");
				reference3.setCreationDate(new Date());
				reference3.setJournalPublicationInfo("Neuron. 1997 Feb;18(2):327-38.");
				reference3.setPubMedCentralId("");
				reference3.setPubMedId("9052802");
				reference3.setPublicationType("Journal Article");
				reference3.setJournalName("Neuron");
				reference3.setSource(EPubMedDatabase.getInstance());
				return reference3;
			} else if(textQuery.equals("17161463")) {
				MPublicationArticleReference reference3 = new MPublicationArticleReference();
				//reference3.setId(id);
				reference3.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/9052802");
				reference3.setDoi("10.1016/j.jneumeth.2006.11.002");
				reference3.setPublisherItemId("S0165-0270(06)00545-0");
				reference3.setTitle("A novel method for oral delivery of drug compounds to the neonatal SMNDelta7 mouse model of spinal muscular atrophy.");
				reference3.setAuthorNames("Butchbach ME, Edwards JD, Schussler KR, Burghes AH.");
				reference3.setCreationDate(new Date());
				reference3.setJournalPublicationInfo("J Neurosci Methods. 2007 Apr 15;161(2):285-90. Epub 2006 Dec 11.");
				reference3.setPubMedCentralId("PMC2699996");
				reference3.setPubMedId("17161463");
				reference3.setPublicationType("Journal Article");
				reference3.setJournalName("Journal of neuroscience methods");
				reference3.setSource(EPubMedDatabase.getInstance());
				return reference3;
			} else if(textQuery.equals("17065443")) {
				MPublicationArticleReference reference3 = new MPublicationArticleReference();
				//reference3.setId(id);
				reference3.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/17065443");
				reference3.setDoi("10.1523/JNEUROSCI.1637-06.2006");
				reference3.setPublisherItemId("26/43/11014");
				reference3.setTitle("Survival motor neuron function in motor axons is independent of functions required for small nuclear ribonucleoprotein biogenesis.");
				reference3.setAuthorNames("Carrel TL, McWhorter ML, Workman E, Zhang H, Wolstencroft EC, Lorson C, Bassell GJ, Burghes AH, Beattie CE.");
				reference3.setCreationDate(new Date());
				reference3.setJournalPublicationInfo("J Neurosci. 2006 Oct 25;26(43):11014-22.");
				reference3.setPubMedCentralId("");
				reference3.setPubMedId("17065443");
				reference3.setPublicationType("Journal Article");
				reference3.setJournalName("The Journal of neuroscience : the official journal of the Society for Neuroscience");
				reference3.setSource(EPubMedDatabase.getInstance());
				return reference3;
			} else if(textQuery.equals("8846004")) {
				MPublicationArticleReference reference3 = new MPublicationArticleReference();
				//reference3.setId(id);
				reference3.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/8846004");
				reference3.setDoi("10.1006/mcne.1995.1027");
				reference3.setPublisherItemId("S1044-7431(85)71027-5");
				reference3.setTitle("Age-dependent penetrance of disease in a transgenic mouse model of familial amyotrophic lateral sclerosis.");
				reference3.setAuthorNames("Chiu AY, Zhai P, Dal Canto MC, Peters TM, Kwon YW, Prattis SM, Gurney ME.");
				reference3.setCreationDate(new Date());
				reference3.setJournalPublicationInfo("Mol Cell Neurosci. 1995 Aug;6(4):349-62.");
				reference3.setPubMedCentralId("");
				reference3.setPubMedId("8846004");
				reference3.setPublicationType("Journal Article");
				reference3.setJournalName("Molecular and cellular neurosciences");
				reference3.setSource(EPubMedDatabase.getInstance());
				return reference3;
			} else if(textQuery.equals("12023986")) {
				MPublicationArticleReference reference3 = new MPublicationArticleReference();
				//reference3.setId(id);
				reference3.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/12023986");
				reference3.setDoi("");
				reference3.setPublisherItemId("");
				reference3.setTitle("Neurofilament accumulation at the motor endplate and lack of axonal sprouting in a spinal muscular atrophy mouse model.");
				reference3.setAuthorNames("Cifuentes-Diaz C, Nicole S, Velasco ME, Borra-Cebrian C, Panozzo C, Frugier T, Millet G, Roblot N, Joshi V, Melki J.");
				reference3.setCreationDate(new Date());
				reference3.setJournalPublicationInfo("Hum Mol Genet. 2002 Jun 1;11(12):1439-47.");
				reference3.setPubMedCentralId("");
				reference3.setPubMedId("12023986");
				reference3.setPublicationType("Journal Article");
				reference3.setJournalName("Human molecular genetics");
				reference3.setSource(EPubMedDatabase.getInstance());
				return reference3;
			} else if(textQuery.equals("9259265")) {
				MPublicationArticleReference reference3 = new MPublicationArticleReference();
				//reference3.setId(id);
				reference3.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/9259265");
				reference3.setDoi("10.1093/hmg/11.12.1439");
				reference3.setPublisherItemId("dda174");
				reference3.setTitle("The survival motor neuron protein in spinal muscular atrophy.");
				reference3.setAuthorNames("Coovert DD, Le TT, McAndrew PE, Strasswimmer J, Crawford TO, Mendell JR, Coulson SE, Androphy EJ, Prior TW, Burghes AH.");
				reference3.setCreationDate(new Date());
				reference3.setJournalPublicationInfo("Hum Mol Genet. 1997 Aug;6(8):1205-14.");
				reference3.setPubMedCentralId("");
				reference3.setPubMedId("9259265");
				reference3.setPublicationType("Journal Article");
				reference3.setJournalName("Human molecular genetics");
				reference3.setSource(EPubMedDatabase.getInstance());
				return reference3;
			}
		} else if(typeQuery.equals(PubMedExtractSubjectCommand.PUBMED_CENTRAL_IDS)) {
			if (textQuery.equals("PMC2759694")) {
				MPublicationArticleReference reference3 = new MPublicationArticleReference();
				//reference3.setId(id);
				reference3.setUrl("http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2759694/");
				reference3.setDoi("10.1523/JNEUROSCI.4872-08.2009");
				reference3.setPublisherItemId("29/23/7459");
				reference3.setTitle("Interaction of reelin with amyloid precursor protein promotes neurite outgrowth.");
				reference3.setAuthorNames("Hoe HS, Lee KJ, Carney RS, Lee J, Markova A, Lee JY, Howell BW, Hyman BT, Pak DT, Bu G, Rebeck GW.");
				reference3.setCreationDate(new Date());
				reference3.setJournalPublicationInfo("J Neurosci. 2009 June 10; 29(23): 7459-7473.");
				reference3.setPubMedCentralId("PMC2759694");
				reference3.setPubMedId("19515914");
				reference3.setPublicationType("Journal Article");
				reference3.setJournalName("The Journal of neuroscience : the official journal of the Society for Neuroscience");
				reference3.setSource(EPubMedDatabase.getInstance());
				return reference3;
			} else if(textQuery.equals("PMC2700002")) {
				MPublicationArticleReference reference3 = new MPublicationArticleReference();
				//reference3.setId(id);
				reference3.setUrl("http://www.ncbi.nlm.nih.gov/pubmed/17561409");
				reference3.setDoi("10.1016/j.nbd.2007.04.009");
				reference3.setPublisherItemId("S0969-9961(07)00090-3");
				reference3.setTitle("Abnormal motor phenotype in the SMNDelta7 mouse model of spinal muscular atrophy.");
				reference3.setAuthorNames("Matthew ER Butchbach, Jonathan D Edwards and Arthur HM Burghes");
				reference3.setCreationDate(new Date());
				reference3.setJournalPublicationInfo("Neurobiol Dis. 2007 Aug;27(2):207-19. Epub 2007 May 5.");
				reference3.setPubMedCentralId("PMC2700002");
				reference3.setPubMedId("17561409");
				reference3.setPublicationType("Journal Article");
				reference3.setJournalName("Neurobiology of disease");
				reference3.setSource(EPubMedDatabase.getInstance());
				return reference3;
			} 
		} 	
		return null;
	}

	@Override
	public void retrieveExistingBibliographySetByUrl(
			IRetrieveExistingBibliographySetHandler handler, String url)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}
}
