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
import org.mindinformatics.gwt.domeo.plugins.annotation.commentaries.linear.model.MLinearCommentAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.commentaries.linear.ui.east.LinearCommentsSidePanel;
import org.mindinformatics.gwt.domeo.plugins.annotation.commentaries.linear.ui.east.LinearCommentsSideTab;
import org.mindinformatics.gwt.domeo.plugins.annotation.commentaries.linear.ui.tile.LinearCommentTileProvider;
import org.mindinformatics.gwt.domeo.plugins.annotation.curation.model.MCurationToken;
import org.mindinformatics.gwt.domeo.plugins.annotation.curation.ui.tile.CurationTileProvider;
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
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.ui.existingsets.ExistingAnnotationViewerPanel2;
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
	
	private ASideTab commentsSideTab;
	public  ASideTab getLinearCommentsSideTab() {
		return commentsSideTab;
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
		pluginsManager.enablePlugin(QualifierPlugin.getInstance(), true);
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
		
		// Curation
		annotationTailsManager.registerAnnotationTile(MCurationToken.class.getName(), 
				new CurationTileProvider(this));
		
		// Antibody
		pluginsManager.registerPlugin(AntibodyPlugin.getInstance(), true);
		pluginsManager.enablePlugin(AntibodyPlugin.getInstance(), true);
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
		pluginsManager.enablePlugin(MicroPublicationsPlugin.getInstance(), false);
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
		annotationTailsManager.registerAnnotationTile(MLinearCommentAnnotation.class.getName(), 
				new LinearCommentTileProvider(this));
		
		// Digesters
		linkedDataDigestersManager.registerLnkedDataDigester(new NifStandardDigester());
		linkedDataDigestersManager.registerLnkedDataDigester(new BioPortalTermsDigester());
		
		sideTabPanel = sidePanelsFacade.getSideTabsContainer();
		final ASideTab annotationsSideTab = new AnnotationSideTab(this, sidePanelsFacade);
		final ASideTab resourceSideTab = new ResourceSideTab(this, sidePanelsFacade);
		final ASideTab annotationSetsSideTab2 = new AnnotationSetSideTab(this, sidePanelsFacade);
		final ASideTab clipboardSideTab = new ClipboardSideTab(this, sidePanelsFacade);
			
		AnnotationSidePanel annotationSidePanel = new AnnotationSidePanel(this, sidePanelsFacade, annotationsSideTab);
		ResourceSidePanel resourceSidePanel = new ResourceSidePanel(this, sidePanelsFacade, resourceSideTab);
		AnnotationSetsSidePanel annotationSetsSidePanel2 = new AnnotationSetsSidePanel(this, sidePanelsFacade, annotationSetsSideTab2);
		ClipboardSidePanel clipboardSidePanel = new ClipboardSidePanel(this, sidePanelsFacade, clipboardSideTab);
		
		sidePanelsFacade.registerSideComponent(resourceSideTab, resourceSidePanel, (ResourceSideTab.HEIGHT+16) + "px");
		sidePanelsFacade.registerSideComponent(annotationsSideTab, annotationSidePanel, (AnnotationSideTab.HEIGHT+16) + "px");
		sidePanelsFacade.registerSideComponent(annotationSetsSideTab2, annotationSetsSidePanel2,  (AnnotationSetSideTab.HEIGHT+16) + "px");
		
		/*
		discussionSideTab = new CommentSideTab(this, sidePanelsFacade);
		//
		if(((BooleanPreference)getPreferences().getPreferenceItem(Domeo.class.getName(), 
				Domeo.PREF_ALLOW_COMMENTING)).getValue()) {
			CommentSidePanel commentSidePanel = new CommentSidePanel(this, sidePanelsFacade, discussionSideTab);
			sidePanelsFacade.registerSideComponent(discussionSideTab, commentSidePanel,  (CommentSideTab.HEIGHT+16) + "px");
		}
		*/
		
		commentsSideTab = new LinearCommentsSideTab(this, sidePanelsFacade);
		if(((BooleanPreference)getPreferences().getPreferenceItem(Domeo.class.getName(), 
				Domeo.PREF_ALLOW_COMMENTING)).getValue()) {
			LinearCommentsSidePanel commentsSidePanel = new LinearCommentsSidePanel(this, sidePanelsFacade, discussionSideTab);
			sidePanelsFacade.registerSideComponent(commentsSideTab, commentsSidePanel,  (LinearCommentsSideTab.HEIGHT+16) + "px");
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
			} else if(url.endsWith("tests/PMC3639628.html")) {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url, "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3639628/");
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
			} else {
				getContentPanel().getAnnotationFrameWrapper().setUrl(url);
			}
		} else {
		    if(isLocalResources()) {
		        getLogger().info(this, "Attempting to open a local resoruce");
		        getContentPanel().getAnnotationFrameWrapper().setUrl(url, url);
		    } else {
		        getLogger().info(this, "Attempting to open a remote resoruce");
		        if(!url.endsWith(".pdf")) {
	    			String PROXY = "http://" +ApplicationUtils.getHostname(GWT.getHostPageBaseURL()) + "/proxy/";
	    			getContentPanel().getAnnotationFrameWrapper().setUrl(PROXY + url, url);
		        } else {
		        	String PREFIX = ApplicationUtils.getUrlBase(ApplicationUtils.getUrlString()) + "web/pdf?pdf=";
		        	String PROXY = "http://" +ApplicationUtils.getHostname(GWT.getHostPageBaseURL()) + "/proxy/";
	    			getContentPanel().getAnnotationFrameWrapper().setUrl(PREFIX+PROXY + url, url);
		        }
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
				ExistingAnnotationViewerPanel2 lwp = new ExistingAnnotationViewerPanel2(this, responseOnSets);
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
