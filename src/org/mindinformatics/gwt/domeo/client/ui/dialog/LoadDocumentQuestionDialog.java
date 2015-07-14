package org.mindinformatics.gwt.domeo.client.ui.dialog;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.component.ui.dialog.QuestionMessagePanel;
import org.mindinformatics.gwt.framework.src.Utils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;

public class LoadDocumentQuestionDialog extends QuestionMessagePanel  {
			
	public LoadDocumentQuestionDialog(final IDomeo domeo, String message, final String url) {
		super(domeo, message, url);
		
		ClickHandler openHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_containerPanel.hide();
				((IDomeo)_application).loadContent(_subMessage);
			}
		};
		addButton("Yes", openHandler);
		
		ClickHandler openInNewTabHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_containerPanel.hide();
				String urlfrom = domeo.getPersistenceManager().getCurrentResourceUrl();
				if(urlfrom.indexOf("?")>0) 
					urlfrom = url.substring(0, urlfrom.indexOf("?"));
				
				String finalLink = Utils.getAnnotationToolLink(domeo, urlfrom, url);
				trackPath(finalLink, domeo.getPersistenceManager().getCurrentResourceUrl(), url, domeo.getUserManager().getUser().getUserName());			}
		};
		addButton("Open in new Tab", openInNewTabHandler);
		
		addCancelButton(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				((IDomeo)_application).resetContentLoading();
				_containerPanel.hide();	
			}
		});
	}
	
	private static native void trackPath(String completeLink, String sourceFrom, String sourceTo, String username) 
	/*-{
		$wnd.trackpath(completeLink, sourceFrom, sourceTo, username);
	}-*/;
}
