package org.mindinformatics.gwt.domeo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.selectors.MAnnotationSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MImageInDocumentSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTargetSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.contentasrdf.model.MContentAsRdf;
import org.mindinformatics.gwt.domeo.plugins.annotation.highlight.model.MHighlightAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.PostitType;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.selection.model.MSelectionAnnotation;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.agents.model.MAgent;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.model.agents.IAgent;
import org.mindinformatics.gwt.framework.model.agents.IPerson;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;
import org.mindinformatics.gwt.framework.model.references.MReference;
import org.mindinformatics.gwt.framework.src.UUID;

/**
 * This factory is supposed to be extended by plugins that are introducing new annotation
 * artifacts. The localId counter is static so that the counter is shared across all
 * the factories.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationFactory implements IInitializableComponent {

	private static final String DOMEO_URN_PREFIX = "urn:domeoclient:uuid:";
	private static final String NEW_SET_LABEL = "New set";
	
	private static Long localIdsCounter = 0L;
	private static Long newSetsCounter = 0L;
	
	public void init() {
		localIdsCounter = 0L;
		newSetsCounter = 0L;
	}
	
	public static Long getLocalId() {
		Long buffer = localIdsCounter;
		localIdsCounter++;
		return buffer;
	}
	
	// TODO manage collisions
	protected static String getUuid() {
		return UUID.uuid();
	}
	
	protected static String getUrn(String uuid) {
		return DOMEO_URN_PREFIX + uuid;
	}
	
	protected static Long getNewSetCounter() {
		Long buffer = newSetsCounter;
		newSetsCounter++;
		return buffer;
	}
	
	// IMAGES
	// TODO move this somewhere else (Resources manager?)
	public static MOnlineImage cloneOnlineImage(String label,
			String xPath, String url) {
		MOnlineImage image = new MOnlineImage();
		image.setLabel(label);
		image.setXPath(xPath);
		image.setUrl(url);
		return image;
	}
	
	//  SETS -----------------------------------------------------------------
	// -----------------------------------------------------------------------
	public static MBibliographicSet createBibliographicSet(ISoftware createdWith, IPerson editor, 
			MGenericResource target, String label, String description) 
	{
		MBibliographicSet set = new MBibliographicSet();
		set.setLocalId(getLocalId());
		set.setUuid(getUuid());
		set.setIndividualUri(getUrn(set.getUuid()));
		set.setTargetResource(target);
		set.setLabel(label);
		set.setDescription(description);
		set.setCreatedWith(createdWith);
		set.setCreatedBy(editor);
		set.setCreatedOn(new Date());
		return set;
	}
	
	public static MAnnotationSet createAnnotationSet(ISoftware createdWith, IPerson editor, 
			MGenericResource target, String label, String description) 
	{
		MAnnotationSet set = new MAnnotationSet();
		set.setLocalId(getLocalId());
		set.setUuid(getUuid());
		set.setIndividualUri(getUrn(set.getUuid()));
		set.setTargetResource(target);
		set.setLabel(label);
		set.setDescription(description);
		set.setCreatedWith(createdWith);
		set.setCreatedBy(editor);
		set.setCreatedOn(new Date());
		return set;
	}
	
	public static MAnnotationSet createNewAnnotationSet(ISoftware createdWith, IPerson editor, 
			MGenericResource target) 
	{
		MAnnotationSet set = new MAnnotationSet();
		set.setLocalId(getLocalId());
		set.setUuid(getUuid());
		set.setIndividualUri(getUrn(set.getUuid()));
		set.setTargetResource(target);
		set.setLabel(NEW_SET_LABEL + " " + getNewSetCounter());
		set.setDescription("");
		set.setCreatedWith(createdWith);
		set.setCreatedBy(editor);
		set.setCreatedOn(new Date());
		return set;
	}
	
//	public static MAnnotationSet cloneAnnotationSet(String individualUri, String lineageUri,
//			Date createdOn, Date lastSavedOn, ISoftware createdWith, IAgent editor, String versionNumber, String previousVersion,
//			GenericResource target, String label, String description) 
//	{
//		MAnnotationSet set = new MAnnotationSet();
//		set.setLocalId(getLocalId());
//		set.setUuid("");
//		set.setLineageUri(lineageUri);
//		set.setIndividualUri(individualUri);
//		set.setTargetResource(target);
//		set.setLabel(label);
//		set.setDescription(description);
//		set.setTool(createdWith);
//		set.setEditor(editor);
//		set.setCreatedOn(createdOn);
//		set.setLastSavedOn(lastSavedOn);
//		set.setVersionNumber(versionNumber);
//		set.setPreviousVersion(previousVersion);
//		return set;
//	}
	
	/**
	 * Annotation Set cloning used by unmarshallers. Tool and creator are not initialized as they
	 * are defined through lazy binding.
	 * @param individualUri		The uri of the Annotation Set
	 * @param lineageUri		The lineage uri of the Annotation Set
	 * @param lastSavedOn		Last saved date (can be null when set is not yet saved)
	 * @param versionNumber		The version number (null if the set is not yet saved)
	 * @param previousVersion	The pointer to the previous Annotation Set version (if exists)
	 * @param target			The target of the annotation
	 * @param label				The label of the Annotation Set
	 * @param description		The description of the Annotation Set
	 * @return The unmarshalled Annotation Set.
	 */
	public static MAnnotationSet createAnnotationSet(String individualUri, String lineageUri,
			Date lastSavedOn, String versionNumber, String previousVersion,
			MGenericResource target, String label, String description) 
	{
		MAnnotationSet set = new MAnnotationSet();
		set.setLocalId(getLocalId());
		set.setUuid("");
		set.setLineageUri(lineageUri);
		set.setIndividualUri(individualUri);
		set.setTargetResource(target);
		set.setLabel(label);
		set.setDescription(description);
		set.setLastSavedOn(lastSavedOn);
		set.setVersionNumber(versionNumber);
		set.setPreviousVersion(previousVersion);
		return set;
	}
	
	//  CITATION & REFERENCES ------------------------------------------------
	// -----------------------------------------------------------------------
	private static void createAnnotationReference(
			MAnnotationReference annotationReference,
			MAgent creator, ISoftware tool, MReference reference, 
			MGenericResource target) 
	{
		annotationReference.setLocalId(getLocalId());
		annotationReference.setUuid(getUuid());
		annotationReference.setIndividualUri(getUrn(annotationReference.getUuid()));
		//annotationReference.setTarget(target);
		annotationReference.setSelector(createTargetSelector(creator, target));
		annotationReference.setCreator(creator);
		annotationReference.setReference(reference);
		annotationReference.setCreatedOn(new Date());
		annotationReference.setTool(tool);
		//set.addAnnotation(annotationReference);
	}
	
	public static MAnnotationReference createCitation(
			MAgent creator, ISoftware tool, MReference reference, 
			MGenericResource target) 
	{
		MAnnotationReference ref = new MAnnotationReference();
		AnnotationFactory.createAnnotationReference(ref, creator, tool, reference, target);
		return ref;
	}
	
	public static MAnnotationReference cloneCitation(
			String individualUri,
			IAgent creator, ISoftware tool, Date createdOn, MReference reference, 
			MSelector selector) 
	{
		MAnnotationReference annotationReference = new MAnnotationReference();
		annotationReference.setLocalId(getLocalId());
		annotationReference.setUuid(getUuid());
		annotationReference.setIndividualUri(individualUri);
		//annotationReference.setTarget(target);
		annotationReference.setSelector(selector);
		annotationReference.setCreator(creator);
		annotationReference.setReference(reference);
		annotationReference.setCreatedOn(createdOn);
		annotationReference.setTool(tool);
		return annotationReference;
	}
	
	public static MAnnotationCitationReference cloneReference(
			String individualUri,
			IAgent creator, ISoftware tool, Date createdOn, Integer referenceIndex, 
			MReference reference, MGenericResource target, MSelector selector) 
	{
		MAnnotationCitationReference ref = new MAnnotationCitationReference();
		ref.setUuid("");
		ref.setLocalId(getLocalId());
		ref.setIndividualUri(individualUri);
		ref.setCreatedOn(createdOn);
		ref.setCreator(creator);
		ref.setReference(reference);
		//AnnotationFactory.createAnnotationReference(ref, creator, reference, target);
		ref.setReferenceIndex(referenceIndex);
		ref.setReferenceSelector(selector);
		//ref.setSelector(referenceSelector);
		//ref.setReferenceSelector(selectors);
		ref.setTool(tool);
		return ref;
	}
	
	public static MAnnotationCitationReference createReference(
			MAgent creator, ISoftware tool, Integer referenceIndex, 
			MReference reference, MGenericResource target, 
			MSelector referenceSelector) 
	{
		MAnnotationCitationReference ref = new MAnnotationCitationReference();
		AnnotationFactory.createAnnotationReference(ref, creator, tool, reference, target);
		ref.setSelector(referenceSelector);
		ref.setReferenceIndex(referenceIndex);
		ref.setReferenceSelector(referenceSelector);
		return ref;
	}
	
	public static MAnnotationCitationReference createCitationReference(
			MAgent creator, ISoftware tool,Integer referenceIndex, 
			MReference reference, MGenericResource target, 
			MSelector referenceSelector) 
	{
		MAnnotationCitationReference ref = new MAnnotationCitationReference();
		AnnotationFactory.createAnnotationReference(ref, creator, tool, reference, target);
		ref.setReferenceIndex(referenceIndex);
		ref.setReferenceSelector(referenceSelector);
		return ref;
	}
	
	public static MAnnotationCitationReference createCitationReference(
			MAgent creator, ISoftware tool, Integer referenceIndex, 
			MReference reference, MGenericResource target, 
			List<MSelector> citationSelector, MSelector referenceSelector) 
	{
		MAnnotationCitationReference ref = new MAnnotationCitationReference();
		AnnotationFactory.createAnnotationReference(ref, creator, tool, reference, target);
		ref.setReferenceIndex(referenceIndex);
		ref.setCitationSelectors(citationSelector);
		ref.setReferenceSelector(referenceSelector);
		return ref;
	}
	
	//  SELECTORS ------------------------------------------------------------
	// -----------------------------------------------------------------------
	/**
	 * Used by the unmarshallers. The creator is initialized through lazy binding.
	 * @param individualUri	The uri 
	 * @param createdOn		The creation date
	 * @param target		The Annotation target
	 * @param exact			The exact match
	 * @param prefix		The prefix
	 * @param suffix		The suffix
	 * @return The unmarshalled MTextQuoteSelector
	 */
	public static MTextQuoteSelector clonePrefixSuffixTextSelector(
			String individualUri, Date createdOn,
			MGenericResource target, String exact,
			String prefix, String suffix) 
	{
		MTextQuoteSelector sel = new MTextQuoteSelector();
		
		sel.setLocalId(getLocalId());
		sel.setUuid("");
		sel.setUri(individualUri);
		sel.setCreatedOn(createdOn);
		sel.setExact(exact);
		sel.setPrefix(prefix);
		sel.setSuffix(suffix);
		sel.setTarget(target);
		return sel;
	}
	
	public static MTextQuoteSelector createPrefixSuffixTextSelector(
			IAgent creator, MGenericResource target, String exact,
			String prefix, String suffix) 
	{
		MTextQuoteSelector sel = new MTextQuoteSelector();
		sel.setLocalId(getLocalId());
		sel.setUuid(getUuid());
		sel.setUri(getUrn(sel.getUuid()));
		sel.setCreatedOn(new Date());
		sel.setCreator(creator);
		sel.setExact(exact);
		sel.setPrefix(prefix);
		sel.setSuffix(suffix);
		sel.setTarget(target);
		return sel;
	}
	
	public static MAnnotationSelector createAnnotationSelector(
			IAgent creator, MGenericResource target, MAnnotation annotation) 
	{
		MAnnotationSelector sel = new MAnnotationSelector();
		sel.setLocalId(getLocalId());
		sel.setUuid(getUuid());
		sel.setUri(getUrn(sel.getUuid()));
		sel.setCreatedOn(new Date());
		sel.setCreator(creator);
		sel.setAnnotation(annotation);
		sel.setTarget(target);
		return sel;
	}
	
	public static MAnnotationSelector cloneAnnotationSelector(
			String individualUri, Date createdOn,
			IAgent creator, MGenericResource target) 
	{
		MAnnotationSelector sel = new MAnnotationSelector();
		sel.setLocalId(getLocalId());
		sel.setUuid("");
		sel.setUri(individualUri);
		sel.setCreatedOn(createdOn);
		sel.setCreator(creator);
		sel.setTarget(target);
		return sel;
	}
	
	/**
	 * Creation of the MAnnotationSelector used by unmarshallers.
	 * @param individualUri	The uri of the selector
	 * @param createdOn		The creation date
	 * @param target		The target of the annotation
	 * @return	The MAnnotationSelector
	 */
	public static MAnnotationSelector cloneAnnotationSelector(
			String individualUri, Date createdOn,
			MGenericResource target) 
	{
		MAnnotationSelector sel = new MAnnotationSelector();
		sel.setLocalId(getLocalId());
		sel.setUuid("");
		sel.setUri(individualUri);
		sel.setCreatedOn(createdOn);
		sel.setTarget(target);
		return sel;
	}
	
	public static MImageInDocumentSelector cloneImageInDocumentSelector(
			String individualUri, Date createdOn,
			IAgent creator, MGenericResource target, MGenericResource context) 
	{
		MImageInDocumentSelector sel = new MImageInDocumentSelector();
		sel.setLocalId(getLocalId());
		sel.setUuid("");
		sel.setUri(individualUri);
		sel.setCreatedOn(createdOn);
		sel.setTarget(target);
		sel.setContext(context);
		return sel;
	}
	
	/**
	 * 
	 * @param individualUri
	 * @param createdOn
	 * @param target
	 * @param context
	 * @return
	 */
	public static MImageInDocumentSelector cloneImageInDocumentSelector(
			String individualUri, Date createdOn,
			MGenericResource target, MGenericResource context) 
	{
		MImageInDocumentSelector sel = new MImageInDocumentSelector();
		sel.setLocalId(getLocalId());
		sel.setUuid("");
		sel.setUri(individualUri);
		sel.setCreatedOn(createdOn);
		sel.setTarget(target);
		sel.setContext(context);
		return sel;
	}
	
	//  HIGHLIGHTS -----------------------------------------------------------
	// -----------------------------------------------------------------------
	public static MHighlightAnnotation createHighlight(
			MAnnotationSet set, IAgent creator, ISoftware tool,
			MGenericResource target, MSelector selector) 
	{
		MHighlightAnnotation ann = new MHighlightAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid(getUuid());
		ann.setIndividualUri(getUrn(ann.getUuid()));
		//ann.setTarget(target);
		ann.setCreator(creator);
		ann.setCreatedOn(new Date());
		ann.setSelector(selector);
		ann.setTool(tool);
		return ann;
	}
	
	public static MHighlightAnnotation createHighlight(
			MAnnotationSet set, IAgent creator, ISoftware tool) 
	{
		MHighlightAnnotation ann = new MHighlightAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid(getUuid());
		ann.setIndividualUri(getUrn(ann.getUuid()));
		//ann.setTarget(target);
		ann.setCreator(creator);
		ann.setCreatedOn(new Date());
		ann.setTool(tool);
		return ann;
	}
	
	public static MHighlightAnnotation cloneHighlight(
			String individualUri, String lineageUri,
			Date createdOn, Date lastSavedOn, 
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			String versionNumber, String previousVersion,
			MGenericResource target, ArrayList<MSelector> selectors,
			String label) 
	{
		MHighlightAnnotation ann = new MHighlightAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid("");
		ann.setLineageUri(lineageUri);
		ann.setIndividualUri(individualUri);
		ann.setCreator(creator);
		ann.setCreatedOn(createdOn);
		ann.setLastSavedOn(lastSavedOn);
		ann.setVersionNumber(versionNumber);
		ann.setPreviousVersion(previousVersion);
		ann.getSelectors().addAll(selectors);
		ann.setHasChanged(false);
		ann.setTool(tool);
		return ann;
	}
	
	//  TEMPORARY ------------------------------------------------------------
	// -----------------------------------------------------------------------
	public static MSelectionAnnotation createTemporary(
			MAnnotationSet set, IAgent creator, 
			MGenericResource target, MSelector selector) 
	{
		MSelectionAnnotation ann = new MSelectionAnnotation();
		ann.setLocalId(getLocalId());
		//ann.setTarget(target);
		ann.setCreator(creator);
		ann.setCreatedOn(new Date());
		ann.setSelector(selector);
		return ann;
	}
	
	//  POST ITS -----------------------------------------------------------
	// -----------------------------------------------------------------------
	public static MPostItAnnotation createPostIt(
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			MGenericResource target, MSelector selector,
			PostitType type, String body) 
	{
		MPostItAnnotation ann = new MPostItAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid(getUuid());
		ann.setIndividualUri(getUrn(ann.getUuid()));
		ann.setCreator(creator);
		ann.setCreatedOn(new Date());
		ann.setSelector(selector);
		ann.setType(type);
		ann.setBody(createContentAsRdf(body));
		ann.setNewVersion(true);
		ann.setTool(tool);
		return ann;
	}
	
	public static MContentAsRdf createContentAsRdf(String text) {
		MContentAsRdf body = new MContentAsRdf();
		body.setIndividualUri(getUuid());
		body.setFormat("text/plain");
		body.setChars(text);
		return body;
	}
	
	public static MTargetSelector createTargetSelector(IAgent creator, 
			MGenericResource target) {
		MTargetSelector selector = new MTargetSelector();
		selector.setCreator(creator);
		selector.setCreatedOn(new Date());
		selector.setTarget(target);
		selector.setLocalId(getLocalId());
		selector.setUuid(getUuid());
		selector.setUri(getUrn(selector.getUuid()));
		return selector;
	}
	
	public static MTargetSelector cloneTargetSelector(
		String individualUri, MGenericResource target) 
	{
		MTargetSelector sel = new MTargetSelector();
		sel.setLocalId(getLocalId());
		sel.setUuid("");
		sel.setUri(individualUri);
		sel.setTarget(target);
		return sel;
	}
	
	public static MPostItAnnotation createPostIt(
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			PostitType type, String body) 
	{
		MPostItAnnotation ann = new MPostItAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid(getUuid());
		ann.setIndividualUri(getUrn(ann.getUuid()));
		ann.setCreator(creator);
		ann.setCreatedOn(new Date());
		ann.setType(type);
		ann.setBody(createContentAsRdf(body));
		ann.setNewVersion(true);
		ann.setTool(tool);
		return ann;
	}
	
	public static MPostItAnnotation createPostIt(IDomeo domeo,
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			MGenericResource target, 
			PostitType type, String body) 
	{
		MImageInDocumentSelector selector = new MImageInDocumentSelector();
		selector.setCreator(creator);
		selector.setCreatedOn(new Date());
		selector.setTarget(target);
		selector.setLocalId(getLocalId());
		selector.setUuid(getUuid());
		selector.setUri(getUrn(selector.getUuid()));
		selector.setContext(domeo.getAnnotationPersistenceManager().getCurrentResource());
		
		MPostItAnnotation ann = new MPostItAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid(getUuid());
		ann.setIndividualUri(getUrn(ann.getUuid()));

		//ann.setTarget(target);
		//ann.setSelector(createTargetSelector(creator, target));
		ann.setSelector(selector);
		ann.setCreator(creator);
		ann.setCreatedOn(new Date());
		ann.setType(type);
		ann.setBody(createContentAsRdf(body));
		ann.setNewVersion(true);
		ann.setTool(tool);
		return ann;
	}
	
	public static MPostItAnnotation clonePostIt(
			String individualUri, String lineageUri,
			Date createdOn, Date lastSavedOn, 
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			String versionNumber, String previousVersion,
			MGenericResource target, ArrayList<MSelector> selectors,
			String label, MContentAsRdf body, PostitType type) 
	{
		MPostItAnnotation ann = new MPostItAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid("");
		ann.setLineageUri(lineageUri);
		ann.setIndividualUri(individualUri);
		ann.setCreator(creator);
		ann.setCreatedOn(createdOn);
		ann.setLastSavedOn(lastSavedOn);
		ann.setVersionNumber(versionNumber);
		ann.setPreviousVersion(previousVersion);
		ann.getSelectors().addAll(selectors);
		ann.setBody(body);
		ann.setType(type);
		ann.setTool(tool);
		ann.setHasChanged(false);
		return ann;
	}
	
	//  QUALIFIER ------------------------------------------------------------
	// -----------------------------------------------------------------------
	public static MQualifierAnnotation createQualifier(
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			MGenericResource target, MSelector selector,
			MLinkedResource term) 
	{
		MQualifierAnnotation ann = new MQualifierAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid(getUuid());
		ann.setIndividualUri(getUrn(ann.getUuid()));
		//ann.setTarget(target);
		ann.setCreator(creator);
		ann.setCreatedOn(new Date());
		ann.setSelector(selector);
		ann.setTerm(term);
		ann.setNewVersion(true);
		ann.setTool(tool);
		return ann;
	}
	
	public static MQualifierAnnotation createQualifier(
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			MGenericResource target, MSelector selector) 
	{
		MQualifierAnnotation ann = new MQualifierAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid(getUuid());
		ann.setIndividualUri(getUrn(ann.getUuid()));
		//ann.setTarget(target);
		ann.setCreator(creator);
		ann.setCreatedOn(new Date());
		ann.setSelector(selector);
		ann.setNewVersion(true);
		ann.setTool(tool);
		return ann;
	}
	
	public static MQualifierAnnotation createQualifier(IDomeo domeo,
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			MGenericResource target, 
			MLinkedResource term) 
	{
		MImageInDocumentSelector selector = new MImageInDocumentSelector();
		selector.setCreator(creator);
		selector.setCreatedOn(new Date());
		selector.setTarget(target);
		selector.setLocalId(getLocalId());
		selector.setUuid(getUuid());
		selector.setUri(getUrn(selector.getUuid()));
		selector.setContext(domeo.getAnnotationPersistenceManager().getCurrentResource());
		
		MQualifierAnnotation ann = new MQualifierAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid(getUuid());
		ann.setIndividualUri(getUrn(ann.getUuid()));
		
		//ann.setTarget(target);
		ann.setSelector(selector);
		ann.setCreator(creator);
		ann.setCreatedOn(new Date());
		ann.setTerm(term);
		ann.setNewVersion(true);
		ann.setTool(tool);
		return ann;
	}
	
	public static MImageInDocumentSelector createImageSelector(IDomeo domeo, IAgent creator, MGenericResource target) {
		MImageInDocumentSelector selector = new MImageInDocumentSelector();
		selector.setCreator(creator);
		selector.setCreatedOn(new Date());
		selector.setTarget(target);
		selector.setLocalId(getLocalId());
		selector.setUuid(getUuid());
		selector.setUri(getUrn(selector.getUuid()));
		selector.setContext(domeo.getAnnotationPersistenceManager().getCurrentResource());
		return selector;
	}
	
	public static MQualifierAnnotation createQualifier(IDomeo domeo,
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			MGenericResource target) 
	{
		
		MImageInDocumentSelector selector = createImageSelector(domeo, creator, target);
		
//		MImageInDocumentSelector selector = new MImageInDocumentSelector();
//		selector.setCreator(creator);
//		selector.setCreatedOn(new Date());
//		selector.setTarget(target);
//		selector.setLocalId(getLocalId());
//		selector.setUuid(getUuid());
//		selector.setUri(getUrn(selector.getUuid()));
//		selector.setContext(domeo.getAnnotationPersistenceManager().getCurrentResource());
		
		MQualifierAnnotation ann = new MQualifierAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid(getUuid());
		ann.setIndividualUri(getUrn(ann.getUuid()));
		
		//ann.setTarget(target);
		ann.setSelector(selector);
		ann.setCreator(creator);
		ann.setCreatedOn(new Date());
		ann.setNewVersion(true);
		ann.setTool(tool);
		return ann;
	}
	
	public static MQualifierAnnotation cloneQualifier(
			String individualUri, String lineageUri,
			Date createdOn, Date lastSavedOn, 
			MAnnotationSet set, IAgent creator, 
			String versionNumber, String previousVersion,
			MGenericResource target, ArrayList<MSelector> selectors,
			String label) 
	{
		MQualifierAnnotation ann = new MQualifierAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid("");
		ann.setLineageUri(lineageUri);
		ann.setIndividualUri(individualUri);
		ann.setCreator(creator);
		ann.setCreatedOn(createdOn);
		ann.setLastSavedOn(lastSavedOn);
		ann.setVersionNumber(versionNumber);
		ann.setPreviousVersion(previousVersion);
		ann.getSelectors().addAll(selectors);
		ann.setHasChanged(false);
		return ann;
	}
	
	//  COMMENTS ------------------------------------------------------------
	// -----------------------------------------------------------------------
	public static MCommentAnnotation cloneComment(
			String individualUri, String lineageUri,
			Date createdOn, Date lastSavedOn, 
			String versionNumber, String previousVersion,
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			MGenericResource target, ArrayList<MSelector> selectors,
			String text) 
	{
		MCommentAnnotation ann = new MCommentAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid("");
		ann.setLineageUri(lineageUri);
		ann.setIndividualUri(individualUri);
		ann.setCreator(creator);
		ann.setCreatedOn(createdOn);
		ann.setLastSavedOn(lastSavedOn);
		ann.setVersionNumber(versionNumber);
		ann.setPreviousVersion(previousVersion);
		ann.getSelectors().addAll(selectors);
		ann.setText(text);
		ann.setHasChanged(false);
		ann.setTool(tool);
		return ann;
	}
	
	public static MCommentAnnotation createComment(
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			MGenericResource target, MSelector selector,
			String text) 
	{
		MCommentAnnotation ann = new MCommentAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid(getUuid());
		ann.setIndividualUri(getUrn(ann.getUuid()));
		//ann.setTarget(target);O
		ann.setCreator(creator);
		ann.setCreatedOn(new Date());
		ann.setSelector(selector);
		ann.setText(text);
		ann.setNewVersion(true);
		ann.setTool(tool);
		return ann;
	}
	
	public static MCommentAnnotation createComment(
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			MGenericResource target, 
			String text)  
	{
		MCommentAnnotation ann = new MCommentAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid(getUuid());
		ann.setIndividualUri(getUrn(ann.getUuid()));
		//ann.setTarget(target);
		ann.setCreator(creator);
		ann.setCreatedOn(new Date());
		ann.setText(text);
		ann.setNewVersion(true);
		ann.setTool(tool);
		return ann;
	}
}
