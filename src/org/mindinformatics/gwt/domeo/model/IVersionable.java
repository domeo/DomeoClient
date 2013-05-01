package org.mindinformatics.gwt.domeo.model;

public interface IVersionable {

	public String getVersionNumber();
	public void setVersionNumber(String versionNumber);

	public String getPreviousVersion();
	public void setPreviousVersion(String previousVersion);

	public Boolean getHasChanged();
	public void setHasChanged(Boolean hasChanged);
}
