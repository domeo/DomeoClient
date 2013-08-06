package org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.viewer;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.selectors.MTargetSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommentsViewerPanel extends Composite implements IContainerPanel, IContentPanel {

	private static final String TITLE = "Discussion";
	public String getTitle() { return TITLE; }
	
	interface Binder extends UiBinder<HorizontalPanel, CommentsViewerPanel> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	private IDomeo _domeo;
	private IContainerPanel _containerPanel;

	private MAnnotation _root;
	private Tree _threadsTree = new Tree();
	private Tree _discussionTree = new Tree();
	
	@UiField VerticalPanel structure;
	@UiField VerticalPanel container;
	@UiField SimplePanel topContent;
	@UiField ScrollPanel mainContent;
	@UiField VerticalPanel bottomContent;
	
	public CommentsViewerPanel(IDomeo domeo, List<MAnnotation> annotations) {
		_domeo = domeo;
		
		initWidget(binder.createAndBindUi(this)); 
		this.setWidth((Window.getClientWidth() - 140) + "px");
		

		VerticalPanel content = new VerticalPanel();
		for(MAnnotation annotation: annotations) {
			content.add(new Label(annotation.getIndividualUri()));
			content.add(new Label(""+annotation.getAnnotatedBy().size()));
		}

		//topContent.add(new HTML("Thread"));
		
		buildTrees(annotations);
		structure.add(_threadsTree);
		mainContent.add(_discussionTree);
		
		refreshThreadsTree();
	}
	
	private void buildTrees(List<MAnnotation> annotations) {
		_threadsTree.setAnimationEnabled(true);
		_discussionTree.setAnimationEnabled(true);
		_discussionTree.setWidth("100%");
		
		_root = annotations.get(0);

		
		ITileComponent c = _domeo.getAnnotationTailsManager().getAnnotationTile(_root.getClass().getName(), null);
		if(c==null) {
			VerticalPanel vp = new VerticalPanel();
			vp.add(new Label(_root.getLocalId() + " - " + _root.getClass().getName() + " - " +  _root.getY()));
			
			TreeItem i = new TreeItem(vp);
			_discussionTree.addItem(i);
			_discussionTree.setSelectedItem(i); 
			_discussionTree.ensureSelectedItemVisible();
			buildDiscussionTree(annotations.get(0), i, null);
		} else {
			if(_root.getSelector() instanceof MTargetSelector) {	
				BCommentThreadBrick lens1 = new BCommentThreadBrick(_domeo, this);
				TreeItem i2 = new TreeItem(lens1);
				lens1.initializeLens(_threadsTree, i2, null, _root);
				i2.setWidth("100%");

				_discussionTree.addItem(i2);
				_discussionTree.setSelectedItem(i2); 
				_discussionTree.ensureSelectedItemVisible();
				buildDiscussionTree(annotations.get(0), i2, lens1);
			} else {
				try {
					c.initializeLens(_root);
					TreeItem i = new TreeItem(c.getTile());
					_discussionTree.addItem(i);
					_discussionTree.setSelectedItem(i); 
					_discussionTree.ensureSelectedItemVisible();
					buildDiscussionTree(annotations.get(0), i, null);
				} catch(Exception e) {
					// If something goes wrong just display the default tile
					_domeo.getLogger().exception(this, "Refreshing tile: " + e.getMessage());
				}
			}
		}
	}
	
	public void refreshThreadsTree() {
		_threadsTree.clear();
		ITileComponent c = _domeo.getAnnotationTailsManager().getAnnotationTile(_root.getClass().getName(), null);
		if(c!=null) {
			if(_root.getSelector() instanceof MTargetSelector) {
				BCommentTopicBrick lens2 = new BCommentTopicBrick(_domeo, this);
				TreeItem i2 = new TreeItem(lens2);
				lens2.initializeLens(_threadsTree, i2, _root);
				lens2.setWidth("500px");

				_threadsTree.addItem(i2);
				_threadsTree.setSelectedItem(i2); 
				_threadsTree.ensureSelectedItemVisible();
				buildThreadsTree(_root, i2, lens2);
			} else {
				try {
					c.initializeLens(_root);
					TreeItem i2 = new TreeItem(c.getTile());
					_threadsTree.addItem(i2);
					_threadsTree.setSelectedItem(i2); 
					_threadsTree.ensureSelectedItemVisible();
					buildThreadsTree(_root, i2, null);
				} catch(Exception e) {
					// If something goes wrong just display the default tile
					_domeo.getLogger().exception(this, "Refreshing tile: " + e.getMessage());
				}
			}
		}
	}
	
	private void buildThreadsTree(MAnnotation annotation, TreeItem parent, BCommentTopicBrick root) {
		ArrayList<MAnnotation> annBy = annotation.getAnnotatedBy();
		for(MAnnotation ann: annBy) {
			if(ann instanceof MCommentAnnotation) {
				if (((MCommentAnnotation)ann).getTitle()!=null
						&& ((MCommentAnnotation)ann).getTitle().trim().length()>0) {
					root.incrementThreads();
					root.refreshCounters();
					BCommentTopicBrick c = new BCommentTopicBrick(_domeo, this);
					TreeItem i = new TreeItem(c.getBrick());
					c.initializeLens(_threadsTree, i, ann);
					i.setState(true);
					parent.addItem(i);
					_threadsTree.setSelectedItem(i); 
					_threadsTree.ensureSelectedItemVisible();
					buildThreadsTree(ann, i, c);
				} else {
					root.incrementReplies();
					root.refreshCounters();
					buildThreadsTree(ann, parent, root);
				}
			} 
		}
	}
	
	public void buildDiscussionTree(MAnnotation annotation) {
			_discussionTree.clear();
			BCommentThreadBrick c = new BCommentThreadBrick(_domeo, this);
			TreeItem i = new TreeItem(c.getBrick());
			c.initializeLens(_discussionTree, i, null, annotation);
			i.setState(true);
			_discussionTree.addItem(i);
			_discussionTree.setSelectedItem(i); 
			_discussionTree.ensureSelectedItemVisible();
			buildDiscussionTree(annotation, i, null);			
	}
	
	private void buildDiscussionTree(MAnnotation annotation, TreeItem parent, BCommentThreadBrick root) {
		ArrayList<MAnnotation> annBy = annotation.getAnnotatedBy();
		for(MAnnotation ann: annBy) {
			if (((MCommentAnnotation)ann).getTitle()!=null
					&& ((MCommentAnnotation)ann).getTitle().trim().length()>0) {
				if(root!=null) {
					root.incrementThreads();
					root.refreshCounters();
				}
			} else {
				if(root!=null) {
					root.incrementReplies();
					root.refreshCounters();
				}
			}
			BCommentThreadBrick c = new BCommentThreadBrick(_domeo, this);
			TreeItem i = new TreeItem(c.getBrick());
			c.initializeLens(_discussionTree, i, root, ann);
			i.setState(true);
			parent.addItem(i);
			_discussionTree.setSelectedItem(i); 
			_discussionTree.ensureSelectedItemVisible();
			buildDiscussionTree(ann, i, c);			
		}
	}
	
	public void addComment(MAnnotation annotation, Tree _tree, TreeItem _item) {
		BCommentViewerBrick c = new BCommentViewerBrick(_domeo, this);
		TreeItem i = new TreeItem(c.getBrick());
		i.setState(true);
		c.initializeLens(_tree, i, annotation, false);
		
		_item.addItem(i);
		_tree.setSelectedItem(i); 
		_tree.ensureSelectedItemVisible();
	}
	
	@Override
	public void setContainer(IContainerPanel glassPanel) {
		_containerPanel = glassPanel;
	}
	@Override
	public IContainerPanel getContainer() {
		return _containerPanel;
	}
	@Override
	public void hide() {
		_containerPanel.hide();
	}
}

