package org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.east;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.selectors.MAnnotationSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTargetSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.lens.DiscussionSummaryListLens;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.lens.LGeneralCommentLens;
import org.mindinformatics.gwt.domeo.plugins.annotation.curation.model.MCurationToken;
import org.mindinformatics.gwt.framework.component.ICommentsRefreshableComponent;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.ui.east.ASidePanel;
import org.mindinformatics.gwt.framework.component.ui.east.ASideTab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CommentSummaryTable extends Composite 
	implements IInitializableComponent, IRefreshableComponent{

	private IDomeo _domeo;
	private IAnnotationEditListener _listener;
	private CommentSidePanel _parentPanel;
	private MAnnotation _root;
	
	private int numberComments;
	
	@UiField ScrollPanel body;
	@UiField VerticalPanel content;
	
	@UiField VerticalPanel topContent;
	@UiField VerticalPanel bottomContent;
	
	interface Binder extends UiBinder<Widget, CommentSummaryTable> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	interface LocalCss extends CssResource {
		String formArea();
		String formBox();
	}
	
	@UiField LocalCss style;
	
	public static final CommentSummaryTableResources localResources = 
		GWT.create(CommentSummaryTableResources.class);
	
	public CommentSummaryTable(IDomeo domeo, CommentSidePanel parentPanel, IAnnotationEditListener listener) {
		_domeo = domeo;
		_listener = listener;
		_parentPanel = parentPanel;
		
		initWidget(binder.createAndBindUi(this));
	}
	
	@Override
	public void init() {		
		topContent.clear();
		bottomContent.clear();
	}
	
	@Override
	public void refresh() {
		//refreshPanel(new ArrayList<MAnnotation>());
		listGeneralThreads();
	}
	
	public void refreshFromRoot() {
		refreshPanel(((IDomeo)_domeo).getAnnotationPersistenceManager().getAnnotationCascade(_root, true));
	}
	
	public void refreshPanel(List<MAnnotation> annotations) {
		
		int threadsCounter = 0;
		
		topContent.clear();
		topContent.setVisible(true);
		bottomContent.clear();
		
		boolean isGeneralComment = false;
		
		long start = System.currentTimeMillis();
		HashSet<String> usersIds = new HashSet<String>();
		try {
			bottomContent.clear();
			if(annotations!=null){  
				if(annotations.size()>0) {
					numberComments = annotations.size();
					_root = annotations.get(0);
					int counter = 0;
					for(MAnnotation ann: annotations) {
						if(ann instanceof MCommentAnnotation) {
							if(((MCommentAnnotation)ann).getTitle()!=null && ((MCommentAnnotation)ann).getTitle().trim().length()>0)
								threadsCounter++;
						}
						ITileComponent c = _domeo.getAnnotationTailsManager().getAnnotationTile(ann.getClass().getName(), _listener);
						if(c==null) {
							VerticalPanel vp = new VerticalPanel();
							vp.add(new Label(ann.getLocalId() + " - " + ann.getClass().getName() + " - " + ann.getY()));
							bottomContent.add(vp);
						} else {
							try {
								if(counter==0) {
									if(ann.getSelector() instanceof MTargetSelector) {
										MAnnotationSet set = _domeo.getAnnotationPersistenceManager().getSetByAnnotationId(ann.getLocalId());
										LGeneralCommentLens lens = new LGeneralCommentLens(_domeo, null, set);
										lens.initializeLens(ann);
										topContent.add(lens);
										isGeneralComment = true;
									} else {			
										c.initializeLens(ann);
										Widget w = c.getTile();
										topContent.add(w);
									}
								} else {
									if(ann instanceof MCommentAnnotation || ann instanceof MCurationToken) { // Remove for simple commenting
										//ITileComponent cc = new TCommentViewerTile(_domeo, _listener);
										//cc.initializeLens(ann);
										//Widget w = cc.getTile();
										c.initializeLens(ann);
										Widget w = c.getTile();
										topContent.add(w);
									} else {
										c.initializeLens(ann);
										Widget w = c.getTile();
										bottomContent.add(w);
									}
								}
							} catch(Exception e) {
								// If something goes wrong just display the default tile
								Window.alert("Exception in refreshPanel(List<MAnnotation> annotations): "+ e.getMessage());
								VerticalPanel vp = new VerticalPanel();
								vp.add(new Label(ann.getLocalId() + " - " + ann.getClass().getName() + " - " + ann.getY()));
								if(counter==0) topContent.add(vp);
								else bottomContent.add(vp);
							}
							counter++;
						}
						if(ann instanceof MCommentAnnotation) usersIds.add(ann.getCreator().getUuid());
					}
					
					final TextBox title = new TextBox();
					title.setWidth("431px");
					title.setVisible(false);
					bottomContent.add(title);
					
					final TextArea ta = new TextArea();
				    ta.setCharacterWidth(68);
				    ta.setVisibleLines(5);
				    ta.setStyleName(style.formBox());
				    bottomContent.add(ta);
				    bottomContent.setStyleName(style.formArea());
				    
				    FlowPanel commands = new FlowPanel();
				    
					Button bu = new Button("Submit");
					final List<MAnnotation> _annotations = annotations;
					bu.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							if(ta.getText().trim().length()==0) return; 
							MAnnotationSelector selector = AnnotationFactory.createAnnotationSelector(_domeo.getAgentManager().getUserPerson(), 
									_domeo.getPersistenceManager().getCurrentResource(), _annotations.get(_annotations.size()-1));
							MCommentAnnotation annotation = AnnotationFactory.createComment(_domeo.getPersistenceManager().getCurrentSet(), _domeo.getAgentManager().getUserPerson(), 
									_domeo.getAgentManager().getSoftware(),
									_domeo.getPersistenceManager().getCurrentResource(), selector, ta.getText());
							if(title.getValue()!=null && title.getValue().trim().length()>0) {
								annotation.setTitle(title.getValue());
							}
								
							_domeo.getAnnotationPersistenceManager().addAnnotationOfAnnotation(annotation,  _annotations.get(_annotations.size()-1), 
									_domeo.getAnnotationPersistenceManager().getSetByAnnotationId(_annotations.get(_annotations.size()-1).getLocalId()));

							ASideTab tab = _domeo.getDiscussionSideTab();
							ASidePanel panel = _domeo.getSidePanelsFacade().getPanelForTab(tab);
							//((ICommentsRefreshableComponent)panel).refresh(_domeo.getAnnotationPersistenceManager().getAnnotationChain(annotation));
							((ICommentsRefreshableComponent)panel).refresh(_domeo.getAnnotationPersistenceManager().getAnnotationCascade(_root, true));
							_domeo.refreshAnnotationComponents();
						}
					});
					commands.add(bu);
					bottomContent.add(commands);					
				} else {
					numberComments = 0;
				}
			}
			if(isGeneralComment)
				_parentPanel.updateStatistics(annotations, annotations.size(), usersIds.size(), threadsCounter);
			else
				_parentPanel.updateStatistics(annotations, annotations.size()-1, usersIds.size(), threadsCounter);
			
			
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
		_domeo.getLogger().debug(this, "Comments summary panel refreshed in (ms):" + (System.currentTimeMillis()-start));
	}
	
	public int getNumberComments() {
		return numberComments;
	}
	
	public void displayThread(Long id) {
		
		HashSet<String> usersIds = new HashSet<String>();
		MCommentAnnotation _comment = ((MCommentAnnotation)_domeo.getAnnotationPersistenceManager().getAnnotationByLocalId(id));
		
		topContent.clear();
		topContent.setVisible(true);
		// display original comment with title
		//topContent.add(new HTML("<b>"+_comment.getTitle()+"</b> " + _comment.getLocalId()));
		
		//topContent.add(new HTML(_comment.getText()));
		//topContent.add(new HTML("By " + _comment.getCreator().getName() + " on " + _comment.getFormattedCreationDate()));
		
		bottomContent.clear();
		// display comments in response
		
		List<MAnnotation> annotations = _domeo.getAnnotationPersistenceManager().getAnnotationCascade(_comment, true);
		
		refreshPanel(annotations);
	}
	
	public void listLocalThreads() {
		
	}
	
	public void listGeneralThreads() {
		if(_domeo.getAnnotationPersistenceManager().getAnnotationOfTargetResourceSize()>0) {
			topContent.clear();
			bottomContent.clear();
			Collection<Long> annLocalIds = _domeo.getAnnotationPersistenceManager().getAnnotationOfTargetResource();
			for(Long id: annLocalIds) {
				DiscussionSummaryListLens lens2 = new DiscussionSummaryListLens(_domeo, _parentPanel, _domeo.getAnnotationPersistenceManager().getSetByAnnotationId(id),
						((MCommentAnnotation)_domeo.getAnnotationPersistenceManager().getAnnotationByLocalId(id)));
				MAnnotationSet set = _domeo.getAnnotationPersistenceManager().getSetByAnnotationId(id);
				_domeo.getComponentsManager().registerObjectLens(set, lens2);
				bottomContent.add(lens2);
				/*
				LCommentThreadSummaryLens lens = new LCommentThreadSummaryLens(_domeo, _parentPanel, ((MCommentAnnotation)_domeo.getAnnotationPersistenceManager().getAnnotationByLocalId(id)));
				bottomContent.add(lens);
				*/
			}
			topContent.setVisible(false);
			/*
			ArrayList<MAnnotationSet> sets = _domeo.getAnnotationPersistenceManager().getAllDiscussionSets();
			for(MAnnotationSet set: sets) {
				
				DiscussionSummaryListLens lens2 = new DiscussionSummaryListLens(_domeo, _parentPanel, set);
				bottomContent.add(lens2);
			}
			*/
 		} else {
 			topContent.clear();
 			topContent.setVisible(true);
 			bottomContent.clear();
 			Label l = new Label("Create the first discussion thread");
 			l.addClickHandler(new ClickHandler() {
 				@Override
 				public void onClick(ClickEvent event) {
 					createNewGeneralThread();
 				}
 			});
 			topContent.add(l);
 			createNewThreadForm(topContent);
 		}
	}
	
	private void createNewThreadForm(VerticalPanel content) {
		
		topContent.setVisible(true);
		
		final TextBox title = new TextBox();
		title.setWidth("431px");
		content.add(title);
		
		final TextArea ta = new TextArea();
	    ta.setCharacterWidth(68);
	    ta.setVisibleLines(5);
		content.add(ta);

		Button bu = new Button("Post message");
		bu.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(ta.getText().trim().length()==0) return; 
				
				MTargetSelector selector = AnnotationFactory.createTargetSelector(_domeo.getAgentManager().getUserPerson(), 
						_domeo.getPersistenceManager().getCurrentResource());
				MCommentAnnotation annotation = AnnotationFactory.createComment(_domeo.getPersistenceManager().getCurrentSet(), _domeo.getAgentManager().getUserPerson(),
						_domeo.getAgentManager().getSoftware(),
						_domeo.getPersistenceManager().getCurrentResource(), selector, ta.getText());
				annotation.setTitle(title.getText());
				
				MAnnotationSet discussionSet = AnnotationFactory.createAnnotationSet(_domeo.getAgentManager().getSoftware(), 
						_domeo.getAgentManager().getUserPerson(), 
						_domeo.getPersistenceManager().getCurrentResource(), title.getText(), ta.getText());
				discussionSet.setType(IDomeoOntology.discussionSet);
				
				_domeo.getAnnotationPersistenceManager().addAnnotationOfTargetResource(annotation, _domeo.getPersistenceManager().getCurrentResource(), discussionSet);

				ASideTab tab = _domeo.getDiscussionSideTab();
				ASidePanel panel = _domeo.getSidePanelsFacade().getPanelForTab(tab);
				
				((ICommentsRefreshableComponent)panel).refresh(_domeo.getAnnotationPersistenceManager().getAnnotationChain(annotation));
				_domeo.refreshAnnotationComponents();
			}
		});
		HorizontalPanel privacy = new HorizontalPanel();
		//privacy.setWidth("150px");
		privacy.setHeight("30px");
		privacy.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		privacy.add(new Image(Domeo.resources.publicLittleIcon()));
		privacy.add(new HTML("Public"));
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(bu);
		hp.add(privacy);
		content.add(hp);
	}
	
	public void createNewGeneralThread() {
		topContent.clear();
		bottomContent.clear();
		createNewThreadForm(topContent);
	}

	public class SortByVerticalPostion implements Comparator<MAnnotation>{
	    public int compare(MAnnotation o1, MAnnotation o2) {
	        return o1.getY() - o2.getY();
	    }
	}
}
