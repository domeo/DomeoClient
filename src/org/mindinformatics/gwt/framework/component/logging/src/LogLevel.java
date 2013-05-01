package org.mindinformatics.gwt.framework.component.logging.src;

public enum LogLevel {

	DEBUG ("Debug"),
	INFO ("Info"),
	COMMAND ("Command"),
	WARNING ("Warning"),
	EXCEPTION ("Exception");
	
	private String value;

	LogLevel(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
    
    public static LogLevel getLevelByValue(String value) {
    	if (value != null) {
            for (LogLevel level: LogLevel.values()) {
                if (value.equals(level.value)) {
                    return level;
                }
            }
        }
        return null;
    }
}
