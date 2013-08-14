package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import java.util.Collection;
import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationCitationReference;
import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.MBibliographicSet;
import org.mindinformatics.gwt.domeo.model.MDocumentAnnotation;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPermissionsOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MAnnotationSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MImageInDocumentSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTargetSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.curation.model.JsonAnnotationCurationSerializer;
import org.mindinformatics.gwt.domeo.plugins.annotation.curation.model.MCurationToken;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.serialization.JsonMpAnnotationSerializer;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibodyAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.framework.component.agents.model.MAgent;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentDatabase;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentGroup;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.model.agents.IAgent;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.model.references.MReference;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;


public class JsonSerializerManager {

	public IDomeo _domeo;
	
	private HashMap<String, ISerializer> serializers = new HashMap<String, ISerializer>();
	private HashMap<String, IAgent> agentsToSerialize = new HashMap<String, IAgent>();
	private HashMap<String, MGenericResource> resourcesToSerialize = new HashMap<String, MGenericResource>();
	
	private JsonSerializerManager(IDomeo domeo) {
		_domeo = domeo;
		registerSerializers();
	}
	
	private static JsonSerializerManager _instance;
	public static JsonSerializerManager getInstance(IDomeo domeo) {
		if(_instance==null) {
			_instance = new JsonSerializerManager(domeo);
		} 
		return _instance;
	}
	
	private void registerSerializers() {
		serializers.put(MDocumentAnnotation.class.getName(), new JsonDocumentAnnotationSerializer());
		serializers.put(MAnnotationSet.class.getName(), new JsonAnnotationSetSerializer());	
		serializers.put(MBibliographicSet.class.getName(), new JsonBibliographicSetSerializer());	
		serializers.put(MAnnotation.class.getName(), new JsonAnnotationSerializer());
		
		serializers.put(MTextQuoteSelector.class.getName(), new JsonTextQuoteSelectorSerializer());	
		serializers.put(MImageInDocumentSelector.class.getName(), new JsonImageInDocumentSelectorSerializer());
		serializers.put(MAnnotationSelector.class.getName(), new JsonAnnotationSelectorSerializer());
		serializers.put(MTargetSelector.class.getName(), new JsonTargetSelectorSerializer());	
		serializers.put(MSelector.class.getName(), new JsonSelectorSerializer());	
		
		serializers.put(MPostItAnnotation.class.getName(), new JsonPostItAnnotationSerializer());
		serializers.put(MQualifierAnnotation.class.getName(), new JsonQualifierAnnotationSerializer());
		serializers.put(MAntibodyAnnotation.class.getName(), new JsonAntibodyAnnotationSerializer(_domeo));
		serializers.put(MCommentAnnotation.class.getName(), new JsonCommentAnnotationSerializer());
		serializers.put(MMicroPublicationAnnotation.class.getName(), new JsonMpAnnotationSerializer());
		serializers.put(MCurationToken.class.getName(), new JsonAnnotationCurationSerializer());
		serializers.put(MAnnotationReference.class.getName(), new JsonReferenceAnnotationSerializer());
		serializers.put(MAnnotationCitationReference.class.getName(), new JsonCitationReferenceAnnotationSerializer());
		
		serializers.put(MPublicationArticleReference.class.getName(), new JsonPublicationArticleReferenceSerializer());
		
		serializers.put(MAgent.class.getName(), new JsonAgentSerializer());
		serializers.put(MAgentDatabase.class.getName(), new JsonAgentSerializer());
		serializers.put(MAgentPerson.class.getName(), new JsonAgentPersonSerializer());
		serializers.put(MAgentSoftware.class.getName(), new JsonAgentSoftwareSerializer());
		serializers.put(MAgentGroup.class.getName(), new JsonUserGroupSerializer());
	}
	
	public void initializeSerializerManager() {
		_domeo.getLogger().debug(this, "Initializing serializer manager");
		agentsToSerialize.clear();
		resourcesToSerialize.clear();
	}
	
	public void clearAgentsToSerialize() {
		agentsToSerialize.clear();
		resourcesToSerialize.clear();
	}
	
	public void addAgentToSerialize(IAgent agent) {
		if(!agentsToSerialize.containsKey(agent.getUri()))
			agentsToSerialize.put(agent.getUri(), agent);
	}
	
	public void addResourceToSerialize(MGenericResource resource) {
		if(!resourcesToSerialize.containsKey(resource.getUrl()))
			resourcesToSerialize.put(resource.getUrl(), resource);
	}
	
	public Collection<IAgent> getAgentsToSerialize() {
		return agentsToSerialize.values();
	}
	
	public Collection<MGenericResource> getResourcesToSerialize() {
		return resourcesToSerialize.values();
	}
	
	public void serialize(MDocumentAnnotation documentAnnotation) {
		JsonDocumentAnnotationSerializer serializer = (JsonDocumentAnnotationSerializer)serializers.get(MDocumentAnnotation.class.getName());
		serializer.serialize(this, documentAnnotation);
	}
	
	public JSONObject serialize(MAnnotationSet annotationSet) {
		initializeSerializerManager();
		_domeo.getLogger().debug(this, "Serializing annotation set with localId " + annotationSet.getLocalId() + " and " + annotationSet.getAnnotations().size() + " items");
		try {
			JsonAnnotationSetSerializer serializer = (JsonAnnotationSetSerializer)serializers.get(MAnnotationSet.class.getName());
			if(serializer!=null) {
				return serializeAnnotationSetAccessDetails(annotationSet, serializer.serialize(this, annotationSet));
			}
			else {
				_domeo.getLogger().exception(this, "Serializer for Annotation Set not found");
				return new JSONObject();
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while serializing the Annotation Set " + e.getMessage());
			return new JSONObject();
		}
	}
	
	public JSONObject serialize(MBibliographicSet bibliographicSet) {
		initializeSerializerManager();
		_domeo.getLogger().debug(this, "Serializing bibliographic set with localId " + bibliographicSet.getLocalId() + " and " + bibliographicSet.getAnnotations().size() + " items");
		try {
			ISerializer serializer = (JsonAnnotationSetSerializer)serializers.get(MBibliographicSet.class.getName());
			if(serializer!=null) {
				return serializeAnnotationSetAccessDetails((MAnnotationSet)bibliographicSet, serializer.serialize(this, bibliographicSet));
			}
			else {
				_domeo.getLogger().exception(this, "Serializer for bibliographic Set not found");
				return new JSONObject();
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while serializing the bibliographic Set " + e.getMessage());
			return new JSONObject();
		}
	}
	
	private JSONString nullableBoolean(Boolean content) {
		return new JSONString(content!=null?Boolean.toString(content):"");
	}
	
	// TODO HIGH Move to a serializer??? How does the _domeo reference would work?
	private JSONObject serializeAnnotationSetAccessDetails(MAnnotationSet annotationSet, JSONObject jsonAnnotationSet) {
		try {
			_domeo.getLogger().debug(this, "Serializing permissions of annotation set with localId " + annotationSet.getLocalId() + " and " + annotationSet.getAnnotations().size() + " items");
			JSONObject permission = new JSONObject();
			permission.put(IPermissionsOntology.accessType, new JSONString(_domeo.getAnnotationAccessManager().getAnnotationSetAccessType(annotationSet)));
			permission.put(IPermissionsOntology.isLocked, nullableBoolean(annotationSet.getIsLocked()));
			
			jsonAnnotationSet.put(IPermissionsOntology.permissions, permission);
			
			JSONArray groupsList = new JSONArray();
			Collection<IUserGroup> groups = _domeo.getAnnotationAccessManager().getAnnotationSetGroups(annotationSet);
			if(groups!=null) {
				// TODO make it more explicit
				ISerializer serializer = (JsonUserGroupSerializer)serializers.get(MAgentGroup.class.getName());
				if(serializer!=null) {
					for(IUserGroup group: groups) {
						groupsList.set(groupsList.size(), serializer.serialize(this, group));
					}
				}
				else {
					_domeo.getLogger().exception(this, "Serializer for agent group not found");
					return new JSONObject();
				}
				JSONObject accessDetails = new JSONObject();
				accessDetails.put(IPermissionsOntology.allowedGroups, groupsList);
				permission.put(IPermissionsOntology.accessDetails, accessDetails);
			}
			return jsonAnnotationSet;
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while serializing the Annotation Set access details " + e.getMessage());
			return new JSONObject();
		}
	}
	
	public JSONObject serializeWhatChanged(MAnnotationSet annotationSet) {
		_domeo.getLogger().debug(this, "Serializing annotation set with localId " + annotationSet.getLocalId() + " and " + annotationSet.getAnnotations().size() + " items");
		try {
			JsonAnnotationSetSerializer serializer = (JsonAnnotationSetSerializer)serializers.get(MAnnotationSet.class.getName());
			if(serializer!=null) return serializer.serializeWhatChanged(this, annotationSet);
			else {
				_domeo.getLogger().exception(this, "Serializer for Annotation Set not found");
				return new JSONObject();
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while serializing the Annotation Set " + e.getMessage());
			return new JSONObject();
		}
	}

	public JSONValue serialize(MAnnotation annotation) {
		_domeo.getLogger().debug(this, "Serializing annotation with localId " + annotation.getLocalId() + " and type " + annotation.getClass().getName());
		try {
			ISerializer serializer = (ISerializer) serializers.get(annotation.getClass().getName());
			if(serializer!=null) {
				return serializer.serialize(this, annotation);
			} else {
				_domeo.getLogger().warn(this, "Dedicated serializer for Annotation "+annotation.getClass().getName()+" not found, applying default");
				ISerializer defaultSerializer = (ISerializer)  serializers.get(MAnnotation.class.getName());
				return defaultSerializer.serialize(this, annotation);
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while serializing the Annotation " + e.getMessage());
			return null;
		}
	}
	
	public JSONValue serialize(MSelector selector) {
		try {
			ISerializer serializer = (ISerializer) serializers.get(selector.getClass().getName());
			if(serializer!=null) {
				return serializer.serialize(this, selector);
			} else {
				_domeo.getLogger().exception(this, "Serializer for Selector not found");
				ISerializer defaultSerializer = (ISerializer)  serializers.get(MSelector.class.getName());
				return defaultSerializer.serialize(this, selector);
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while serializing the Selector " + e.getMessage());
			return new JSONObject();
		}
	}
	
	public JSONValue serialize(MReference reference) {
		try {
			ISerializer serializer = (ISerializer) serializers.get(reference.getClass().getName());
			if(serializer!=null) {
				return serializer.serialize(this, reference);
			} else {
				_domeo.getLogger().exception(this, "Serializer for Reference not found");
				ISerializer defaultSerializer = (ISerializer)  serializers.get(MSelector.class.getName());
				return defaultSerializer.serialize(this, reference);
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while serializing the Selector " + e.getMessage());
			return new JSONObject();
		}
	}
	
	public JSONValue serialize(MAgentGroup group) {
		_domeo.getLogger().debug(this, "Serializing Group with uuid " + group.getUuid());
		try {
			ISerializer serializer = (ISerializer) serializers.get(group.getClass().getName());
			if(serializer!=null) {
				return serializer.serialize(this, group);
			} else {
				_domeo.getLogger().exception(this, "Serializer for Group not found");
				ISerializer defaultSerializer = (ISerializer)  serializers.get(MAgentGroup.class.getName());
				return defaultSerializer.serialize(this, group);
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while serializing the Person " + e.getMessage());
			return null;
		}
	}
	
	public JSONValue serialize(MAgentPerson person) {
		_domeo.getLogger().debug(this, "Serializing AgentPerson with uuid " + person.getUuid());
		try {
			ISerializer serializer = (ISerializer) serializers.get(person.getClass().getName());
			if(serializer!=null) {
				return serializer.serialize(this, person);
			} else {
				_domeo.getLogger().exception(this, "Serializer for Person not found");
				ISerializer defaultSerializer = (ISerializer)  serializers.get(MAgentPerson.class.getName());
				return defaultSerializer.serialize(this, person);
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while serializing the Person " + e.getMessage());
			return null;
		}
	}
	
	public JSONValue serialize(MAgentSoftware software) {
		_domeo.getLogger().debug(this, "Serializing AgentSoftware with uuid " + software.getUuid()+ " and type " + software.getClass().getName());
		try {
			ISerializer serializer = (ISerializer) serializers.get(software.getClass().getName());
			if(serializer!=null) {
				return serializer.serialize(this, software);
			} else {
				_domeo.getLogger().warn(this, "Serializer for " + software.getClass().getName() + " not found, applying default Software serializer");
				ISerializer defaultSerializer = (ISerializer)  serializers.get(MAgentSoftware.class.getName());
				return defaultSerializer.serialize(this, software);
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while serializing the Software " + e.getMessage());
			return null;
		}
	}
	
	public JSONValue serialize(MAgent agent) {
		_domeo.getLogger().debug(this, "Serializing Agent with Uri " + agent.getUri() + " and type " + agent.getClass().getName());
		try {
			ISerializer serializer = (ISerializer) serializers.get(agent.getClass().getName());
			if(serializer!=null) {
				return serializer.serialize(this, agent);
			} else {
				_domeo.getLogger().warn(this, "Serializer for " + agent.getClass().getName() + " not found, applying default Agent serializer");
				ISerializer defaultSerializer = (ISerializer)  serializers.get(MAgent.class.getName());
				return defaultSerializer.serialize(this, agent);
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Exception while serializing the Person " + e.getMessage());
			return null;
		}
	}
}
