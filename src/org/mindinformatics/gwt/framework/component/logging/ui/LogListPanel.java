package org.mindinformatics.gwt.framework.component.logging.ui;

import org.mindinformatics.gwt.framework.component.logging.src.LogItem;
import org.mindinformatics.gwt.framework.component.logging.src.LogLevel;
import org.mindinformatics.gwt.framework.component.logging.src.LogsManager;
import org.mindinformatics.gwt.framework.component.preferences.src.TextPreference;
import org.mindinformatics.gwt.framework.src.Application;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IRefresh;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class LogListPanel extends Composite implements IContentPanel, IRefresh {

	interface Binder extends UiBinder<VerticalPanel, LogListPanel> { }
	
	private static final Binder binder = GWT.create(Binder.class);
	
	private IApplication _application;
	private IContainerPanel _containerPanel;
	
	private boolean _textual;
	
	// Layout
	@UiField VerticalPanel logPanel;

	public void setContainer(IContainerPanel glassPanel) {
		_containerPanel = glassPanel;
	}
	
	public IContainerPanel getContainer() {
		return _containerPanel;
	}
	
	
	// ------------------------------------------------------------------------
	//  CREATION OF ANNOTATIONS OF VARIOUS KIND
	// ------------------------------------------------------------------------
	public LogListPanel(IApplication application, boolean textual) {
		_application = application;
		_textual = textual;

		// Create layout
		initWidget(binder.createAndBindUi(this)); 
		
		refresh();
	}
	
	public void refresh() {
		logPanel.clear();
		
		if(_application.getLogger()!=null) {
			int counter = 0;
			for(LogItem item: _application.getLogger().getLogs()) {
				if(_application.getLogger().doesLogEntryDisplays(
						LogLevel.getLevelByValue(((TextPreference)_application.getPreferences().
							getPreferenceItem(Application.class.getName(), LogsManager.LEVEL)).getValue()), 
						item))
					logPanel.add(new LogItemLens(_application, item, _textual, counter++));
			}
		}	
	}
}

