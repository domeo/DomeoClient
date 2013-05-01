package org.mindinformatics.gwt.domeo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MDocumentAnnotation implements Serializable, IsSerializable,
		IVersionable, IUniquelyIdentifiable {

	private String uuid;
	private Long localId;
	
	private String versionNumber;
	private String previousVersion;	  // computed by the server and saved
	private Boolean hasChanged = false;	// flag for triggering the saving
	
	private Date createdOn;
	private Date lastSavedOn;
	
	private MGenericResource currentResource;
	private MAgentPerson editor; 

    Set<MAnnotationSet> annotationSets= new LinkedHashSet<MAnnotationSet>();

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getLocalId() {
		return localId;
	}

	public void setLocalId(Long localId) {
		this.localId = localId;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getPreviousVersion() {
		return previousVersion;
	}

	public void setPreviousVersion(String previousVersion) {
		this.previousVersion = previousVersion;
	}

	public Boolean getHasChanged() {
		return hasChanged;
	}

	public void setHasChanged(Boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getLastSavedOn() {
		return lastSavedOn;
	}

	public void setLastSavedOn(Date lastSavedOn) {
		this.lastSavedOn = lastSavedOn;
	}

	public MGenericResource getCurrentResource() {
		return currentResource;
	}

	public void setCurrentResource(MGenericResource currentResource) {
		this.currentResource = currentResource;
	}

	public MAgentPerson getEditor() {
		return editor;
	}

	public void setEditor(MAgentPerson editor) {
		this.editor = editor;
	}

	public Set<MAnnotationSet> getAnnotationSets() {
		return annotationSets;
	}

	public void setAnnotationSets(Set<MAnnotationSet> annotationSets) {
		this.annotationSets = annotationSets;
	}
}
