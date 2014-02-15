package org.mindinformatics.gwt.domeo.plugins.annotation.spls.serialization;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.IPharmgxOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonAnnotationSerializer;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsonSerializerManager;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.src.UUID;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;



/**
 * This class provides the serialization aspects peculiar to the SPLs pharmacogenomics annotation.
 * 
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class JsonSPLsAnnotationSerializer extends JsonAnnotationSerializer {

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		//TODO: use the IPharmgxOntology for these 
		String SPL_URN_PREFIX = "urn:linkedspls:uuid:";
		String SPL_POC_PREFIX = "poc:";
		String SIO_PREFIX = "sio:";
		
		MSPLsAnnotation ann = (MSPLsAnnotation) obj;
		JSONObject annotation = initializeAnnotation(manager, ann);
		manager._domeo.getLogger().debug(this, "Initialized JSON annotation object");

		// Body creation
		JSONArray bodies = new JSONArray();
		JSONObject body = new JSONObject();
		
		// content serialization 
		String statementUUID = UUID.uuid();
		body.put("@id", new JSONString(SPL_URN_PREFIX + statementUUID));
		body.put("@type", new JSONString(SPL_POC_PREFIX + "PharmacogenomicsStatement"));
		
		//TODO: fix this to use the SPL ontology 
		//TODO; create resources that include all of the resources in the model. Make concordant with the USPLJsonUnmarshaller class where the data gets unmarshalled
		MLinkedResource pkImpact = ann.getPKImpact(); 
		if (pkImpact != null){
			body.put(SIO_PREFIX + "SIO_000563", new JSONString(SPL_POC_PREFIX + "PharmacokineticImpact"));
			body.put(SPL_POC_PREFIX + "PharmacokineticImpact", new JSONString(pkImpact.getUrl()));
			body.put(IRdfsOntology.label, new JSONString(pkImpact.getLabel()));
			body.put(IDublinCoreTerms.description, new JSONString(pkImpact.getDescription()));			
		}
		
		bodies.set(0, body); 
		annotation.put(IDomeoOntology.content, bodies);
		//annotation.put(IDomeoOntology.content, new JSONString(ann.getText()));
		return annotation;
	}
}
