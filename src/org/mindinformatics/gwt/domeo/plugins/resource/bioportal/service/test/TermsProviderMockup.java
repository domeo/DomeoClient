package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.framework.component.agents.model.MAgent;
import org.mindinformatics.gwt.framework.component.agents.src.AgentsFactory;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTrustedResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTrustedTypedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;

public class TermsProviderMockup {

	public static ArrayList<MLinkedResource> getTerms() {
		ArrayList<MLinkedResource> terms = new ArrayList<MLinkedResource>();
		
		AgentsFactory factory = new AgentsFactory();
		
		MAgent agent = factory.createAgentSoftware("Ontology", "http://example/ontology/1", "1");
		agent.setUrl("http://example/ontology/1");
		agent.setLabel("Ontology");
		
		
		MTrustedResource termA = ResourcesFactory.createTrustedResource("http://example.org/term/protein/protein", "Protein", agent);
		termA.setDescription("Protein");
		terms.add(termA);
		
		MTrustedResource termB = ResourcesFactory.createTrustedResource("http://example.org/term/gene/gene", "Gene", agent);
		termB.setDescription("Gene");
		terms.add(termB);
		
		Set<String> typesA = new HashSet<String>();
		typesA.add(termA.getUrl());
		
		MTrustedTypedResource term1 = ResourcesFactory.createTrustedTypedResource("http://example.org/term/protein/app", "APP", agent);
		term1.setDescription("Amyloyd Precursor Protein");
		term1.setTypesUris(typesA);
		terms.add(term1);
		
		MTrustedTypedResource term2 = ResourcesFactory.createTrustedTypedResource("http://example.org/term/protein/bace1","BACE1",agent);
		term2.setDescription("Beta Secretase 1");
		term2.setSource(agent);
		term2.setTypesUris(typesA);
		terms.add(term2);
		
		MTrustedTypedResource term4 = ResourcesFactory.createTrustedTypedResource("http://example.org/term/protein/bace1","BACE1",agent);
		term4.setDescription("Beta Secretase 1");
		term4.setTypesUris(typesA);
		terms.add(term4);
		
		Set<String> typesB = new HashSet<String>();
		typesB.add(termB.getUrl());
		
		MTrustedTypedResource term3 = ResourcesFactory.createTrustedTypedResource("http://example.org/term/gene/bace1","BACE1 Gene",agent);
		term3.setDescription("Beta Secretase 1 Gene");
		term2.setTypesUris(typesB);
		terms.add(term3);
		
		/*
		MClassIdentifiedByUri[] domain0 = new MClassIdentifiedByUri[1];
		domain0[0] = termB;
		MClassIdentifiedByUri[] range0 = new MClassIdentifiedByUri[1];
		range0[0] = termA;
		
		MRelationshipIdentifiedByUri rel0 = new MRelationshipIdentifiedByUri();
		rel0.setUrl("http://example.org/term/rel/expresses");
		rel0.setLabel("Expressed in");
		rel0.setDescription("Expressed in (gene -> protein)");
		rel0.setSource(agent);
		rel0.setDomain(domain0);
		rel0.setRange(range0);
		terms.add(rel0);
		*/
		
		return terms;
	}
}
