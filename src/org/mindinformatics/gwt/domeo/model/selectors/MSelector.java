package org.mindinformatics.gwt.domeo.model.selectors;

import java.io.Serializable;
import java.util.Date;

import org.mindinformatics.gwt.domeo.model.IUniquelyIdentifiable;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.model.agents.IAgent;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MSelector implements Serializable, IsSerializable,
		IUniquelyIdentifiable {
	
	DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yy h:mma");

	private String uri;
    private String uuid;				// computed by the server
    private Long localId;				// Transient
    
    private String compositRespecificResourceUri;
    private String compositRespecificResourceUuid;
    
	private Date createdOn;
	private IAgent creator;
    
    private MGenericResource target;
    
    public String getSelectorType() {
    	return "oa:Selector";
    }
	public String getCompositRespecificResourceUri() {
		return compositRespecificResourceUri;
	}
	public void setCompositRespecificResourceUri(
			String compositRespecificResourceUri) {
		this.compositRespecificResourceUri = compositRespecificResourceUri;
	}
	public String getCompositRespecificResourceUuid() {
		return compositRespecificResourceUuid;
	}
	public void setCompositRespecificResourceUuid(
			String compositRespecificResourceUuid) {
		this.compositRespecificResourceUuid = compositRespecificResourceUuid;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
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
	public MGenericResource getTarget() {
		return target;
	}
	public void setTarget(MGenericResource target) {
		this.target = target;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public String getFormattedCreationDate() {
		return fmt.format(createdOn);
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public IAgent getCreator() {
		return creator;
	}
	public void setCreator(IAgent creator) {
		this.creator = creator;
	}
}
