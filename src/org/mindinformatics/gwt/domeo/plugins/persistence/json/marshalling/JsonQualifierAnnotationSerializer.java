package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * This class provides the serialization aspects peculiar to the Qualifier annotation.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonQualifierAnnotationSerializer extends JsonAnnotationSerializer {

	// TODO HIGH Externalize the strings in variables (DCT?)
	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MQualifierAnnotation ann = (MQualifierAnnotation) obj;
		JSONObject annotation = initializeAnnotation(manager, ann);
		JSONArray semanticTags = new JSONArray();
		for(int i=0; i<ann.getTerms().size(); i++) {
			JSONObject term = new JSONObject();
			term.put(IDomeoOntology.generalId, new JSONString(ann.getTerms().get(i).getUrl()));
			term.put(IRdfsOntology.rdfLabel, new JSONString(ann.getTerms().get(i).getLabel()));
			if(ann.getTerms().get(i).getDescription()!=null) 
				term.put(IDublinCoreTerms.description, new JSONString(ann.getTerms().get(i).getDescription()));
			if(ann.getTerms().get(i).getSynonyms()!=null) 
				term.put(IDomeoOntology.synonyms, new JSONString(ann.getTerms().get(i).getSynonyms()));
			
			// This should be lazy linked and not redaundant
			JSONObject resource = new JSONObject();
			MGenericResource res = ann.getTerms().get(i).getSource();
			resource.put(IDomeoOntology.generalId, new JSONString(res.getUrl()));
			resource.put(IRdfsOntology.rdfLabel, new JSONString(res.getLabel()));
			term.put(IDublinCoreTerms.source, resource);
			semanticTags.set(i, term);
		}
		annotation.put(IDomeoOntology.semanticTag, semanticTags);
		return annotation;
	}
}
