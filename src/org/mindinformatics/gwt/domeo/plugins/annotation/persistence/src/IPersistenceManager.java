package org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.mindinformatics.gwt.domeo.model.ICache;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.MBibliographicSet;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.model.JsAnnotationSetSummary;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.Element;

public interface IPersistenceManager {

	public boolean registerCache(ICache cache);
	public ICache getCache(String type);
	public boolean addAnnotation(MAnnotation annotation, boolean newSet);
	public boolean addAnnotation(MAnnotation annotation, MAnnotationSet set);
	public boolean addAnnotationOfAnnotation(MCommentAnnotation annotation, MAnnotation targetAnnotation, MAnnotationSet set);
	public boolean addAnnotationOfAnnotation(MAnnotation annotation, MAnnotation tagetAnnotation, MAnnotationSet set);
	public void bindAnnotationOfAnnotation(MAnnotation annotation, MAnnotation targetAnnotation);
	
	public boolean addAnnotationOfTargetResource(MAnnotation annotation, MGenericResource targetResource, MAnnotationSet set);
	public Collection<Long> getAnnotationOfTargetResource();
	public int getAnnotationOfTargetResourceSize();
	
	public ArrayList<MAnnotation> getAnnotationChain(MAnnotation annotation);
	public ArrayList<MAnnotation> getAnnotationCascade(MAnnotation annotation, boolean pruneSubThreads);
	public ArrayList<MAnnotation> getAllAnnotations();
	public ArrayList<MAnnotation> getAllAnnotationsForResource(String url) ;
	public ArrayList<MAnnotation> getAllAnnotationsForImageElement(Element element);
	public LinkedHashMap<String, String> getAllAnnotationTypes();
	public Set<String> getAllTermsUrls();
	public MBibliographicSet getBibliographicSet();
	public boolean isBibliograhicSet();
	public boolean isCurrentSet();
	public MAnnotationSet getCurrentSet();
	public ArrayList<MAnnotationSet> getAllUserSets();
	public ArrayList<MAnnotationSet> getAllDiscussionSets();
	public ArrayList<MLinkedResource> getAllTerms();
	public boolean updateAnnotationAnnotationSet(MAnnotation annotation, MAnnotationSet set);
	public void removeAnnotation(MAnnotation annotation, boolean mark);
	public void removeAnnotationSet(MAnnotationSet annotationSet);
	public void removeDiscussionSet(MAnnotationSet annotationSet);
	public MAnnotationSet getSetByLocalId();
	public MAnnotation getAnnotationByLocalId(Long localId);
	public MAnnotation getAnnotationByUri(String uri);
	public boolean isAnnotationInSet(MAnnotation annotation, Long setLocalId) ;
	public void setCurrentAnnotationSet(MAnnotationSet set);
	public MAnnotationSet createNewAnnotationSet();
	public MAnnotationSet getSetByAnnotationId(Long id);
	public void mockupSavingOfTheAnnotation();
	
	public MAnnotationSet getAnnotationSetById(String id);
	public MAnnotationSet getAnnotationSetByLocalId(String localId);
	
	public void logStatus();
	public void stageCompleted();
	public boolean isResourceLoaded();
	public void setResourceLoaded(boolean resourceLoaded);
	public boolean isDocumentAlreadyLoaded(String newDocument);
	public boolean isWorskspaceUnsaved();
	
	public String getCurrentResourceUrl();
	public MGenericResource getCurrentResource();
	public void setCurrentResource(MGenericResource currentResource);
	
	// Read only retrievals
	public JsArray<JsAnnotationSetSummary> retrieveAnnotationByDocumentUrl(String url, String allowed);
	public JsArray<JsAnnotationSetSummary> retrieveAnnotationByDocumentUrl(String url, String allowed, boolean extend);

	// Read/write
	//public void textmine();
	public void saveAnnotation();
	public void retrieveExistingBibliographySet(IRetrieveExistingBibliographySetHandler handler);
	public void retrieveExistingAnnotationSets(List<String> ids, IRetrieveExistingAnnotationSetHandler handler);
	public void retrieveExistingAnnotationSetList(IRetrieveExistingAnnotationSetListHandler handler);
	public void saveBibliography();

	public void cacheAnnotationOfImage(String src, MAnnotation annotation);
	public ArrayList<MAnnotation> annotationsForImage(String src);
}
