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
package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IMicroPublicationsOntology {

	public final String Prefix = "micro-publications";
	public final String NS = "mp:";
	
	public final String challengedBy = "challengedBy";
	public final String supportedBy = "supportedBy";
	
	public final String mpReference = NS + "Reference";
	
	public final String mpArgues = NS + "argues";
	public final String mpAsserts = NS + "asserts";
	public final String mpCitation = NS + "citation";
	public final String mpStatement = NS + "statement";
	public final String mpSupportedBy = NS + "supportedBy";
	public final String mpChallengedBy = NS + challengedBy;
	public final String mpQualifiedBy = NS + "qualifiedBy";
}
