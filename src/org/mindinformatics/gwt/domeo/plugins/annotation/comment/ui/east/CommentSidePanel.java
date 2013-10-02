package org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.east;

import java.util.Collection;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.lens.CommentsSetLens;
import org.mindinformatics.gwt.framework.component.ICommentsRefreshableComponent;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.ui.east.ASidePanel;
import org.mindinformatics.gwt.framework.component.ui.east.ASideTab;
import org.mindinformatics.gwt.framework.component.ui.east.SidePanelsFacade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
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
	@UiField VerticalPanel toolbar;
	@UiField ScrollPanel setsListScroller;
	@UiField VerticalPanel setsList;
	@UiField VerticalPanel info;
	
	private CommentSidePanelTopbar topbar;
	private CommentSummaryTable table;
	
	private MAnnotationSet _setInfo;
	private CommentsSetLens _lensInfo;

	public CommentSidePanel(IDomeo domeo, SidePanelsFacade facade, ASideTab tab) {
		super(domeo, facade, tab);
		
		initWidget(binder.createAndBindUi(this));
		
		topbar = new CommentSidePanelTopbar(domeo, this);
		toolbar.add(topbar);
		toolbar.setCellHeight(topbar, "18px");
		
		table = new CommentSummaryTable(domeo, this, domeo.getContentPanel().getAnnotationFrameWrapper());
		table.init();
		
		setsList.setCellVerticalAlignment(info, HasVerticalAlignment.ALIGN_TOP);
		setsList.add(table);
		
		Collection<Long> annLocalIds = ((IDomeo)_application).getAnnotationPersistenceManager().getAnnotationOfTargetResource();
		if(annLocalIds.size()>0) {
			setsListScroller.setHeight((annLocalIds.size()*30) + "px");
			body.setCellHeight(setsListScroller, (annLocalIds.size()*30)+"px");
		} else
		setsListScroller.setHeight((Window.getClientHeight()-200)+"px");
		
		body.setHeight("100%");
		
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
//			Collection<Long> annLocalIds = ((IDomeo)_application).getAnnotationPersistenceManager().getAnnotationOfTargetResource();
//			if(annLocalIds.size()>0) {
//				setsListScroller.setHeight((annLocalIds.size()*47) + "px");
//				body.setCellHeight(setsListScroller, (annLocalIds.size()*47)+"px");
//			}
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
		setsListScroller.setHeight((Window.getClientHeight()-200) + "px");
		body.setCellHeight(setsListScroller, (Window.getClientHeight()-200) + "px");
		
		resetAnnotationSetInfo();
	}
	
	public void displayThread(Long id) {
		table.displayThread(id);
		setsListScroller.setHeight((Window.getClientHeight()-200) + "px");
		body.setCellHeight(setsListScroller, (Window.getClientHeight()-200) + "px");
		
		resetAnnotationSetInfo();
	}
	
	public void listGeneralThreads() {
		table.listGeneralThreads();
		topbar.updateStatistics(((IDomeo)_application).getAnnotationPersistenceManager().getAnnotationOfTargetResourceSize());

		Collection<Long> annLocalIds = ((IDomeo)_application).getAnnotationPersistenceManager().getAnnotationOfTargetResource();
		if(annLocalIds.size()>0) {
			setsListScroller.setHeight(Math.min(Window.getClientHeight()-350, (annLocalIds.size()*50 + 16))+ "px");
			body.setCellHeight(setsListScroller, Math.min(Window.getClientHeight()-350, (annLocalIds.size()*50 + 16)) +"px");
		} 
	}
	
	public void listLocalThreads() {
		table.listLocalThreads();
		
	}
	
	public void createNewGeneralThread() {
		table.createNewGeneralThread();
		setsListScroller.setHeight((Window.getClientHeight()-200) + "px");
		body.setCellHeight(setsListScroller, (Window.getClientHeight()-200) + "px");
		
		resetAnnotationSetInfo();
	}
	
	public void displayCommentsSetInfo(MAnnotationSet set) {
		// Unregister previous info lens
		if(_setInfo!=null && _setInfo!=set) {
			_application.getLogger().debug(this, "Unregistering lens for set " + set.getLocalId());
			_application.getComponentsManager().unregisterObjectLens(_setInfo, _lensInfo);
		}
		
		_setInfo = set;
		info.clear();
		
		_lensInfo = new CommentsSetLens((IDomeo) _application, this);
		_application.getComponentsManager().registerObjectLens(set, _lensInfo);
		_lensInfo.initialize(set);
		info.add(_lensInfo);
	}
	
	public void resetAnnotationSetInfo() {
		_setInfo = null;
		info.clear();
	}
}
