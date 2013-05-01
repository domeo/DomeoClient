package org.mindinformatics.gwt.domeo.model;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.model.selectors.MSelector;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MAnnotationCitationReference extends MAnnotationReference {

	private Integer referenceIndex;
	private List<MSelector> citationSelectors = new ArrayList<MSelector>();
	private MSelector referenceSelector;
	
	public Integer getReferenceIndex() {
		return referenceIndex;
	}
	public void setReferenceIndex(Integer referenceIndex) {
		this.referenceIndex = referenceIndex;
	}
	public List<MSelector> getCitationSelectors() {
		return citationSelectors;
	}
	public void addCitation(MSelector selector) {
		citationSelectors.add(selector);
	}
	public void setCitationSelectors(List<MSelector> citationSelectors) {
		this.citationSelectors = citationSelectors;
	}
	public MSelector getReferenceSelector() {
		return referenceSelector;
	}
	public void setReferenceSelector(MSelector referenceSelector) {
		this.referenceSelector = referenceSelector;
	}
}
