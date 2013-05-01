package org.mindinformatics.gwt.framework.component.logging.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 * 
 * This class manages the logging of the GWT applications and of all the 
 * plugins. The log creation is happening only through the exposed methods 
 * which are allowing four levels of logging: INFO, DEBUG, WARNING and 
 * EXCEPTION. COMMAND is used for building the history.
 */
public class LogsManager {

	public static final String LEVEL = "Level";
	
	public HashMap<LogLevel, Integer> priority = new HashMap<LogLevel, Integer>();
	
	public LogsManager() {
		priority.put(LogLevel.DEBUG, 0);
		priority.put(LogLevel.INFO, 1);
		priority.put(LogLevel.COMMAND, 2);
		priority.put(LogLevel.WARNING, 3);
		priority.put(LogLevel.EXCEPTION, 4);
	}
	
	public boolean doesLogEntryDisplays(LogLevel level, LogItem item) {
		if(priority.get(item.getType())>=priority.get(level)) return true;
		return false;
	}
	
	/**
	 * List of logs
	 */
	private ArrayList<LogItem> logs = new ArrayList<LogItem>();

	/**
	 * Info logging message
	 * @param category	Category of the message 
	 * @param className The name of the class that originated the message
	 * @param message	The textual content of the message
	 */
	public void info(String category, String className, String message) {
		addLogMessage(LogLevel.INFO, category, className, message);
	}
	
	public void info(String category, Object clazz, String message) {
		addLogMessage(LogLevel.INFO, category, clazz.getClass().getName(), message);
	}
	
	public void info(String category, Object clazz, String method, String message) {
		addLogMessage(LogLevel.INFO, category, clazz.getClass().getName(), method,  message);
	}
	
	/**
	 * Info logging message
	 * @param className The name of the class that originated the message
	 * @param message	The textual content of the message
	 */
	public void info(String className, String message) {
		addLogMessage(LogLevel.INFO, "", className, message);
	}
	
	public void info(Object clazz, String message) {
		addLogMessage(LogLevel.INFO, "", clazz.getClass().getName(), message);
	}
	
	public void info(Object clazz, String method, String message) {
		addLogMessage(LogLevel.INFO, "", clazz.getClass().getName(), method, message);
	}
	
	/**
	 * Debug logging message
	 * @param category	Category of the message 
	 * @param className The name of the class that originated the message
	 * @param message	The textual content of the message
	 */
	public void debug(String category, String className, String message) {
		addLogMessage(LogLevel.DEBUG, category, className, message);
	}
	
	public void debug(String category, Object clazz, String message) {
		addLogMessage(LogLevel.DEBUG, category, clazz.getClass().getName(), message);
	}
	
	public void debug(String category, Object clazz, String method, String message) {
		addLogMessage(LogLevel.DEBUG, category, clazz.getClass().getName(), method, message);
	}
	
	/**
	 * Debug logging message
	 * @param className The name of the class that originated the message
	 * @param message	The textual content of the message
	 */
	public void debug(String className, String message) {
		addLogMessage(LogLevel.DEBUG, "", className, message);
	}
	
	public void debug(Object clazz, String message) {
		addLogMessage(LogLevel.DEBUG, "", clazz.getClass().getName(), message);
	}
	
	public void debug(Object clazz, String method, String message) {
		addLogMessage(LogLevel.DEBUG, "", clazz.getClass().getName(), method, message);
	}
	
	/**
	 * Warning logging message
	 * @param category	Category of the message 
	 * @param className The name of the class that originated the message
	 * @param message	The textual content of the message
	 */
	public void warn(String category, String className, String message) {
		addLogMessage(LogLevel.WARNING, category, className, message);
	}
	
	public void warn(String category, Object clazz, String message) {
		addLogMessage(LogLevel.WARNING, category, clazz.getClass().getName(), message);
	}
	
	public void warn(String category, Object clazz, String method, String message) {
		addLogMessage(LogLevel.WARNING, category, clazz.getClass().getName(), method, message);
	}
	
	/**
	 * Warning logging message
	 * @param className The name of the class that originated the message
	 * @param message	The textual content of the message
	 */
	public void warn(String className, String message) {
		addLogMessage(LogLevel.WARNING, "", className, message);
	}
	
	public void warn(Object clazz, String message) {
		addLogMessage(LogLevel.WARNING, "", clazz.getClass().getName(), message);
	}
	
	public void warn(Object clazz, String method, String message) {
		addLogMessage(LogLevel.WARNING, "", clazz.getClass().getName(), method, message);
	}
	
	/**
	 * Command logging message
	 * @param category	Category of the message 
	 * @param className The name of the class that originated the message
	 * @param message	The textual content of the message
	 */
	public void command(String category, String className, String message) {
		addLogMessage(LogLevel.COMMAND, category, className, message);
	}
	
	public void command(String category, Object clazz, String message) {
		addLogMessage(LogLevel.COMMAND, category, clazz.getClass().getName(), message);
	}
	
	public void command(String category, Object clazz, String method, String message) {
		addLogMessage(LogLevel.COMMAND, category, clazz.getClass().getName(), method, message);
	}
	
	/**
	 * Command logging message
	 * @param className The name of the class that originated the message
	 * @param message	The textual content of the message
	 */
	public void command(String className, String message) {
		addLogMessage(LogLevel.COMMAND, "", className, message);
	}
	
	public void command(Object clazz, String message) {
		addLogMessage(LogLevel.COMMAND, "", clazz.getClass().getName(), message);
	}
	
	/**
	 * Exception logging message
	 * @param category	Category of the message 
	 * @param className The name of the class that originated the message
	 * @param message	The textual content of the message
	 */
	public void exception(String category, String className, String message) {
		addLogMessage(LogLevel.EXCEPTION, category, className, message);
	}
	
	public void exception(String category, Object clazz, String message) {
		addLogMessage(LogLevel.EXCEPTION, category, clazz.getClass().getName(), message);
	}
	
	public void exception(String category, Object clazz, String method, String message) {
		addLogMessage(LogLevel.EXCEPTION, category, clazz.getClass().getName(), method, message);
	}
	
	/**
	 * Exception logging message
	 * @param className The name of the class that originated the message
	 * @param message	The textual content of the message
	 */
	public void exception(String className, String message) {
		addLogMessage(LogLevel.EXCEPTION, "", className, message);
	}
	
	public void exception(Object clazz, String message) {
		addLogMessage(LogLevel.EXCEPTION, "", clazz.getClass().getName(), message);
	}
	
	public void exception(Object clazz, String method, String message) {
		addLogMessage(LogLevel.EXCEPTION, "", clazz.getClass().getName(), method, message);
	}
	
	/**
	 * Adds a message to the tracer.
	 * @param type		Type of message (taken from LogsTypes)
	 * @param category	Category of the message 
	 * @param className The name of the class that originated the message
	 * @param message	The textual content of the message
	 */
	private void addLogMessage(LogLevel type, String category, String className, String message) {
		logs.add(new LogItem(type, category, className, message));
	}
	
	private void addLogMessage(LogLevel type, String category, String className, String method, String message) {
		logs.add(new LogItem(type, category, className+":"+method, message));
	}

	/**
	 * Returns the list of messages in the trace list
	 * @return	The list of trace messages
	 */
	public List<LogItem> getLogs() {
		return logs;
	}
}
