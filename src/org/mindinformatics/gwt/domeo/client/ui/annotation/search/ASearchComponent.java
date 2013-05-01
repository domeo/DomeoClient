package org.mindinformatics.gwt.domeo.client.ui.annotation.search;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.selectors.MTextSelector;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;

import com.google.gwt.user.client.ui.Composite;

/**
 * Abstract class to be extended for creating contextualized search components.
 * Every annotation item/type might have a different search component that 
 * knows better the structure of the artifact.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ASearchComponent extends Composite implements ISearchComponent {

	public static final String ANYBODY = "Anybody's Annotation";
	public static final String ONLY_MINE = "My Annotation";
	public static final String ONLY_OTHERS = "Annotation of Others";
	
	// By contract 
	protected IDomeo _domeo;
	
	public ASearchComponent(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean filterBySet(MAnnotation annotation, Long setFilter) {
		if(setFilter == -1l) return true; 
		return _domeo.getAnnotationPersistenceManager().isAnnotationInSet(annotation, setFilter);
	}
	
	@Override
	public boolean filterByAccess(MAnnotation annotation, String creatorFilter) {
		if(creatorFilter.equals(ANYBODY)) {
			return true;
		} else if(creatorFilter.equals(ONLY_MINE) && annotation.getCreator().getUri()
			.equals(_domeo.getAgentManager().getUserPerson().getUri())) {
				return true;
		} else if(creatorFilter.equals(ONLY_OTHERS) && !annotation.getCreator().getUri()
				.equals(_domeo.getAgentManager().getUserPerson().getUri())) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean filterByType(MAnnotation annotation, String typeFilter) {
		if(typeFilter.equals("All types")) return true; 
		return annotation.getClass().getName().equals(typeFilter);
	}
	
	/**
	 * The abstract search on the text match is case sensitive.
	 */
	@Override
	public boolean filterByText(MAnnotation annotation, String textSearch) {
		// TODO take into account multiple selectors
		if(SelectorUtils.isOnTextFragment(annotation.getSelectors()) && 
			annotation.getSelector() instanceof MTextSelector) {
				return (((MTextSelector)annotation.getSelector())
						.getExact().contains(textSearch));
		}
		return false;
	}




}
