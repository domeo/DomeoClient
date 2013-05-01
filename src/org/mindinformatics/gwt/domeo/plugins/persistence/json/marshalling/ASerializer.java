package org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONString;

/**
 * This class provides basic methods for serializing JSON
 * content.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ASerializer {
	
	protected DateTimeFormat dateFormatter = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss Z");

	public String property(String name, String value) {
		return "\""+name+"\":\""+value+"\"";
	}
	
	public String list(String name, String content) {
		return "\""+name+"\": ["+content+"]";
	}
	
	public String object(String name, String content) {
		return "\""+name+"\": {"+content+"}";
	}
	
	public JSONString nonNullable(Date content) {
		return new JSONString(content!=null?dateFormatter.format(content):"<EXCEPTION NULL:to-be-fixed>");
	}
	
	public JSONString nonNullable(String content) {
		return new JSONString(content!=null?content:"<EXCEPTION NULL:to-be-fixed>");
	}
	
	public JSONString nonNullable(Long content) {
		return new JSONString(content!=null?Long.toString(content):"<EXCEPTION NULL:to-be-fixed>");
	}
	
	public JSONString nullable(Date content) {
		return new JSONString(content!=null?dateFormatter.format(content):"");
	}
	
	public JSONString nullable(Long content) {
		return new JSONString(content!=null?Long.toString(content):"");
	}

	public JSONString nullable(String content) {
		return new JSONString(content!=null?content:"");
	}
	
	public JSONString nullableBoolean(Boolean content) {
		return new JSONString(content!=null?Boolean.toString(content):"");
	}
}
	