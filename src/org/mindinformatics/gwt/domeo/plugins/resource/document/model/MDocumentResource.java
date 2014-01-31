package org.mindinformatics.gwt.domeo.plugins.resource.document.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.model.MOnlineResource;
import org.mindinformatics.gwt.framework.component.agents.model.MAgent;
import org.mindinformatics.gwt.framework.model.references.IReferences;
import org.mindinformatics.gwt.framework.model.references.ISelfReference;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MDocumentResource extends MOnlineResource implements ISelfReference, IReferences {
	
	DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yyyy h:mma");

	public static final String PARAM_DESCRIPTION_READONLY = "Description Read Only";
	public static final String PARAM_AUTHORS_READONLY = "Authors Read Only";
	public static final String PARAM_KEYWORDS_READONLY = "Keywords Read Only";
	
	private Date createdOn;
	private MAgent creator; 
	private String description;
	private String[] authors;
	private String[] keywords;
	
	private MAgent source;
	private MAnnotationReference reference;
	private List<MAnnotationReference> references = new ArrayList<MAnnotationReference>();
	
	public MAgent getSource() {
		return source;
	}
	public void setSource(MAgent source) {
		this.source = source;
	}
	@Override
	public void setSelfReference(MAnnotationReference citation) {
		this.reference = (MAnnotationReference) citation;
	}
	@Override
	public MAnnotationReference getSelfReference() {
		return reference;
	}
	@Override
	public void setReferences(List<MAnnotationReference> references) {
		this.references = references;
	}
	@Override
	public List<MAnnotationReference> getReferences() {
		return references;
	}
	// Maybe making it more robust cheking if the index is present already
	@Override
	public void addReference(MAnnotationReference reference) {
		references.add(reference);
	}
	@Override
	public MAnnotationReference getReferenceByIndex(Integer index) {
		if((index-1)>=0 && (index-1)<references.size())  
			return references.get(index-1);
		else return null;
	}
	@Override
	public MAnnotationReference getReferenceByUrl(String url) {
		for(MAnnotationReference reference: references) {
			if(((MPublicationArticleReference)reference.getReference()).getUrl()!=null && 
					((MPublicationArticleReference)reference.getReference()).getUrl().equals(url)) {
				return reference;
			}
		}
		return null;
	}
	
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
