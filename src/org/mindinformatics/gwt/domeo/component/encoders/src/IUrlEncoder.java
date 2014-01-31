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
package org.mindinformatics.gwt.domeo.component.encoders.src;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IUrlEncoder {
	
	/**
	 * Checks if the implementation filter applies to the URL.
	 * @param url	The requested URL
	 * @return True if the filter applies to the URL
	 */
	public boolean doesEncoderApply(String url);
	
	/**
	 * Encode the requested URL and provides a more suitable one.
	 * @param url	The requested URL
	 * @return The revised URL to be opened
	 */
	public String encodeUrl(String url);

	/**
	 * Checks if the implementation filter applies to the URL.
	 * @param url	The requested URL
	 * @return True if the filter applies to the URL
	 */
	public boolean doesDecoderApply(String url);
	
	/**
	 * Decode the requested URL and provides a more suitable one for persistence.
	 * @param url	The requested URL
	 * @return The revised URL to be persisted.
	 */
	public String decodeUrl(String url);
}
