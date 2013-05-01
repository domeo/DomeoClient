package org.mindinformatics.gwt.framework.component.logging.ui;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.framework.component.logging.src.LogItem;
import org.mindinformatics.gwt.framework.component.logging.src.LogLevel;
import org.mindinformatics.gwt.framework.src.IApplication;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class LogItemLens extends Composite {

	interface Binder extends UiBinder<HTMLPanel, LogItemLens> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private Resources _resources;
	
	@UiField HTMLPanel logItemPanel;
	@UiField Element levelPanel;
	@UiField Element user;
	@UiField Element date;
	@UiField Element className;
	@UiField Element type;
	@UiField Element message;
	
	/**
	 * The static images used throughout the Domeo application.
	 */
	public static final LogResources localResources = 
		GWT.create(LogResources.class);
	
	public LogItemLens(IApplication application, LogItem item, boolean textual, int index) {
		_resources =  Domeo.resources;

		// Create layout
		initWidget(binder.createAndBindUi(this)); 
		
		if(textual) {
			levelPanel.setInnerHTML(item.getType().name());
		} else {
			levelPanel.setInnerHTML((new Image(getIcon(item))).getElement().getString());
		}
		
		date.setInnerText(getDateTime(item));
		className.setInnerText(item.getClassName());
		type.setInnerText(item.getCategory().toUpperCase());
		message.setInnerText(item.getMessage());	
	}
	
	private ImageResource getIcon(LogItem item) {
		if(item.getType().equals(LogLevel.EXCEPTION)) 
			return _resources.loggingException();
		else if(item.getType().equals(LogLevel.WARNING)) 
			return _resources.loggingWarning();
		else if(item.getType().equals(LogLevel.COMMAND)) 
			return _resources.loggingCommand();
		else if(item.getType().equals(LogLevel.INFO)) 
			return _resources.loggingInfo();
		
		return _resources.loggingDebug();
	}
	
	/**
	 * Returns a textual representation of the current date/time
	 * @return	Current date/time
	 */
	private String getDateTime(LogItem item) {
		return DateTimeFormat.getFormat(DateTimeFormat
			.PredefinedFormat.DATE_TIME_MEDIUM).format(item.getDateTime());
	}
}
