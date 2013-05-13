package org.mindinformatics.gwt.domeo.plugins.annotation.comment.ui.viewer;

import java.util.Date;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionDeleteAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionShowAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ATileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MOnlineImage;
import org.mindinformatics.gwt.domeo.model.selectors.MAnnotationSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTargetSelector;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;
import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;
import org.mindinformatics.gwt.framework.component.ICommentsRefreshableComponent;
import org.mindinformatics.gwt.framework.component.preferences.src.BooleanPreference;
import org.mindinformatics.gwt.framework.component.ui.east.ASidePanel;
import org.mindinformatics.gwt.framework.component.ui.east.ASideTab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the standard lens for the annotation type Highlight.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class TCommentViewerTile extends ATileComponent implements ITileComponent {

	interface Binder extends UiBinder<Widget, TCommentViewerTile> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private MCommentAnnotation _annotation;
	
	@UiField VerticalPanel body;
	@UiField HorizontalPanel provenance;
	@UiField FlowPanel content;
	@UiField Label title;
	@UiField Label text;
	@UiField VerticalPanel addCommentSection;
	@UiField TextBox addCommentTitle;
	@UiField TextArea addCommentBody;
	@UiField Button submitButton;
	@UiField Button cancelButton;
	
	public TCommentViewerTile(IDomeo domeo, IAnnotationEditListener listener) {
		super(domeo, listener);
		
		initWidget(binder.createAndBindUi(this));
		
		tileResources.css().ensureInjected();
		
		addCommentSection.setVisible(false);
		addCommentTitle.setVisible(false);
	}
	
	public MAnnotation getAnnotation() {
		return _annotation;
	}
	
	@Override
	public void initializeLens(MAnnotation annotation) {
		_annotation = (MCommentAnnotation) annotation;
		refresh();
	}	
	@Override
	public Widget getTile() {
		return this;
	}
	@Override
	public void refresh() {
		try {
			createProvenanceBar(provenance, _annotation);
			
			/*
			MAnnotationSet set = _domeo.getAnnotationPersistenceManager().getSetByAnnotationId(_annotation.getLocalId());
*/
			if(_annotation.getTitle()!=null && _annotation.getTitle().trim().length()>0) 
				title.setText(_annotation.getTitle());
			else 
				title.setVisible(false);
				
			
			//type.setText("Comment:");
			text.setText(_annotation.getText());
			text.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.alert("Edit or display?");
				}
			});
			
			injectButtons("", content, _annotation);
			
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
			Image editIcon = new Image(resource.editLittleIcon());
			editIcon.setTitle("Edit Item");
			editIcon.setStyleName(ATileComponent.tileResources.css().button());
			//editIcon.addClickHandler(ActionEditAnnotation.getClickHandler(_domeo, this, _listener, getAnnotation()));
			step=1;
			
			Image showIcon = new Image(resource.showLittleIcon());
			showIcon.setTitle("Show Item in Context");
			showIcon.setStyleName(ATileComponent.tileResources.css().button());
			showIcon.addClickHandler(ActionShowAnnotation.getClickHandler(_domeo, this, getAnnotation()));
			step=2;
			
			Image addCommentIcon = new Image(resource.addCommentIcon());
			addCommentIcon.setTitle("Comment on item");
			addCommentIcon.setStyleName(ATileComponent.tileResources.css().button());
			addCommentIcon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					showCommentForm();
				}
			});
			step=3;
			
			Image commentBranchIcon = new Image(resource.splitCommentIcon());
			commentBranchIcon.setTitle("Comment on item");
			commentBranchIcon.setStyleName(ATileComponent.tileResources.css().button());
			commentBranchIcon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					showCommentBranchForm();
				}
			});
			step=4;
			
			//final CommentsViewerPanel viewer = _viewer;
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
					
					TCommentViewerTile c = new TCommentViewerTile(_domeo, _listener);
					TreeItem i = new TreeItem(c.getTile());
					i.setState(true);
					c.initializeLens(annotation);

					hideCommentForm();
					//viewer.refreshThreadsTree();
				}
			});
			
			cancelButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					hideCommentForm();
				}
			});
	
			Image deleteIcon = new Image(resource.deleteLittleIcon());
			deleteIcon.setTitle("Delete Item");
			deleteIcon.setStyleName(ATileComponent.tileResources.css().button());
			deleteIcon.addClickHandler(ActionDeleteAnnotation.getClickHandler(_domeo, this, getAnnotation()));
			step=5;
			
			_domeo.getLogger().debug(this, ""+annotation.getCreator());
			_domeo.getLogger().debug(this, annotation.getCreator().getUri());
			
			// TODO move to an abstract tile class
			if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_PROVENANCE)).getValue()) {
				if(annotation.getCreator().getUri().equals(_domeo.getAgentManager().getUserPerson().getUri())) {
					if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_USER_PROVENANCE)).getValue()) {
						provenance.clear();
						step=6;
						// TODO Externalize the icon management to the plugins
						if(SelectorUtils.isOnMultipleTargets(annotation.getSelectors())) { 
							Image ic = new Image(Domeo.resources.multipleLittleIcon());
							ic.setTitle("Annotation on multiple targets");
							provenance.add(ic);
							provenance.setCellWidth(ic, "18px");
						} else if(annotation.getSelector()!=null && annotation.getSelector().getTarget() instanceof MOnlineImage) {
							Image ic = new Image(Domeo.resources.littleImageIcon());
							ic.setTitle("Annotation on image");
							provenance.add(ic);
							provenance.setCellWidth(ic, "18px");
						} else {
							if(SelectorUtils.isOnAnnotation(annotation.getSelectors())) {
								if(((MCommentAnnotation)annotation).getTitle()!=null && ((MCommentAnnotation)annotation).getTitle().trim().length()>0) {
									Image ic = new Image(Domeo.resources.splitCommentIcon());
									ic.setTitle("Discussion branch");
									provenance.add(ic);
									provenance.setCellWidth(ic, "18px");
								} else {
									Image ic = new Image(Domeo.resources.littleCommentIcon());
									ic.setTitle("Discussion comment");
									provenance.add(ic);
									provenance.setCellWidth(ic, "18px");
								}
							} else if(SelectorUtils.isOnResourceTarget(annotation.getSelectors())) {
								Image ic = new Image(Domeo.resources.littleCommentsIcon());
								ic.setTitle("Annotation on annotation");
								provenance.add(ic);
								provenance.setCellWidth(ic, "18px");
							} else {
								Image ic = new Image(Domeo.resources.littleTextIcon());
								ic.setTitle("Annotation on text");
								provenance.add(ic);
								provenance.setCellWidth(ic, "18px");
							}
						}
						step=7;
						
						provenance.add(new HTML("<span style='font-weight: bold; font-size: 12px; color: #696969'>By Me</span>  <span style='padding-left:5px' title='" + annotation.getFormattedCreationDate() + "'>" + elaspedTime((new Date()).getTime() - annotation.getCreatedOn().getTime()) + " ago</span>" ));
						if(!(annotation.getSelector() instanceof MTargetSelector) && !(annotation.getSelector() instanceof MAnnotationSelector)) {
							provenance.add(showIcon);
							provenance.setCellWidth(showIcon, "22px");
							provenance.add(editIcon);
							provenance.setCellWidth(editIcon, "22px");
						}
						provenance.add(addCommentIcon);
						provenance.add(commentBranchIcon);
						provenance.add(deleteIcon);
						provenance.setCellHorizontalAlignment(deleteIcon, HasHorizontalAlignment.ALIGN_LEFT);
						provenance.setCellWidth(deleteIcon, "22px");
					} else {
						provenance.setVisible(false);
					}
				} else {
					provenance.clear();
					step=8;
					if(SelectorUtils.isOnMultipleTargets(annotation.getSelectors())) { 
						Image ic = new Image(Domeo.resources.multipleLittleIcon());
						ic.setTitle("Annotation on multiple targets");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					} else if(annotation.getSelector()!=null && annotation.getSelector().getTarget() instanceof MOnlineImage) {
						Image ic = new Image(Domeo.resources.littleImageIcon());
						ic.setTitle("Annotation on image");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					} else {
						Image ic = new Image(Domeo.resources.littleTextIcon());
						ic.setTitle("Annotation on text");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					}
					
					step=9;
					provenance.add(new HTML("<span style='font-weight: bold; font-size: 12px; color: #696969'>By " + annotation.getCreator().getName() + "</span>  <span style='padding-left:5px' title='" + annotation.getFormattedCreationDate() + "'>" + elaspedTime((new Date()).getTime() - annotation.getCreatedOn().getTime()) + " ago</span>" ));
					 
					provenance.add(showIcon);
					provenance.add(editIcon);
					provenance.add(commentBranchIcon);
					provenance.add(addCommentIcon);
					provenance.add(deleteIcon);
				}
			} else {
				provenance.setVisible(false);
			}
		} catch (Exception e) {
			_domeo.getLogger().exception(this, "Provenance bar generation exception @" + step + " " + e.getMessage());
		}
	}
}
