package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.serialization;

import java.util.Set;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Iexpertstudy_pDDI;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonAnnotationSerializer;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonSerializerManager;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.src.UUID;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * This class provides the serialization aspects peculiar to the DDI annotation.
 * 
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class Jsonexpertstudy_pDDIAnnotationSerializer extends
		JsonAnnotationSerializer implements Iexpertstudy_pDDI {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {

		Mexpertstudy_pDDIAnnotation ann = (Mexpertstudy_pDDIAnnotation) obj;
		JSONObject annotation = initializeAnnotation(manager, ann);
		manager._domeo.getLogger().debug(this,
				"Initialized JSON annotation object");

		// Body creation
		JSONArray bodies = new JSONArray();
		Integer idx = 0;

		// TODO; create resources that include all of the resources in the
		// model. Make concordant with the JsonUnmarshaller class where the
		// data gets unmarshalled

		// type of drug 1
		MLinkedResource drug1 = ann.getDrug1();
		
		if (drug1 != null) {
			JSONObject body = new JSONObject(); 
			String statementUUID = UUID.uuid(); 
			body.put("@id", new JSONString(PDDI_URN_PREFIX + statementUUID)); 
			body.put("@type", new JSONString("poc:"
					+ "DrugDrugInteractionStatement")); 
			
			body.put("sio:" + "SIO_000628", new JSONString("rxnorm:"
					+ "drug1"));
			
			body.put("rxnorm:" + "drug1",
					new JSONString(drug1.getUrl()));
			
			body.put(IRdfsOntology.label, new JSONString(drug1.getLabel()));
			body.put(IDublinCoreTerms.description,
					new JSONString(drug1.getDescription()));
			
			
			bodies.set(idx, body);
			idx += 1;
		}

		
		MLinkedResource drug2 = ann.getDrug1();
		if (drug2 != null) {
			JSONObject body = new JSONObject(); 
			String statementUUID = UUID.uuid(); 
			body.put("@id", new JSONString(PDDI_URN_PREFIX + statementUUID)); 
			body.put("@type", new JSONString("poc:"
					+ "DrugDrugInteractionStatement")); 
			
			body.put("sio:" + "SIO_000628", new JSONString("rxnorm:"
					+ "drug2"));
			
			body.put("rxnorm:" + "drug2",
					new JSONString(drug2.getUrl()));
			
			body.put(IRdfsOntology.label, new JSONString(drug2.getLabel()));
			body.put(IDublinCoreTerms.description,
					new JSONString(drug2.getDescription()));
			
			
			bodies.set(idx, body);
			idx += 1;
		}
		
		
		annotation.put(IDomeoOntology.content, bodies);

		return annotation;
	}

}
