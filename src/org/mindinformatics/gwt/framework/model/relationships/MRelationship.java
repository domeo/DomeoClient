package org.mindinformatics.gwt.framework.model.relationships;

import java.io.Serializable;
import java.util.Date;

import org.mindinformatics.gwt.framework.component.agents.model.MAgent;
import org.mindinformatics.gwt.framework.component.resources.model.IIdentifiable;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MRelationship implements IIdentifiable, Serializable, IsSerializable {

	DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy h:mma");
	
	private String id;
	private MAgent creator;
	private Date creationDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MAgent getCreator() {
		return creator;
	}
	public void setCreator(MAgent creator) {
		this.creator = creator;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public String getFormattedCreationDate() {
		return fmt.format(creationDate);
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
