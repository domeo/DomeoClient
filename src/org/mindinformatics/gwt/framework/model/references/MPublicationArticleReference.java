package org.mindinformatics.gwt.framework.model.references;

import java.util.Date;

import org.mindinformatics.gwt.framework.component.agents.model.MAgent;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MPublicationArticleReference extends MReference {

    private String url;
    private String title;
    private String journalName;
    private String journalIssn;
    private String publicationDate;
    private String authorNames;
    private String journalPublicationInfo;
    private String publicationType;
    private String unrecognized;
    
    // Source
    private MAgent source;
    private Date creationDate;
    
    // IDs
    private String doi;
    private String pubMedId;
    private String pubMedCentralId;
    private String publisherItemId;
    
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getJournalName() {
		return journalName;
	}
	public void setJournalName(String journalName) {
		this.journalName = journalName;
	}
	public String getJournalIssn() {
		return journalIssn;
	}
	public void setJournalIssn(String journalIssn) {
		this.journalIssn = journalIssn;
	}
	public String getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}
	public String getAuthorNames() {
		return authorNames;
	}
	public void setAuthorNames(String authorNames) {
		this.authorNames = authorNames;
	}
	public String getJournalPublicationInfo() {
		return journalPublicationInfo;
	}
	public void setJournalPublicationInfo(String journalPublicationInfo) {
		this.journalPublicationInfo = journalPublicationInfo;
	}
	public String getPublicationType() {
		return publicationType;
	}
	public void setPublicationType(String publicationType) {
		this.publicationType = publicationType;
	}
	public String getUnrecognized() {
		return unrecognized;
	}
	public void setUnrecognized(String unrecognized) {
		this.unrecognized = unrecognized;
	}
	public MAgent getProvidedBy() {
		return source;
	}
	public void setSource(MAgent source) {
		this.source = source;
	}
	public Date getImportedOn() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getDoi() {
		return doi;
	}
	public void setDoi(String doi) {
		this.doi = doi;
	}
	public String getPubMedId() {
		return pubMedId;
	}
	public void setPubMedId(String pubMedId) {
		this.pubMedId = pubMedId;
	}
	public String getPubMedCentralId() {
		return pubMedCentralId;
	}
	public void setPubMedCentralId(String pubMedCentralId) {
		this.pubMedCentralId = pubMedCentralId;
	}
	public String getPublisherItemId() {
		return publisherItemId;
	}
	public void setPublisherItemId(String publisherItemId) {
		this.publisherItemId = publisherItemId;
	}   
}
