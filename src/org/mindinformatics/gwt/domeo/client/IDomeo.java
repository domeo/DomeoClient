package org.mindinformatics.gwt.domeo.client;

import org.mindinformatics.gwt.domeo.client.feature.clipboard.ClipboardManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.AnnotationCardsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AnnotationFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.helpers.AnnotationHelpersManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.PluginsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.search.AnnotationSearchManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.AnnotationTailsManager;
import org.mindinformatics.gwt.domeo.client.ui.content.ContentPanel;
import org.mindinformatics.gwt.domeo.client.ui.content.IContentHandler;
import org.mindinformatics.gwt.domeo.client.ui.toolbar.DomeoToolbarPanel;
import org.mindinformatics.gwt.domeo.component.cache.images.src.ImagesCache;
import org.mindinformatics.gwt.domeo.component.linkeddata.digesters.LinkedDataDigestersManager;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.accesscontrol.AnnotationAccessManager;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IPersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling.JsonUnmarshallingManager;
import org.mindinformatics.gwt.domeo.services.extractors.ContentExtractorsManager;
import org.mindinformatics.gwt.framework.component.ui.east.ASideTab;
import org.mindinformatics.gwt.framework.src.IApplication;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IDomeo extends IContentHandler, IApplication {

	// Content loading
	public void resetContentLoading();
	public void attemptContentLoading(String url);
	public void loadContent(String url);
	
	public IPersistenceManager getAnnotationPersistenceManager();

	public boolean isManualHighlightEnabled();
	public boolean isManualAnnotationEnabled();
	public boolean isManualClipAnnotationEnabled();
	
	public ContentPanel getContentPanel();
	public PluginsManager getPluginsManager();
	public IPersistenceManager getPersistenceManager();
	public ContentExtractorsManager getExtractorsManager();
	public AnnotationAccessManager getAnnotationAccessManager();
	
	public void checkForExistingAnnotationSets();
	
	public ClipboardManager getClipboardManager();
	
	public AnnotationTailsManager getAnnotationTailsManager();
	public AnnotationFormsManager getAnnotationFormsManager();
	public AnnotationCardsManager getAnnotationCardsManager();
	public AnnotationSearchManager getAnnotationSearchManager();
	public AnnotationHelpersManager getAnnotationHelpersManager();
	
	public LinkedDataDigestersManager getLinkedDataDigestersManager();

	public  ASideTab getDiscussionSideTab();
	
	
	public void notifyEndExtraction();

	public void refreshAllComponents();
	public void refreshResourceComponents();
	public void refreshClipboardComponents();
	public void refreshAnnotationComponents();
	
	public void displayAnnotationOfSet(MAnnotationSet set);
	public void removeAnnotationSetTab(MAnnotationSet set);
	
	public DomeoToolbarPanel getToolbarPanel();
	public JsonUnmarshallingManager getUnmarshaller();
	public ImagesCache getImagesCache();
	
	public void jumpToLocation(float percentage);
}
