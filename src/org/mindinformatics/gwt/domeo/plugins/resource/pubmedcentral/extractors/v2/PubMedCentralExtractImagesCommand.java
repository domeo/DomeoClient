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
package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.document.extractor.GenericDocumentExtractImagesCommand;
import org.mindinformatics.gwt.domeo.services.extractors.IContentExtractorCallback;
import org.mindinformatics.gwt.framework.component.ui.dialog.ProgressMessagePanel;
import org.mindinformatics.gwt.framework.component.ui.glass.DialogGlassPanel;
import org.mindinformatics.gwt.framework.src.ICommand;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.user.client.Element;

/**
*
* @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
*/
public class PubMedCentralExtractImagesCommand extends GenericDocumentExtractImagesCommand implements ICommand {

	public PubMedCentralExtractImagesCommand(IDomeo domeo, IContentExtractorCallback callback,  
			ICommandCompleted completionCallback) {
		super(domeo, callback, completionCallback);
	}
	
	@Override
	public void execute() {
		_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, "Extracting PMC document images");
		((ProgressMessagePanel)((DialogGlassPanel)_domeo.getDialogPanel()).getPanel()).setMessage("Extracting PMC document images");
		_domeo.getLogger().debug(this,"1");
		extractImages(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement());
		_domeo.getLogger().debug(this,"1end");
		_completionCallback.notifyStageCompletion(); // Necessary as synchronous
	}
	
	public native void extractImages(Element iframe) /*-{
		var foo = this;
		if (iframe) {
			var images = iframe.contentDocument.getElementsByTagName('img');
			var imagesLength = images.length;
			for(var i=0;i<imagesLength;++i) {
				var originalUrl = images[i].getAttribute("src");
				var large = (images[i].getAttribute("src-large"));
				if (large!=null) {
					images[i].setAttribute('src', large);
					images[i].removeAttribute('src-large');
					images[i].setAttribute('class', 'none');
					var parent = images[i].parentNode;
					if(parent.nodeName.toLowerCase() == "a") {
						parent.setAttribute('class', 'none');
					}
				}
				
				images[i].setAttribute('imageid', 'domeo_img_'+i);
				
				var frameBaseWithProxy = $doc.getElementById("domeoframe").contentWindow.document.location;
				//alert('frameBaseWithProxy ' + frameBaseWithProxy)
				if(new String(frameBaseWithProxy).lastIndexOf("proxy/")>0) {
					frameBaseWithProxy = new String(frameBaseWithProxy).substring(new String(frameBaseWithProxy).lastIndexOf("proxy/")+6);
				} 

				// Detect base url
				var frameBaseUrl = $wnd.getBaseUrl(new String(frameBaseWithProxy));
				//alert('frameBase ' + frameBaseUrl);
				var appBaseUrl = $wnd.getHostUrl(new String($doc.location));
				
				var src = images[i].src;
				if(images[i].src.substring(0,appBaseUrl.length) == appBaseUrl) {
					src = src.replace(appBaseUrl, frameBaseUrl);
				}  
				foo.@org.mindinformatics.gwt.domeo.plugins.resource.document.extractor.GenericDocumentExtractImagesCommand::addImage(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/dom/client/Element;)(originalUrl, src, ''+images[i].width, ''+images[i].height, images[i].alt, images[i]);
			}
		}
	}-*/;
	
//	private void addImage(String src, String width, String height, String title, com.google.gwt.dom.client.Element image) {
//		
//		int MIN_WIDTH = 90;
//		int MIN_HEIGHT = 50;
//		if(new Integer(width)>MIN_WIDTH && new Integer(height)>MIN_HEIGHT)
//			_domeo.getImagesCache().cacheImage(new ImageProxy(src, width, height, title, image));
//		else
//			_domeo.getLogger().debug(this, "Image rejected because of size " + src);
//	}
	
}
