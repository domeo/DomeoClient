package org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.viewer;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.client.ui.annotation.bricks.IBrickComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ATileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.TileResources;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the standard lens for the annotation type Highlight.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class BCommentTopicBrick extends Composite implements IBrickComponent {

	interface Binder extends UiBinder<Widget, BCommentTopicBrick> { }
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
	
	@UiField VerticalPanel body;
	@UiField HorizontalPanel provenance;
	@UiField SimplePanel showIconPanel;
	@UiField FlowPanel content;
	@UiField HTML title;
	@UiField HTML titleProvenance;
	
	public BCommentTopicBrick(IDomeo domeo, CommentsViewerPanel viewer) {
		_domeo = domeo;
		_viewer = viewer;
		
		initWidget(binder.createAndBindUi(this));
		
		tileResources.css().ensureInjected();
	}
	
	public MAnnotation getAnnotation() {
		return _annotation;
	}
	
	public void initializeLens(Tree tree, TreeItem item, MAnnotation annotation) {
		_tree = tree;
		_item = item;
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

			Image ic = new Image(Domeo.resources.littleCommentsIcon());
			
			if(_annotation.getTitle()!=null && _annotation.getTitle().trim().length()>0) {
				title.setHTML("<img src='" + ic.getUrl() + "'> <b>" +_annotation.getTitle() + "</b>. ");
				titleProvenance.setHTML(getProvenance(_annotation));
			} else {
				title.setVisible(false);			
			}
			
			provenance.add(new Label("No replies"));
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}

	public void createProvenanceBar(HorizontalPanel provenance, final MAnnotation annotation) {
		int step = 0;
		try {
			Resources resource = Domeo.resources;
			
			Image showIcon = new Image(resource.arrowRightIcon());
			showIcon.setTitle("Show Item in Context");
			showIcon.setStyleName(ATileComponent.tileResources.css().button());
			showIcon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					_viewer.buildDiscussionTree(annotation);
				}
			});
			step=1;		
			showIconPanel.add(showIcon);
		} catch (Exception e) {
			_domeo.getLogger().exception(this, "Provenance bar generation exception @" + step + " " + e.getMessage());
		}
	}
	
	public void refreshCounters() {
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
