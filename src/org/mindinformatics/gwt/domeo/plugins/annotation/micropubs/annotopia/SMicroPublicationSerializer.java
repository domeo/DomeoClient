package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.annotopia;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublication;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpQualifier;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpRelationship;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.serializers.AAnnotopiaSerializer;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.serializers.AnnotopiaSerializerManager;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.serializers.IAnnotopiaSerializer;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class SMicroPublicationSerializer extends AAnnotopiaSerializer implements IAnnotopiaSerializer {

	@Override
	public JSONObject serialize(AnnotopiaSerializerManager manager, Object obj) {
		MMicroPublicationAnnotation annotation = (MMicroPublicationAnnotation) obj;
		JSONObject body = new JSONObject();
		body.put(IRdfsOntology.id, new JSONString(annotation.getMicroPublication().getId()));
		body.put(IRdfsOntology.type, new JSONString("mp:Micropublication"));
		body.put(IRdfsOntology.label, new JSONString(annotation.getMicroPublication().getArgues().getText()));

		// Claim or hypothesis
		JSONObject discourseElement = new JSONObject();
		discourseElement.put(IRdfsOntology.id, new JSONString(annotation.getMicroPublication().getArgues().getId()));
		if(annotation.getMicroPublication().getType().equals(MMicroPublication.HYPOTHESIS)) {
			discourseElement.put(IRdfsOntology.type, new JSONString(MMicroPublication.HYPOTHESIS));
		} else {
			discourseElement.put(IRdfsOntology.type, new JSONString(MMicroPublication.CLAIM));
		}
		discourseElement.put("mp:statement", new JSONString(annotation.getMicroPublication().getArgues().getText()));
		
		if(!annotation.getMicroPublication().getQualifiers().isEmpty()) {
			JSONArray qualifiedBy = new JSONArray();
			int counter = 0;
			for(MMpRelationship rel: annotation.getMicroPublication().getQualifiers()) {
				if(rel.getObject() instanceof MMpQualifier) {
					JSONObject q = new JSONObject();
					q.put(IRdfsOntology.id, new JSONString(((MMpQualifier)rel.getObject()).getQualifier().getUrl()));
					q.put(IRdfsOntology.label, new JSONString(((MMpQualifier)rel.getObject()).getQualifier().getLabel()));
					qualifiedBy.set(counter++, q);
					// TODO source?
					//((MMpQualifier)rel.getObject()).getQualifier().getSource()
				}
			}
			discourseElement.put("mp:qualifiedBy", qualifiedBy);
		}
		
		JSONArray asserts = new JSONArray();
		asserts.set(0, discourseElement);

		body.put("mp:argues", new JSONString(annotation.getMicroPublication().getArgues().getId()));
		body.put("mp:asserts", asserts);

		return body;
	}

}
