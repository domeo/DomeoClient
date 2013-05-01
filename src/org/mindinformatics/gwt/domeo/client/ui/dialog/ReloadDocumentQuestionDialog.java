package org.mindinformatics.gwt.domeo.client.ui.dialog;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.component.ui.dialog.QuestionMessagePanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class ReloadDocumentQuestionDialog extends QuestionMessagePanel  {
			
	public ReloadDocumentQuestionDialog(IDomeo application, String message, String url) {
		super(application, message, url);
		
		ClickHandler openHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_containerPanel.hide();
				((IDomeo)_application).loadContent(_subMessage);
			}
		};
		addButton("Yes", openHandler);
		
		addCancelButton(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				((IDomeo)_application).resetContentLoading();
				_containerPanel.hide();	
			}
		});
	}
}
