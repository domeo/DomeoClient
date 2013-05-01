package org.mindinformatics.gwt.domeo.plugins.annotation.curation.model;

import java.util.ArrayList;
import java.util.Date;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.selectors.MAnnotationSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.model.agents.IAgent;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CurationFactory extends AnnotationFactory {

	public static MCurationToken createCurationToken(IDomeo domeo,
			IAgent creator, ISoftware tool, 
			MAnnotation target, String status, String description) 
	{
		MAnnotationSelector selector = AnnotationFactory.createAnnotationSelector(
				creator, domeo.getPersistenceManager().getCurrentResource(), target);
		
		MCurationToken ann = new MCurationToken();
		ann.setLocalId(getLocalId());
		ann.setUuid(getUuid());
		ann.setIndividualUri(getUrn(ann.getUuid()));
		
		//ann.setTarget(target);
		ann.setSelector(selector);
		ann.setCreator(creator);
		ann.setCreatedOn(new Date());
		ann.setNewVersion(true);
		ann.setTool(tool);
		
		ann.setStatus(status);
		ann.setDescription(description);
		return ann;
	}
	
	public static MCurationToken cloneCurationToken(
			String individualUri, String lineageUri,
			Date createdOn, Date lastSavedOn, 
			String versionNumber, String previousVersion,
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			MGenericResource target, ArrayList<MSelector> selectors,
			String value, String description) 
	{
		MCurationToken ann = new MCurationToken();
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
		ann.setStatus(value);
		ann.setDescription(description);
		ann.setHasChanged(false);
		ann.setTool(tool);
		return ann;
	}
}
