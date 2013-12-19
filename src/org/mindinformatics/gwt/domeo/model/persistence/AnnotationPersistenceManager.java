package org.mindinformatics.gwt.domeo.model.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.helpers.IAnnotationHelper;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.ICache;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.MBibliographicSet;
import org.mindinformatics.gwt.domeo.model.MOnlineImage;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MAnnotationSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTargetSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.commentaries.linear.model.MLinearCommentAnnotation;
import org.mindinformatics.gwt.framework.component.ICommentsRefreshableComponent;
import org.mindinformatics.gwt.framework.component.persistance.src.PersistenceManager;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.ui.east.ASidePanel;
import org.mindinformatics.gwt.framework.component.ui.east.ASideTab;
import org.mindinformatics.gwt.framework.src.ApplicationUtils;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;

/**
 * The class takes care of all annotation related persistence.
 * It extends the persistence manager that takes care of all
 * the structural persistence aspects.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationPersistenceManager extends PersistenceManager {

	// Annotation Caches
	private HashMap<String, ICache> caches = new HashMap<String, ICache>();
	
	public ICache getCache(String type) {
		return caches.get(type);
	}
	
	public boolean registerCache(ICache cache) {
		if(caches.containsValue(cache)) return false;
		caches.put(cache.getCachedType(), cache);
		return true;
	}
	
	
	// ANNOTATIONS SETS
	// ----------------
	/**
	 * The complete list of active sets for this resource.
	 */
	private ArrayList<MAnnotationSet> allUserSets = new ArrayList<MAnnotationSet>();
	/**
	 * The current set represents initially the default (empty) set.
	 * After that, it will become the last updated set.
	 */
	private MAnnotationSet currentSet;
	/**
	 * The bibliographic set is a special set created to collect all
	 * the bibliographic information. 
	 */
	private MBibliographicSet bibliographicSet;
	
	/**
	 * The complete list of active discussions for this resource.
	 */
	private ArrayList<MAnnotationSet> allDiscussionsSets = new ArrayList<MAnnotationSet>();
	
	private HashMap<Long, MAnnotationSet> annotationsSetsByAnnotationLocalIdCache =
		new  HashMap<Long, MAnnotationSet>();

	// ANNOTATIONS
	// -----------
	/**
	 * Caches the annotations by type. The type is the full path of the
	 * class accommodating the annotation. The annotations are then organized
	 * in a map were the key are the local ids.
	 */
	private HashMap<String, HashMap<Long, MAnnotation>> annotationsByTypeCache =
		new  HashMap<String, HashMap<Long, MAnnotation>>();
	/**
	 * Caches the annotations by local ids.
	 */
	private HashMap<Long, MAnnotation> annotationsByLocalIdCache =
		new  HashMap<Long, MAnnotation>();
	
	private HashMap<Long, Long> annotationsOfAnnotations =
			new  HashMap<Long, Long>();
	
	private HashMap<String, ArrayList<MAnnotation>> annotationsOfImages =
			new HashMap<String, ArrayList<MAnnotation>>();
	
	private HashMap<Long, MGenericResource> annotationsOfTargetResource =
			new  HashMap<Long, MGenericResource>();
	
	private HashMap<String, ArrayList<MAnnotation>> qualifiersByTermUrl =
		new  HashMap<String, ArrayList<MAnnotation>>();
	
	private HashMap<String, MLinkedResource> termsByUrl =
		new  HashMap<String, MLinkedResource>();
	
	
	public HashMap<MAnnotation, ArrayList<MLinearCommentAnnotation>> commentsOnAnnotationCache 
		= new HashMap<MAnnotation, ArrayList<MLinearCommentAnnotation>>();
	
//	private HashMap<Long, Integer> annotationsOfAnnotationsCounter =
//			new  HashMap<Long, Integer>();
	
	/**
	 * Constructor
	 * @param domeo	Pointer to the main application
	 */
	public AnnotationPersistenceManager(IDomeo domeo) {
		super(domeo);
	}
	
	/**
	 * Initialize the persistence manager
	 */
	public void init() {
		resetCurrentResource(); // Super class
		initialize(); 			// Cache
	}
	
	/**
	 *  Initialize annotation persistence manager
	 */
	private void initialize() {
		initializeAnnotationSetsCache();
		initializeAnnotationCache();
	}
	
	// SAVING 
	// ----------------------
	public void mockupSavingOfTheAnnotation() {
		_application.getLogger().warn("FAKE SAVING (TESTING)", this, "Fake saving for testing....");
		for(MAnnotationSet set:allUserSets) {
			for(MAnnotation annotation: set.getAnnotations()) {
				annotation.setHasChanged(false);
				annotation.setNewVersion(false);
				annotation.setVersionNumber("<test>");
			}
			set.setHasChanged(false);
			set.setVersionNumber("<test>");
			set.setLastSavedOn(new Date());
		}
		
		for(MAnnotationSet set:allDiscussionsSets) {
			for(MAnnotation annotation: set.getAnnotations()) {
				annotation.setHasChanged(false);
				annotation.setNewVersion(false);
				annotation.setVersionNumber("<test>");
			}
			set.setHasChanged(false);
			set.setVersionNumber("<test>");
			set.setLastSavedOn(new Date());
		}
		
		((IDomeo)_application).refreshAnnotationComponents();
	}
	
	public boolean isWorskspaceUnsaved() {
		boolean unsaved = false;
		for(MAnnotationSet set: ((IDomeo)_application).getAnnotationPersistenceManager().getAllUserSets()) {
			if(set.getHasChanged() && set.getAnnotations().size()>0) unsaved = true;
		}
		return unsaved;
	}
	
	// ANNOTATIONS SETS CACHE
	// ----------------------	
	/**
	 * Initialization of the annotation cache after loading another
	 * document
	 */
	private void initializeAnnotationSetsCache() {
		allUserSets = new ArrayList<MAnnotationSet>();
		allDiscussionsSets = new ArrayList<MAnnotationSet>();
		bibliographicSet = null;
		currentSet = null;
		termsByUrl.clear(); 
	}
	
	/**
	 * Returns a set by local id.
	 * @return	The set identified by the requested local id
	 */
	public MAnnotationSet getSetByLocalId() {
		return null;
	}
	
	public ArrayList<MAnnotationSet> getAllUserSets() {
		return allUserSets;
	}
	
	public boolean isAnnotationSetLoaded(String setId) {
		_application.getLogger().debug(this, "isAnnotationSetLoaded 1: " + setId);
		for(MAnnotationSet userSet: allUserSets) {
			_application.getLogger().debug(this, "isAnnotationSetLoaded 2 " + userSet.getIndividualUri());
			if(userSet.getIndividualUri().equals(setId)) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<MAnnotationSet> getAllDiscussionSets() {
		return allDiscussionsSets;
	}
	
	/**
	 * The application is always servicing a set to collect the 
	 * created annotation. At initialization the set is a default
	 * set created by the application itself. After the initialization
	 * the set is the last set used.
	 * @return	The current set in use.
	 */
	public MAnnotationSet getCurrentSet() {
		if(currentSet==null) {
			MAnnotationSet set = AnnotationFactory.createAnnotationSet(
					_application.getAgentManager().getSoftware(),
				_application.getAgentManager().getUserPerson(), currentResource, "Default Set", "The default set is created automatically by Domeo when no other set is existing.");
			_application.getLogger().command("CREATED NEW SET", this, "with id " + set.getLocalId());
			setDefaultAnnotationSetAccessPolicy(set);
			setCurrentAnnotationSet(set);
			allUserSets.add(set);
		}
		return currentSet;
	}
	
	public boolean isCurrentSet() {
		return currentSet!=null;
	}
	
	/**
	 * The bibliographic set is a special set that collects all the 	
	 * bibliographic information of a document.
	 * @return	The bibliographic set.
	 */
	public MBibliographicSet getBibliographicSet() {
		if(bibliographicSet==null) {
			bibliographicSet = AnnotationFactory.createBibliographicSet(
					_application.getAgentManager().getSoftware(), 
				_application.getAgentManager().getUserPerson(), currentResource, "Bibliography", "Bibliographic Set");
			bibliographicSet.setHasChanged(false);
		}
		setDefaultAnnotationSetAccessPolicy(bibliographicSet);
		return bibliographicSet;
	}
	
	public boolean isBibliograhicSet() {
		return bibliographicSet!=null;
	}
	
	private void setDefaultAnnotationSetAccessPolicy(MAnnotationSet set) {
		String defaultSetPrivacy = ApplicationUtils.getDefaultSetPrivacy();
		if(defaultSetPrivacy!=null && defaultSetPrivacy.trim().equals("private"))
			((IDomeo)_application).getAnnotationAccessManager().setAnnotationSetPrivate(set);
		else 
			((IDomeo)_application).getAnnotationAccessManager().setAnnotationSetPublic(set);
		
	}
	
	public MAnnotationSet getSetByAnnotationId(Long id) {
		return annotationsSetsByAnnotationLocalIdCache.get(id);
	}
	
	public MAnnotationSet createNewAnnotationSet() {
		MAnnotationSet set= AnnotationFactory.createNewAnnotationSet(
				_application.getAgentManager().getSoftware(),
				_application.getAgentManager().getUserPerson(), currentResource);
		// TODO Check default access policy in the preferences
		setDefaultAnnotationSetAccessPolicy(set);
		allUserSets.add(set);
		_application.getLogger().command("CREATED NEW SET", this, "with id " + set.getLocalId());
		return set;
	}
	
	public void loadAnnotationSet(MAnnotationSet set) {
		setDefaultAnnotationSetAccessPolicy(set);
		if(set.getType().equals(IDomeoOntology.discussionSet)) {
			allDiscussionsSets.add(set);
		} else {
			allUserSets.add(set);
			this.setCurrentAnnotationSet(set);
		}
		_application.getLogger().command("LOADED NEW SET", this, "with id " + set.getLocalId());
	}
	
	public MAnnotationSet getCurrentAnnotationSet() {
		return currentSet;
	}
	
	public void setCurrentAnnotationSet(MAnnotationSet set) {
		_application.getLogger().command(this, "Selected set with id " + set.getLocalId());
		currentSet = set;
	}
	
	public boolean isAnnotationInSet(MAnnotation annotation, Long setLocalId) {
		return annotationsSetsByAnnotationLocalIdCache.get(annotation.getLocalId()).getLocalId().equals(setLocalId);
	}
	
	public MAnnotationSet getAnnotationSetById(String id) {
		for(MAnnotationSet set: allUserSets) {
			if(set.getIndividualUri().equals(id)) {
				return set;
			}
		}
		for(MAnnotationSet set: allDiscussionsSets) {
			if(set.getIndividualUri().equals(id)) {
				return set;
			}
		}
		return null;
	}
	
	public MAnnotationSet getAnnotationSetByLocalId(String localId) {
		for(MAnnotationSet set: allUserSets) {
			if(Long.toString(set.getLocalId()).equals(localId.trim())) {
				return set;
			}
		}
		for(MAnnotationSet set: allDiscussionsSets) {
			if(Long.toString(set.getLocalId()).equals(localId.trim())) {
				return set;
			}
		}
		return null;
	}

	public void removeAnnotationSet(MAnnotationSet set) {
		_application.getLogger().command("REMOVING SET", this, "with id " + set.getLocalId());
		
		//ArrayList<MAnnotationSet> allSets = new ArrayList<MAnnotationSet>();
		//allSets.addAll(allDiscussionsSets);
		//allSets.addAll(allUserSets);
		
		for(MAnnotationSet _set: allUserSets) {
			if(_set.getLocalId()==set.getLocalId()) {
				if(currentSet.getLocalId().equals(set.getLocalId())) {
					currentSet = null;
				}
				if(_set.getLastSavedOn()==null) {
					//Window.alert("Unsaved " + _set.getLabel());
					((IDomeo)_application).removeAnnotationSetTab(set);
					// reset set permissions
					((IDomeo)_application).getAnnotationAccessManager().clearAnnotationSet(_set);
					((IDomeo)_application).getContentPanel().getAnnotationFrameWrapper().removeAnnotationSetAnnotation(_set, false);
					allUserSets.remove(_set);
					((IDomeo)_application).refreshAnnotationComponents();
					break;
				} else {
					//Window.alert("Saved");
					((IDomeo)_application).removeAnnotationSetTab(set);
					((IDomeo)_application).getContentPanel().getAnnotationFrameWrapper().removeAnnotationSetAnnotation(_set, true);
					_set.setIsDeleted(true);
					_set.setHasChanged(true);
					((IDomeo)_application).refreshAnnotationComponents();
				}
			}
		}
	}
	
	public void removeDiscussionSet(MAnnotationSet set) {
		_application.getLogger().command("REMOVING SET", this, "with id " + set.getLocalId());
		
//		ArrayList<MAnnotationSet> allSets = new ArrayList<MAnnotationSet>();
//		allSets.addAll(allDiscussionsSets);
//		allSets.addAll(allUserSets);
		
		for(MAnnotationSet _set: allDiscussionsSets) {
			if(_set.getLocalId()==set.getLocalId()) {
				if(currentSet.getLocalId().equals(set.getLocalId())) {
					currentSet = null;
				}
				if(_set.getLastSavedOn()==null) {
					//Window.alert("Unsaved " + _set.getLocalId());
					((IDomeo)_application).removeAnnotationSetTab(set);
					// reset set permissions
					((IDomeo)_application).getAnnotationAccessManager().clearAnnotationSet(_set);
					removeAnnotationOfTargetResource(_set);
					((IDomeo)_application).getContentPanel().getAnnotationFrameWrapper().removeAnnotationSetAnnotation(_set, false);
					allDiscussionsSets.remove(_set);
					
					((IDomeo)_application).refreshAnnotationComponents();
					break;
				} else {
					//Window.alert("Saved");
					((IDomeo)_application).removeAnnotationSetTab(set);
					((IDomeo)_application).getContentPanel().getAnnotationFrameWrapper().removeAnnotationSetAnnotation(_set, true);
					_set.setIsDeleted(true);
					_set.setHasChanged(true);
					((IDomeo)_application).refreshAnnotationComponents();
				}
			}
		}
	}
	
	// ANNOTATIONS CACHE
	// ------------------	
	/**
	 * Initialization of the annotation cache after loading another
	 * document
	 */
	private void initializeAnnotationCache() {
		annotationsByTypeCache = new  HashMap<String, HashMap<Long, MAnnotation>>();
		annotationsByLocalIdCache = new  HashMap<Long, MAnnotation>();
		commentsOnAnnotationCache = new HashMap<MAnnotation, ArrayList<MLinearCommentAnnotation>>();
		annotationsOfTargetResource.clear();
		annotationsOfAnnotations.clear();
//		annotationsOfAnnotationsCounter.clear();
		annotationsOfImages.clear();
		
		for(ICache c:caches.values()) {
			c.resetCache();
		}
	}
	
	public boolean addAnnotation(MAnnotation annotation, boolean newSet) {
		MAnnotationSet set;
		if(newSet) { 
			set = createNewAnnotationSet();
		} else {
			set = currentSet;
		}
		return addAnnotation(annotation, set);
	}
	
	public boolean addAnnotationOfAnnotation(MCommentAnnotation annotation, MAnnotation targetAnnotation, MAnnotationSet set) {
		//Window.alert("Cache comment");
		//annotationsOfAnnotationsCounter.put(annotation.getLocalId(), arg1);
		//cacheCommentOnAnnotation(targetAnnotation, annotation);
		return addAnnotationOfAnnotation((MAnnotation)annotation, targetAnnotation, set);
	}
	
	public boolean addAnnotationOfAnnotation(MLinearCommentAnnotation annotation, MAnnotation targetAnnotation, MAnnotationSet set) {
		//Window.alert("Cache comment " + (targetAnnotation.getSelector() instanceof MTargetSelector));
		
		if(!(targetAnnotation.getSelector() instanceof MTargetSelector)) 
			cacheCommentOnAnnotation(targetAnnotation, annotation);
		return addAnnotationOfAnnotation((MAnnotation)annotation, targetAnnotation, set);
	}
	
	// -------------------------------------------------------------------------------------------
	public void cacheCommentOnAnnotation(MAnnotation target, MLinearCommentAnnotation comment) {
		if(commentsOnAnnotationCache.containsKey(target)) {
			ArrayList<MLinearCommentAnnotation> comments = commentsOnAnnotationCache.get(target);
			comments.add(comment);
		} else {
			ArrayList<MLinearCommentAnnotation> comments = new ArrayList<MLinearCommentAnnotation>();
			comments.add(comment);
			commentsOnAnnotationCache.put(target, comments);
		}
	}
	
	public Set<MAnnotation> getListOfAnnotationCommentedOn() {
		return commentsOnAnnotationCache.keySet();
	}
	
	public int getCommentsOnAnnotationCounter(MAnnotation target) {
		if(commentsOnAnnotationCache.containsKey(target)) {
			return commentsOnAnnotationCache.get(target).size();
		}
		return 0;
	}
	// -------------------------------------------------------------------------------------------
	
	
	/**
	 * Adds an annotation item to the cache. This method indexes the item 
	 * according to the different existing criteria.
	 * @param annotation	The annotation item to be cached.
	 */
	public boolean addAnnotationOfAnnotation(MAnnotation annotation, MAnnotation targetAnnotation, MAnnotationSet set) {
		try {
			annotationsOfAnnotations.put(annotation.getLocalId(), targetAnnotation.getLocalId());
			targetAnnotation.addAnnotatedBy(annotation);
			return addAnnotation(annotation, set);
		} catch(Exception exc) {
			_application.getLogger().exception(this, "The annotation creation failed " + annotation.getClass().getName() + "-" + annotation.getLocalId());
			return false;
		}
	}
	
	public void bindAnnotationOfAnnotation(MAnnotation annotation, MAnnotation targetAnnotation) {
		try {
			annotationsOfAnnotations.put(annotation.getLocalId(), targetAnnotation.getLocalId());
			targetAnnotation.addAnnotatedBy(annotation);
		} catch(Exception exc) {
			_application.getLogger().exception(this, "The annotation creation failed " + annotation.getClass().getName() + "-" + annotation.getLocalId());
		}
	}
	
	public Collection<Long> getAnnotationOfTargetResource() {
		return annotationsOfTargetResource.keySet();
	}
	
	public int getAnnotationOfTargetResourceSize() {
		return annotationsOfTargetResource.size();
	}
	
	public void removeAnnotationOfTargetResource(MAnnotationSet set) {
		for(MAnnotation ann: set.getAnnotations()) {
			annotationsOfTargetResource.remove(ann.getLocalId());
		}
	}
	
	public boolean addAnnotationOfTargetResource(MAnnotation annotation, MGenericResource targetResource, MAnnotationSet set) {
		try {
			//setDefaultAnnotationSetAccessPolicy(set);
			allDiscussionsSets.add(set);
			annotationsOfTargetResource.put(annotation.getLocalId(), targetResource);
			return addAnnotation(annotation, set);
		} catch(Exception exc) {
			_application.getLogger().exception(this, "The annotation creation failed " + annotation.getClass().getName() + "-" + annotation.getLocalId());
			return false;
		}
	}
	
	public boolean addAnnotationOfTargetResource(MAnnotation annotation, MGenericResource targetResource, MAnnotationSet set, boolean isPublic) {
		try {
			if(isPublic) ((IDomeo)_application).getAnnotationAccessManager().setAnnotationSetPublic(set);
			else setDefaultAnnotationSetAccessPolicy(set);
			allDiscussionsSets.add(set);
			annotationsOfTargetResource.put(annotation.getLocalId(), targetResource);
			return addAnnotation(annotation, set);
		} catch(Exception exc) {
			_application.getLogger().exception(this, "The annotation creation failed " + annotation.getClass().getName() + "-" + annotation.getLocalId());
			return false;
		}
	}		
	
	/**
	 * Adds an annotation item to the cache. This method indexes the item 
	 * according to the different existing criteria.
	 * @param annotation	The annotation item to be cached.
	 */
	public boolean addAnnotation(MAnnotation annotation, MAnnotationSet set) {
		try {
			// Adding annotation to the set
			set.addAnnotation(annotation);
			
			// Caching annotation and set
			if(!annotationsSetsByAnnotationLocalIdCache.containsKey(annotation.getLocalId())) {
				annotationsSetsByAnnotationLocalIdCache.put(annotation.getLocalId(), set);
			}  else {
				_application.getLogger().exception(this, "An annotation has been generated with already existing id:  " + annotation.getLocalId());
				return false;
			}
			
			// Caching by Type and Id
			HashMap<Long, MAnnotation> annotationsByLocalId = annotationsByTypeCache.get(annotation.getClass().getName());
			if(annotationsByLocalId!=null) {
				if(!annotationsByLocalId.containsKey(annotation.getLocalId())) {
					annotationsByLocalId.put(annotation.getLocalId(), annotation);
				} else {
					_application.getLogger().exception(this, "An annotation has been generated with already existing id:  " + annotation.getLocalId());
					return false;
				}
			} else {
				HashMap<Long, MAnnotation> newHash = new HashMap<Long, MAnnotation>();
				newHash.put(annotation.getLocalId(), annotation);
				annotationsByTypeCache.put(annotation.getClass().getName(), newHash);
			}
			
			// Caching by Id
			if(!annotationsByLocalIdCache.containsKey(annotation.getLocalId())) {
				annotationsByLocalIdCache.put(annotation.getLocalId(), annotation);
			} else {
				// ?????
			}
			
			// Caching terms
			// TODO revise, the management is not complete, removal is not happening
			//if(annotation instanceof MQualifierAnnotation) { 
				IAnnotationHelper helper = ((IDomeo)_application).getAnnotationHelpersManager().getAnnotationHelper(annotation.getClass().getName());
				if(helper!=null) {
					List<MLinkedResource> terms = helper.getTerms(annotation);
					for(MLinkedResource term: terms) {
						if(qualifiersByTermUrl.containsKey(term.getUrl())) {
							ArrayList<MAnnotation> list = qualifiersByTermUrl.get(term.getUrl());
							list.add(annotation);
						} else {
							ArrayList<MAnnotation> anns = new ArrayList<MAnnotation>();
							anns.add(annotation);
							qualifiersByTermUrl.put(term.getUrl(), anns);
						}
						if(!termsByUrl.containsKey(term.getUrl())) {
							termsByUrl.put(term.getUrl(), term);
						}
					}
				}
			//}
				
			// Cache by type
			for(ICache cache: caches.values()) {
				if(annotation.getClass().getName().equals(cache.getCachedType())) {
					cache.cacheAnnotation(annotation);
					break;
				}
			}
			
			//annotation.get
			//annotationsByQualifiers
			
			return true;
		} catch(Exception exc) {
			_application.getLogger().exception(this, "The annotation creation failed " + annotation.getClass().getName() + "-" + annotation.getLocalId());
			return false;
		}
	}
	
	public boolean updateAnnotationAnnotationSet(MAnnotation annotation, MAnnotationSet set) {
		// Remove item from previous set
		MAnnotationSet oldSet = annotationsSetsByAnnotationLocalIdCache.get(annotation.getLocalId());
		oldSet.removeAnnotation(annotation);
		_application.getLogger().info(this, "Removed annotation " + annotation.getLocalId() + " from set " + set.getLocalId());
		// Refresh of the annotation set caches
		annotationsSetsByAnnotationLocalIdCache.put(annotation.getLocalId(), set);
		// Add item to new set
		set.addAnnotation(annotation);
		_application.getLogger().info(this, "Added annotation " + annotation.getLocalId() + " to set " + set.getLocalId());
		
		return true;
	}
	
	public void removeAnnotation(MAnnotation annotation, boolean mark) {	
		try {
			if(annotation.getSelector() instanceof MAnnotationSelector) {
				//Window.alert("removal " + annotation.getClass().getName());
				if(annotation instanceof MCommentAnnotation) {
					// Remove cross pointers if chaining is implemented
					ArrayList<MAnnotation> anns = annotation.getAnnotatedBy();
					((MAnnotationSelector)annotation.getSelector()).getAnnotation().getAnnotatedBy().remove(annotation);
					((MAnnotationSelector)annotation.getSelector()).getAnnotation().getAnnotatedBy().addAll(anns);
				} else if(annotation instanceof MLinearCommentAnnotation) {
					ArrayList<MLinearCommentAnnotation> l = commentsOnAnnotationCache.get(((MAnnotationSelector)annotation.getSelector()).getAnnotation());
					//Window.alert("removal b" +  l.size());
					l.remove(annotation);
					//Window.alert("removal a" +  l.size());
					//Window.alert("removal " + ((MAnnotationSelector)annotation.getSelector()).getAnnotation().getAnnotatedBy().size());
					//ArrayList<MAnnotation> anns = annotation.getAnnotatedBy();
					((MAnnotationSelector)annotation.getSelector()).getAnnotation().getAnnotatedBy().remove(annotation);
					//Window.alert("removal " + ((MAnnotationSelector)annotation.getSelector()).getAnnotation().getAnnotatedBy().size());
					
					ASideTab tab = ((IDomeo)_application).getLinearCommentsSideTab();//.getDiscussionSideTab();
					ASidePanel panel = ((IDomeo)_application).getSidePanelsFacade().getPanelForTab(tab);
					List<MAnnotation> annotations = new ArrayList<MAnnotation>();
					//annotations.add(annotation);
					ArrayList<MAnnotation>  anns= ((IDomeo)_application).getPersistenceManager().getAnnotationCascade(((MAnnotationSelector)annotation.getSelector()).getAnnotation(), true);
					annotations.addAll(anns);
					((ICommentsRefreshableComponent)panel).refresh(annotations);		
				}
			}
			HashMap<Long, MAnnotation> annotationsByLocalId = annotationsByTypeCache.get(annotation.getClass().getName());
			annotationsByLocalId.remove(annotation.getLocalId());
			annotationsByLocalIdCache.remove(annotation.getLocalId());
			MAnnotationSet set = annotationsSetsByAnnotationLocalIdCache.get(annotation.getLocalId());
			if(!mark) set.removeAnnotation(annotation);
		} catch (Exception e) {
			_application.getLogger().exception(this, e.getMessage());
		}
	}
	
	/**
	 * Returns the annotation by local id. The annotation has also a global identifier
	 * once that is created.
	 * @param localId The annotation local id
	 * @return The annotation locally identified by the local id
	 */
	public MAnnotation getAnnotationByLocalId(Long localId) {
		return annotationsByLocalIdCache.get(localId);
	}
	
	public MAnnotation getAnnotationByUri(String uri) {
		Collection<MAnnotation> annotations = annotationsByLocalIdCache.values();
		Iterator<MAnnotation> annIterator = annotations.iterator();
		while(annIterator.hasNext()) {
			MAnnotation ann = annIterator.next();
			//Window.alert(uri + " getAnnotationByUri: " + ann.getIndividualUri());
			if(ann.getIndividualUri().equals(uri)) return ann; 
		}
		return null;
	}
	
	
	
	public ArrayList<MAnnotation> getAllAnnotations() {
		return new ArrayList<MAnnotation>(annotationsByLocalIdCache.values());
	}
	
	public ArrayList<MAnnotation> getAllAnnotationsForResource(String url) {
		ArrayList<MAnnotation> anns = new ArrayList<MAnnotation>();
		for(MAnnotation annotation: annotationsByLocalIdCache.values()) {
			if(annotation.getSelector()!=null && annotation.getSelector().getTarget().getUrl().equals(url)) {
				anns.add(annotation); 
			}
		}
		return anns;
	}
	
	public ArrayList<MAnnotation> getAllAnnotationsForImageElement(Element element) {
		ArrayList<MAnnotation> anns = new ArrayList<MAnnotation>();
		for(MAnnotation annotation: annotationsByLocalIdCache.values()) {
			if(annotation.getSelector().getTarget() instanceof MOnlineImage) {
				if(((MOnlineImage)annotation.getSelector().getTarget()).getImage()==element) 
					anns.add(annotation); 
			}
		}
		return anns;
	}
	
	/**
	 * Returns the list of annotations chained starting from the first one.
	 * For reverse chaining see getAnnotationChain.
	 * @param annotation
	 * @return
	 */
	public ArrayList<MAnnotation> getAnnotationCascade(MAnnotation annotation, boolean pruneSubThreads) {
		//Window.alert("retrieveAnnotatedBy: " + annotation.getIndividualUri());
		ArrayList<MAnnotation> annotations = new ArrayList<MAnnotation>();
		annotations.add(annotation);
		if(annotation.getAnnotatedBy().size()>0) {
			// TODO return all items not only the first one
			// If it is a branch just tone comment
			// Order by time???
			for(MAnnotation ann: annotation.getAnnotatedBy()) {
				retrieveAnnotatedBy(annotations, ann);
			}
		}
		return annotations;
	}
	
	private void retrieveAnnotatedBy(ArrayList<MAnnotation> annotations, MAnnotation annotation) {
		annotations.add(annotation);
		for(MAnnotation ann: annotation.getAnnotatedBy()) {
			retrieveAnnotatedBy(annotations, ann);
		}
	}
	
	/**
	 * Returns the list of annotations chain from the given annotation.
	 * The list is then reversed so that the annotations can appear in the right order.
	 * @param annotation The annotation to start from
	 * @return The list of annotations chained with the given one.
	 */
	public ArrayList<MAnnotation> getAnnotationChain(MAnnotation annotation) {
		ArrayList<MAnnotation> annotations = new ArrayList<MAnnotation>();
		annotations.add(annotation);
		retrieveTargetAnnotation(annotations, annotation);
		Collections.reverse(annotations);
		return annotations;
	}
	
	private void retrieveTargetAnnotation(ArrayList<MAnnotation> annotations, MAnnotation annotation) {
		MAnnotation ann = annotationsByLocalIdCache.get(annotationsOfAnnotations.get(annotation.getLocalId()));
		if(ann!=null) {
			annotations.add(ann);
			retrieveTargetAnnotation(annotations, ann);
		} else {
			ann = annotationsByLocalIdCache.get(annotationsOfTargetResource.get(annotation.getLocalId()));
			if(ann!=null) {
				annotations.add(ann);
				retrieveTargetAnnotation(annotations, ann);
			}
		} 
	}
	
	public LinkedHashMap<String, String> getAllAnnotationTypes() {
		LinkedHashMap<String, String> labels = new LinkedHashMap<String, String>();
		for(String name: annotationsByTypeCache.keySet()) {
			Iterator<MAnnotation> it = annotationsByTypeCache.get(name).values().iterator();
			if(it.hasNext()) {
				MAnnotation ann = it.next();
				labels.put(ann.getClass().getName(), ann.getLabel());
			}
		}
		return labels;
	}
	
	public Set<String> getAllTermsUrls() {
		return termsByUrl.keySet();
	}
	
	public ArrayList<MLinkedResource> getAllTerms() {
		ArrayList<MLinkedResource> list = new ArrayList<MLinkedResource>();
		list.addAll(termsByUrl.values());
		return list;
	}
	
	// Images annotation cache
	// -----------------------
	public void cacheAnnotationOfImage(String src, MAnnotation annotation) {
		_application.getLogger().debug(this, "Caching " + src);
		if(annotationsOfImages.containsKey(src)) {
			annotationsOfImages.get(src).add(annotation);
		} else {
			ArrayList<MAnnotation> annotations = new ArrayList<MAnnotation>();
			annotations.add(annotation);
			annotationsOfImages.put(src, annotations);
		}
	}
	
	public ArrayList<MAnnotation> annotationsForImage(String src) {
		_application.getLogger().debug(this, "Looking for " + src);
		Set<String> urls = annotationsOfImages.keySet();
		for(String url: urls) {
			_application.getLogger().info(this, "annotationsForImage: " + src + " - " + url);
			if(url.endsWith(src) || url.equals(src)) return annotationsOfImages.get(url);
		}
		
		return new ArrayList<MAnnotation>();
	}
	
	public void uncacheAnnotationOfImage(String src, MAnnotation annotation) {
		
	}
}
