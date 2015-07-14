/*
 * Copyright 2014 Massachusetts General Hospital
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.serializers;

import java.util.Date;

import org.mindinformatics.gwt.framework.src.Utils;

import com.google.gwt.json.client.JSONString;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AAnnotopiaSerializer {

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
		return new JSONString(content!=null?Utils.fullfmt2.format(content):"<EXCEPTION NULL:to-be-fixed>");
	}
	
	public JSONString nonNullable(String content) {
		return new JSONString(content!=null?content:"<EXCEPTION NULL:to-be-fixed>");
	}
	
	public JSONString nonNullable(Long content) {
		return new JSONString(content!=null?Long.toString(content):"<EXCEPTION NULL:to-be-fixed>");
	}
	
	public JSONString nullable(Date content) {
		return new JSONString(content!=null?Utils.fullfmt2.format(content):"");
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
