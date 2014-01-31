package org.mindinformatics.gwt.services.pubmed.src;

import java.util.Date;

import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.extractors.PubMedExtractSubjectCommand;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.identities.EPubMedDatabase;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service.PubMedService;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class PubMedServiceImpl extends RemoteServiceServlet implements PubMedService {

	@Override
	public MPublicationArticleReference getBibliographicObject(
			String typeQuery, String textQuery) throws IllegalArgumentException {
		
		if(typeQuery.equals(PubMedExtractSubjectCommand.PUBMED_IDS)) {
			if(textQuery.equals("10679938")) {
				MPublicationArticleReference reference2 = new MPublicationArticleReference();
				//reference2.setId(id);
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
		
		throw new IllegalArgumentException("No entry in PubMed with ID: " + textQuery);
	}

}
