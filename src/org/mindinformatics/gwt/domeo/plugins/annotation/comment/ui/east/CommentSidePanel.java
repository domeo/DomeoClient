package org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.east;

import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.viewer.CommentsViewerPanel;
import org.mindinformatics.gwt.framework.component.ICommentsRefreshableComponent;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.ui.east.ASidePanel;
import org.mindinformatics.gwt.framework.component.ui.east.ASideTab;
import org.mindinformatics.gwt.framework.component.ui.east.SidePanelsFacade;
import org.mindinformatics.gwt.framework.component.ui.glass.EnhancedGlassPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CommentSidePanel extends ASidePanel 
	implements IInitializableComponent, IRefreshableComponent,
	ICommentsRefreshableComponent {

	interface Binder extends UiBinder<Widget, CommentSidePanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	public int status = 0;
	public static final int GENERAL_THREADS = 1;
	public static final int NEW_GENERAL_THREAD = 2;
	public static final int LOCAL_THREADS = 3;
	
	@UiField VerticalPanel body;
	
	private CommentSidePanelTopbar topbar;
	private CommentSummaryTable table;

	public CommentSidePanel(IDomeo domeo, SidePanelsFacade facade, ASideTab tab) {
		super(domeo, facade, tab);
		
		initWidget(binder.createAndBindUi(this));
		
		topbar = new CommentSidePanelTopbar(domeo, this);
		body.add(topbar);
		body.setCellHeight(topbar, "18px");
		
		table = new CommentSummaryTable(domeo, this, domeo.getContentPanel().getAnnotationFrameWrapper());
		table.init();
		
		body.setHeight("100%");
		body.add(table);
	}
	
	@Override
	public void init() {
		_application.getLogger().debug(this, "Initializing...");
		topbar.init();
		table.init();
	}
	
	@Override
	public void refresh() {
		_application.getLogger().debug(this, "Refreshing...");
		try {
			topbar.refresh();
			table.refresh();
		} catch (Exception e) {
			_application.getLogger().exception(this, "Refreshing failed " + e.getMessage());
		}
	}
	
	public void updateStatistics(List<MAnnotation> annotations, int numberComments, int numberUsers, int threadsCounter) {
		topbar.updateStatistics(annotations, numberComments, numberUsers, threadsCounter);
	}
	
	public void refreshFromRoot() {
		table.refreshFromRoot();
	}
	
	/**
	 * Performs a refresh with the provided list of annotation items.
	 * @param annotations	The annotations to display
	 */
	public void refresh(List<MAnnotation> annotations) {
		table.refreshPanel(annotations);
	}
	
	public void displayThread(Long id) {
		table.displayThread(id);
	}
	
	public void listGeneralThreads() {
		table.listGeneralThreads();
		topbar.updateStatistics(((IDomeo)_application).getAnnotationPersistenceManager().getAnnotationOfTargetResourceSize());
	}
	
	public void listLocalThreads() {
		table.listLocalThreads();
	}
	
	public void createNewGeneralThread() {
		table.createNewGeneralThread();
	}
}
