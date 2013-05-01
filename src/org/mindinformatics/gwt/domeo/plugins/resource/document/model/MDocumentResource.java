package org.mindinformatics.gwt.domeo.plugins.resource.document.model;

import java.util.Date;

import org.mindinformatics.gwt.domeo.model.MOnlineResource;
import org.mindinformatics.gwt.framework.component.agents.model.MAgent;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MDocumentResource extends MOnlineResource {
	
	DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy h:mma");

	public static final String PARAM_DESCRIPTION_READONLY = "Description Read Only";
	public static final String PARAM_AUTHORS_READONLY = "Authors Read Only";
	public static final String PARAM_KEYWORDS_READONLY = "Keywords Read Only";
	
	private Date createdOn;
	private MAgent creator; 
	private String description;
	private String[] authors;
	private String[] keywords;
	
	public String getFormattedCreationDate() {
		return fmt.format(createdOn);
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public MAgent getCreator() {
		return creator;
	}
	public void setCreator(MAgent creator) {
		this.creator = creator;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getAuthors() {
		return authors;
	}
	public void setAuthors(String[] authors) {
		this.authors = authors;
	}
	public String[] getKeywords() {
		return keywords;
	}
	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}
}
