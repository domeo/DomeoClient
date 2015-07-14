package org.mindinformatics.gwt.framework.component.resources.serialization;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDublinCoreTerms;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTrustedResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTrustedTypedResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTypedResource;
import org.mindinformatics.gwt.framework.src.Utils;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonResourcesSerializer {
	
	/**
	 * Serializes a generic source starting from the GenericResource instance
	 * @param genericResource Instance to be serialized
	 * @return The JSON representation of the GenericResource instance
	 */
	public static JSONObject serialize(MGenericResource genericResource) {
		return marshallGenericResource(genericResource.getUrl(), genericResource.getLabel());
	}
	private static JSONObject marshallGenericResource(String url, String label) {
		Utils.isValidUrl(url, true);
		JSONObject resource = new JSONObject();
		resource.put(IDomeoOntology.generalId, new JSONString(url));
		if(label!=null && label.trim().length()>0) 
			resource.put(IRdfsOntology.label, new JSONString(label));
		return resource;
	}
	
	/**
	 * Serializes a linked resource.
	 * @param linkedResource	Instance to be serialized
	 * @return The JSON representation of the MLinkedResource instance
	 */
	public static JSONObject serialize(MLinkedResource linkedResource) {
		return marshallLinkedResource(linkedResource);
	}
	private static JSONObject marshallLinkedResource(MLinkedResource linkedResource) {
		JSONObject resource = marshallGenericResource(linkedResource.getUrl(), linkedResource.getLabel());
		resource.put(IDublinCoreTerms.description, new JSONString(linkedResource.getDescription()));
		return resource;
	}
	
	/**
	 * Serializes a trusted resource, a linked resource with a source.
	 * @param trustedResource	Instance to be serialized
	 * @return The JSON representation of the MTrustedResource instance
	 */
	public static JSONObject serialize(MTrustedResource trustedResource) {
		return marshallTrustedResource(trustedResource);
	}	
	private static JSONObject marshallTrustedResource(MTrustedResource trustedResource) {
		JSONObject resource = marshallLinkedResource(trustedResource);
		if(trustedResource.getSource()!=null) { 
			resource.put(IDublinCoreTerms.source, 
				marshallGenericResource(
						trustedResource.getSource().getUrl(), 
						trustedResource.getSource().getLabel()));
		}
		return resource;
	}
	
	/**
	 * Serializes a typed resource, a linked resource with a type.
	 * @param typedResource	Instance to be serialized
	 * @return The JSON representation of the MTypedResource instance
	 */
	public static JSONObject serialize(MTypedResource typedResource) {
		JSONObject resource = marshallLinkedResource(typedResource);
		resource.put(IDomeoOntology.generalType, new JSONString("Not implemented yet"));
		return resource;
	}
	
	/**
	 * Serializes a trusted typed resource, a linked resource with a type.
	 * @param trustedTypedResource	Instance to be serialized
	 * @return The JSON representation of the MTrustedTypedResource instance
	 */
	public static JSONObject serialize(MTrustedTypedResource trustedTypedResource) {
		JSONObject resource = marshallTrustedResource(trustedTypedResource);
		resource.put(IDomeoOntology.generalType, new JSONString("Not implemented yet"));
		return resource;
	}
}
