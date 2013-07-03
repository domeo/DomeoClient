/*
 * Copyright 2013 Massachusetts General Hospital
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
package org.mindinformatics.gwt.framework.model.relationships;

import java.io.Serializable;
import java.util.Date;

import org.mindinformatics.gwt.framework.component.agents.model.MAgent;
import org.mindinformatics.gwt.framework.component.resources.model.IIdentifiable;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MRelationship implements IIdentifiable, Serializable, IsSerializable {

	DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy h:mma");
	
	private String id;
	private String name;
	private MAgent creator;
	private Date creationDate;
	protected Object object;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MAgent getCreator() {
		return creator;
	}
	public void setCreator(MAgent creator) {
		this.creator = creator;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public String getFormattedCreationDate() {
		return fmt.format(creationDate);
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
}
