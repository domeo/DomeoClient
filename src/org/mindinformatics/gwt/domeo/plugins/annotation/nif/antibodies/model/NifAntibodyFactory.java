package org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model;

import java.util.ArrayList;
import java.util.Date;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.selectors.MImageInDocumentSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.model.agents.IAgent;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class NifAntibodyFactory extends AnnotationFactory {

	//  QUALIFIER ------------------------------------------------------------
	// -----------------------------------------------------------------------
	public static MAntibodyAnnotation createAntibody(IDomeo domeo,
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			MGenericResource target) 
	{
		MImageInDocumentSelector selector = new MImageInDocumentSelector();
		selector.setCreator(creator);
		selector.setCreatedOn(new Date());
		selector.setTarget(target);
		selector.setLocalId(getLocalId());
		selector.setUuid(getUuid());
		selector.setUri(getUrn(selector.getUuid()));
		selector.setContext(domeo.getAnnotationPersistenceManager().getCurrentResource());
		
		MAntibodyAnnotation ann = new MAntibodyAnnotation();
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
	
	public static MAntibodyAnnotation createAntibody(
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			MGenericResource target, MSelector selector) 
	{
		MAntibodyAnnotation ann = new MAntibodyAnnotation();
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
	
	public static MAntibodyUsage createAntibodyUsage() {
		MAntibodyUsage usage = new MAntibodyUsage();
		usage.setLocalId(getLocalId());
		usage.setIndividualUri(getUrn(getUuid()));
		return usage;
	}
	
	public static MAntibodyAnnotation createAntibody(
			String individualUri, String lineageUri,
			Date createdOn, Date lastSavedOn, 
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			String versionNumber, String previousVersion,
			MGenericResource target, ArrayList<MSelector> selectors,
			String label) 
	{
		MAntibodyAnnotation ann = new MAntibodyAnnotation();
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
	
}
