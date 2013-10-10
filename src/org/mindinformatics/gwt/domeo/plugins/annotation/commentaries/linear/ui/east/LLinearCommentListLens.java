package org.mindinformatics.gwt.domeo.plugins.annotation.commentaries.linear.ui.east;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.sets.AnnotationRightsPopup;
import org.mindinformatics.gwt.domeo.client.ui.sets.DiscussionAccessPopup;
import org.mindinformatics.gwt.domeo.client.ui.sets.ILensRefresh;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.annotation.commentaries.linear.model.MLinearCommentAnnotation;
import org.mindinformatics.gwt.framework.component.ui.lenses.ILensComponent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class LLinearCommentListLens extends Composite implements ILensRefresh, ILensComponent {

	interface Binder extends UiBinder<Widget, LLinearCommentListLens> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private IDomeo _domeo;
	private MAnnotationSet _set;
	private MLinearCommentAnnotation _comment;
	private LLinearCommentListLens _this;
	private LinearCommentsSidePanel _parent;
	
	
	@UiField VerticalPanel body;
	@UiField FocusPanel wrapper;
	@UiField Label nameEditableField;
	@UiField SimplePanel itemsNumberLabel;
	@UiField SimplePanel accessPolicy;
	@UiField SimplePanel lockingPolicy; 
	@UiField FlowPanel users; 
	@UiField HorizontalPanel provenanceLabel;
	//@UiField SimplePanel selectionButton;

	public LLinearCommentListLens(IDomeo domeo, final LinearCommentsSidePanel parent, MAnnotationSet set, MLinearCommentAnnotation comment) {
		_domeo = domeo;
		_this = this;
		_set = set;
		_comment = comment;
		_parent = parent;
		
		initWidget(binder.createAndBindUi(this));
		
		
		wrapper.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				parent.displayCommentsSetInfo(_set);
			}
		});
		
		
		/*
		nameEditableField.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				_set.setLabel(event.getValue());
				_set.setHasChanged(true);
				_domeo.refreshAnnotationComponents();
			}
		});
		*/
		
		//selectionButton.add(new Image(Domeo.resources.littleCommentsIcon()));
		
		refreshLens();
	}
	
	public void refresh() {
		//_parent.refresh();
		refreshLens();
	}

	public void refreshLens() {
		try {
			/*
			if(_parent.isAnnotationSetSelected(_set)) {
				selectionButton.setValue(true);
			}
			*/
			nameEditableField.setText(_set.getLabel());
			itemsNumberLabel.clear();
			if(_set.getAnnotations().size()>0) {
				Label itemsNumberText = new Label(_set.getAnnotations().size() +(_set.getAnnotations().size()==1?" item":" items"));
				itemsNumberText.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						//_domeo.displayAnnotationOfSet(_set);
						_parent.displayThread(_comment.getLocalId());
					}
				});
				itemsNumberLabel.add(itemsNumberText);
			} else {
				itemsNumberLabel.add(new HTML("No items"));
			}
						
			// Provenence
			// ----------
			provenanceLabel.clear();
			//provenance.add(new HTML("<span style='font-weight: bold'>By " + _set.getCreator().getName() + "</span>  <span style='padding-left:5px' title='" + annotation.getFormattedCreationDate() + "'>" + elaspedTime((new Date()).getTime() - annotation.getCreatedOn().getTime()) + " ago</span>" ));

			provenanceLabel.add(new HTML("By " + _set.getCreatedBy().getName() + " on " + _set.getFormattedCreationDate()));
			
			// Access control
			// --------------
			final Image accessIcon = new Image();
			ClickHandler accessHandler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					DiscussionAccessPopup popup = new DiscussionAccessPopup(_domeo, _this, _set);
					popup.show(accessIcon.getAbsoluteLeft(), accessIcon.getAbsoluteTop());
					event.stopPropagation();
				}
			};
			
			if(_domeo.getAnnotationAccessManager().isAnnotationSetPublic(_set)) {
				accessIcon.setResource(Domeo.resources.publicLittleIcon());
				addSimpleButton(accessPolicy, accessIcon, accessHandler);
			} else if(_domeo.getAnnotationAccessManager().isAnnotationSetGroups(_set)) {
				accessIcon.setResource(Domeo.resources.friendsLittleIcon());
				addSimpleButton(accessPolicy, accessIcon, accessHandler);
			} else if(_domeo.getAnnotationAccessManager().getAnnotationSetAccess(_set).equals(_domeo.getAgentManager().getUserPerson().getUri())) {
				accessIcon.setResource(Domeo.resources.privateLittleIcon());
				addSimpleButton(accessPolicy, accessIcon, accessHandler);
			}

			// Locking
			// -------
			final Image rightsIcon = new Image();
			ClickHandler lockHandler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					AnnotationRightsPopup popup = new AnnotationRightsPopup(_domeo, _this, _set);
					popup.show(rightsIcon.getAbsoluteLeft(), rightsIcon.getAbsoluteTop());
					event.stopPropagation();
				}
			};
			if(_set.getIsLocked()) {
				rightsIcon.setResource(Domeo.resources.readOnlyLittleIcon());
				addSimpleButton(lockingPolicy, rightsIcon, lockHandler);
			} else {
				rightsIcon.setResource(Domeo.resources.readWriteLittleIcon());
				addSimpleButton(lockingPolicy, rightsIcon, lockHandler);
			}

		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Visualization of the set " + _set.getLabel() + " failed");
		}
	}
	
	public MAnnotationSet getSet() {
		return _set;
	}

	private void addSimpleButton(SimplePanel panel, Image img, ClickHandler clickHandler) {
		panel.clear();
		img.addClickHandler(clickHandler);
		panel.add(img);
	}
}
