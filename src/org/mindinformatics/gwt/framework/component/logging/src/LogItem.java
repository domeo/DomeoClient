package org.mindinformatics.gwt.framework.component.logging.src;

import java.util.Date;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 * 
 * The category is optional and the allowed categories can be defined at the
 * application level.
 */
public class LogItem {

	private Date _dateTime;
	private LogLevel _type;
	private String _className;
	private String _category;
	private String _message;
	
	public LogItem(LogLevel type, String category, String className, String message) {
		_dateTime = new Date();
		_type = type;
		_className = className;
		_category = category;
		_message = message;
	}

	public Date getDateTime() {
		return _dateTime;
	}

	public String getClassName() {
		return _className;
	}

	public LogLevel getType() {
		return _type;
	}
	
	public String getCategory() {
		return _category;
	}

	public String getMessage() {
		return _message;
	}
}
