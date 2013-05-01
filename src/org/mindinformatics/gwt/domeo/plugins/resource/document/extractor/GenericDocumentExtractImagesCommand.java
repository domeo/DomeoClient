package org.mindinformatics.gwt.domeo.plugins.resource.document.extractor;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.cache.images.model.ImageProxy;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.extractors.v2.PubMedCentralDocumentPipeline;
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
public class GenericDocumentExtractImagesCommand implements ICommand {

	IDomeo _domeo;
	IContentExtractorCallback _callback;
	ICommandCompleted _completionCallback;
	
	public GenericDocumentExtractImagesCommand(IDomeo domeo, IContentExtractorCallback callback,  
			ICommandCompleted completionCallback) {
		_completionCallback = completionCallback;
		_domeo = domeo;
		_callback = callback;
	}
	
	@Override
	public void execute() {
		_domeo.getLogger().debug(PubMedCentralDocumentPipeline.LOGGER, this, "Extracting document images");
		((ProgressMessagePanel)((DialogGlassPanel)_domeo.getDialogPanel()).getPanel()).setMessage("Extracting document images");
		
		extractImages(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement());
		_completionCallback.notifyStageCompletion(); // Necessary as synchronous
	}
	
	public native void extractImages(Element iframe) /*-{
		var foo = this;
		if (iframe) {
			var images = iframe.contentDocument.getElementsByTagName('img');
			var imagesLength = images.length;
			for(var i=0;i<imagesLength;++i) {
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
				foo.@org.mindinformatics.gwt.domeo.plugins.resource.document.extractor.GenericDocumentExtractImagesCommand::addImage(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/dom/client/Element;)(src, ''+images[i].width, ''+images[i].height, images[i].alt, images[i]);
			}
		}
	}-*/;
	
	private void addImage(String src, String width, String height, String title, com.google.gwt.dom.client.Element image) {
		
		int MIN_WIDTH = 90;
		int MIN_HEIGHT = 50;
		if(new Integer(width)>MIN_WIDTH && new Integer(height)>MIN_HEIGHT)
			_domeo.getImagesCache().cacheImage(new ImageProxy(src, width, height, title, image));
		else
			_domeo.getLogger().debug(this, "Image rejected because of size " + src);
	}
	
}
