package org.mindinformatics.gwt.framework.component.pipelines.src;

public interface IParametersCache {

	public void addParam(String paramName, String paramValue);
	public String getParamValue(String paramName);
	public void removeParam(String paramName);
}
