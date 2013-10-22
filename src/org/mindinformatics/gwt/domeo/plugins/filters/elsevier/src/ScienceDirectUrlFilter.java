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
package org.mindinformatics.gwt.domeo.plugins.filters.elsevier.src;

import org.mindinformatics.gwt.domeo.component.filters.src.IUrlFilter;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ScienceDirectUrlFilter implements IUrlFilter {

	public final String KEYS = "apiKey=&Insttoken=";

	@Override
	public boolean doesFilterApply(String url) {
		// TODO Auto-generated method stub
		return url.startsWith("http://www.sciencedirect.com/science/article/pii/");
	}
	
	@Override
	public String filterUrl(String url) {
		if(url.startsWith("http://www.sciencedirect.com/science/article/pii/")) {
			String prefix = url.replaceAll("http://www.sciencedirect.com/science/article/pii/", "https://api.elsevier.com/content/article/PII:");
			String postfix = "?httpAccept=text/html&";
			return prefix + postfix + KEYS;
		}
		return url;
	}
}
