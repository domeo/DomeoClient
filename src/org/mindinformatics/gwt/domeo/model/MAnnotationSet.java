package org.mindinformatics.gwt.domeo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.model.agents.IAgent;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MAnnotationSet implements Serializable, IsSerializable,
		IVersionable, IUniquelyIdentifiable {

	DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yy h:mma");
	
	// Transient fields: these are not going to be stored in the 
	// Annotation Set graph
	private Long localId;
	private Boolean hasChanged = true;	// flag for triggering the saving
	
	// Persistent mutable fields: these are going to be stored in the 
    // Annotation Set sometimes after being modified by the server
	private String lineageUri;		// Lineage Uniform Resource Identifier
	private String individualUuid;	// Individual Universal Unique Identifier (USED?)
	private String individualUri;	// Individual Uniform Resource Identifier
	private String versionNumber;
	private String previousVersion;	  // computed by the server and saved
	private Date lastSavedOn;
	
	// Persistent immutable fields: these are going to be stored in the 
    // Annotation Set without being modified by the server
	private Date createdOn;
	private IAgent createdBy; 
	private ISoftware createdWith; 
	private ISoftware importedFrom; 
	private ISoftware importedBy; 
	private Date importedOn;
	
	private String label;
	private String description;
	private MGenericResource targetResource;
	
    private Boolean isLocked = false;
    
    transient private String type = IDomeoOntology.annotationSet;
    transient private Boolean isVisible = true;
    transient private boolean isEmphasized = false;
    
    // A virtual set is not placing the elements in context.
    // Domeo is not trying to place the annotation through the selectors
    // and no reference links are provided.
    transient private boolean isVirtual = false;
    
    List<MAnnotation> annotations = new ArrayList<MAnnotation>();
    
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void addAnnotation(MAnnotation annotation) {
    	annotations.add(annotation);
    	hasChanged = true;
    }
    
    public void removeAnnotation(MAnnotation annotation) {
    	annotations.remove(annotation);
    	hasChanged = true;
    }
    
	public String getFormattedCreationDate() {
		return fmt.format(createdOn);
	}
	
	public String getFormattedImportedOn() {
		if(importedOn!=null) 
			return fmt.format(importedOn);
		else return null;
	}
	
	public String getFormattedLastSavedOn() {
		if(lastSavedOn!=null) 
			return fmt.format(lastSavedOn);
		else return null;
	}

	public String getLineageUri() {
		return lineageUri;
	}

	public void setLineageUri(String lineageUri) {
		this.lineageUri = lineageUri;
	}

	public String getUuid() {
		return individualUuid;
	}

	public void setUuid(String individualUuid) {
		this.individualUuid = individualUuid;
	}

	public String getIndividualUri() {
		return individualUri;
	}

	public void setIndividualUri(String individualUri) {
		this.individualUri = individualUri;
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

	public MGenericResource getTargetResource() {
		return targetResource;
	}

	public void setTargetResource(MGenericResource targetResource) {
		this.targetResource = targetResource;
	}

	public IAgent getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(IAgent createdBy) {
		this.createdBy = createdBy;
	}
	
	public ISoftware getCreatedWith() {
		return createdWith;
	}

	public void setCreatedWith(ISoftware createdWith) {
		this.createdWith = createdWith;
	}

	public ISoftware getImportedFrom() {
		return importedFrom;
	}

	public void setImportedFrom(ISoftware importedFrom) {
		this.importedFrom = importedFrom;
	}

	public ISoftware getImportedBy() {
		return importedBy;
	}

	public void setImportedBy(ISoftware importedBy) {
		this.importedBy = importedBy;
	}

	public Date getImportedOn() {
		return importedOn;
	}

	public void setImportedOn(Date importedOn) {
		this.importedOn = importedOn;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<MAnnotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<MAnnotation> annotations) {
		this.annotations = annotations;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		if(getIsLocked()!=isLocked) this.setHasChanged(true);
		this.isLocked = isLocked;
	}

	public boolean isEmphasized() {
		return isEmphasized;
	}

	public void setEmphasized(boolean isEmphasized) {
		this.isEmphasized = isEmphasized;
	}

	public boolean isVirtual() {
		return isVirtual;
	}

	public void setVirtual(boolean isVirtual) {
		this.isVirtual = isVirtual;
	}
}
