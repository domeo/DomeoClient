package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IRdfsOntology;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class JsonUserGroupSerializer extends ASerializer implements ISerializer {

	DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss Z");
	
	protected JSONObject initializeAgent(JsonSerializerManager manager, IUserGroup group) {
		JSONObject jo = new JSONObject();
		jo.put("@type", new JSONString("foafx:Group"));
		jo.put("@id", new JSONString(group.getUri()!=null?group.getUri():""));
		jo.put(IRdfsOntology.rdfLabel, new JSONString(group.getName()!=null?group.getName():""));	
		jo.put("name", new JSONString(group.getName()!=null?group.getName():""));	
		jo.put("description", new JSONString(group.getDescription()!=null?group.getDescription():""));	
		
		jo.put("homepage", new JSONString(group.getGroupLink()!=null?group.getGroupLink():""));	
		jo.put(IDomeoOntology.uuid, new JSONString(group.getUuid()!=null?group.getUuid():"<unknown-fix>"));
		return jo;
	}

	public JSONObject serialize(JsonSerializerManager manager, Object obj) {
		IUserGroup o = (IUserGroup) obj;
		JSONObject jo = initializeAgent(manager, o);		
		return jo;
	}
	
	public JSONObject serializeWhatChanged(JsonSerializerManager manager, Object obj) {
		return new JSONObject();
	}
}
