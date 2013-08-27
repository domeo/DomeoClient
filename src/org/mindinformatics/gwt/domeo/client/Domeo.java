/*
 * Copyright 2013 Massachusetts General Hospital
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.mindinformatics.gwt.domeo.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.feature.clipboard.ClipboardManager;
import org.mindinformatics.gwt.domeo.client.feature.clipboard.ui.east.ClipboardSidePanel;
import org.mindinformatics.gwt.domeo.client.feature.clipboard.ui.east.ClipboardSideTab;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.AnnotationCardsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AnnotationFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.helpers.AnnotationHelpersManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.PluginsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.search.AnnotationSearchManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.style.AnnotationTypeStyleStrategy;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.AnnotationTailsManager;
import org.mindinformatics.gwt.domeo.client.ui.content.AnnotationFrameWrapper;
import org.mindinformatics.gwt.domeo.client.ui.content.ContentPanel;
import org.mindinformatics.gwt.domeo.client.ui.dialog.LoadDocumentQuestionDialog;
import org.mindinformatics.gwt.domeo.client.ui.dialog.LoadingDocumentMessageDialog;
import org.mindinformatics.gwt.domeo.client.ui.dialog.ReloadDocumentQuestionDialog;
import org.mindinformatics.gwt.domeo.client.ui.east.annotation.AnnotationSidePanel;
import org.mindinformatics.gwt.domeo.client.ui.east.annotation.AnnotationSideTab;
import org.mindinformatics.gwt.domeo.client.ui.east.annotation.AnnotationSummaryTable;
import org.mindinformatics.gwt.domeo.client.ui.east.annotation.view.AnnotationForSetSidePanel;
import org.mindinformatics.gwt.domeo.client.ui.east.annotation.view.AnnotationSetViewerSideTab;
import org.mindinformatics.gwt.domeo.client.ui.east.debug.AnnotationDebugSidePanel;
import org.mindinformatics.gwt.domeo.client.ui.east.debug.AnnotationDebugSideTab;
import org.mindinformatics.gwt.domeo.client.ui.east.resource.ResourceSidePanel;
import org.mindinformatics.gwt.domeo.client.ui.east.resource.ResourceSideTab;
import org.mindinformatics.gwt.domeo.client.ui.east.sets.AnnotationSetSideTab;
import org.mindinformatics.gwt.domeo.client.ui.east.sets.AnnotationSetsSidePanel;
import org.mindinformatics.gwt.domeo.client.ui.toolbar.DomeoToolbarPanel;
import org.mindinformatics.gwt.domeo.component.cache.images.src.ImagesCache;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.ICachedImages;
import org.mindinformatics.gwt.domeo.component.linkeddata.digesters.LinkedDataDigestersManager;
import org.mindinformatics.gwt.domeo.component.linkeddata.model.JsoLinkedDataResource;
import org.mindinformatics.gwt.domeo.component.textmining.src.TextMiningRegistry;
import org.mindinformatics.gwt.domeo.model.MAnnotationCitationReference;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.MOnlineResource;
import org.mindinformatics.gwt.domeo.model.accesscontrol.AnnotationAccessManager;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.east.CommentSidePanel;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.east.CommentSideTab;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.tile.CommentTileProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.highlight.info.HighlightPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.highlight.model.MHighlightAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.highlight.search.HighligthSearchComponent;
import org.mindinformatics.gwt.domeo.plugins.annotation.highlight.ui.card.HighlightCardProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.highlight.ui.tile.HighlightTileProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.info.MicroPublicationsPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MicroPublicationCache;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.card.MicroPublicationCardProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.form.MicroPublicationFormProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.tile.MicroPublicationTileProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.info.AntibodyPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibodyAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.search.AntibodySearchComponent;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.ui.card.AntibodyCardProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.ui.form.AntibodyFormProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.ui.tile.AntibodyTileProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.info.PersistencePlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IPersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingAnnotationSetHandler;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingAnnotationSetListHandler;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.testing.GwtPersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.testing.JsonPersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.testing.StandalonePersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.ui.ExistingAnnotationViewerPanel;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.info.PostitPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.search.PostItSearchComponent;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.ui.card.PostItCardProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.ui.form.PostItFormProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.ui.tile.PostItTileProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.info.QualifierPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.HQualifierHelper;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.search.QualifierSearchComponent;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.card.QualifierCardProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.form.QualifierFormProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.tile.QualifierTileProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.selection.info.SelectionPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.selection.model.MSelectionAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.selection.ui.tile.SelectionTileProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.info.SPLsPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.form.SPLsFormProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.tile.SPLsTileProvider;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationTarget;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling.JsonUnmarshallingManager;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.digesters.BioPortalTermsDigester;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.info.BioPortalPlugin;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.BioPortalManager;
import org.mindinformatics.gwt.domeo.plugins.resource.document.info.DocumentPlugin;
import org.mindinformatics.gwt.domeo.plugins.resource.document.lenses.LDocumentResourceCardPanel;
import org.mindinformatics.gwt.domeo.plugins.resource.document.model.MDocumentResource;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.digesters.NifStandardDigester;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.service.NifManager;
import org.mindinformatics.gwt.domeo.plugins.resource.omim.info.OmimPlugin;
import org.mindinformatics.gwt.domeo.plugins.resource.omim.lenses.LOmimDocumentCardPanel;
import org.mindinformatics.gwt.domeo.plugins.resource.omim.model.MOmimDocument;
import org.mindinformatics.gwt.domeo.plugins.resource.opentrials.info.OpenTrialsPlugin;
import org.mindinformatics.gwt.domeo.plugins.resource.opentrials.lenses.LOpenTrialsDocumentCardPanel;
import org.mindinformatics.gwt.domeo.plugins.resource.opentrials.model.MOpenTrialsDocument;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.info.PubMedPlugin;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.lenses.LPubMedDocumentCardPanel;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.model.MPubMedDocument;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.info.PmcPlugin;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.ui.card.ReferenceCardProvider;
import org.mindinformatics.gwt.domeo.services.extractors.ContentExtractorsManager;
import org.mindinformatics.gwt.framework.component.agents.src.IAgentManager;
import org.mindinformatics.gwt.framework.component.agents.src.testing.GwtAgentManager;
import org.mindinformatics.gwt.framework.component.agents.src.testing.JsonAgentManager;
import org.mindinformatics.gwt.framework.component.agents.src.testing.StandaloneAgentManager;
import org.mindinformatics.gwt.framework.component.persistance.src.PersistenceManager;
import org.mindinformatics.gwt.framework.component.preferences.src.BooleanPreference;
import org.mindinformatics.gwt.framework.component.profiles.src.IProfileManager;
import org.mindinformatics.gwt.framework.component.profiles.src.testing.GwtProfileManager;
import org.mindinformatics.gwt.framework.component.profiles.src.testing.JsonProfileManager;
import org.mindinformatics.gwt.framework.component.profiles.src.testing.StandaloneProfileManager;
import org.mindinformatics.gwt.framework.component.reporting.src.IReportsManager;
import org.mindinformatics.gwt.framework.component.reporting.src.testing.GwtReportManager;
import org.mindinformatics.gwt.framework.component.reporting.src.testing.JsonReportManager;
import org.mindinformatics.gwt.framework.component.reporting.src.testing.StandaloneReportManager;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.ui.dialog.ProgressMessagePanel;
import org.mindinformatics.gwt.framework.component.ui.east.ASideTab;
import org.mindinformatics.gwt.framework.component.ui.east.SidePanelsContainer;
import org.mindinformatics.gwt.framework.component.ui.east.SidePanelsFacade;
import org.mindinformatics.gwt.framework.component.ui.east.SideTabsContainer;
import org.mindinformatics.gwt.framework.component.ui.glass.DialogGlassPanel;
import org.mindinformatics.gwt.framework.component.ui.glass.EnhancedGlassPanel;
import org.mindinformatics.gwt.framework.component.ui.lenses.resources.LGenericResourceCardPanel;
import org.mindinformatics.gwt.framework.component.users.src.IUserManager;
import org.mindinformatics.gwt.framework.component.users.src.testing.GwtUserManager;
import org.mindinformatics.gwt.framework.component.users.src.testing.JsonUserManager;
import org.mindinformatics.gwt.framework.component.users.src.testing.StandaloneUserManager;
import org.mindinformatics.gwt.framework.src.Application;
import org.mindinformatics.gwt.framework.src.ApplicationUtils;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;
import org.mindinformatics.gwt.utils.src.HtmlUtils;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
//import org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.card.SPLsCardProvider;
//import org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.tile.SPLsTileProvider;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class Domeo extends Application implements IDomeo, EntryPoint, /*IRetrieveExistingBibliographySetHandler,*/ IRetrieveExistingAnnotationSetHandler, IRetrieveExistingAnnotationSetListHandler {
	
	public static final boolean verbose = false;
	public static final boolean pathfinder = true;

	public static String APP_NAME = "Domeo";
	public static String APP_VERSION = "b31";
	public static String APP_VERSION_LABEL = "build 31";
	
	public String getApplicationName() { return APP_NAME; }
	public String getApplicationVersion() { return APP_VERSION; }
	public String getApplicationVersionLabel() { return APP_VERSION_LABEL; }

	public static final String PREF_ANN_MULTIPLE_TARGETS = "Annotation of multPiple targets";
	public static final String PREF_WRAP_TABLES = AnnotationFrameWrapper.PREF_WRAP_TABLES;
	public static final String PREF_WRAP_IMAGES = AnnotationFrameWrapper.PREF_WRAP_IMAGES;
	public static final String PREF_WRAP_LINKS = AnnotationFrameWrapper.PREF_WRAP_LINKS;
	public static final String PREF_DISPLAY_PROVENANCE = "Display provenance";
	public static final String PREF_DISPLAY_USER_PROVENANCE = "Display users provenance";
	public static final String PREF_DISPLAY_ANNOTATION_FOR_DEBUG = "Display annotation for debug";
	
	public static final String PREF_PERFORM_ANNOTATION_OF_REFERENCES = "Perform references annotation";
	public static final String PREF_PERFORM_ANNOTATION_OF_CITATIONS = "Perform references citations";
	public static final String PREF_SHOW_ANNOTATION_OF_REFERENCES = "Show references annotation";
	public static final String PREF_SHOW_ANNOTATION_OF_CITATIONS = "Show references citations";
	
	public static final String PREF_ALLOW_COMMENTING = "Allow commenting";
	public static final String PREF_ALLOW_CURATION = "Allow curation";
	public static final String PREF_ASK_BEFORE_DELETION = "Ask before deletion";
	
	public static final String PREF_ALLOW_CURATION_MOTIVATION = "Allow curation motivation";
	
	/**
	 * The static images used throughout the Domeo application.
	 */
	public static final Resources resources = GWT.create(Resources.class);
	
	private JsonUnmarshallingManager jsonUnmarshallingManager;
	
	// UI
	Label initUserLabel;
	DockLayoutPanel p = new DockLayoutPanel(Unit.PX);
	DomeoToolbarPanel domeoToolbarPanel;
	ContentPanel contentPanel;
	
	SideTabsContainer sideTabPanel;
	SidePanelsContainer eastPanel;
	
	ImagesCache imagesCache;
	
	//AnnotationPersistenceManager persistenceManager;
	AnnotationAccessManager accessManager;
	ContentExtractorsManager extractorsManager;
	ClipboardManager clipboardManager;
	
	// ========================================================================
	// Annotation Tails Manager
	// ========================================================================
	protected PluginsManager pluginsManager = new PluginsManager();
	public PluginsManager getPluginsManager() {
		return pluginsManager;
	}
	
	// ========================================================================
	// Annotation Tails Manager
	// ========================================================================
	protected AnnotationTailsManager annotationTailsManager = new AnnotationTailsManager();
	public AnnotationTailsManager getAnnotationTailsManager() {
		return annotationTailsManager;
	}
	
	// ========================================================================
	// Annotation Forms Manager
	// ========================================================================
	protected AnnotationFormsManager annotationFormsManager = new AnnotationFormsManager();
	public AnnotationFormsManager getAnnotationFormsManager() {
		return annotationFormsManager;
	}
	
	// ========================================================================
	// Annotation Cards Manager
	// ========================================================================
	protected AnnotationCardsManager annotationCardsManager = new AnnotationCardsManager();
	public AnnotationCardsManager getAnnotationCardsManager() {
		return annotationCardsManager;
	}
	
	// ========================================================================
	// Annotation Helper Components Manager
	// ========================================================================
	protected AnnotationHelpersManager annotationHelpersManager = new AnnotationHelpersManager();
	public AnnotationHelpersManager getAnnotationHelpersManager() {
		return annotationHelpersManager;
	}
	
	// ========================================================================
	// Annotation Search Components Manager
	// ========================================================================
	protected AnnotationSearchManager searchComponentsManager = new AnnotationSearchManager();
	public AnnotationSearchManager getAnnotationSearchManager() {
		return searchComponentsManager;
	}
	
	// ========================================================================
	// Linked Data Digesters Manager
	// ========================================================================
	protected LinkedDataDigestersManager linkedDataDigestersManager = new LinkedDataDigestersManager();
	public LinkedDataDigestersManager getLinkedDataDigestersManager() {
		return linkedDataDigestersManager;
	}
	
	private ASideTab discussionSideTab;
	public  ASideTab getDiscussionSideTab() {
		return discussionSideTab;
	}
	
	public Domeo _this;
	
	/**
	 * This is the entry point method. The bulk of the application is 
	 * initialized through an asynchronous pipeline. When the pipeline
	 * is completed, the method completeInitialization() is called
	 */
	public void onModuleLoad() {
		this.logger.info(this, "Creating Domeo " + getStartingMode());
		_this=this;
		Window.addWindowClosingHandler(new Window.ClosingHandler() {

            @Override
            public void onWindowClosing(ClosingEvent event) {
            	
            	if(_this.getAnnotationPersistenceManager().isWorskspaceUnsaved()) {
            		Window.alert("The workspace contains unsaved annotation.\n\n" +
            			"By selecting 'Leaving the Page', the unsaved annotations will be lost.\n\n" +
            			"By selecting 'Stay on Page', you will have the chance to save the annotation.\n\n");
            		event.setMessage("The workspace contains unsaved annotation.");
            	}
            }
        });
		
//		 Window.addCloseHandler(new CloseHandler<Window>() {
//
//	            @Override
//	            public void onClose(CloseEvent<Window> event) {
//	            	 Window.alert("2 Closing");
//	            	 
//	            }
//	        });
		
		initApplication();
		
		resources.generalCss().ensureInjected();		
	}
	
	public  void completeInitialization() {

		// Preferences
		Preferences preferences = new Preferences(this);
		componentsManager.addComponent(preferences);
		
		// Images Cache
		imagesCache = new ImagesCache(this);
		componentsManager.addComponent(imagesCache);

		// Resources
		componentsManager.addComponent(resourcesManager);
		
		// Style
		this.getCssManager().setStrategy(new AnnotationTypeStyleStrategy(this));
		
		// Persistence
		//persistenceManager = new AnnotationPersistenceManager(this);
		//componentsManager.addComponent(persistenceManager);
		_persistenceManager = this.selectPersistenceManager();
		if(_persistenceManager!=null) {
			this.getLogger().debug(this.getClass().getName(), 
				"Persistence manager selected " + _persistenceManager.getClass().getName());
			componentsManager.addComponent(_persistenceManager);
			pluginsManager.registerPlugin(PersistencePlugin.getInstance());
		}
		
		// Clipboard
		clipboardManager = new ClipboardManager();
		componentsManager.addComponent(clipboardManager);
		
		accessManager = new AnnotationAccessManager(this);
		componentsManager.addComponent(accessManager);
		// Components initialization
		componentsManager.initializeComponents();
		
		getInitializer().updateMessage("Creating Domeo UI...");

		domeoToolbarPanel = new DomeoToolbarPanel(this);
		componentsManager.addComponent(domeoToolbarPanel);
		p.addNorth(domeoToolbarPanel, 31);
		//p.addSouth(new HTML("footer"), 20);
		//p.addWest(s, 200);
		//placeholder.setStyleName("sidebarPlaceholder");
		
		sidePanelsFacade = new SidePanelsFacade(this, p);
		componentsManager.addComponent(sidePanelsFacade);
		p.addEast(sidePanelsFacade.getSidePanelsContainer(), 35);
		
		jsonUnmarshallingManager = new JsonUnmarshallingManager(this);
		
		//eastPanel = new EastPanelsContainer(this);
		//componentsManager.addComponent(eastPanel);
		//p.addEast(eastPanel, 15);
		contentPanel = new ContentPanel(this);
		p.add(contentPanel);
		
		resourcePanelsManager.registerResourcePanel(MGenericResource.class.getName(), 
				new LGenericResourceCardPanel(this));
		//resourcePanelsManager.registerResourcePanel(MOnlineResource.class.getName(), 
		//		new LOnlineResourceCardPanel(this));
		resourcePanelsManager.registerResourcePanel(MDocumentResource.class.getName(), 
				new LDocumentResourceCardPanel(this));
		
		// OMIM Plugin 
		pluginsManager.registerPlugin(OmimPlugin.getInstance());
		if(_profileManager.getUserCurrentProfile().isPluginEnabled(OmimPlugin.getInstance().getPluginName()) ) {
			resourcePanelsManager.registerResourcePanel(MOmimDocument.class.getName(), 
				new LOmimDocumentCardPanel(this));
			pluginsManager.enablePlugin(OmimPlugin.getInstance(), true);
		}
		// Open Trials Plugin
		pluginsManager.registerPlugin(OpenTrialsPlugin.getInstance());
		if(_profileManager.getUserCurrentProfile().isPluginEnabled(OpenTrialsPlugin.getInstance().getPluginName()) ) {
			resourcePanelsManager.registerResourcePanel(MOpenTrialsDocument.class.getName(), 
				new LOpenTrialsDocumentCardPanel(this));
			pluginsManager.enablePlugin(OpenTrialsPlugin.getInstance(), true);
		}
		// Pubmed Plugin and Pubmed Central Plugin
		// PMC plugin is dependent from the PubMed plugin
		pluginsManager.registerPlugin(PubMedPlugin.getInstance());
		pluginsManager.registerPlugin(PmcPlugin.getInstance());
		if(_profileManager.getUserCurrentProfile().isPluginEnabled(PubMedPlugin.getInstance().getPluginName()) ) {
			resourcePanelsManager.registerResourcePanel(MPubMedDocument.class.getName(), new LPubMedDocumentCardPanel(this));
			pluginsManager.enablePlugin(PubMedPlugin.getInstance(), true);
			if(_profileManager.getUserCurrentProfile().isPluginEnabled(PmcPlugin.getInstance().getPluginName()) ) {
				pluginsManager.enablePlugin(PmcPlugin.getInstance(), true);
			}
		}
		
		pluginsManager.registerPlugin(DocumentPlugin.getInstance(), true);
		pluginsManager.registerPlugin(BioPortalPlugin.getInstance(), true);
		
		annotationCardsManager.registerAnnotationCard(MAnnotationCitationReference.class.getName(), 
				new ReferenceCardProvider(this));
		
		// Selection
		pluginsManager.registerPlugin(SelectionPlugin.getInstance(), true);
		annotationTailsManager.registerAnnotationTile(MSelectionAnnotation.class.getName(), 
				new SelectionTileProvider(this));
		
		// Highlight
		pluginsManager.registerPlugin(HighlightPlugin.getInstance(), true);
		annotationTailsManager.registerAnnotationTile(MHighlightAnnotation.class.getName(), 
			new HighlightTileProvider(this));
		annotationCardsManager.registerAnnotationCard(MHighlightAnnotation.class.getName(), 
			new HighlightCardProvider(this));
		searchComponentsManager.registerAnnotationCard(MHighlightAnnotation.class.getName(), 
			new HighligthSearchComponent(this));
		
		// Post It
		pluginsManager.registerPlugin(PostitPlugin.getInstance(), true);
		annotationFormsManager.registerAnnotationForm(MPostItAnnotation.class.getName(),
			new PostItFormProvider(this));
		annotationTailsManager.registerAnnotationTile(MPostItAnnotation.class.getName(), 
			new PostItTileProvider(this));
		annotationCardsManager.registerAnnotationCard(MPostItAnnotation.class.getName(), 
			new PostItCardProvider(this));
		searchComponentsManager.registerAnnotationCard(MPostItAnnotation.class.getName(), 
			new PostItSearchComponent(this));
		
		// Qualifier
		pluginsManager.registerPlugin(QualifierPlugin.getInstance(), true);	
		//pluginsManager.enablePlugin(QualifierPlugin.getInstance(), true);
		if(_profileManager.getUserCurrentProfile().isPluginEnabled(QualifierPlugin.getInstance().getPluginName())) {
			annotationFormsManager.registerAnnotationForm(MQualifierAnnotation.class.getName(),
					new QualifierFormProvider(this));
		}
		annotationTailsManager.registerAnnotationTile(MQualifierAnnotation.class.getName(), 
				new QualifierTileProvider(this));
		annotationCardsManager.registerAnnotationCard(MQualifierAnnotation.class.getName(), 
				new QualifierCardProvider(this));
		searchComponentsManager.registerAnnotationCard(MQualifierAnnotation.class.getName(), 
				new QualifierSearchComponent(this));
		annotationHelpersManager.registerAnnotationHelper(MQualifierAnnotation.class.getName(), 
				new HQualifierHelper());
		
		// Antibody
		pluginsManager.registerPlugin(AntibodyPlugin.getInstance(), true);
		//pluginsManager.enablePlugin(AntibodyPlugin.getInstance(), true);
		if(_profileManager.getUserCurrentProfile().isPluginEnabled(AntibodyPlugin.getInstance().getPluginName())) {	
			annotationFormsManager.registerAnnotationForm(MAntibodyAnnotation.class.getName(),
					new AntibodyFormProvider(this));
		}
		annotationTailsManager.registerAnnotationTile(MAntibodyAnnotation.class.getName(), 
				new AntibodyTileProvider(this));
		annotationCardsManager.registerAnnotationCard(MAntibodyAnnotation.class.getName(), 
				new AntibodyCardProvider(this));
		searchComponentsManager.registerAnnotationCard(MAntibodyAnnotation.class.getName(), 
				new AntibodySearchComponent(this));
		
		// Micropublications
		
		pluginsManager.registerPlugin(MicroPublicationsPlugin.getInstance(), true);
		//pluginsManager.enablePlugin(MicroPublicationsPlugin.getInstance(), false);
		if(_profileManager.getUserCurrentProfile().isPluginEnabled(MicroPublicationsPlugin.getInstance().getPluginName())) {	
			annotationFormsManager.registerAnnotationForm(MMicroPublicationAnnotation.class.getName(),
					new MicroPublicationFormProvider(this));
		}
		annotationTailsManager.registerAnnotationTile(MMicroPublicationAnnotation.class.getName(), 
				new MicroPublicationTileProvider(this));
		annotationCardsManager.registerAnnotationCard(MMicroPublicationAnnotation.class.getName(), 
				new MicroPublicationCardProvider(this));
		getAnnotationPersistenceManager().registerCache(new MicroPublicationCache());
		
		// Comments
		annotationTailsManager.registerAnnotationTile(MCommentAnnotation.class.getName(), 
				new CommentTileProvider(this));
		
		// SPLs
		pluginsManager.registerPlugin(SPLsPlugin.getInstance(), true);
		//pluginsManager.enablePlugin(SPLsPlugin.getInstance(), false);
		// Paolo added the check to see if the PLugin is enabled
		if(_profileManager.getUserCurrentProfile().isPluginEnabled(SPLsPlugin.getInstance().getPluginName())) {	
			annotationFormsManager.registerAnnotationForm(MSPLsAnnotation.class.getName(),
				new SPLsFormProvider(this));
		}
		annotationTailsManager.registerAnnotationTile(MSPLsAnnotation.class.getName(), 
				new SPLsTileProvider(this));
		
		// Digesters
		linkedDataDigestersManager.registerLnkedDataDigester(new NifStandardDigester());
		linkedDataDigestersManager.registerLnkedDataDigester(new BioPortalTermsDigester());
		
		sideTabPanel = sidePanelsFacade.getSideTabsContainer();
		final ASideTab annotationsSideTab = new AnnotationSideTab(this, sidePanelsFacade);
		final ASideTab resourceSideTab = new ResourceSideTab(this, sidePanelsFacade);
		final ASideTab annotationSetsSideTab2 = new AnnotationSetSideTab(this, sidePanelsFacade);
		final ASideTab clipboardSideTab = new ClipboardSideTab(this, sidePanelsFacade);
		discussionSideTab = new CommentSideTab(this, sidePanelsFacade);
		
		AnnotationSidePanel annotationSidePanel = new AnnotationSidePanel(this, sidePanelsFacade, annotationsSideTab);
		ResourceSidePanel resourceSidePanel = new ResourceSidePanel(this, sidePanelsFacade, resourceSideTab);
		AnnotationSetsSidePanel annotationSetsSidePanel2 = new AnnotationSetsSidePanel(this, sidePanelsFacade, annotationSetsSideTab2);
		ClipboardSidePanel clipboardSidePanel = new ClipboardSidePanel(this, sidePanelsFacade, clipboardSideTab);
		
		sidePanelsFacade.registerSideComponent(resourceSideTab, resourceSidePanel, (ResourceSideTab.HEIGHT+16) + "px");
		sidePanelsFacade.registerSideComponent(annotationsSideTab, annotationSidePanel, (AnnotationSideTab.HEIGHT+16) + "px");
		sidePanelsFacade.registerSideComponent(annotationSetsSideTab2, annotationSetsSidePanel2,  (AnnotationSetSideTab.HEIGHT+16) + "px");
		
		
		if(((BooleanPreference)getPreferences().getPreferenceItem(Domeo.class.getName(), 
				Domeo.PREF_ALLOW_COMMENTING)).getValue()) {
			CommentSidePanel commentSidePanel = new CommentSidePanel(this, sidePanelsFacade, discussionSideTab);
			sidePanelsFacade.registerSideComponent(discussionSideTab, commentSidePanel,  (CommentSideTab.HEIGHT+16) + "px");
		}
		
		if(((BooleanPreference)this.getPreferences().
				getPreferenceItem(Application.class.getName(), Domeo.PREF_ANN_MULTIPLE_TARGETS))!=null &&
				((BooleanPreference)this.getPreferences().getPreferenceItem(Application.class.getName(), Domeo.PREF_ANN_MULTIPLE_TARGETS)).getValue()) {
			sidePanelsFacade.registerSideComponent(clipboardSideTab, clipboardSidePanel,  (ClipboardSideTab.HEIGHT+16) + "px");
		}
	
		
		if(((BooleanPreference)getPreferences().getPreferenceItem(Domeo.class.getName(), 
				Domeo.PREF_DISPLAY_ANNOTATION_FOR_DEBUG)).getValue()) {
			final ASideTab annotationsDebugSideTab = new AnnotationDebugSideTab(this, sidePanelsFacade);
			
			AnnotationDebugSidePanel annotationDebugSidePanel = new AnnotationDebugSidePanel(this, sidePanelsFacade, annotationsDebugSideTab);
			componentsManager.addComponent(annotationDebugSidePanel);
			
			sidePanelsFacade.registerSideComponent(annotationsDebugSideTab, annotationDebugSidePanel, (AnnotationDebugSideTab.HEIGHT+16) + "px");
		}
		
		// -----------------------------------
		//  TEXT MINING SERVICES REGISTRATION
		// ===================================
		TextMiningRegistry.getInstance(this).registerTextMiningService(BioPortalManager.getInstance());
		TextMiningRegistry.getInstance(this).registerTextMiningService(NifManager.getInstance());
		
		RootPanel rpp = RootPanel.get();
		rpp.add(sideTabPanel);


		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
	    // Attach the LayoutPanel to the RootLayoutPanel. The latter will listen for
	    // resize events on the window to ensure that its children are informed of
	    // possible size changes.
	    RootLayoutPanel rp = RootLayoutPanel.get();
	    getInitializer().removeFromParent();
	    rp.add(p);
	    
	    Window.addResizeHandler(new ResizeHandler() {
	    	/*
	    	  Timer resizeTimer = new Timer() {  
	    	    @Override
	    	    public void run() {
	    	      Window.alert("Resized");
	    	    }
	    	  };
	    	  */

	    	@Override
	    	public void onResize(ResizeEvent event) {
	    		//resizeTimer.cancel();
	    		//resizeTimer.schedule(250);
	    		notifyResizing();
	    	}
	    });
		
		this.logger.info(this, "Domeo Creation completed");
		this.logger.info(this, "-------------------------------------");
		extractorsManager = new ContentExtractorsManager(this);
		componentsManager.addComponent(extractorsManager);
		
		/*
		if(!ApplicationUtils.getUrlParameter("url").isEmpty())
			Window.alert("Url: " + ApplicationUtils.getUrlParameter("url"));
		if(!ApplicationUtils.getUrlParameter("setId").isEmpty())
			Window.alert("Url: " + ApplicationUtils.getUrlParameter("setId"));
		if(!ApplicationUtils.getUrlParameter("setIds").isEmpty())
			Window.alert("Url: " + ApplicationUtils.getUrlParameter("setIds"));
		*/
		
		if(!ApplicationUtils.getUrlParameter("url").isEmpty()) {
			domeoToolbarPanel.getAddressBarPanel().setAddress(ApplicationUtils.decodeURIComponent(ApplicationUtils.getUrlParameter("url")));
			this.attemptContentLoading(ApplicationUtils.decodeURIComponent(ApplicationUtils.getUrlParameter("url")));
		}
		
		/*
		if(ApplicationUtils.getDocumentUrl()!=null && ApplicationUtils.getDocumentUrl().trim().length()>13) {
			domeoToolbarPanel.getAddressBarPanel().setAddress(ApplicationUtils.getDocumentUrl());
			this.attemptContentLoading(ApplicationUtils.getDocumentUrl());
		}
		*/
	}
	
	/* Should provide the right user manager */
	public IUserManager selectUserManager(ICommandCompleted completionCallback) {
		if(this.getUserManager()==null) {
			if(isStandaloneMode()) {
				this.setUserManager(new StandaloneUserManager((IApplication) this, completionCallback));
			} else {
				if (isHostedMode()) {
					if(isJsonFormat()) {
						this.setUserManager(new JsonUserManager((IApplication) this, completionCallback));
					} else {
						this.setUserManager(new GwtUserManager((IApplication) this, completionCallback));
					}
				} else {
					this.setUserManager(new JsonUserManager((IApplication) this, completionCallback));
					// Real service
					//getInitializer().addException("UserManager not implemented for a real situation");
					//throw new RuntimeException("UserManager not implemented for a real situation");
				}
			}
		}
		return this.getUserManager();
	}
	
	public boolean isAgentManagerDefined() {
		return this.getAgentManager()!=null;
	}
	
	/* Should provide the right agent manager */
	public IAgentManager selectAgentManager(ICommandCompleted completionCallback) {
		if(this.getAgentManager()==null) {
			if(isStandaloneMode()) {
				this.setAgentManager(new StandaloneAgentManager((IApplication) this, completionCallback));
			} else {
				if (isHostedMode()) {
					if(isJsonFormat()) {
						this.setAgentManager(new JsonAgentManager((IApplication) this, completionCallback));
					} else {
						this.setAgentManager(new GwtAgentManager((IApplication) this, completionCallback));
					}
				} else {
					// Real service
					this.setAgentManager(new JsonAgentManager((IApplication) this, completionCallback));
					//getInitializer().addException("AgentManager not implemented for a real situation");
					//throw new RuntimeException("AgentManager not implemented for a real situation");
				}
			}
		}
		return this.getAgentManager();
	}
	
	/* Should provide the right user manager */
	public IProfileManager selectProfileManager(ICommandCompleted completionCallback) {
		if(this.getProfileManager()==null) {
			if(isStandaloneMode()) {
				this.setProfileManager(new StandaloneProfileManager((IApplication) this, completionCallback));
			} else {
				if (isHostedMode()) {
					if(isJsonFormat()) {
						this.setProfileManager(new JsonProfileManager((IApplication) this, completionCallback));
					} else {
						this.setProfileManager(new GwtProfileManager((IApplication) this, completionCallback));
					}
				} else {
					// Real service
					this.setProfileManager(new JsonProfileManager((IApplication) this, completionCallback));
					//getInitializer().addException("ProfileManager not implemented for a real situation");
					//throw new RuntimeException("ProfileManager not implemented for a real situation");
				}
			}
		}
		return this.getProfileManager();
	}
	
	/* Should provide the right report manager */
	public IReportsManager selectReportManager(ICommandCompleted completionCallback) {
		if(this.getReportManager()==null) {
			if(isStandaloneMode()) {
				this.setReportManager(new StandaloneReportManager((IApplication) this, completionCallback));
			} else {
				if (isHostedMode()) {
					if(isJsonFormat()) {
						this.setReportManager(new JsonReportManager((IApplication) this, completionCallback));
					} else {
						this.setReportManager(new GwtReportManager((IApplication) this, completionCallback));
					}
				} else {
					// Real service
					this.setReportManager(new JsonReportManager((IApplication) this, completionCallback));
					//getInitializer().addException("ReportsManager not implemented for a real situation");
					//throw new RuntimeException("ReportsManager not implemented for a real situation");
				}
			}
		}
		return this.getReportManager();
	}
	
	// ========================================================================
	// Persistence Management
	// ------------------------------------------------------------------------
	// 
	// ========================================================================
	protected IPersistenceManager _persistenceManager;
	public void setPersistenceManager(IPersistenceManager persistenceManager) { _persistenceManager = persistenceManager; }
	public IPersistenceManager getAnnotationPersistenceManager() { return _persistenceManager; }
	public IPersistenceManager getPersistenceManager() { return _persistenceManager; }

	/* Should provide the right report manager */
	public IPersistenceManager selectPersistenceManager() {
		if(isStandaloneMode()) {
			return new StandalonePersistenceManager(this, null);
		} else {
			if (isHostedMode()) {
				if(isJsonFormat()) {
					return new JsonPersistenceManager(this, null);
				} else {
					return new GwtPersistenceManager(this, null);
				}
			} else {
				// Real service
				return new JsonPersistenceManager(this, null);
				//throw new RuntimeException("PersistenceManager not implemented for a real situation");
			}
		}
	}

	private boolean contentLoading = false;
	
	public void resetContentLoading() {
		domeoToolbarPanel.getAddressBarPanel().setAddress(_persistenceManager.getCurrentResourceUrl());
		contentLoading = false;
	}
	
	
	@Override
	public void attemptContentLoading(String url) {
		if(!contentLoading) {
			contentLoading = true;
			this.getLogger().command(this.getClass().getName(), "Attempt loading of " + url);
			
			// If no document just load
			if(!_persistenceManager.isResourceLoaded()) {
				loadContent(url);
			} else if (!_persistenceManager.isDocumentAlreadyLoaded(url)){
				// If another document loaded
				LoadDocumentQuestionDialog lwp = new LoadDocumentQuestionDialog(this, "Do you want to load the new page in the current tab?", url);
				addDialogGlassPanel(lwp);
			} else {
				// Reload document
				ReloadDocumentQuestionDialog lwp = new ReloadDocumentQuestionDialog(this, "Do you want to reload the page in the current tab?", url);
				addDialogGlassPanel(lwp);
			}
		}
	}
	
	
	@Override
	public void loadContent(String url) {
		LoadingDocumentMessageDialog ldmd = new LoadingDocumentMessageDialog(this, "Loading...", url);
		addDialogGlassPanel(ldmd);
		reinitEnvironment();
		
		if (!GWT.isScript() || url.indexOf("tests/")>=0) {
			if(url.endsWith("tests/OMIM253300.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://omim.org/entry/253300");
			} else if(url.endsWith("tests/OMIM600354.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://omim.org/entry/600354");
			} else if(url.endsWith("tests/gene6606.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://www.ncbi.nlm.nih.gov/gene/6606");
			} else if(url.endsWith("tests/PM10679938.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://www.ncbi.nlm.nih.gov/pubmed/10679938");
			} else if(url.endsWith("tests/PMC2759694.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2759694/");
			} else if(url.endsWith("tests/PMC2759694_v062012.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2759694/");
			} else if(url.endsWith("tests/PM17561409.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://www.ncbi.nlm.nih.gov/pubmed/17561409");
			} else if(url.endsWith("tests/PMC2700002.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2700002/");
			} else if(url.endsWith("tests/PMC2700002_v062012.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2700002/");
			} else if(url.endsWith("tests/PMC2799499.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2799499/");
			} else if(url.endsWith("tests/PMC2799499_v062012.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2799499/");
			} else if(url.endsWith("tests/PMC2714656.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2714656/");
			} else if(url.endsWith("tests/PMC2714656_v062012.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2714656/");
			} else if(url.endsWith("tests/ImagesTest.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://www.foaffy.org/gallery2/main.php");
			} else if(url.endsWith("tests/NCT000001549.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://clinicaltrials.gov/ct2/show/NCT000001549");
			} else if(url.endsWith("tests/NCT00368199.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://clinicaltrials.gov/ct2/show/NCT00368199");
			} else if(url.endsWith("tests/NCT01136213.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://clinicaltrials.gov/ct2/show/NCT01136213");
			} else if(url.endsWith("tests/PMC3308009_v082012.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3308009/");
            } else if(url.indexOf("03bec9ad-cd22-44d3-9b82-2796157de1d1")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("040d010a-b1b2-4db7-905d-8aa7f1bab0cd")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("0679dd4c-fece-4c6d-b273-2c62237e8973")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("0792169d-c6f9-4af0-93ae-b75d710c47a9")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("0804aaf5-de2d-4e38-915d-4b3c0da379d4")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("0ab0c9f8-3eee-4e0f-9f3f-c1e16aaffe25")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("0ae10bc4-6b65-402f-9db5-2d7753054922")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("0d706611-abab-42f1-ae8a-2097eb1be38d")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("0dc9682c-a4ae-4786-8dd9-31e33ce9861b")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("0e7f054c-7a27-4192-bd1c-6115d8be858f")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("0f81f505-a962-414e-8612-c3ef3b159e9a")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("0fac73b4-3bfe-40bc-b0b7-ac0867583e08")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("1086a7b4-b89b-4bee-8120-5f752626c046")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("11544DEA-2F5E-1536-584A-5D1F37A99770")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("128846a7-18c6-4bc4-b3a0-9aeed0e8b986")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("1431ca79-5995-46c2-a16f-882ed7bc43ca")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("1553312b-ed76-421c-a055-2579bdcf366c")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("15904472-4c32-4224-95d3-eb131a7ff9c8")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("16348d76-055f-4a7a-9de9-00144c9ee339")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("16bda7b5-a5a5-4478-9df3-beb396bd677f")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("17f85d17-ab71-4f5b-9fe3-0b8c822f69ff")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("189b7b6a-9717-44eb-b87b-fd3e985b2268")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("194218c4-3f18-4b6b-a0d9-acc8c862d4b7")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("199a6ab3-9b00-49fc-90d4-644d58141235")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("1f641c0d-ccca-44da-85dd-3d88d1efffa7")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("211ef2da-2868-4a77-8055-1cb2cd78e24b")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("217e52b5-3a7b-4bbe-af3c-b18de7d426c3")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("232c9769-17c7-4b7d-8e91-742f1b048d61")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("23c702e2-3abc-4d7d-b88f-a089e2179f42")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("2a51b0de-47d6-455e-a94c-d2c737b04ff7")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("2b5e5c5d-852a-413b-825c-8491d4539e67")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("309de576-c318-404a-bc15-660c2b1876fb")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("33c772f8-08f0-4f99-8fbd-4de48ad82d89")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("33f60b40-3fca-11de-8f56-0002a5d5c51b")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("38eea320-7e0c-485a-bc30-98c3c45e2763")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("3904f8dd-1aef-3490-e48f-bd55f32ed67f")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("3af8cfe6-1c0d-4b57-ae31-05cffa64132e")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("3ccada47-7eb9-285d-5059-06afb4466114")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("3d021d9b-c0b8-41a3-a99b-37f50d2a20ae")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("3d35df36-1a19-4559-91fe-252b7082295b")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("407a372d-4448-4d53-afd4-eb0e74bbc336")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("4216e0ce-e3c4-4cd7-9321-34e1a9e3d867")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("42dfffb1-d0f3-42d5-944c-64f092fc436e")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("43452bf8-76e7-47a9-a5d8-41fe84d061f0")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("45806bf7-893a-4dd6-b9ab-c4dc6ce53946")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("46f30ac5-c96b-429e-976d-8c5ee1c0761b")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("47137273-b9c1-4cb4-99ca-2dab9690b560")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("4764f37b-c9e6-4ede-bcc2-8a03b7c521df")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("47c4897c-ee95-4eaf-adbc-84130d021d2d")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("488e765a-7f2d-4323-8a5c-114a46862e8f")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("49212741-389f-4112-94a9-d14ea2115825")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("4c8a0544-5e03-47e3-9236-c9646a841e9b")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("4e5c06f1-f279-4f2f-b10d-0f70005a27e6")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("4e9b122b-eddf-42d3-aeae-bee38badb2d5")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("4fb02ad7-6e93-4aff-9aca-5162559b99a5")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("52fea941-0b47-41c1-b00d-f88150e8ab93")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("57bccb29-1c47-4c64-ab6a-77960a91cc20")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("595838d9-95ee-4842-981a-87a6eff8423e")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("5bd620d9-c1b6-4d62-bfc8-5dfa9f74b558")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("5cb9d285-1803-4a99-946a-d0b239b32df6")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("5d103551-978f-472a-9c62-51e6e4dea068")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("5e29f133-270c-4e5a-8493-e038c163a891")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("5fa97bf5-28a2-48f1-8955-f56012d296be")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("5fe9c118-c44b-48d7-a142-9668ae3df0c6")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("6093952a-5248-45cb-ad17-33716a411146")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("6289f7b2-84ac-4282-86be-990d7beec121")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("63319b01-cad6-4d0a-c39b-938fa951a808")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("634ec8e3-6d83-4cb2-90a3-fc9c973b06bf")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("63e54b62-a30d-4a56-9215-75e7a0b0bf02")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("69abd0ff-b200-4d1a-b14a-80e980a8e781")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("6a3b10fc-4b2a-45e3-16a1-ef79187a6dfb")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("6bc0214b-b6f9-463d-a689-a93e59512b60")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("6c76e98d-b8c3-441f-bac5-a9de6dc8f14f")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("6ca01de3-a0f9-4cfb-b36a-723c7c42e2a4")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("6e6f6c6d-4826-4817-90ff-5c3f4fbe8909")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("738947e9-b8ae-4592-8c10-5e5bdcd451b9")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("73eb9256-dcda-4eb1-8a6e-700522146f2e")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("7500c65e-9680-4719-b426-90a7162352c9")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("76ea60c9-62fa-4972-8006-635b0d766724")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("78c329f9-f0c9-486d-9e54-0123699fb9e1")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("7c9184c0-7a2e-11df-8c8d-0002a5d5c51b")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("7ee3d3d2-85d1-4018-8e70-5ed8a64ae1f0")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("80f3b79c-a399-45b3-b755-8535457719af")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("827d60e8-7e07-41b7-c28b-49ef1c4a5a41")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("82b77d74-085f-45ac-a7dd-1f5c038bf406")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("83d7a440-e904-4e36-afb5-cb02b1c919f7")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("8613764c-5c70-43cb-bad6-3859d1095eb1")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("87315917-3e54-44e6-bb64-2f722501c11b")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("8750bc89-340d-4abf-b0b8-b83012ab7cae")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("88cd0b26-6575-4132-9749-0f038d8663c7")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("8bb1bc4a-a019-49c8-af81-be899822428f")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("8bc6397e-4bd8-4d37-a007-a327e4da34d9")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("8cf6e5be-7e69-4cfd-9174-c5333d641078")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("8ebda73d-82a3-4b17-8a81-a1f258003d32")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("91e9be98-8d98-43c3-a1c0-0249b65d2744")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("93ddb778-854f-4486-adca-4c5f9071a741")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("9c4bedb4-2d59-4fcd-aad7-fce988cd96d8")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("9ce0e304-583e-4850-ba40-c3534cf1b068")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("9d8b08a9-02c8-4b02-9e0e-a24b90d7a7e0")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("A5359176-9E01-4D51-A48C-1685F4C16BCA")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("A6EB861C-085E-442A-8E71-722587493D5D")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("A719EC72-771F-49C9-B93D-9D5F585176A4")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("AA249ECD-AFD3-4800-B196-D4F7B8C8FC6E")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("a0c8c0ac-863c-4385-94d2-f6691bee66d9")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("a1cb24ae-9b12-4c9b-a4bd-27d49b64d974")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("a2505576-801a-4dc5-9ac6-423664406481")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("a2a74cd9-115b-dbb1-777e-424e173c8045")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("a6fe8e00-a1ee-4dbb-bac7-46ac2af9aee9")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("a732b303-9bbd-42ba-a7c5-61c21629ba93")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("a94a9a2b-337b-4c13-8622-fc392194dc21")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("aa0690af-2cb2-48fb-8f12-d81f13d83378")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("ab047628-67d0-4a64-8d77-36b054969b44")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("abf3db95-913b-432a-984f-caaef72088b8")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("ac3e3964-90c0-44dd-800c-ec9606629572")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("ac768bab-8afa-4446-bc7f-caeeffec0cda")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("ae879ebe-b620-4829-b6f8-74b58da1c771")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("b19238be-9962-478c-bd29-0f5f8d0abef3")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("b1d149db-ad43-4f3f-aef1-fb0395ba4191")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("b23021d4-c00a-e0ef-eb93-1dc824ae56ca")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("b2a8e240-5e93-4a58-9ba2-688a87a5e20c")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("b47a0a1f-800c-46bd-905d-0b10858aa5f1")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("b73bf908-7979-4955-af20-0414388920da")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("b7c4c175-d3f9-4ed8-abb8-cfb33b9dc2b3")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("b7d605cf-bf84-461b-939b-1f773a4cba65")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("b8881a81-75d7-43e8-825f-37c352c146dc")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("b8ad0b63-76cf-4fa8-9326-d4b750f02374")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("ba02a95d-d82e-4a13-90b4-a219abc0249a")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("bb05420c-fd24-4672-9f62-fdd313819287")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("bcf7e761-786b-4ac8-aecd-ea1c745880b1")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("be716692-6b4f-47b6-83d1-01665861626b")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("bed1e444-f0a7-49fc-952f-fa4c2469cfc9")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("c3248d43-3dd6-4ca9-afca-528c7abe0fce")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("c3b5b8b0-bc5c-4ce9-bbdc-febba60c2658")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("c43da258-73bb-4ca5-bfe8-1c8a0500f91d")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("c45f5286-a52b-43e5-8a6f-d0312e7da0c8")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("c55cf30f-6d98-4212-86bd-f7f9f08bbce3")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("c80a98e4-ded4-487d-9281-55865dd5c25c")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("c83616c9-c222-4e3e-91bd-c7839406bb2a")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("c856d72f-4a1a-4f75-ae54-c4ac83ddcfa1")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("c95f0b0d-77ef-e753-be04-2228139df2d3")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("c98eb213-9c80-4698-9710-a9855059b8bb")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("caa123c0-462e-463f-a831-671783febfa9")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("cb684ad9-0b72-406f-8a07-a419254ccd36")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("cd53d182-3d90-4a67-81d0-c61353aff4f6")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("cf066b7a-032a-416c-8d40-15ba581423e3")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("d0ef6df3-67e5-47b1-bcf7-f67aa030caca")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("d62d754e-2310-4912-b022-603ae925710d")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("d8275c2d-f42d-4f32-aa9f-03a202c8e99f")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("de03bd69-2dca-459c-93b4-541fd3e9571c")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("dec51fab-3784-deb7-752f-2d4d5692a20f")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("dfda4177-3c9d-44ab-3ca6-e4db8e9fc603")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("e007e7c6-7d5d-4d5c-9a6c-c82d9e9cfaf7")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("e082a024-7850-400b-a5c2-2a140612562a")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("e0fa4bca-f245-4d92-ae29-b0c630a315c2")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("e0fc8605-1d64-41ab-af7e-bcf66a57c1fd")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("e16c26ad-7bc2-d155-3a5d-da83ad6492c8")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("e480e538-4ea7-47e6-9e5d-dc0f8179e06c")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("e5073974-ff27-4620-8b4b-79729a957b11")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("e518dfc6-7e93-4fee-a66c-51e1ab71c056")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("ea79f802-e9ac-480b-91e6-1e3900b11803")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("ecb298ce-4817-4530-9aee-cf5f3041b836")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("ed0e4f33-cf21-4fe3-918d-1d5b3a23eee4")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("ee15d911-73c7-4bc6-96f2-089a8aa64e18")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("ee15d911-73c7-4bc6-96f2-089a8aa64e18~")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("ee944d28-f596-4163-a502-e779c0d622bc")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("f0b31daa-0792-43d5-af08-8b6a864dc90a")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("f1139e7f-2db9-4961-9da3-9b7f2088736f")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("f2c7e54b-ed7c-4d8f-a86d-5099a8b52f28")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("f4853677-1622-4037-688b-fdf533a11d96")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("f7b3f443-e83d-4bf2-0e96-023448fed9a8")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("f7d013b1-5ee9-4126-8297-9efd9a5a8344")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("f8f80fca-32b7-4fba-b0ab-e87530bc36b6")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("f9af557d-37bf-4078-888e-37c086dfc6e9")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("fa3ed180-298a-4f9d-9d05-15182d7218bf")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("fb8ef96b-ed37-4439-9294-834ef2e2a7c6")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("fd3c2497-ab34-4e19-a7c8-e599025435c8")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("fd9729c3-545f-4d34-9bc7-72b61e028fc4")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else if(url.indexOf("ff62bcaa-a200-4ebf-a9ca-1ab2c254751c")>=0) {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url, url.replace("tests/SPL-annotation/section-pages", "http://dbmi-icode-01.dbmi.pitt.edu:2020/SPL-annotation/section-pages/"));
            } else {
            	getContentPanel().getAnnotationFrameWrapper().setUrl(url);
            }
		} else {
			if(isLocalResources()) {
		        getLogger().info(this, "Attempting to open a local resoruce");
		        getContentPanel().getAnnotationFrameWrapper().setUrl(url, url);
		    } else {
		        getLogger().info(this, "Attempting to open a remote resoruce");
    			String PROXY = "http://" +ApplicationUtils.getHostname(GWT.getHostPageBaseURL()) + "/proxy/";
    			getContentPanel().getAnnotationFrameWrapper().setUrl(PROXY + url, url);
		    }
		}
		
//		
	}
	
	/**
	 * This is used to calculate the duration of the different stages of the pipeline
	 * triggered by the document loaded completion
	 */
	private long documentPipelineTimer;

	@Override
	public void notifyDocumentLoadedStageOne() { // Document loaded in the browser
		try {
			documentPipelineTimer = System.currentTimeMillis();
			this.getLogger().debug(this, "AFTER DOCUMENT", "Executing notifyDocumentLoadedStageOne()");
			
			contentLoading = false;
			domeoToolbarPanel.attachGroup(DomeoToolbarPanel.DOCUMENT_COMMANDS_GROUP);
			
			// Enables the right side panels
			getSidePanelsFacade().setActive(true);
			
			// Resource metadata extraction
			((ProgressMessagePanel)((DialogGlassPanel)_dialogPanel).getPanel()).setMessage("Resource Metadata extractor");
			this.getLogger().debug(this.getClass().getName(), "Resource Metadata extractor");
			
			contentPanel.getAnnotationFrameWrapper().retrieveDocumentTitle();
			contentPanel.getAnnotationFrameWrapper().retrieveDocumentMetadata();
			
			MOnlineResource resource = new MOnlineResource();
			resource.setUrl(contentPanel.getAnnotationFrameWrapper().getUrl());
			resource.setLabel(contentPanel.getAnnotationFrameWrapper().getDocumentTitle());
			
			// Persistence
			((ProgressMessagePanel)((DialogGlassPanel)_dialogPanel).getPanel()).setMessage("Persistence manager");
			this.getLogger().debug(this.getClass().getName(), "Persistence manager setup");
			_persistenceManager.setResourceLoaded(true);
			_persistenceManager.setCurrentResource(resource); // Overridden by a more appropriate one
			_persistenceManager.logStatus();
					
			 notifyDocumentLoadedStageTwo();
		} catch (Exception e) {
			this.getLogger().exception(this, "::notifyDocumentLoaded "+e.getMessage());
		}
	}
	
	@Override
	public void notifyNotProxiedDocumentLoaded() {
		this.getLogger().warn(PersistenceManager.class.getName(),"Document loaded " + contentPanel.getAnnotationFrameWrapper().getUrl());
		this.getLogger().warn(this.getClass().getName(), "Impossible to access the document content, did you forget to go through the proxy?");
		_dialogPanel.hide();
	}
	
	public void notifyDocumentLoadedStageTwo() {
		
		this.getLogger().debug(this, "AFTER DOCUMENT", "Completed Execution of notifyDocumentLoadedStageOne() in " + (System.currentTimeMillis()-documentPipelineTimer)+ "ms");
		
//		documentPipelineTimer = System.currentTimeMillis();
//		this.getLogger().debug(this, "AFTER DOCUMENT", "Executing startBibliographyRetrieval()");
//
//		//startBibliographyRetrieval();
//		notifyEndBibliographyRetrieval(0);
		
		documentPipelineTimer = System.currentTimeMillis();
		this.getLogger().debug(this, "AFTER DOCUMENT", "Executing startExtraction()");
		
		// TODO call the extraction with the parameter to exclude some stages?
		// TODO or maybe create a stage in the pipe for retrieving the existing
		startExtraction();
	}
	
//	public void startBibliographyRetrieval() {
//		this.getPersistenceManager().retrieveExistingBibliographySet(this);
//	}
	
//	public void setExistingBibliographySetList(JsArray responseOnSets, boolean isVirtual) {
//		if(responseOnSets.length()>0) notifyEndBibliographyRetrieval(1);
//		else notifyEndBibliographyRetrieval(0);
//	}
	
//	public void notifyEndBibliographyRetrieval(int code) {
//		
//		this.getLogger().debug(this, "AFTER DOCUMENT", "Completed Execution of startBibliographyRetrieval() in " + (System.currentTimeMillis()-documentPipelineTimer)+ "ms");
//		
//		documentPipelineTimer = System.currentTimeMillis();
//		this.getLogger().debug(this, "AFTER DOCUMENT", "Executing startExtraction()");
//		
//		// TODO call the extraction with the parameter to exclude some stages?
//		// TODO or maybe create a stage in the pipe for retrieving the existing
//		startExtraction();
//		//else endExtraction();
//		//endExtraction();
//	}
	
	public void startExtraction() {
		// Extractor 
		((ProgressMessagePanel)((DialogGlassPanel)_dialogPanel).getPanel()).setMessage("Starting extraction pipeline");
		extractorsManager.setUpExtractor(getContentPanel().getAnnotationFrameWrapper().getUrl());		
		extractorsManager.parametrizeExtractorAndProcess();
	}

	public void notifyEndExtraction() {
		this.logger.info(this, "Document extraction completed");
		endExtraction();
	}
	
	public void endExtraction() {
		
		this.getLogger().debug(this, "AFTER DOCUMENT", "Completed Document Pipeline Execution in " + (System.currentTimeMillis()-documentPipelineTimer)+ "ms");
		
		documentPipelineTimer = System.currentTimeMillis();
		this.getLogger().debug(this, "AFTER DOCUMENT", "Executing endExtraction()");
		
		// Document handlers registration
		//((ProgressMessagePanel)((DialogGlassPanel)_dialogPanel).getPanel()).setMessage("Document handlers");
		this.getLogger().debug(this.getClass().getName(), "Document handlers setup");
		contentPanel.getAnnotationFrameWrapper().registerHighlightHandlers();
		
		//((ProgressMessagePanel)((DialogGlassPanel)_dialogPanel).getPanel()).setMessage("CSS injection");
		this.getLogger().debug(this.getClass().getName(), "CSS injection");
		IFrameElement iframe = IFrameElement.as(contentPanel.getAnnotationFrameWrapper().getFrame().getElement());
		final Document frameDocument = iframe.getContentDocument();
		// Injecting the stylesheet in the loaded document
		if(frameDocument!=null) HtmlUtils.addCssStylesheet(frameDocument, getCssManager().createStylesheet());
		
		//((ProgressMessagePanel)((DialogGlassPanel)_dialogPanel).getPanel()).setMessage("Refresh components");
		refreshAllComponents();
		
		// Display cached images
		Object w = this.getResourcePanelsManager().getResourcePanel(
				((IDomeo)this).getPersistenceManager().getCurrentResource().getClass().getName());
		if(w instanceof ICachedImages) {
			((ICachedImages)w).createVisualization();
		}
		
		this.getLogger().debug(this, "AFTER DOCUMENT", "Completed Execution of endExtraction() in " + (System.currentTimeMillis()-documentPipelineTimer)+ "ms");
		this.getLogger().debug(this, "AFTER DOCUMENT", "Document url: " + ApplicationUtils.getUrlParameter("url"));
		this.getLogger().debug(this, "AFTER DOCUMENT", "Annotation set id: " + ApplicationUtils.getUrlParameter("setId"));

		if(!ApplicationUtils.getUrlParameter("url").isEmpty() && ApplicationUtils.getUrlParameter("url").trim().length()>13 && !ApplicationUtils.getUrlParameter("lineage").isEmpty()) {
			Window.alert("Attempting retrieving existing annotation with lineageId " + ApplicationUtils.getUrlParameter("lineage"));
			Window.alert("Feature not yet implemented");
			((ProgressMessagePanel)((DialogGlassPanel)_dialogPanel).getPanel()).setMessage("Attempting retrieving existing annotation");
			_dialogPanel.hide();
		} else if(!ApplicationUtils.getUrlParameter("url").isEmpty() && ApplicationUtils.getUrlParameter("url").trim().length()>13 && !ApplicationUtils.getUrlParameter("setId").isEmpty()) {
			List<String> uuids = new ArrayList<String>();
			uuids.add(ApplicationUtils.decodeURIComponent(ApplicationUtils.getUrlParameter("setId")));
			((DialogGlassPanel)_dialogPanel).hide();
			this.getProgressPanelContainer().setProgressMessage("Retrieving requested annotation");
			((DialogGlassPanel)_dialogPanel).hideSoon();
			this.getAnnotationPersistenceManager().retrieveExistingAnnotationSets(uuids, (IRetrieveExistingAnnotationSetHandler)this);
			if(!isLocalResources() && !isHostedMode()) ApplicationUtils.updateUrl(ApplicationUtils.encodeUrlComponent(ApplicationUtils.getUrlParameter("url")));
		} else if(!ApplicationUtils.getUrlParameter("url").isEmpty() && ApplicationUtils.getUrlParameter("url").trim().length()>13 && !ApplicationUtils.getUrlParameter("setIds").isEmpty()) {
			List<String> uuids = new ArrayList<String>();
			String[] st = ApplicationUtils.decodeURIComponent(ApplicationUtils.getUrlParameter("setIds")).split(",");
			for(int i = 0; i<st.length; i++) {
				uuids.add(st[i]);
				this.getLogger().debug(this, "Queuing annotation set with id: " + st[i]);
			}
			
			((DialogGlassPanel)_dialogPanel).hide();
			this.getProgressPanelContainer().setProgressMessage("Retrieving requested annotation");
			((DialogGlassPanel)_dialogPanel).hideSoon();
			this.getAnnotationPersistenceManager().retrieveExistingAnnotationSets(uuids, (IRetrieveExistingAnnotationSetHandler)this);
			if(!isLocalResources() && !isHostedMode()) ApplicationUtils.updateUrl(ApplicationUtils.encodeUrlComponent(ApplicationUtils.getUrlParameter("url")));
		} else {
			if(!isLocalResources() && !isHostedMode()) ApplicationUtils.updateUrl(ApplicationUtils.encodeUrlComponent(this.getPersistenceManager().getCurrentResourceUrl()));
			checkForExistingAnnotationSets();
		}
		
		/*
		if(ApplicationUtils.getDocumentUrl()!=null && ApplicationUtils.getDocumentUrl().trim().length()>13 && ApplicationUtils.getLineageId()!=null && ApplicationUtils.getLineageId().trim().length()>0) {
			Window.alert("Attempting retrieving existing annotation with lineageId " + ApplicationUtils.getLineageId());
			((ProgressMessagePanel)((DialogGlassPanel)_dialogPanel).getPanel()).setMessage("Attempting retrieving existing annotation");
			_dialogPanel.hide();
		} else if (ApplicationUtils.getDocumentUrl()!=null && ApplicationUtils.getDocumentUrl().trim().length()>13 && ApplicationUtils.getAnnotationId()!=null && ApplicationUtils.getAnnotationId().trim().length()>0) {
			List<String> uuids = new ArrayList<String>();
			uuids.add(ApplicationUtils.getAnnotationId());
			((DialogGlassPanel)_dialogPanel).hide();
			this.getProgressPanelContainer().setProgressMessage("Retrieving requested annotation");

			//((ProgressMessagePanel)((DialogGlassPanel)_dialogPanel).getPanel()).setMessage("Retrieving requested annotation");
			((DialogGlassPanel)_dialogPanel).hideSoon();
			this.getAnnotationPersistenceManager().retrieveExistingAnnotationSets(uuids, (IRetrieveExistingAnnotationSetHandler)this);
			//_dialogPanel.hide();
		} else {
			checkForExistingAnnotationSets();
		}
		*/
	}
	
	public void checkForExistingAnnotationSets() {
		documentPipelineTimer = System.currentTimeMillis();
		this.getLogger().debug(this, "AFTER DOCUMENT", "Executing checkForExistingAnnotationSets()");
		this.getDialogPanel().hide();
		this.getPersistenceManager().retrieveExistingAnnotationSetList(this);
		
		//ExistingAnnotationViewerPanel lwp = new ExistingAnnotationViewerPanel(this);
		//new EnhancedGlassPanel(this, lwp, lwp.getTitle(), false, false, false);
		//this.getPersistenceManager().retrieveExistingAnnotationSetList();
	}
	
	public void setExistingAnnotationSetList(JsArray responseOnSets) {
		//_dialogPanel.hide();
		this.getLogger().debug(this, "Completed Execution of checkForExistingAnnotationSets() in " + (System.currentTimeMillis()-documentPipelineTimer)+ "ms");

		if(responseOnSets==null || responseOnSets.length()==0) {
			// TODO message no annotation found
			this.getProgressPanelContainer().setCompletionMessage("No annotation exist for this document");
		} else {
			this.getProgressPanelContainer().hide();
			try {
				ExistingAnnotationViewerPanel lwp = new ExistingAnnotationViewerPanel(this, responseOnSets);
				new EnhancedGlassPanel(this, lwp, lwp.getTitle(), false, false, false);
				//_dialogPanel.hide();
			} catch (Exception e) {
				this.getLogger().exception(this, "Exeption in visualizing existing annotation");
				//_dialogPanel.hide();
			}		
		}
	}

	public void refreshAllComponents() {
		this.getLogger().debug(this.getClass().getName(), "Refresh components");
		componentsManager.refreshAllComponents();
	}
	
	public void refreshAnnotationComponents() {
		this.getLogger().debug(this.getClass().getName(), "Refresh annotation components");
		componentsManager.refreshAnnotationComponents();
	}
	
	public void refreshClipboardComponents() {
		this.getLogger().debug(this.getClass().getName(), "Refresh clipboard components");
		componentsManager.refreshClipboardComponents();
	}
	
	public void refreshResourceComponents() {
		this.getLogger().debug(this.getClass().getName(), "Refresh resource components");
		componentsManager.refreshResourceComponents();
	}
	
	public void reinitEnvironment() {
		this.getLogger().info(this.getClass().getName(), "Re-initializing environment");
		componentsManager.initializeComponents();
		tabsSets.clear();
	}

	@Override
	public boolean isManualAnnotationEnabled() {
		return domeoToolbarPanel.isManualAnnotationSelected();
	}
	@Override
	public boolean isManualClipAnnotationEnabled() {
		return domeoToolbarPanel.isManualMultipleAnnotationSelected();
	}
	@Override
	public boolean isManualHighlightEnabled() {
		return domeoToolbarPanel.isManualHighlightSelected();
	}
	
	public void displayAnnotationOfSet(MAnnotationSet set) {
		AnnotationSetViewerSideTab tab = new AnnotationSetViewerSideTab(this, sidePanelsFacade, set.getLabel().substring(0,8), 58);
		final AnnotationForSetSidePanel sidePanel = new AnnotationForSetSidePanel(this, sidePanelsFacade, tab, new AnnotationSummaryTable(this, this.getContentPanel().getAnnotationFrameWrapper()), set);
		sidePanel.refresh();
		
		componentsManager.addComponent(sidePanel);

		ASideTab selectedTab = sidePanelsFacade.registerSideComponent(tab, sidePanel, (58+16) + "px");
		tabsSets.put(set, selectedTab);
		sidePanelsFacade.toggleTab(selectedTab);
	}
	
	private HashMap<MAnnotationSet, ASideTab> tabsSets = new HashMap<MAnnotationSet, ASideTab>();
	
	public void removeAnnotationSetTab(MAnnotationSet set) {
		this.getLogger().debug(this, set.getLabel());
		Object obj = tabsSets.get(set);
		this.getLogger().debug(this, ""+obj);
		if(obj!=null) {
			sidePanelsFacade.removeSideTab((ASideTab) obj);
		}
	}
	
	// ----------------------------------
	//  TIMERS
	// ----------------------------------
	public void loadingTimeOut(final String message) {
		(new Timer() {
			public void run() {
				if (!_persistenceManager.isResourceLoaded()) {
					//messagePanel.hide();
					//headerPanel.getAddressBarPanel().resetActiveFlag();
					contentPanel.getAnnotationFrameWrapper().setUrl("");
					_persistenceManager.setResourceLoaded(false);
					//showErrorMessage(message);
				}
			}
		}).schedule(35000);
	}
	
	public ContentPanel getContentPanel() {
		return contentPanel;
	}
	
	public ClipboardManager getClipboardManager() {
		return clipboardManager;
	}

	public AnnotationAccessManager getAnnotationAccessManager() {
		return accessManager;
	}
	
	public ContentExtractorsManager getExtractorsManager() {
		return extractorsManager;
	}
	@Override
	public void loadExistingAnnotationSetList(JsArray responseOnSets, int size) {
		if(Domeo.verbose) this.getLogger().debug(this, "Beginning of Domeo.loadExistingAnnotationSetList()");
		if(responseOnSets.length()!=size) {
			this.getProgressPanelContainer().setErrorMessage("Something went terribly wrong while retrieving the annotation." +
					"Only " + responseOnSets.length() + " sets out of " + size + " have been retrieved.");
			this.getLogger().exception(this, "Something went terribly wrong while retrieving the annotation." +
					"Only " + responseOnSets.length() + " sets out of " + size + " have been retrieved.");
			return;
		}
		
		JsonUnmarshallingManager manager = this.getUnmarshaller();
		if(Domeo.verbose) this.getLogger().debug(this, "Beginning of unmarshalling");
		manager.unmarshall(responseOnSets, size);

		if(Domeo.verbose) this.getLogger().debug(this, "Unmarshalling completed");
		//this.getDialogPanel().hide();
		if(this.getProgressPanelContainer()!=null) 
			this.getProgressPanelContainer().setCompletionMessage("Annotation loaded (#sets: " + size + ")!");
		this.refreshAllComponents();
	}
	
	public DomeoToolbarPanel getToolbarPanel() {
		return domeoToolbarPanel;
	}
	
	public JsonUnmarshallingManager getUnmarshaller() {
		return jsonUnmarshallingManager;
	}
	
	public ImagesCache getImagesCache() {
		return imagesCache;
	}
	public final native String getAnnotationType(Object obj) /*-{ return obj['@type']; }-*/;
	public final native String getSelectorType(Object obj) /*-{ return obj['@type']; }-*/;
	public final native String getSelectorTargetUrl(Object obj) /*-{ return obj['ao:annotatesResource']; }-*/;
	public final native String getAnnotationTargetUri(Object obj) /*-{ return obj['ao:annotation']; }-*/;
	public final native JsArray<JsAnnotationTarget> getTargets(Object obj) /*-{ return obj['ao:context']; }-*/;
	public final native JsArray<JsoLinkedDataResource> getSemanticTags(Object obj) /*-{ return obj['oax:hasSemanticTag']; }-*/;
	
	public void jumpToLocation(float percentage) {
	    IFrameElement iframe = IFrameElement.as(contentPanel.getAnnotationFrameWrapper().getFrame().getElement());
        final Document frameDocument = iframe.getContentDocument();
        
	    //this.getContentPanel().getAnnotationFrameWrapper().getFrame().getElement().
        //Window.alert(""+frameDocument.getBody().getStyle().getHeight());
       // Window.alert(""+documentHeight(frameDocument));
        //Window.alert(""+frameDocument.getOwnerDocument().ge
	    //Window.alert(percentage + " - " + DOM.getElementPropertyInt(frameDocument.getBody().getOffsetHeight(), "height"));
	    
	}
	
   public static native int documentHeight(Document frameDocument) /*-{
       return frameDocument.documentElement.scrollHeight;
   }-*/;
	

}
