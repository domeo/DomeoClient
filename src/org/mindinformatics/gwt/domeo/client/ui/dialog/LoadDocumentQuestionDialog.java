package org.mindinformatics.gwt.domeo.client.ui.dialog;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.component.ui.dialog.QuestionMessagePanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;

public class LoadDocumentQuestionDialog extends QuestionMessagePanel  {
			
	public LoadDocumentQuestionDialog(IDomeo domeo, String message, String url) {
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
				Window.alert("Open in new tab not implemented");
			}
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
}
