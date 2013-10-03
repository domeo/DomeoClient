package org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.east;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ATileComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.viewer.CommentsViewerPanel;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.ui.glass.EnhancedGlassPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CommentSidePanelTopbar  extends Composite 
	implements IInitializableComponent, IRefreshableComponent {

	interface Binder extends UiBinder<VerticalPanel, CommentSidePanelTopbar> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private IDomeo _domeo;
	private CommentSidePanel _annotationSidePanel;
	
	private List<String> accessFilters = new ArrayList<String>();
	
	@UiField SimplePanel explorePanelIcon;
	@UiField SimplePanel explorePanelLabel;
	@UiField SimplePanel newThreadPanel;
	@UiField SimplePanel seeThreadsPanel;
	@UiField SimplePanel seeLocalizedThreadsPanel;
	@UiField SimplePanel secondLineTopbar;
	
	public CommentSidePanelTopbar(IDomeo domeo, final CommentSidePanel annotationSidePanel) {
		_domeo = domeo;
		_annotationSidePanel = annotationSidePanel;
		
		initWidget(binder.createAndBindUi(this));
		
		Label l = new Label("General threads");
		l.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				annotationSidePanel.listGeneralThreads();
			}
		});
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.add(new Image(Domeo.resources.littleThreadIcon()));
		hp1.add(l);
		seeThreadsPanel.add(hp1);
		seeThreadsPanel.setWidth("110px");
		
		Label l1 = new Label("New general threads");
		l1.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				annotationSidePanel.createNewGeneralThread();
			}
		});
		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.add(new Image(Domeo.resources.littleAddIcon()));
		hp2.add(l1);
		newThreadPanel.add(hp2);
		
//		Label l2 = new Label("Local threads");
//		l2.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				Window.alert("Local threads not implemented");
//			}
//		});
//		HorizontalPanel hp4 = new HorizontalPanel();
//		hp4.add(new Image(Domeo.resources.littleThreadIcon()));
//		hp4.add(l2);
//		seeLocalizedThreadsPanel.add(hp4);
//		seeLocalizedThreadsPanel.setWidth("110px");
		
		secondLineTopbar.add(new Label("No available threads"));
		
		explorePanelIcon.clear();
		//explorePanelIcon.add(new Image(Domeo.resources.littleZoomIcon()));
	}
	
	@Override
	public void init() {
		
	}
	
	@Override
	public void refresh() {
		
	}
	
	public void updateStatistics(final List<MAnnotation> annotations, int numberComments, int numberUsers, int threadsCounter) {
		secondLineTopbar.clear();

		if(numberComments>0 && numberUsers>0) {
			/*
			threadsCounter++;
			Label l = new Label(threadsCounter + ((threadsCounter==1) ? " Thread": " Threads"));
			l.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					CommentsViewerPanel lwp = new CommentsViewerPanel(_domeo, annotations);
					new EnhancedGlassPanel(_domeo, lwp, lwp.getTitle(), false, false, false);
				}
			});
			explorePanelLabel.clear();
			explorePanelLabel.add(l);
			explorePanelLabel.setWidth("60px");
			*/
			
			HorizontalPanel socialPanel = new HorizontalPanel();
			
			Resources resource = Domeo.resources;
			
			//Image commentIcon = new Image(resource.littleCommentsIcon());
			
			String label =  (numberComments==1) ? " comment by": " omments by";
			
			//commentIcon.setTitle("Comment on Item");
		//	commentIcon.setStyleName(ATileComponent.tileResources.css().button());
			//commentIcon.addClickHandler(ActionCommentAnnotation.getClickHandler(_domeo, this, annotation));
			//socialPanel.add(commentIcon);
			socialPanel.add(new Label("Thread with " + numberComments + label));
			
			Image usersIcon = new Image(resource.usersIcon());
			usersIcon.setTitle("Involved users");
			usersIcon.setStyleName(ATileComponent.tileResources.css().button());
			//usersIcon.addClickHandler(ActionCommentAnnotation.getClickHandler(_domeo, this, annotation));
			socialPanel.add(usersIcon);
			socialPanel.setCellWidth(usersIcon, "24px");
			socialPanel.setCellHorizontalAlignment(usersIcon, HasHorizontalAlignment.ALIGN_RIGHT);
			
			String label2 = (numberUsers==1) ? " User": " Users";
			socialPanel.add(new Label(numberUsers + label2));
			secondLineTopbar.add(socialPanel);
		} else {
			secondLineTopbar.add(new HTML("No discussion thread for this document"));
		}
	}
	
	public void updateStatistics(int numberComments) {
		secondLineTopbar.clear();
		
		if(numberComments>0) {
			HorizontalPanel socialPanel = new HorizontalPanel();
			
			Resources resource = Domeo.resources;
			
			Image commentIcon = new Image(resource.littleCommentsIcon());
			commentIcon.setTitle("Comment on Item");
			commentIcon.setStyleName(ATileComponent.tileResources.css().button());
			//commentIcon.addClickHandler(ActionCommentAnnotation.getClickHandler(_domeo, this, annotation));
			socialPanel.add(commentIcon);
			String label = numberComments==1 ? "thread" : "threads";
			socialPanel.add(new Label(numberComments + " general " + label));

			secondLineTopbar.add(socialPanel);
			explorePanelLabel.clear();
		} else {
			secondLineTopbar.add(new HTML("No discussion thread for this document"));
			explorePanelLabel.clear();
		}
	}

}
