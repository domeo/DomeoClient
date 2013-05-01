package org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.viewer;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.client.ui.annotation.bricks.IBrickComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ATileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.TileResources;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.selectors.MAnnotationSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;
import org.mindinformatics.gwt.framework.component.ICommentsRefreshableComponent;
import org.mindinformatics.gwt.framework.component.ui.east.ASidePanel;
import org.mindinformatics.gwt.framework.component.ui.east.ASideTab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the standard lens for the annotation type Highlight.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class BCommentThreadBrick extends Composite implements IBrickComponent {

	public static final CommentViewerResources localResources = 
			GWT.create(CommentViewerResources.class);
	
	interface Binder extends UiBinder<Widget, BCommentThreadBrick> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	public static final TileResources tileResources = GWT.create(TileResources.class);
	
	private int replies;
	private int threads;
	private int users;
	
	// By contract 
	private MCommentAnnotation _annotation;
	private Tree _tree;
	private TreeItem _item;
	protected IDomeo _domeo;
	private CommentsViewerPanel _viewer;
	private BCommentThreadBrick _parent;
	
	@UiField VerticalPanel body;
	@UiField HorizontalPanel provenance;
	@UiField SimplePanel showIconPanel;
	@UiField SimplePanel commentIconPanel;
	@UiField FlowPanel content;
	@UiField HTML title;
	@UiField HTML titleProvenance;
	@UiField SimplePanel contentFrame;
	
	@UiField VerticalPanel addCommentSection;
	@UiField TextBox addCommentTitle;
	@UiField TextArea addCommentBody;
	@UiField Button submitButton;
	@UiField Button cancelButton;
	
	public BCommentThreadBrick(IDomeo domeo, CommentsViewerPanel viewer) {
		_domeo = domeo;
		_viewer = viewer;
		
		initWidget(binder.createAndBindUi(this));
		
		tileResources.css().ensureInjected();
		localResources.css().ensureInjected();
		
		hideCommentForm();
	}
	
	public MAnnotation getAnnotation() {
		return _annotation;
	}
	
	public void initializeLens(Tree tree, TreeItem item, BCommentThreadBrick parent, MAnnotation annotation) {
		_tree = tree;
		_item = item;
		_parent = parent;
		_annotation = (MCommentAnnotation) annotation;
		refresh();
	}	
	
	private String getProvenance(MAnnotation annotation) {
		if(annotation.getCreator().getUri().equals(_domeo.getAgentManager().getUserPerson().getUri())) {
			return "Me on " + annotation.getFormattedCreationDate();
		} else return annotation.getCreator().getName() + " on " + annotation.getFormattedCreationDate();
	}
	
	@Override
	public Widget getBrick() {
		return this;
	}
	@Override
	public void initializeBrick(MAnnotation annotation) {
		_annotation = (MCommentAnnotation) annotation;
		refresh();
	}
	@Override
	public void refresh() {
		try {
			createProvenanceBar(provenance, _annotation);
			
			//CommentViewerResources.
			
			
			if(_annotation.getTitle()!=null && _annotation.getTitle().trim().length()>0) {
				Image ic = new Image(Domeo.resources.littleCommentsIcon());
				title.setHTML("<img src='" + ic.getUrl() + "'> <b>" +_annotation.getTitle() + "</b>.");
				titleProvenance.setHTML(_annotation.getText());
				contentFrame.addStyleName(localResources.css().titleFrame());
			} else {
				Image ic = new Image(Domeo.resources.littleCommentIcon());
				title.setVisible(false);		
				titleProvenance.setHTML("<img src='" + ic.getUrl() + "'> "+_annotation.getText());
			}		
			
			provenance.add(new Label(getProvenance(_annotation)));
			
			//final CommentsViewerPanel viewer = _viewer;
			final BCommentThreadBrick _this = this;
			final MAnnotation ann = _annotation;
			submitButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(addCommentBody.getText().trim().length()==0) return; 
					MAnnotationSelector selector = AnnotationFactory.createAnnotationSelector(_domeo.getAgentManager().getUserPerson(), 
							_domeo.getPersistenceManager().getCurrentResource(), ann);
					MCommentAnnotation annotation = AnnotationFactory.createComment(_domeo.getPersistenceManager().getCurrentSet(), _domeo.getAgentManager().getUserPerson(), 
							_domeo.getAgentManager().getSoftware(),
							_domeo.getPersistenceManager().getCurrentResource(), selector, addCommentBody.getText());
					if(addCommentTitle.getValue()!=null && addCommentTitle.getValue().trim().length()>0) {
						annotation.setTitle(addCommentTitle.getValue());
					}
						
					_domeo.getAnnotationPersistenceManager().addAnnotationOfAnnotation(annotation,  ann, 
							_domeo.getAnnotationPersistenceManager().getSetByAnnotationId(ann.getLocalId()));

					ASideTab tab = _domeo.getDiscussionSideTab();
					ASidePanel panel = _domeo.getSidePanelsFacade().getPanelForTab(tab);
					((ICommentsRefreshableComponent)panel).refreshFromRoot();
					_domeo.refreshAnnotationComponents();
					
					BCommentThreadBrick c = new BCommentThreadBrick(_domeo, _viewer);
					TreeItem i = new TreeItem(c.getBrick());
					i.setState(true);
					c.initializeLens(_tree, i, _this, annotation);
					
					hideCommentForm();
					_item.addItem(i);
					_tree.setSelectedItem(i); 
					_tree.ensureSelectedItemVisible();
					
					
					
					if(annotation.getTitle()!=null && annotation.getTitle().length()>0) { 
						_viewer.refreshThreadsTree();
						if(_this!=null) {
							_this.incrementThreads();
							_this.refreshCounters();
						}
					} else {
						if(_this!=null) {
							_this.incrementReplies();
							_this.refreshCounters();
						}
					}
				}
			});
			
			cancelButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					hideCommentForm();
				}
			});
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}
	
	protected void showCommentForm() {
		addCommentSection.setVisible(true);
		addCommentTitle.setVisible(false);
	}
	
	protected void showCommentBranchForm() {
		addCommentSection.setVisible(true);
		addCommentTitle.setVisible(true);
	}
	
	protected void hideCommentForm() {
		addCommentSection.setVisible(false);
		addCommentTitle.setVisible(false);
		addCommentTitle.setText("");
		addCommentBody.setText("");
	}

	public void createProvenanceBar(HorizontalPanel provenance, final MAnnotation annotation) {
		int step = 0;
		try {
			Resources resource = Domeo.resources;
			
			Image showIcon = new Image(resource.splitCommentIcon());
			showIcon.setTitle("Create a new sub topic");
			showIcon.setStyleName(ATileComponent.tileResources.css().button());
			showIcon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					showCommentBranchForm();
				}
			});
			step=1;		
			showIconPanel.add(showIcon);
			
			addCommentIcon();
		} catch (Exception e) {
			_domeo.getLogger().exception(this, "Provenance bar generation exception @" + step + " " + e.getMessage());
		}
	}
	
	private void addCommentIcon() {
		commentIconPanel.clear();
		if(getReplies()==0) {
			Resources resource = Domeo.resources;
			Image showIcon = new Image(resource.addCommentIcon());
			showIcon.setTitle("Add comment");
			showIcon.setStyleName(ATileComponent.tileResources.css().button());
			showIcon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					showCommentForm();
				}
			});
			commentIconPanel.add(showIcon);
		} 
	}
	
	public void refreshCounters() {
		addCommentIcon();
		
		/*
		int counter = 0;
		StringBuffer sb = new StringBuffer();
		if(getReplies()>0) {
			sb.append("Replies: " + getReplies());
			counter++;
		}
		if(getThreads()>0) {
			if(counter>0) sb.append(", "); 
			sb.append("Threads: " + getThreads());
			counter++;
		}
		if(counter>0) sb.append(", "); 
		sb.append("Users: n/a");
		
		provenance.clear();
		provenance.add(new Label(sb.toString()));
		*/
	}

	public void incrementReplies() {
		replies++;
	}
	
	public int getReplies() {
		return replies;
	}

	public void setReplies(int replies) {
		this.replies = replies;
	}
	
	public void incrementThreads() {
		threads++;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}
	
	public void incrementUsers() {
		users++;
	}

	public int getUsers() {
		return users;
	}

	public void setUsers(int users) {
		this.users = users;
	}
}
