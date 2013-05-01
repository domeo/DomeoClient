package org.mindinformatics.gwt.domeo.model.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.helpers.IAnnotationHelper;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * The class takes care of terms caching.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationTermsCacheManager implements IInitializableComponent {

	private IDomeo _domeo;
	
	// Caching of terms by url
	private HashMap<String, MLinkedResource> termsByUrl =
		new  HashMap<String, MLinkedResource>();
	
	// Counting terms by url
	private HashMap<String, Integer> termsCounterByUrl =
		new  HashMap<String, Integer>();
	
	// Caching of annotation by terms url
	private HashMap<String, ArrayList<MAnnotation>> annotationsByTermUrl =
		new  HashMap<String, ArrayList<MAnnotation>>();
	
	private HashMap<Long, ArrayList<String>> termsUrlsByAnnotationId =
		new HashMap<Long, ArrayList<String>>();
	
	public AnnotationTermsCacheManager(IDomeo domeo) {
		_domeo = domeo;
	}
	
	public void init() {
		termsByUrl = new  HashMap<String, MLinkedResource>();
		termsCounterByUrl = new  HashMap<String, Integer>();
		annotationsByTermUrl = new  HashMap<String, ArrayList<MAnnotation>>();
		termsUrlsByAnnotationId = new HashMap<Long, ArrayList<String>>();
	}
	
	public void addInAnnotationsByTermUrl(MAnnotation annotation, MLinkedResource term) {
		if(annotationsByTermUrl.containsKey(term.getUrl())) {
			annotationsByTermUrl.get(term.getUrl()).add(annotation);
		} else {
			ArrayList<MAnnotation> anns = new ArrayList<MAnnotation>();
			anns.add(annotation);
			annotationsByTermUrl.put(term.getUrl(), anns);
		}
	}
	
	public void addInTermsByUrl(MLinkedResource term) {
		if(!termsByUrl.containsKey(term.getUrl())) {
			termsByUrl.put(term.getUrl(), term);
		}
	}
	
	public void removeFromTermsByUrl(MLinkedResource term) {
		if(termsByUrl.containsKey(term.getUrl())) {
			termsByUrl.remove(term.getUrl());
		}
	}
	
	public void addInTermsCounterByUrl(MLinkedResource term) {
		if(termsCounterByUrl.containsKey(term.getUrl())) {
			Integer counter = termsCounterByUrl.get(term.getUrl());
			counter++;
			termsCounterByUrl.put(term.getUrl(), counter++);
		} else {
			termsCounterByUrl.put(term.getUrl(), new Integer(1));
		}
	}
	
	public void removeFromTermsCounterByUrl(MLinkedResource term) {
		if(termsCounterByUrl.containsKey(term.getUrl())) {
			Integer counter = termsCounterByUrl.get(term.getUrl());
			counter--;
			if(counter>0) termsCounterByUrl.put(term.getUrl(), counter++);
			else termsCounterByUrl.remove(term.getUrl());
		} else {
			//TODO log issue
		}
	}
	
	public void addInTermsUrlsByAnnotationId(MAnnotation annotation, MLinkedResource term) {
		if(!termsUrlsByAnnotationId.containsKey(annotation.getLocalId())) {
			ArrayList<String> termsIds = new ArrayList<String>();
			termsIds.add(term.getUrl());
			termsUrlsByAnnotationId.put(annotation.getLocalId(), termsIds);
		} else {
			
		}
	}
	
	public void addAnnotation(MAnnotation annotation) {
		IAnnotationHelper helper = ((IDomeo)_domeo).getAnnotationHelpersManager().getAnnotationHelper(annotation.getClass().getName());
		if(helper!=null) {
			List<MLinkedResource> terms = helper.getTerms(annotation);
			for(MLinkedResource term: terms) {
				addInTermsByUrl(term);
				addInTermsCounterByUrl(term);
				addInAnnotationsByTermUrl(annotation, term);
				addInTermsUrlsByAnnotationId(annotation, term);
			}
		}
	}
	
	public void removeAnnotation(MAnnotation annotation) {
		IAnnotationHelper helper = ((IDomeo)_domeo).getAnnotationHelpersManager().getAnnotationHelper(annotation.getClass().getName());
		if(helper!=null) {
			List<MLinkedResource> terms = helper.getTerms(annotation);
			for(MLinkedResource term: terms) {
				removeFromTermsByUrl(term);
				removeFromTermsCounterByUrl(term);
			}
		}
	}
}
