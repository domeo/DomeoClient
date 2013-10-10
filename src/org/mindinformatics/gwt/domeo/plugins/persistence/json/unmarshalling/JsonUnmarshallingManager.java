package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.linkeddata.model.JsoLinkedDataResource;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationCitationReference;
import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.MBibliographicSet;
import org.mindinformatics.gwt.domeo.model.MOnlineImage;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPermissionsOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MAnnotationSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MImageInDocumentSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTargetSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.commentaries.linear.model.LinearCommentsFactory;
import org.mindinformatics.gwt.domeo.plugins.annotation.commentaries.linear.model.MLinearCommentAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.contentasrdf.model.MContentAsRdf;
import org.mindinformatics.gwt.domeo.plugins.annotation.curation.model.CurationFactory;
import org.mindinformatics.gwt.domeo.plugins.annotation.curation.model.JsAnnotationCuration;
import org.mindinformatics.gwt.domeo.plugins.annotation.curation.model.MCurationToken;
import org.mindinformatics.gwt.domeo.plugins.annotation.highlight.model.MHighlightAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.IMicroPublicationsOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublication;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpDataImage;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpQualifier;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpReference;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpRelationship;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMpStatement;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MicroPublicationFactory;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.serialization.JsoMicroPublication;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.serialization.JsoMicroPublicationAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.serialization.JsoMpDataImage;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.serialization.JsoMpRelationship;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.serialization.JsoMpStatement;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.JsoAntibody;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.JsoAntibodyAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.JsoAntibodyUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibody;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibodyAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibodyUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.NifAntibodyFactory;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.PostitType;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationHighlight;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationPostIt;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationReference;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSelector;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationTarget;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsBibliographicSet;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsImageInDocumentSelector;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsPublicationArticleReference;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsTextQuoteSelector;
import org.mindinformatics.gwt.framework.component.agents.model.JsoAgent;
import org.mindinformatics.gwt.framework.component.agents.src.AgentsFactory;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
import org.mindinformatics.gwt.framework.component.resources.serialization.JsonGenericResource;
import org.mindinformatics.gwt.framework.model.agents.IDatabase;
import org.mindinformatics.gwt.framework.model.agents.IPerson;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;
import org.mindinformatics.gwt.framework.model.references.IReferences;
import org.mindinformatics.gwt.framework.model.references.ISelfReference;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;
import org.mindinformatics.gwt.utils.src.HtmlUtils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */ 
public class JsonUnmarshallingManager {

	public static final String LOGGING_PREFIX = "JSON UNMARSHALLING";
	
	private static IDomeo _domeo;
	
	public JsonUnmarshallingManager(IDomeo domeo) {
		_domeo = domeo;
		registerDeserializers();
	}
		
	// ------------------------------------------------------------------------
	//  General
	// ------------------------------------------------------------------------
	private final native String getObjectId(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalId]; 
	}-*/;
	
	private final native boolean hasMultipleTypes(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType] instanceof Array; 
	}-*/;
	private final native String getObjectType(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;
	private final native JsArrayString getObjectTypes(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;
	
	// ------------------------------------------------------------------------
	//  Annotation Ontology
	// ------------------------------------------------------------------------
	private final native JsArray<JsAnnotationTarget> getTargets(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::hasTarget]; 
	}-*/;
	private final native String getSelectorTargetUrl(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::source]; 
	}-*/;
	private final native String getImageDisplayUrl(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::displaySource]; 
	}-*/;
	
	public final native JsArray<JsoLinkedDataResource> getSemanticTags(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::semanticTag]; 
	}-*/;
	
	public final native JsArray<JsoAntibodyUsage> getAntibodyUsage(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::content]; 
	}-*/;
	

	
	/**
	 * Selects the right unmarshaller for the specified class name.
	 * @param className	The name of the class to be unmarshalled
	 * @return The unmarshalled class or null if exceptions raised
	 */
	private IUnmarshaller selectUnmarshaller(String className) {
		IUnmarshaller unmarshaller = unmarshallers.get(className);
		if(unmarshaller==null) {
			_domeo.getLogger().exception(this, LOGGING_PREFIX, "Unmarshaller not found for: " + className);
			throw new RuntimeException("Unmarshaller not found for: " + className);
		}
		return unmarshaller;
	}
	
	public void unmarshallTextmining(JsAnnotationSet jsonSet) {
		annotationSelectorsLazyBinding.clear();
		creatorAgentsLazyBinding.clear();
		
		try {
			MAnnotationSet set = unmarshallAnnotationSet(jsonSet, IUnmarshaller.IMPORT_VALIDATION);
			((AnnotationPersistenceManager) _domeo.getPersistenceManager()).loadAnnotationSet(set);
			
			// Unmarshalling agents
			JsArray<JsoAgent> jsonAgents = jsonSet.getAgents();
			for(int j=0; j<jsonAgents.length(); j++) {
				if(getObjectType(jsonAgents.get(j)).equals(IPerson.TYPE) || 
						getObjectType(jsonAgents.get(j)).equals(ISoftware.TYPE) || 
							getObjectType(jsonAgents.get(j)).equals(IDatabase.TYPE)) {
					_domeo.getAgentManager().addAgent(jsonAgents.get(j));
				} else {
					Window.alert("To request: " + getObjectType(jsonAgents.get(j)));
				}
			}
			
			// Unmarshalling permissions
			if(jsonSet.getPermissions().isLocked().equals("true")) set.setIsLocked(true);
			else set.setIsLocked(false);
			_domeo.getAnnotationAccessManager().setAnnotationSetAccess(set, jsonSet.getPermissions().getPermissionType());
			if(jsonSet.getPermissions().getPermissionType().equals(IPermissionsOntology.accessGroups)) {
				Set<IUserGroup> groups = new HashSet<IUserGroup>();
				for(int ii=0; ii<jsonSet.getPermissions().getPermissionDetails().getAllowedGroups().length(); ii++) {
					String uuid = jsonSet.getPermissions().getPermissionDetails().getAllowedGroups().get(ii).getUuid();
					IUserGroup group = _domeo.getUserManager().getUserGroup(uuid); 
					if(group!=null) {
						groups.add(group);
					}
				}
				if(groups.size()>0) _domeo.getAnnotationAccessManager().setAnnotationSetGroups(set, groups);
			}
			
			// Unmarshalling annotations
			JsArray<JavaScriptObject> jsonAnnotations = jsonSet.getAnnotation();
			for(int j=0; j<jsonAnnotations.length(); j++) {
				_domeo.getLogger().debug(this, "" + j);
				boolean isGeneral = false;
				// Selectors
				ArrayList<MSelector> selectors = new ArrayList<MSelector>();
				JsArray<JsAnnotationTarget> targets = getTargets(jsonAnnotations.get(j));
				for(int z=0; z<targets.length(); z++) {
					JsAnnotationTarget target = targets.get(z);
					if(target.getSelector()!=null) {
						String selectorType = getObjectType(target.getSelector());
						_domeo.getLogger().debug(this, selectorType);
						if(selectorType.equals(MTextQuoteSelector.TYPE)) {
							JsTextQuoteSelector sel = (JsTextQuoteSelector) target.getSelector();
							MTextQuoteSelector selector = unmarshallPrefixSuffixTextSelector(sel, IUnmarshaller.OFF_VALIDATION);
							selectors.add(selector);
						} else if(selectorType.equals(MImageInDocumentSelector.TYPE)) {
							JsImageInDocumentSelector sel = (JsImageInDocumentSelector) target.getSelector();
							MImageInDocumentSelector selector = unmarshallImageInDocumentSelector(sel, IUnmarshaller.OFF_VALIDATION);
							selector.getTarget().setUrl(getSelectorTargetUrl(target));
							selectors.add(selector);
						} else if(selectorType.equals(MAnnotationSelector.TYPE)) {
							JsAnnotationSelector sel = (JsAnnotationSelector) target.getSelector();
							MAnnotationSelector selector = unmarshallAnnotationSelector(sel, IUnmarshaller.OFF_VALIDATION);
							
							// TODO to improve with a Virtual Annotation
							MAnnotation annotation = new MPostItAnnotation();
							annotation.setIndividualUri(sel.getAnnotation());
							selector.setAnnotation(annotation);
							selectors.add(selector);
						}
					} else {
						String targetType = getObjectType(target);
						_domeo.getLogger().debug(this, targetType);
						if(targetType.equals(MTargetSelector.TYPE)) {
							
							MTargetSelector selector = AnnotationFactory.cloneTargetSelector(
								target.getId(),
								_domeo.getPersistenceManager().getCurrentResource()
							);
							selectors.add(selector);
							isGeneral = true;
						}
					}
				}

				// Detect annotation type
				HashSet<String> typesSet = new HashSet<String>();
				if(hasMultipleTypes(jsonAnnotations.get(j))) {
					JsArrayString types = getObjectTypes(jsonAnnotations.get(j));
					for(int k=0; k<types.length(); k++) {
						typesSet.add(types.get(k));
					}
				} else {
					typesSet.add(getObjectType(jsonAnnotations.get(j)));
				}
				
				// Post it detected
				MAnnotation ann = null;
				if(typesSet.contains(MQualifierAnnotation.TYPE)) {
					JsAnnotationPostIt postIt = (JsAnnotationPostIt) jsonAnnotations.get(j); // TODO change
					ann = AnnotationFactory.cloneQualifier(
						postIt.getId(), 
						postIt.getLineageUri(), 
						postIt.getFormattedCreatedOn(),
						postIt.getFormattedLastSaved(),
						set,
						_domeo.getAgentManager().getAgentByUri(postIt.getCreatedBy()),
						postIt.getVersionNumber(),
						postIt.getPreviousVersion(),
						_domeo.getPersistenceManager().getCurrentResource(),
						selectors,
						postIt.getLabel()
						);
					
					if(postIt.getCreatedWith()!=null && postIt.getCreatedWith().trim().length()>0) {
						ann.setTool((ISoftware) _domeo.getAgentManager().getAgentByUri(postIt.getCreatedWith()));
					}
					
					JsArray<JsoLinkedDataResource> tags = getSemanticTags(jsonAnnotations.get(j));
					for(int k=0; k<tags.length(); k++) {
						JsonGenericResource gr = tags.get(k).getSource();
						MGenericResource r = ResourcesFactory.createGenericResource(gr.getUrl(), gr.getLabel());
						
						MLinkedResource ldr = ResourcesFactory.createTrustedResource(tags.get(k).getUrl(), 
								tags.get(k).getLabel(), r);
						ldr.setUrl(tags.get(k).getUrl());
						ldr.setLabel(tags.get(k).getLabel());
						ldr.setDescription(tags.get(k).getDescription());
						//ldr.setSynonyms(tags.get(k).getSynonyms()!=null? tags.get(k).getSynonyms():"");

						((MQualifierAnnotation)ann).addTerm(ldr);
					}
				} 
				if(ann!=null) {
					for(int z=0; z<ann.getSelectors().size(); z++) {
						try {
							if(ann.getSelectors().get(z) instanceof MTextQuoteSelector) {
//								HtmlUtils.performAnnotation(Long.toString(ann.getLocalId())+((ann.getSelectors().size()>1)?(":"+ann.getSelectors().get(z).getLocalId()):""), 
//										((MTextQuoteSelector)ann.getSelectors().get(z)).getExact(), 
//										((MTextQuoteSelector)ann.getSelectors().get(z)).getPrefix(), 
//										((MTextQuoteSelector)ann.getSelectors().get(z)).getSuffix(), 
//										HtmlUtils.createSpan(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement(), 0L), "domeo-annotation");
								
								HtmlUtils.performAnnotation(Long.toString(ann.getLocalId())+((ann.getSelectors().size()>1)?(":"+ann.getSelectors().get(z).getLocalId()):""), 
										((MTextQuoteSelector)ann.getSelectors().get(z)).getExact(), 
										((MTextQuoteSelector)ann.getSelectors().get(z)).getPrefix(), 
										((MTextQuoteSelector)ann.getSelectors().get(z)).getSuffix(), 
										HtmlUtils.createSpan(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement(), 0L),
										_domeo.getCssManager().getStrategy().getObjectStyleClass(ann));
							} else if(ann.getSelectors().get(z) instanceof MImageInDocumentSelector) {
								// Place the annotation???
								Element image = HtmlUtils.getImage(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement(), 
										((MOnlineImage)((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget()).getUrl(), 
										((MOnlineImage)((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget()).getXPath());
								if(image!=null) {
									ann.setY(((com.google.gwt.user.client.Element) image).getAbsoluteTop());
									((MOnlineImage)((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget()).setImage((com.google.gwt.user.client.Element) image);
									_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(ann, 
											((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget(), (com.google.gwt.user.client.Element) image);
								}
							} else if(ann.getSelectors().get(z) instanceof MAnnotationSelector) {
								//Window.alert("Individual url: " + ((MAnnotationSelector) ann.getSelectors().get(z)).getAnnotation().getIndividualUri() + " - " + ann.getIndividualUri());
								this.cacheForLazyBinding(((MAnnotationSelector) ann.getSelectors().get(z)).getAnnotation().getIndividualUri(), 
										ann, (MAnnotationSelector) ann.getSelectors().get(z));
							}
						} catch(Exception e) {
							_domeo.getLogger().exception(this, LOGGING_PREFIX, e.getMessage());
							// TODO better tracking of terms that don't fall in text.
						}
					}

					if(isGeneral) 
						_domeo.getAnnotationPersistenceManager().addAnnotationOfTargetResource(ann, _domeo.getPersistenceManager().getCurrentResource(), set); 
					else  ((AnnotationPersistenceManager)_domeo.getPersistenceManager()).addAnnotation(ann, set);
					set.setHasChanged(true);
				}
			}
			this.completeDeserialization();
		} catch (Exception e) {
			_domeo.getLogger().exception(this, LOGGING_PREFIX, e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void unmarshall(JsArray<JavaScriptObject> responseOnSets, int expectedSize) {
		annotationSelectorsLazyBinding.clear();
		creatorAgentsLazyBinding.clear();
		
		if(Domeo.verbose) _domeo.getLogger().debug(this, "Before performing unmarshalling");
		try {
			for(int i=0; i<responseOnSets.length(); i++) {
				if(Domeo.verbose) _domeo.getLogger().debug(this, "Annotation set get");
				JsAnnotationSet jsonSet = (JsAnnotationSet) responseOnSets.get(i);
				if(Domeo.verbose) _domeo.getLogger().debug(this, "Annotation set unmarshall");
				MAnnotationSet set = unmarshallAnnotationSet(jsonSet, IUnmarshaller.LOAD_VALIDATION);
				if(Domeo.verbose) _domeo.getLogger().debug(this, "Annotation set loading");
				((AnnotationPersistenceManager) _domeo.getPersistenceManager()).loadAnnotationSet(set);
				
				if(Domeo.verbose) _domeo.getLogger().debug(this, "Unmarshalling agents");
				// Unmarshalling agents
				JsArray<JsoAgent> jsonAgents = jsonSet.getAgents();
				for(int j=0; j<jsonAgents.length(); j++) {
					if(getObjectType(jsonAgents.get(j)).equals(IPerson.TYPE) || 
							getObjectType(jsonAgents.get(j)).equals(ISoftware.TYPE) || 
							getObjectType(jsonAgents.get(j)).equals(IDatabase.TYPE)) {
						_domeo.getAgentManager().addAgent(jsonAgents.get(j));
					} else {
						Window.alert("To request: " + getObjectType(jsonAgents.get(j)));
					}
				}
				
				if(Domeo.verbose) _domeo.getLogger().debug(this, "Unmarshalling permissions");
				// Unmarshalling permissions
				if(jsonSet.getPermissions().isLocked().equals("true")) set.setIsLocked(true);
				else set.setIsLocked(false);
				_domeo.getAnnotationAccessManager().setAnnotationSetAccess(set, jsonSet.getPermissions().getPermissionType());
				if(jsonSet.getPermissions().getPermissionType().equals(IPermissionsOntology.accessGroups)) {
					Set<IUserGroup> groups = new HashSet<IUserGroup>();
					for(int ii=0; ii<jsonSet.getPermissions().getPermissionDetails().getAllowedGroups().length(); ii++) {
						String uuid = jsonSet.getPermissions().getPermissionDetails().getAllowedGroups().get(ii).getUuid();
						IUserGroup group = _domeo.getUserManager().getUserGroup(uuid); 
						if(group!=null) {
							groups.add(group);
						}
					}
					if(groups.size()>0) _domeo.getAnnotationAccessManager().setAnnotationSetGroups(set, groups);
				}
				
				if(Domeo.verbose) _domeo.getLogger().debug(this, "Unmarshalling annotations");
				// Unmarshalling annotations
				JsArray<JavaScriptObject> jsonAnnotations = jsonSet.getAnnotation();
				for(int j=0; j<jsonAnnotations.length(); j++) {
					boolean isGeneral = false;
					
					if(Domeo.verbose) _domeo.getLogger().debug(this, "Unmarshalling annotation selector (" + j + ")");
					// Selectors
					ArrayList<MSelector> selectors = new ArrayList<MSelector>();
					JsArray<JsAnnotationTarget> targets = getTargets(jsonAnnotations.get(j));
					for(int z=0; z<targets.length(); z++) {
						JsAnnotationTarget target = targets.get(z);
						if(target.getSelector()!=null) {
							String selectorType = getObjectType(target.getSelector());
							_domeo.getLogger().debug(this, selectorType);
							if(selectorType.equals(MTextQuoteSelector.TYPE)) {
								MTextQuoteSelector selector = unmarshallPrefixSuffixTextSelector((JsTextQuoteSelector) target.getSelector(), IUnmarshaller.OFF_VALIDATION);
								selectors.add(selector);
							} else if(selectorType.equals(MImageInDocumentSelector.TYPE)) {
								MImageInDocumentSelector selector = unmarshallImageInDocumentSelector((JsImageInDocumentSelector) target.getSelector(), IUnmarshaller.EASY_VALIDATION);
								selector.getTarget().setUrl(getSelectorTargetUrl(target));
								selectors.add(selector);
							} else if(selectorType.equals(MAnnotationSelector.TYPE)) {
								MAnnotationSelector selector = unmarshallAnnotationSelector((JsAnnotationSelector)target.getSelector(), IUnmarshaller.EASY_VALIDATION);
								
								// TODO to improve with a Virtual Annotation
								// MAnnotation annotation = new MPostItAnnotation();
								// annotation.setIndividualUri(sel.getAnnotation());
								// selector.setAnnotation(annotation);
								selectors.add(selector);
							}
						} else {
							String targetType = getObjectType(target);
							_domeo.getLogger().debug(this, targetType);
							if(targetType.equals(MTargetSelector.TYPE)) {
								
								MTargetSelector selector = AnnotationFactory.cloneTargetSelector(
									target.getId(),
									_domeo.getPersistenceManager().getCurrentResource()
								);
								selectors.add(selector);
								isGeneral = true;
							}
						}
					}
					
					if(Domeo.verbose) _domeo.getLogger().debug(this, "Detect annotation type (" + j + ")");
					// Detect annotation type
					HashSet<String> typesSet = new HashSet<String>();
					if(hasMultipleTypes(jsonAnnotations.get(j))) {
						JsArrayString types = getObjectTypes(jsonAnnotations.get(j));
						for(int k=0; k<types.length(); k++) {
							typesSet.add(types.get(k));
						}
					} else {
						typesSet.add(getObjectType(jsonAnnotations.get(j)));
					}
					
					if(Domeo.verbose) _domeo.getLogger().debug(this, "Detected annotation of type (" + typesSet.size() + ")");
					// Post it detected
					MAnnotation ann = null;
					if(typesSet.contains(MHighlightAnnotation.TYPE)) {
						JsAnnotationHighlight highlight = (JsAnnotationHighlight) jsonAnnotations.get(j);
						ann = AnnotationFactory.cloneHighlight(
								highlight.getId(), 
								highlight.getLineageUri(), 
								highlight.getFormattedCreatedOn(),
								highlight.getFormattedLastSaved(),
								set,
								_domeo.getAgentManager().getAgentByUri(highlight.getCreatedBy()),
								(ISoftware)_domeo.getAgentManager().getAgentByUri(highlight.getCreatedWith()),
								// _domeo.getAgentManager().getUserPerson(), // TODO fix with the right user 
								highlight.getVersionNumber(),
								highlight.getPreviousVersion(),
								_domeo.getPersistenceManager().getCurrentResource(),
								selectors,
								highlight.getLabel());
						
						// Highlight allows only text selectors at the moment
						for(int z=0; z<ann.getSelectors().size(); z++) {
							HtmlUtils.performHighlight(Long.toString(ann.getLocalId()), ((MTextQuoteSelector)ann.getSelectors().get(z)).getExact(), 
									((MTextQuoteSelector)ann.getSelectors().get(z)).getPrefix(), ((MTextQuoteSelector)ann.getSelectors().get(z)).getSuffix(), 
									HtmlUtils.createSpan(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement(), 0L), 
									_domeo.getCssManager().getStrategy().getObjectStyleClass(ann));
						}
						
						((AnnotationPersistenceManager)_domeo.getPersistenceManager()).addAnnotation(ann, set);
						set.setHasChanged(false);
					} else {
						if(typesSet.contains(MPostItAnnotation.TYPE)) {
							PostitType postItType = null;
							if(typesSet.contains(PostitType.NOTE)) {
								postItType = PostitType.NOTE_TYPE;
							} else if(typesSet.contains(PostitType.TAG)) {
								postItType = PostitType.TAG_TYPE;
							} else if(typesSet.contains(PostitType.ERRATUM)) {
								postItType = PostitType.ERRATUM_TYPE;
							} else if(typesSet.contains(PostitType.EXAMPLE)) {
								postItType = PostitType.ERRATUM_TYPE;
							} else if(typesSet.contains(PostitType.COMMENT)) {
								postItType = PostitType.COMMENT_TYPE;
							}
							if(postItType!=null) {
								JsAnnotationPostIt postIt = (JsAnnotationPostIt) jsonAnnotations.get(j);
								
								MContentAsRdf body = new MContentAsRdf();
								body.setIndividualUri(postIt.getBody().get(0).getId());
								body.setFormat(postIt.getBody().get(0).getFormat());
								body.setChars(postIt.getBody().get(0).getChars());
								
								ann = AnnotationFactory.clonePostIt(
									postIt.getId(), 
									postIt.getLineageUri(), 
									postIt.getFormattedCreatedOn(),
									postIt.getFormattedLastSaved(),
									set,
									_domeo.getAgentManager().getAgentByUri(postIt.getCreatedBy()),
									(ISoftware)_domeo.getAgentManager().getAgentByUri(postIt.getCreatedWith()),
									postIt.getVersionNumber(),
									postIt.getPreviousVersion(),
									_domeo.getPersistenceManager().getCurrentResource(),
									selectors,
									postIt.getLabel(),
									body,
									postItType
									);
							}
						} else  if(typesSet.contains(MQualifierAnnotation.TYPE)) {
							JsAnnotationPostIt postIt = (JsAnnotationPostIt) jsonAnnotations.get(j); // TODO change
							ann = AnnotationFactory.cloneQualifier(
								postIt.getId(), 
								postIt.getLineageUri(), 
								postIt.getFormattedCreatedOn(),
								postIt.getFormattedLastSaved(),
								set,
								_domeo.getAgentManager().getAgentByUri(postIt.getCreatedBy()),
								postIt.getVersionNumber(),
								postIt.getPreviousVersion(),
								_domeo.getPersistenceManager().getCurrentResource(),
								selectors,
								postIt.getLabel()
								);
							//Window.alert("????? " +postIt.getCreatedWith());
							if(postIt.getCreatedWith()!=null && postIt.getCreatedWith().trim().length()>0) {
								//Window.alert("????? " + postIt.getCreatedWith());
								ann.setTool((ISoftware) _domeo.getAgentManager().getAgentByUri(postIt.getCreatedWith()));
							}
							
							JsArray<JsoLinkedDataResource> tags = getSemanticTags(jsonAnnotations.get(j));
							for(int k=0; k<tags.length(); k++) {
								JsonGenericResource gr = tags.get(k).getSource();
								MGenericResource r = ResourcesFactory.createGenericResource(gr.getUrl(), gr.getLabel());
								
								MLinkedResource ldr = ResourcesFactory.createTrustedResource(tags.get(k).getUrl(), 
										tags.get(k).getLabel(), r);
								ldr.setDescription(tags.get(k).getDescription());
								//ldr.setSynonyms(tags.get(k).getSynonyms()!=null? tags.get(k).getSynonyms():"");

								((MQualifierAnnotation)ann).addTerm(ldr);
							}
						} else if(typesSet.contains(MCommentAnnotation.TYPE)) {
							JsAnnotationPostIt postIt = (JsAnnotationPostIt) jsonAnnotations.get(j); // TODO change
							ann = AnnotationFactory.cloneComment(
									postIt.getId(), 
									postIt.getLineageUri(), 
									postIt.getFormattedCreatedOn(),
									postIt.getFormattedLastSaved(),
									postIt.getVersionNumber(),
									postIt.getPreviousVersion(),
									set,
									_domeo.getAgentManager().getAgentByUri(postIt.getCreatedBy()),
									(ISoftware)_domeo.getAgentManager().getAgentByUri(postIt.getCreatedWith()),
									_domeo.getPersistenceManager().getCurrentResource(),
									selectors,
									postIt.getBody().get(0).getChars()
									);
							if(postIt.getTitle()!=null && postIt.getTitle().trim().length()>0)
								((MCommentAnnotation)ann).setTitle(postIt.getTitle());
						} else if(typesSet.contains(MLinearCommentAnnotation.TYPE)) {
							JsAnnotationPostIt postIt = (JsAnnotationPostIt) jsonAnnotations.get(j); // TODO change
							ann = LinearCommentsFactory.cloneLinearComment(
									postIt.getId(), 
									postIt.getLineageUri(), 
									postIt.getFormattedCreatedOn(),
									postIt.getFormattedLastSaved(),
									postIt.getVersionNumber(),
									postIt.getPreviousVersion(),
									_domeo.getAgentManager().getAgentByUri(postIt.getCreatedBy()),
									(ISoftware)_domeo.getAgentManager().getAgentByUri(postIt.getCreatedWith()),
									selectors,
									postIt.getBody().get(0).getChars()
									);
							if(postIt.getTitle()!=null && postIt.getTitle().trim().length()>0)
								((MLinearCommentAnnotation)ann).setTitle(postIt.getTitle());
						} else if(typesSet.contains(MCurationToken.TYPE)) {
							JsAnnotationCuration annotationCuration = (JsAnnotationCuration) jsonAnnotations.get(j); // TODO change
							ann = CurationFactory.cloneCurationToken(
									annotationCuration.getId(), 
									annotationCuration.getLineageUri(), 
									annotationCuration.getFormattedCreatedOn(),
									annotationCuration.getFormattedLastSaved(),
									annotationCuration.getVersionNumber(),
									annotationCuration.getPreviousVersion(),
									set,
									_domeo.getAgentManager().getAgentByUri(annotationCuration.getCreatedBy()),
									(ISoftware)_domeo.getAgentManager().getAgentByUri(annotationCuration.getCreatedWith()),
									_domeo.getPersistenceManager().getCurrentResource(),
									selectors,
									annotationCuration.getBody().get(0).getValue(),
									annotationCuration.getBody().get(0).getDescription()
									);
						} else if(typesSet.contains(MAntibodyAnnotation.TYPE)) {
							JsoAntibodyAnnotation antibody = (JsoAntibodyAnnotation) jsonAnnotations.get(j); // TODO change
							ann = NifAntibodyFactory.createAntibody(
								antibody.getId(), 
								antibody.getLineageUri(), 
								antibody.getFormattedCreatedOn(),
								antibody.getFormattedLastSaved(),
								set,
								_domeo.getAgentManager().getAgentByUri(antibody.getCreatedBy()),
								(ISoftware)_domeo.getAgentManager().getAgentByUri(antibody.getCreatedWith()),
								antibody.getVersionNumber(),
								antibody.getPreviousVersion(),
								_domeo.getPersistenceManager().getCurrentResource(),
								selectors,
								antibody.getLabel()
								);
							if(antibody.getCreatedWith()!=null && antibody.getCreatedWith().trim().length()>0) {
								ann.setTool((ISoftware) _domeo.getAgentManager().getAgentByUri(antibody.getCreatedWith()));
							}

							JsArray<JsoAntibodyUsage> aus = antibody.getBody();
							JsoAntibodyUsage au = aus.get(0);
							MAntibodyUsage usage = new MAntibodyUsage();
							usage.setIndividualUri(au.getId());
							usage.setComment(au.getComment());
							
							JsonGenericResource sub = au.getSubject();
							if(sub!=null) {
								MLinkedResource gr = ResourcesFactory.createLinkedResource(sub.getUrl(), sub.getLabel());
								usage.setSubject(gr);
							}
							
							JsArray<JsonGenericResource>  protocols = au.getProtocols();
							for(int x=0; x<protocols.length(); x++) {
								MLinkedResource protocol = ResourcesFactory.createLinkedResource(protocols.get(x).getUrl(), protocols.get(x).getLabel());
								usage.addProtocol(protocol);
							}
						
							JsoAntibody a =  au.getAntibody().get(0);
							
							JsonGenericResource source = a.getSource();
							MGenericResource gSource = ResourcesFactory.createGenericResource(source.getUrl(), source.getLabel());
							MAntibody abody = new MAntibody(a.getId(), a.getLabel(), gSource);
							abody.setType(a.getType());
							abody.setOrganism(a.getOrganism());
							abody.setCloneId(a.getCloneId());
							abody.setVendor(a.getVendor());
							abody.setCatalog(a.getCatalog());
							usage.setAntibody(abody);
							
							((MAntibodyAnnotation)ann).setAntibodyUsage(usage);
							
							//fff
							//JsArray<JsAntibodyUsage> tags = getSemanticTags(jsonAnnotations.get(j));
//							for(int k=0; k<tags.length(); k++) {
//								MLinkedDataResource ldr = new MLinkedDataResource();
//								ldr.setUrl(tags.get(k).getUrl());
//								ldr.setLabel(tags.get(k).getLabel());
//								ldr.setDescription(tags.get(k).getDescription());
//								ldr.setSynonyms(tags.get(k).getSynonyms()!=null? tags.get(k).getSynonyms():"");
//								
//								GenericResource r = new GenericResource();
//								JsoGenericResource gr = tags.get(k).getSource();
//								r.setUrl(gr.getUrl());
//								r.setLabel(gr.getLabel());
//								ldr.setSource(r);
//								((MQualifierAnnotation)ann).addTerm(ldr);
//							}
						} else if(typesSet.contains(MMicroPublicationAnnotation.TYPE)) {
							JsoMicroPublicationAnnotation mp = (JsoMicroPublicationAnnotation) jsonAnnotations.get(j);
							_domeo.getLogger().debug(this, "0-1 "+ mp);
							ann = MicroPublicationFactory.createMicroPublicationAnnotation(
								mp.getId(), 
								mp.getLineageUri(), 
								mp.getFormattedCreatedOn(),
								mp.getFormattedLastSaved(),
								set,
								_domeo.getAgentManager().getAgentByUri(mp.getCreatedBy()),
								(ISoftware)_domeo.getAgentManager().getAgentByUri(mp.getCreatedWith()),
								mp.getVersionNumber(),
								mp.getPreviousVersion(),
								_domeo.getPersistenceManager().getCurrentResource(),
								selectors,
								mp.getLabel()
								);
							if(mp.getCreatedWith()!=null && mp.getCreatedWith().trim().length()>0) {
								ann.setTool((ISoftware) _domeo.getAgentManager().getAgentByUri(mp.getCreatedWith()));
							}
							_domeo.getLogger().debug(this, "0-1a");
							JsArray<JsoMicroPublication> aus = mp.getBody();
							JsoMicroPublication au = aus.get(0);
							//JsArray<JsoMpStatement> statements = au.getArgues();
							JsoMpStatement argues = au.getArgues();			
							_domeo.getLogger().debug(this, "0-1b");
							HashSet<String> arguesTypesSet = new HashSet<String>();
							if(hasMultipleTypes(argues)) {
								JsArrayString types = getObjectTypes(argues);
								for(int k=0; k<types.length(); k++) {
									arguesTypesSet.add(types.get(k));
								}
							} else {
								arguesTypesSet.add(getObjectType(argues));
							}
							_domeo.getLogger().debug(this, "0-1c");
							JsAnnotationTarget tar = argues.getContext();	
							MTextQuoteSelector selector = unmarshallPrefixSuffixTextSelector((JsTextQuoteSelector) tar.getSelector(), IUnmarshaller.OFF_VALIDATION);
							
							_domeo.getLogger().debug(this, mp.getId() + " --- " + argues.getId());
							MMicroPublication microPublication = MicroPublicationFactory.cloneMicroPublication(mp.getId(), argues.getId(), selector);
							
							
							_domeo.getLogger().debug(this, "0-1d");
							if(arguesTypesSet.contains("swande:Claim")) {
								microPublication.setType(MMicroPublication.CLAIM);
							} else if(arguesTypesSet.contains("swande:Hypothesis")) {
								microPublication.setType(MMicroPublication.HYPOTHESIS);
							} else {
								microPublication.setType(MMicroPublication.CLAIM);
							}
							_domeo.getLogger().debug(this, "0-1e");
							((MMicroPublicationAnnotation)ann).setMicroPublication(microPublication);
							if(argues.isSupportingEvidence()) {
								JsArray<JsoMpRelationship> relsEvidence = argues.getSupportingEvidence();
								_domeo.getLogger().debug(this, "0-1-1 " + relsEvidence.length());
								for(int x=0; x<relsEvidence.length(); x++) {
									JsoMpRelationship rel = relsEvidence.get(x);
									JavaScriptObject res = rel.getResource();
									_domeo.getLogger().debug(this, "0-1-2 " + getObjectType(res));
									if(getObjectType(res).equals("mp:DataImage")) {
										_domeo.getLogger().debug(this, "0-1-3 mp:DataImage");
										JsoMpDataImage dataImage = (JsoMpDataImage) res;
										JsAnnotationTarget target = dataImage.getContext();
										_domeo.getLogger().debug(this, "0-1-3 " + target.getSelector()); 
										_domeo.getLogger().debug(this, "0-1-3 " + getObjectType(target.getSelector())); 
										if(getObjectType(target.getSelector()).equals("domeo:ImageInDocumentSelector")) {
											MImageInDocumentSelector s = this.unmarshallImageInDocumentSelector((JsImageInDocumentSelector) target.getSelector(), IUnmarshaller.OFF_VALIDATION);
											s.getTarget().setUrl(getSelectorTargetUrl(target));
											((MOnlineImage)s.getTarget()).setDisplayUrl(getImageDisplayUrl(target));
											
											MMpDataImage di = new MMpDataImage(s);
											di.setId(dataImage.getId());
											MMpRelationship rr = new MMpRelationship(di, IMicroPublicationsOntology.supportedBy);
											rr.setCreator(_domeo.getAgentManager().getAgentByUri(rel.getCreatedBy()));
											rr.setId(rel.getId());
											rr.setCreationDate(rel.getFormattedCreatedOn());
											microPublication.getEvidence().add(rr);
										}
									} else if(getObjectType(res).equals("mp:Statement")) {
										_domeo.getLogger().debug(this, "0-1-4 mp:Statement");
										JsoMpStatement statement = (JsoMpStatement) res;
										JsAnnotationTarget target = statement.getContext();
										_domeo.getLogger().debug(this, "0-1-4 " + target.getSelector()); 
										_domeo.getLogger().debug(this, "0-1-4 " + getObjectType(target.getSelector())); 
										if(getObjectType(target.getSelector()).equals("ao:PrefixSuffixTextSelector")) {
											MTextQuoteSelector sss = unmarshallPrefixSuffixTextSelector((JsTextQuoteSelector) tar.getSelector(), IUnmarshaller.OFF_VALIDATION);
											MMpStatement di = MicroPublicationFactory.createMicroPublicationStatement();
											di.setId(statement.getId());
											di.setSelector(sss);
											MMpRelationship rr = new MMpRelationship(di, IMicroPublicationsOntology.supportedBy);
											rr.setCreator(_domeo.getAgentManager().getAgentByUri(rel.getCreatedBy()));
											rr.setId(rel.getId());
											rr.setCreationDate(rel.getFormattedCreatedOn());
											microPublication.getEvidence().add(rr);
										}
									} else if(getObjectType(res).equals("org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference")) {
										_domeo.getLogger().debug(this, "0-1-5 mp:Reference");

										JsPublicationArticleReference reference = (JsPublicationArticleReference) res;
										
										MPublicationArticleReference publicationReference = new MPublicationArticleReference();
										publicationReference.setDoi(reference.getDoi());
										publicationReference.setPubMedId(reference.getPubMedId());
										publicationReference.setPubMedCentralId(reference.getPubMedCentralId());
										publicationReference.setAuthorNames(reference.getAuthorNames());
										publicationReference.setTitle(reference.getTitle());
										publicationReference.setJournalPublicationInfo(reference.getPublicationInfo());
										publicationReference.setJournalName(reference.getJournalName());
										publicationReference.setJournalIssn(reference.getJournalIssn());
										publicationReference.setUnrecognized(reference.getUnrecognized());
										
										MMpReference rrrr = new MMpReference();
										rrrr.setReference(publicationReference);
										
										MMpRelationship rr = new MMpRelationship(rrrr, IMicroPublicationsOntology.supportedBy);
										rr.setCreator(_domeo.getAgentManager().getAgentByUri(rel.getCreatedBy()));
										rr.setId(rel.getId());
										rr.setCreationDate(rel.getFormattedCreatedOn());
										microPublication.getEvidence().add(rr);
									}
								}
							}
							
							if(argues.isChallengingEvidence()) {
								JsArray<JsoMpRelationship> relsEvidence = argues.getChallengingEvidence();
								_domeo.getLogger().debug(this, "0-1-1 " + relsEvidence.length());
								for(int x=0; x<relsEvidence.length(); x++) {
									JsoMpRelationship rel = relsEvidence.get(x);
									JavaScriptObject res = rel.getResource();
									_domeo.getLogger().debug(this, "0-1-2 " + getObjectType(res));
									if(getObjectType(res).equals("mp:DataImage")) {
										_domeo.getLogger().debug(this, "0-1-3 mp:DataImage");
										JsoMpDataImage dataImage = (JsoMpDataImage) res;
										JsAnnotationTarget target = dataImage.getContext();
										_domeo.getLogger().debug(this, "0-1-3 " + target.getSelector()); 
										_domeo.getLogger().debug(this, "0-1-3 " + getObjectType(target.getSelector())); 
										if(getObjectType(target.getSelector()).equals("domeo:ImageInDocumentSelector")) {
											MImageInDocumentSelector s = this.unmarshallImageInDocumentSelector((JsImageInDocumentSelector) target.getSelector(), IUnmarshaller.OFF_VALIDATION);
											s.getTarget().setUrl(getSelectorTargetUrl(target));
											((MOnlineImage)s.getTarget()).setDisplayUrl(getImageDisplayUrl(target));
											
											MMpDataImage di = new MMpDataImage(s);
											di.setId(dataImage.getId());
											MMpRelationship rr = new MMpRelationship(di, IMicroPublicationsOntology.challengedBy);
											rr.setCreator(_domeo.getAgentManager().getAgentByUri(rel.getCreatedBy()));
											rr.setId(rel.getId());
											rr.setCreationDate(rel.getFormattedCreatedOn());
											microPublication.getEvidence().add(rr);
										}
									} else if(getObjectType(res).equals("mp:Statement")) {
										_domeo.getLogger().debug(this, "0-1-4 mp:Statement");
										JsoMpStatement statement = (JsoMpStatement) res;
										JsAnnotationTarget target = statement.getContext();
										_domeo.getLogger().debug(this, "0-1-4 " + target.getSelector()); 
										_domeo.getLogger().debug(this, "0-1-4 " + getObjectType(target.getSelector())); 
										if(getObjectType(target.getSelector()).equals("ao:PrefixSuffixTextSelector")) {
											MTextQuoteSelector sss = unmarshallPrefixSuffixTextSelector((JsTextQuoteSelector) tar.getSelector(), IUnmarshaller.OFF_VALIDATION);
											MMpStatement di = MicroPublicationFactory.createMicroPublicationStatement();
											di.setId(statement.getId());
											di.setSelector(sss);
											MMpRelationship rr = new MMpRelationship(di, IMicroPublicationsOntology.challengedBy);
											rr.setCreator(_domeo.getAgentManager().getAgentByUri(rel.getCreatedBy()));
											rr.setId(rel.getId());
											rr.setCreationDate(rel.getFormattedCreatedOn());
											microPublication.getEvidence().add(rr);
										}
									} else if(getObjectType(res).equals("org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference")) {
										_domeo.getLogger().debug(this, "0-1-5 mp:Reference");

										JsPublicationArticleReference reference = (JsPublicationArticleReference) res;
										
										MPublicationArticleReference publicationReference = new MPublicationArticleReference();
										publicationReference.setDoi(reference.getDoi());
										publicationReference.setPubMedId(reference.getPubMedId());
										publicationReference.setPubMedCentralId(reference.getPubMedCentralId());
										publicationReference.setAuthorNames(reference.getAuthorNames());
										publicationReference.setTitle(reference.getTitle());
										publicationReference.setJournalPublicationInfo(reference.getPublicationInfo());
										publicationReference.setJournalName(reference.getJournalName());
										publicationReference.setJournalIssn(reference.getJournalIssn());
										publicationReference.setUnrecognized(reference.getUnrecognized());
										
										MMpReference rrrr = new MMpReference();
										rrrr.setReference(publicationReference);
										
										MMpRelationship rr = new MMpRelationship(rrrr, IMicroPublicationsOntology.supportedBy);
										rr.setCreator(_domeo.getAgentManager().getAgentByUri(rel.getCreatedBy()));
										rr.setId(rel.getId());
										rr.setCreationDate(rel.getFormattedCreatedOn());
										microPublication.getEvidence().add(rr);
									}
								}
							}
							
							if(argues.isQualifiers()) {
								JsArray<JsoMpRelationship> relsQualifiers = argues.getQualifier();
								_domeo.getLogger().debug(this, "0-2-1 " + relsQualifiers.length());
								for(int x=0; x<relsQualifiers.length(); x++) {
									JsoMpRelationship rel = relsQualifiers.get(x);
									JavaScriptObject res = rel.getResource();
	
									JsonGenericResource gr = ((JsoLinkedDataResource)res).getSource();
									MGenericResource r = ResourcesFactory.createGenericResource(gr.getUrl(), gr.getLabel());
									
									MLinkedResource ldr = ResourcesFactory.createTrustedResource(((JsoLinkedDataResource)res).getUrl(), 
											((JsoLinkedDataResource)res).getLabel(), r);
									ldr.setDescription(((JsoLinkedDataResource)res).getDescription());
									
									MMpQualifier q = new MMpQualifier();
									q.setQualifier(ldr);
	
							
									MMpRelationship rr = new MMpRelationship(q, IMicroPublicationsOntology.supportedBy);
									rr.setCreator(_domeo.getAgentManager().getAgentByUri(rel.getCreatedBy()));
									rr.setId(rel.getId());
									rr.setCreationDate(rel.getFormattedCreatedOn());
									microPublication.getQualifiers().add(rr);
								}
							}
						}
					
						if(Domeo.verbose) _domeo.getLogger().debug(this, "Lazy binding (" + j + ")");
						if(ann!=null) {
							for(int z=0; z<ann.getSelectors().size(); z++) {
								try {
									if(ann.getSelectors().get(z) instanceof MTextQuoteSelector) {
//										HtmlUtils.performAnnotation(Long.toString(ann.getLocalId())+((ann.getSelectors().size()>1)?(":"+ann.getSelectors().get(z).getLocalId()):""), 
//												((MTextQuoteSelector)ann.getSelectors().get(z)).getExact(), 
//												((MTextQuoteSelector)ann.getSelectors().get(z)).getPrefix(), 
//												((MTextQuoteSelector)ann.getSelectors().get(z)).getSuffix(), 
//												HtmlUtils.createSpan(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement(), 0L), "domeo-annotation");
										
										HtmlUtils.performAnnotation(Long.toString(ann.getLocalId())+((ann.getSelectors().size()>1)?(":"+ann.getSelectors().get(z).getLocalId()):""), 
												((MTextQuoteSelector)ann.getSelectors().get(z)).getExact(), 
												((MTextQuoteSelector)ann.getSelectors().get(z)).getPrefix(), 
												((MTextQuoteSelector)ann.getSelectors().get(z)).getSuffix(), 
												HtmlUtils.createSpan(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement(), 0L),
												_domeo.getCssManager().getStrategy().getObjectStyleClass(ann));
									} else if(ann.getSelectors().get(z) instanceof MImageInDocumentSelector) {
										// Place the annotation???
										_domeo.getLogger().info(this, "image;;;" + ((MOnlineImage)((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget()).getUrl());
										Element image = HtmlUtils.getImage(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement(), 
												((MOnlineImage)((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget()).getUrl(), 
												((MOnlineImage)((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget()).getXPath());
										if(image!=null) {
											ann.setY(((com.google.gwt.user.client.Element) image).getAbsoluteTop());
											((MOnlineImage)((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget()).setImage((com.google.gwt.user.client.Element) image);
											_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(ann, 
													((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget(), (com.google.gwt.user.client.Element) image);
											_domeo.getLogger().info(this, "caching annotation for image;;;" + ((MOnlineImage)((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget()).getUrl());
											_domeo.getAnnotationPersistenceManager().cacheAnnotationOfImage(((MOnlineImage)((MImageInDocumentSelector)ann.getSelectors().get(z)).getTarget()).getUrl(), ann);
										} else {
											_domeo.getLogger().exception(this, "Image element not found!");
										}
	
										// TODO add to the image cache
										
									} else if(ann.getSelectors().get(z) instanceof MAnnotationSelector) {
										//Window.alert("Individual url: " + ((MAnnotationSelector) ann.getSelectors().get(z)).getAnnotation().getIndividualUri() + " - " + ann.getIndividualUri());
										this.cacheForLazyBinding(((MAnnotationSelector) ann.getSelectors().get(z)).getAnnotation().getIndividualUri(), 
												ann, (MAnnotationSelector) ann.getSelectors().get(z));
									}
								} catch(Exception e) {
									_domeo.getLogger().exception(this, LOGGING_PREFIX, e.getMessage());
								}
							}
		
							if(isGeneral) 
								_domeo.getAnnotationPersistenceManager().addAnnotationOfTargetResource(ann, _domeo.getPersistenceManager().getCurrentResource(), set); 
							else  ((AnnotationPersistenceManager)_domeo.getPersistenceManager()).addAnnotation(ann, set);
							set.setHasChanged(false);
						}
					}
				}
				this.completeDeserialization();
			}
		} catch (Exception e) {
			_domeo.getLogger().exception(this, LOGGING_PREFIX, e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void unmarshallBibliography(JsArray<JavaScriptObject> responseOnSets, boolean isVirtual, int expectedSize) {
		annotationSelectorsLazyBinding.clear();
		creatorAgentsLazyBinding.clear();
		try {
			for(int i=0; i<responseOnSets.length(); i++) {
				JsBibliographicSet jsonSet = (JsBibliographicSet) responseOnSets.get(i);
				MBibliographicSet bibliographicSet = ((AnnotationPersistenceManager) _domeo.getPersistenceManager()).getBibliographicSet();
				if(jsonSet.getLevel()!=null) {
					bibliographicSet.setLevel(Integer.parseInt(jsonSet.getLevel()));
				}
				if(jsonSet.getLineageUri()!=null) {
					bibliographicSet.setLineageUri(jsonSet.getLineageUri());
				}
				if(jsonSet.getId()!=null) {
					bibliographicSet.setIndividualUri(jsonSet.getId());
				}
				
				// TODO createdWith
				
				
				if(jsonSet.getVersionNumber()!=null) bibliographicSet.setVersionNumber(jsonSet.getVersionNumber());
				if(jsonSet.getPreviousVersion()!=null) bibliographicSet.setPreviousVersion(jsonSet.getPreviousVersion());
				if(jsonSet.getLastSaved()!=null) bibliographicSet.setLastSavedOn(jsonSet.getFormattedLastSaved());

				// Unmarshalling agents
				AgentsFactory factory = new AgentsFactory();
				JsArray<JsoAgent> jsonAgents = jsonSet.getAgents();
				if(jsonAgents!=null) {
					for(int j=0; j<jsonAgents.length(); j++) {
						if(getObjectType(jsonAgents.get(j)).equals(IPerson.TYPE) || 
								getObjectType(jsonAgents.get(j)).equals(ISoftware.TYPE) || 
								getObjectType(jsonAgents.get(j)).equals(IDatabase.TYPE)) {
							_domeo.getAgentManager().addAgent(jsonAgents.get(j));
						} else {
							Window.alert("To request: " + getObjectType(jsonAgents.get(j)));
						}
					}
				}

				// Unmarshalling annotations
				JsArray<JavaScriptObject> jsonAnnotations = jsonSet.getAnnotation();
				for(int j=0; j<jsonAnnotations.length(); j++) {
					boolean isGeneral = false;
					// Selectors
					ArrayList<MSelector> selectors = new ArrayList<MSelector>();
					JsArray<JsAnnotationTarget> targets = getTargets(jsonAnnotations.get(j));
					for(int z=0; z<targets.length(); z++) {
						JsAnnotationTarget target = targets.get(z);
						if(target.getSelector()!=null) {
							String selectorType = getObjectType(target.getSelector());
							_domeo.getLogger().debug(this, selectorType);
							if(selectorType.equals(MTextQuoteSelector.TYPE)) {
								JsTextQuoteSelector sel = (JsTextQuoteSelector) target.getSelector();
								MTextQuoteSelector selector = unmarshallPrefixSuffixTextSelector(sel, IUnmarshaller.OFF_VALIDATION);
								selectors.add(selector);
							} 
						} else {
							String targetType = getObjectType(target);
							_domeo.getLogger().debug(this, targetType);
							if(targetType.equals(MTargetSelector.TYPE)) {
								
								MTargetSelector selector = AnnotationFactory.cloneTargetSelector(
									target.getId(),
									_domeo.getPersistenceManager().getCurrentResource()
								);
								selectors.add(selector);
								isGeneral = true;
							}
						}
					}
					
					// Detect annotation type
					String annotationType = getObjectType(jsonAnnotations.get(j));
					_domeo.getLogger().debug(this, annotationType+ " - " + getObjectId(jsonAnnotations.get(j)));
					if(annotationType.equals(MAnnotationReference.TYPE)) {
						JsAnnotationReference reference = (JsAnnotationReference) jsonAnnotations.get(j);

						MPublicationArticleReference publicationReference = new MPublicationArticleReference();
						publicationReference.setDoi(reference.getReference().getDoi());
						publicationReference.setPubMedId(reference.getReference().getPubMedId());
						publicationReference.setPubMedCentralId(reference.getReference().getPubMedCentralId());
						publicationReference.setAuthorNames(reference.getReference().getAuthorNames());
						publicationReference.setTitle(reference.getReference().getTitle());
						publicationReference.setJournalPublicationInfo(reference.getReference().getPublicationInfo());
						publicationReference.setJournalName(reference.getReference().getJournalName());
						publicationReference.setJournalIssn(reference.getReference().getJournalIssn());
						publicationReference.setUnrecognized(reference.getReference().getUnrecognized());

						if(!isGeneral) {
							MAnnotationCitationReference annotation = AnnotationFactory.cloneReference(
									reference.getId(), 
									_domeo.getAgentManager().getAgentByUri(reference.getCreatedBy()),
									(ISoftware)_domeo.getAgentManager().getAgentByUri(reference.getCreatedWith()),
									reference.getFormattedCreatedOn(),
									new Integer(reference.getIndex()), 
									publicationReference,
									_domeo.getPersistenceManager().getCurrentResource(),
									selectors.get(0)
									);

							// 1 ??
							bibliographicSet.acknowledgeReference(annotation);
							// 2 ??
							((IReferences)_domeo.getPersistenceManager().getCurrentResource()).getReferences().add(annotation);
	
							//for(int z=0; z<annotation.getSelectors().size(); z++) {
							if(annotation.getReferenceSelector()!=null) {
								if(annotation.getReferenceSelector() instanceof MTextQuoteSelector) {
//									HtmlUtils.performAnnotation(Long.toString(annotation.getLocalId())+((annotation.getSelectors().size()>1)?(":"+annotation.getReferenceSelector().getLocalId()):""), 
//											((MTextQuoteSelector)annotation.getReferenceSelector()).getExact(), 
//											((MTextQuoteSelector)annotation.getReferenceSelector()).getPrefix(), 
//											((MTextQuoteSelector)annotation.getReferenceSelector()).getSuffix(), 
//											HtmlUtils.createSpan(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement(), 0L), "domeo-annotation");
									
									try {
										HtmlUtils.performAnnotation(Long.toString(annotation.getLocalId())+((annotation.getSelectors().size()>1)?(":"+annotation.getReferenceSelector().getLocalId()):""), 
											((MTextQuoteSelector)annotation.getReferenceSelector()).getExact(), 
											((MTextQuoteSelector)annotation.getReferenceSelector()).getPrefix(), 
											((MTextQuoteSelector)annotation.getReferenceSelector()).getSuffix(), 
											HtmlUtils.createSpan(_domeo.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement(), 0L), 
											_domeo.getCssManager().getStrategy().getObjectStyleClass(annotation));
									} catch(Exception e) {
										_domeo.getLogger().exception(this, LOGGING_PREFIX, e.getMessage());
									}
								} 
							}
							// 3 ??
							((AnnotationPersistenceManager)_domeo.getPersistenceManager()).addAnnotation(annotation, bibliographicSet);
						} else {
							MAnnotationReference selfReference = AnnotationFactory.cloneCitation(reference.getId(),
									_domeo.getAgentManager().getAgentByUri(reference.getCreatedBy()),
									(ISoftware)_domeo.getAgentManager().getAgentByUri(reference.getCreatedWith()),
									reference.getFormattedCreatedOn(), publicationReference, selectors.get(0));
							bibliographicSet.addSelfReference(selfReference);
							//_domeo.getAnnotationPersistenceManager().addAnnotationOfTargetResource(citation, _domeo.getPersistenceManager().getCurrentResource(), bibliographicSet); 
							((ISelfReference)_domeo.getPersistenceManager().getCurrentResource()).setSelfReference(selfReference);
						} 
						
						bibliographicSet.setHasChanged(false);
					}
				}
				this.completeDeserialization();
			}
		} catch (Exception e) {
			_domeo.getLogger().exception(this, LOGGING_PREFIX, e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void unmarshallVirtualBibliography(JsArray<JavaScriptObject> responseOnSets, boolean isVirtual, int expectedSize) {
		annotationSelectorsLazyBinding.clear();
		creatorAgentsLazyBinding.clear();
		try {
			for(int i=0; i<responseOnSets.length(); i++) {
				JsAnnotationSet jsonSet = (JsAnnotationSet) responseOnSets.get(i);
				//MAnnotationSet set = unmarshallAnnotationSet(jsonSet);
				MBibliographicSet bibliographicSet = ((AnnotationPersistenceManager) _domeo.getPersistenceManager()).getBibliographicSet();
				bibliographicSet.setVirtual(true);
				if(jsonSet.getVersionNumber()!=null) bibliographicSet.setVersionNumber(jsonSet.getVersionNumber());
				if(jsonSet.getLastSaved()!=null) bibliographicSet.setLastSavedOn(jsonSet.getFormattedLastSaved());
				
				JsArray<JavaScriptObject> jsonAnnotations = jsonSet.getAnnotation();
				for(int j=0; j<jsonAnnotations.length(); j++) {
					boolean isGeneral = false;
					// Selectors
					JsArray<JsAnnotationTarget> targets = getTargets(jsonAnnotations.get(j));
					for(int z=0; z<targets.length(); z++) {
						JsAnnotationTarget target = targets.get(z);
						if(target.getSelector()!=null) {
							String selectorType = getObjectType(target.getSelector());
							_domeo.getLogger().debug(this, selectorType);
							if(selectorType.equals(MTextQuoteSelector.TYPE)) {
							} else {
								Window.alert("unmarshallBibliography problem, selector not recognized");
							}
						} else {
							String targetType = getObjectType(target);
							_domeo.getLogger().debug(this, targetType);
							if(targetType.equals(MTargetSelector.TYPE)) {
								isGeneral = true;
							}
						}
					}
					
					// Detect annotation type
					String annotationType = getObjectType(jsonAnnotations.get(j));
					_domeo.getLogger().debug(this, annotationType);
					if(!isGeneral && annotationType.equals(MAnnotationReference.TYPE)) {
						JsAnnotationReference reference = (JsAnnotationReference) jsonAnnotations.get(j);

						MPublicationArticleReference publicationReference = new MPublicationArticleReference();
						publicationReference.setDoi(reference.getReference().getDoi());
						publicationReference.setPubMedId(reference.getReference().getPubMedId());
						publicationReference.setPubMedCentralId(reference.getReference().getPubMedCentralId());
						publicationReference.setAuthorNames(reference.getReference().getAuthorNames());
						publicationReference.setTitle(reference.getReference().getTitle());
						publicationReference.setJournalPublicationInfo(reference.getReference().getPublicationInfo());
						publicationReference.setUnrecognized(reference.getReference().getUnrecognized());
						publicationReference.setJournalName(reference.getReference().getJournalName());
						publicationReference.setJournalIssn(reference.getReference().getJournalIssn());

						MAnnotationCitationReference annotation = new MAnnotationCitationReference();
						annotation.setIndividualUri(reference.getId());
						annotation.setCreator(_domeo.getAgentManager().getSoftware());
						annotation.setReference(publicationReference);
						annotation.setReferenceIndex(new Integer(reference.getIndex()));
						annotation.setCreatedOn(reference.getFormattedCreatedOn());

						((IReferences)_domeo.getPersistenceManager().getCurrentResource()).getReferences().add(annotation);
						
						// The virtual references are not added to the bibliographic set
						// as they don't have to get saved again.
						//bibliographicSet.addReference(annotation);
						//bibliographicSet.setHasChanged(false);
					}
				}			
				_domeo.refreshResourceComponents();
				//this.completeDeserialization();
			}
		} catch (Exception e) {
			_domeo.getLogger().exception(this, LOGGING_PREFIX, e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Unmarshall the json serialization of a Prefix Suffix Text Selector
	 * @param prefixSuffixTextSelectorInJson Prefix Suffix Text Selector in json format
	 * @return Prefix Suffix Text Selector or null if exception raised
	 */
	private MTextQuoteSelector unmarshallPrefixSuffixTextSelector(JsTextQuoteSelector prefixSuffixTextSelectorInJson, String validation) {
		_domeo.getLogger().debug(this, "Unmarshalling " + MTextQuoteSelector.class.getName() + 
				" with id " + getObjectId(prefixSuffixTextSelectorInJson));
		try {
			IUnmarshaller unmarshaller = selectUnmarshaller(MTextQuoteSelector.class.getName());
			return (MTextQuoteSelector) unmarshaller.unmarshall(this, prefixSuffixTextSelectorInJson, validation);
		} catch(Exception e) {
			_domeo.getLogger().exception(this, LOGGING_PREFIX, "Exception while serializing the Prefix Suffix text Selector " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Unmarshall the json serialization of a Annotation Selector
	 * @param annotationSelectorInJson Annotation Selector in json format
	 * @return Annotation Selector or null if exception raised
	 */
	private MAnnotationSelector unmarshallAnnotationSelector(JsAnnotationSelector annotationSelectorInJson, String validation) {
		_domeo.getLogger().debug(this, "Unmarshalling " + MAnnotationSelector.class.getName() + 
				" with id " + getObjectId(annotationSelectorInJson));
		try {
			IUnmarshaller unmarshaller = selectUnmarshaller(MAnnotationSelector.class.getName());
			return (MAnnotationSelector) unmarshaller.unmarshall(this, annotationSelectorInJson, validation);
		} catch(Exception e) {
			_domeo.getLogger().exception(this, LOGGING_PREFIX, "Exception while serializing the Annotation Selector " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Unmarshall the json serialization of a Image in Document Selector
	 * @param imageInDocumentSelectorInJson Image in Document Selector in json format
	 * @return Image in Document Selector or null if exception raised
	 */	
	private MImageInDocumentSelector unmarshallImageInDocumentSelector(JsImageInDocumentSelector imageInDocumentSelectorInJson, String validation) {
		_domeo.getLogger().debug(this, "Unmarshalling " + MImageInDocumentSelector.class.getName() + 
				" with id " + getObjectId(imageInDocumentSelectorInJson));
		try {
			IUnmarshaller unmarshaller = selectUnmarshaller(MImageInDocumentSelector.class.getName());
			return (MImageInDocumentSelector) unmarshaller.unmarshall(this, imageInDocumentSelectorInJson, validation);
		} catch(Exception e) {
			_domeo.getLogger().exception(this, LOGGING_PREFIX, "Exception while serializing the Image in Document Selector " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Unmarshall the json serialization of an Annotation Set
	 * @param annotationSetInJson	Annotation set in json format
	 * @return Annotation Set or null if exception raised
	 */
	private MAnnotationSet unmarshallAnnotationSet(JsAnnotationSet annotationSetInJson, String validation) {
		_domeo.getLogger().debug(this, "Unmarshalling " + MAnnotationSet.class.getName() + 
			" with id " + getObjectId(annotationSetInJson));
		IUnmarshaller unmarshaller = null;
		try {
			unmarshaller = selectUnmarshaller(MAnnotationSet.class.getName());
			return (MAnnotationSet) unmarshaller.unmarshall(this, annotationSetInJson, validation);
		} catch(Exception e) {
			_domeo.getLogger().exception(this, LOGGING_PREFIX, "Exception while deserializing the Annotation Set " + e.getMessage());
			return null;
		}
	}
	
// ------------------------------------------------------------------------
//  Cache for lazy binding
// ------------------------------------------------------------------------
	private HashMap<String, IUnmarshaller> unmarshallers = new HashMap<String, IUnmarshaller>();
	private HashMap<String, Set<AnnotationLazyBinding>> annotationSelectorsLazyBinding = new HashMap<String, Set<AnnotationLazyBinding>>();
	private HashMap<String, AgentLazyBinding> creatorAgentsLazyBinding = new HashMap<String, AgentLazyBinding>();
	private HashMap<String, AgentLazyBinding> generatorAgentsLazyBinding = new HashMap<String, AgentLazyBinding>();
	private HashMap<String, AgentLazyBinding> importedFromAgentsLazyBinding = new HashMap<String, AgentLazyBinding>();
	private HashMap<String, AgentLazyBinding> importedByAgentsLazyBinding = new HashMap<String, AgentLazyBinding>();
	
	private void registerDeserializers() {
		unmarshallers.put(MAnnotationSet.class.getName(), new AnnotationSetJsonUnmarshaller(_domeo));	
		unmarshallers.put(MAnnotationSelector.class.getName(), new AnnotationSelectorJsonUnmarshaller(_domeo));
		unmarshallers.put(MTextQuoteSelector.class.getName(), new TextQuoteSelectorJsonUnmarshaller(_domeo));
		unmarshallers.put(MImageInDocumentSelector.class.getName(), new ImageInDocumentSelectorJsonUnmarshaller(_domeo));		
	}
	
	/**
	 * It caches the selectors that are pointing to other annotation items to 
	 * be bound when the whole loading process has been completed and all the
	 * annotation items will be present as objects in the client.
	 * @param targetAnnotationId	The id of the target annotation that might not have been loaded yet
	 * @param annotation			The main annotation (null when it is an annotation set)
	 * @param selector				The main annotation selector. This is necessary to identify the right selector when multiple selectors are present
	 */
	public void cacheForLazyBinding(String targetAnnotationId, MAnnotation annotation, MAnnotationSelector selector) {
		if(annotationSelectorsLazyBinding.containsKey(targetAnnotationId)) {
			annotationSelectorsLazyBinding.get(targetAnnotationId).add(new AnnotationLazyBinding(targetAnnotationId, annotation, selector));
		} else {
			Set<AnnotationLazyBinding> set = new HashSet<AnnotationLazyBinding>();
			set.add(new AnnotationLazyBinding(targetAnnotationId, annotation, selector));
			annotationSelectorsLazyBinding.put(targetAnnotationId, set);
		}
	}
	
	/**
	 * It caches the agent that have created an Annotation or an Annotation Set.
	 * @param agentId		The creator Agent id.
	 * @param annotationId	The Annotation (null when it is an Annotation Set).
	 * @param setId			The Annotation Set id.
	 */
	public void cacheForAgentsLazyBinding(String agentId, String annotationId, String setId) {
		creatorAgentsLazyBinding.put(agentId, new AgentLazyBinding(agentId, annotationId, setId));
	}
	
	/**
	 * It caches the agent that have generated an Annotation or an Annotation Set.
	 * @param agentId		The creator Agent id.
	 * @param annotationId	The Annotation (null when it is an Annotation Set).
	 * @param setId			The Annotation Set id.
	 */
	public void cacheForGeneratorAgentsLazyBinding(String agentId, String annotationId, String setId) {
		generatorAgentsLazyBinding.put(agentId, new AgentLazyBinding(agentId, annotationId, setId));
	}
	
	/**
	 * It caches the agent that have generated an Annotation or an Annotation Set.
	 * @param agentId		The creator Agent id.
	 * @param annotationId	The Annotation (null when it is an Annotation Set).
	 * @param setId			The Annotation Set id.
	 */
	public void cacheForImportedFromAgentsLazyBinding(String agentId, String annotationId, String setId) {
		importedFromAgentsLazyBinding.put(agentId, new AgentLazyBinding(agentId, annotationId, setId));
	}
	
	/**
	 * It caches the agent that have imported an Annotation or an Annotation Set.
	 * @param agentId		The creator Agent id.
	 * @param annotationId	The Annotation (null when it is an Annotation Set).
	 * @param setId			The Annotation Set id.
	 */
	public void cacheForImportedByAgentsLazyBinding(String agentId, String annotationId, String setId) {
		importedByAgentsLazyBinding.put(agentId, new AgentLazyBinding(agentId, annotationId, setId));
	}
	
	/**
	 * This method has to be called at the end of the de-serialization process and 
	 * it binds all virtual ids to the real objects that have been created during
	 * the de-serialization process.
	 */
	public void completeDeserialization() {
		Set<String> annotationIds = annotationSelectorsLazyBinding.keySet();
		for(String annotationId: annotationIds) {
			MAnnotation annotation = _domeo.getPersistenceManager().getAnnotationByUri(annotationId);
			if(annotation!=null) {
				Set<AnnotationLazyBinding> bindings = annotationSelectorsLazyBinding.get(annotationId);
				for(AnnotationLazyBinding binding: bindings) {
					binding.selector.setAnnotation(annotation);
					_domeo.getAnnotationPersistenceManager().bindAnnotationOfAnnotation(binding.annotation, annotation);
				}
			}
		}
		annotationSelectorsLazyBinding.clear();

		Set<String> agentsIds = creatorAgentsLazyBinding.keySet();
		for(String agentId: agentsIds) {
			AgentLazyBinding creatorlb = creatorAgentsLazyBinding.get(agentId);
			if(creatorlb.setId!=null) {
				_domeo.getLogger().debug(this, "Binding agent creator: " + agentId + " - " + _domeo.getAnnotationPersistenceManager().getAnnotationSetById(creatorlb.setId).getLabel());
				_domeo.getAnnotationPersistenceManager().getAnnotationSetById(creatorlb.setId).setCreatedBy(_domeo.getAgentManager().getAgentByUri(agentId));
			} else if(creatorlb.annotationId!=null) {
				Window.alert(this.getClass().getName() + creatorlb.annotationId);
				_domeo.getAnnotationPersistenceManager().getAnnotationByUri(creatorlb.annotationId).setCreator(_domeo.getAgentManager().getAgentByUri(agentId));
			}
		}
		creatorAgentsLazyBinding.clear();
		
		Set<String> generatorsIds = generatorAgentsLazyBinding.keySet();
		for(String agentId: generatorsIds) {
			AgentLazyBinding generatorlb = generatorAgentsLazyBinding.get(agentId);
			if(generatorlb.setId!=null) {
				_domeo.getLogger().debug(this, "Binding agent generator: " + agentId + " - " + _domeo.getAnnotationPersistenceManager().getAnnotationSetById(generatorlb.setId).getLabel());
				_domeo.getAnnotationPersistenceManager().getAnnotationSetById(generatorlb.setId).setCreatedWith((ISoftware) _domeo.getAgentManager().getAgentByUri(agentId));
			} else if(generatorlb.annotationId!=null) {
				Window.alert(this.getClass().getName() + generatorlb.annotationId);
				//_domeo.getAnnotationPersistenceManager().getAnnotationByUri(lb.getAnnotationId()).setTool(_domeo.getAgentManager().getAgentByUri(agentId));
			}
		}
		generatorAgentsLazyBinding.clear();
		
		Set<String> importedFromIds = importedFromAgentsLazyBinding.keySet();
		for(String agentId: importedFromIds) {
			AgentLazyBinding importedFromlb = importedFromAgentsLazyBinding.get(agentId);
			if(importedFromlb.setId!=null) {
				_domeo.getLogger().debug(this, "Binding agent importedFrom: " + agentId + " - " + _domeo.getAnnotationPersistenceManager().getAnnotationSetById(importedFromlb.setId).getLabel());
				_domeo.getAnnotationPersistenceManager().getAnnotationSetById(importedFromlb.setId).setImportedFrom((ISoftware) _domeo.getAgentManager().getAgentByUri(agentId));
			} else if(importedFromlb.annotationId!=null) {
				Window.alert(this.getClass().getName() + importedFromlb.annotationId);
				//_domeo.getAnnotationPersistenceManager().getAnnotationByUri(lb.getAnnotationId()).setTool(_domeo.getAgentManager().getAgentByUri(agentId));
			}
		}
		importedFromAgentsLazyBinding.clear();
		
		Set<String> importedByIds = importedByAgentsLazyBinding.keySet();
		for(String agentId: importedByIds) {
			AgentLazyBinding importedBylb = importedByAgentsLazyBinding.get(agentId);
			if(importedBylb.setId!=null) {
				_domeo.getLogger().debug(this, "Binding agent importedBy: " + agentId + " - " + _domeo.getAnnotationPersistenceManager().getAnnotationSetById(importedBylb.setId).getLabel());
				_domeo.getAnnotationPersistenceManager().getAnnotationSetById(importedBylb.setId).setImportedBy((ISoftware) _domeo.getAgentManager().getAgentByUri(agentId));
			} else if(importedBylb.annotationId!=null) {
				Window.alert(this.getClass().getName() + importedBylb.annotationId);
				//_domeo.getAnnotationPersistenceManager().getAnnotationByUri(lb.getAnnotationId()).setTool(_domeo.getAgentManager().getAgentByUri(agentId));
			}
		}
		importedByAgentsLazyBinding.clear();
	}
	
	class AgentLazyBinding {
		public String agentId, setId, annotationId;
		
		public AgentLazyBinding(String agentId, String annotationId, String setId) {
			this.agentId = agentId;
			this.annotationId = annotationId;
			this.setId = setId;
		}
	}
	
	class AnnotationLazyBinding {
		public String targetAnnotatioId;
		public MAnnotationSelector selector;
		public MAnnotation annotation;
		
		public AnnotationLazyBinding(String targetAnnotatioId, MAnnotation annotation, MAnnotationSelector selector) {
			this.targetAnnotatioId = targetAnnotatioId;
			this.annotation = annotation;
			this.selector = selector;
		}
	}
}
