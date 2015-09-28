package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.annotopia;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublication;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpQualifier;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpReference;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpRelationship;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.serializers.AAnnotopiaSerializer;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.serializers.AnnotopiaSerializerManager;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.serializers.IAnnotopiaSerializer;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.lenses.PubMedCitationPainter;

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
			discourseElement.put(IRdfsOntology.type, new JSONString(IMicroPublicationsOntology.mpHypothesis));
		} else {
			discourseElement.put(IRdfsOntology.type, new JSONString(IMicroPublicationsOntology.mpClaim));
		}
		discourseElement.put(IMicroPublicationsOntology.mpStatement, new JSONString(annotation.getMicroPublication().getArgues().getText()));
		
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
			discourseElement.put(IMicroPublicationsOntology.mpQualifiedBy, qualifiedBy);
		}
		
		JSONArray asserts = new JSONArray();
		asserts.set(asserts.size(), discourseElement);

		if(!annotation.getMicroPublication().getEvidence().isEmpty()) {
			JSONArray supportedBy = new JSONArray();
			JSONArray challengedBy = new JSONArray();
			for(MMpRelationship rel: annotation.getMicroPublication().getEvidence()) {
				if(rel.getName().equals(IMicroPublicationsOntology.mpSupportedBy)) {
					if(rel.getObjectElement()  instanceof MMpReference) {
						MMpReference mMpReference = (MMpReference) rel.getObjectElement();
						JSONObject reference = new JSONObject();
						if(!mMpReference.getReference().getPubMedId().isEmpty()) {
							reference.put(IRdfsOntology.id, new JSONString("urn:pubmed:" + mMpReference.getReference().getPubMedId()));
						}
						reference.put(IRdfsOntology.type, new JSONString(IMicroPublicationsOntology.mpReference));
						reference.put(IMicroPublicationsOntology.mpCitation, new JSONString(PubMedCitationPainter.getFullCitationPlainString(mMpReference.getReference(), manager._domeo)));
						manager.serializeExpression(reference, new JSONString("urn:pubmed:" + mMpReference.getReference().getPubMedId() + ":expr"), mMpReference.getReference());
						supportedBy.set(supportedBy.size(), reference);
						asserts.set(asserts.size(), reference);
					}
				} else if(rel.getName().equals(IMicroPublicationsOntology.mpChallengedBy)) {
					if(rel.getObjectElement()  instanceof MMpReference) {
						MMpReference mMpReference = (MMpReference) rel.getObjectElement();
						JSONObject reference = new JSONObject();
						if(!mMpReference.getReference().getPubMedId().isEmpty()) {
							reference.put(IRdfsOntology.id, new JSONString("urn:pubmed:" + mMpReference.getReference().getPubMedId()));
						}
						reference.put(IRdfsOntology.type, new JSONString(IMicroPublicationsOntology.mpReference));
						reference.put(IMicroPublicationsOntology.mpCitation, new JSONString(PubMedCitationPainter.getFullCitationPlainString(mMpReference.getReference(), manager._domeo)));
						manager.serializeExpression(reference, new JSONString("urn:pubmed:" + mMpReference.getReference().getPubMedId() + ":expr"), mMpReference.getReference());
						challengedBy.set(challengedBy.size(), reference);
						asserts.set(asserts.size(), reference);
					}
				}
			}	

			body.put(IMicroPublicationsOntology.mpArgues, new JSONString(annotation.getMicroPublication().getArgues().getId()));
			body.put(IMicroPublicationsOntology.mpAsserts, asserts);
			
			if(supportedBy.size()>0) {
				discourseElement.put(IMicroPublicationsOntology.mpSupportedBy, supportedBy);
			}
			if(challengedBy.size()>0) {
				discourseElement.put(IMicroPublicationsOntology.mpChallengedBy, supportedBy);
			}
		}
		

		return body;
	}

}
