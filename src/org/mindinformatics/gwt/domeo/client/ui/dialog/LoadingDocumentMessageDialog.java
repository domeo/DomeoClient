package org.mindinformatics.gwt.domeo.client.ui.dialog;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.component.ui.dialog.ProgressMessagePanel;

public class LoadingDocumentMessageDialog extends ProgressMessagePanel  {
			
	public LoadingDocumentMessageDialog(IDomeo domeo, String message, String url) {
		super(domeo, message, url);
	}
}
