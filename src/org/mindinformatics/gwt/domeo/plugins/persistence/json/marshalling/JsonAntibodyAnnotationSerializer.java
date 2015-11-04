package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibodyAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * This class provides the serialization aspects peculiar to the Qualifier annotation.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonAntibodyAnnotationSerializer extends JsonAnnotationSerializer {

	private IDomeo _domeo;
	
	public JsonAntibodyAnnotationSerializer(IDomeo domeo) {
		_domeo = domeo;
	}
	
	// TODO HIGH Externalize the strings in variables (DCT?)
	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		_domeo.getLogger().debug(this, "0");
		MAntibodyAnnotation ann = (MAntibodyAnnotation) obj;
		_domeo.getLogger().debug(this, "1");
		JSONObject annotation = initializeAnnotation(manager, ann);
		
		JSONArray entities = new JSONArray();
		_domeo.getLogger().debug(this, "2");
		JSONObject term = new JSONObject();
		term.put(IDomeoOntology.generalId, new JSONString(ann.getAntibodyUsage().getAntibody().getUrl()));
		_domeo.getLogger().debug(this, "2a");
		term.put(IDomeoOntology.generalType, new JSONString(ann.getAntibodyUsage().getAntibody().getType()));
		_domeo.getLogger().debug(this, "2b");
		term.put(IRdfsOntology.rdfLabel, new JSONString(ann.getAntibodyUsage().getAntibody().getLabel()));
		_domeo.getLogger().debug(this, "2c");
		if(ann.getAntibodyUsage().getAntibody().getDescription()!=null) 
			term.put(IDublinCoreTerms.description, new JSONString(ann.getAntibodyUsage().getAntibody().getDescription()));
		if(ann.getAntibodyUsage().getAntibody().getVendor()!=null) 
			term.put("domeo:vendor", new JSONString(ann.getAntibodyUsage().getAntibody().getVendor()));
		if(ann.getAntibodyUsage().getAntibody().getCloneId()!=null) 
			term.put("domeo:cloneId", new JSONString(ann.getAntibodyUsage().getAntibody().getCloneId()));
		if(ann.getAntibodyUsage().getAntibody().getOrganism()!=null) 
			term.put("domeo:organism", new JSONString(ann.getAntibodyUsage().getAntibody().getOrganism()));
		if(ann.getAntibodyUsage().getAntibody().getCatalog()!=null) 
			term.put("domeo:catalog", new JSONString(ann.getAntibodyUsage().getAntibody().getCatalog()));
		
		_domeo.getLogger().debug(this, "3");
		
		//if(ann.getAntibodies().get(i).getSynonyms()!=null) 
		//	term.put(IDomeoOntology.synonyms, new JSONString(ann.getAntibodies().get(i).getSynonyms()));
		// This should be lazy linked and not redaundant
		
		MGenericResource res = ann.getAntibodyUsage().getAntibody().getSource();
		if(res!=null) {
			JSONObject resource = new JSONObject();
			resource.put(IDomeoOntology.generalId, new JSONString(res.getUrl()));
			resource.put(IRdfsOntology.rdfLabel, new JSONString(res.getLabel()));
			term.put(IPavOntology.importedFrom, resource);
		}
		
		_domeo.getLogger().debug(this, "5");
		
		entities.set(0, term);
		JSONObject antibodyUsage = new JSONObject();
		antibodyUsage.put(IDomeoOntology.generalType, new JSONString(MAntibodyAnnotation.BODY_TYPE));
		antibodyUsage.put(IDublinCoreTerms.description, new JSONString(ann.getAntibodyUsage().getComment()));
		antibodyUsage.put("domeo:antibody", entities);
		
		_domeo.getLogger().debug(this, "6");
		
		if(ann.getSubject()!=null) {
			JSONObject species = new JSONObject();
			species.put(IDomeoOntology.generalId, new JSONString(ann.getSubject().getUrl()));
			//species.put(IDomeoOntology.generalType, new JSONString(ann.getSubject().getType()));
			species.put(IRdfsOntology.rdfLabel, new JSONString(ann.getSubject().getLabel()));
			
//			JSONObject rs = new JSONObject();
//			rs.put(IDomeoOntology.generalId, new JSONString(ann.getSubject().getSource().getUrl()));
//			rs.put(IRdfsOntology.label, new JSONString(ann.getSubject().getSource().getLabel()));
//			species.put(IDublinCoreTerms.source, rs);
			
			antibodyUsage.put("domeo:model", species);
		}
		
		_domeo.getLogger().debug(this, "7");
		
		if(ann.getProtocols()!=null && ann.getProtocols().size()>0) {
			JSONArray protocols = new JSONArray();
			int counter = 0;
			for(MLinkedResource prot: ann.getProtocols()) {
				JSONObject protocol = new JSONObject();
				protocol.put(IDomeoOntology.generalId, new JSONString(prot.getUrl()));
				//species.put(IDomeoOntology.generalType, new JSONString(ann.getSubject().getType()));
				protocol.put(IRdfsOntology.rdfLabel, new JSONString(prot.getLabel()));
				protocols.set(counter, protocol);
				counter++;
			}
			antibodyUsage.put("domeo:protocol", protocols);
		}
		
		JSONArray usages = new JSONArray();
		usages.set(0, antibodyUsage);
		
		annotation.put(IDomeoOntology.content, usages);
		return annotation;
	}
}
