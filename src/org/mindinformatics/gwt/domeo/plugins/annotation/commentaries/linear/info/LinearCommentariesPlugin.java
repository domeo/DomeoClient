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
package org.mindinformatics.gwt.domeo.plugins.annotation.commentaries.linear.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * This plugin allows to add comments on existing annotations.
 * The approach is very simple. Every reply is targeting the original annotation.
 * No chaining is implemented and the comments are normally ordered by creation time.
 * 
 * This simple approach seems to be more appropriate for crowd-sourcing (and versioning).
 * It allows an easy implementation of the deletion. A a down side, the comments are 
 * always targeting the root and not the specific comment they are replying to.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class LinearCommentariesPlugin extends APlugin {
	
	public static final String VERSION = "0.1";
	public static final String TYPE = "General";
	public static final String SUB_TYPE = "Commentaries";
	public static final String PLUGIN = LinearCommentariesPlugin.class.getName().substring(0, LinearCommentariesPlugin.class.getName().indexOf(".info"));
	
	private static LinearCommentariesPlugin instance;
	private LinearCommentariesPlugin() {}
	
	public static LinearCommentariesPlugin getInstance() {
		if(instance==null) instance = new LinearCommentariesPlugin();
		return instance;
	}
	
	@Override
	public String getPluginName() {
		return PLUGIN;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public String getSubType() {
		return SUB_TYPE;
	}

	@Override
	public Boolean getMandatory() {
		return true;
	}

	@Override
	public String getVersion() {
		return VERSION;
	}
}
