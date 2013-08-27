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
package org.mindinformatics.gwt.framework.component.styles.src;


/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IStyleStrategy {
	
	/**
	 * Returns the right class for the requested annotation according
	 * to the selected strategy.
	 * @param annotation	The annotation to render
	 * @return The style dictated by the selected strtegy
	 */
	public String getObjectStyleClass(Object object);
}
