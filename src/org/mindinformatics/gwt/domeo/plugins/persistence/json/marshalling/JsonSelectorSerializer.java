package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTargetSelector;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonSelectorSerializer extends ASerializer implements ISerializer {
	
	protected JSONValue initializeSelector(MSelector sel) {
		JSONObject selector = new JSONObject();
	
		// These have to be defined
		selector.put(IRdfsOntology.type, new JSONString(sel.getSelectorType()));
		selector.put(IRdfsOntology.id, new JSONString(sel.getUri()!=null?sel.getUri():"<unknown-fix>"));
		selector.put(IDomeoOntology.transientLocalId, new JSONString(Long.toString(sel.getLocalId())));
		selector.put(IDomeoOntology.uuid, new JSONString(sel.getUuid()!=null?sel.getUuid():"<unknown-fix>"));	
		selector.put(IPavOntology.createdOn, new JSONString(sel.getFormattedCreationDate()!=null?dateFormatter.format(sel.getCreatedOn()):""));
		return selector;
	}
	
	protected JSONValue initializeSpecificTarget(MSelector sel) {
		JSONObject specificTarget = new JSONObject();
		// These have to be defined
		specificTarget.put(IRdfsOntology.type, new JSONString(IDomeoOntology.specificResource));
		specificTarget.put(IRdfsOntology.id, new JSONString(sel.getUri()!=null?sel.getUri():"<unknown-fix>"));
		specificTarget.put(IDomeoOntology.transientLocalId, new JSONString(Long.toString(sel.getLocalId())));
		specificTarget.put(IDomeoOntology.source, new JSONString(sel.getTarget().getUrl()));
		return specificTarget;
	}
	
	protected JSONValue initializeTarget(MTargetSelector sel) {
		JSONObject specificTarget = new JSONObject();
		// These have to be defined
		specificTarget.put(IRdfsOntology.type, new JSONString(sel.getSelectorType()));
		specificTarget.put(IRdfsOntology.id, new JSONString(sel.getUri()!=null?sel.getUri():"<unknown-fix>"));
		specificTarget.put(IDomeoOntology.transientLocalId, new JSONString(Long.toString(sel.getLocalId())));
		specificTarget.put(IDomeoOntology.source, new JSONString(sel.getTarget().getUrl()));
		return specificTarget;
	}
	
	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		MSelector sel = (MSelector) obj;
		JSONValue selector = initializeSelector(sel);
		JSONObject specificTarget = (JSONObject) initializeSpecificTarget(sel);
		specificTarget.put(IDomeoOntology.selector, selector);
		return specificTarget;
	}
	
	public JSONObject serializeWhatChanged(JsonSerializerManager manager, Object obj) {
		return serialize(manager, obj);
	}
}
